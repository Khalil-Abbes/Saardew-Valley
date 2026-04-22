package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory

/**
 * Manages a collection of tiles, providing methods to access and manipulate them.
 *
 * @param idToTile A map where the key is the tile ID and the value is the corresponding `Tile` object.
 *                 This map is used to initialize the internal data structures for managing tiles.
 */
class TileManager {
    val idToTile = mutableMapOf<Int, Tile>()

    val coordinateToTile = mutableMapOf<Coordinate, Tile>()

    /**
     * tile setter at the start of parsing
     */
    fun setTiles(tiles: MutableMap<Int, Tile>) {
        tiles.forEach { (key, value) -> idToTile[key] = value }
        createCoordinateToTile()
    }

    /**
     * Retrieves all tiles managed by this `TileManager`.
     *
     * @return A list of all `Tile` objects.
     */
    fun getAllTiles(): List<Tile> {
        return idToTile.values.toList()
    }

    /**
     * Retrieves a tile by its unique ID.
     *
     * @param id The unique identifier of the tile.
     * @return The `Tile` object if found, or `null` if no tile matches the given ID.
     */
    fun getTileById(id: Int): Tile? {
        return idToTile[id]
    }

    /**
     * Retrieves a tile by its coordinate.
     *
     * @param coordinate The coordinate of the tile.
     * @return The `Tile` object if found, or `null` if no tile matches the given coordinate.
     */
    fun getTileByCoordinate(coordinate: Coordinate?): Tile? {
        coordinate ?: return null
        return coordinateToTile[coordinate] // updated it to remove a line that was O(n)
    }

    /**
     * Retrieves the neighboring tile in the direction specified by the given tile.
     *
     * @param tile The tile whose neighbor is to be found.
     * @return The neighboring `Tile` object if found, or `null` if no neighbor exists.
     */
    fun getNeighborInDirection(tile: Tile): Tile? {
        val tileDirection = tile.direction ?: return null
        val coordinates = tile.coordinate
        val neighborCoordinates = coordinates.getNeighborCoordinateInDirection(tileDirection)
        return getTileByCoordinate(neighborCoordinates)
    }

    /**
     * Retrieves all neighboring tiles within a specified radius including the center tile.
     * In order by tile ID.
     *
     * @param tileID The ID of the center tile.
     * @param radius The radius within which to find neighboring tiles.
     * @return A list of `Tile` objects representing the neighbors within the radius.
     */
    fun getAllNeighborTilesInRadius(tileID: Int, radius: Int): List<Tile> {
        val center = getTileById(tileID) ?: return emptyList()
        return getAllNeighborTilesInRadius(center, radius).sortedBy { it.id }
    }

    /**
     * Retrieves all neighboring tiles within a specified radius including the center tile.
     * In order by tile ID.
     *
     * @param tile The center tile.
     * @param radius The radius within which to find neighboring tiles.
     * @return A list of `Tile` objects representing the neighbors within the radius.
     */
    fun getAllNeighborTilesInRadius(tile: Tile, radius: Int): List<Tile> {
        return tile.coordinate
            .getCoordinatesWithinRadius(radius).mapNotNull { getTileByCoordinate(it) }
            .sortedBy { it.id }
    }

    /**
     * Retrieves all farm fields within a specified radius of a given tile.
     *
     * @param farmID The ID of the farm.
     * @param tile The center tile.
     * @param radius The radius within which to find farm fields.
     * @return A list of `Field` objects representing the farm fields within the radius.
     */
    fun getFarmFieldsInRadius(farmID: Int, tile: Tile, radius: Int): List<Field> {
        return getAllNeighborTilesInRadius(tile.id, radius)
            .filter { it.farm == farmID && it is Field }
            .sortedBy { it.id }
            .map { it as Field }
    }

    /**
     * Retrieves all farm plantations within a specified radius of a given tile.
     *
     * @param farmID The ID of the farm.
     * @param tile The center tile.
     * @param radius The radius within which to find farm plantations.
     * @return A list of `Plantation` objects representing the farm plantations within the radius.
     */
    fun getFarmPlantationsInRadius(farmID: Int, tile: Tile, radius: Int): List<Plantation> {
        return getAllNeighborTilesInRadius(tile.id, radius)
            .filter { it.farm == farmID && it is Plantation }
            .sortedBy { it.id }
            .map { it as Plantation }
    }

