package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfClouds

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class CloudCircularDirection2 : ExampleSystemTestExtension() {
    override val name = "CloudTestOnCircularDirection, farm phase"
    override val description = "this is for tick 1 log"
    override val farms = "fullComponent/AllSimFarm.json"
    override val scenario = "fullComponent/AllScenarioAtOnce.json"
    override val map = "fullComponent/smallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    private val expectedLogs = listOf(
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,10,20.",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilString("[IMPORTANT] Farm")
        assertCurrentLine("[IMPORTANT] Farm: Farm 1 starts its actions.",)
        for (log in expectedLogs) {
            assertNextLine(log)
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
