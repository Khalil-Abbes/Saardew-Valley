package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfClouds

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class CloudCircularDirection3 : ExampleSystemTestExtension() {
    override val name = "CloudTestOnCircularDirection, incident phase"
    override val description = "this is for tick 1 log"
    override val farms = "fullComponent/AllSimFarm.json"
    override val scenario = "fullComponent/AllScenarioAtOnce.json"
    override val map = "fullComponent/smallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    private val expectedLogs = listOf(
        "[IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 10,14.",
        "[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles .",
        "[IMPORTANT] Incident: Incident 3 of type CLOUD_CREATION happened and affected tiles 5,7,8,9,10,13,14.",
        "[IMPORTANT] Incident: Incident 4 of type CITY_EXPANSION happened and affected tiles 16.",
        "[IMPORTANT] Incident: Incident 5 of type BROKEN_MACHINE happened and affected tiles 1.",
        "[IMPORTANT] Incident: Incident 6 of type DROUGHT happened and affected tiles 5."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilString("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertCurrentLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        for (log in expectedLogs) {
            assertNextLine(log)
        }
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
