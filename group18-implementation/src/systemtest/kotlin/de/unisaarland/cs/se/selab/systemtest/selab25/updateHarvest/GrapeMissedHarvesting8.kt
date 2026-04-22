package de.unisaarland.cs.se.selab.systemtest.selab25.updateHarvest

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Test sowing a single WHEAT field at OCT1, machine returns to shed,
 * stats reflect remaining harvest.
 */
class GrapeMissedHarvesting8 : ExampleSystemTestExtension() {
    override val name = "when to log missed harvesting = check first and check second with numbers8"
    override val description = ""
    override val farms = "FieldAndPlantation/smallPlantationFarm2.json"
    override val scenario = "FieldAndPlantation/smallPlantationScenario.json"
    override val map = "FieldAndPlantation/onlyGrape.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 30
    override val startYearTick = 14 // OCT1 (fits WHEAT sowing window OCT1...OCT2)

    override suspend fun run() {
        skipUntilString(HARVEST_ESTIMATE)
        assertCurrentLine("[DEBUG] Harvest Estimate: Required actions on tile 4 were not performed: CUTTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 600000 g of GRAPE.")
        skipUntilString(HARVEST_ESTIMATE)
        assertCurrentLine(MISSED_HARVEST)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 570000 g of GRAPE.")
        skipUntilString("[INFO] Simulation Info: Tick 4 started at tick 18 within the year.")
        skipLines(4)
        assertNextLine(MISSED_HARVEST)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 541500 g of GRAPE.")
        skipUntilString(HARVEST_ESTIMATE)
        assertCurrentLine(MISSED_HARVEST)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 514425 g of GRAPE.")
        skipUntilString(HARVEST_ESTIMATE)
        assertCurrentLine(MISSED_HARVEST)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 4 changed to 0 g of GRAPE.")
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
