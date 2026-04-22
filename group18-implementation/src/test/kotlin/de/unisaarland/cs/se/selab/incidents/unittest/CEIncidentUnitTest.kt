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
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CEIncidentUnitTest {

    private lateinit var tm: TileManager
    private lateinit var cloudHandler: CloudHandler

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        tm = mock()
        cloudHandler = mock()
    }

    @Disabled("")
    @Test
    fun `city expansion converts field to village, removes field from farm, machine stays on the new village tile`() {
        val m = buildTheMap()

        val farm = Farm(
            id = 1,
            name = "F1",
            farmsteads = listOf(m.shed),
            fields = listOf(m.field40),
            plantations = emptyList(),
            machines = listOf(m.machineOnField),
            sowingPlans = mutableListOf<SowingPlan>()
        ).apply {
            machineToShed[m.machineOnField] = m.shed
        }

        stubTileManager(tm, m)

        val incident = CityExpansionIncident(
            id = 3,
            tick = 200,
            farms = listOf(farm),
            tileManager = tm,
            location = m.field40.id,
            cloudHandler = cloudHandler
        )

        val before = m.field40
        assertEquals(TileCategory.FIELD, before.categoryType)
        assertTrue(farm.fields.any { it.id == m.field40.id })

        // Act
        incident.perform(currentTick = 200, currentYearTick = YearTick.OCT1)

        // Assert
        assertEquals(TileCategory.VILLAGE, m.field40.categoryType)
        assertFalse(farm.fields.any { it.id == m.field40.id })
        assertEquals(m.field40.id, m.machineOnField.location.id)
        assertEquals(TileCategory.VILLAGE, m.machineOnField.location.categoryType)
    }

    //  helpers

    private data class TheMap(
        val field40: Field,
        val villageNeighbor: Tile,
        val shed: Tile,
        val machineOnField: Machine
    )

    private fun buildTheMap(): TheMap {
        val field40 = Field(
            40,
            Coordinate(0, 0),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.WHEAT)
        ).apply {
            plant = FieldPlant(FieldPlantType.WHEAT)
        }

        val villageNeighbor = Tile(
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

        val machineOnField = Machine(
            id = 7,
            name = "M",
            actions = emptyList(),
            plants = emptyList(),
            duration = 1,
            location = field40,
            tileManager = tm,
            farm = 1
        )

        return TheMap(field40, villageNeighbor, shed, machineOnField)
    }

    private fun stubTileManager(tm: TileManager, m: TheMap) {
        // the incident reads target tile and neighbors
        whenever(tm.getTileById(m.field40.id)).thenReturn(m.field40)
        whenever(
            tm.getAllNeighborTilesInRadius(
                m.field40,
                1
            )
        ).thenReturn(listOf(m.villageNeighbor))
        whenever(
            tm.getAllNeighborTilesInRadius(
                m.field40.id,
                1
            )
        ).thenReturn(listOf(m.villageNeighbor))

        // when updateTile is called with a VILLAGE replacement, mutate our instance to mirror conversion
        doAnswer {
            val newTile = (it.arguments[0] ?: error("null assertion message")) as Tile
            if (newTile.id == m.field40.id) {
                m.field40.categoryType = newTile.categoryType
                m.field40.farm = newTile.farm
            }
            null
        }.whenever(tm).updateTile(any())

        whenever(
            tm.getAllNeighborTilesInRadius(
                any<Tile>(),
                any()
            )
        ).thenReturn(emptyList())
        whenever(
            tm.getAllNeighborTilesInRadius(
                any<Int>(),
                any()
            )
        ).thenReturn(emptyList())
    }
}
