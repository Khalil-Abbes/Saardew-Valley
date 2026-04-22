package de.unisaarland.cs.se.selab.systemtest.selab25.fullTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

private const val FARM0_START = "[IMPORTANT] Farm: Farm 0 starts its actions."
private const val FARM0_FINISH = "[IMPORTANT] Farm: Farm 0 finished its actions."

/**
 * Example system test
 */
class MediumFullTest5Tick5Farm1 : ExampleSystemTestExtension() {
    override val name = "Medium all component tick 5, do action for first farm"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullTest/mediumFarmFull.json"
    override val scenario = "FullTest/mediumScenarioFull.json"
    override val map = "FullTest/mediumMapFull.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 1
    private val expectedLogs = listOf(
        FARM0_START,
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 2.",
        FARM0_FINISH
    )

    override suspend fun run() {
        var num = 0
        while (num < 7) {
            num++
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        }
        skipLines(1)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
