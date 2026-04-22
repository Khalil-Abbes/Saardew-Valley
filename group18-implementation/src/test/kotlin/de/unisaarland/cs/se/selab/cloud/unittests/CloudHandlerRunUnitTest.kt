package de.unisaarland.cs.se.selab.cloud.unittests

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.cloud.RAIN_THRESHOLD
import de.unisaarland.cs.se.selab.cloud.REDUCE_SUNLIGHT_CLOUDY_TILES
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.FarmableTile
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import java.io.ByteArrayOutputStream
import java.io.PrintWriter
import kotlin.math.max
import kotlin.test.assertEquals

class CloudHandlerRunUnitTest {

    private lateinit var tileManager: TileManager
    private lateinit var handler: CloudHandler
    private val clouds = mutableListOf<Cloud>()

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(ByteArrayOutputStream()))
        tileManager = mock()
        handler = CloudHandler(tileManager, clouds)
    }

    @Test
    fun `cloud moves 10 times`() {
        val cloud = Cloud(
            id = 1,
            duration = 3,
            location = 100,
            amount = RAIN_THRESHOLD - 1
        )
        clouds += cloud

        // Start tile (non-farmable, FOREST) and a neighbor tile (farmable) returned for any starting point
        val start = Mockito.mock(Tile::class.java).also {
            Mockito.`when`(it.id)
                .thenReturn(100)
            Mockito.`when`(it.categoryType)
                .thenReturn(TileCategory.FOREST)
        }
        val dest = Mockito.mock(FarmableTile::class.java)!!
        Mockito.`when`(dest.id)
            .thenReturn(200)
        Mockito.`when`(dest.categoryType)
            .thenReturn(TileCategory.FOREST)
        Mockito.`when`(dest.sunlightThisTick)
            .thenReturn(120)

        // getTileById respects current cloud.location
        Mockito.`when`(
            tileManager.getTileById(100)
        ).thenReturn(start)
        Mockito.`when`(
            tileManager.getTileById(200)
        ).thenReturn(dest)

        // Always return dest as the neighbor to guarantee movement
        Mockito.`when`(tileManager.getNeighborInDirection(any())).thenReturn(dest)

        // Act
        handler.run()

        // moves reset then consumed down to 0 by recursive handleCloud
        assertEquals(
            0,
            clouds.first().availableMovesLeft,
            "All available moves should be consumed in one run"
        )
        // duration decremented by 1 (not infinite and still present)
        assertEquals(
            2,
            clouds.first().duration,
            "Duration should decrement by 1 for surviving clouds"
        )
        // final per-tile sunlight reduction on farmable tile
        Mockito.verify(
            dest
        ).sunlightThisTick = max(120 - REDUCE_SUNLIGHT_CLOUDY_TILES, 0)
        // cloud ended at the destination id at least once
        assertEquals(
            200,
            clouds.first().location,
            "Cloud should have moved to the neighbor tile"
        )
    }
}
