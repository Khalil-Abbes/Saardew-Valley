package de.unisaarland.cs.se.selab.incidents.unittest

import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.farm.SowingPlan
import de.unisaarland.cs.se.selab.incident.CityExpansionIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CityExpansionUnitTest {

    private lateinit var tm: TileManager
    private lateinit var cloudHandler: CloudHandler

    private lateinit var field40: Field
    private lateinit var villageNeighbor: Tile
    private lateinit var farm: Farm
    private lateinit var machineOnField: Machine

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))

        tm = mock()
        cloudHandler = mock()

        // FIELD to be transformed
        field40 = Field(
            40,
            Coordinate(0, 0),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.WHEAT)
        ).apply {
            plant = FieldPlant(FieldPlantType.WHEAT)
        }

        // An existing VILLAGE tile adjoining
        villageNeighbor = Tile(
            id = 99,
            coordinate = Coordinate(0, 2),
            categoryType = TileCategory.VILLAGE,
            shed = false,
            farm = null,
            direction = null
        )

        val shed = Tile(
            id = 12,
            coordinate = Coordinate(-1, -1),
            categoryType = TileCategory.FARMSTEAD,
            shed = true,
            farm = 1,
            direction = null
        )

        machineOnField = Machine(
            id = 7,
            name = "M",
            actions = emptyList(),
            plants = emptyList(),
            duration = 1,
            location = field40,
            tileManager = tm,
            farm = 1
        )

        farm = Farm(
            id = 1,
            name = "F1",
            farmsteads = listOf(shed),
            fields = listOf(field40),
            plantations = emptyList(),
            machines = listOf(machineOnField),
            sowingPlans = emptyList<SowingPlan>().toMutableList()
        )

        whenever(tm.getTileById(40)).thenReturn(field40)
        whenever(
            tm.getAllNeighborTilesInRadius(40, 1)
        ).thenReturn(listOf(villageNeighbor))
        whenever(
            tm.getAllNeighborTilesInRadius(field40, 1)
        ).thenReturn(listOf(villageNeighbor))
    }

    @Test
    fun `city expansion converts field to village, removes field from farm, machine stays on the new village tile`() {
        val before = tm.getTileById(40)!!
        assertEquals(TileCategory.FIELD, before.categoryType)
        assertTrue(farm.fields.any { it.id == 40 })

        val incident = CityExpansionIncident(
            id = 3,
            tick = 200,
            farms = listOf(farm),
            tileManager = tm,
            location = 40,
            cloudHandler = cloudHandler
        )

        incident.perform(currentTick = 200, currentYearTick = YearTick.OCT1)

        whenever(tm.getTileById(40)).thenReturn(villageNeighbor)

        val after = tm.getTileById(40)!!
        assertEquals(TileCategory.VILLAGE, after.categoryType)

        assertFalse(farm.fields.any { it.id == 40 })

        assertEquals(40, machineOnField.location.id)
        assertEquals(TileCategory.VILLAGE, machineOnField.location.categoryType)
    }
}
