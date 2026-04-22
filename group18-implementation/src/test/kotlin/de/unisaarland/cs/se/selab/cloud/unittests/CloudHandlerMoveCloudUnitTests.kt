package de.unisaarland.cs.se.selab.cloud.unittests

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.cloud.REDUCE_SUNLIGHT_PER_TRAVERSE
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.FarmableTile
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.PrintWriter
import kotlin.math.max

class CloudHandlerMoveCloudUnitTests {

    private lateinit var tileManager: TileManager
    private lateinit var handler: CloudHandler
    private val clouds = mutableListOf<Cloud>()

    @BeforeEach
    fun setup() {
        tileManager = Mockito.mock(TileManager::class.java)
        handler = CloudHandler(tileManager, clouds)
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    // Helper to call the private moveCloud(Cloud): Boolean via reflection
    private fun invokeMoveCloud(target: CloudHandler, cloud: Cloud): Boolean {
        val m = CloudHandler::class.java.getDeclaredMethod(
            "moveCloud",
            Cloud::class.java
        )
        m.isAccessible = true
        return m.invoke(target, cloud)!! as Boolean
    }

    @Test
    fun `start tile null returns false`() {
        val cloud = Cloud(
            id = 1,
            duration = 5,
            location = 100,
            amount = 200
        )
        clouds.add(cloud)
        Mockito.`when`(
            tileManager
                .getTileById(100)
        ).thenReturn(null)

        val moved = invokeMoveCloud(handler, cloud)

        assertFalse(moved)
        assertEquals(1, clouds.size) // not removed
        assertEquals(100, cloud.location) // unchanged
    }

    @Test
    fun `neighbor null returns false`() {
        val start = Mockito.mock(Tile::class.java)
        Mockito.`when`(start.id)
            .thenReturn(100)
        val cloud = Cloud(
            id = 2,
            duration = 5,
            location = 100,
            amount = 200
        )
        clouds.add(cloud)
        Mockito.`when`(
            tileManager.getTileById(100)
        ).thenReturn(start)
        Mockito.`when`(
            tileManager.getNeighborInDirection(start)
        ).thenReturn(null)

        val moved = invokeMoveCloud(handler, cloud)

        assertFalse(moved)
        assertEquals(1, clouds.size) // not removed
        assertEquals(100, cloud.location) // unchanged
    }

    @Test
    fun `start farmable sunlight reduced, non-village destination returns true`() {
        val start = Mockito.mock(FarmableTile::class.java)
        Mockito.`when`(start.id)
            .thenReturn(100)
        Mockito.`when`(
            start
                .sunlightThisTick
        ).thenReturn(10)

        val destination = Mockito.mock(Tile::class.java)
        Mockito.`when`(destination.id)
            .thenReturn(101)
        Mockito.`when`(destination.categoryType)
            .thenReturn(TileCategory.FOREST) // any non-VILLAGE

        val cloud = Cloud(id = 3, duration = 5, location = 100, amount = 200)
        clouds.add(cloud)
        Mockito.`when`(tileManager.getTileById(100))
            .thenReturn(start)
        Mockito.`when`(tileManager.getNeighborInDirection(start))
            .thenReturn(destination)

        val moved = invokeMoveCloud(handler, cloud)

        assertTrue(moved)
        assertEquals(101, cloud.location)
        Mockito.verify(start)
            .sunlightThisTick = max(
            10 - REDUCE_SUNLIGHT_PER_TRAVERSE, 0
        )
    }

    @Test
    fun `destination is VILLAGE, cloud removed, returns false`() {
        val start = Mockito.mock(Tile::class.java)
        Mockito
            .`when`(start.id)
            .thenReturn(200)

        val destination = Mockito.mock(Tile::class.java)
        Mockito.`when`(destination.id)
            .thenReturn(201)
        Mockito.`when`(destination.categoryType)
            .thenReturn(TileCategory.VILLAGE)

        val cloud = Cloud(
            id = 4,
            duration = 5,
            location = 200,
            amount = 300
        )
        clouds.add(cloud)
        Mockito.`when`(tileManager.getTileById(200))
            .thenReturn(start)
        Mockito.`when`(tileManager.getNeighborInDirection(start))
            .thenReturn(destination)

        val moved = invokeMoveCloud(handler, cloud)

        assertFalse(moved)
        assertEquals(0, clouds.size) // removed from list
        assertEquals(201, cloud.location) // updated before removal (as per implementation)
    }

    @Test
    fun `normal move non-farmable start, non-VILLAGE destination, returns true and updates location`() {
        val start = Mockito.mock(Tile::class.java)
        Mockito.`when`(start.id).thenReturn(300)

        val destination = Mockito.mock(Tile::class.java)
        Mockito.`when`(destination.id)
            .thenReturn(301)
        Mockito.`when`(destination.categoryType)
            .thenReturn(TileCategory.FOREST) // any non-VILLAGE

        val cloud = Cloud(
            id = 5,
            duration = 7,
            location = 300,
            amount = 1234
        )
        clouds.add(cloud)
        Mockito.`when`(tileManager.getTileById(300))
            .thenReturn(start)
        Mockito.`when`(tileManager.getNeighborInDirection(start))
            .thenReturn(destination)

        val moved = invokeMoveCloud(handler, cloud)

        assertTrue(moved)
        assertEquals(301, cloud.location)
        assertEquals(1, clouds.size) // still present
    }
}
