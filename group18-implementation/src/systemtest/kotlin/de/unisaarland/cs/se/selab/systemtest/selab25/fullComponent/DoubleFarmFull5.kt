package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFull5 : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 5"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 6
    override val startYearTick = 18

    private val expectedLogs = listOf(
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 2,10,20.",
        "[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 4 for 2 days.",
        "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Farm: Farm 2 starts its actions.",
        "[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: 22,100,200.",
        "[IMPORTANT] Farm Action: Machine 22 performs WEEDING on tile 17 for 2 days.",
        "[IMPORTANT] Farm Machine: Machine 22 is finished and returns to the shed at 18.",
        "[IMPORTANT] Farm: Farm 2 finished its actions."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
