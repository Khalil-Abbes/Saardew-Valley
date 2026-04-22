package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.validation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * wrong neighbor adjacent tiles
 */
class MediumMapFullFarm20toEnd : ExampleSystemTestExtension() {
    override val name = "MediumMapFullFarm Fields tick 20 to end"
    override val description = "Checks normal plantation plant actions/periods without incidents."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullTest/mediumFarmFullFields.json"
    override val scenario = "FullTest/mediumScenarioFull.json"
    override val map = "FullTest/mediumMapFullFields.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 30
    override val startYearTick = 1

    val expectedOutput = """
                [INFO] Soil Moisture: The soil moisture is below threshold in 2 FIELD and 0 PLANTATION tiles.
                [IMPORTANT] Farm: Farm 0 starts its actions.
                [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
                [IMPORTANT] Farm: Farm 0 finished its actions.
                [IMPORTANT] Farm: Farm 1 starts its actions.
                [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
                [IMPORTANT] Farm Action: Machine 3 performs IRRIGATING on tile 44 for 4 days.
                [IMPORTANT] Farm Action: Machine 3 performs IRRIGATING on tile 55 for 4 days.
                [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
                [IMPORTANT] Farm: Farm 1 finished its actions.
                [IMPORTANT] Incident: Incident 3 of type CLOUD_CREATION happened and affected tiles 5,6,10,11,15,16,17,20,21,22,26,27,28,32,33,38,39,44.
                [IMPORTANT] Incident: Incident 6 of type DROUGHT happened and affected tiles 10,11.
                [INFO] Simulation Info: Tick 21 started at tick 22 within the year.
                [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
                [IMPORTANT] Cloud Rain: Cloud 13 on tile 5 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 13 dissipates on tile 5.
                [IMPORTANT] Cloud Rain: Cloud 14 on tile 6 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 14 dissipates on tile 6.
                [IMPORTANT] Cloud Rain: Cloud 15 on tile 10 rained down 1000 L water.
                [INFO] Cloud Movement: Cloud 15 with 9000 L water moved from tile 10 to tile 11.
                [DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 95.
                [IMPORTANT] Cloud Union: Clouds 16 and 15 united to cloud 31 with 19000 L water and duration 10 on tile 11.
                [IMPORTANT] Cloud Rain: Cloud 17 on tile 15 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 17 dissipates on tile 15.
                [IMPORTANT] Cloud Rain: Cloud 18 on tile 16 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 18 dissipates on tile 16.
                [IMPORTANT] Cloud Rain: Cloud 19 on tile 17 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 19 dissipates on tile 17.
                [IMPORTANT] Cloud Rain: Cloud 20 on tile 20 rained down 1000 L water.
                [INFO] Cloud Movement: Cloud 20 with 9000 L water moved from tile 20 to tile 19.
                [DEBUG] Cloud Movement: On tile 20, the amount of sunlight is 95.
                [IMPORTANT] Cloud Rain: Cloud 20 on tile 19 rained down 700 L water.
                [INFO] Cloud Movement: Cloud 20 with 8300 L water moved from tile 19 to tile 18.
                [DEBUG] Cloud Movement: On tile 19, the amount of sunlight is 95.
                [IMPORTANT] Cloud Rain: Cloud 20 on tile 18 rained down 730 L water.
                [INFO] Cloud Movement: Cloud 20 with 7570 L water moved from tile 18 to tile 29.
                [DEBUG] Cloud Movement: On tile 18, the amount of sunlight is 95.
                [IMPORTANT] Cloud Rain: Cloud 20 on tile 29 rained down 7570 L water.
                [INFO] Cloud Dissipation: Cloud 20 dissipates on tile 29.
                [IMPORTANT] Cloud Rain: Cloud 21 on tile 21 rained down 1000 L water.
                [INFO] Cloud Movement: Cloud 21 with 9000 L water moved from tile 21 to tile 20.
                [DEBUG] Cloud Movement: On tile 21, the amount of sunlight is 95.
                [INFO] Cloud Movement: Cloud 21 with 9000 L water moved from tile 20 to tile 19.
                [DEBUG] Cloud Movement: On tile 20, the amount of sunlight is 92.
                [INFO] Cloud Movement: Cloud 21 with 9000 L water moved from tile 19 to tile 18.
                [DEBUG] Cloud Movement: On tile 19, the amount of sunlight is 92.
                [INFO] Cloud Movement: Cloud 21 with 9000 L water moved from tile 18 to tile 29.
                [DEBUG] Cloud Movement: On tile 18, the amount of sunlight is 92.
                [IMPORTANT] Cloud Rain: Cloud 21 on tile 29 rained down 9000 L water.
                [INFO] Cloud Dissipation: Cloud 21 dissipates on tile 29.
                [IMPORTANT] Cloud Rain: Cloud 22 on tile 22 rained down 1000 L water.
                [INFO] Cloud Movement: Cloud 22 with 9000 L water moved from tile 22 to tile 21.
                [DEBUG] Cloud Movement: On tile 22, the amount of sunlight is 95.
                [INFO] Cloud Movement: Cloud 22 with 9000 L water moved from tile 21 to tile 20.
                [DEBUG] Cloud Movement: On tile 21, the amount of sunlight is 92.
                [INFO] Cloud Movement: Cloud 22 with 9000 L water moved from tile 20 to tile 19.
                [DEBUG] Cloud Movement: On tile 20, the amount of sunlight is 89.
                [INFO] Cloud Movement: Cloud 22 with 9000 L water moved from tile 19 to tile 18.
                [DEBUG] Cloud Movement: On tile 19, the amount of sunlight is 89.
                [INFO] Cloud Movement: Cloud 22 with 9000 L water moved from tile 18 to tile 29.
                [DEBUG] Cloud Movement: On tile 18, the amount of sunlight is 89.
                [IMPORTANT] Cloud Rain: Cloud 22 on tile 29 rained down 9000 L water.
                [INFO] Cloud Dissipation: Cloud 22 dissipates on tile 29.
                [IMPORTANT] Cloud Rain: Cloud 23 on tile 26 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 23 dissipates on tile 26.
                [IMPORTANT] Cloud Rain: Cloud 24 on tile 27 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 24 dissipates on tile 27.
                [IMPORTANT] Cloud Rain: Cloud 25 on tile 28 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 25 dissipates on tile 28.
                [IMPORTANT] Cloud Rain: Cloud 26 on tile 32 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 26 dissipates on tile 32.
                [IMPORTANT] Cloud Rain: Cloud 27 on tile 33 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 27 dissipates on tile 33.
                [IMPORTANT] Cloud Rain: Cloud 28 on tile 38 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 28 dissipates on tile 38.
                [IMPORTANT] Cloud Rain: Cloud 29 on tile 39 rained down 10000 L water.
                [INFO] Cloud Dissipation: Cloud 29 dissipates on tile 39.
                [IMPORTANT] Cloud Rain: Cloud 30 on tile 44 rained down 100 L water.
                [INFO] Cloud Movement: Cloud 30 with 9900 L water moved from tile 44 to tile 43.
                [DEBUG] Cloud Movement: On tile 44, the amount of sunlight is 95.
                [IMPORTANT] Cloud Rain: Cloud 30 on tile 43 rained down 1750 L water.
                [INFO] Cloud Movement: Cloud 30 with 8150 L water moved from tile 43 to tile 42.
                [DEBUG] Cloud Movement: On tile 43, the amount of sunlight is 95.
                [IMPORTANT] Cloud Rain: Cloud 30 on tile 42 rained down 1840 L water.
                [INFO] Cloud Movement: Cloud 30 with 6310 L water moved from tile 42 to tile 41.
                [DEBUG] Cloud Movement: On tile 42, the amount of sunlight is 95.
                [IMPORTANT] Cloud Rain: Cloud 30 on tile 41 rained down 850 L water.
                [INFO] Cloud Movement: Cloud 30 with 5460 L water moved from tile 41 to tile 30.
                [DEBUG] Cloud Movement: On tile 41, the amount of sunlight is 95.
                [IMPORTANT] Cloud Rain: Cloud 30 on tile 30 rained down 5460 L water.
                [INFO] Cloud Dissipation: Cloud 30 dissipates on tile 30.
                [IMPORTANT] Cloud Rain: Cloud 31 on tile 11 rained down 1000 L water.
                [INFO] Cloud Movement: Cloud 31 with 18000 L water moved from tile 11 to tile 22.
                [DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 95.
                [INFO] Cloud Movement: Cloud 31 with 18000 L water moved from tile 22 to tile 21.
                [DEBUG] Cloud Movement: On tile 22, the amount of sunlight is 92.
                [INFO] Cloud Movement: Cloud 31 with 18000 L water moved from tile 21 to tile 20.
                [DEBUG] Cloud Movement: On tile 21, the amount of sunlight is 89.
                [INFO] Cloud Movement: Cloud 31 with 18000 L water moved from tile 20 to tile 19.
                [DEBUG] Cloud Movement: On tile 20, the amount of sunlight is 86.
                [INFO] Cloud Movement: Cloud 31 with 18000 L water moved from tile 19 to tile 18.
                [DEBUG] Cloud Movement: On tile 19, the amount of sunlight is 86.
                [INFO] Cloud Movement: Cloud 31 with 18000 L water moved from tile 18 to tile 29.
                [DEBUG] Cloud Movement: On tile 18, the amount of sunlight is 86.
                [IMPORTANT] Cloud Rain: Cloud 31 on tile 29 rained down 18000 L water.
                [INFO] Cloud Dissipation: Cloud 31 dissipates on tile 29.
                [IMPORTANT] Farm: Farm 0 starts its actions.
                [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
                [IMPORTANT] Farm: Farm 0 finished its actions.
                [IMPORTANT] Farm: Farm 1 starts its actions.
                [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
                [IMPORTANT] Farm: Farm 1 finished its actions.
                [INFO] Simulation Info: Tick 22 started at tick 23 within the year.
                [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
                [IMPORTANT] Farm: Farm 0 starts its actions.
                [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
                [IMPORTANT] Farm: Farm 0 finished its actions.
                [IMPORTANT] Farm: Farm 1 starts its actions.
                [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
                [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 44 for 4 days.
                [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 55 for 4 days.
                [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
                [IMPORTANT] Farm: Farm 1 finished its actions.
                [INFO] Simulation Info: Tick 23 started at tick 24 within the year.
                [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
                [IMPORTANT] Farm: Farm 0 starts its actions.
                [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
                [IMPORTANT] Farm: Farm 0 finished its actions.
                [IMPORTANT] Farm: Farm 1 starts its actions.
                [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
                [IMPORTANT] Farm: Farm 1 finished its actions.
                [INFO] Simulation Info: Tick 24 started at tick 1 within the year.
                [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
                [IMPORTANT] Farm: Farm 0 starts its actions.
                [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
                [IMPORTANT] Farm: Farm 0 finished its actions.
                [IMPORTANT] Farm: Farm 1 starts its actions.
                [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
                [IMPORTANT] Farm: Farm 1 finished its actions.
                [INFO] Simulation Info: Tick 25 started at tick 2 within the year.
                [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
                [IMPORTANT] Farm: Farm 0 starts its actions.
                [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
                [IMPORTANT] Farm: Farm 0 finished its actions.
                [IMPORTANT] Farm: Farm 1 starts its actions.
                [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
                [IMPORTANT] Farm: Farm 1 finished its actions.
                [INFO] Simulation Info: Tick 26 started at tick 3 within the year.
                [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
                [IMPORTANT] Farm: Farm 0 starts its actions.
                [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
                [IMPORTANT] Farm: Farm 0 finished its actions.
                [IMPORTANT] Farm: Farm 1 starts its actions.
                [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
                [IMPORTANT] Farm: Farm 1 finished its actions.
                [INFO] Simulation Info: Tick 27 started at tick 4 within the year.
                [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
                [IMPORTANT] Farm: Farm 0 starts its actions.
                [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
                [IMPORTANT] Farm: Farm 0 finished its actions.
                [IMPORTANT] Farm: Farm 1 starts its actions.
                [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
                [IMPORTANT] Farm: Farm 1 finished its actions.
                [INFO] Simulation Info: Tick 28 started at tick 5 within the year.
                [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
                [IMPORTANT] Farm: Farm 0 starts its actions.
                [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
                [IMPORTANT] Farm: Farm 0 finished its actions.
                [IMPORTANT] Farm: Farm 1 starts its actions.
                [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
                [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 44 for 4 days.
                [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 55 for 4 days.
                [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
                [IMPORTANT] Farm: Farm 1 finished its actions.
                [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 1350000 g of WHEAT.
                [INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 1350000 g of WHEAT.
                [INFO] Simulation Info: Tick 29 started at tick 6 within the year.
                [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
                [IMPORTANT] Farm: Farm 0 starts its actions.
                [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
                [IMPORTANT] Farm: Farm 0 finished its actions.
                [IMPORTANT] Farm: Farm 1 starts its actions.
                [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
                [IMPORTANT] Farm: Farm 1 finished its actions.
                [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 1215000 g of WHEAT.
                [INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 1215000 g of WHEAT.
                [IMPORTANT] Simulation Info: Simulation ended at tick 30.
                [IMPORTANT] Simulation Info: Simulation statistics are calculated.
                [IMPORTANT] Simulation Statistics: Farm 0 collected 200123 g of harvest.
                [IMPORTANT] Simulation Statistics: Farm 1 collected 2682786 g of harvest.
                [IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 1865354 g.
                [IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
                [IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 722442 g.
                [IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 295113 g.
                [IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
                [IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.
                [IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.
                [IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.
                [IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 2430000 g.
    """.trimIndent()

    override suspend fun run() {
        val expectedLines = expectedOutput.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        var num = 0
        while (num < 22) {
            num++
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        }
        for (line in expectedLines) {
            assertNextLine(line)
        }
    }
}
