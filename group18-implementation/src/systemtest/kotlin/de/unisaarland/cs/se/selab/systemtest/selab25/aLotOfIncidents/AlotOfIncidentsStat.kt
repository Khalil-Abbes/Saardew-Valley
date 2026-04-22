package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfIncidents

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Example system test
 */
class AlotOfIncidentsStat : ExampleSystemTestExtension() {
    override val name = "medium map with alot of incidents tick 6 statistics"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/highIncidentScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 7
    override val startYearTick = 12

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")
        assertCurrentLine(
            "[IMPORTANT] Simulation Info: Simulation statistics are calculated."
        )
        val expectedLogs = listOf(
            "[IMPORTANT] Simulation Statistics: Farm 0 collected 2467184 g of harvest.",
            "[IMPORTANT] Simulation Statistics: Farm 1 collected 1986516 g of harvest.",
            "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 221616 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 392684 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 513000 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 1166400 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 2160000 g.",
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."

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