    /**
     * Used to replace an existing tile with a new one.
     * Tile to be replaced is determined by the coordinate of the new tile.
     *
     * @param newTile The updated `Tile` object.
     */
    fun updateTile(newTile: Tile) {
        idToTile[newTile.id] = newTile
        coordinateToTile[newTile.coordinate] = newTile
    }

    /**
     * Calculates the shortest path length between two tiles.
     *
     * @param start The starting `Tile`.
     * @param target The target `Tile`.
     * @return The length of the shortest path as an `Int`, or `null` if no path exists.
     */
    fun shortestPathLength(
        start: Tile,
        target: Tile,
        farmID: Int?,
        hasHarvest: Boolean = false
    ): Int? {
        if (start == target) return 0

        val queue = ArrayDeque<Pair<Tile, Int>>()
        val visited = mutableSetOf<Tile>()

        queue.addLast(start to 0)
        visited.add(start)

        while (queue.isNotEmpty()) {
            val (currentTile, distance) = queue.removeFirst()

            for (neighborCoordinate in currentTile.coordinate.getAllNeighbors()) {
                val neighborTile = getTileByCoordinate(neighborCoordinate) ?: continue

                if (neighborTile in visited || !canPassThroughTile(neighborTile, farmID, hasHarvest)) continue

                if (neighborTile == target) {
                    return distance + 1
                }

                visited.add(neighborTile)
                queue.addLast(neighborTile to distance + 1)
            }
        }

        return null
    }

    /**
     * Checks if a path exists between two tiles, considering farm-specific constraints.
     *
     * @param start The starting `Tile`.
     * @param target The target `Tile`.
     * @param farmID The ID of the farm.
     * @param hasHarvest A flag indicating whether the path should consider harvest constraints.
     * @return `true` if a path exists, `false` otherwise.
     */
    fun existsPath(start: Tile, target: Tile, farmID: Int? = null, hasHarvest: Boolean = false): Boolean {
        return shortestPathLength(start, target, farmID, hasHarvest) != null
    }

    /**
     * Checks if a path exists between two tiles, considering farm-specific constraints.
     *
     * @param startID The ID of the starting `Tile`.
     * @param targetID The ID of the target `Tile`.
     * @param farmID The ID of the farm.
     * @param hasHarvest A flag indicating whether the path should consider harvest constraints.
     * @return `true` if a path exists, `false` otherwise.
     */
    fun existsPath(startID: Int, targetID: Int, farmID: Int? = null, hasHarvest: Boolean = false): Boolean {
        val start = getTileById(startID) ?: return false
        val target = getTileById(targetID) ?: return false
        return shortestPathLength(start, target, farmID, hasHarvest) != null
    }

    /**
     * Retrieves all tiles of type `Field`.
     *
     * @return A list of `Field` objects.
     */
    fun getFields(): List<Field> {
        return getAllTiles().filterIsInstance<Field>()
    }

    /**
     * Retrieves all tiles of type `Plantation`.
     *
     * @return A list of `Plantation` objects.
     */
    fun getPlantations(): List<Plantation> {
        return getAllTiles().filterIsInstance<Plantation>()
    }

    /**
     * Retrieves all forest tiles within a specified radius of a given location.
     *
     * @param location The ID of the center tile.
     * @param radius The radius within which to find forest tiles.
     * @return A list of `Tile` objects representing the forest tiles within the radius.
     */
    fun getForestsInRadius(location: Int, radius: Int): List<Tile> {
        return getAllNeighborTilesInRadius(
            location,
            radius
        ).filter { it.categoryType == TileCategory.FOREST }
    }

    /**
     * Creates a mapping of coordinates to tiles from a given map of tile IDs to tiles.
     *
     * @param idToTile A map where the key is the tile ID and the value is the corresponding `Tile` object.
     * @return A mutable map where the key is the coordinate and the value is the corresponding `Tile` object.
     */
    private fun createCoordinateToTile() {
        for (tile in idToTile.values) {
            coordinateToTile[tile.coordinate] = tile
        }
    }

    /**
     * Helper methods for `TileManager` inner logic.
     */
    companion object {

        private fun canPassThroughTile(tile: Tile, farmID: Int?, hasHarvest: Boolean) =
            when (tile.categoryType) {
                TileCategory.FOREST -> false
                TileCategory.MEADOW, TileCategory.ROAD -> true
                TileCategory.VILLAGE -> !hasHarvest
                TileCategory.FARMSTEAD, TileCategory.FIELD, TileCategory.PLANTATION -> tile.farm == farmID
            }
    }
}
