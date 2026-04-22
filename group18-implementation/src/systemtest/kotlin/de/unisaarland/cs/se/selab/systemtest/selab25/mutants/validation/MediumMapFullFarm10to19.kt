package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.validation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * wrong neighbor adjacent tiles
 */
class MediumMapFullFarm10to19 : ExampleSystemTestExtension() {
    override val name = "MediumMapFullFarm Fields tick 10 to 19"
    override val description = "Checks normal plantation plant actions/periods without incidents."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullTest/mediumFarmFullFields.json"
    override val scenario = "FullTest/mediumScenarioFull.json"
    override val map = "FullTest/mediumMapFullFields.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 21
    override val startYearTick = 1

    val expectedOutput = """
        [INFO] Soil Moisture: The soil moisture is below threshold in 4 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
        [IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 10 for 4 days.
        [IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 11 for 4 days.
        [IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 21 for 4 days.
        [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13.
        [IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 22 for 4 days.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9.
        [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 41 for 4 days.
        [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 42 for 4 days.
        [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 53 for 4 days.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 54 for 4 days.
        [IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [IMPORTANT] Incident: Incident 10 of type DROUGHT happened and affected tiles 10,11,19,20,21,22.
        [IMPORTANT] Incident: Incident 11 of type CLOUD_CREATION happened and affected tiles 19,24,25,29,30,31,35,36,41.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 0 g of PUMPKIN.
        [INFO] Harvest Estimate: Harvest estimate on tile 11 changed to 0 g of PUMPKIN.
        [INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 274518 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 19 changed to 0 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 0 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 21 changed to 0 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 22 changed to 0 g of PUMPKIN.
        [INFO] Harvest Estimate: Harvest estimate on tile 41 changed to 729000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 729000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 247066 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 247066 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 53 changed to 729000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 54 changed to 729000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 222359 g of OAT.
        [INFO] Simulation Info: Tick 11 started at tick 12 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Cloud Rain: Cloud 3 on tile 19 rained down 1000 L water.
        [INFO] Cloud Movement: Cloud 3 with 9000 L water moved from tile 19 to tile 18.
        [DEBUG] Cloud Movement: On tile 19, the amount of sunlight is 165.
        [IMPORTANT] Cloud Rain: Cloud 3 on tile 18 rained down 200 L water.
        [INFO] Cloud Movement: Cloud 3 with 8800 L water moved from tile 18 to tile 29.
        [DEBUG] Cloud Movement: On tile 18, the amount of sunlight is 165.
        [IMPORTANT] Cloud Union: Clouds 6 and 3 united to cloud 12 with 18800 L water and duration 15 on tile 29.
        [IMPORTANT] Cloud Rain: Cloud 4 on tile 24 rained down 10000 L water.
        [INFO] Cloud Dissipation: Cloud 4 dissipates on tile 24.
        [IMPORTANT] Cloud Rain: Cloud 5 on tile 25 rained down 10000 L water.
        [INFO] Cloud Dissipation: Cloud 5 dissipates on tile 25.
        [IMPORTANT] Cloud Rain: Cloud 7 on tile 30 rained down 10000 L water.
        [INFO] Cloud Dissipation: Cloud 7 dissipates on tile 30.
        [IMPORTANT] Cloud Rain: Cloud 8 on tile 31 rained down 10000 L water.
        [INFO] Cloud Dissipation: Cloud 8 dissipates on tile 31.
        [IMPORTANT] Cloud Rain: Cloud 9 on tile 35 rained down 10000 L water.
        [INFO] Cloud Dissipation: Cloud 9 dissipates on tile 35.
        [IMPORTANT] Cloud Rain: Cloud 10 on tile 36 rained down 10000 L water.
        [INFO] Cloud Dissipation: Cloud 10 dissipates on tile 36.
        [IMPORTANT] Cloud Rain: Cloud 11 on tile 41 rained down 400 L water.
        [INFO] Cloud Movement: Cloud 11 with 9600 L water moved from tile 41 to tile 30.
        [DEBUG] Cloud Movement: On tile 41, the amount of sunlight is 165.
        [IMPORTANT] Cloud Rain: Cloud 11 on tile 30 rained down 9600 L water.
        [INFO] Cloud Dissipation: Cloud 11 dissipates on tile 30.
        [IMPORTANT] Cloud Rain: Cloud 12 on tile 29 rained down 18800 L water.
        [INFO] Cloud Dissipation: Cloud 12 dissipates on tile 29.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 7,9.
        [IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 40 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 3 has sowed PUMPKIN according to sowing plan 7.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 200123 g of OAT.
        [DEBUG] Harvest Estimate: Required actions on tile 40 were not performed: IRRIGATING.
        [INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 449800 g of PUMPKIN.
        [INFO] Harvest Estimate: Harvest estimate on tile 41 changed to 656100 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 656100 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 180110 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 180110 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 53 changed to 656100 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 54 changed to 656100 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 162099 g of OAT.
        [INFO] Simulation Info: Tick 12 started at tick 13 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
        [IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 18 for 4 days.
        [IMPORTANT] Farm Harvest: Machine 1 has collected 200123 g of OAT harvest.
        [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13.
        [IMPORTANT] Farm Machine: Machine 1 unloads 200123 g of OAT harvest in the shed.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9.
        [IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 43 for 4 days.
        [IMPORTANT] Farm Harvest: Machine 3 has collected 180110 g of OAT harvest.
        [IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 44 for 4 days.
        [IMPORTANT] Farm Harvest: Machine 3 has collected 180110 g of OAT harvest.
        [IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 55 for 4 days.
        [IMPORTANT] Farm Harvest: Machine 3 has collected 162099 g of OAT harvest.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm Machine: Machine 3 unloads 522319 g of OAT harvest in the shed.
        [IMPORTANT] Farm Action: Machine 4 performs IRRIGATING on tile 40 for 4 days.
        [IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 404820 g of PUMPKIN.
        [DEBUG] Harvest Estimate: Required actions on tile 41 were not performed: WEEDING.
        [INFO] Harvest Estimate: Harvest estimate on tile 41 changed to 531441 g of POTATO.
        [DEBUG] Harvest Estimate: Required actions on tile 42 were not performed: WEEDING.
        [INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 531441 g of POTATO.
        [DEBUG] Harvest Estimate: Required actions on tile 53 were not performed: WEEDING.
        [INFO] Harvest Estimate: Harvest estimate on tile 53 changed to 531441 g of POTATO.
        [DEBUG] Harvest Estimate: Required actions on tile 54 were not performed: WEEDING.
        [INFO] Harvest Estimate: Harvest estimate on tile 54 changed to 531441 g of POTATO.
        [INFO] Simulation Info: Tick 13 started at tick 14 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
        [IMPORTANT] Farm Action: Machine 3 performs IRRIGATING on tile 53 for 4 days.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 40 for 4 days.
        [IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles .
        [IMPORTANT] Incident: Incident 7 of type BEE_HAPPY happened and affected tiles .
        [INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 364338 g of PUMPKIN.
        [INFO] Harvest Estimate: Harvest estimate on tile 41 changed to 478296 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 478296 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 53 changed to 478296 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 54 changed to 478296 g of POTATO.
        [INFO] Simulation Info: Tick 14 started at tick 15 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
        [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 41 for 4 days.
        [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 42 for 4 days.
        [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 53 for 4 days.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 54 for 4 days.
        [IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 327904 g of PUMPKIN.
        [INFO] Simulation Info: Tick 15 started at tick 16 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
        [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 40 for 4 days.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 295113 g of PUMPKIN.
        [INFO] Simulation Info: Tick 16 started at tick 17 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
        [IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 40 for 4 days.
        [IMPORTANT] Farm Harvest: Machine 3 has collected 295113 g of PUMPKIN harvest.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm Machine: Machine 3 unloads 295113 g of PUMPKIN harvest in the shed.
        [IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 41 for 4 days.
        [IMPORTANT] Farm Harvest: Machine 4 has collected 478296 g of POTATO harvest.
        [IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 42 for 4 days.
        [IMPORTANT] Farm Harvest: Machine 4 has collected 478296 g of POTATO harvest.
        [IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 53 for 4 days.
        [IMPORTANT] Farm Harvest: Machine 4 has collected 478296 g of POTATO harvest.
        [IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.
        [IMPORTANT] Farm Machine: Machine 4 unloads 1434888 g of POTATO harvest in the shed.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [DEBUG] Harvest Estimate: Required actions on tile 54 were not performed: WEEDING.
        [INFO] Harvest Estimate: Harvest estimate on tile 54 changed to 430466 g of POTATO.
        [INFO] Simulation Info: Tick 17 started at tick 18 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
        [IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 54 for 4 days.
        [IMPORTANT] Farm Harvest: Machine 3 has collected 430466 g of POTATO harvest.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm Machine: Machine 3 unloads 430466 g of POTATO harvest in the shed.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Simulation Info: Tick 18 started at tick 19 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,3,31.
        [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 10 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 1.
        [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 11 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 1.
        [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 21 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 1.
        [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13.
        [IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 22 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 2 has sowed WHEAT according to sowing plan 1.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9,10.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [DEBUG] Harvest Estimate: Required actions on tile 10 were not performed: IRRIGATING.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 0 g of WHEAT.
        [DEBUG] Harvest Estimate: Required actions on tile 11 were not performed: IRRIGATING.
        [INFO] Harvest Estimate: Harvest estimate on tile 11 changed to 0 g of WHEAT.
        [DEBUG] Harvest Estimate: Required actions on tile 21 were not performed: IRRIGATING.
        [INFO] Harvest Estimate: Harvest estimate on tile 21 changed to 0 g of WHEAT.
        [DEBUG] Harvest Estimate: Required actions on tile 22 were not performed: IRRIGATING.
        [INFO] Harvest Estimate: Harvest estimate on tile 22 changed to 0 g of WHEAT.
        [INFO] Simulation Info: Tick 19 started at tick 20 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 3,31.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5,9,10.
        [IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 44 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 3 has sowed WHEAT according to sowing plan 5.
        [IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 55 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 3 has sowed WHEAT according to sowing plan 5.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm: Farm 1 finished its actions.
    """.trimIndent()

    override suspend fun run() {
        val expectedLines = expectedOutput.split("\n").map { it.trim() }.filter { it.isNotEmpty() }
        var num = 0
        while (num < 12) {
            num++
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        }
        for (line in expectedLines) {
            assertNextLine(line)
        }
    }
}
