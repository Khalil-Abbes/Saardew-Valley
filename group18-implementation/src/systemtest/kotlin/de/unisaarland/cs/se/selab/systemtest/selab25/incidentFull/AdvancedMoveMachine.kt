package de.unisaarland.cs.se.selab.systemtest.selab25.incidentFull

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Verifies machines cannot traverse other farms' FARMSTEAD/FIELD/PLANTATION.
 * Uses the scenario where Farm 0 is blocked on all three routes;
 * Farm 1 harvests APPLE at OCT1.
 */
class AdvancedMoveMachine : ExampleSystemTestExtension() {
    override val name = "AdvancedMoveMachine"
    override val description =
        "Three routes blocked by other farm's FIELD/PLANTATION/FARMSTEAD"

    override val farms = "farmaction/AdvancedMoveMachine/farms.json"
    override val scenario = "farmaction/AdvancedMoveMachine/scenario.json"
    override val map = "farmaction/AdvancedMoveMachine/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 19 // OCT1

    private val expectedLogs = listOf(
        "[INFO] Initialization Info: map.json successfully parsed and validated.",
        "[INFO] Initialization Info: farms.json successfully parsed and validated.",
        "[INFO] Initialization Info: scenario.json successfully parsed and validated.",
        "[INFO] Simulation Info: Simulation started at tick 19 within the year.",
        "[INFO] Simulation Info: Tick 0 started at tick 19 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 0 starts its actions.",
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 5.",
        "[IMPORTANT] Farm: Farm 0 finished its actions.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm Action: Machine 1 performs HARVESTING on tile 22 for 3 days.",
        "[IMPORTANT] Farm Harvest: Machine 1 has collected 1700000 g of APPLE harvest.",
        "[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 32.",
        "[IMPORTANT] Farm Machine: Machine 1 unloads 1700000 g of APPLE harvest in the shed.",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Simulation Info: Simulation ended at tick 1.",
        "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
        "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.",
        "[IMPORTANT] Simulation Statistics: Farm 1 collected 1700000 g of harvest.",
        "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 1700000 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."
    )

    override suspend fun run() {
        for (line in expectedLogs) {
            assertNextLine(line)
        }
    }
}
