package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.tile.Direction

/**
 * coordinate class
 */
data class Coordinate(val x: Int, val y: Int) {
    init {
        require(
            x.mod(2) == y.mod(2)
        ) { "Coordinate must contain either two even or two odd numbers" }
    }

    val isOctagonal: Boolean get() = x % 2 == 0

    /**
     * returns neighboring coordinates based on a direction
     */
    fun getNeighborCoordinateInDirection(direction: Direction): Coordinate? {
        if (isOctagonal) {
            return when (direction) {
                Direction.ANGLE_0 -> Coordinate(x, y - 2)
                Direction.ANGLE_45 -> Coordinate(x + 1, y - 1)
                Direction.ANGLE_90 -> Coordinate(x + 2, y)
                Direction.ANGLE_135 -> Coordinate(x + 1, y + 1)
                Direction.ANGLE_180 -> Coordinate(x, y + 2)
                Direction.ANGLE_225 -> Coordinate(x - 1, y + 1)
                Direction.ANGLE_270 -> Coordinate(x - 2, y)
                Direction.ANGLE_315 -> Coordinate(x - 1, y - 1)
            }
        }

        // square
        return when (direction) {
            Direction.ANGLE_45 -> Coordinate(x + 1, y - 1)
            Direction.ANGLE_135 -> Coordinate(x + 1, y + 1)
            Direction.ANGLE_225 -> Coordinate(x - 1, y + 1)
            Direction.ANGLE_315 -> Coordinate(x - 1, y - 1)
            else -> null
        }
    }

    /**
     * Returns a list of all coordinates within a given radius from this coordinate, including itself.
     *
     * @param radius radius
     * @return a list of coordinates within the specified radius
     */
    fun getCoordinatesWithinRadius(radius: Int): List<Coordinate> {
        var previousStepCoordinates = listOf(this)
        val processedCoordinates = mutableSetOf(this)
        repeat(radius) {
            val currentStepTiles = mutableListOf<Coordinate>()

            previousStepCoordinates.forEach { previousCoordinate ->
                for (newCoordinate in previousCoordinate.getAllNeighbors()) {
                    if (newCoordinate in processedCoordinates) continue

                    currentStepTiles.add(newCoordinate)
                    processedCoordinates.add(newCoordinate)
                }
            }
            previousStepCoordinates = currentStepTiles
        }
        return processedCoordinates.toList()
    }

    /**
     * returns all neighboring coordinates in the form of list
     */
    fun getAllNeighbors(): List<Coordinate> {
        return Direction.entries.mapNotNull { getNeighborCoordinateInDirection(it) }
    }

    override fun equals(other: Any?): Boolean {
        if (other is Coordinate) {
            return this.x == other.x && this.y == other.y
        }
        return false
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}
