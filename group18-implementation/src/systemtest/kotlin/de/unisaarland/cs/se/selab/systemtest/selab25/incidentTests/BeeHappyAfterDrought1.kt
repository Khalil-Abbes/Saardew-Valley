package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 */
class BeeHappyAfterDrought1 : ExampleSystemTestExtension() {
    override val name = "BeeHappyAfterDrought1"
    override val description =
        ""
    override val farms = "farmaction/BeeHappyAfterDrought/farms.json"
    override val scenario = "farmaction/BeeHappyAfterDrought/scenario.json"
    override val map = "farmaction/BeeHappyAfterDrought/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 10 // MAY2

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 2.")
        if (!(getNextLine()?.startsWith("[DEBUG] Harvest Estimate:") ?: false)) {
            throw SystemTestAssertionError("Unexpected log line after DROUGHT incident.")
        }
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
