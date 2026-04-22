package de.unisaarland.cs.se.selab.systemtest.selab25.incidentFull

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.debug
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.important
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.info

/**
 * APPLE estimate decay with repeated missed IRRIGATING; no actions.
 */
class MissedIrrigationTest : ExampleSystemTestExtension() {
    override val name = "ARandomTest_Apple_MissedIrrigating"
    override val description = "APPLE estimate decay with repeated missed IRRIGATING; no actions."

    override val farms = "farmaction/ARandomTest/farms.json"
    override val scenario = "farmaction/ARandomTest/scenario.json"
    override val map = "farmaction/ARandomTest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 8
    override val startYearTick = 21 // NOV1

    private companion object {
        const val APPLE = "APPLE"
        const val IRRIGATING = "IRRIGATING"
    }

    private val expected = listOf(
        info.parseSuccess("map.json"),
        info.parseSuccess("farms.json"),
        info.parseSuccess("scenario.json"),

        info.simulationStart(21),
        info.tickStart(0, 21),
        info.soilMoisture(0, 0),
        important.farmStart(100),
        debug.farmActiveSowingPlans(100, emptyList()),
        important.farmFinish(100),
        info.harvestEstimate(101, 1_530_000, APPLE),

        info.tickStart(1, 22),
        info.soilMoisture(0, 0),
        important.farmStart(100),
        debug.farmActiveSowingPlans(100, emptyList()),
        important.farmFinish(100),
        info.harvestEstimate(101, 1_377_000, APPLE),

        info.tickStart(2, 23),
        info.soilMoisture(0, 1),
        important.farmStart(100),
        debug.farmActiveSowingPlans(100, emptyList()),
        important.farmFinish(100),
        info.harvestEstimate(101, 1_239_300, APPLE),

        info.tickStart(3, 24),
        info.soilMoisture(0, 1),
        important.farmStart(100),
        debug.farmActiveSowingPlans(100, emptyList()),
        important.farmFinish(100),
        debug.missedActions(101, IRRIGATING),
        info.harvestEstimate(101, 0, APPLE),

        info.tickStart(4, 1),
        info.soilMoisture(0, 1),
        important.farmStart(100),
        debug.farmActiveSowingPlans(100, emptyList()),
        important.farmFinish(100),
        debug.missedActions(101, IRRIGATING),

        info.tickStart(5, 2),
        info.soilMoisture(0, 1),
        important.farmStart(100),
        debug.farmActiveSowingPlans(100, emptyList()),
        important.farmFinish(100),
        debug.missedActions(101, IRRIGATING),

        info.tickStart(6, 3),
        info.soilMoisture(0, 1),
        important.farmStart(100),
        debug.farmActiveSowingPlans(100, emptyList()),
        important.farmFinish(100),
        debug.missedActions(101, IRRIGATING),

        info.tickStart(7, 4),
        info.soilMoisture(0, 1),
        important.farmStart(100),
        debug.farmActiveSowingPlans(100, emptyList()),
        important.farmFinish(100),
        debug.missedActions(101, IRRIGATING),

        important.simEnd(8),
        "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
        important.statsFarmCollected(100, 0),
        important.statsTotalPlant("POTATO", 0),
        important.statsTotalPlant("WHEAT", 0),
        important.statsTotalPlant("OAT", 0),
        important.statsTotalPlant("PUMPKIN", 0),
        important.statsTotalPlant("APPLE", 0),
        important.statsTotalPlant("GRAPE", 0),
        important.statsTotalPlant("ALMOND", 0),
        important.statsTotalPlant("CHERRY", 0),
        important.statsRemaining(0)
    )

    override suspend fun run() {
        for (line in expected) {
            assertNextLine(line)
        }
    }
}
