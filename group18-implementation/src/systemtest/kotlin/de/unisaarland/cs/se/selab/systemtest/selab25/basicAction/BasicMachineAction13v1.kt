package de.unisaarland.cs.se.selab.systemtest.selab25.basicAction

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
/**
 * test actions
 */
class BasicMachineAction13v1 : ExampleSystemTestExtension() {
    override val name = "Basic Machine Test ticks 13-16 v1"
    override val description = "Testing of machine actions"
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/mediumScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 1

    private companion object {
        const val MACHINE_1_ACTION = "[IMPORTANT] Farm Action: Machine 1"
        const val MACHINE_2_ACTION = "[IMPORTANT] Farm Action: Machine 2"
        const val MACHINE_3_ACTION = "[IMPORTANT] Farm Action: Machine 3"
        const val MACHINE_4_ACTION = "[IMPORTANT] Farm Action: Machine 4"
        const val MACHINE_2_CUTTING_11 = "[IMPORTANT] Farm Action: Machine 2 performs CUTTING on tile 11 for 1 days."
    }

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 12")
        skipUntilString(MACHINE_2_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 2 performs HARVESTING on tile 22 for 1 days.")
        skipUntilString(MACHINE_1_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 8 for 1 days.")
        skipUntilString(MACHINE_4_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 4 performs HARVESTING on tile 41 for 1 days.")
        skipUntilString(MACHINE_3_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 3 performs HARVESTING on tile 44 for 1 days.")
        skipUntilString(MACHINE_2_ACTION)
        assertCurrentLine(MACHINE_2_CUTTING_11)
        skipUntilString(MACHINE_1_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 19 for 1 days.")
        skipUntilString(MACHINE_1_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 1 performs IRRIGATING on tile 20 for 1 days.")
        skipUntilString(MACHINE_4_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 4 performs CUTTING on tile 51 for 1 days.")
        skipUntilString(MACHINE_3_ACTION)
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 3 performs WEEDING on tile 55 for 1 days.")
        skipUntilString(MACHINE_2_ACTION)
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
