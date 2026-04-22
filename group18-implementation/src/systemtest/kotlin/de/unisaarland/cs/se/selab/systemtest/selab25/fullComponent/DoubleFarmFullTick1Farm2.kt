package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFullTick1Farm2 : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 1, farm 2 action phase"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 18
    private val expectedLogs = listOf(
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
        "[INFO] Cloud Dissipation: Cloud 6 dissipates on tile 8.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 15 changed to 1377000 g of APPLE.",
        "[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: HARVESTING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 1083000 g of GRAPE."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipLines(56)
        assertNextLine("[IMPORTANT] Farm: Farm 2 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 2 has the following active sowing " +
                "plans it intends to pursue in this tick: 11,22,100,200."
        )
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
