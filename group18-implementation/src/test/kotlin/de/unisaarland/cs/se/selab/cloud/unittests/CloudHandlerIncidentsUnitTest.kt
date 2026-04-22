package de.unisaarland.cs.se.selab.cloud.unittests

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.cloud.REDUCE_SUNLIGHT_CLOUDY_TILES
import de.unisaarland.cs.se.selab.cloud.REDUCE_SUNLIGHT_PER_TRAVERSE
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.FarmableTile
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.PrintWriter
import kotlin.math.max

class CloudHandlerIncidentsUnitTest {

    private lateinit var tileManager: TileManager
    private lateinit var cloudHandler: CloudHandler
    val clouds = mutableListOf<Cloud>()

    @BeforeEach
    fun setup() {
        tileManager = Mockito.mock(TileManager::class.java)
        cloudHandler = CloudHandler(tileManager, clouds)
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    /**
     * Verifies that sunlight is reduced on farmable tiles when clouds move.
     */
    @Test
    fun `test reduce sunlight after cloud movement`() {
        val farmableTile = Mockito.mock(FarmableTile::class.java)
        Mockito.`when`(
            tileManager
                .getTileById(100)
        ).thenReturn(farmableTile)
        Mockito.`when`(
            tileManager
                .getNeighborInDirection(farmableTile)
        )
            .thenReturn(Mockito.mock(Tile::class.java))
        Mockito.`when`(
            farmableTile
                .sunlightThisTick
        ).thenReturn(10)

        val cloud = Cloud(
            1,
            5,
            100,
            200
        )
        clouds.add(cloud)

        cloudHandler.run()

        Mockito.verify(farmableTile)
            .sunlightThisTick = max(
            10 - REDUCE_SUNLIGHT_PER_TRAVERSE, 0
        )
    }

    /**
     * Tests that a new cloud is created and appropriately gets added to the cloud list.
     */
    @Test
    fun `test create new cloud`() {
        cloudHandler.createCloud(location = 100, duration = 5, amount = 200)

        Assertions.assertEquals(
            1,
            clouds.size
        )
        Assertions.assertEquals(
            100,
            clouds[0].location
        )
        Assertions.assertEquals(
            5,
            clouds[0].duration
        )
        Assertions.assertEquals(
            200,
            clouds[0].amount
        )
    }

    /**
     * Tests that 2 new clouds are created and appropriately get added to the cloud list.
     */
    @Test
    fun `test create 2 new clouds`() {
        cloudHandler.createCloud(
            location = 100,
            duration = 5,
            amount = 200
        )
        cloudHandler.createCloud(
            location = 100,
            duration = 2,
            amount = 200
        )

        Assertions.assertEquals(
            1,
            clouds.size
        )
        Assertions.assertEquals(
            100,
            clouds[0].location
        )
        Assertions.assertEquals(
            2,
            clouds[0].duration
        )
        Assertions.assertEquals(
            400,
            clouds[0].amount
        )
    }

    /**
     * Tests that sunlight reduction happens after all cloud movements are completed.
     */
    @Test
    fun `test reduce sunlight after all movements`() {
        val farmableTile = Mockito.mock(FarmableTile::class.java)
        Mockito.`when`(
            tileManager
                .getTileById(100)
        ).thenReturn(farmableTile)
        Mockito.`when`(
            farmableTile
                .sunlightThisTick
        ).thenReturn(120)

        val cloud = Cloud(1, 5, 100, 200)
        clouds.add(cloud)

        cloudHandler.run()

        Mockito.verify(farmableTile)
            .sunlightThisTick = max(
            120 - REDUCE_SUNLIGHT_CLOUDY_TILES, 0
        )
    }

    @Test
    fun `test handle city expansion with existing cloud`() {
        val tile = Mockito.mock(Tile::class.java)
        Mockito.`when`(tile.id)
            .thenReturn(100)

        val cloud = Cloud(
            1,
            5,
            100,
            200
        )
        clouds.add(cloud)

        cloudHandler.handleCityExpansion(tile)

        Assertions.assertEquals(0, clouds.size)
    }
}
