package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * CITY_EXPANSION turns a freshly sowed WHEAT FIELD into a VILLAGE at tick 0.
 * Expect: sow logs, incident log, then harvest estimate -> 0 g for that tile.
 *
 * startYearTick = 19 (OCT1) so WHEAT sowing is in-window.
 */
class CloudCreationFailScenario : ExampleSystemTestExtension() {
    override val name = "cloud starting on village fail scenario"
    override val description = "creation of clouds where the affected tiles is village and we create a cloud on it"

    override val farms = "CloudCreationIncident/cloudcreationfarm.json"
    override val scenario = "CloudCreationIncident/cloudcreationscenariocloudzilla.json"
    override val map = "CloudCreationIncident/cloudcreationmap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Initialization Info")
        assertCurrentLine(
            "[IMPORTANT] Initialization Info: cloudcreationscenariocloudzilla.json is invalid."
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
