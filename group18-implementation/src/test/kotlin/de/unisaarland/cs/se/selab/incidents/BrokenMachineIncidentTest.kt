package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.incident.BrokenMachineIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BrokenMachineIncidentTest {

    private lateinit var tm: TileManager
    private lateinit var shed: Tile

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        tm = TileManager()
        shed = Tile(
            id = 1,
            coordinate = Coordinate(
                -1,
                -1
            ),
            categoryType = TileCategory.FARMSTEAD, shed = true, farm = 42, direction = null
        )
        tm.setTiles(mutableMapOf(1 to shed))
    }

    @Test
    fun `duration counts from next tick - duration 1 blocks only next tick, available again after`() {
        val m = Machine(
            id = 7,
            name = "M",
            actions = emptyList(),
            plants = emptyList(),
            duration = 1,
            location = shed,
            tileManager = tm,
            farm = 42
        )
        val incident = BrokenMachineIncident(id = 5, tick = 10, duration = 1, machine = m)

        // Initially available
        assertTrue(m.isAvailable)

        // Perform at tick 10: “counts from the start of the next tick”
        incident.perform(currentTick = 10, currentYearTick = YearTick.MAY1)

        // Still available property stays true in current tick
        // (simulation doesn’t re-refresh availability after incidents)
        assertTrue(m.isAvailable)

        // Next tick (11): should be blocked
        m.refreshForNewTick(11)
        assertFalse(m.isAvailable)

        // Tick after next (12): available again
        m.refreshForNewTick(12)
        assertTrue(m.isAvailable)
    }

    @Test
    fun `duration -1 keeps machine broken indefinitely`() {
        val m = Machine(
            id = 8,
            name = "M2",
            actions = emptyList(),
            plants = emptyList(),
            duration = 1,
            location = shed,
            tileManager = tm,
            farm = 42
        )
        val incident = BrokenMachineIncident(id = 6, tick = 20, duration = -1, machine = m)

        incident.perform(currentTick = 20, currentYearTick = YearTick.JUN1)

        // Next tick and far in the future: still unavailable
        m.refreshForNewTick(21)
        assertFalse(m.isAvailable)
        m.refreshForNewTick(9999)
        assertFalse(m.isAvailable)
    }
}
