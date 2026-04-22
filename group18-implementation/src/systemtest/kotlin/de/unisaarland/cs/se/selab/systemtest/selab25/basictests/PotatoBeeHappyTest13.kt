package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class PotatoBeeHappyTest13 : ExampleSystemTestExtension() {
    override val name = "PotatoBeeHappyTest13"
    override val description = "Test the interaction of BeeHappy and Potato (sowing 0, blooming 4, potato logged)"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallFieldPotatoFarm.json"
    override val scenario = "FieldAndPlantation/smallFieldPotatoBeeHappyScenario4.json"
    override val map = "FieldAndPlantation/smallFieldMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, logType = LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, logType = LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, logType = LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, logType = LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, logType = LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, logType = LogType.SIMULATION_INFO)
        skipUntilString("[IMPORTANT] Incident")
        assertCurrentLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 9.")
    }

    private suspend fun skipUntilString(startString: String): String {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        return if (line.startsWith(startString)) {
            line
        } else {
            skipUntilString(startString)
        }
    }
}
