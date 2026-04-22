package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfIncidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Example system test
 */
class AlotOfIncidents4 : ExampleSystemTestExtension() {
    override val name = "medium map with alot of incidents tick 4 with animal attack incident"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/highIncidentScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 7
    override val startYearTick = 12

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 4 started at tick 16 within the year.")
        assertCurrentLine(
            "[INFO] Simulation Info: Tick 4 started at tick 16 within the year."
        )
        val expectedLogs = listOf(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 1 FIELD and 0 PLANTATION tiles.",
            "[IMPORTANT] Farm: Farm 0 starts its actions.",
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,2,4.",
            "[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 21 for 1 days.",
            "[IMPORTANT] Farm Harvest: Machine 2 has collected 583200 g of ALMOND harvest.",
            "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.",
            "[IMPORTANT] Farm Machine: Machine 2 unloads 583200 g of ALMOND harvest in the shed.",
            "[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 7 for 1 days.",
            "[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13.",
            "[IMPORTANT] Farm: Farm 0 finished its actions.",
            "[IMPORTANT] Farm: Farm 1 starts its actions.",
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5,6,8.",
            "[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 52 for 1 days.",
            "[IMPORTANT] Farm Harvest: Machine 4 has collected 583200 g of ALMOND harvest.",
            "[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.",
            "[IMPORTANT] Farm Machine: Machine 4 unloads 583200 g of ALMOND harvest in the shed.",
            "[IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 55 for 1 days.",
            "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.",
            "[IMPORTANT] Farm: Farm 1 finished its actions.",
            "[IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened " +
                "and affected tiles 7,8,10,11,18,42,43,51,52,54,55.",
            "[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: WEEDING.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 104976 g of PUMPKIN.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 186008 g of APPLE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 11 changed to 540000 g of GRAPE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 206676 g of APPLE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 51 changed to 540000 g of GRAPE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 116640 g of PUMPKIN."
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
