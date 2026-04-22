package de.unisaarland.cs.se.selab.systemtest.selab25.fullTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class MediumFullTest5Tick5HarvestEstimate : ExampleSystemTestExtension() {
    override val name = "Medium all component tick 5, update harvest estimate phase"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullTest/mediumFarmFull.json"
    override val scenario = "FullTest/mediumScenarioFull.json"
    override val map = "FullTest/mediumMapFull.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 1
    private val expectedLogs = listOf(
        "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 480127 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 480127 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 1080000 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 1080000 g of OAT.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 1080000 g of OAT.",
    )

    override suspend fun run() {
        var num = 0
        while (num < 7) {
            num++
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        }
        skipLines(14)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
