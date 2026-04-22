package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfIncidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Example system test
 */
class AlotOfIncidents5 : ExampleSystemTestExtension() {
    override val name = "medium map with alot of incidents tick 5 with drought and broken machine"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/highIncidentScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 7
    override val startYearTick = 12

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 5 started at tick 17 within the year.")
        assertCurrentLine(
            "[INFO] Simulation Info: Tick 5 started at tick 17 within the year."
        )
        val expectedLogs = listOf(
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
            "[IMPORTANT] Farm: Farm 0 starts its actions.",
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1,2,4.",
            "[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 10 for 1 days.",
            "[IMPORTANT] Farm Harvest: Machine 2 has collected 186008 g of APPLE harvest.",
            "[IMPORTANT] Farm Machine: Machine 2 is finished and returns to the shed at 16.",
            "[IMPORTANT] Farm Machine: Machine 2 unloads 186008 g of APPLE harvest in the shed.",
            "[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 7 for 1 days.",
            "[IMPORTANT] Farm Harvest: Machine 1 has collected 104976 g of PUMPKIN harvest.",
            "[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 13.",
            "[IMPORTANT] Farm Machine: Machine 1 unloads 104976 g of PUMPKIN harvest in the shed.",
            "[IMPORTANT] Farm: Farm 0 finished its actions.",
            "[IMPORTANT] Farm: Farm 1 starts its actions.",
            "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 5,6,8.",
            "[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 40 for 1 days.",
            "[IMPORTANT] Farm Harvest: Machine 4 has collected 206676 g of APPLE harvest.",
            "[IMPORTANT] Farm Machine: Machine 4 is finished and returns to the shed at 49.",
            "[IMPORTANT] Farm Machine: Machine 4 unloads 206676 g of APPLE harvest in the shed.",
            "[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 55 for 1 days.",
            "[IMPORTANT] Farm Harvest: Machine 3 has collected 116640 g of PUMPKIN harvest.",
            "[IMPORTANT] Farm Machine: Machine 3 is finished and returns to the shed at 46.",
            "[IMPORTANT] Farm Machine: Machine 3 unloads 116640 g of PUMPKIN harvest in the shed.",
            "[IMPORTANT] Farm: Farm 1 finished its actions.",
            "[IMPORTANT] Incident: Incident 2 of type DROUGHT happened and affected tiles 40,51,52.",
            "[IMPORTANT] Incident: Incident 3 of type DROUGHT happened and affected tiles 44,54,55.",
            "[IMPORTANT] Incident: Incident 4 of type BROKEN_MACHINE happened and affected tiles 46.",
            "[IMPORTANT] Incident: Incident 5 of type BROKEN_MACHINE happened and affected tiles 49.",
            "[DEBUG] Harvest Estimate: Required actions on tile 11 were not performed: HARVESTING.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 11 changed to 513000 g of GRAPE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 51 changed to 0 g of GRAPE.",
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
