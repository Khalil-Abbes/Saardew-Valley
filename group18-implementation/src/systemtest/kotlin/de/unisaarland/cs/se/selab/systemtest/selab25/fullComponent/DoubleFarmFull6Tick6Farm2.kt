package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFull6Tick6Farm2 : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 6, farm 2"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 6
    override val startYearTick = 18

    private val expectedLogs = listOf(
        "[IMPORTANT] Farm: Farm 2 starts its actions.",
        "[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: 22,100,200.",
        "[IMPORTANT] Farm: Farm 2 finished its actions."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipLines(4)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
