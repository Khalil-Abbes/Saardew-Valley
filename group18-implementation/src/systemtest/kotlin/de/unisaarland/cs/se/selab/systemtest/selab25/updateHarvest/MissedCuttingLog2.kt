package de.unisaarland.cs.se.selab.systemtest.selab25.updateHarvest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Test sowing a single WHEAT field at OCT1, machine returns to shed,
 * stats reflect remaining harvest.
 */
class MissedCuttingLog2 : ExampleSystemTestExtension() {
    override val name = "MissedCuttingLog2"
    override val description = "we dont log missed cutting at all when drought happen same tick"
    override val farms = "FieldAndPlantation/smallPlantationFarmDrought2.json"
    override val scenario = "FieldAndPlantation/plantationDrought2.json"
    override val map = "FieldAndPlantation/smallPlantationMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipLines(10)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 0 g of APPLE.")
    }
}
