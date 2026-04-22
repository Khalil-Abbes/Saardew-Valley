package de.unisaarland.cs.se.selab.cloud.integrationtests

import de.unisaarland.cs.se.selab.incident.CityExpansionIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Direction
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import kotlin.test.junit5.JUnit5Asserter.assertEquals

private val village = Tile(
    id = 1,
    coordinate = Coordinate(0, 0),
    categoryType = TileCategory.VILLAGE,
    shed = false,
    farm = null,
    direction = null
)

private val road = Tile(
    id = 2,
    coordinate = Coordinate(0, 2),
    categoryType = TileCategory.ROAD,
    shed = false,
    farm = null,
    direction = Direction.ANGLE_0,
)

private val forest = Tile(
    id = 3,
    coordinate = Coordinate(0, 4),
    categoryType = TileCategory.FOREST,
    shed = false,
    farm = null,
    direction = null,
)

private val road2 = Tile(
    id = 4,
    coordinate = Coordinate(0, 6),
    categoryType = TileCategory.ROAD,
    shed = false,
    farm = null,
    direction = Direction.ANGLE_0,
)

private val cloudVillage = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 1,
    duration = -1,
    location = 1,
    amount = 2000
)
private val cloudRoad = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 2,
    duration = -1,
    location = 2,
    amount = 2000
)
private val cloudDissipate = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 3,
    duration = 1,
    location = 3,
    amount = 2000
)
private val cloudMove = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 0,
    duration = -1,
    location = 4,
    amount = 2000
)

private val tileManagerDissipation = TileManager()
private val tilesDissipation = mutableMapOf(
    1 to village,
    2 to road,
    3 to forest,
    4 to road2
)

class CloudDissipationTests {

    @BeforeEach
    fun setup() {
        tileManagerDissipation.setTiles(tilesDissipation)
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    @Test
    fun `no cloud dissipation after city expansion`() {
        val cloudHandler = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(
            tileManagerDissipation,
            mutableListOf(cloudMove)
        )
        val clouds = cloudHandler.clouds
        val cityExpansion = CityExpansionIncident(
            id = 1,
            tick = 0,
            location = 2,
            farms = emptyList(),
            tileManager = tileManagerDissipation,
            cloudHandler = cloudHandler
        )

        cityExpansion.perform(0, YearTick.APR2)
        val updatedTile = tileManagerDissipation.getTileById(2)!!
        kotlin.test.assertEquals(
            cloudHandler.clouds,
            clouds,
            "no changes in the cloud list"
        )
        assertEquals(
            actual = updatedTile.categoryType.name,
            expected = TileCategory.VILLAGE.name,
            message = "Tile ${2} must become a village"
        )
    }

    @Test
    fun `cloud spawned on a village dissipates`() {
        val cloudHandler = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(
            tileManagerDissipation,
            mutableListOf(cloudVillage)
        )
        cloudHandler.run()

        assertTrue(
            cloudHandler.clouds.isEmpty(),
            "Cloud ${cloudVillage.id} must dissipate on a village"
        )
    }

    @Test
    fun `cloud dissipates in moving to village`() {
        val cloudHandler = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(
            tileManagerDissipation,
            mutableListOf(cloudRoad)
        )
        cloudHandler.run()

        assertTrue(
            cloudHandler.clouds.isEmpty(),
            "Cloud ${cloudRoad.id} must dissipate before reaching a village"
        )
    }

    @Test
    fun `cloud dissipates after duration decrease due to merge`() {
        val cloudHandler = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(
            tileManagerDissipation,
            mutableListOf(cloudDissipate, cloudMove)
        )
        val highestId = cloudHandler.highestID
        cloudHandler.run()

        assertTrue(
            cloudHandler.clouds.isEmpty(),
            "Cloud ${cloudHandler.highestID} must dissipate before reaching a village"
        )
        assertTrue(
            cloudHandler.highestID == highestId + 1,
            "Clouds must merge"
        )
    }
}
