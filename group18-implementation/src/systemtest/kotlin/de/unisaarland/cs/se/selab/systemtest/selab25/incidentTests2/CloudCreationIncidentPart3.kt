package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * CITY_EXPANSION turns a freshly sowed WHEAT FIELD into a VILLAGE at tick 0.
 * Expect: sow logs, incident log, then harvest estimate -> 0 g for that tile.
 *
 * startYearTick = 19 (OCT1) so WHEAT sowing is in-window.
 */
class CloudCreationIncidentPart3 : ExampleSystemTestExtension() {
    override val name = "Cloud Creation Incident Part 3 True"
    override val description = " creation and merging of clouds one at a time with duration -1 and radius 0"
    override val farms = "CloudCreationIncident/cloudcreationfarm.json"
    override val scenario = "CloudCreationIncident/cloudcreationscenario.json"
    override val map = "CloudCreationIncident/cloudcreationmap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Incident: Incident 15")
        assertCurrentLine("[IMPORTANT] Incident: Incident 15 of type CLOUD_CREATION happened and affected tiles 8.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 1 and 15 united to cloud 16 with 2 L water and duration -1 on tile 8."
        )
        assertNextLine("[IMPORTANT] Incident: Incident 16 of type CLOUD_CREATION happened and affected tiles 9.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 2 and 17 united to cloud 18 with 2 L water and duration -1 on tile 9."
        )
        assertNextLine("[IMPORTANT] Incident: Incident 18 of type CLOUD_CREATION happened and affected tiles 11.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 4 and 19 united to cloud 20 with 2 L water and duration -1 on tile 11."
        )
        assertNextLine("[IMPORTANT] Incident: Incident 19 of type CLOUD_CREATION happened and affected tiles 12.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 5 and 21 united to cloud 22 with 2 L water and duration -1 on tile 12."
        )
        assertNextLine("[IMPORTANT] Incident: Incident 20 of type CLOUD_CREATION happened and affected tiles 13.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 6 and 23 united to cloud 24 with 2 L water and duration -1 on tile 13."
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
