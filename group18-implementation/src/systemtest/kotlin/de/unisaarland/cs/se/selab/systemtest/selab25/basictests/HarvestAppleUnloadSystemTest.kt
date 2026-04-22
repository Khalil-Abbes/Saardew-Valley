package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Harvest APPLE at SEP1, return & unload, stats show 1700000g for APPLE and 0 remaining.
 */
class HarvestAppleUnloadSystemTest : ExampleSystemTestExtension() {
    override val name = "HarvestAppleUnload"
    override val description = "Harvest APPLE at SEP1, return & unload, stats show 1700000g for APPLE and 0 remaining."
    override val farms = "farmaction/harvest-apple-unload/farms.json"
    override val scenario = "farmaction/harvest-apple-unload/scenario.json"
    override val map = "farmaction/harvest-apple-unload/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17 // SEP1 (APPLE harvest window SEP1..OCT1)

    override suspend fun run() {
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Simulation started at tick 17 within the year."
        )
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 17 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        // no active plans -> still logged (empty list)
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active " +
                "sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 2 for 3 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 0.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 unloads 1700000 g of APPLE harvest in the shed.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS)
                == "[IMPORTANT] Simulation Statistics: Farm 0 collected 1700000 g of harvest."
        )
        listOf(
            "POTATO" to 0,
            "WHEAT" to 0,
            "OAT" to 0,
            "PUMPKIN" to 0,
            "APPLE" to 1700000,
            "GRAPE" to 0,
            "ALMOND" to 0,
            "CHERRY" to 0
        ).forEach { (plant, amt) ->
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: $amt g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."
        )
    }
}
