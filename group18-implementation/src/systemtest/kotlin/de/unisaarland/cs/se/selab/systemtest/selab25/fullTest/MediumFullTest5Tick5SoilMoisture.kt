package de.unisaarland.cs.se.selab.systemtest.selab25.fullTest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

private const val SOIL_MOISTURE_0 = "[INFO] Soil Moisture: The " +
    "soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles."

/**
 * Example system test
 */
class MediumFullTest5Tick5SoilMoisture : ExampleSystemTestExtension() {
    override val name = "Medium all component tick 5, soil moisture"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FullTest/mediumFarmFull.json"
    override val scenario = "FullTest/mediumScenarioFull.json"
    override val map = "FullTest/mediumMapFull.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 1
    private val expectedLogs = listOf(
        SOIL_MOISTURE_0
    )

    override suspend fun run() {
        var num = 0
        while (num < 7) {
            num++
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        }
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
