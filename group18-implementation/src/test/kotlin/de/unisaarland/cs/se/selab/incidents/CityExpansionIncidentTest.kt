package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.farm.SowingPlan
import de.unisaarland.cs.se.selab.incident.CityExpansionIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.parser.TileFactory
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
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CityExpansionIncidentTest {

    private lateinit var tm: TileManager
    private lateinit var field40: Field
    private lateinit var villageNeighbor: Tile
    private lateinit var farm: Farm
    private lateinit var machineOnField: Machine
    private lateinit var cloudHandler: CloudHandler

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))

        val tiles = mutableMapOf<Int, Tile>()

        // FIELD to be transformed
        field40 = Field(
            40, Coordinate(0, 0), farm = 1,
            direction = null, moistureCapacity = 400, possiblePlants = listOf(FieldPlantType.WHEAT)
        )
        field40.plant = FieldPlant(FieldPlantType.WHEAT)
        tiles[40] = field40

        // Existing VILLAGE adjacency (parser would validate this precondition)
        villageNeighbor = TileFactory.createVillageTile(99, Coordinate(0, 2))
        tiles[99] = villageNeighbor

        // A farmstead for machine (so we can prove machine stays if on the tile)
        val shed = TileFactory.createFarmsteadTile(
            12,
            Coordinate(-1, -1),
            shed = true,
            farm = 1,
            direction = null
        )
        tiles[12] = shed

        tm = TileManager()
        tm.setTiles(tiles)

        cloudHandler = CloudHandler(tm, mutableListOf())

        // Minimal farm containing that field
        farm = Farm(
            id = 1,
            name = "F1",
            farmsteads = listOf(shed),
            fields = listOf(field40),
            plantations = emptyList(),
            machines = emptyList(),
            sowingPlans = emptyList<SowingPlan>().toMutableList()
        )

        // Machine currently located on the field to be converted; it must "simply stay there" after transform
        machineOnField = Machine(
            id = 7, name = "M", actions = emptyList(), plants = emptyList(),
            duration = 1, location = field40, tileManager = tm, farm = 1
        )
        // Add it to farm (so CityExpansionIncident sees it)
        farm.machineToShed[machineOnField] = shed
        // Reflect as part of farm.machines (Farm exposes getter of internal list)
        // We’ll construct a new Farm with this machine included (easiest)
        val farm2 = Farm(
            id = 1,
            name = "F1",
            farmsteads = listOf(shed),
            fields = listOf(field40),
            plantations = emptyList(),
            machines = listOf(machineOnField),
            sowingPlans = emptyList<SowingPlan>().toMutableList()
        )
        farm = farm2
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

        val after = tm.getTileById(40)!!
        assertEquals(TileCategory.VILLAGE, after.categoryType)

        // Farm no longer owns that field
        assertFalse(farm.fields.any { it.id == 40 })

        // If a machine is located there, it can simply stay -> location re-bound to the new tile
        assertEquals(40, machineOnField.location.id)
        assertEquals(TileCategory.VILLAGE, machineOnField.location.categoryType)
    }
}
