package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.debug
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.important
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.info
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * BROKEN_MACHINE semantics:
 *  - Incident at t0 with duration=1 makes machine unusable at t1, usable again at t2.
 *  - Crop GRAPE: harvest window SEP1; late penalty 5%/tick afterward.
 * Flow:
 *  t0 (AUG2): CUTTING by machine 1; BROKEN_MACHINE on harvester id=0; affected tile = 0 (shed).
 *  t1 (SEP1): harvester disabled => no harvest; estimate reduced to 1_140_000.
 *  t2 (SEP2): harvester works again => harvest 1_140_000; unload; remaining 0.
 */
class BrokenMachineBasic : ExampleSystemTestExtension() { // I still need to check this.
    override val name = "BrokenMachineSimple"
    override val description = "Broken machine blocks harvester for next tick only; grape harvest delayed by one tick."
    override val farms = "farmaction/BrokenMachineBasic/farms.json"
    override val scenario = "farmaction/BrokenMachineBasic/scenario.json"
    override val map = "farmaction/BrokenMachineBasic/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3 // t=0..2
    override val startYearTick = 16 // AUG2

    private companion object {
        const val GRAPE = "GRAPE"
    }

    override suspend fun run() {
        // ---- t0 (AUG2): CUTTING; then BROKEN_MACHINE applies for next tick ----
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.simulationStart(16))
        assertNextLine(info.tickStart(0, 16))
        assertNextLine(info.soilMoisture(0, 0))

        assertNextLine(important.farmStart(0))
        assertNextLine(debug.farmActiveSowingPlans(0, emptyList()))
        // CUTTING happens in AUG2 (valid window)
        assertNextLine(important.farmAction(1, "CUTTING", 2, 2))
        assertNextLine(important.machineReturn(1, 0))
        assertNextLine(important.farmFinish(0))

        // Incident after farms: machine 0 broken, duration=1; machine sits in shed tile 0
        assertNextLine(important.incident(1, "BROKEN_MACHINE", listOf(0)))
        // No estimate change at t0 (no sun/moisture penalty here)

        // ---- t1 (SEP1): machine 0 unusable -> no harvest; late penalty 5% applies now ----
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.tickStart(1, 17))
        assertNextLine(info.soilMoisture(0, 0))
        assertNextLine(important.farmStart(0))
        assertNextLine(debug.farmActiveSowingPlans(0, emptyList()))
        assertNextLine(important.farmFinish(0))
        // Late penalty kicks in at end of last window tick (your tutors' rule)
        assertNextLine(info.harvestEstimate(2, 1140000, GRAPE))

        // ---- t2 (SEP2): machine usable again -> harvest (uses previous tick's estimate) ----
        assert(skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO) == info.tickStart(2, 18))
        assertNextLine(info.soilMoisture(0, 0))
        assertNextLine(important.farmStart(0))
        assertNextLine(debug.farmActiveSowingPlans(0, emptyList()))
        assertNextLine(important.farmAction(0, "HARVESTING", 2, 3))
        assertNextLine(important.farmHarvest(0, 1140000, GRAPE))
        assertNextLine(important.machineReturn(0, 0))
        assertNextLine(important.machineUnload(0, 1140000, GRAPE))
        assertNextLine(important.farmFinish(0))

        // ---- Stats ----
        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS) == important.statsFarmCollected(
                0,
                1140000
            )
        )
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", GRAPE, "ALMOND", "CHERRY").forEach { plant ->
            val amt = if (plant == GRAPE) 1140000 else 0
            assertNextLine(important.statsTotalPlant(plant, amt))
        }
        assertNextLine(important.statsRemaining(0))
    }
}
