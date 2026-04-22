package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfClouds

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class CloudTestOnCircularDirection1 : ExampleSystemTestExtension() {
    override val name = "CloudTestOnCircularDirection, cloud phase"
    override val description = "this is for tick 1 log"
    override val farms = "fullComponent/AllSimFarm.json"
    override val scenario = "fullComponent/AllScenarioAtOnce.json"
    override val map = "fullComponent/smallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    private val expectedLogs = listOf(
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 4 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9930 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 5 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9830 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 16 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9760 L water moved from tile 16 to tile 17.",
        "[DEBUG] Cloud Movement: On tile 16, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 17 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9690 L water moved from tile 17 to tile 15.",
        "[DEBUG] Cloud Movement: On tile 17, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 15 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9590 L water moved from tile 15 to tile 14.",
        "[DEBUG] Cloud Movement: On tile 15, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 14 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9490 L water moved from tile 14 to tile 10.",
        "[DEBUG] Cloud Movement: On tile 14, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Union: Clouds 2 and 1 united to cloud 3 with 19490 L water and duration -1 on tile 10.",
        "[IMPORTANT] Cloud Rain: Cloud 3 on tile 10 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 3 with 19420 L water moved from tile 10 to tile 9.",
        "[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 3 on tile 9 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 3 with 19320 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 95.",
        "[INFO] Cloud Movement: Cloud 3 with 19320 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 3 with 19320 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 3 with 19320 L water moved from tile 16 to tile 17.",
        "[DEBUG] Cloud Movement: On tile 16, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 3 with 19320 L water moved from tile 17 to tile 15.",
        "[DEBUG] Cloud Movement: On tile 17, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 3 with 19320 L water moved from tile 15 to tile 14.",
        "[DEBUG] Cloud Movement: On tile 15, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 3 with 19320 L water moved from tile 14 to tile 10.",
        "[DEBUG] Cloud Movement: On tile 14, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 3 with 19320 L water moved from tile 10 to tile 9.",
        "[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 3 with 19320 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 92.",
        "[DEBUG] Cloud Position: Cloud 3 is on tile 4, where the amount of sunlight is 42."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
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
