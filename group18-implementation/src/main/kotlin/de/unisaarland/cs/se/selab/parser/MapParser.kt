package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.Direction
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

private const val FARM = "farm"
private const val CAPACITY = "capacity"
private const val POSSIBLE_PLANTS = "possiblePlants"
private const val PLANT = "plant"
private const val DIRECTION = "direction"
private const val SHED = "shed"

// Direction angle constants
private const val NORTH = 0
private const val NORTHEAST = 45
private const val EAST = 90
private const val SOUTHEAST = 135
private const val SOUTH = 180
private const val SOUTHWEST = 225
private const val WEST = 270
private const val NORTHWEST = 315

// Angle sets using the constants
private val SQUARE_TILE_ANGLES = setOf(NORTHEAST, SOUTHEAST, SOUTHWEST, NORTHWEST)
private val OCTAGONAL_TILE_ANGLES = setOf(NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST)

/**
 * Parser for the map file
 */
class MapParser(val tileManager: TileManager) {

    private val coordinateToTile = mutableMapOf<Pair<Int, Int>, Int>()

    /**
     * Parser for the map file
     * @param mapFile Path to the JSON map configuration file
     * @return TileManager if successful, null if validation fails
     */
    fun parseMap(mapFile: String): Boolean {
        if (!Validator.validateFile(mapFile, "classpath://schema/map.schema")) return false
        val jsonContent = File(mapFile).readText()
        val jsonObject = JSONObject(jsonContent)
        val tilesArray = jsonObject.getJSONArray("tiles")

        val tiles = mutableMapOf<Int, Tile>()

        for (i in 0 until tilesArray.length()) {
            val jsonTile = tilesArray.getJSONObject(i)
            if (!validateTileProperties(jsonTile)) {
                return false
            }
            val tile = createTileFromJson(jsonTile)
            if (tiles[tile.id] != null) {
                return false
            }
            tiles[tile.id] = tile
        }
        tileManager.setTiles(tiles)
        for (tile in tiles.values) {
            if (!validateAdjoiningTiles(tile, tileManager)) {
                return false
            }
        }
        return true
    }

    /**
     * Validates tile properties as shown in sequence diagram
     * @param jsonTile The JSON object representing a tile
     * @return true if validation passes
     */
    private fun validateTileProperties(jsonTile: JSONObject): Boolean {
        val id = jsonTile.optInt(ID)
        val coordinates = jsonTile.getJSONObject("coordinates")
        val x = coordinates.optInt("x")
        val y = coordinates.optInt("y")
        if (coordinateToTile[Pair(x, y)] != null) {
            return false
        } else {
            coordinateToTile[Pair(x, y)] = id
        }
        val categoryString = jsonTile.optString("category")
        val category = TileCategory.entries.find { it.name == categoryString }
            ?: return false

        val farm = if (jsonTile.has(FARM)) jsonTile.optInt(FARM) else null

        val capacity = if (jsonTile.has(CAPACITY)) jsonTile.optInt(CAPACITY) else null
        val possiblePlants = if (jsonTile.has(POSSIBLE_PLANTS)) jsonTile.optJSONArray(POSSIBLE_PLANTS) else null
        val plantType = if (jsonTile.has(PLANT)) jsonTile.optString(PLANT) else null
        val direction = jsonTile.optString(DIRECTION)
        val directionInt = if (direction.isEmpty()) {
            -1
        } else {
            direction.toInt()
        }
        // Validate tile shape based on category
        if (!validateTileShape(category, x, y)) {
            return false
        }

        // Validate direction based on coordinates
        if (!validateDirection(jsonTile, directionInt, x, y)) {
            return false
        }
        return validateTileParameters(
            jsonTile,
            Pair(x, y),
            category,
            farm,
            capacity,
            possiblePlants,
            plantType
        )
    }

    private fun validateTileParameters(
        jsonTile: JSONObject,
        coordinate: Pair<Int?, Int?>,
        category: TileCategory,
        farm: Int?,
        capacity: Int?,
        possiblePlants: JSONArray?,
        plantType: String?,
    ): Boolean {
        val id = jsonTile.optInt(ID)
        if (id < 0) return false
        val x = coordinate.first
        val y = coordinate.second
        if (x == null || y == null) return false

        return when (category) {
            TileCategory.FARMSTEAD -> validateFarmsteadTile(farm) &&
                capacity == null &&
                plantType == null &&
                possiblePlants == null
            TileCategory.FIELD -> validateFieldTile(
                farm,
                capacity, possiblePlants
            ) && plantType == null &&
                !jsonTile.has(SHED)
            TileCategory.PLANTATION -> validatePlantationTile(
                farm,
                capacity, plantType
            ) && possiblePlants == null &&
                !jsonTile.has(SHED)
            TileCategory.VILLAGE -> validateVillage(jsonTile)
            else ->
                farm == null &&
                    capacity == null &&
                    possiblePlants == null &&
                    plantType == null &&
                    !jsonTile.has(SHED)
        }
    }

