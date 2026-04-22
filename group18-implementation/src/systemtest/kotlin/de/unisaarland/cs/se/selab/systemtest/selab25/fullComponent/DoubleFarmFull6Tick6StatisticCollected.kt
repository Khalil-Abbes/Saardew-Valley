package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFull6Tick6StatisticCollected : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 6, sim ended and farm collection"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 6
    override val startYearTick = 18

    private val expectedLogs = listOf(
        "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
        "[IMPORTANT] Simulation Statistics: Farm 1 collected 1140000 g of harvest.",
        "[IMPORTANT] Simulation Statistics: Farm 2 collected 3205850 g of harvest."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_INFO)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
