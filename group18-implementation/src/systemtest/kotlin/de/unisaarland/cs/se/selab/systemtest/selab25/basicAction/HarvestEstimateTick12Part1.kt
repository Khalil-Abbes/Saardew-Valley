package de.unisaarland.cs.se.selab.systemtest.selab25.basicAction

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
/**
 * test actions
 */
class HarvestEstimateTick12Part1 : ExampleSystemTestExtension() {
    override val name = "Test Harvesting Estimate at tick 12 part 1"
    override val description = "testing harvest estimate before the failing BasicMachineAction13 part 1"
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/mediumScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 13
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 11")
        assertCurrentLine("[INFO] Simulation Info: Tick 11 started at tick 12 within the year.")
        skipUntilString("[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: WEEDING.")
        assertCurrentLine("[DEBUG] Harvest Estimate: Required actions on tile 7 were not performed: WEEDING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 163969 g of PUMPKIN.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 8 changed to 72943 g of OAT.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 10 were not performed: IRRIGATING.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 11 were not performed: IRRIGATING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 19 changed to 531441 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 20 changed to 531441 g of POTATO.")
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
