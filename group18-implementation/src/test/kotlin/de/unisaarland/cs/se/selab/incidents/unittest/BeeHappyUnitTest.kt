package de.unisaarland.cs.se.selab.incidents.unittest

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.incident.BeeHappyIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.PrintWriter
import kotlin.test.assertEquals

class BeeHappyUnitTest {

    private lateinit var tm: TileManager

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        tm = mock() // mock instead of real TileManager
    }

    @Test
    fun `bee happy increases by effect only for insect-pollinated plants that are blooming`() {
        // Arrange: build tiles
        val meadowA = Tile(
            id = 200,
            coordinate = Coordinate(0, 0),
            categoryType = TileCategory.MEADOW,
            shed = false,
            farm = null,
            direction = null
        )
        val meadowB = Tile(
            id = 201,
            coordinate = Coordinate(2, 0),
            categoryType = TileCategory.MEADOW,
            shed = false,
            farm = null,
            direction = null
        )

        val field = Field(
            10,
            Coordinate(0, 2),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.POTATO)
        ).apply {
            plant = FieldPlant(FieldPlantType.POTATO)
            currentHarvestEstimate = 1000
            // sow at tick 97 so bloom (sow+3, dur 1) is exactly tick 100
            plant!!.setProgress(ActionType.SOWING, 97, YearTick.MAR2)
        }

        val apple = Plantation(
            20,
            Coordinate(2, 2),
            farm = 1,
            direction = null,
            moistureCapacity = 300,
            plant = PlantationPlant(PlantationPlantType.APPLE)
        ).apply { currentHarvestEstimate = 1000 }

        whenever(tm.getTileById(200)).thenReturn(meadowA)

        whenever(tm.getAllNeighborTilesInRadius(200, 1)).thenReturn(listOf(meadowA, meadowB))

        whenever(tm.getAllNeighborTilesInRadius(meadowA, 2)).thenReturn(listOf(field, apple))
        whenever(tm.getAllNeighborTilesInRadius(meadowB, 2)).thenReturn(listOf(field, apple))

        val incident = BeeHappyIncident(
            id = 1,
            tick = 100,
            tileManager = tm,
            location = 200,
            radius = 1,
            effect = 20
        )

        incident.perform(currentTick = 100, currentYearTick = YearTick.MAY1)

        field.sunlightThisTick = field.plant!!.type.sunlightComfort
        field.currentMoisture = field.plant!!.type.moistureRequired
        apple.sunlightThisTick = apple.plant.type.sunlightComfort
        apple.currentMoisture = apple.plant.type.moistureRequired

        field.updateHarvestEstimate(100, YearTick.MAY1)
        apple.updateHarvestEstimate(100, YearTick.MAY1)
        assertEquals(1000, field.currentHarvestEstimate)
        assertEquals(1000, apple.currentHarvestEstimate)
    }
}
