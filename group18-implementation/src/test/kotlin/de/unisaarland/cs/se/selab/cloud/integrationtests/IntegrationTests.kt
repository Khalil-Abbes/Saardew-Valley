package de.unisaarland.cs.se.selab.cloud.integrationtests

import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.Direction
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter

private val tile1F_noMove = Field(
    id = 1,
    coordinate = Coordinate(0, 0),
    farm = 1,
    direction = null,
    moistureCapacity = 6000,
    possiblePlants = emptyList()
)

private val tile2F_noMove = Field(
    id = 2,
    coordinate = Coordinate(2, 0),
    farm = 1,
    direction = null,
    moistureCapacity = 6000,
    possiblePlants = emptyList()
)

private val tile3F_noMove = Field(
    id = 3,
    coordinate = Coordinate(4, 0),
    farm = 1,
    direction = null,
    moistureCapacity = 6000,
    possiblePlants = emptyList()
)

private val tile4F_noMove = Field(
    id = 4,
    coordinate = Coordinate(6, 0),
    farm = 1,
    direction = null,
    moistureCapacity = 6000,
    possiblePlants = emptyList()
)

private val tile5NF_noMove = Tile(
    id = 5,
    coordinate = Coordinate(8, 0),
    farm = 1,
    direction = null,
    shed = false,
    categoryType = TileCategory.ROAD
)

private val tile6NF_move_village = Tile(
    id = 6,
    coordinate = Coordinate(1, 1),
    farm = 1,
    direction = Direction.ANGLE_225,
    shed = false,
    categoryType = TileCategory.MEADOW
)

private val tile7NF_noMove = Tile(
    id = 7,
    coordinate = Coordinate(3, 1),
    farm = 1,
    direction = null,
    shed = false,
    categoryType = TileCategory.ROAD
)

private val tile8NF_noMove = Tile(
    id = 8,
    coordinate = Coordinate(5, 1),
    farm = 1,
    direction = null,
    shed = false,
    categoryType = TileCategory.ROAD
)

// tile10 is empty tile

private val tile10NF_village = Tile(
    id = 10,
    coordinate = Coordinate(0, 2),
    farm = 1,
    direction = null,
    shed = false,
    categoryType = TileCategory.VILLAGE
)

private val tile11F_noMove_noRain = Plantation(
    id = 11,
    coordinate = Coordinate(2, 2),
    farm = 1,
    direction = null,
    moistureCapacity = 1000,
    plant = PlantationPlant(PlantationPlantType.CHERRY)
)

private val tile12NF_noMove_noRain = Field(
    id = 12,
    coordinate = Coordinate(4, 2),
    farm = 1,
    direction = null,
    possiblePlants = emptyList(),
    moistureCapacity = 1000
)

private val tile13NF_noMove_noRain = Field(
    id = 13,
    coordinate = Coordinate(6, 2),
    farm = 1,
    direction = Direction.ANGLE_45,
    possiblePlants = emptyList(),
    moistureCapacity = 1000
)

private val tile14NF_noMove = Tile(
    id = 14,
    coordinate = Coordinate(8, 2),
    farm = 1,
    direction = null,
    shed = false,
    categoryType = TileCategory.ROAD
)

private val tile15NF_noMove = Tile(
    id = 15,
    coordinate = Coordinate(5, 3),
    farm = 1,
    direction = null,
    shed = false,
    categoryType = TileCategory.MEADOW
)

private val tile16NF_move = Tile(
    id = 16,
    coordinate = Coordinate(4, 4),
    farm = 1,
    direction = Direction.ANGLE_0,
    shed = false,
    categoryType = TileCategory.MEADOW,
)

private val tile17F_move_noRain = Field(
    id = 17,
    coordinate = Coordinate(6, 4),
    farm = 1,
    direction = Direction.ANGLE_315,
    possiblePlants = emptyList(),
    moistureCapacity = 100
)

private val tile18F_move_noRain = Field(
    id = 18,
    coordinate = Coordinate(8, 4),
    farm = 1,
    direction = Direction.ANGLE_0,
    possiblePlants = emptyList(),
    moistureCapacity = 100
)

