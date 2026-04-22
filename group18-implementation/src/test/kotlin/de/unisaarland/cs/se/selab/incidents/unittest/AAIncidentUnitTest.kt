package de.unisaarland.cs.se.selab.incidents.unittest

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.incident.AnimalAttackIncident
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
import kotlin.test.assertTrue

class AAIncidentUnitTest {

    private lateinit var tm: TileManager

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        tm = mock() // mock instead of real TileManager
    }

    @Test
    fun `animal attack halves field and grape harvest, apple gets -10pct and mowing reset for this and next tick`() {
        // Arrange
        val f = buildTheMap()
        stubTileManager(tm, f)

        val incident = AnimalAttackIncident(
            id = 1,
            tick = 50,
            tileManager = tm,
            location = 300,
            radius = 1
        )

        // Act
        incident.perform(currentTick = 50, currentYearTick = YearTick.MAY1)

        neutralizeEnvironment(f)

        f.field.updateHarvestEstimate(50, YearTick.MAY1)
        f.grape.updateHarvestEstimate(50, YearTick.MAY1)
        f.apple.updateHarvestEstimate(50, YearTick.MAY1)

        // Assert
        assertEquals(500, f.field.currentHarvestEstimate) // halved
        assertEquals(500, f.grape.currentHarvestEstimate) // halved
        assertEquals(900, f.apple.currentHarvestEstimate) // -10%
        assertTrue(f.apple.mowingDoneTicks.contains(50))
        assertTrue(f.apple.mowingDoneTicks.contains(51))
    }

    // helpers

    private data class TheMap(
        val forest1: Tile,
        val forest2: Tile,
        val field: Field,
        val grape: Plantation,
        val apple: Plantation
    )

    private fun buildTheMap(): TheMap {
        val forest1 = Tile(
            id = 300,
            coordinate = Coordinate(0, 0),
            categoryType = TileCategory.FOREST,
            shed = false,
            farm = null,
            direction = null
        )
        val forest2 = Tile(
            id = 301,
            coordinate = Coordinate(0, -2),
            categoryType = TileCategory.FOREST,
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
            possiblePlants = listOf(FieldPlantType.WHEAT)
        ).apply {
            plant = FieldPlant(FieldPlantType.WHEAT)
            currentHarvestEstimate = 1000
            plant!!.setProgress(ActionType.SOWING, 1, YearTick.OCT1)
        }

        val grape = Plantation(
            20,
            Coordinate(2, 0),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.GRAPE)
        ).apply { currentHarvestEstimate = 1000 }

        val apple = Plantation(
            21,
            Coordinate(-2, 0),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.APPLE)
        ).apply { currentHarvestEstimate = 1000 }

        return TheMap(forest1, forest2, field, grape, apple)
    }

    private fun stubTileManager(tm: TileManager, f: TheMap) {
        whenever(tm.getTileById(300)).thenReturn(f.forest1)
        whenever(tm.getForestsInRadius(300, 1)).thenReturn(
            listOf(
                f.forest1,
                f.forest2
            )
        )
        whenever(tm.getAllNeighborTilesInRadius(f.forest1, 1)).thenReturn(
            listOf(
                f.field,
                f.grape,
                f.apple
            )
        )
        whenever(tm.getAllNeighborTilesInRadius(f.forest2, 1)).thenReturn(
            listOf(
                f.field,
                f.grape,
                f.apple
            )
        )
        // ID-based overload
        whenever(tm.getAllNeighborTilesInRadius(300, 1)).thenReturn(
            listOf(
                f.field,
                f.grape,
                f.apple
            )
        )
    }

    private fun neutralizeEnvironment(f: TheMap) {
        f.field.sunlightThisTick = f.field.plant!!.type.sunlightComfort
        f.field.currentMoisture = f.field.plant!!.type.moistureRequired

        f.grape.sunlightThisTick = f.grape.plant.type.sunlightComfort
        f.grape.currentMoisture = f.grape.plant.type.moistureRequired

        f.apple.sunlightThisTick = f.apple.plant.type.sunlightComfort
        f.apple.currentMoisture = f.apple.plant.type.moistureRequired
    }
}
