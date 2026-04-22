package de.unisaarland.cs.se.selab.systemtest.selab25.fullTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
private const val FARM_START_0 = "[IMPORTANT] Farm: Farm 0 starts its actions."
private const val FARM_FINISH_0 = "[IMPORTANT] Farm: Farm 0 finished its actions."
private const val FARM_START_1 = "[IMPORTANT] Farm: Farm 1 starts its actions."
private const val FARM_FINISH_1 = "[IMPORTANT] Farm: Farm 1 finished its actions."
private const val REDUCE_SOIL = "[INFO] Soil Moisture: The soil moisture is" +
    " below threshold in 0 FIELD and 0 PLANTATION tiles."
private const val FARM_ACTION_0 = "[DEBUG] Farm: Farm 0 has the following active sowing plans" +
    " it intends to pursue in this tick: ."
private const val FARM_ACTION_1 = "[DEBUG] Farm: Farm 1 has the following active sowing plans" +
    " it intends to pursue in this tick: ."

/**
 * Example system test
 */
class MediumFullTest0 : ExampleSystemTestExtension() {
    override val name = "Medium all component tick 0 to 4"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullTest/mediumFarmFull.json"
    override val scenario = "FullTest/mediumScenarioFull.json"
    override val map = "FullTest/mediumMapFull.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 1
    private val expectedLogs = listOf(
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 7 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9930 L water moved from tile 7 to tile 8.",
        "[DEBUG] Cloud Movement: On tile 7, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 1 on tile 8 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 1 with 9860 L water moved from tile 8 to tile 9.",
        "[DEBUG] Cloud Movement: On tile 8, the amount of sunlight is 95.",
        "[INFO] Cloud Dissipation: Cloud 1 got stuck on tile 9.",
        "[IMPORTANT] Cloud Rain: Cloud 2 on tile 10 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 2 with 9900 L water moved from tile 10 to tile 11.",
        "[DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 2 on tile 11 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 2 with 9800 L water moved from tile 11 to tile 22.",
        "[DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 2 on tile 22 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 2 with 9700 L water moved from tile 22 to tile 21.",
        "[DEBUG] Cloud Movement: On tile 22, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 2 on tile 21 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 2 with 9600 L water moved from tile 21 to tile 20.",
        "[DEBUG] Cloud Movement: On tile 21, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 2 on tile 20 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 2 with 9530 L water moved from tile 20 to tile 19.",
        "[DEBUG] Cloud Movement: On tile 20, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 2 on tile 19 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 2 with 9460 L water moved from tile 19 to tile 18.",
        "[DEBUG] Cloud Movement: On tile 19, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 2 on tile 18 rained down 70 L water.",
        "[INFO] Cloud Movement: Cloud 2 with 9390 L water moved from tile 18 to tile 29.",
        "[DEBUG] Cloud Movement: On tile 18, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 2 on tile 29 rained down 9390 L water.",
        "[INFO] Cloud Dissipation: Cloud 2 dissipates on tile 29.",
        FARM_START_0,
        FARM_ACTION_0,
        FARM_FINISH_0,
        FARM_START_1,
        FARM_ACTION_1,
        FARM_FINISH_1,
        "[IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 18.",
        "[IMPORTANT] Incident: Incident 5 of type BROKEN_MACHINE happened and affected tiles 13.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 1530000 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 1530000 g of APPLE.",
        "[INFO] Simulation Info: Tick 1 started at tick 2 within the year.",
        REDUCE_SOIL,
        FARM_START_0,
        FARM_ACTION_0,
        FARM_FINISH_0,
        FARM_START_1,
        FARM_ACTION_1,
        FARM_FINISH_1,
        "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 1377000 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 1377000 g of APPLE.",
        "[INFO] Simulation Info: Tick 2 started at tick 3 within the year.",
        REDUCE_SOIL,
        FARM_START_0,
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm Action: Machine 2 performs CUTTING on tile 10 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 2 performs CUTTING on tile 21 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 2 performs CUTTING on tile 22 for 3 days.",
        "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.",
        FARM_FINISH_0,
        FARM_START_1,
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm Action: Machine 4 performs CUTTING on tile 40 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 4 performs CUTTING on tile 41 for 3 days.",
        "[IMPORTANT] Farm Action: Machine 4 performs CUTTING on tile 52 for 3 days.",
        "[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.",
        FARM_FINISH_1,
        "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 1115370 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 1115370 g of APPLE.",
        "[INFO] Simulation Info: Tick 3 started at tick 4 within the year.",
        REDUCE_SOIL,
        FARM_START_0,
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .",
        FARM_FINISH_0,
        FARM_START_1,
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 8.",
        FARM_FINISH_1,
        "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 903449 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 903449 g of APPLE.",
        "[INFO] Simulation Info: Tick 4 started at tick 5 within the year.",
        REDUCE_SOIL,
        FARM_START_0,
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 2.",
        FARM_FINISH_0,
        FARM_START_1,
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 8.",
        FARM_FINISH_1,
        "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 658613 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 658613 g of APPLE."
    )

    override suspend fun run() {
        skipLines(6)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
