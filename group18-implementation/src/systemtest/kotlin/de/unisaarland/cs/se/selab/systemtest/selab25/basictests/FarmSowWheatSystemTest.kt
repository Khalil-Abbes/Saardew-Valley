package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Test sowing a single WHEAT field at OCT1, machine returns to shed,
 * stats reflect remaining harvest.
 */
class FarmSowWheatSystemTest : ExampleSystemTestExtension() {
    override val name = "FarmSowWheat"
    override val description = "Sowing a single WHEAT field at OCT1, " +
        "machine returns to shed, stats reflect remaining harvest."
    override val farms = "farmaction/sow-wheat-happy/farms.json"
    override val scenario = "farmaction/sow-wheat-happy/scenario.json"
    override val map = "farmaction/sow-wheat-happy/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 19 // OCT1 (fits WHEAT sowing window OCT1...OCT2)

    override suspend fun run() {
        // Start + tick header
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Simulation started at tick 19 within the year."
        )
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 19 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        // Farm phase (order & content)
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 5."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs SOWING on tile 2 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Sowing: Machine 0 has sowed WHEAT according to sowing plan 5.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // Jump to statistics
        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS)
                == "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest."
        )
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach { plant ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate" +
                " still in fields and plantations: 1500000 g."
        )
    }
}
