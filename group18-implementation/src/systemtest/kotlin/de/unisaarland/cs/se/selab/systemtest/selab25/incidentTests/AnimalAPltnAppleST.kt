package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Animal Attack on APPLE at SEP1.
 * Sunlight overload and -10% fruit apply, but no mowing penalty this tick due to incident.
 */
class AnimalAPltnAppleST : ExampleSystemTestExtension() {
    override val name = "AnimalAttackApplePlantation"
    override val description = "ANIMAL_ATTACK incident on FOREST adjoining an APPLE plantation at SEP1," +
        " estimate reflects sunlight and -10% fruit, mowing not penalized."
    override val farms = "farmaction/AAOnPltnApple/farms.json"
    override val scenario = "farmaction/AAOnPltnApple/scenario.json"
    override val map = "farmaction/AAOnPltnApple/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17
    // SEP1 (APPLE harvest window includes SEP1; mowing window SEP1 exists but is reset by incident)

    override suspend fun run() {
        // Startup & tick header
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Simulation started at tick 17 within the year."
        )
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 17 within the year.")
        // Moisture above requirement (900 >= 100), so counts are 0/0
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        // Farm phase (no actions possible)
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active" +
                " sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // Incident phase
        assertNextLine("[IMPORTANT] Incident: Incident 2 of type ANIMAL_ATTACK happened and affected tiles 2.")

        // Expectations:
        // Incident affects adjoining APPLE plantation.
        // MOWING need is reset for this tick and next -> no missed-mowing penalty this tick.
        // Fruit -10% applies.
        // Sunlight overage at SEP1 (126h vs comfort 50h) -> 3 steps of 10% (0.9^3).
        // Final estimate = 1_700_000 * 0.9^3 (from Sunlight) * 0.9 (from AA) = 1_115_370 g.
        // Harvest-estimate recompute: final APPLE estimate = 1_115_370 g

        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1115370 g of APPLE.")

        // Statistics (no harvest collected, only estimate remains)
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
            "[IMPORTANT] Simulation Statistics: " +
                "Total harvest estimate still in fields and plantations: 1115370 g."
        )
    }
}
