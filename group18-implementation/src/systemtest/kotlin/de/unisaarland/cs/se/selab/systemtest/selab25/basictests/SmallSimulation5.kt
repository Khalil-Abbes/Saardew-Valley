package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * wrong neighbor adjacent tiles
 */
class SmallSimulation5 : ExampleSystemTestExtension() {
    override val name = "Small Simulation for 5 tick, tick 5"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallFarm.json"
    override val scenario = "FieldAndPlantation/smallScenario.json"
    override val map = "FieldAndPlantation/smallMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 20

    override suspend fun run() {
        repeat(6) {
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        }
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below" +
                " threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 2 on tile 10 rained down 70 L water.")
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 3 on tile 9 rained down 100 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 3 dissipates on tile 9.")
        assertNextLine("[DEBUG] Cloud Position: Cloud 2 is on tile 10, where the amount of sunlight is 34.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 1 has the following " +
                "active sowing plans it intends to pursue in this tick: 2."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 4 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 8.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 5.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 1 collected 1028850 g of harvest.")
        skipLines(8)
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest" +
                " estimate still in fields and plantations: 3600000 g."
        )
    }
}
