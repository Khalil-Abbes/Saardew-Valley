package de.unisaarland.cs.se.selab.systemtest.selab25.fullTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

private const val SOIL_MOISTURE_0 = "[INFO] Soil Moisture: The " +
    "soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."

private const val FARM0_START = "[IMPORTANT] Farm: Farm 0 starts its actions."
private const val FARM0_FINISH = "[IMPORTANT] Farm: Farm 0 finished its actions."
private const val FARM1_START = "[IMPORTANT] Farm: Farm 1 starts its actions."
private const val FARM1_FINISH = "[IMPORTANT] Farm: Farm 1 finished its actions."

private const val MACHINE1_FINISH = "[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13."
private const val MACHINE2_FINISH = "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16."
private const val MACHINE3_FINISH = "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46."
private const val MACHINE4_FINISH = "[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49."

/**
 * Example system test
 */
class MediumFullTest5Tick6 : ExampleSystemTestExtension() {
    override val name = "Medium all component tick 6"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullTest/mediumFarmFull.json"
    override val scenario = "FullTest/mediumScenarioFull.json"
    override val map = "FullTest/mediumMapFull.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 1
    private val expectedLogs = listOf(
        SOIL_MOISTURE_0,
        FARM0_START,
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 2.",
        "[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 7 for 3 days.",
        "[IMPORTANT] Farm Sowing: Machine 1 " +
            "has sowed OAT according to sowing plan 2.",
        "[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 8 for 3 days.",
        "[IMPORTANT] Farm Sowing: Machine 1" +
            " has sowed OAT according to sowing plan 2.",
        "[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 18 for 3 days.",
        "[IMPORTANT] Farm Sowing: Machine 1 has sowed OAT according to sowing plan 2.",
        "[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 19 for 3 days.",
        "[IMPORTANT] Farm Sowing: Machine 1 has sowed OAT according to sowing plan 2.",
        MACHINE1_FINISH,
        "[IMPORTANT] Farm Action: Machine 2 performs MOWING on tile 11 for 3 days.",
        MACHINE2_FINISH,
        FARM0_FINISH,
        FARM1_START,
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 8.",
        "[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 42 for 3 days.",
        "[IMPORTANT] Farm Sowing: Machine 3 has sowed POTATO according to sowing plan 8.",
        "[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 54 for 3 days.",
        "[IMPORTANT] Farm Sowing: Machine 3 has sowed POTATO according to sowing plan 8.",
        MACHINE3_FINISH,
        "[IMPORTANT] Farm Action: Machine 4 performs MOWING on tile 51 for 3 days.",
        MACHINE4_FINISH,
        FARM1_FINISH,
        "[IMPORTANT] Incident: Incident 4 of type CITY_EXPANSION happened and affected tiles 8.",
        "[IMPORTANT] Incident: Incident 9 of type CITY_EXPANSION happened and affected tiles 7.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 350011 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 777600 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 19 changed to 777600 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 350011 g of APPLE.",
        "[DEBUG] Harvest Estimate: Required actions on tile 43 were not performed: WEEDING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 787320 g of OAT.",
        "[DEBUG] Harvest Estimate: Required actions on tile 44 were not performed: WEEDING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 787320 g of OAT.",
        "[DEBUG] Harvest Estimate: Required actions on tile 55 were not performed: WEEDING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 787320 g of OAT."
    )

    override suspend fun run() {
        var num = 0
        while (num < 8) {
            num++
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        }
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
