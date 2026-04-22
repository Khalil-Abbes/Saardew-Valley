package de.unisaarland.cs.se.selab.systemtest.selab25.fullTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

private const val FARM1_START = "[IMPORTANT] Farm: Farm 1 starts its actions."
private const val FARM1_FINISH = "[IMPORTANT] Farm: Farm 1 finished its actions."

private const val MACHINE3_FINISH = "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46."

/**
 * Example system test
 */
class MediumFullTest5Tick5Farm2 : ExampleSystemTestExtension() {
    override val name = "Medium all component tick 5, do action for second farm"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullTest/mediumFarmFull.json"
    override val scenario = "FullTest/mediumScenarioFull.json"
    override val map = "FullTest/mediumMapFull.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 1
    private val expectedLogs = listOf(
        FARM1_START,
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 6,8.",
        "[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 43 for 3 days.",
        "[IMPORTANT] Farm Sowing: Machine 3 has sowed " +
            "OAT according to sowing plan 6.",
        "[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 44 for 3 days.",
        "[IMPORTANT] Farm Sowing: Machine 3 has sowed OAT according to sowing plan 6.",
        "[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 55 for 3 days.",
        "[IMPORTANT] Farm Sowing: Machine 3 has sowed OAT according to sowing plan 6.",
        MACHINE3_FINISH,
        FARM1_FINISH
    )

    override suspend fun run() {
        var num = 0
        while (num < 7) {
            num++
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        }
        skipLines(4)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
