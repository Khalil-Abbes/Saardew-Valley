package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**

 */
class MissedActionsLoggingAfterDroughtSameTick2 : ExampleSystemTestExtension() {
    override val name = "MissedActionsLoggingAfterDroughtSameTick2"
    override val description = "DO NOT LOG missed actions after drought on the same tick"
    override val farms = "MissedActionsLoggingAfterDroughtSameTick/farms.json"
    override val scenario = "MissedActionsLoggingAfterDroughtSameTick/scenario.json"
    override val map = "MissedActionsLoggingAfterDroughtSameTick/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 10

    override suspend fun run() {
        skipUntilString(
            "[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 2."
        )
        assertNextLine(
            "[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 0 g of PUMPKIN."
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
