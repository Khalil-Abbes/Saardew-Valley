package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFull6Tick6LastStatistic : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 6, total stat for plantation and field"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 6
    override val startYearTick = 18

    private val expectedLogs = listOf(
        "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 4650000 g."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS)
        skipLines(9)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
