package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFullTick1Farm1 : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 1, farm1 action phase"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 18

    private val expectedLogs1 = listOf(
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,10,20.",
        "[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 9 for 2 days.",
        "[IMPORTANT] Farm Harvest: Machine 3 has collected 1140000 g of GRAPE harvest.",
        "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 1.",
        "[IMPORTANT] Farm Machine: Machine 3 unloads 1140000 g of GRAPE harvest in the shed.",
        "[IMPORTANT] Farm: Farm 1 finished its actions."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipLines(49)
        for (expectedLog in expectedLogs1) {
            assertNextLine(expectedLog)
        }
    }
}
