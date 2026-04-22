package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfIncidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Example system test
 */
class AlotOfIncidents6 : ExampleSystemTestExtension() {
    override val name = "medium map with alot of incidents tick 6"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/highIncidentScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 7
    override val startYearTick = 12

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 6 started at tick 18 within the year.")
        assertCurrentLine(
            "[INFO] Simulation Info: Tick 6 started at tick 18 within the year."
        )
        val expectedLogs = listOf(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 1 PLANTATION tiles.",
            "[IMPORTANT] Farm: Farm 0 starts its actions.",
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,2,4.",
            "[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 11 for 1 days.",
            "[IMPORTANT] Farm Harvest: Machine 2 has collected 513000 g of GRAPE harvest.",
            "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.",
            "[IMPORTANT] Farm Machine: Machine 2 unloads 513000 g of GRAPE harvest in the shed.",
            "[IMPORTANT] Farm: Farm 0 finished its actions.",
            "[IMPORTANT] Farm: Farm 1 starts its actions.",
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5,6,8.",
            "[IMPORTANT] Farm: Farm 1 finished its actions.",
            "[IMPORTANT] Simulation Info: Simulation ended at tick 7."
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
