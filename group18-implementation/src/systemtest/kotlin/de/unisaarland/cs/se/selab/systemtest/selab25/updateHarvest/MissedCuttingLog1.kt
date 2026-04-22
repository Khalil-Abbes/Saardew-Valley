package de.unisaarland.cs.se.selab.systemtest.selab25.updateHarvest

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Test sowing a single WHEAT field at OCT1, machine returns to shed,
 * stats reflect remaining harvest.
 */
class MissedCuttingLog1 : ExampleSystemTestExtension() {
    override val name = "MissedCuttingLog1 False"
    override val description = "we log missed cutting the same tick as drought"
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
        assertNextLine("[DEBUG] Harvest Estimate: Required actions on tile 10 were not performed: CUTTING.")
    }
}