private val cloud1_noMove_noRain = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 1,
    duration = 2,
    location = 1,
    amount = 100
)
private val cloud2_noMove_rainAll = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 2,
    duration = 2,
    location = 2,
    amount = 5000
)
private val cloud3_noMove_rainPart = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 3,
    duration = 2,
    location = 3,
    amount = 7000
)
private val cloud4_noMove_rainEnough = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 4,
    duration = 2,
    location = 4,
    amount = 6000
)
private val cloud5_noMove_rainAll = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 5,
    duration = 2,
    location = 5,
    amount = 5000
)
private val cloud11_noMove_noRain = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 6,
    duration = 2,
    location = 11,
    amount = 6000
)

private val cloud7_noMove_noRain_dissipate = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 7,
    duration = 1,
    location = 7,
    amount = 500
)
private val cloud8_noMove_noRain = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 8,
    duration = 3,
    location = 8,
    amount = 500
)
private val cloud12_noMove_noRain = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 12,
    duration = 2,
    location = 12,
    amount = 600
)
private val cloud13_noMove_noRain = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 13,
    duration = 2,
    location = 13,
    amount = 600
)
private val cloud14_merge1 = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 14,
    duration = -1,
    location = 14,
    amount = 600
)
private val cloud16F_move_noMerge = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 16,
    duration = 2,
    location = 16,
    amount = 600
)
private val cloud17F_noMove_empty = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 17,
    duration = 2,
    location = 17,
    amount = 600
)
private val cloud18_move_merge = _root_ide_package_.de.unisaarland.cs.se.selab.cloud.Cloud(
    id = 18,
    duration = 2,
    location = 18,
    amount = 600
)

private val tileManager = TileManager()
private val tiles = mutableMapOf(
    1 to tile1F_noMove,
    2 to tile2F_noMove,
    3 to tile3F_noMove,
    4 to tile4F_noMove,
    5 to tile5NF_noMove,
    6 to tile6NF_move_village,
    7 to tile7NF_noMove,
    8 to tile8NF_noMove,
    10 to tile10NF_village,
    11 to tile11F_noMove_noRain,
    12 to tile12NF_noMove_noRain,
    13 to tile13NF_noMove_noRain,
    14 to tile14NF_noMove,
    15 to tile15NF_noMove,
    16 to tile16NF_move,
    17 to tile17F_move_noRain,
    18 to tile18F_move_noRain
)

private val clouds_rain_variations = mutableListOf(
    cloud1_noMove_noRain,
    cloud2_noMove_rainAll,
    cloud3_noMove_rainPart,
    cloud4_noMove_rainEnough,
    cloud5_noMove_rainAll,
    cloud11_noMove_noRain
)
private val clouds_duration_variations = mutableListOf(
    cloud7_noMove_noRain_dissipate,
    cloud8_noMove_noRain,
    cloud14_merge1
)
private val clouds_sunlight_traverse = mutableListOf(cloud17F_noMove_empty, cloud18_move_merge)
private val clouds_sunlight_staying = mutableListOf(cloud12_noMove_noRain, cloud13_noMove_noRain)
private val clouds_move_no_merge = mutableListOf(cloud16F_move_noMerge)

class IntegrationTests {

    @BeforeEach
    fun setup() {
        tileManager.setTiles(tiles)
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    /**
     * Test for 6 cases for rain - 2x no rain, 3x dissipation, 1x fill tile
     */
    @Test
    fun `test all rain cases`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, clouds_rain_variations)
        tile1F_noMove.currentMoisture = 0
        tile2F_noMove.currentMoisture = 0
        tile3F_noMove.currentMoisture = 0
        tile4F_noMove.currentMoisture = 0

        cloudHandler.run()

        assertEquals(
            clouds_rain_variations[0].amount,
            cloudHandler.clouds[0].amount,
            "Expected cloud ${cloudHandler.clouds[0].id} to have ${clouds_rain_variations[0].amount} L"
        )

        assertEquals(
            tile2F_noMove.currentMoisture,
            tile2F_noMove.moistureCapacity - cloudHandler.clouds[1].amount,
            "Expected tile ${tile2F_noMove.id} to have " +
                "${tile2F_noMove.moistureCapacity - cloudHandler.clouds[1].amount} moisture"
        )
        assertFalse(
            cloudHandler.clouds.contains(cloud2_noMove_rainAll),
            "Expected cloud ${cloud2_noMove_rainAll.id} to be removed"
        )

