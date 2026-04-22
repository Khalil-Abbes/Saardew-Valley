package de.unisaarland.cs.se.selab.systemtest.selab25.fullTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

private const val SIM_TICK_8 = "[INFO] Simulation Info: Tick 8 started at tick 9 within the year."
private const val SIM_TICK_9 = "[INFO] Simulation Info: Tick 9 started at tick 10 within the year."

private const val SOIL_MOISTURE_1 = "[INFO] Soil Moisture: " +
    "The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles."
private const val SOIL_MOISTURE_2 = "[INFO] Soil Moisture: The soil moisture " +
    "is below threshold in 3 FIELD and 1 PLANTATION tiles."

private const val FARM0_START = "[IMPORTANT] Farm: Farm 0 starts its actions."
private const val FARM0_FINISH = "[IMPORTANT] Farm: Farm 0 finished its actions."
private const val FARM1_START = "[IMPORTANT] Farm: Farm 1 starts its actions."
private const val FARM1_FINISH = "[IMPORTANT] Farm: Farm 1 finished its actions."

private const val MACHINE1_FINISH = "[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13."
private const val MACHINE2_FINISH = "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16."
private const val MACHINE3_FINISH = "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46."

/**
 * Example system test
 */
class MediumFullTest7to9 : ExampleSystemTestExtension() {
    override val name = "Medium all component tick 7"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullTest/mediumFarmFull.json"
    override val scenario = "FullTest/mediumScenarioFull.json"
    override val map = "FullTest/mediumMapFull.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 1
    private val expectedLogs = listOf(

        SOIL_MOISTURE_1,
        FARM0_START,
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 18 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 19 for 3 days.",
        MACHINE1_FINISH,
        "[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 21 for 3 days.",
        MACHINE2_FINISH,
        FARM0_FINISH,
        FARM1_START,
        "[DEBUG] Farm: Farm 1 has the following active sowing" +
            " plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 43 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 44 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 55 for 3 days.",
        MACHINE3_FINISH,
        FARM1_FINISH,
        "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 255157 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 629856 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 19 changed to 629856 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 255157 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 637729 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 637729 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 637729 g of OAT.",

        SIM_TICK_8,
        SOIL_MOISTURE_1,
        FARM0_START,
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 4.",
        "[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 20 for 3 days.",
        "[IMPORTANT] Farm Sowing: Machine 1 has sowed POTATO according to sowing plan 4.",
        MACHINE1_FINISH,
        "[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 11 for 3 days.",
        MACHINE2_FINISH,
        FARM0_FINISH,
        FARM1_START,
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 42 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 43 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 44 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 54 for 3 days.",
        MACHINE3_FINISH,
        FARM1_FINISH,
        "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 167407 g of APPLE.",
        "[DEBUG] Harvest Estimate: Required actions on tile 18 were not performed: WEEDING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 413247 g of OAT.",
        "[DEBUG] Harvest Estimate: Required actions on tile 19 were not performed: WEEDING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 19 changed to 413247 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 900000 g of POTATO.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 21 changed to 720000 g of ALMOND.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 22 changed to 1080000 g of CHERRY.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 167407 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 41 changed to 1080000 g of CHERRY.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 900000 g of POTATO.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 464904 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 464904 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 52 changed to 720000 g of ALMOND.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 54 changed to 900000 g of POTATO.",
        "[DEBUG] Harvest Estimate: Required actions on tile 55 were not performed: WEEDING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 418413 g of OAT.",

        SIM_TICK_9,
        SOIL_MOISTURE_2,
        FARM0_START,
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 18 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 19 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 20 for 3 days.",
        MACHINE1_FINISH,
        "[IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 22 for 3 days.",
        MACHINE2_FINISH,
        FARM0_FINISH,
        FARM1_START,
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .",
        FARM1_FINISH,
        "[IMPORTANT] Incident: Incident 8 of type BEE_HAPPY happened and affected tiles .",
        "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 109835 g of APPLE.",
        "[DEBUG] Harvest Estimate: Required actions on tile 18 were not performed: WEEDING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 271130 g of OAT.",
        "[DEBUG] Harvest Estimate: Required actions on tile 19 were not performed: WEEDING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 19 changed to 271130 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 810000 g of POTATO.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 21 changed to 648000 g of ALMOND.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 22 changed to 972000 g of CHERRY.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 109835 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 41 changed to 972000 g of CHERRY.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 810000 g of POTATO.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 338913 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 338913 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 52 changed to 648000 g of ALMOND.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 54 changed to 810000 g of POTATO.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 305021 g of OAT."
    )

    override suspend fun run() {
        var num = 0
        while (num < 9) {
            num++
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        }
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
