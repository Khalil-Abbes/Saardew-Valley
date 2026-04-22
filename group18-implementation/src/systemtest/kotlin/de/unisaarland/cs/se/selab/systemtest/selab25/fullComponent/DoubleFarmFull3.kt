package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFull3 : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 3"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 18

    private val expectedLogs = listOf(
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[INFO] Cloud Movement: Cloud 12 with 1000 L water moved from tile 15 to tile 14.",
        "[DEBUG] Cloud Movement: On tile 15, the amount of sunlight is 109.",
        "[INFO] Cloud Movement: Cloud 12 with 1000 L water moved from tile 14 to tile 10.",
        "[DEBUG] Cloud Movement: On tile 14, the amount of sunlight is 109.",
        "[INFO] Cloud Movement: Cloud 12 with 1000 L water moved from tile 10 to tile 9.",
        "[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 109.",
        "[INFO] Cloud Movement: Cloud 12 with 1000 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 109.",
        "[INFO] Cloud Movement: Cloud 12 with 1000 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 109.",
        "[INFO] Cloud Movement: Cloud 12 with 1000 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 109.",
        "[INFO] Cloud Dissipation: Cloud 12 got stuck on tile 16.",
        "[INFO] Cloud Movement: Cloud 14 with 1000 L water moved from tile 19 to tile 20.",
        "[DEBUG] Cloud Movement: On tile 19, the amount of sunlight is 109.",
        "[IMPORTANT] Cloud Union: Clouds 15 and 14 united to cloud 16 with 2000 L water and duration 10 on tile 20.",
        "[INFO] Cloud Movement: Cloud 16 with 2000 L water moved from tile 20 to tile 15.",
        "[DEBUG] Cloud Movement: On tile 20, the amount of sunlight is 109.",
        "[INFO] Cloud Movement: Cloud 16 with 2000 L water moved from tile 15 to tile 14.",
        "[DEBUG] Cloud Movement: On tile 15, the amount of sunlight is 106.",
        "[INFO] Cloud Movement: Cloud 16 with 2000 L water moved from tile 14 to tile 10.",
        "[DEBUG] Cloud Movement: On tile 14, the amount of sunlight is 106.",
        "[INFO] Cloud Movement: Cloud 16 with 2000 L water moved from tile 10 to tile 9.",
        "[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 106.",
        "[INFO] Cloud Movement: Cloud 16 with 2000 L water moved from tile 9 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 9, the amount of sunlight is 106.",
        "[INFO] Cloud Movement: Cloud 16 with 2000 L water moved from tile 4 to tile 5.",
        "[DEBUG] Cloud Movement: On tile 4, the amount of sunlight is 106.",
        "[INFO] Cloud Movement: Cloud 16 with 2000 L water moved from tile 5 to tile 16.",
        "[DEBUG] Cloud Movement: On tile 5, the amount of sunlight is 106.",
        "[INFO] Cloud Dissipation: Cloud 16 got stuck on tile 16.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 2,10,20.",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Farm: Farm 2 starts its actions.",
        "[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: 22,100,200.",
        "[IMPORTANT] Farm Action: Machine 33 performs HARVESTING on tile 20 for 2 days.",
        "[IMPORTANT] Farm Harvest: Machine 33 has collected 1028850 g of GRAPE harvest.",
        "[IMPORTANT] Farm Machine: Machine 33 is finished and returns to the shed at 18.",
        "[IMPORTANT] Farm Machine: Machine 33 unloads 1028850 g of GRAPE harvest in the shed.",
        "[IMPORTANT] Farm: Farm 2 finished its actions."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
