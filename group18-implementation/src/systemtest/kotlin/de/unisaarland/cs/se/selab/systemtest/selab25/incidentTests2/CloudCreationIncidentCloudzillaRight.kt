package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * CITY_EXPANSION turns a freshly sowed WHEAT FIELD into a VILLAGE at tick 0.
 * Expect: sow logs, incident log, then harvest estimate -> 0 g for that tile.
 *
 * startYearTick = 19 (OCT1) so WHEAT sowing is in-window.
 */
class CloudCreationIncidentCloudzillaRight : ExampleSystemTestExtension() {
    override val name = "cloud creation radius includes village Right"
    override val description = "creation of clouds where the affected tiles is village and we create a cloud on it"

    override val farms = "CloudCreationIncident/cloudcreationfarm.json"
    override val scenario = "CloudCreationIncident/newcloudcreationscenariocloudzilla.json"
    override val map = "CloudCreationIncident/cloudcreationmap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Incident")
        assertCurrentLine(
            "[IMPORTANT] Incident: Incident 8 of type CLOUD_CREATION happened and affected tiles" +
                " 1,2,3,4,5,6,8,9,10,11,12,13."
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
