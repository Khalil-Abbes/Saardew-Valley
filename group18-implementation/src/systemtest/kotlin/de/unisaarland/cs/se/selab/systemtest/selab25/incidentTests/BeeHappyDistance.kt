package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.debug
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.important
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.info
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Bee-Happy range test (meadow swarm radius = 2; incident radius).
 *
 * startYearTick = 8 (APR2, APPLE bloom active). One simulation tick only.
 * Layout (octagons at even coords, squares at odd):
 *  - APPLE at (2,2)=id2, (4,2)=id6, (8,2)=id14
 *  - MEADOW A at (3,3)=id4, MEADOW B at (7,3)=id8
 *
 * Incidents (both at tick 0):
 *  - #1 loc=4 radius=0 (activates MEADOW A) -> affects tiles 2,6
 *  - #2 loc=8 radius=0 (activates MEADOW B) -> affects tiles 6,14
 *
 * Expected end-of-tick estimates (APR2 sunlight 140h → 0.9^3 first, then bee bonuses):
 *  Base after sunlight: 1_700_000 * 0.9^3 = 1_239_300
 *   - tile 2:  +20% once  => 1_487_160
 *   - tile 6:  +20% twice => 1_784_592
 *   - tile 14: +20% once  => 1_487_160
 * Total remaining = 4_758_912 g.
 */
class BeeHappyDistance : ExampleSystemTestExtension() {
    override val name = "BeeHappyDistance"
    override val description = "Bee-Happy range: two meadows, radius=2 from each;" +
        " verify inside vs outside & incident radius."
    override val farms = "farmaction/BeeHappyDistance/farms.json"
    override val scenario = "farmaction/BeeHappyDistance/scenario.json"
    override val map = "farmaction/BeeHappyDistance/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 8 // APR2

    private companion object {
        const val APPLE = "APPLE"
    }

    override suspend fun run() {
        // Header
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.simulationStart(8))
        assertNextLine(info.tickStart(0, 8))
        assertNextLine(info.soilMoisture(0, 0))

        // Farm (no actions)
        assertNextLine(important.farmStart(0))
        assertNextLine(debug.farmActiveSowingPlans(0, emptyList()))
        assertNextLine(important.farmFinish(0))

        // Incidents and affected tiles (this asserts both incident radius and meadow-radius=2)
        assertNextLine(important.incident(1, "BEE_HAPPY", listOf(2, 6)))
        assertNextLine(important.incident(2, "BEE_HAPPY", listOf(6, 14)))

        // Harvest estimates in ascending tile id (2, then 6, then 14)
        assertNextLine(info.harvestEstimate(2, 1487160, APPLE))
        assertNextLine(info.harvestEstimate(6, 1784592, APPLE))
        assertNextLine(info.harvestEstimate(14, 1487160, APPLE))

        // Stats
        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS) == important.statsFarmCollected(
                0,
                0
            )
        )
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", APPLE, "GRAPE", "ALMOND", "CHERRY").forEach {
            assertNextLine(important.statsTotalPlant(it, 0))
        }
        assertNextLine(important.statsRemaining(4758912))
    }
}
