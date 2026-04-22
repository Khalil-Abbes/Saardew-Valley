package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFull6Tick6PlantCollected : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 6, each plant collected harvest"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 6
    override val startYearTick = 18

    private val expectedLogs = listOf(
        "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 1377000 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 2168850 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 800000 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS)
        skipLines(1)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
