package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class TickEndTest1 : ExampleSystemTestExtension() {
    override val name = "TickEnd Test1"
    override val description = "Tick end for maxTick = 0 is 0"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallFarm.json"
    override val scenario = "FieldAndPlantation/smallFieldScenario.json"
    override val map = "FieldAndPlantation/smallMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 0.")
    }
}
