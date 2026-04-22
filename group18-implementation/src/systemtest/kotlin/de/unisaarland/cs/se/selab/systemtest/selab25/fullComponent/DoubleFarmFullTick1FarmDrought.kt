package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFullTick1FarmDrought : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 1, incident phase drought does not affect 0g"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 18
    private val expectedLogs = listOf(
        "[IMPORTANT] Incident: Incident 6 of type DROUGHT happened and affected tiles ."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipLines(69)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
