package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class FullComponentTick2 : ExampleSystemTestExtension() {
    override val name = "test all component tick 2"
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

        val expectedLogs = listOf(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
            "[IMPORTANT] Cloud Rain: Cloud 4 on tile 5 rained down 1000 L water.",
            "[INFO] Cloud Movement: Cloud 4 with 9000 L water moved from tile 5 to tile 10.",
            "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 95.",
            "[IMPORTANT] Cloud Union: Clouds 9 and 4 united to cloud 12 with 38760 L water and duration 10 on tile 10.",
            "[IMPORTANT] Cloud Rain: Cloud 5 on tile 7 rained down 10000 L water.",
            "[INFO] Cloud Dissipation: Cloud 5 dissipates on tile 7.",
            "[IMPORTANT] Cloud Rain: Cloud 6 on tile 8 rained down 10000 L water.",
            "[INFO] Cloud Dissipation: Cloud 6 dissipates on tile 8.",
            "[IMPORTANT] Cloud Rain: Cloud 7 on tile 9 rained down 200 L water.",
            "[IMPORTANT] Cloud Rain: Cloud 10 on tile 13 rained down 10000 L water.",
            "[INFO] Cloud Dissipation: Cloud 10 dissipates on tile 13.",
            "[IMPORTANT] Cloud Rain: Cloud 11 on tile 14 rained down 200 L water.",
            "[IMPORTANT] Cloud Rain: Cloud 12 on tile 10 rained down 70 L water.",
            "[DEBUG] Cloud Position: Cloud 7 is on tile 9, where the amount of sunlight is 48.",
            "[DEBUG] Cloud Position: Cloud 12 is on tile 10, where the amount of sunlight is 48.",
            "[DEBUG] Cloud Position: Cloud 11 is on tile 14, where the amount of sunlight is 48.",
            "[IMPORTANT] Farm: Farm 1 starts its actions.",
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 2,10,20.",
            "[IMPORTANT] Farm Action: Machine 3 performs CUTTING on tile 14 for 2 days.",
            "[IMPORTANT] Farm Action: Machine 3 performs CUTTING on tile 15 for 2 days.",
            "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 1.",
            "[IMPORTANT] Farm: Farm 1 finished its actions.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 1200000 g of GRAPE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 14 changed to 800000 g of ALMOND.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 1530000 g of APPLE."
        )
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
