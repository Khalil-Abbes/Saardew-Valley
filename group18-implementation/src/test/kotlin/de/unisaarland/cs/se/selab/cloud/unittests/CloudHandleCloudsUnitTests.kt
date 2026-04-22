package de.unisaarland.cs.se.selab.cloud.unittests

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.cloud.RAIN_THRESHOLD
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import java.io.PrintWriter
import kotlin.test.assertEquals

class CloudHandleCloudsUnitTests {

    private lateinit var tileManager: TileManager
    private val clouds = mutableListOf<Cloud>()
    private lateinit var handler: CloudHandler

    @BeforeEach
    fun setup() {
        tileManager = mock()
        handler = CloudHandler(tileManager, clouds)
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    private fun invokeHandleClouds(processedClouds: MutableList<Cloud>) {
        val m = CloudHandler::class.java.getDeclaredMethod(
            "handleClouds",
            MutableList::class.java
        )
        m.isAccessible = true
        m.invoke(handler, processedClouds)
    }

    @Test
    fun `tile null bypasses VILLAGE check and proceeds`() {
        Mockito.`when`(
            tileManager.getTileById(700)
        ).thenReturn(null)

        val cloud = Cloud(
            id = 7,
            duration = 2,
            location = 700,
            amount = RAIN_THRESHOLD - 1
        ) // won’t dissipate
        clouds.add(cloud)

        val processedClouds = mutableListOf<Cloud>()
        invokeHandleClouds(processedClouds)

        assertEquals(1, clouds.size) // remains
        assertEquals(1, cloud.duration) // decremented
        assertEquals(1, processedClouds.size)
        assertEquals(7, processedClouds[0].id)
    }

    @Test
    fun `empty worklist terminates recursion`() {
        val processed = mutableListOf<Cloud>()
        invokeHandleClouds(processed)
        assertEquals(0, clouds.size)
        assertEquals(0, processed.size)
    }

    @Test
    fun `cloud on VILLAGE is removed before handling and recursion continues`() {
        val village = mock<Tile>().also {
            Mockito.`when`(it.categoryType)
                .thenReturn(TileCategory.VILLAGE)
            Mockito.`when`(it.id)
                .thenReturn(100)
        }
        Mockito.`when`(tileManager.getTileById(100)).thenReturn(village)
        val forest = mock<Tile>().also {
            Mockito.`when`(it.categoryType)
                .thenReturn(TileCategory.FOREST)
            Mockito.`when`(it.id)
                .thenReturn(200)
        }
        Mockito.`when`(
            tileManager
                .getTileById(200)
        ).thenReturn(forest)
        val c1 = Cloud(
            id = 1,
            duration = 5,
            location = 100,
            amount = 1000
        )
        val c2 = Cloud(
            id = 2,
            duration = 3,
            location = 200,
            amount = 500
        )
        clouds += listOf(c1, c2)
        val processed = mutableListOf<Cloud>()
        invokeHandleClouds(processed)
        assertEquals(1, clouds.size)
        assertEquals(2, clouds[0].id)
    }

    @Test
    fun `duration equals 1 after handling is removed post processing`() {
        val forest = mock<Tile>().also {
            Mockito.`when`(it.categoryType)
                .thenReturn(TileCategory.FOREST)
            Mockito.`when`(it.id)
                .thenReturn(300)
        }
        Mockito.`when`(
            tileManager
                .getTileById(300)
        ).thenReturn(forest)
        val c = Cloud(
            id = 3,
            duration = 1,
            location = 300,
            amount = RAIN_THRESHOLD - 1
        )
        clouds += c
        val processed = mutableListOf<Cloud>()
        invokeHandleClouds(processed)
        assertEquals(0, clouds.size)
    }

    @Test
    fun `finite duration greater than 1 is decremented when still present`() {
        val forest = mock<Tile>().also {
            Mockito.`when`(it.categoryType)
                .thenReturn(TileCategory.FOREST)
            Mockito.`when`(it.id)
                .thenReturn(400)
        }
        Mockito.`when`(tileManager.getTileById(400)).thenReturn(forest)
        val c = Cloud(id = 4, duration = 5, location = 400, amount = RAIN_THRESHOLD - 1)
        clouds += c
        val processed = mutableListOf<Cloud>()
        invokeHandleClouds(processed)
        assertEquals(1, clouds.size)
        assertEquals(4, c.duration)
    }

    @Test
    fun `infinite duration minus one remains unchanged when still present`() {
        val forest = mock<Tile>().also {
            Mockito.`when`(it.categoryType)
                .thenReturn(TileCategory.FOREST)
            Mockito.`when`(it.id)
                .thenReturn(500)
        }
        Mockito.`when`(tileManager.getTileById(500)).thenReturn(forest)
        val c = Cloud(
            id = 5,
            duration = -1,
            location = 500,
            amount = RAIN_THRESHOLD - 1
        )
        clouds += c
        val processed = mutableListOf<Cloud>()
        invokeHandleClouds(processed)
        assertEquals(1, clouds.size)
        assertEquals(-1, c.duration)
    }

    @Test
    fun `cloud removed during handleCloud by rain dissipation skips post duration logic`() {
        val nonFarm = mock<Tile>().also {
            Mockito.`when`(it.categoryType)
                .thenReturn(TileCategory.FOREST)
            Mockito.`when`(it.id)
                .thenReturn(600)
        }
        Mockito.`when`(tileManager.getTileById(600))
            .thenReturn(nonFarm)
        val c = Cloud(
            id = 6,
            duration = 3,
            location = 600,
            amount = RAIN_THRESHOLD + 1
        )
        clouds += c
        val processed = mutableListOf<Cloud>()
        invokeHandleClouds(processed)
        assertEquals(0, clouds.size)
    }
}
