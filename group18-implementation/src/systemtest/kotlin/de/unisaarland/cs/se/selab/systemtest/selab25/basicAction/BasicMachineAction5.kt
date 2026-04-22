package de.unisaarland.cs.se.selab.systemtest.selab25.basicAction

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
/**
 * test actions
 */

class BasicMachineAction5 : ExampleSystemTestExtension() {
    override val name = "Basic Machine Test ticks 5-8"
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
    }

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 4")
        skipUntilString(MACHINE_1_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 8 for 1 days.")
        skipUntilString(MACHINE_3_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 44 for 1 days.")
        skipUntilString(MACHINE_1_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 19 for 1 days.")
        skipUntilString(MACHINE_1_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 20 for 1 days.")
        skipUntilString(MACHINE_3_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 42 for 1 days.")
        skipUntilString(MACHINE_3_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 3 performs SOWING on tile 43 for 1 days.")
        skipUntilString(MACHINE_1_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 19 for 1 days.")
        skipUntilString(MACHINE_1_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 20 for 1 days.")
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
