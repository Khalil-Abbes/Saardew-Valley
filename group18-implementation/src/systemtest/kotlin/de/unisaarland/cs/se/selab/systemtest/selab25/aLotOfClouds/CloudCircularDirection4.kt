package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfClouds

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class CloudCircularDirection4 : ExampleSystemTestExtension() {
    override val name = "CloudTestOnCircularDirection, incident phase"
    override val description = "this is for tick 1 log"
    override val farms = "fullComponent/AllSimFarm.json"
    override val scenario = "fullComponent/AllScenarioAtOnce.json"
    override val map = "fullComponent/smallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.HARVEST_ESTIMATE)
        assertCurrentLine("[INFO] Harvest Estimate: Harvest estimate on tile 5 changed to 0 g of CHERRY.")
    }
}
