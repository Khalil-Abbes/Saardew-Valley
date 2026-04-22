package test.kotlin.de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.tile.Direction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CoordinateTest {
    @Test
    fun getNeighborCoordinateInDirectionOctagonal() {
        val center = Coordinate(0, 0)

        assertEquals(Coordinate(0, -2), center.getNeighborCoordinateInDirection(Direction.ANGLE_0))
        assertEquals(Coordinate(1, -1), center.getNeighborCoordinateInDirection(Direction.ANGLE_45))
        assertEquals(Coordinate(2, 0), center.getNeighborCoordinateInDirection(Direction.ANGLE_90))
        assertEquals(Coordinate(1, 1), center.getNeighborCoordinateInDirection(Direction.ANGLE_135))
        assertEquals(Coordinate(0, 2), center.getNeighborCoordinateInDirection(Direction.ANGLE_180))
        assertEquals(
            Coordinate(-1, 1),
            center.getNeighborCoordinateInDirection(Direction.ANGLE_225)
        )
        assertEquals(
            Coordinate(-2, 0),
            center.getNeighborCoordinateInDirection(Direction.ANGLE_270)
        )
        assertEquals(
            Coordinate(-1, -1),
            center.getNeighborCoordinateInDirection(Direction.ANGLE_315)
        )
    }

    @Test
    fun getNeighborCoordinateInDirectionSquare() {
        val center = Coordinate(3, 5)

        assertNull(center.getNeighborCoordinateInDirection(Direction.ANGLE_0))
        assertEquals(Coordinate(4, 4), center.getNeighborCoordinateInDirection(Direction.ANGLE_45))
        assertNull(center.getNeighborCoordinateInDirection(Direction.ANGLE_90))
        assertEquals(Coordinate(4, 6), center.getNeighborCoordinateInDirection(Direction.ANGLE_135))
        assertNull(center.getNeighborCoordinateInDirection(Direction.ANGLE_180))
        assertEquals(
            Coordinate(2, 6),
            center.getNeighborCoordinateInDirection(Direction.ANGLE_225)
        )
        assertNull(center.getNeighborCoordinateInDirection(Direction.ANGLE_270))
        assertEquals(
            Coordinate(2, 4),
            center.getNeighborCoordinateInDirection(Direction.ANGLE_315)
        )
    }

    @Test
    fun `radius 0 should return only center for octagonal`() {
        val center = Coordinate(0, 0)
        val result = center.getCoordinatesWithinRadius(0)

        assertEquals(1, result.size)
        assertTrue(result.contains(center))
    }

    @Test
    fun `radius 0 should return only center for square`() {
        val center = Coordinate(1, 5)
        val result = center.getCoordinatesWithinRadius(0)

        assertEquals(1, result.size)
        assertTrue(result.contains(center))
    }

    @Test
    fun `radius 1 for octagonal should return center plus all direct neighbors`() {
        val center = Coordinate(0, 0)
        val result = center.getCoordinatesWithinRadius(1)

        val expected = setOf(center) + center.getAllNeighbors()
        assertEquals(expected.size, result.size)
        assertTrue(result.containsAll(expected))
    }

    @Test
    fun `radius 1 for octagonal center should return center plus its 8 direct neighbors`() {
        val center = Coordinate(0, 0)
        val result = center.getCoordinatesWithinRadius(1).toSet()

        val expected = setOf(
            Coordinate(0, 0), // center
            Coordinate(0, -2), // north
            Coordinate(1, -1), // northeast
            Coordinate(2, 0), // east
            Coordinate(1, 1), // southeast
            Coordinate(0, 2), // south
            Coordinate(-1, 1), // southwest
            Coordinate(-2, 0), // west
            Coordinate(-1, -1) // northwest
        )

        assertEquals(expected, result)
    }

    @Test
    fun `radius 1 for square center should return center plus its 4 direct neighbors`() {
        val center = Coordinate(3, 5)
        val result = center.getCoordinatesWithinRadius(1).toSet()

        val expected = setOf(
            center,
            Coordinate(2, 4),
            Coordinate(4, 4),
            Coordinate(4, 6),
            Coordinate(2, 6)
        )

        assertEquals(expected, result)
    }

    @Test
    fun `radius 2 for square center`() {
        val center = Coordinate(3, 5)

        val result = center.getCoordinatesWithinRadius(2).toSet()

        val expected = center.getCoordinatesWithinRadius(1).toMutableSet()
        center.getCoordinatesWithinRadius(1).forEach {
            expected += it.getCoordinatesWithinRadius(1)
        }

        assertEquals(expected, result.toSet())
    }

    @Test
    fun `radius 2 for octagonal center should include radius 1 and neighbors of neighbors`() {
        val center = Coordinate(0, 0)
        val radius1 = center.getCoordinatesWithinRadius(1)
        val radius2 = center.getCoordinatesWithinRadius(2)

        // radius 2 must be larger than radius 1
        assertTrue(radius2.size > radius1.size)

        // radius 2 must include everything from radius 1
        assertTrue(radius2.containsAll(radius1))

        // center must always be included
        assertTrue(radius2.contains(center))
    }

    @Test
    fun `radius 2 for octagonal center should return all 25 coordinates`() {
        val center = Coordinate(0, 0)
        val result = center.getCoordinatesWithinRadius(2).toSet()

        val expected = setOf(
            Coordinate(0, 0),
            // radius 1 neighbors
            Coordinate(0, -2), Coordinate(1, -1), Coordinate(2, 0), Coordinate(1, 1),
            Coordinate(0, 2), Coordinate(-1, 1), Coordinate(-2, 0), Coordinate(-1, -1),
            // radius 2 new ones
            Coordinate(0, -4), Coordinate(1, -3), Coordinate(2, -2), Coordinate(3, -1),
            Coordinate(4, 0), Coordinate(3, 1), Coordinate(2, 2), Coordinate(1, 3),
            Coordinate(0, 4), Coordinate(-1, 3), Coordinate(-2, 2), Coordinate(-3, 1),
            Coordinate(-4, 0), Coordinate(-3, -1), Coordinate(-2, -2), Coordinate(-1, -3)
        )

        assertEquals(expected, result)
    }
}
