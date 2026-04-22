package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**

 */
class ConcurrentCityExpWrongOrder1 : ExampleSystemTestExtension() {
    override val name = "ConcurrentCityExpWrongOrder1"
    override val description = "2 village expansions in a line can happen in the same tick" +
        "even when the incident with lower id(only becomes adj to village after the other city exp)" +
        "logically happens after one with higher id."
    override val farms = "ConcurrentCityExpWrongOrder/farms.json"
    override val scenario = "ConcurrentCityExpWrongOrder/scenario.json"
    override val map = "ConcurrentCityExpWrongOrder/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 7 // APR1

    override suspend fun run() {
        skipUntilString(
            "[INFO] Initialization Info: scenario.json successfully parsed and validated."
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
