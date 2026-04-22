package de.unisaarland.cs.se.selab.cloud.systemtests

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.Direction
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.Test
import java.io.PrintWriter

class SimulationTest {

    var tile1FieldRun = Field(
        id = 1,
        coordinate = Coordinate(0, 0),
        farm = 1,
        direction = Direction.ANGLE_90,
        moistureCapacity = 5000,
        possiblePlants = listOf(FieldPlantType.PUMPKIN)
    )
    var tile2PlantationRun = Plantation(
        id = 2,
        coordinate = Coordinate(2, 0),
        farm = 1,
        direction = Direction.ANGLE_180,
        plant = PlantationPlant(PlantationPlantType.CHERRY),
        moistureCapacity = 5000
    )
    var tile3RoadRun = Tile(
        id = 3,
        coordinate = Coordinate(4, 0),
        farm = null,
        direction = null,
        categoryType = TileCategory.ROAD,
        shed = true
    )
    var tile4MeadowRun = Tile(
        id = 4,
        coordinate = Coordinate(1, 1),
        farm = null,
        direction = Direction.ANGLE_45,
        categoryType = TileCategory.MEADOW,
        shed = false
    )
    var tile5VillageRun = Tile(
        id = 5,
        coordinate = Coordinate(3, 1),
        farm = null,
        direction = null,
        categoryType = TileCategory.VILLAGE,
        shed = true
    )
    var tile6RoadRun = Tile(
        id = 4,
        coordinate = Coordinate(0, 2),
        farm = null,
        direction = null,
        categoryType = TileCategory.ROAD,
        shed = true,
    )
    var tile7PlantationRun = Plantation(
        id = 7,
        coordinate = Coordinate(2, 2),
        farm = 1,
        direction = null,
        plant = PlantationPlant(PlantationPlantType.CHERRY),
        moistureCapacity = 5000
    )
    var tile8RoadRun = Tile(
        id = 8,
        coordinate = Coordinate(4, 2),
        farm = null,
        direction = Direction.ANGLE_315,
        categoryType = TileCategory.ROAD,
        shed = true
    )
    var tile9MeadowRun = Tile(
        id = 9,
        coordinate = Coordinate(-1, -1),
        farm = null,
        direction = Direction.ANGLE_135,
        categoryType = TileCategory.MEADOW,
        shed = false
    )
    var tile10VillageRun = Tile(
        id = 10,
        coordinate = Coordinate(2, -2),
        farm = null,
        direction = null,
        categoryType = TileCategory.VILLAGE,
        shed = true
    )
    var tile11MeadowRun = Tile(
        id = 11,
        coordinate = Coordinate(3, -1),
        farm = null,
        direction = Direction.ANGLE_225,
        categoryType = TileCategory.MEADOW,
        shed = false
    )
    var tile12FieldRun = Field(
        id = 12,
        coordinate = Coordinate(4, -2),
        farm = 1,
        direction = null,
        moistureCapacity = 5000,
        possiblePlants = listOf(FieldPlantType.PUMPKIN)
    )
    var tile13MeadowRun = Tile(
        id = 13,
        coordinate = Coordinate(5, -1),
        farm = null,
        direction = Direction.ANGLE_225,
        categoryType = TileCategory.MEADOW,
        shed = false
    )
    val cloud1t1 = Cloud(
        id = 1,
        duration = 2,
        location = 1,
        amount = 6000
    )
    val cloud2t7 = Cloud(
        id = 2,
        duration = -1,
        location = 7,
        amount = 1000
    )
    val cloud3t3 = Cloud(
        id = 3,
        duration = 4,
        location = 3,
        amount = 6000
    )
    val cloud4t9 = Cloud(
        id = 4,
        duration = 6,
        location = 9,
        amount = 1000
    )
    val cloud5t6 = Cloud(
        id = 5,
        duration = 3,
        location = 6,
        amount = 500
    )
    val cloud6t10 = Cloud(
        id = 6,
        duration = 12,
        location = 10,
        amount = 5000
    )
    val cloud7t11 = Cloud(
        id = 7,
        duration = 7,
        location = 11,
        amount = 200
    )
    val cloud8t12 = Cloud(
        id = 8,
        duration = 5,
        location = 12,
        amount = 14000
    )
    val cloud9t13 = Cloud(
        id = 9,
        duration = 1,
        location = 13,
        amount = 1000
    )
    val tileManager = TileManager()
    val tiles = mutableMapOf(
        1 to tile1FieldRun, 2 to tile2PlantationRun, 3 to tile3RoadRun,
        4 to tile4MeadowRun, 5 to tile5VillageRun, 6 to tile6RoadRun,
        7 to tile7PlantationRun, 8 to tile8RoadRun, 9 to tile9MeadowRun,
        10 to tile10VillageRun, 11 to tile11MeadowRun, 12 to tile12FieldRun,
        13 to tile13MeadowRun
    )
    val cloudHandler = CloudHandler(
        tileManager,
        mutableListOf(
            cloud1t1, cloud2t7, cloud3t3, cloud4t9, cloud5t6, cloud6t10, cloud7t11, cloud8t12, cloud9t13
        )
    )

    @Test
    fun `Run full cloud phase simulation`() {
        tileManager.setTiles(tiles)
        tile1FieldRun.currentMoisture = 500
        tile2PlantationRun.currentMoisture = 3000
        tile7PlantationRun.currentMoisture = 2000
        tile12FieldRun.currentMoisture = 1000
        tile1FieldRun.sunlightThisTick = 70
        tile2PlantationRun.sunlightThisTick = 80
        tile7PlantationRun.sunlightThisTick = 90
        tile12FieldRun.sunlightThisTick = 10

        Logger.concreteLogger = DebugLogger(
            PrintWriter(".\\src\\test\\kotlin\\de\\unisaarland\\cs\\se\\selab\\cloud\\systemtests\\log.txt")
        )

        cloudHandler.run()
    }
}
