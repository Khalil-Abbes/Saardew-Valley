package de.unisaarland.cs.se.selab.systemtest.selab25.basicAction

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
/**
 * test actions
 */
class HarvestEstimateTick12Part3 : ExampleSystemTestExtension() {
    override val name = "Test Harvesting Estimate at tick 12 part 1, 3"
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
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 21 changed to 524880 g of ALMOND.")
        assertCurrentLine("[INFO] Harvest Estimate: Harvest estimate on tile 21 changed to 524880 g of ALMOND.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 22 changed to 787320 g of CHERRY.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 47277 g of APPLE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 41 changed to 787320 g of CHERRY.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 656100 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 656100 g of POTATO.")
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
