package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * CITY_EXPANSION turns a freshly sowed WHEAT FIELD into a VILLAGE at tick 0.
 * Expect: sow logs, incident log, then harvest estimate -> 0 g for that tile.
 *
 * startYearTick = 19 (OCT1) so WHEAT sowing is in-window.
 */
class CloudCreationIncidentPart2 : ExampleSystemTestExtension() {
    override val name = "Cloud Creation Incident Part 2 True"
    override val description = " creation and merging of clouds one at a time with duration -1 and radius 0"
    override val farms = "CloudCreationIncident/cloudcreationfarm.json"
    override val scenario = "CloudCreationIncident/cloudcreationscenario.json"
    override val map = "CloudCreationIncident/cloudcreationmap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Incident: Incident 8")
        assertCurrentLine("[IMPORTANT] Incident: Incident 8 of type CLOUD_CREATION happened and affected tiles 1.")
        assertNextLine("[IMPORTANT] Incident: Incident 9 of type CLOUD_CREATION happened and affected tiles 2.")
        assertNextLine("[IMPORTANT] Incident: Incident 10 of type CLOUD_CREATION happened and affected tiles 3.")
        assertNextLine("[IMPORTANT] Incident: Incident 11 of type CLOUD_CREATION happened and affected tiles 4.")
        assertNextLine("[IMPORTANT] Incident: Incident 12 of type CLOUD_CREATION happened and affected tiles 5.")
        assertNextLine("[IMPORTANT] Incident: Incident 13 of type CLOUD_CREATION happened and affected tiles 6.")
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
