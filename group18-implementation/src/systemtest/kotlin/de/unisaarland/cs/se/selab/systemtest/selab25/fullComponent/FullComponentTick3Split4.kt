package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class FullComponentTick3Split4 : ExampleSystemTestExtension() {
    override val name = "test all component tick 4, stats phase"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/AllSimFarm.json"
    override val scenario = "fullComponent/AllScenarioAtOnce.json"
    override val map = "fullComponent/smallAllThingsMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 20

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilString("[IMPORTANT] Simulation Info: Simulation ended at tick 3.")
        assertCurrentLine("[IMPORTANT] Simulation Info: Simulation ended at tick 3.")
        val expectedLogs = listOf(
            "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
            "[IMPORTANT] Simulation Statistics: Farm 1 collected 1028850 g of harvest.",
            "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 1028850 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 4877000 g."
        )
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
    private suspend fun skipUntilString(startString: String): String {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        return if (line.startsWith(startString)) {
            line
        } else {
            skipUntilString(startString)
        }
    }
}
