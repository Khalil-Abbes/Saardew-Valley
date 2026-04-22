package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class FullComponentTick1 : ExampleSystemTestExtension() {
    override val name = "test all component tick 1"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/AllSimFarm.json"
    override val scenario = "fullComponent/AllScenarioAtOnce.json"
    override val map = "fullComponent/smallAllThingsMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 20

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)

        val expectedLogs = listOf(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
            "[IMPORTANT] Cloud Rain: Cloud 1 on tile 4 rained down 70 L water.",
            "[INFO] Cloud Movement: Cloud 1 with 9930 L water moved from tile 4 to tile 5.",
            "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 109.",
            "[IMPORTANT] Cloud Rain: Cloud 1 on tile 5 rained down 100 L water.",
            "[INFO] Cloud Movement: Cloud 1 with 9830 L water moved from tile 5 to tile 10.",
            "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 109.",
            "[IMPORTANT] Cloud Union: Clouds 2 and 1 united to cloud 3 with 19830 L water and duration -1 on tile 10.",
            "[IMPORTANT] Cloud Rain: Cloud 3 on tile 10 rained down 70 L water.",
            "[DEBUG] Cloud Position: Cloud 3 is on tile 10, where the amount of sunlight is 62.",
            "[IMPORTANT] Farm: Farm 1 starts its actions.",
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,10,20.",
            "[IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 4 for 2 days.",
            "[IMPORTANT] Farm Sowing: Machine 2 has sowed WHEAT according to sowing plan 1.",
            "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 8.",
            "[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 9 for 2 days.",
            "[IMPORTANT] Farm Harvest: Machine 3 has collected 1028850 g of GRAPE harvest.",
            "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 1.",
            "[IMPORTANT] Farm Machine: Machine 3 unloads 1028850 g of GRAPE harvest in the shed.",
            "[IMPORTANT] Farm: Farm 1 finished its actions.",
            "[IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 10,14.",
            "[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles .",
            "[IMPORTANT] Incident: Incident 3 of type CLOUD_CREATION happened and affected tiles 5,7,8,9,10,13,14.",
            "[IMPORTANT] Cloud Union: Clouds 3 and 8 united to cloud 9 with 29760 L water and duration 10 on tile 10.",
            "[IMPORTANT] Incident: Incident 4 of type CITY_EXPANSION happened and affected tiles 16.",
            "[IMPORTANT] Incident: Incident 5 of type BROKEN_MACHINE happened and affected tiles 1.",
            "[IMPORTANT] Incident: Incident 6 of type DROUGHT happened and affected tiles 5.",
            "[DEBUG] Harvest Estimate: Required actions on tile 14 were not performed: HARVESTING.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 14 changed to 0 g of ALMOND.",
            "[DEBUG] Harvest Estimate: Required actions on tile 15 were not performed: HARVESTING.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 0 g of APPLE."
        )
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
