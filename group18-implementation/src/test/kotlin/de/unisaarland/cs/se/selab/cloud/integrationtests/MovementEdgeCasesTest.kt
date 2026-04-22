package de.unisaarland.cs.se.selab.cloud.integrationtests

import de.unisaarland.cs.se.selab.incident.CloudCreationIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Direction
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import java.io.PrintWriter
import kotlin.test.Test
import kotlin.test.assertFalse

private val field1 = Field(
    id = 1,
    coordinate = Coordinate(0, 0),
    farm = 1,
    direction = Direction.ANGLE_0,
    moistureCapacity = 120,
    possiblePlants = emptyList()
)

private val field2 = Field(
    id = 2,
    coordinate = Coordinate(0, 2),
    farm = 1,
    direction = Direction.ANGLE_0,
    moistureCapacity = 10000,
    possiblePlants = emptyList()
)
private val road = Tile(
    id = 3,
    coordinate = Coordinate(0, 4),
    categoryType = TileCategory.ROAD,
    farm = null,
    direction = null,
    shed = false
)

private val tileManager = TileManager()
private val tiles = mutableMapOf(
    1 to field1 as Tile,
    2 to field2 as Tile,
    3 to road
)

private val cloud1 = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 2,
    duration = -1,
    location = 1,
    amount = 1500
)
private val cloud2 = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 1,
    duration = -1,
    location = 2,
    amount = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CLOUD_AMOUNT_MAX_VALUE
)
private val cloud3 = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 3,
    duration = -1,
    location = 5,
    amount = 10000
)

class MovementEdgeCasesTest {
    @BeforeEach
    fun setup() {
        tileManager.setTiles(tiles)
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    @Test
    fun `cloud moves to empty tile tile`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, mutableListOf(cloud1))
        cloudHandler.run()

        assertEquals(
            cloudHandler.clouds.first().location,
            cloud1.location,
            "Cloud ${cloud1.id} must not move to an empty tile"
        )
    }

    @Test
    fun `cloud duration does not go below -1`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, mutableListOf(cloud1))
        cloudHandler.run()

        assertEquals(
            cloudHandler.clouds.first().duration,
            -1,
            "Cloud ${cloud1.id}'s duration must remain the same if equals -1"
        )
    }

    @Test
    fun `clouds merge with amount causing int overflow`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, mutableListOf(cloud1, cloud2))
        cloudHandler.run()

        assertEquals(
            cloudHandler.clouds.first().amount,
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CLOUD_AMOUNT_MAX_VALUE,
            "Cloud ${cloud1.id}'s overflows and gets max int number as a value"
        )
    }

    @Test
    fun `cloud incident creates the first cloud`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, mutableListOf())

        val cloudCreation = CloudCreationIncident(
            id = 1,
            tick = 0,
            location = 3,
            amount = 4000,
            duration = 3,
            cloudHandler = cloudHandler,
            radius = 0
        )

        cloudCreation.perform(0, YearTick.APR2)

        assertEquals(cloudHandler.highestID, 1, "first cloud is being created")
    }

    @Test
    fun `cloud incident creates the first clouds with merge`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, mutableListOf())

        val cloudCreation = CloudCreationIncident(
            id = 1,
            tick = 0,
            location = 3,
            amount = 4000,
            duration = 3,
            cloudHandler = cloudHandler,
            radius = 0
        )

        cloudCreation.perform(0, YearTick.APR2)
        cloudCreation.perform(0, YearTick.APR2)

        assertEquals(cloudHandler.highestID, 3, "first cloud is being created")
    }

    @Test
    fun `cloud phase does not happen after incident`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, mutableListOf())

        val cloudCreation = CloudCreationIncident(
            id = 1,
            tick = 0,
            location = 2,
            amount = 6000,
            duration = 1,
            cloudHandler = cloudHandler,
            radius = 0
        )
        field2.currentMoisture = 500

        cloudCreation.perform(0, YearTick.APR2)

        assertFalse(
            cloudHandler.clouds.isEmpty(),
            "cloud must not dissipate in the same tick as the incident"
        )
        assertEquals(
            cloudHandler.clouds.first().location,
            2,
            "cloud must not move in the same tick as the incident"
        )
        assertEquals(
            cloudHandler.clouds.first().amount,
            6000,
            "cloud must not rain in the same tick as the incident"
        )
    }

    @Test
    fun `no clouds created during incident`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, mutableListOf())

        val cloudCreation = CloudCreationIncident(
            id = 1,
            tick = 0,
            location = 5,
            amount = 6000,
            duration = 1,
            cloudHandler = cloudHandler,
            radius = 0
        )

        cloudCreation.perform(0, YearTick.APR2)

        assertTrue(
            cloudHandler.clouds.isEmpty(),
            "no tiles in the current radius"
        )
    }

    @Test
    fun `cloud stays on an empty tile`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, mutableListOf(cloud3))
        cloudHandler.run()

        assertEquals(
            cloudHandler.clouds.first(),
            cloud3,
            "Cloud ${cloud3.id} must remain unchanged if on an empty tile"
        )
    }
}
