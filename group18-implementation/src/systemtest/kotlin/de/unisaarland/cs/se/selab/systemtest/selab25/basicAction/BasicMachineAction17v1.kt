package de.unisaarland.cs.se.selab.systemtest.selab25.basicAction

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val DEBUG_HARVEST_ESTIMATE = "[DEBUG] Harvest Estimate:"

/**
 * test actions
 */

class BasicMachineAction17v1 : ExampleSystemTestExtension() {
    override val name = "Basic Machine Test ticks 17-20 v1"
    override val description = "Testing of machine actions"
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/mediumScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 1

    private companion object {
        const val MACHINE_1_ACTION = "[IMPORTANT] Farm Action: Machine 1"
        const val MACHINE_3_ACTION = "[IMPORTANT] Farm Action: Machine 3"
        const val MACHINE_4_ACTION = "[IMPORTANT] Farm Action: Machine 4"
    }

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 16")
        skipUntilString(DEBUG_HARVEST_ESTIMATE)
        assertCurrentLine(
            "[DEBUG] Harvest Estimate: Required actions on tile 11 were not performed: IRRIGATING,HARVESTING."
        )
        assertNextLine(
            "[INFO] Harvest Estimate: Harvest estimate on tile 11 changed to 461652 g of GRAPE."
        )
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
