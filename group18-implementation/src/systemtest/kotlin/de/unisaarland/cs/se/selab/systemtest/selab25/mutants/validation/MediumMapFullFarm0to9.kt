package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.validation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class MediumMapFullFarm0to9 : ExampleSystemTestExtension() {
    override val name = "MediumMapFullFarm Fields tick 0 to 9"
    override val description = "Checks normal plantation plant actions/periods without incidents."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullTest/mediumFarmFullFields.json"
    override val scenario = "FullTest/mediumScenarioFull.json"
    override val map = "FullTest/mediumMapFullFields.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 11
    override val startYearTick = 1

    val expectedOutput = """
        [INFO] Initialization Info: mediumMapFullFields.json successfully parsed and validated.
        [INFO] Initialization Info: mediumFarmFullFields.json successfully parsed and validated.
        [INFO] Initialization Info: mediumScenarioFull.json successfully parsed and validated.
        [INFO] Simulation Info: Simulation started at tick 1 within the year.
        [INFO] Simulation Info: Tick 0 started at tick 1 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Cloud Rain: Cloud 1 on tile 7 rained down 70 L water.
        [INFO] Cloud Movement: Cloud 1 with 9930 L water moved from tile 7 to tile 8.
        [DEBUG] Cloud Movement: On tile 7, the amount of sunlight is 95.
        [IMPORTANT] Cloud Rain: Cloud 1 on tile 8 rained down 70 L water.
        [INFO] Cloud Movement: Cloud 1 with 9860 L water moved from tile 8 to tile 9.
        [DEBUG] Cloud Movement: On tile 8, the amount of sunlight is 95.
        [INFO] Cloud Dissipation: Cloud 1 got stuck on tile 9.
        [IMPORTANT] Cloud Rain: Cloud 2 on tile 10 rained down 70 L water.
        [INFO] Cloud Movement: Cloud 2 with 9930 L water moved from tile 10 to tile 11.
        [DEBUG] Cloud Movement: On tile 10, the amount of sunlight is 95.
        [IMPORTANT] Cloud Rain: Cloud 2 on tile 11 rained down 70 L water.
        [INFO] Cloud Movement: Cloud 2 with 9860 L water moved from tile 11 to tile 22.
        [DEBUG] Cloud Movement: On tile 11, the amount of sunlight is 95.
        [IMPORTANT] Cloud Rain: Cloud 2 on tile 22 rained down 70 L water.
        [INFO] Cloud Movement: Cloud 2 with 9790 L water moved from tile 22 to tile 21.
        [DEBUG] Cloud Movement: On tile 22, the amount of sunlight is 95.
        [IMPORTANT] Cloud Rain: Cloud 2 on tile 21 rained down 70 L water.
        [INFO] Cloud Movement: Cloud 2 with 9720 L water moved from tile 21 to tile 20.
        [DEBUG] Cloud Movement: On tile 21, the amount of sunlight is 95.
        [IMPORTANT] Cloud Rain: Cloud 2 on tile 20 rained down 70 L water.
        [INFO] Cloud Movement: Cloud 2 with 9650 L water moved from tile 20 to tile 19.
        [DEBUG] Cloud Movement: On tile 20, the amount of sunlight is 95.
        [IMPORTANT] Cloud Rain: Cloud 2 on tile 19 rained down 70 L water.
        [INFO] Cloud Movement: Cloud 2 with 9580 L water moved from tile 19 to tile 18.
        [DEBUG] Cloud Movement: On tile 19, the amount of sunlight is 95.
        [IMPORTANT] Cloud Rain: Cloud 2 on tile 18 rained down 70 L water.
        [INFO] Cloud Movement: Cloud 2 with 9510 L water moved from tile 18 to tile 29.
        [DEBUG] Cloud Movement: On tile 18, the amount of sunlight is 95.
        [IMPORTANT] Cloud Rain: Cloud 2 on tile 29 rained down 9510 L water.
        [INFO] Cloud Dissipation: Cloud 2 dissipates on tile 29.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 30.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 18.
        [IMPORTANT] Incident: Incident 5 of type BROKEN_MACHINE happened and affected tiles 13.
        [INFO] Simulation Info: Tick 1 started at tick 2 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 30.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Simulation Info: Tick 2 started at tick 3 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 30.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Simulation Info: Tick 3 started at tick 4 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 30.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 8,9.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Simulation Info: Tick 4 started at tick 5 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 2,30.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 8,9.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Simulation Info: Tick 5 started at tick 6 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 2,30.
        [IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 7 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 2 has sowed OAT according to sowing plan 2.
        [IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 8 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 2 has sowed OAT according to sowing plan 2.
        [IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 18 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 2 has sowed OAT according to sowing plan 2.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 6,8,9.
        [IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 43 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 3 has sowed OAT according to sowing plan 6.
        [IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 44 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 3 has sowed OAT according to sowing plan 6.
        [IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 55 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 3 has sowed OAT according to sowing plan 6.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 1080000 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 8 changed to 1080000 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 1080000 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 1080000 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 1080000 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 1080000 g of OAT.
        [INFO] Simulation Info: Tick 6 started at tick 7 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 30.
        [IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 7 for 4 days.
        [IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 8 for 4 days.
        [IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 18 for 4 days.
        [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 8,9.
        [IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 41 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 3 has sowed POTATO according to sowing plan 8.
        [IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 42 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 3 has sowed POTATO according to sowing plan 8.
        [IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 53 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 3 has sowed POTATO according to sowing plan 8.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm Action: Machine 4 performs SOWING on tile 54 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 4 has sowed POTATO according to sowing plan 8.
        [IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [IMPORTANT] Incident: Incident 4 of type CITY_EXPANSION happened and affected tiles 8.
        [IMPORTANT] Incident: Incident 9 of type CITY_EXPANSION happened and affected tiles 7.
        [INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 874800 g of OAT.
        [DEBUG] Harvest Estimate: Required actions on tile 43 were not performed: WEEDING.
        [INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 787320 g of OAT.
        [DEBUG] Harvest Estimate: Required actions on tile 44 were not performed: WEEDING.
        [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 787320 g of OAT.
        [DEBUG] Harvest Estimate: Required actions on tile 55 were not performed: WEEDING.
        [INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 787320 g of OAT.
        [INFO] Simulation Info: Tick 7 started at tick 8 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 2 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 30.
        [IMPORTANT] Farm Action: Machine 1 performs WEEDING on tile 18 for 4 days.
        [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9.
        [IMPORTANT] Farm Action: Machine 3 performs IRRIGATING on tile 41 for 4 days.
        [IMPORTANT] Farm Action: Machine 3 performs IRRIGATING on tile 53 for 4 days.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 43 for 4 days.
        [IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 44 for 4 days.
        [IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 55 for 4 days.
        [IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 708588 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 637729 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 637729 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 637729 g of OAT.
        [INFO] Simulation Info: Tick 8 started at tick 9 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 4,30.
        [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 19 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 1 has sowed POTATO according to sowing plan 4.
        [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 20 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 1 has sowed POTATO according to sowing plan 4.
        [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 21 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 1 has sowed POTATO according to sowing plan 4.
        [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13.
        [IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 18 for 4 days.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9.
        [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 41 for 4 days.
        [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 42 for 4 days.
        [IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 43 for 4 days.
        [IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.
        [IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 44 for 4 days.
        [IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 54 for 4 days.
        [IMPORTANT] Farm Action: Machine 4 performs WEEDING on tile 53 for 4 days.
        [IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 516560 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 19 changed to 900000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 900000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 21 changed to 900000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 41 changed to 900000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 900000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 464904 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 464904 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 53 changed to 900000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 54 changed to 900000 g of POTATO.
        [DEBUG] Harvest Estimate: Required actions on tile 55 were not performed: WEEDING.
        [INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 418413 g of OAT.
        [INFO] Simulation Info: Tick 9 started at tick 10 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 4 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 0 starts its actions.
        [DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 30.
        [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 10 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 1 has sowed PUMPKIN according to sowing plan 30.
        [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 11 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 1 has sowed PUMPKIN according to sowing plan 30.
        [IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 22 for 4 days.
        [IMPORTANT] Farm Sowing: Machine 1 has sowed PUMPKIN according to sowing plan 30.
        [IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13.
        [IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 18 for 4 days.
        [IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 19 for 4 days.
        [IMPORTANT] Farm Action: Machine 2 performs IRRIGATING on tile 20 for 4 days.
        [IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.
        [IMPORTANT] Farm: Farm 0 finished its actions.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 9.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [IMPORTANT] Incident: Incident 8 of type BEE_HAPPY happened and affected tiles .
        [DEBUG] Harvest Estimate: Required actions on tile 10 were not performed: IRRIGATING.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 449900 g of PUMPKIN.
        [DEBUG] Harvest Estimate: Required actions on tile 11 were not performed: IRRIGATING.
        [INFO] Harvest Estimate: Harvest estimate on tile 11 changed to 449900 g of PUMPKIN.
        [INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 376571 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 19 changed to 810000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 810000 g of POTATO.
        [DEBUG] Harvest Estimate: Required actions on tile 21 were not performed: IRRIGATING.
        [INFO] Harvest Estimate: Harvest estimate on tile 21 changed to 809950 g of POTATO.
        [DEBUG] Harvest Estimate: Required actions on tile 22 were not performed: IRRIGATING.
        [INFO] Harvest Estimate: Harvest estimate on tile 22 changed to 449900 g of PUMPKIN.
        [INFO] Harvest Estimate: Harvest estimate on tile 41 changed to 810000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 810000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 338913 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 338913 g of OAT.
        [INFO] Harvest Estimate: Harvest estimate on tile 53 changed to 810000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 54 changed to 810000 g of POTATO.
        [INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 305021 g of OAT.
    """.trimIndent()

    override suspend fun run() {
        val expectedLines = expectedOutput.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

        for (line in expectedLines) {
            assertNextLine(line)
        }
    }
}
