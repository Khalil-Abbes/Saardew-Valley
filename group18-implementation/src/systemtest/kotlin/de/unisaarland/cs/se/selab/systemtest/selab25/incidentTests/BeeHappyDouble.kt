package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Two BEE_HAPPY incidents in the same tick at the same location (MEADOW),
 * during GRAPE blooming (JUN2, yearTick 12). GRAPE self-pollinates, so bees
 * have NO effect even though it's in range and blooming.
 */
class BeeHappyDouble : ExampleSystemTestExtension() {
    override val name = "Double_BeeHappy_GrapeOnBloom"
    override val description =
        "Passes on ours, if fail on theirs -> logging bug found: Two BEE_HAPPY incidents " +
            "at the same MEADOW in the same tick during GRAPE bloom, no effect on self-pollinating GRAPE."
    override val farms = "farmaction/BeeHappyDouble/farms.json"
    override val scenario = "farmaction/BeeHappyDouble/scenario.json"
    override val map = "farmaction/BeeHappyDouble/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 12 // JUN2, GRAPE blooming tick

    override suspend fun run() {
        // Startup & tick header
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Simulation started at tick 12 within the year."
        )
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 12 within the year.")
        // Moisture safely above thresholds everywhere
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        // Farm phase: no relevant actions
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active" +
                " sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // Expect:
        // Both incidents log with an EMPTY affected-tiles list.
        // No harvest-estimate change lines.
        // Final remaining estimate = 1,200,000 g (GRAPE initial).

        // Incidents: both BEE_HAPPY should report *no* impacted tiles
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles .")
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles .")

        // No harvest estimate changes should be printed (GRAPE unaffected).
        // Jump straight to statistics.
        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS)
                == "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest."
        )
        listOf(
            "POTATO" to 0,
            "WHEAT" to 0,
            "OAT" to 0,
            "PUMPKIN" to 0,
            "APPLE" to 0,
            "GRAPE" to 0,
            "ALMOND" to 0,
            "CHERRY" to 0
        ).forEach { (plant, amt) ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: $amt g.")
        }
        // Remaining estimate: GRAPE initial unchanged
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1200000 g."
        )
    }
}
