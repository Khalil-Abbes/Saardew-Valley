package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * BEE_HAPPY on ALMOND at FEB2 (yearTick 4).
 * CUTTING is done (allowed in Feb) -> no halving penalty.
 * Feb sunlight 112h <= comfort 130h -> no sunlight penalty.
 * Moisture 1100L >= req 400L -> no moisture penalty.
 * ALMOND blooms at tick 4..5 and needs insects -> +25% applies.
 * Final estimate: 800000 -> 1000000 g.
 * Recompute: estimate increases by +25% -> 1,000,000 g
 */
class BeeHappyPltnAlmondST : ExampleSystemTestExtension() {
    override val name = "BeeHappyAlmond"
    override val description =
        "BEE_HAPPY incident at a MEADOW (radius 0) adjacent to ALMOND plantation at FEB2, " +
            "CUTTING done, +25% pollination boost -> estimate 1,000,000g."
    override val farms = "farmaction/BeeHappyAlmond/farms.json"
    override val scenario = "farmaction/BeeHappyAlmond/scenario.json"
    override val map = "farmaction/BeeHappyAlmond/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 4 // FEB2 (ALMOND bloom window)

    override suspend fun run() {
        // Start & tick header
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Simulation started at tick 4 within the year."
        )
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 4 within the year.")
        // Moisture above threshold everywhere
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        // Farm phase: CUTTING on ALMOND
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active" +
                " sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs CUTTING on tile 2 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // Incident phase: BEE_HAPPY affects tile 2
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 2.")

        // Expectations:
        // CUTTING is performed this tick (allowed in Feb) -> no halving penalty.
        // Feb sunlight 112h <= comfort 130h -> no sunlight penalty.
        // Moisture 1100L >= req 400L -> no moisture penalty.
        // ALMOND blooms at tick 4..5 and needs insects -> +25% applies.
        // Final estimate: 800000 -> 1000000 g.
        // Recompute: estimate increases by +25% -> 1,000,000 g

        assertNextLine("[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1000000 g of ALMOND.")

        // Statistics: nothing harvested; remaining estimate is 1,000,000 g
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
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 1000000 g."
        )
    }
}
