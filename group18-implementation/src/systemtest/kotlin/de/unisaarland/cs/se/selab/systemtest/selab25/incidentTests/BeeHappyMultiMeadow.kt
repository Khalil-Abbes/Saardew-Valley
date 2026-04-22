package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.debug
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.important
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.info
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * One BEE_HAPPY incident whose area contains two MEADOW tiles.
 * Verify the APPLE tile is affected only ONCE by the incident (no per-meadow double-count).
 * startYearTick = 8 (APR2, APPLE bloom), single tick.
 * Final expected: 1,487,160 g.
 */
class BeeHappyMultiMeadow : ExampleSystemTestExtension() {
    override val name = "BeeHappySingleIncidentMultiMeadows"
    override val description = "Single BEE_HAPPY with 2 meadows in range doesn't double-boost;" +
        " applies once per incident per tile."
    override val farms = "farmaction/BeeHappyStacking/farms.json"
    override val scenario = "farmaction/BeeHappyStacking/scenario1.json"
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

        // Incident affects APPLE tile 2 exactly once
        assertNextLine(important.incident(1, "BEE_HAPPY", listOf(2)))

        // Harvest estimate (sun first, then +20%)
        assertNextLine(info.harvestEstimate(2, 1487160, "APPLE"))

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
        assertNextLine(important.statsRemaining(1487160))
    }
}
