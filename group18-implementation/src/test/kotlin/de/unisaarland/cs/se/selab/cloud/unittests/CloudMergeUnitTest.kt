package de.unisaarland.cs.se.selab.cloud.unittests

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.TileManager
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CloudMergeUnitTest {

    val clouds = mutableListOf<Cloud>()

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    @Test
    fun `test checkCollision returns null when no collision occurs`() {
        // Arrange
        val tileManager: TileManager = mock()
        val cloud1 = Cloud(
            1,
            10,
            5,
            500
        )
        val cloud2 = Cloud(
            2,
            15,
            10,
            700
        )
        val clouds = mutableListOf(cloud1, cloud2)
        val cloudHandler = CloudHandler(tileManager, clouds)
        val testCloud = Cloud(
            3,
            20,
            15,
            300
        )

        // Act
        val collision = cloudHandler.checkCollision(testCloud)

        // Assert
        assertNull(collision)
    }

    @Test
    fun `test checkCollision returns colliding cloud when collision occurs`() {
        // Arrange
        val tileManager: TileManager = mock()
        val cloud1 = Cloud(
            1,
            10,
            5,
            500
        )
        val cloud2 = Cloud(
            2,
            15,
            10,
            700
        )
        val clouds = mutableListOf(cloud1, cloud2)
        val cloudHandler = CloudHandler(tileManager, clouds)
        val testCloud = Cloud(3, 20, 10, 300)

        // Act
        val collision = cloudHandler.checkCollision(testCloud)

        // Assert
        assertNotNull(collision)
        assertEquals(cloud2, collision)
    }

    @Test
    fun `test checkCollision does not match with itself`() {
        // Arrange
        val tileManager: TileManager = mock()
        val cloud1 = Cloud(
            1,
            10,
            5,
            500
        )
        val clouds = mutableListOf(cloud1)
        val cloudHandler = CloudHandler(tileManager, clouds)

        // Act
        val collision = cloudHandler.checkCollision(cloud1)

        // Assert
        assertNull(collision)
    }

    private fun invokeMerge(handler: CloudHandler, a: Cloud, b: Cloud) {
        val m = CloudHandler::class.java.getDeclaredMethod(
            "merge",
            Cloud::class.java,
            Cloud::class.java
        )
        m.isAccessible = true
        m.invoke(handler, a, b)
    }

    private fun setHighestId(handler: CloudHandler, value: Int) {
        val f = CloudHandler::class.java.getDeclaredField("highestID")
        f.isAccessible = true
        f.setInt(handler, value)
    }

    @Test
    fun `merge sum without overflow, finite durations, min duration, sum amount, max moves, id and removals`() {
        // Arrange
        val tileManager: TileManager = mock()
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)

        // Force next new merged id = 42
        setHighestId(handler, 42)

        val colliding = Cloud(
            id = 1,
            duration = 8,
            location = 10,
            amount = 100
        )
            .also { it.availableMovesLeft = 3 }
        val moving = Cloud(
            id = 2,
            duration = 5,
            location = 10,
            amount = 200
        )
            .also { it.availableMovesLeft = 7 }
        clouds += listOf(colliding, moving)

        // Act
        invokeMerge(handler, colliding, moving)

        // Assert
        assertEquals(1, clouds.size) // only new merged cloud
        val merged = clouds.single()
        assertEquals(42, merged.id) // took current highestID
        assertEquals(300, merged.amount) // 100 + 200
        assertEquals(5, merged.duration) // min(8,5) for both finite
        assertEquals(7, merged.availableMovesLeft) // max(3,7)
        assertEquals(10, merged.location) // from colliding cloud
    }

    @Test
    fun `merge amount overflow - clamp to Int_MAX_VALUE`() {
        // Arrange
        val tileManager: TileManager = mock()
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        setHighestId(handler, 100)

        val colliding = Cloud(
            id = 11,
            duration = 10,
            location = 5,
            amount = Int.MAX_VALUE - 10
        ).also { it.availableMovesLeft = 1 }
        val moving = Cloud(
            id = 12,
            duration = 12,
            location = 5,
            amount = 20
        ).also { it.availableMovesLeft = 2 }
        clouds += listOf(colliding, moving)

        // Act
        invokeMerge(handler, colliding, moving)

        // Assert
        val merged = clouds.single()
        assertEquals(Int.MAX_VALUE, merged.amount) // clamped at max
        assertEquals(10, merged.duration) // both finite -> min(10,12) = 10
        assertEquals(2, merged.availableMovesLeft) // max(1,2)
        assertEquals(5, merged.location)
        assertEquals(100, merged.id)
    }

    @Test
    fun `merge with one duration -1 - choose max(colliding,moving) which is the finite value`() {
        // Arrange
        val tileManager: TileManager = mock()
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        setHighestId(handler, 7)

        val colliding = Cloud(
            id = 21,
            duration = -1,
            location = 9,
            amount = 50
        ).also { it.availableMovesLeft = 9 }
        val moving = Cloud(
            id = 22,
            duration = 7,
            location = 9,
            amount = 75
        ).also { it.availableMovesLeft = 4 }
        clouds += listOf(colliding, moving)

        // Act
        invokeMerge(handler, colliding, moving)

        // Assert
        val merged = clouds.single()
        assertEquals(125, merged.amount) // 50 + 75
        assertEquals(7, merged.duration) // max(-1,7) == 7 by implementation
        assertEquals(9, merged.availableMovesLeft) // max(9,4)
        assertEquals(9, merged.location)
        assertEquals(7, merged.id)
    }

    @Test
    fun `merge with both durations -1 - result duration -1`() {
        // Arrange
        val tileManager: TileManager = mock()
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        setHighestId(handler, 5)

        val colliding = Cloud(
            id = 31,
            duration = -1,
            location = 42,
            amount = 10
        ).also { it.availableMovesLeft = 0 }
        val moving = Cloud(
            id = 32,
            duration = -1,
            location = 99,
            amount = 15
        ).also { it.availableMovesLeft = 3 }
        clouds += listOf(colliding, moving)

        invokeMerge(handler, colliding, moving)

        val merged = clouds.single()
        assertEquals(25, merged.amount) // 10 + 15
        assertEquals(-1, merged.duration) // both -1 -> max(-1,-1) == -1
        assertEquals(3, merged.availableMovesLeft) // max(0,3)
        assertEquals(42, merged.location) // from colliding
        assertEquals(5, merged.id)
    }

    @Test
    fun `merge removes sources and increments highestID`() {
        // Arrange
        val tileManager: TileManager = mock()
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        setHighestId(handler, 2)

        val colliding = Cloud(
            id = 1,
            duration = 3,
            location = 1,
            amount = 1
        )
        val moving = Cloud(
            id = 9,
            duration = 2,
            location = 1,
            amount = 2
        )
        clouds += listOf(colliding, moving)

        invokeMerge(handler, colliding, moving)

        // After merge, highestID should be incremented internally;
        // the newly created cloud has id=2 and highestID becomes 3.
        val merged = clouds.single()
        assertEquals(2, merged.id)
        // Ensure neither source cloud remains
        assertTrue(clouds.none { it.id == 1 || it.id == 9 })
    }
}
