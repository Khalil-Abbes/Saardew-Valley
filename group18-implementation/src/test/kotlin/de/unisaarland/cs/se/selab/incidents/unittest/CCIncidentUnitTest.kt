package de.unisaarland.cs.se.selab.incidents.unittest

import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.incident.CloudCreationIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.io.PrintWriter

class CCIncidentUnitTest {

    private lateinit var tm: TileManager
    private lateinit var cloudHandler: CloudHandler

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        tm = mock()
        // real instance so we can spy on createCloud()
        cloudHandler = CloudHandler(tm, mutableListOf())
    }

    @Disabled("")
    @Test
    fun `creates clouds on all tiles in radius, in ascending id order, with given duration and amount`() {
        // Arrange
        val m = buildTheMap()
        val location = 42
        val radius = 2
        val duration = 7
        val amount = 456

        stubTileManager(tm, m, location, radius)

        val incident = CloudCreationIncident(
            id = 9,
            tick = 123,
            cloudHandler = cloudHandler,
            duration = duration,
            location = location,
            radius = radius,
            amount = amount
        )

        // Act
        incident.perform(currentTick = 123, currentYearTick = YearTick.MAY1)

        // Assert — verify calls happen in ascending tile.id order: 3,5,10
        inOrder(cloudHandler) {
            verify(cloudHandler).createCloud(3, duration, amount)
            verify(cloudHandler).createCloud(5, duration, amount)
            verify(cloudHandler).createCloud(10, duration, amount)
        }
        verify(cloudHandler, times(3)).createCloud(any(), eq(duration), eq(amount))
        verifyNoMoreInteractions(cloudHandler)
    }

    // ----- helpers -----

    private data class TheMap(
        val t3: Tile,
        val t5: Tile,
        val t10: Tile
    )

    private fun buildTheMap(): TheMap {
        val t3 = Tile(
            id = 3,
            coordinate = Coordinate(0, 0),
            categoryType = TileCategory.MEADOW,
            shed = false,
            farm = null,
            direction = null
        )
        val t5 = Tile(
            id = 5,
            coordinate = Coordinate(2, 0),
            categoryType = TileCategory.ROAD,
            shed = false,
            farm = null,
            direction = null
        )
        val t10 = Tile(
            id = 10,
            coordinate = Coordinate(0, 2),
            categoryType = TileCategory.MEADOW,
            shed = false,
            farm = null,
            direction = null
        )
        return TheMap(t3, t5, t10)
    }

    private fun stubTileManager(tm: TileManager, m: TheMap, location: Int, radius: Int) {
        // Return unsorted list to ensure incident sorts before createCloud
        whenever(tm.getAllNeighborTilesInRadius(location, radius))
            .thenReturn(listOf(m.t5, m.t10, m.t3))
    }
}
