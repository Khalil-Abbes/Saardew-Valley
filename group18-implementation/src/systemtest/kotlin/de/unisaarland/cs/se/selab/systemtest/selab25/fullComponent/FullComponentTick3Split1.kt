package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class FullComponentTick3Split1 : ExampleSystemTestExtension() {
    override val name = "test all component tick 2, cloud phase"
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

        val expectedLogs = listOf(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
            "[IMPORTANT] Cloud Rain: Cloud 7 on tile 9 rained down 100 L water.",
            "[IMPORTANT] Cloud Rain: Cloud 11 on tile 14 rained down 100 L water.",
            "[IMPORTANT] Cloud Rain: Cloud 12 on tile 10 rained down 70 L water.",
            "[DEBUG] Cloud Position: Cloud 7 is on tile 9, where the amount of sunlight is 48.",
            "[DEBUG] Cloud Position: Cloud 12 is on tile 10, where the amount of sunlight is 48.",
            "[DEBUG] Cloud Position: Cloud 11 is on tile 14, where the amount of sunlight is 48."
        )
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
