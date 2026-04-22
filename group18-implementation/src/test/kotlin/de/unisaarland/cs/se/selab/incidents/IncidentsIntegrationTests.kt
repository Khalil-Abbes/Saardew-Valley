package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.incident.CityExpansionIncident
import de.unisaarland.cs.se.selab.incident.Incident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.Simulation
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.to

private val tile1_village = Tile(
    id = 1,
    coordinate = Coordinate(0, 0),
    categoryType = TileCategory.VILLAGE,
    shed = false,
    farm = null,
    direction = null,
)

private val tile2_field = Field(
    id = 2,
    coordinate = Coordinate(2, 0),
    farm = 1,
    direction = null,
    moistureCapacity = 450,
    possiblePlants = listOf(FieldPlantType.POTATO)
)

private val tile3_road = Tile(
    id = 3,
    coordinate = Coordinate(3, 1),
    farm = 1,
    direction = null,
    shed = false,
    categoryType = TileCategory.ROAD,
)

private val tile4_forrest = Tile(
    id = 4,
    coordinate = Coordinate(0, -2),
    farm = 1,
    direction = null,
    shed = false,
    categoryType = TileCategory.FOREST,
)

private val tile5_road = Tile(
    id = 5,
    coordinate = Coordinate(1, 1),
    farm = 1,
    direction = null,
    shed = false,
    categoryType = TileCategory.ROAD,
)

private val cloud = Cloud(
    id = 1,
    duration = 4,
    location = 2,
    amount = 600
)

private val tileManager = TileManager()
private val cloudHandler: CloudHandler = CloudHandler(tileManager, mutableListOf(cloud))
private val incident_city = CityExpansionIncident(
    id = 2,
    tick = 1,
    farms = emptyList(),
    tileManager = tileManager,
    location = 2,
    cloudHandler = cloudHandler,
)

private val incident_city2 = CityExpansionIncident(
    id = 3,
    tick = 1,
    farms = emptyList(),
    tileManager = tileManager,
    location = 3,
    cloudHandler = cloudHandler
)

class IncidentsIntegrationTests {

    @BeforeEach
    fun setup() {
        tileManager.setTiles(
            mutableMapOf(
                1 to tile1_village,
                2 to tile2_field,
                3 to tile3_road,
                4 to tile4_forrest,
                5 to tile5_road
            )
        )
        Logger.concreteLogger = DebugLogger(
            PrintWriter(".\\src\\test\\kotlin\\de\\unisaarland\\cs\\se\\selab\\incidents\\logIncident.txt")
        )
    }

    @Test
    fun `test stucked cloud`() {
        incident_city.perform(1, YearTick.APR2)

        assertEquals(
            tileManager.getTileById(1)!!
                .categoryType,
            TileCategory.VILLAGE
        )
        assertEquals(
            tileManager.getTileById(2)!!
                .categoryType,
            TileCategory.VILLAGE
        )
        assertFalse(cloudHandler.clouds.contains(cloud))
    }

    @Test
    fun `test multiple village expansions`() {
        val farms = emptyMap<Int, Farm>()
        val incidents = listOf<Incident>(incident_city2, incident_city)

        val startTick = 0
        val startYearTick = YearTick.JUL1
        val maxTick = 4

        val sim = Simulation(
            tileManager = tileManager,
            cloudHandler = cloudHandler,
            farms = farms,
            incidents = incidents,
            currentTick = startTick,
            currentYearTick = startYearTick,
            maxTick = maxTick
        )
        sim.run()
    }

    @Test
    fun `test city expansion empty tile`() {
        val expansion = CityExpansionIncident(
            id = 3,
            tick = 0,
            farms = emptyList(),
            tileManager = tileManager,
            location = 7,
            cloudHandler = cloudHandler
        )

        val villages = tileManager.idToTile.values.filter {
            it.categoryType == TileCategory.VILLAGE
        }

        expansion.perform(0, YearTick.APR1)
        val villagesNew = tileManager.idToTile.values.filter {
            it.categoryType == TileCategory.VILLAGE
        }

        assertEquals(villages, villagesNew)
    }

    @Test
    fun `test city expansion not allowed tile`() {
        val expansion = CityExpansionIncident(
            id = 1,
            tick = 1,
            farms = emptyList(),
            tileManager = tileManager,
            location = 4,
            cloudHandler = cloudHandler
        )

        val villages = tileManager.idToTile.values.filter {
            it.categoryType == TileCategory.VILLAGE
        }

        expansion.perform(1, YearTick.APR1)
        val villagesNew = tileManager.idToTile.values.filter {
            it.categoryType == TileCategory.VILLAGE
        }

        assertEquals(villages, villagesNew)
    }

    @Test
    fun `test city expansion road tile`() {
        val expansion = CityExpansionIncident(
            id = 1,
            tick = 1,
            farms = emptyList(),
            tileManager = tileManager,
            location = 5,
            cloudHandler = cloudHandler
        )

        expansion.perform(1, YearTick.APR1)
        assertEquals(
            tileManager.getTileById(5)!!
                .categoryType,
            TileCategory.VILLAGE
        )
    }
}
