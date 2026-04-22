package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**

 */
class ConcurrentCityExpWrongOrder2 : ExampleSystemTestExtension() {
    override val name = "ConcurrentCityExpWrongOrder2"
    override val description = "2 village expansions in a line can NOT happen in the same tick" +
        "when the incident with lower id(only becomes adj to village after the other city exp)" +
        "logically happens after one with higher id."
    override val farms = "ConcurrentCityExpWrongOrder/farms.json"
    override val scenario = "ConcurrentCityExpWrongOrder/scenario.json"
    override val map = "ConcurrentCityExpWrongOrder/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 7 // APR1

    override suspend fun run() {
        skipUntilString(
            "[IMPORTANT] Initialization Info: scenario.json is invalid."
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
