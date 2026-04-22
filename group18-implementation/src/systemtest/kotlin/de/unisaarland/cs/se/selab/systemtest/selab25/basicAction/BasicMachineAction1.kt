package de.unisaarland.cs.se.selab.systemtest.selab25.basicAction

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * test actions
 */
class BasicMachineAction1 : ExampleSystemTestExtension() {
    override val name = "Basic Machine Test ticks 1-4"
    override val description = "Testing of machine actions"
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/mediumScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 24
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 2")
        skipUntilString("[IMPORTANT] Farm Action: Machine 2")
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 2 performs CUTTING on tile 10 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs CUTTING on tile 21 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 2 performs CUTTING on tile 22 for 1 days.")
        skipUntilString("[IMPORTANT] Farm Action: Machine 4")
        assertCurrentLine("[IMPORTANT] Farm Action: Machine 4 performs CUTTING on tile 40 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs CUTTING on tile 41 for 1 days.")
        assertNextLine("[IMPORTANT] Farm Action: Machine 4 performs CUTTING on tile 52 for 1 days.")
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
