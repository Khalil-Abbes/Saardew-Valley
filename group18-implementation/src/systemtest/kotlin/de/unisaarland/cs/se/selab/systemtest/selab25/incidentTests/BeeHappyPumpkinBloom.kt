package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.debug
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.important
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.info
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Bee-Happy around PUMPKIN bloom with correct relative-tick semantics:
 * - SOW at t0 (MAY2), bloom starts at t3 and lasts t3...t4.
 * - Bee at t1 (pre-bloom) -> no effect.
 * - Bee at t3 (during bloom) -> +25% applies.
 * - Bee at t5 (post-bloom) -> no effect.
 * Weeding every second tick after sowing -> do at t2 and t4.
 * Final remaining after t5: 332,149 g.
 */
class BeeHappyPumpkinBloom : ExampleSystemTestExtension() {
    override val name = "BeeHappy_Pumpkin_WithinBloomPeriods"
    override val description =
        "BEE_HAPPY before/during/after PUMPKIN bloom with relative-tick bloom, effect only during bloom."
    override val farms = "farmaction/BeeHappyPumpkin/farms.json"
    override val scenario = "farmaction/BeeHappyPumpkin/scenario.json"
    override val map = "farmaction/BeeHappyPumpkin/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 6 // t=0..5
    override val startYearTick = 10 // MAY2

    private companion object {
        const val PUMPKIN = "PUMPKIN"
        const val BEE_HAPPY = "BEE_HAPPY"
    }

    override suspend fun run() {
        // t0: sow PUMPKIN, estimate applies sunlight first time
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.simulationStart(10))
        assertTickHeader(0, 10)
        assertNextLine(important.farmStart(0))
        assertNextLine(debug.farmActiveSowingPlans(0, listOf(7)))
        assertNextLine(important.farmAction(0, "SOWING", 2, 2))
        assertNextLine(important.farmSowing(0, PUMPKIN, 7))
        assertNextLine(important.machineReturn(0, 0))
        assertNextLine(important.farmFinish(0))
        assertNextLine(info.harvestEstimate(2, 450000, PUMPKIN))

        // t1: pre-bloom bee -> no effect
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.tickStart(1, 11))
        assertNextLine(info.soilMoisture(0, 0))
        assertFarmNoOp()
        assertNextLine(important.incident(1, BEE_HAPPY, emptyList()))
        assertNextLine(info.harvestEstimate(2, 405000, PUMPKIN))

        // t2: weeding due and done; (still pre-bloom)
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.tickStart(2, 12))
        assertNextLine(info.soilMoisture(0, 0))
        assertNextLine(important.farmStart(0))
        assertNextLine(debug.farmActiveSowingPlans(0, emptyList()))
        assertNextLine(important.farmAction(0, "WEEDING", 2, 2))
        assertNextLine(important.machineReturn(0, 0))
        assertNextLine(important.farmFinish(0))
        // no incident at t2 in this test
        assertNextLine(info.harvestEstimate(2, 364500, PUMPKIN))

        // t3: bloom starts, bee applies
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.tickStart(3, 13))
        assertNextLine(info.soilMoisture(0, 0))
        assertFarmNoOp()
        assertNextLine(important.incident(2, BEE_HAPPY, listOf(2)))
        assertNextLine(info.harvestEstimate(2, 410062, PUMPKIN))

        // t4: bloom (2nd tick), weeding due and done; no incident
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.tickStart(4, 14))
        assertNextLine(info.soilMoisture(0, 0))
        assertNextLine(important.farmStart(0))
        assertNextLine(debug.farmActiveSowingPlans(0, emptyList()))
        assertNextLine(important.farmAction(0, "WEEDING", 2, 2))
        assertNextLine(important.machineReturn(0, 0))
        assertNextLine(important.farmFinish(0))
        assertNextLine(info.harvestEstimate(2, 369055, PUMPKIN))

        // t5: post-bloom, bee -> no effect
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.tickStart(5, 15))
        assertNextLine(info.soilMoisture(0, 0))
        assertFarmNoOp()
        assertNextLine(important.incident(3, BEE_HAPPY, emptyList()))
        assertNextLine(info.harvestEstimate(2, 332149, PUMPKIN))

        // stats
        assertZeroHarvestStatsAndRemaining(332149)
    }

    // helpers
    private suspend fun assertTickHeader(simTick: Int, yearTick: Int) {
        assertNextLine(info.tickStart(simTick, yearTick))
        assertNextLine(info.soilMoisture(0, 0))
    }
    private suspend fun assertFarmNoOp() {
        assertNextLine(important.farmStart(0))
        assertNextLine(debug.farmActiveSowingPlans(0, emptyList()))
        assertNextLine(important.farmFinish(0))
    }
    private suspend fun assertZeroHarvestStatsAndRemaining(remaining: Int) {
        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS) == important.statsFarmCollected(
                0,
                0
            )
        )
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach {
            assertNextLine(important.statsTotalPlant(it, 0))
        }
        assertNextLine(important.statsRemaining(remaining))
    }
}
