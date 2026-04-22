package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * WHEAT is sowed at OCT1. Animal attack from adjacent FOREST halves FIELD estimate.
 * Expected end-of-tick estimate on tile 2: 750000 g WHEAT.
 */
class AnimalAFieldST : ExampleSystemTestExtension() {
    override val name = "AnimalAttackFieldHalves"
    override val description = "Animal attack halves FIELD harvest estimate."
    override val farms = "farmaction/AAField/farms.json"
    override val scenario = "farmaction/AAField/scenario.json"
    override val map = "farmaction/AAField/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 19 // OCT1 (WHEAT sow window)

    private companion object {
        const val WHEAT = "WHEAT"
    }

    override suspend fun run() {
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == LogBuilder.info.simulationStart(19))
        assertNextLine(LogBuilder.info.tickStart(0, 19))
        assertNextLine(LogBuilder.info.soilMoisture(0, 0))

        // Farm sows
        assertNextLine(LogBuilder.important.farmStart(0))
        assertNextLine(LogBuilder.debug.farmActiveSowingPlans(0, listOf(5)))
        assertNextLine(LogBuilder.important.farmAction(0, "SOWING", 2, 4))
        assertNextLine(LogBuilder.important.farmSowing(0, WHEAT, 5))
        assertNextLine(LogBuilder.important.machineReturn(0, 0))
        assertNextLine(LogBuilder.important.farmFinish(0))

        // Incident
        assertNextLine(LogBuilder.important.incident(2, "ANIMAL_ATTACK", listOf(2)))

        // Estimate after incidents (1_500_000 -> 750_000)
        assertNextLine(LogBuilder.info.harvestEstimate(2, 750000, WHEAT))

        // Stats
        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS)
                == LogBuilder.important.statsFarmCollected(0, 0)
        )
        listOf("POTATO", WHEAT, "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { p ->
            val amt = if (p == WHEAT) 0 else 0
            assertNextLine(LogBuilder.important.statsTotalPlant(p, amt))
        }
        assertNextLine(LogBuilder.important.statsRemaining(750000))
    }
}