    private fun validateVillage(jsonTile: JSONObject): Boolean {
        return !jsonTile.has(FARM) &&
            !jsonTile.has("airflow") &&
            !jsonTile.has(DIRECTION) &&
            !jsonTile.has(CAPACITY) &&
            !jsonTile.has(PLANT) &&
            !jsonTile.has(POSSIBLE_PLANTS) &&
            !jsonTile.has(SHED)
    }
    private fun validateFarmsteadTile(farm: Int?): Boolean {
        return farm != null && farm >= 0
    }

    private fun validateFieldTile(farm: Int?, capacity: Int?, possiblePlants: JSONArray?): Boolean {
        if (farm == null || farm < 0) return false
        if (capacity == null || capacity <= 0) return false
        if (possiblePlants == null) return false

        for (i in 0 until possiblePlants.length()) {
            val plantName = possiblePlants.optString(i)
            if (plantName.isEmpty() || !isValidFieldPlantType(plantName)) return false
        }
        return true
    }

    private fun validatePlantationTile(farm: Int?, capacity: Int?, plantType: String?): Boolean {
        if (farm == null || farm < 0) return false
        if (capacity == null || capacity <= 0) return false
        if (plantType == null) return false

        return isValidPlantationPlantType(plantType)
    }

    private fun isValidFieldPlantType(plantName: String): Boolean {
        return FieldPlantType.entries.any { it.name == plantName }
    }

    private fun isValidPlantationPlantType(plantType: String): Boolean {
        return PlantationPlantType.entries.any { it.name == plantType }
    }

    /**
     * Creates tiles based on type following sequence diagram alternatives
     */
    private fun createTileFromJson(jsonTile: JSONObject): Tile {
        // Basic field validation and extraction
        val id = jsonTile.optInt(ID)
        val coordinates = jsonTile.getJSONObject("coordinates")
        val x = coordinates.optInt("x")
        val y = coordinates.optInt("y")
        val category = jsonTile.optString("category")
        val tileCoordinate = Coordinate(x, y)
        return when (category) {
            "VILLAGE" -> createVillageTileFromJson(id, tileCoordinate)
            "FIELD" -> createFieldTileFromJson(id, tileCoordinate, jsonTile)
            "FARMSTEAD" -> createFarmsteadTileFromJson(id, tileCoordinate, jsonTile)
            "PLANTATION" -> createPlantationTileFromJson(id, tileCoordinate, jsonTile)
            "MEADOW" -> createMeadowTileFromJson(id, tileCoordinate, jsonTile)
            "ROAD" -> createRoadTileFromJson(id, tileCoordinate, jsonTile)
            "FOREST" -> createForestTileFromJson(id, tileCoordinate, jsonTile)
            else -> throw IllegalArgumentException("Unknown tile category: $category")
        }
    }

    /**
     * validates that the direction of the tile is possible to move to
     */

    private fun validateDirection(jsonTile: JSONObject, direction: Int, x: Int, y: Int): Boolean {
        val airflow = jsonTile.optBoolean("airflow")
        // If no direction is provided, it's valid (optional field)
        if (direction == -1) return !airflow
        val isSquare = x % 2 != 0 && y % 2 != 0 // Both odd (handles negatives if square tile is -1,-1)
        val validAngles = if (isSquare) {
            // Square tiles: both x and y are odd (only diagonal directions)
            SQUARE_TILE_ANGLES
        } else {
            // Octagonal tiles: both x and y are even (all directions)
            OCTAGONAL_TILE_ANGLES
        }

        return airflow && direction in validAngles
    }

    /**
     * Validates that the tile coordinates match the required shape for the tile category
     * @param category The tile category
     * @param x The x coordinate
     * @param y The y coordinate
     * @return true if coordinates match the required shape for this tile type
     */
    private fun validateTileShape(category: TileCategory, x: Int, y: Int): Boolean {
        val isSquare = x % 2 != 0 && y % 2 != 0 // Both odd (handles negatives if square tile is -1,-1)
        val isOctagonal = x % 2 == 0 && y % 2 == 0 // Both even

        return when (category) {
            // Square tiles only (odd coordinates only)
            TileCategory.FARMSTEAD, TileCategory.MEADOW -> isSquare

            // Octagonal tiles only (even coordinates only)
            TileCategory.FIELD, TileCategory.PLANTATION -> isOctagonal

            // Both square and octagonal allowed
            TileCategory.FOREST, TileCategory.VILLAGE, TileCategory.ROAD -> isSquare || isOctagonal
        }
    }

