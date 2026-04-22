package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import org.junit.jupiter.api.Test
import java.io.PrintWriter

class LoggerTest {

    @Test
    fun testMissedActions() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        Logger.logMissedActions(2, listOf(ActionType.IRRIGATING, ActionType.WEEDING))
    }
}
