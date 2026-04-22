package de.unisaarland.cs.se.selab.cloud.integrationtests

import de.unisaarland.cs.se.selab.incident.CloudCreationIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter

private val road = Tile(
    id = 1,
    coordinate = Coordinate(0, 0),
    categoryType = TileCategory.VILLAGE,
    shed = false,
    farm = null,
    direction = null
)

private val forest = Tile(
    id = 2,
    coordinate = Coordinate(0, 2),
    categoryType = TileCategory.FOREST,
    shed = false,
    farm = null,
    direction = null
)

private val village = Tile(
    id = 3,
    coordinate = Coordinate(0, 4),
    categoryType = TileCategory.VILLAGE,
    shed = false,
    farm = null,
    direction = null
)

private val cloudRoad =
    _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(id = 1, duration = -1, location = 2, amount = 1000)
private val tileManagerCreate = TileManager()
private val tilesCreate = mutableMapOf(
    1 to road,
    2 to forest,
    3 to village
)

class CloudCreationTests {
    @BeforeEach
    fun setup() {
        tileManagerCreate.setTiles(tilesCreate)
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    @Test
    fun `cloud merges during incident`() {
        val cloudHandler = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(
            tileManagerCreate,
            mutableListOf(cloudRoad)
        )

        val cloudCreation = CloudCreationIncident(
            id = 1,
            tick = 0,
            location = 2,
            cloudHandler = cloudHandler,
            radius = 0,
            amount = 1000,
            duration = -1,
        )

        cloudCreation.perform(0, YearTick.APR2)

        val mergedCloud = cloudHandler.clouds[0]
        assertFalse(
            cloudHandler.clouds.contains(cloudRoad),
            "Cloud ${cloudRoad.id} must be removed during merge"
        )
        assertEquals(
            mergedCloud.id,
            cloudRoad.id + 2,
            "Maximum id must be increased by 2 due to" +
                " a merge with a cloud from the incident"
        )
        assertEquals(
            mergedCloud.duration,
            -1,
            "Cloud ${mergedCloud.id} must have duration = -1"
        )
    }

    @Test
    fun `cloud creation not possible on a village`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManagerCreate, mutableListOf())

        val cloudCreation = CloudCreationIncident(
            id = 1,
            tick = 0,
            location = 3,
            cloudHandler = cloudHandler,
            radius = 0,
            amount = 1000,
            duration = -1,
        )

        cloudCreation.perform(0, YearTick.APR2)

        assertTrue(
            cloudHandler.clouds.isEmpty(),
            "A cloud cannot be created on a village"
        )
    }
}
