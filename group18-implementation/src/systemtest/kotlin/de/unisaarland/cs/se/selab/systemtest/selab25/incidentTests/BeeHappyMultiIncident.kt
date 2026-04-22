package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.debug
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.important
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.info
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Two BEE_HAPPY incidents (20% then 10%) in the same tick hitting the same APPLE tile.
 * Verify multiplicative stacking: ×1.2 then ×1.1.
 * startYearTick = 8 (APR2), single tick.
 * Final expected: 1,635,876 g.
 */
class BeeHappyMultiIncident : ExampleSystemTestExtension() {
    override val name = "BeeHappyTwoIncidentsStack"
    override val description = "Two BEE_HAPPY incidents on same tile in same tick should stack multiplicatively."
    override val farms = "farmaction/BeeHappyStacking/farms.json"
    override val scenario = "farmaction/BeeHappyStacking/scenario2.json"
    override val map = "farmaction/BeeHappyStacking/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 8

    override suspend fun run() {
        // Header
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.simulationStart(8))
        assertNextLine(info.tickStart(0, 8))
        assertNextLine(info.soilMoisture(0, 0))

        // No farm actions
        assertNextLine(important.farmStart(0))
        assertNextLine(debug.farmActiveSowingPlans(0, emptyList()))
        assertNextLine(important.farmFinish(0))

        // Two incidents hit the APPLE tile
        assertNextLine(important.incident(1, "BEE_HAPPY", listOf(2)))
        assertNextLine(important.incident(2, "BEE_HAPPY", listOf(2)))

        // Harvest estimate (sun first, then +20%, then +10%)
        assertNextLine(info.harvestEstimate(2, 1635876, "APPLE"))

        // Stats
        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS) == important.statsFarmCollected(
                0,
                0
            )
        )
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach {
            assertNextLine(important.statsTotalPlant(it, 0))
        }
        assertNextLine(important.statsRemaining(1635876))
    }
}
