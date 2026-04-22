package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFull : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 1"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 18

    private val expectedLogs1 = listOf(
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 4 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9930 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 123.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 5 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9830 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 123.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 16 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9760 L water moved from tile 16 to tile 17.",
        "[DEBUG] Cloud Movement: On tile 16, the amount of sunlight is 123.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 17 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9690 L water moved from tile 17 to tile 19.",
        "[DEBUG] Cloud Movement: On tile 17, the amount of sunlight is 123.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 19 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9620 L water moved from tile 19 to tile 20.",
        "[DEBUG] Cloud Movement: On tile 19, the amount of sunlight is 123.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 20 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9520 L water moved from tile 20 to tile 15.",
        "[DEBUG] Cloud Movement: On tile 20, the amount of sunlight is 123.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 15 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9420 L water moved from tile 15 to tile 14.",
        "[DEBUG] Cloud Movement: On tile 15, the amount of sunlight is 123.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 14 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9320 L water moved from tile 14 to tile 10.",
        "[DEBUG] Cloud Movement: On tile 14, the amount of sunlight is 123.",
        "[IMPORTANT] Cloud Union: Clouds 2 and 1 united to cloud 3 with 19320 L water and duration -1 on tile 10.",
        "[IMPORTANT] Cloud Rain: Cloud 3 on tile 10 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 3 with 19250 L water moved from tile 10 to tile 9.",
        "[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 123.",
        "[IMPORTANT] Cloud Rain: Cloud 3 on tile 9 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 3 with 19150 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 123.",
        "[INFO] Cloud Movement: Cloud 3 with 19150 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 120.",
        "[INFO] Cloud Movement: Cloud 3 with 19150 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 120.",
        "[INFO] Cloud Movement: Cloud 3 with 19150 L water moved from tile 16 to tile 17.",
        "[DEBUG] Cloud Movement: On tile 16, the amount of sunlight is 120.",
        "[INFO] Cloud Movement: Cloud 3 with 19150 L water moved from tile 17 to tile 19.",
        "[DEBUG] Cloud Movement: On tile 17, the amount of sunlight is 120.",
        "[INFO] Cloud Movement: Cloud 3 with 19150 L water moved from tile 19 to tile 20.",
        "[DEBUG] Cloud Movement: On tile 19, the amount of sunlight is 120.",
        "[INFO] Cloud Movement: Cloud 3 with 19150 L water moved from tile 20 to tile 15.",
        "[DEBUG] Cloud Movement: On tile 20, the amount of sunlight is 120.",
        "[INFO] Cloud Movement: Cloud 3 with 19150 L water moved from tile 15 to tile 14.",
        "[DEBUG] Cloud Movement: On tile 15, the amount of sunlight is 120.",
        "[INFO] Cloud Movement: Cloud 3 with 19150 L water moved from tile 14 to tile 10.",
        "[DEBUG] Cloud Movement: On tile 14, the amount of sunlight is 120.",
        "[DEBUG] Cloud Position: Cloud 3 is on tile 10, where the amount of sunlight is 73.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2,10,20.",
        "[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 9 for 2 days.",
        "[IMPORTANT] Farm Harvest: Machine 3 has collected 1140000 g of GRAPE harvest.",
        "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 1.",
        "[IMPORTANT] Farm Machine: Machine 3 unloads 1140000 g of GRAPE harvest in the shed.",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Farm: Farm 2 starts its actions."
    )
    private val expectedLogs2 = listOf(
        "[IMPORTANT] Farm Action: Machine 33 performs HARVESTING on tile 14 for 2 days.",
        "[IMPORTANT] Farm Harvest: Machine 33 has collected 800000 g of ALMOND harvest.",
        "[IMPORTANT] Farm Machine: Machine 33 is finished and returns to the shed at 18.",
        "[IMPORTANT] Farm Machine: Machine 33 unloads 800000 g of ALMOND harvest in the shed.",
        "[IMPORTANT] Farm: Farm 2 finished its actions.",
        "[IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 10,14.",
        "[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles .",
        "[IMPORTANT] Incident: Incident 3 of type CLOUD_CREATION happened and affected tiles 5,7,8,9,10,13,14.",
        "[IMPORTANT] Cloud Union: Clouds 3 and 8 united to cloud 9 with 29150 L water and duration 10 on tile 10.",
        "[IMPORTANT] Incident: Incident 4 of type CITY_EXPANSION happened and affected tiles 16.",
        "[IMPORTANT] Incident: Incident 5 of type BROKEN_MACHINE happened and affected tiles 1.",
        "[IMPORTANT] Incident: Incident 6 of type DROUGHT happened and affected tiles 5.",
        "[IMPORTANT] Incident: Incident 44 of type CITY_EXPANSION happened and affected tiles 8.",
        "[INFO] Cloud Dissipation: Cloud 6 got stuck on tile 8.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 1377000 g of APPLE.",
        "[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: HARVESTING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 1083000 g of GRAPE."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        for (expectedLog in expectedLogs1) {
            assertNextLine(expectedLog)
        }
        assertNextLine(
            "[DEBUG] Farm: Farm 2 has the following active sowing " +
                "plans it intends to pursue in this tick: 11,22,100,200."
        )
        for (expectedLog in expectedLogs2) {
            assertNextLine(expectedLog)
        }
    }
}