    private fun extractDirection(jsonTile: JSONObject): Direction? {
        val directionInt = jsonTile.optInt(DIRECTION, -1)

        // Return null if direction is -1 (representing empty/missing direction)
        if (directionInt == -1) {
            return null
        }

        return Direction.entries.find { it.angle == directionInt }
    }
    private fun createVillageTileFromJson(id: Int, coordinate: Coordinate): Tile {
        return TileFactory.createVillageTile(id, coordinate)
    }

    private fun createFieldTileFromJson(id: Int, coordinate: Coordinate, jsonTile: JSONObject): Tile {
        val farm = jsonTile.optInt(FARM)
        val capacity = jsonTile.optInt(CAPACITY)
        val possiblePlantsArray = jsonTile.getJSONArray(POSSIBLE_PLANTS)
        val possiblePlants = mutableListOf<FieldPlantType>()
        for (i in 0 until possiblePlantsArray.length()) {
            possiblePlants.add(
                FieldPlantType.valueOf(possiblePlantsArray.optString(i))
            )
        }
        val direction = extractDirection(jsonTile)

        return TileFactory.createFieldTile(
            id,
            coordinate,
            farm,
            direction,
            capacity,
            possiblePlants
        )
    }

    private fun createFarmsteadTileFromJson(id: Int, coordinate: Coordinate, jsonTile: JSONObject): Tile {
        val farm = jsonTile.optInt(FARM)
        val shed = jsonTile.optBoolean(SHED)
        val direction = extractDirection(jsonTile)

        return TileFactory.createFarmsteadTile(id, coordinate, shed, farm, direction)
    }

    private fun createPlantationTileFromJson(id: Int, coordinate: Coordinate, jsonTile: JSONObject): Tile {
        val farm = jsonTile.optInt(FARM)
        val capacity = jsonTile.optInt(CAPACITY)
        val plantType = PlantationPlantType.valueOf(jsonTile.optString(PLANT))
        val direction = extractDirection(jsonTile)

        return TileFactory.createPlantationTile(
            id,
            coordinate,
            farm,
            direction,
            capacity,
            PlantationPlant(plantType)
        )
    }

    private fun createMeadowTileFromJson(id: Int, coordinate: Coordinate, jsonTile: JSONObject): Tile {
        val direction = extractDirection(jsonTile)
        return TileFactory.createMeadowTile(id, coordinate, direction)
    }

    private fun createRoadTileFromJson(id: Int, coordinate: Coordinate, jsonTile: JSONObject): Tile {
        val direction = extractDirection(jsonTile)
        return TileFactory.createRoadTile(id, coordinate, direction)
    }

    private fun createForestTileFromJson(id: Int, coordinate: Coordinate, jsonTile: JSONObject): Tile {
        val direction = extractDirection(jsonTile)
        return TileFactory.createForestTile(id, coordinate, direction)
    }

    /**
     * Validates adjoining tiles based on specification
     */
    private fun validateAdjoiningTiles(tile: Tile, tileManager: TileManager): Boolean {
        val neighbors = tileManager.getAllNeighborTilesInRadius(tile.id, 1).filter { it != tile }

        return when (tile.categoryType) {
            TileCategory.FARMSTEAD -> {
                // [t1, t2].all(!(it is Farmstead || it is MEADOW))
                neighbors.all { neighbor ->
                    !(
                        neighbor.categoryType == TileCategory.FARMSTEAD ||
                            neighbor.categoryType == TileCategory.MEADOW ||
                            (
                                neighbor.categoryType == TileCategory.FIELD &&
                                    neighbor.farm != tile.farm
                                ) ||
                            (
                                neighbor.categoryType == TileCategory.PLANTATION &&
                                    neighbor.farm != tile.farm
                                )

                        )
                }
            }

            TileCategory.FOREST -> {
                // [t1, t2].all(!(it is Village))
                neighbors.all { neighbor ->
                    neighbor.categoryType != TileCategory.VILLAGE
                }
            }

            TileCategory.VILLAGE -> {
                // [t1, t2].all(!(it is Forest))
                neighbors.all { neighbor ->
                    neighbor.categoryType != TileCategory.FOREST
                }
            }

            else -> {
                true
            }
        }
    }
}
