package de.unisaarland.cs.se.selab.systemtest.selab25.mutants

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Potential mutant test for complex scenario with 2 farms.
 */
class ThisIsHuge : ExampleSystemTestExtension() {
    override val name = "ThisIsHuge"
    override val description = "A complex scenario with 2 farms."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "mutants/ThisIsHuge/farms.json"
    override val scenario = "mutants/ThisIsHuge/scenario.json"
    override val map = "mutants/ThisIsHuge/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 1

    val expectedOutput = """
[INFO] Initialization Info: map.json successfully parsed and validated.
[INFO] Initialization Info: farms.json successfully parsed and validated.
[INFO] Initialization Info: scenario.json successfully parsed and validated.
[INFO] Simulation Info: Simulation started at tick 1 within the year.
[INFO] Simulation Info: Tick 0 started at tick 1 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 1530000 g of APPLE.
[INFO] Simulation Info: Tick 1 started at tick 2 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[IMPORTANT] Incident: Incident 6 of type ANIMAL_ATTACK happened and affected tiles 20,164.
[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 1239300 g of APPLE.
[INFO] Harvest Estimate: Harvest estimate on tile 164 changed to 600000 g of GRAPE.
[INFO] Simulation Info: Tick 2 started at tick 3 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 1003833 g of APPLE.
[INFO] Simulation Info: Tick 3 started at tick 4 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: CUTTING.
[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 406552 g of APPLE.
[INFO] Simulation Info: Tick 4 started at tick 5 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 296375 g of APPLE.
[INFO] Simulation Info: Tick 5 started at tick 6 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 216056 g of APPLE.
[INFO] Simulation Info: Tick 6 started at tick 7 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 212 performs MOWING on tile 160 for 1 days.
[IMPORTANT] Farm Action: Machine 212 performs MOWING on tile 162 for 1 days.
[IMPORTANT] Farm Action: Machine 212 performs MOWING on tile 164 for 1 days.
[IMPORTANT] Farm Machine: Machine 212 is finished and returns to the shed at 170.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: 601.
[IMPORTANT] Farm Action: Machine 301 performs SOWING on tile 42 for 1 days.
[IMPORTANT] Farm Sowing: Machine 301 has sowed POTATO according to sowing plan 601.
[IMPORTANT] Farm Action: Machine 301 performs SOWING on tile 44 for 1 days.
[IMPORTANT] Farm Sowing: Machine 301 has sowed POTATO according to sowing plan 601.
[IMPORTANT] Farm Machine: Machine 301 is finished and returns to the shed at 40.
[IMPORTANT] Farm: Farm 2 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 157504 g of APPLE.
[INFO] Simulation Info: Tick 7 started at tick 8 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 2 FIELD and 3 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 213 performs IRRIGATING on tile 160 for 1 days.
[IMPORTANT] Farm Action: Machine 213 performs IRRIGATING on tile 162 for 1 days.
[IMPORTANT] Farm Action: Machine 213 performs IRRIGATING on tile 164 for 1 days.
[IMPORTANT] Farm Machine: Machine 213 is finished and returns to the shed at 170.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 302 performs IRRIGATING on tile 42 for 1 days.
[IMPORTANT] Farm Action: Machine 302 performs IRRIGATING on tile 44 for 1 days.
[IMPORTANT] Farm Machine: Machine 302 is finished and returns to the shed at 40.
[IMPORTANT] Farm: Farm 2 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 114819 g of APPLE.
[INFO] Simulation Info: Tick 8 started at tick 9 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 301 performs WEEDING on tile 42 for 1 days.
[IMPORTANT] Farm Action: Machine 301 performs WEEDING on tile 44 for 1 days.
[IMPORTANT] Farm Machine: Machine 301 is finished and returns to the shed at 40.
[IMPORTANT] Farm: Farm 2 finished its actions.
[IMPORTANT] Incident: Incident 4 of type BEE_HAPPY happened and affected tiles .
[IMPORTANT] Incident: Incident 7 of type BEE_HAPPY happened and affected tiles .
[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 75331 g of APPLE.
[INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 900000 g of POTATO.
[INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 900000 g of POTATO.
[INFO] Simulation Info: Tick 9 started at tick 10 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[IMPORTANT] Incident: Incident 5 of type BEE_HAPPY happened and affected tiles .
[IMPORTANT] Incident: Incident 8 of type BEE_HAPPY happened and affected tiles .
[IMPORTANT] Incident: Incident 12 of type CITY_EXPANSION happened and affected tiles 44.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 0 g of APPLE.
[INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 810000 g of POTATO.
[INFO] Simulation Info: Tick 10 started at tick 11 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 501.
[IMPORTANT] Farm Action: Machine 101 performs SOWING on tile 10 for 1 days.
[IMPORTANT] Farm Sowing: Machine 101 has sowed PUMPKIN according to sowing plan 501.
[IMPORTANT] Farm Action: Machine 101 performs SOWING on tile 12 for 1 days.
[IMPORTANT] Farm Sowing: Machine 101 has sowed PUMPKIN according to sowing plan 501.
[IMPORTANT] Farm Action: Machine 101 performs SOWING on tile 14 for 1 days.
[IMPORTANT] Farm Sowing: Machine 101 has sowed PUMPKIN according to sowing plan 501.
[IMPORTANT] Farm Machine: Machine 101 is finished and returns to the shed at 1.
[IMPORTANT] Farm Action: Machine 102 performs SOWING on tile 16 for 2 days.
[IMPORTANT] Farm Sowing: Machine 102 has sowed PUMPKIN according to sowing plan 501.
[IMPORTANT] Farm Action: Machine 102 performs SOWING on tile 18 for 2 days.
[IMPORTANT] Farm Sowing: Machine 102 has sowed PUMPKIN according to sowing plan 501.
[IMPORTANT] Farm Machine: Machine 102 is finished and returns to the shed at 30.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 301 performs WEEDING on tile 42 for 1 days.
[IMPORTANT] Farm Machine: Machine 301 is finished and returns to the shed at 40.
[IMPORTANT] Farm: Farm 2 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 10 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 449850 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 12 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 449850 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 14 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 14 changed to 449850 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 16 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 16 changed to 449850 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 18 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 449850 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 729000 g of POTATO.
[INFO] Simulation Info: Tick 11 started at tick 12 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 5 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 105 performs IRRIGATING on tile 10 for 1 days.
[IMPORTANT] Farm Action: Machine 105 performs IRRIGATING on tile 12 for 1 days.
[IMPORTANT] Farm Action: Machine 105 performs IRRIGATING on tile 14 for 1 days.
[IMPORTANT] Farm Machine: Machine 105 is finished and returns to the shed at 1.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 404865 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 404865 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 14 changed to 404865 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 16 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 16 changed to 404665 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 18 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 404665 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 656100 g of POTATO.
[INFO] Simulation Info: Tick 12 started at tick 13 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 2 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 103 performs WEEDING on tile 10 for 1 days.
[IMPORTANT] Farm Action: Machine 103 performs WEEDING on tile 12 for 1 days.
[IMPORTANT] Farm Action: Machine 103 performs WEEDING on tile 14 for 1 days.
[IMPORTANT] Farm Machine: Machine 103 is finished and returns to the shed at 1.
[IMPORTANT] Farm Action: Machine 105 performs IRRIGATING on tile 16 for 1 days.
[IMPORTANT] Farm Action: Machine 105 performs IRRIGATING on tile 18 for 1 days.
[IMPORTANT] Farm Machine: Machine 105 is finished and returns to the shed at 1.
[IMPORTANT] Farm Action: Machine 212 performs MOWING on tile 160 for 1 days.
[IMPORTANT] Farm Action: Machine 212 performs MOWING on tile 162 for 1 days.
[IMPORTANT] Farm Action: Machine 212 performs MOWING on tile 164 for 1 days.
[IMPORTANT] Farm Machine: Machine 212 is finished and returns to the shed at 170.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 301 performs WEEDING on tile 42 for 1 days.
[IMPORTANT] Farm Machine: Machine 301 is finished and returns to the shed at 40.
[IMPORTANT] Farm: Farm 2 finished its actions.
[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles .
[IMPORTANT] Incident: Incident 10 of type DROUGHT happened and affected tiles 16.
[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 364378 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 364378 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 14 changed to 364378 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 16 changed to 0 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 18 were not performed: WEEDING.
[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 327778 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 590490 g of POTATO.
[INFO] Simulation Info: Tick 13 started at tick 14 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 211 performs CUTTING on tile 160 for 1 days.
[IMPORTANT] Farm Action: Machine 211 performs CUTTING on tile 162 for 1 days.
[IMPORTANT] Farm Action: Machine 211 performs CUTTING on tile 164 for 1 days.
[IMPORTANT] Farm Machine: Machine 211 is finished and returns to the shed at 170.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 302 performs IRRIGATING on tile 42 for 1 days.
[IMPORTANT] Farm Machine: Machine 302 is finished and returns to the shed at 40.
[IMPORTANT] Farm: Farm 2 finished its actions.
[IMPORTANT] Incident: Incident 3 of type BEE_HAPPY happened and affected tiles 18.
[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 327940 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 327940 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 14 changed to 327940 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 590000 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 531441 g of POTATO.
[INFO] Simulation Info: Tick 14 started at tick 15 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 103 performs WEEDING on tile 10 for 1 days.
[IMPORTANT] Farm Action: Machine 103 performs WEEDING on tile 12 for 1 days.
[IMPORTANT] Farm Action: Machine 103 performs WEEDING on tile 14 for 1 days.
[IMPORTANT] Farm Machine: Machine 103 is finished and returns to the shed at 1.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 301 performs WEEDING on tile 42 for 1 days.
[IMPORTANT] Farm Machine: Machine 301 is finished and returns to the shed at 40.
[IMPORTANT] Farm: Farm 2 finished its actions.
[IMPORTANT] Incident: Incident 9 of type BEE_HAPPY happened and affected tiles .
[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 295146 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 295146 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 14 changed to 295146 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 18 were not performed: WEEDING.
[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 477900 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Simulation Info: Tick 15 started at tick 16 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 213 performs IRRIGATING on tile 160 for 1 days.
[IMPORTANT] Farm Action: Machine 213 performs IRRIGATING on tile 162 for 1 days.
[IMPORTANT] Farm Action: Machine 213 performs IRRIGATING on tile 164 for 1 days.
[IMPORTANT] Farm Machine: Machine 213 is finished and returns to the shed at 170.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 265631 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 265631 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 14 changed to 265631 g of PUMPKIN.
[INFO] Harvest Estimate: Harvest estimate on tile 18 changed to 430110 g of PUMPKIN.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Simulation Info: Tick 16 started at tick 17 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 3 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 210 performs HARVESTING on tile 160 for 1 days.
[IMPORTANT] Farm Harvest: Machine 210 has collected 1200000 g of GRAPE harvest.
[IMPORTANT] Farm Action: Machine 210 performs HARVESTING on tile 162 for 1 days.
[IMPORTANT] Farm Harvest: Machine 210 has collected 1200000 g of GRAPE harvest.
[IMPORTANT] Farm Action: Machine 210 performs HARVESTING on tile 164 for 1 days.
[IMPORTANT] Farm Harvest: Machine 210 has collected 600000 g of GRAPE harvest.
[IMPORTANT] Farm Machine: Machine 210 is finished and returns to the shed at 170.
[IMPORTANT] Farm Machine: Machine 210 unloads 3000000 g of GRAPE harvest in the shed.
[IMPORTANT] Farm Action: Machine 104 performs HARVESTING on tile 10 for 1 days.
[IMPORTANT] Farm Harvest: Machine 104 has collected 265631 g of PUMPKIN harvest.
[IMPORTANT] Farm Action: Machine 104 performs HARVESTING on tile 12 for 1 days.
[IMPORTANT] Farm Harvest: Machine 104 has collected 265631 g of PUMPKIN harvest.
[IMPORTANT] Farm Action: Machine 104 performs HARVESTING on tile 14 for 1 days.
[IMPORTANT] Farm Harvest: Machine 104 has collected 265631 g of PUMPKIN harvest.
[IMPORTANT] Farm Machine: Machine 104 is finished and returns to the shed at 1.
[IMPORTANT] Farm Machine: Machine 104 unloads 796893 g of PUMPKIN harvest in the shed.
[IMPORTANT] Farm Action: Machine 103 performs WEEDING on tile 18 for 1 days.
[IMPORTANT] Farm Machine: Machine 103 is finished and returns to the shed at 1.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 302 performs HARVESTING on tile 42 for 1 days.
[IMPORTANT] Farm Harvest: Machine 302 has collected 531441 g of POTATO harvest.
[IMPORTANT] Farm Machine: Machine 302 is finished but failed to return.
[IMPORTANT] Farm: Farm 2 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Simulation Info: Tick 17 started at tick 18 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 104 performs HARVESTING on tile 18 for 1 days.
[IMPORTANT] Farm Harvest: Machine 104 has collected 430110 g of PUMPKIN harvest.
[IMPORTANT] Farm Machine: Machine 104 is finished and returns to the shed at 30.
[IMPORTANT] Farm Machine: Machine 104 unloads 430110 g of PUMPKIN harvest in the shed.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Simulation Info: Tick 18 started at tick 19 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Simulation Info: Tick 19 started at tick 20 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[IMPORTANT] Incident: Incident 1 of type BROKEN_MACHINE happened and affected tiles 1.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Simulation Info: Tick 20 started at tick 21 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Harvest Estimate: Harvest estimate on tile 160 changed to 1200000 g of GRAPE.
[INFO] Harvest Estimate: Harvest estimate on tile 162 changed to 1200000 g of GRAPE.
[INFO] Harvest Estimate: Harvest estimate on tile 164 changed to 1200000 g of GRAPE.
[INFO] Simulation Info: Tick 21 started at tick 22 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Simulation Info: Tick 22 started at tick 23 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[INFO] Simulation Info: Tick 23 started at tick 24 within the year.
[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 4 PLANTATION tiles.
[IMPORTANT] Farm: Farm 1 starts its actions.
[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm Action: Machine 213 performs IRRIGATING on tile 160 for 1 days.
[IMPORTANT] Farm Action: Machine 213 performs IRRIGATING on tile 162 for 1 days.
[IMPORTANT] Farm Action: Machine 213 performs IRRIGATING on tile 164 for 1 days.
[IMPORTANT] Farm Machine: Machine 213 is finished and returns to the shed at 170.
[IMPORTANT] Farm: Farm 1 finished its actions.
[IMPORTANT] Farm: Farm 2 starts its actions.
[DEBUG] Farm: Farm 2 has the following active sowing plans it intends to pursue in this tick: .
[IMPORTANT] Farm: Farm 2 finished its actions.
[DEBUG] Harvest Estimate: Required actions on tile 20 were not performed: IRRIGATING.
[IMPORTANT] Simulation Info: Simulation ended at tick 24.
[IMPORTANT] Simulation Info: Simulation statistics are calculated.
[IMPORTANT] Simulation Statistics: Farm 1 collected 4227003 g of harvest.
[IMPORTANT] Simulation Statistics: Farm 2 collected 531441 g of harvest.
[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 531441 g.
[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 1227003 g.
[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 3000000 g.
[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.
[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 3600000 g.
    """.trimIndent()

    override suspend fun run() {
        val expectedLines = expectedOutput.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

        for (line in expectedLines) {
            assertNextLine(line)
        }
    }
}
