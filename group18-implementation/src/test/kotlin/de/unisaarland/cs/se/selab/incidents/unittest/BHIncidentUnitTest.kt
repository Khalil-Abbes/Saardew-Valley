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
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.PrintWriter
import kotlin.test.assertEquals

class BHIncidentUnitTest {

    private lateinit var tm: TileManager

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        tm = mock()
    }

    @Disabled("")
    @Test
    fun `bee happy increases by effect only for insect-pollinated plants that are blooming`() {
        // Arrange
        val m = buildTheMap()
        stubTileManager(tm, m)

        val incident = BeeHappyIncident(
            id = 1,
            tick = 100,
            tileManager = tm,
            location = 200, // meadow1
            radius = 1,
            effect = 20
        )

        // Act
        incident.perform(currentTick = 100, currentYearTick = YearTick.MAY1)

        // Neutralize environment so only incident effect is measured
        neutralizeEnvironment(m)

        m.field.updateHarvestEstimate(100, YearTick.MAY1)
        m.apple.updateHarvestEstimate(100, YearTick.MAY1)

        // Assert: +20% once per impacted tile (dedup across two meadows)
        assertEquals(1200, m.field.currentHarvestEstimate)
        assertEquals(1200, m.apple.currentHarvestEstimate)
    }

    // helpers

    private data class TheMap(
        val meadow1: Tile,
        val meadow2: Tile,
        val field: Field, // POTATO (INSECTS), relative bloom after sowing 3, dur 1
        val apple: Plantation // APPLE (INSECTS), fixed bloom APR2...MAY1
    )

    private fun buildTheMap(): TheMap {
        val meadow1 = Tile(
            id = 200,
            coordinate = Coordinate(0, 0),
            categoryType = TileCategory.MEADOW,
            shed = false,
            farm = null,
            direction = null
        )
        val meadow2 = Tile(
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
            // sow 3 ticks before currentTick=100 -> bloom at ticks 100..101
            plant!!.setProgress(ActionType.SOWING, 97, YearTick.MAR2)
        }

        val apple = Plantation(
            20,
            Coordinate(2, 2),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.APPLE)
        ).apply {
            currentHarvestEstimate = 1000
        }

        return TheMap(meadow1, meadow2, field, apple)
    }

    private fun stubTileManager(tm: TileManager, m: TheMap) {
        whenever(tm.getTileById(200)).thenReturn(m.meadow1)

        whenever(tm.getAllNeighborTilesInRadius(200, 1)).thenReturn(
            listOf(m.field, m.apple, m.meadow1, m.meadow2)
        )

        whenever(
            tm.getAllNeighborTilesInRadius(
                m.meadow1,
                1
            )
        ).thenReturn(listOf(m.field, m.apple, m.meadow1, m.meadow2))
        whenever(
            tm.getAllNeighborTilesInRadius(
                m.meadow2,
                1
            )
        ).thenReturn(listOf(m.field, m.apple, m.meadow1, m.meadow2))
    }

    private fun neutralizeEnvironment(m: TheMap) {
        m.field.sunlightThisTick = m.field.plant!!.type.sunlightComfort
        m.field.currentMoisture = m.field.plant!!.type.moistureRequired

        m.apple.sunlightThisTick = m.apple.plant.type.sunlightComfort
        m.apple.currentMoisture = m.apple.plant.type.moistureRequired
    }
}
