package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class PotatoBeeHappyTest2 : ExampleSystemTestExtension() {
    override val name = "PotatoBeeHappyTest2"
    override val description = "Test the interaction of BeeHappy and Potato (specific version: no log)"

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
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 810000 g of POTATO.")
    }
}
