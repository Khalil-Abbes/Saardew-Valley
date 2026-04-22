package de.unisaarland.cs.se.selab.systemtest.selab25.updateHarvest

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Test sowing a single WHEAT field at OCT1, machine returns to shed,
 * stats reflect remaining harvest.
 */
class PotatoMissedSowing : ExampleSystemTestExtension() {
    override val name = "potato missed sow"
    override val description = ""
    override val farms = "farmaction/sow-wheat-happy/farms.json"
    override val scenario = "farmaction/sow-wheat-happy/scenario.json"
    override val map = "farmaction/sow-wheat-happy/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 10 // OCT1 (fits WHEAT sowing window OCT1...OCT2)

    override suspend fun run() {
        skipUntilString("[DEBUG] Farm")
        assertCurrentLine(
            "[DEBUG] Farm: Farm 0 has the following active" +
                " sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 2 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed POTATO according to sowing plan 5.")
        skipUntilString("[INFO] Harvest Estimate:")
        assertCurrentLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 900000 g of POTATO.")
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 810000 g of POTATO.")
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
