package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class PotatoBeeHappyTest : ExampleSystemTestExtension() {
    override val name = "PotatoBeeHappyTest"
    override val description = "Test the interaction of BeeHappy and Potato (specific version)"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallFieldPotatoFarm.json"
    override val scenario = "FieldAndPlantation/smallFieldPotatoBeeHappyScenario.json"
    override val map = "FieldAndPlantation/smallFieldMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipLines(4)
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 9.")
    }
}
