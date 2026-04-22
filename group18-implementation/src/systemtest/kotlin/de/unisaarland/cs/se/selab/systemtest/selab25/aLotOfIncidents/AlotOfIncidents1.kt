package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfIncidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Example system test
 */
class AlotOfIncidents1 : ExampleSystemTestExtension() {
    override val name = "medium map with alot of incidents tick 1"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/highIncidentScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 7
    override val startYearTick = 12

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 1 started at tick 13 within the year.")
        assertCurrentLine(
            "[INFO] Simulation Info: Tick 1 started at tick 13 within the year."
        )
        val expectedLogs = listOf(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
            "[IMPORTANT] Farm: Farm 0 starts its actions.",
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,2.",
            "[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 22 for 1 days.",
            "[IMPORTANT] Farm Harvest: Machine 2 has collected 1080000 g of CHERRY harvest.",
            "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.",
            "[IMPORTANT] Farm Machine: Machine 2 unloads 1080000 g of CHERRY harvest in the shed.",
            "[IMPORTANT] Farm: Farm 0 finished its actions.",
            "[IMPORTANT] Farm: Farm 1 starts its actions.",
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5,6.",
            "[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 41 for 1 days.",
            "[IMPORTANT] Farm Harvest: Machine 4 has collected 1080000 g of CHERRY harvest.",
            "[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.",
            "[IMPORTANT] Farm Machine: Machine 4 unloads 1080000 g of CHERRY harvest in the shed.",
            "[IMPORTANT] Farm: Farm 1 finished its actions.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 731793 g of APPLE.",
            "[DEBUG] Harvest Estimate: Required actions on tile 11 were not performed: MOWING.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 11 changed to 1080000 g of GRAPE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 21 changed to 648000 g of ALMOND.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 731793 g of APPLE.",
            "[DEBUG] Harvest Estimate: Required actions on tile 51 were not performed: MOWING.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 51 changed to 1080000 g of GRAPE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 52 changed to 648000 g of ALMOND."

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
