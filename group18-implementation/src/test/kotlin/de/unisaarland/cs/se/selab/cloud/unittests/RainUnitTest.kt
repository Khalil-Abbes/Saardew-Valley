package de.unisaarland.cs.se.selab.cloud.unittests

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.cloud.RAIN_THRESHOLD
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.FarmableTile
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RainUnitTest {

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    private fun invokeRain(handler: CloudHandler, cloud: Cloud): Boolean {
        val m = CloudHandler::class.java.getDeclaredMethod(
            "rain",
            Cloud::class.java
        )
        m.isAccessible = true
        return m.invoke(handler, cloud)!! as Boolean
    }

    private fun farmable(
        id: Int,
        capacity: Int,
        initial: Int
    ): Pair<FarmableTile<*>, () -> Int> {
        val t = Mockito.mock(FarmableTile::class.java)!!
        Mockito.`when`(t.id)
            .thenReturn(id)
        Mockito.`when`(t.moistureCapacity)
            .thenReturn(capacity)
        var cur = initial
        Mockito.`when`(t.currentMoisture)
            .thenAnswer { cur }
        Mockito.doAnswer { inv ->
            cur = inv.getArgument(0)
            null
        }.`when`(t).currentMoisture = Mockito.anyInt()
        return t to { cur }
    }

    @Test
    fun `no rain when amount below threshold`() {
        val tileManager: TileManager = mock()
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        val cloud = Cloud(
            id = 1,
            duration = 5,
            location = 100,
            amount = RAIN_THRESHOLD - 1
        )

        val result = invokeRain(handler, cloud)

        assertFalse(result) // early return, nothing else happens [RAIN < THRESHOLD]
        assertEquals(RAIN_THRESHOLD - 1, cloud.amount) // unchanged
    }

    @Test
    fun `tile null returns false`() {
        val tileManager: TileManager = mock()
        Mockito.`when`(tileManager.getTileById(100))
            .thenReturn(null)
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        val cloud = Cloud(
            id = 2,
            duration = 5,
            location = 100,
            amount = RAIN_THRESHOLD + 1
        )

        val result = invokeRain(handler, cloud)

        assertFalse(result) // tile lookup null branch
        assertEquals(RAIN_THRESHOLD + 1, cloud.amount) // unchanged
    }

    @Test
    fun `farmable at capacity difference==0 returns false and does not mutate`() {
        val (farm, curGetter) = farmable(
            id = 10,
            capacity = 1000,
            initial = 1000
        )
        val tileManager: TileManager = mock()
        Mockito.`when`(tileManager.getTileById(10))
            .thenReturn(farm)
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        val cloud = Cloud(
            id = 3,
            duration = 5,
            location = 10,
            amount = RAIN_THRESHOLD + 500
        )

        val result = invokeRain(handler, cloud)

        assertFalse(result) // fillTile path with amount==0
        assertEquals(1000, curGetter()) // unchanged
        assertEquals(RAIN_THRESHOLD + 500, cloud.amount) // unchanged
    }

    @Test
    fun `farmable partial fill difference less than amount returns false and reduces cloud amount`() {
        // capacity=1000, current=900, difference=100; cloud.amount big enough
        val (farm, curGetter) = farmable(id = 11, capacity = 1000, initial = 900)
        val tileManager: TileManager = mock()
        Mockito.`when`(tileManager.getTileById(11))
            .thenReturn(farm)
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        val cloud = Cloud(
            id = 4,
            duration = 5,
            location = 11,
            amount = RAIN_THRESHOLD + 200
        ) // 5200 if threshold=5000

        val result = invokeRain(handler, cloud)

        assertFalse(result) // rainToFillTile with amount=100
        assertEquals(1000, curGetter()) // filled to capacity
        assertEquals(RAIN_THRESHOLD + 200 - 100, cloud.amount) // reduced by difference
    }

    @Test
    fun `farmable equal difference equals amount dissipates and returns true`() {
        // capacity-current == cloud.amount
        val amount = RAIN_THRESHOLD + 1000 // ensure >= threshold
        val (farm, curGetter) = farmable(
            id = 12,
            capacity = 7000,
            initial = 7000 - amount
        ) // difference == amount
        val tileManager: TileManager = mock()
        Mockito.`when`(tileManager.getTileById(12))
            .thenReturn(farm)
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        val cloud = Cloud(
            id = 5,
            duration = 5,
            location = 12,
            amount = amount
        )

        val result = invokeRain(handler, cloud)

        assertTrue(result) // rainToEmptyCloudOnFarmable → dissipate
        assertEquals(7000, curGetter()) // reached capacity exactly
        assertEquals(amount, RAIN_THRESHOLD + 1000) // boundary sanity
    }

    @Test
    fun `farmable difference greater than amount dissipates below capacity and returns true`() {
        // difference > cloud.amount
        val (farm, curGetter) = farmable(
            id = 13,
            capacity = 10000,
            initial = 1000
        )
        val tileManager: TileManager = mock()
        Mockito.`when`(tileManager.getTileById(13))
            .thenReturn(farm)
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        val cloud = Cloud(
            id = 6,
            duration = 5,
            location = 13,
            amount = RAIN_THRESHOLD + 200
        ) // far less than difference 9000

        val result = invokeRain(handler, cloud)

        assertTrue(result) // rainToEmptyCloudOnFarmable
        assertEquals(1000 + (RAIN_THRESHOLD + 200), curGetter()) // increased but still < capacity
    }

    @Test
    fun `non farmable tile dissipates entire cloud and returns true`() {
        val nonFarmTile = Mockito.mock(
            Tile::class.java
        ).also {
            Mockito.`when`(it.id).thenReturn(20)
        }
        val tileManager: TileManager = mock()
        Mockito.`when`(tileManager.getTileById(20))
            .thenReturn(nonFarmTile)
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        val cloud = Cloud(
            id = 7,
            duration = 5,
            location = 20,
            amount = RAIN_THRESHOLD + 123
        )

        val result = invokeRain(handler, cloud)

        assertTrue(result) // rainToEmptyCloud on non-farmable
        assertEquals(RAIN_THRESHOLD + 123, cloud.amount) // unchanged by rainToEmptyCloud; removal handled elsewhere
    }

    @Test
    fun `boundary amount equals threshold with farmable partial fill returns false`() {
        // difference < amount (==threshold), to exercise equality boundary on first guard (<)
        val (farm, curGetter) = farmable(
            id = 14,
            capacity = 10000,
            initial = 9500
        ) // diff=500
        val tileManager: TileManager = mock()
        Mockito.`when`(tileManager.getTileById(14))
            .thenReturn(farm)
        val clouds = mutableListOf<Cloud>()
        val handler = CloudHandler(tileManager, clouds)
        val cloud = Cloud(
            id = 8,
            duration = 5,
            location = 14,
            amount = RAIN_THRESHOLD
        ) // exactly threshold

        val result = invokeRain(handler, cloud)

        assertFalse(result) // enters rain path; diff < amount → fill-to-capacity branch
        assertEquals(10000, curGetter()) // filled to capacity
        assertEquals(RAIN_THRESHOLD - 500, cloud.amount) // reduced by difference
    }
}
