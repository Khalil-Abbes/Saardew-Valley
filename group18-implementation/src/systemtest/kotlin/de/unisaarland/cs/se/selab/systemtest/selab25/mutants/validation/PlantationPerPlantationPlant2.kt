package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.validation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class PlantationPerPlantationPlant2 : ExampleSystemTestExtension() {
    override val name = "PlantationPerPlantationPlant 2"
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
    """.trimIndent()

    override suspend fun run() {
        val expectedLines = expectedOutput.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

        for (line in expectedLines) {
            assertNextLine(line)
        }
    }
}
