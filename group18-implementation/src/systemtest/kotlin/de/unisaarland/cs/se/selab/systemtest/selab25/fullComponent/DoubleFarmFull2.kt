package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFull2 : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 2"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 18

    private val expectedLogs = listOf(
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Cloud Rain: Cloud 4 on tile 5 rained down 1000 L water.",
        "[INFO] Cloud Movement: Cloud 4 with 9000 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 109.",
        "[INFO] Cloud Dissipation: Cloud 4 got stuck on tile 16.",
        "[IMPORTANT] Cloud Rain: Cloud 5 on tile 7 rained down 10000 L water.",
        "[INFO] Cloud Dissipation: Cloud 5 dissipates on tile 7.",
        "[IMPORTANT] Cloud Rain: Cloud 7 on tile 9 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 7 with 9900 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 109.",
        "[IMPORTANT] Cloud Rain: Cloud 7 on tile 4 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 7 with 9830 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 109.",
        "[INFO] Cloud Movement: Cloud 7 with 9830 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 106.",
        "[INFO] Cloud Dissipation: Cloud 7 got stuck on tile 16.",
        "[IMPORTANT] Cloud Rain: Cloud 9 on tile 10 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 9 with 29080 L water moved from tile 10 to tile 9.",
        "[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 109.",
        "[INFO] Cloud Movement: Cloud 9 with 29080 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 106.",
        "[INFO] Cloud Movement: Cloud 9 with 29080 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 106.",
        "[INFO] Cloud Movement: Cloud 9 with 29080 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 103.",
        "[INFO] Cloud Dissipation: Cloud 9 got stuck on tile 16.",
        "[IMPORTANT] Cloud Rain: Cloud 10 on tile 13 rained down 10000 L water.",
        "[INFO] Cloud Dissipation: Cloud 10 dissipates on tile 13.",
        "[IMPORTANT] Cloud Rain: Cloud 11 on tile 14 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 11 with 9900 L water moved from tile 14 to tile 10.",
        "[DEBUG] Cloud Movement: On tile 14, the amount of sunlight is 109.",
        "[INFO] Cloud Movement: Cloud 11 with 9900 L water moved from tile 10 to tile 9.",
        "[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 106.",
        "[INFO] Cloud Movement: Cloud 11 with 9900 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 103.",
        "[INFO] Cloud Movement: Cloud 11 with 9900 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 103.",
        "[INFO] Cloud Movement: Cloud 11 with 9900 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 100.",
        "[INFO] Cloud Dissipation: Cloud 11 got stuck on tile 16.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,10,20.",
        "[IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 4 for 2 days.",
        "[IMPORTANT] Farm Sowing: Machine 2 has sowed WHEAT according to sowing plan 1.",
        "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 1.",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Farm: Farm 2 starts its actions.",
        "[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: 11,22,100,200.",
        "[IMPORTANT] Farm Action: Machine 22 performs SOWING on tile 17 for 2 days.",
        "[IMPORTANT] Farm Sowing: Machine 22 has sowed WHEAT according to sowing plan 11.",
        "[IMPORTANT] Farm Machine: Machine 22 is finished and returns to the shed at 18.",
        "[IMPORTANT] Farm Action: Machine 33 performs HARVESTING on tile 15 for 2 days.",
        "[IMPORTANT] Farm Harvest: Machine 33 has collected 1377000 g of APPLE harvest.",
        "[IMPORTANT] Farm Machine: Machine 33 is finished and returns to the shed at 18.",
        "[IMPORTANT] Farm Machine: Machine 33 unloads 1377000 g of APPLE harvest in the shed.",
        "[IMPORTANT] Farm: Farm 2 finished its actions.",
        "[IMPORTANT] Incident: Incident 11 of type ANIMAL_ATTACK happened and affected tiles 4,5,9,10,14,15.",
        "[IMPORTANT] Incident: Incident 22 of type BEE_HAPPY happened and affected tiles .",
        "[IMPORTANT] Incident: Incident 33 of type CLOUD_CREATION happened and affected tiles 15,18,19,20.",
        "[IMPORTANT] Incident: Incident 55 of type BROKEN_MACHINE happened and affected tiles 18.",
        "[IMPORTANT] Incident: Incident 66 of type DROUGHT happened and affected tiles 14,15.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 750000 g of WHEAT.",
        "[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: HARVESTING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 1028850 g of GRAPE."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
