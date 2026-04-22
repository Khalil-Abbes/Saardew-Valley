package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class SmallSimulation2 : ExampleSystemTestExtension() {
    override val name = "Small Simulation for 5 tick, tick 2"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallFarm.json"
    override val scenario = "FieldAndPlantation/smallScenario.json"
    override val map = "FieldAndPlantation/smallMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 20

    override suspend fun run() {
        skipLines(3)
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 20 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 20 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold " +
                "in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 1 on tile 1 rained down 10000 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 1 dissipates on tile 1.")
        skipLines(2)
        assertNextLine("[DEBUG] Cloud Position: Cloud 3 is on tile 9, where the amount of sunlight is 62.")
        assertNextLine("[DEBUG] Cloud Position: Cloud 2 is on tile 10, where the amount of sunlight is 62.")
        skipLines(1)
        assertNextLine(
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 9 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 3 has collected 1028850 g of GRAPE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 unloads 1028850 g of GRAPE harvest in the shed.")
        skipLines(1)
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 21 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 " +
                "FIELD and 0 PLANTATION tiles."
        )
        skipLines(2)
        assertNextLine("[DEBUG] Cloud Position: Cloud 3 is on tile 9, where the amount of sunlight is 48.")
        assertNextLine("[DEBUG] Cloud Position: Cloud 2 is on tile 10, where the amount of sunlight is 48.")
        skipLines(1)
        assertNextLine(
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs SOWING on tile 4 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 2 has sowed WHEAT according to sowing plan 1.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 8.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 3 performs CUTTING on tile 5 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 1.")
        skipLines(1)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 1200000 g of WHEAT.")
    }
}
