package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class FullComponentTick2Split2 : ExampleSystemTestExtension() {
    override val name = "test all component tick 2, farm action phase"
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
        skipLines(17)

        val expectedLogs = listOf(
            "[IMPORTANT] Farm: Farm 1 starts its actions.",
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 2,10,20.",
            "[IMPORTANT] Farm Action: Machine 3 performs CUTTING on tile 14 for 2 days.",
            "[IMPORTANT] Farm Action: Machine 3 performs CUTTING on tile 15 for 2 days.",
            "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 1.",
            "[IMPORTANT] Farm: Farm 1 finished its actions."
        )
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
