package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class StuckMachineEmptyTileTest : ExampleSystemTestExtension() {
    override val name = "Stuck machine because of empty tile."
    override val description = "Test for machine to not move as there is no path"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "farmaction/machineTest/stuckMachineFarm.json"
    override val scenario = "farmaction/machineTest/stuckMachineScenario.json"
    override val map = "farmaction/machineTest/smallBrokenMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 20

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipLines(1)
        assertNextLine("[IMPORTANT] Farm: Farm 1 starts its actions.")
        skipLines(1)
        assertNextLine("[IMPORTANT] Farm: Farm 1 finished its actions.")
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 9 were not performed: HARVESTING.")
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 9 changed to 0 g of GRAPE.")
    }
}
