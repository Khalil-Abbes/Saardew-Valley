package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * CITY_EXPANSION turns a freshly sowed WHEAT FIELD into a VILLAGE at tick 0.
 * Expect: sow logs, incident log, then harvest estimate -> 0 g for that tile.
 *
 * startYearTick = 19 (OCT1) so WHEAT sowing is in-window.
 */
class CloudCreationIncident12 : ExampleSystemTestExtension() {
    override val name = "Cloud Creation Incident Part 12 True"
    override val description = " creation and merging of clouds one at a time with duration -1 and radius 0"
    override val farms = "CloudCreationIncident/cloudcreationfarm.json"
    override val scenario = "CloudCreationIncident/cloudcreationscenario.json"
    override val map = "CloudCreationIncident/cloudcreationmap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Incident")
        assertCurrentLine("[IMPORTANT] Incident: Incident 17 of type CLOUD_CREATION happened and affected tiles 10.")
        assertNextLine(
            "[IMPORTANT] Cloud Union: Clouds 3 and 7 united to cloud 8 with 2 L water and duration -1 on tile 10."
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
