package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.validation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class PlantationPerPlantationPlant8 : ExampleSystemTestExtension() {
    override val name = "PlantationPerPlantationPlant 8"
    override val description = "Checks normal plantation plant actions/periods without incidents."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "mutants/PlantationPerPlantationPlant/farms.json"
    override val scenario = "mutants/PlantationPerPlantationPlant/scenario.json"
    override val map = "mutants/PlantationPerPlantationPlant/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 30
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
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 1530000 g of APPLE.
        [INFO] Simulation Info: Tick 1 started at tick 2 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 1377000 g of APPLE.
        [INFO] Simulation Info: Tick 2 started at tick 3 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 202 performs CUTTING on tile 10 for 4 days.
        [IMPORTANT] Farm Action: Machine 202 performs CUTTING on tile 12 for 4 days.
        [IMPORTANT] Farm Action: Machine 202 performs CUTTING on tile 16 for 4 days.
        [IMPORTANT] Farm Machine: Machine 202 is finished and returns to the shed at 1.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 1115370 g of APPLE.
        [INFO] Simulation Info: Tick 3 started at tick 4 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 903449 g of APPLE.
        [INFO] Simulation Info: Tick 4 started at tick 5 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 658613 g of APPLE.
        [INFO] Simulation Info: Tick 5 started at tick 6 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 480127 g of APPLE.
        [INFO] Simulation Info: Tick 6 started at tick 7 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 203 performs MOWING on tile 14 for 5 days.
        [IMPORTANT] Farm Machine: Machine 203 is finished and returns to the shed at 1.
        [IMPORTANT] Farm Action: Machine 204 performs IRRIGATING on tile 16 for 6 days.
        [IMPORTANT] Farm Machine: Machine 204 is finished and returns to the shed at 1.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 350011 g of APPLE.
        [INFO] Simulation Info: Tick 7 started at tick 8 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 204 performs IRRIGATING on tile 14 for 6 days.
        [IMPORTANT] Farm Machine: Machine 204 is finished and returns to the shed at 1.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 255157 g of APPLE.
        [INFO] Simulation Info: Tick 8 started at tick 9 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 204 performs IRRIGATING on tile 12 for 6 days.
        [IMPORTANT] Farm Machine: Machine 204 is finished and returns to the shed at 1.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 167407 g of APPLE.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 1080000 g of CHERRY.
        [INFO] Harvest Estimate: Harvest estimate on tile 16 changed to 720000 g of ALMOND.
        [INFO] Simulation Info: Tick 9 started at tick 10 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 204 performs IRRIGATING on tile 10 for 6 days.
        [IMPORTANT] Farm Machine: Machine 204 is finished and returns to the shed at 1.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 109835 g of APPLE.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 972000 g of CHERRY.
        [INFO] Harvest Estimate: Harvest estimate on tile 16 changed to 648000 g of ALMOND.
        [INFO] Simulation Info: Tick 10 started at tick 11 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 203 performs MOWING on tile 10 for 5 days.
        [IMPORTANT] Farm Action: Machine 203 performs MOWING on tile 12 for 5 days.
        [IMPORTANT] Farm Machine: Machine 203 is finished and returns to the shed at 1.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 72061 g of APPLE.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 874800 g of CHERRY.
        [DEBUG] Harvest Estimate: Required actions on tile 16 were not performed: MOWING.
        [INFO] Harvest Estimate: Harvest estimate on tile 16 changed to 524880 g of ALMOND.
        [INFO] Simulation Info: Tick 11 started at tick 12 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 47277 g of APPLE.
        [INFO] Harvest Estimate: Harvest estimate on tile 12 changed to 787320 g of CHERRY.
        [INFO] Harvest Estimate: Harvest estimate on tile 16 changed to 472392 g of ALMOND.
        [INFO] Simulation Info: Tick 12 started at tick 13 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 201 performs HARVESTING on tile 12 for 2 days.
        [IMPORTANT] Farm Harvest: Machine 201 has collected 787320 g of CHERRY harvest.
        [IMPORTANT] Farm Machine: Machine 201 is finished and returns to the shed at 1.
        [IMPORTANT] Farm Machine: Machine 201 unloads 787320 g of CHERRY harvest in the shed.
        [IMPORTANT] Farm Action: Machine 203 performs MOWING on tile 14 for 5 days.
        [IMPORTANT] Farm Machine: Machine 203 is finished and returns to the shed at 1.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 31017 g of APPLE.
        [INFO] Harvest Estimate: Harvest estimate on tile 16 changed to 425152 g of ALMOND.
        [INFO] Simulation Info: Tick 13 started at tick 14 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 202 performs CUTTING on tile 14 for 4 days.
        [IMPORTANT] Farm Machine: Machine 202 is finished and returns to the shed at 1.
        [IMPORTANT] Farm Action: Machine 204 performs IRRIGATING on tile 16 for 6 days.
        [IMPORTANT] Farm Machine: Machine 204 is finished and returns to the shed at 1.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 20349 g of APPLE.
        [INFO] Harvest Estimate: Harvest estimate on tile 16 changed to 382636 g of ALMOND.
        [INFO] Simulation Info: Tick 14 started at tick 15 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 13349 g of APPLE.
        [INFO] Simulation Info: Tick 15 started at tick 16 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 201 performs HARVESTING on tile 16 for 2 days.
        [IMPORTANT] Farm Harvest: Machine 201 has collected 382636 g of ALMOND harvest.
        [IMPORTANT] Farm Machine: Machine 201 is finished and returns to the shed at 1.
        [IMPORTANT] Farm Machine: Machine 201 unloads 382636 g of ALMOND harvest in the shed.
        [IMPORTANT] Farm Action: Machine 204 performs IRRIGATING on tile 14 for 6 days.
        [IMPORTANT] Farm Machine: Machine 204 is finished and returns to the shed at 1.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 8757 g of APPLE.
        [INFO] Simulation Info: Tick 16 started at tick 17 within the year.
        [INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.
        [IMPORTANT] Farm: Farm 1 starts its actions.
        [DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .
        [IMPORTANT] Farm Action: Machine 201 performs HARVESTING on tile 10 for 2 days.
        [IMPORTANT] Farm Harvest: Machine 201 has collected 8757 g of APPLE harvest.
        [IMPORTANT] Farm Machine: Machine 201 is finished and returns to the shed at 1.
        [IMPORTANT] Farm Machine: Machine 201 unloads 8757 g of APPLE harvest in the shed.
        [IMPORTANT] Farm: Farm 1 finished its actions.
        [DEBUG] Harvest Estimate: Required actions on tile 14 were not performed: HARVESTING.
        [INFO] Harvest Estimate: Harvest estimate on tile 14 changed to 1140000 g of GRAPE.
    """.trimIndent()

    override suspend fun run() {
        val expectedLines = expectedOutput.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

        for (line in expectedLines) {
            assertNextLine(line)
        }
    }
}
