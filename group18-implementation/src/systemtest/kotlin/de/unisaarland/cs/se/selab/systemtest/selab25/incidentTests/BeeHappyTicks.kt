package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Two Bee-Happy incidents stack on APPLE (blooming, insect-pollinated),
 * later Bee-Happy during GRAPE bloom has no effect (self-pollinating).
 *
 * startYearTick = 8 (APR2).
 * Mapping: t0->8 (APR2), t1->9 (MAY1), t2->10 (MAY2), t3->11 (JUN1), t4->12 (JUN2).
 */
class BeeHappyTicks : ExampleSystemTestExtension() {
    override val name = "DoubleBeeHappy_MultipleTicks_GrapeAndApple"
    override val description =
        "Two Bee-Happy incidents stack on APPLE in bloom, later Bee-Happy during GRAPE bloom affects no tiles."
    override val farms = "farmaction/BeeHappyTicks/farms.json"
    override val scenario = "farmaction/BeeHappyTicks/scenario.json"
    override val map = "farmaction/BeeHappyTicks/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 8 // APR2

    override suspend fun run() {
        // Tick 0 (APR2)
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Simulation started at tick 8 within the year."
        )
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 8 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture" +
                " is below threshold in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine(
            "[IMPORTANT] Farm: " +
                "Farm 0 starts its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following" +
                " active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine(
            "[IMPORTANT] Farm: Farm" +
                " 0 finished its actions."
        )

        // Two Bee-Happy incidents affect APPLE tile 2
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 2.")
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles 2.")

        // Tick 0 (APR2): APPLE blooms and needs insects -> two incidents (25% and 20%) stack multiplicatively.
        // Sun APR2: 140h vs APPLE comfort 50h -> floor((140-50)/25)=3 -> ×0.9^3.
        // Result end of t0: 1_700_000 × 0.9^3 × 1.25 × 1.20 = 1_858_950 g.
        // Recompute: stacked bee effect on APPLE (with 0.9^3 sunlight first)
        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1858950 g of APPLE.")

        // Tick 1 (MAY1)
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Tick 1 started at tick 9 within the year."
        )
        // Tick 2 (MAY2)
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Tick 2 started at tick 10 within the year."
        )
        // Tick 3 (JUN1)
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Tick 3 started at tick 11 within the year."
        )
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        // Tick 3 (JUN1): APPLE mowing window, mower acts (no penalty).
        // Mowing on APPLE (prevents 10% miss penalty at JUN1)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs MOWING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // Tick 4 (JUN2, GRAPE bloom)
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Tick 4 started at tick 12 within the year."
        )
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // Tick 4 (JUN2): GRAPE blooms but self-pollinates -> Bee-Happy has NO effect (affected tiles empty).
        assertNextLine("[IMPORTANT] Incident: Incident 3 of type BEE_HAPPY happened and affected tiles .")
        assertNextLine("[IMPORTANT] Incident: Incident 4 of type BEE_HAPPY happened and affected tiles .")

        // Statistics
        assertZeroHarvestStatsAndRemaining()
    }

    private suspend fun assertZeroHarvestStatsAndRemaining() {
        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS)
                == "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest."
        )
        listOf(
            "POTATO",
            "WHEAT",
            "OAT",
            "PUMPKIN",
            "APPLE",
            "GRAPE",
            "ALMOND",
            "CHERRY"
        ).forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        // Final expected remaining = APPLE(344,464) (APPLE dropped a lot due to sunlight penalties over ticks)
        // + GRAPE(1,200,000) = 1,544,464 g
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1544464 g."
        )
    }
}