        assertEquals(
            tile3F_noMove.currentMoisture,
            tile3F_noMove.moistureCapacity,
            "Expected tile ${tile3F_noMove.id} to have ${tile3F_noMove.moistureCapacity} moisture"
        )
        assertTrue(
            cloudHandler.clouds.contains(cloud3_noMove_rainPart),
            "Expected cloud ${cloud3_noMove_rainPart.id} to be added"
        )

        assertEquals(
            tile4F_noMove.currentMoisture,
            tile4F_noMove.moistureCapacity,
            "Expected tile ${tile4F_noMove.id} to have ${tile4F_noMove.moistureCapacity} moisture"
        )
        assertFalse(
            cloudHandler.clouds.contains(cloud4_noMove_rainEnough),
            "Expected cloud ${cloud4_noMove_rainEnough.id} to be removed"
        )

        assertEquals(
            clouds_rain_variations.last().amount,
            cloudHandler.clouds.last().amount,
            "Expected cloud ${cloudHandler.clouds.last().id} to have ${clouds_rain_variations.last().amount} L"
        )
    }

    @Test
    fun `test duration decrease and dissipation`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, clouds_duration_variations)

        cloudHandler.run()

        assertEquals(
            2,
            cloudHandler.clouds[0].duration,
            "Expected duration of cloud ${cloud8_noMove_noRain.id} to decrease from by 1}"
        )
        assertEquals(
            -1,
            cloudHandler.clouds[1].duration,
            "Expected duration of cloud ${cloud14_merge1.id} to remain -1"
        )
        assertFalse(
            cloudHandler.clouds.contains(cloud7_noMove_noRain_dissipate),
            "Expected cloud ${cloud7_noMove_noRain_dissipate.id} to be removed"
        )
    }

    @Test
    fun `test sunlight traverse`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, clouds_sunlight_traverse)

        tile17F_move_noRain.sunlightThisTick = 10
        tile18F_move_noRain.sunlightThisTick = 2
        cloudHandler.run()

        assertEquals(
            10 - _root_ide_package_.de.unisaarland.cs.se.selab.cloud.REDUCE_SUNLIGHT_PER_TRAVERSE,
            tile17F_move_noRain.sunlightThisTick,
            "Expected tile ${tile17F_move_noRain.id} to have sunlight decreased " +
                "to ${tile17F_move_noRain.sunlightThisTick} sunlight"
        )

        assertEquals(
            0,
            tile18F_move_noRain.sunlightThisTick,
            "Expected tile ${tile18F_move_noRain.id} to have sunlight decreased to 0"
        )
    }

    @Test
    fun `test sunlight decrease at the end`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, clouds_sunlight_staying)
        tile12NF_noMove_noRain.sunlightThisTick = 100
        tile13NF_noMove_noRain.sunlightThisTick = 10

        cloudHandler.run()

        assertEquals(
            100 - _root_ide_package_.de.unisaarland.cs.se.selab.cloud.REDUCE_SUNLIGHT_CLOUDY_TILES,
            tile12NF_noMove_noRain.sunlightThisTick,
            "Expected tile ${tile12NF_noMove_noRain.id} to have sunlight decreased " +
                "to ${tile12NF_noMove_noRain.sunlightThisTick} sunlight"
        )

        assertEquals(
            0,
            tile13NF_noMove_noRain.sunlightThisTick,
            "Expected tile ${tile13NF_noMove_noRain.id} to have sunlight decreased to 0"
        )
    }

    @Test
    fun `test cloud move no merge`() {
        val cloudHandler =
            _root_ide_package_.de.unisaarland.cs.se.selab.cloud.CloudHandler(tileManager, clouds_move_no_merge)
        cloudHandler.run()

        assertEquals(
            cloudHandler.clouds[0].location,
            cloud16F_move_noMerge.location,
            "Expected cloud ${cloudHandler.clouds[0].id} to have ${1} tiles of travel distance"
        )
    }
}
