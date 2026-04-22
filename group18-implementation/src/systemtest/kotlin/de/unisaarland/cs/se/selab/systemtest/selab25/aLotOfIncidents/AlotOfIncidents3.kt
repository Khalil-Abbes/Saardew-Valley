package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfIncidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Example system test
 */
class AlotOfIncidents3 : ExampleSystemTestExtension() {
    override val name = "medium map with alot of incidents tick 3"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/highIncidentScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 7
    override val startYearTick = 12

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 3 started at tick 15 within the year.")
        assertCurrentLine(
            "[INFO] Simulation Info: Tick 3 started at tick 15 within the year."
        )
        val expectedLogs = listOf(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
            "[IMPORTANT] Farm: Farm 0 starts its actions.",
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,2,4.",
            "[IMPORTANT] Farm: Farm 0 finished its actions.",
            "[IMPORTANT] Farm: Farm 1 starts its actions.",
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5,6,8.",
            "[IMPORTANT] Farm: Farm 1 finished its actions.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 259200 g of PUMPKIN.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 315009 g of APPLE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 315009 g of APPLE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 259200 g of PUMPKIN."
        )
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
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
