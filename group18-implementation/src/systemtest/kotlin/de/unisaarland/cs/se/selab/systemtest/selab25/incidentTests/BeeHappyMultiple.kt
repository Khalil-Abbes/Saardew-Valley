package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Bee Happy with multiple meadows and a self-pollinating control.
 *
 * Setup:
 * - FEB2 (yearTick 4): ALMOND is blooming and needs insects; GRAPE is self-pollinating and not affected.
 * - Two MEADOW tiles are inside the BEE_HAPPY incident's affected area (radius 2).
 */
class BeeHappyMultiple : ExampleSystemTestExtension() {
    override val name = "BeeHappy_Multi_Meadow_Almond"
    override val description =
        "BEE_HAPPY with two MEADOW sources in radius, buffs blooming ALMOND once, GRAPE unaffected."
    override val farms = "farmaction/BeeHappyMultiple/farms.json"
    override val scenario = "farmaction/BeeHappyMultiple/scenario.json"
    override val map = "farmaction/BeeHappyMultiple/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 4 // FEB2

    override suspend fun run() {
        // Startup & tick header
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Simulation started at tick 4 within the year."
        )
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 4 within the year.")
        // No moisture deficits
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        // Farm phase: CUTTING on ALMOND
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // Incident phase: BEE_HAPPY should list only tiles actually impacted (ALMOND=2). GRAPE (6) must not be listed.
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 2.")

        // Expectations:
        // Farm performs CUTTING on ALMOND (Feb window) -> no halving penalty.
        // No sunlight penalty (Feb 112h <= ALMOND comfort 130h; GRAPE comfort 150h).
        // Moisture fine: capacity 1200 -> after -100L = 1100L >= req (400 ALMOND, 250 GRAPE).
        // BEE_HAPPY applies +25% to ALMOND exactly once despite multiple meadows.
        // GRAPE unchanged (self-pollinates).
        // Final ALMOND estimate: 800000 * 1.25 = 1000000 g.

        // Recompute: ALMOND boosted once by +25%, GRAPE unchanged -> only ALMOND logs a change
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1000000 g of ALMOND.")

        // Stats: nothing harvested; remaining = 1,000,000 g
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
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 2200000 g."
        )
    }
}
