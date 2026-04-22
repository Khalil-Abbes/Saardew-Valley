package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class WheatWeedingTest1 : ExampleSystemTestExtension() {
    override val name = "Wheat Weeding Test 1"
    override val description = "Test weeding after 3 tick, weed at tick 22 when sowed at tick 19"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/wheatWeedingFarm.json"
    override val scenario = "FieldAndPlantation/smallFieldScenario.json"
    override val map = "FieldAndPlantation/smallFieldMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 19

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipLines(3)
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs WEEDING on tile 4 for 4 days.")
    }
}
