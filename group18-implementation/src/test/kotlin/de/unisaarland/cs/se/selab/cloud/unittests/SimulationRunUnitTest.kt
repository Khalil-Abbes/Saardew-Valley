package de.unisaarland.cs.se.selab.cloud.unittests

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.cloud.RAIN_THRESHOLD
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.incident.Incident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.Simulation
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.FarmableTile
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import java.io.PrintWriter
import kotlin.test.assertNotNull

class SimulationRunUnitTest {

    @BeforeEach
    fun initLogger() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    @Test
    fun `sim run for one tick`() {
        val tm: TileManager = mock()

        val start = Mockito.mock(Tile::class.java).also {
            Mockito.`when`(it.id).thenReturn(100)
            Mockito.`when`(it.categoryType).thenReturn(TileCategory.FOREST)
        }
        val dest = Mockito.mock(FarmableTile::class.java)!!
        Mockito.`when`(dest.id).thenReturn(200)
        Mockito.`when`(dest.categoryType).thenReturn(TileCategory.FOREST)
        Mockito.`when`(dest.sunlightThisTick).thenReturn(120)

        Mockito.`when`(tm.getTileById(100)).thenReturn(start)
        Mockito.`when`(tm.getTileById(200)).thenReturn(dest)

        Mockito.`when`(tm.getNeighborInDirection(any())).thenReturn(dest)

        val clouds = mutableListOf(Cloud(id = 1, duration = 3, location = 100, amount = RAIN_THRESHOLD - 1))
        val handler = CloudHandler(tm, clouds)

        val startYearTick = YearTick.APR1
        val ticksToRun = 2

        val sim = Simulation(
            tileManager = tm,
            cloudHandler = handler,
            farms = emptyMap(),
            incidents = emptyList(),
            currentTick = 0,
            currentYearTick = YearTick.APR1,
            maxTick = 2
        )

        sim.run()

        assertEquals(
            ticksToRun,
            sim.currentTick,
            "Simulation should advance currentTick by maxTick"
        )
        var expectedYear = startYearTick
        repeat(ticksToRun) { expectedYear = expectedYear.getNext() }
        assertEquals(
            expectedYear,
            sim.currentYearTick,
            "YearTick should advance exactly maxTick steps"
        )

        // the remaining cloud ends the last processed tick with zero moves and decreased duration
        val remaining = handler.clouds.firstOrNull()
        assertNotNull(remaining) {
            "Cloud should still exist with amount below threshold"
        }
        assertEquals(
            0,
            remaining.availableMovesLeft,
            "Cloud should end the last tick with zero available moves"
        )
        assertEquals(
            1,
            remaining.duration,
            "Duration should decrement once per tick for surviving clouds"
        )
    }

    @Test
    fun `sim run with dissipating cloud removes it and still advances tick and year`() {
        val tm: TileManager = mock()

        val tile = Mockito.mock(Tile::class.java).also {
            Mockito.`when`(it.id)
                .thenReturn(500)
            Mockito.`when`(it.categoryType)
                .thenReturn(TileCategory.FOREST)
        }
        Mockito.`when`(tm.getTileById(500))
            .thenReturn(tile)

        val clouds = mutableListOf(
            Cloud(
                id = 2,
                duration = 5,
                location = 500,
                amount = RAIN_THRESHOLD + 1
            )
        )
        val handler = CloudHandler(tm, clouds)

        val farms: Map<Int, Farm> = emptyMap()
        val incidents: List<Incident> = emptyList()
        val startTick = 0
        val startYearTick = YearTick.APR1
        val ticksToRun = 3

        val sim = Simulation(
            tileManager = tm,
            cloudHandler = handler,
            farms = farms,
            incidents = incidents,
            currentTick = startTick,
            currentYearTick = startYearTick,
            maxTick = ticksToRun
        )

        sim.run()

        // sim finished ticks and advanced year
        assertEquals(
            ticksToRun,
            sim.currentTick,
            "Simulation should advance currentTick by maxTick"
        )
        var expectedYear = startYearTick
        repeat(ticksToRun) {
            expectedYear = expectedYear.getNext()
        }
        assertEquals(
            expectedYear,
            sim.currentYearTick,
            "YearTick should advance exactly maxTick steps"
        )

        assertEquals(
            0,
            handler.clouds.size,
            "Cloud list should be empty after dissipation"
        )
    }
}
