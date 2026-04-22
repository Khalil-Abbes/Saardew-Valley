package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * wrong neighbor adjacent tiles
 */
class SmallSimulation4 : ExampleSystemTestExtension() {
    override val name = "Small Simulation for 5 tick, tick 4"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallFarm.json"
    override val scenario = "FieldAndPlantation/smallScenario.json"
    override val map = "FieldAndPlantation/smallMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 20

    override suspend fun run() {
        repeat(5) {
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        }
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below" +
                " threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 2 on tile 10 rained down 70 L water.")
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 3 on tile 9 rained down 100 L water.")
        assertNextLine("[DEBUG] Cloud Position: Cloud 3 is on tile 9, where the amount of sunlight is 34.")
        assertNextLine("[DEBUG] Cloud Position: Cloud 2 is on tile 10, where the amount of sunlight is 34.")
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 1 has the following " +
                "active sowing plans it intends to pursue in this tick: 2."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles .")
    }
}
