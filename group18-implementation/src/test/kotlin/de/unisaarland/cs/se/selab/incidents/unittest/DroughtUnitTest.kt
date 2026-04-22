package de.unisaarland.cs.se.selab.incidents.unittest

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.incident.DroughtIncident
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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DroughtUnitTest {

    private lateinit var tm: TileManager

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        tm = mock()
    }

    @Test
    fun `drought sets moisture to 0, kills plants, disables plantation, field fallow starts next tick`() {
        val field = Field(
            10,
            Coordinate(0, 0),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.WHEAT)
        ).apply {
            plant = FieldPlant(FieldPlantType.WHEAT)
            plant!!.setProgress(ActionType.SOWING, 1, YearTick.OCT1)
            currentHarvestEstimate = 1000
        }

        val plantation = Plantation(
            20,
            Coordinate(2, 0),
            farm = 1,
            direction = null,
            moistureCapacity = 300,
            plant = PlantationPlant(PlantationPlantType.CHERRY)
        ).apply {
            currentHarvestEstimate = 1000
        }

        whenever(tm.getTileById(10)).thenReturn(field)
        whenever(tm.getAllNeighborTilesInRadius(10, 1)).thenReturn(listOf(field, plantation))
        whenever(tm.getAllNeighborTilesInRadius(field, 1)).thenReturn(listOf(field, plantation))

        val incident = DroughtIncident(
            id = 99,
            tick = 77,
            tileManager = tm,
            location = 10,
            radius = 1
        )

        incident.perform(currentTick = 77, currentYearTick = YearTick.AUG1)
        field.updateHarvestEstimate(currentTick = 77, currentYearTick = YearTick.AUG1)
        plantation.updateHarvestEstimate(currentTick = 77, currentYearTick = YearTick.AUG1)

        assertEquals(0, field.currentMoisture)
        assertEquals(0, field.currentHarvestEstimate)
        assertEquals(77, field.lastFallow)

        assertEquals(0, plantation.currentMoisture)
        assertEquals(0, plantation.currentHarvestEstimate)
        assertNotNull(plantation.permanentDisabled)
    }
}
