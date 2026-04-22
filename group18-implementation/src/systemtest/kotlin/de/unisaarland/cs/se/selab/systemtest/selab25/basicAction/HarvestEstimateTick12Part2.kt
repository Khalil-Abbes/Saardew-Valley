package de.unisaarland.cs.se.selab.systemtest.selab25.basicAction

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
/**
 * test actions
 */
class HarvestEstimateTick12Part2 : ExampleSystemTestExtension() {
    override val name = "Test Harvesting Estimate at tick 12 part 2"
    override val description = "testing harvest estimate before the failing BasicMachineAction13 part 2"
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/mediumScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 13
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilString("[INFO] Simulation Info: Tick 11")
        assertCurrentLine("[INFO] Simulation Info: Tick 11 started at tick 12 within the year.")
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 42549 g of APPLE.")
        assertCurrentLine("[INFO] Harvest Estimate: Harvest estimate on tile 40 changed to 42549 g of APPLE.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 41 changed to 708588 g of CHERRY.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 42 changed to 531441 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 43 changed to 531441 g of POTATO.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 44 changed to 145889 g of OAT.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 52 changed to 425152 g of ALMOND.")
        skipUntilString("[INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 164025 g of PUMPKIN.")
        assertCurrentLine("[INFO] Harvest Estimate: Harvest estimate on tile 55 changed to 164025 g of PUMPKIN.")
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
