package de.unisaarland.cs.se.selab.systemtest.selab25.updateHarvest

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Test sowing a single WHEAT field at OCT1, machine returns to shed,
 * stats reflect remaining harvest.
 */
class PotatoMissedSowing2 : ExampleSystemTestExtension() {
    override val name = "potato missed sow2"
    override val description = ""
    override val farms = "farmaction/sow-wheat-happy/farms.json"
    override val scenario = "farmaction/sow-wheat-happy/scenario.json"
    override val map = "farmaction/sow-wheat-happy/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 11 // OCT1 (fits WHEAT sowing window OCT1...OCT2)

    override suspend fun run() {
        skipUntilString("[INFO] Harvest Estimate:")
        assertCurrentLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 720000 g of POTATO.")
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
