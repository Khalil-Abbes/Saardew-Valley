package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfClouds

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class CloudTestOnCircularDirection2 : ExampleSystemTestExtension() {
    override val name = "CloudTestOnCircularDirection2"
    override val description = "this is for tick 2 log"
    override val farms = "fullComponent/AllSimFarm.json"
    override val scenario = "fullComponent/AllScenarioAtOnce.json"
    override val map = "fullComponent/smallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    private val expectedLogs = listOf(
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Cloud Rain: Cloud 3 on tile 4 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 3 with 19250 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Union: Clouds 4 and 3 united to cloud 11 with 29250 L water and duration 10 on tile 5.",
        "[IMPORTANT] Cloud Rain: Cloud 5 on tile 7 rained down 10000 L water.",
        "[INFO] Cloud Dissipation: Cloud 5 dissipates on tile 7.",
        "[IMPORTANT] Cloud Rain: Cloud 6 on tile 8 rained down 10000 L water.",
        "[INFO] Cloud Dissipation: Cloud 6 dissipates on tile 8.",
        "[IMPORTANT] Cloud Rain: Cloud 7 on tile 9 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 7 with 9900 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 95.",
        "[INFO] Cloud Movement: Cloud 7 with 9900 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 92.",
        "[IMPORTANT] Cloud Union: Clouds 11 and 7 united to cloud 12 with 39150 L water and duration 10 on tile 5.",
        "[IMPORTANT] Cloud Rain: Cloud 8 on tile 10 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 8 with 9930 L water moved from tile 10 to tile 9.",
        "[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 95.",
        "[INFO] Cloud Movement: Cloud 8 with 9930 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 8 with 9930 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 89.",
        "[IMPORTANT] Cloud Union: Clouds 12 and 8 united to cloud 13 with 49080 L water and duration 10 on tile 5.",
        "[IMPORTANT] Cloud Rain: Cloud 9 on tile 13 rained down 10000 L water.",
        "[INFO] Cloud Dissipation: Cloud 9 dissipates on tile 13.",
        "[IMPORTANT] Cloud Rain: Cloud 10 on tile 14 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 10 with 9900 L water moved from tile 14 to tile 10.",
        "[DEBUG] Cloud Movement: On tile 14, the amount of sunlight is 95.",
        "[INFO] Cloud Movement: Cloud 10 with 9900 L water moved from tile 10 to tile 9.",
        "[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 10 with 9900 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 89.",
        "[INFO] Cloud Movement: Cloud 10 with 9900 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 86.",
        "[IMPORTANT] Cloud Union: Clouds 13 and 10 united to cloud 14 with 58980 L water and duration 10 on tile 5.",
        "[IMPORTANT] Cloud Rain: Cloud 14 on tile 5 rained down 1000 L water.",
        "[INFO] Cloud Movement: Cloud 14 with 57980 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 95.",
        "[INFO] Cloud Dissipation: Cloud 14 got stuck on tile 16.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,10,20.",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 1377000 g of APPLE.",
        "[IMPORTANT] Simulation Info: Simulation ended at tick 2.",
        "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
        "[IMPORTANT] Simulation Statistics: Farm 1 collected 0 g of harvest.",
        "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 3297000 g."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        for (log in expectedLogs) {
            assertNextLine(log)
        }
    }
}
