package de.unisaarland.cs.se.selab.systemtest.selab25.incidentFull

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Verifies Apple harvest across two ticks, mowing miss penalty application, and failed return.
 * Uses the MoreComplexMachine resources; asserts the exact emitted log lines.
 */
class MoreComplexMachine : ExampleSystemTestExtension() {
    override val name = "MoreComplexMachine"
    override val description =
        "Farm 0 harvests APPLE on two plantations across two ticks; first unloads on different farmstead," +
            " second fails to return; harvest estimate updated with missed MOWING penalty."

    override val farms = "farmaction/MoreComplexMachine/farms.json"
    override val scenario = "farmaction/MoreComplexMachine/scenario.json"
    override val map = "farmaction/MoreComplexMachine/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 17

    private val expectedLogs = listOf(
        "[INFO] Initialization Info: map.json successfully parsed and validated.",
        "[INFO] Initialization Info: farms.json successfully parsed and validated.",
        "[INFO] Initialization Info: scenario.json successfully parsed and validated.",
        "[INFO] Simulation Info: Simulation started at tick 17 within the year.",
        "[INFO] Simulation Info: Tick 0 started at tick 17 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 0 starts its actions.",
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 24 for 2 days.",
        "[IMPORTANT] Farm Harvest: Machine 0 has collected 1700000 g of APPLE harvest.",
        "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 2000.",
        "[IMPORTANT] Farm Machine: Machine 0 unloads 1700000 g of APPLE harvest in the shed.",
        "[IMPORTANT] Farm: Farm 0 finished its actions.",
        "[DEBUG] Harvest Estimate: Required actions on tile 30 were not performed: MOWING.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 30 changed to 1115370 g of APPLE.",
        "[INFO] Simulation Info: Tick 1 started at tick 18 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 0 starts its actions.",
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 30 for 2 days.",
        "[IMPORTANT] Farm Harvest: Machine 0 has collected 1115370 g of APPLE harvest.",
        "[IMPORTANT] Farm Machine: Machine 0 is finished but failed to return.",
        "[IMPORTANT] Farm: Farm 0 finished its actions.",
        "[IMPORTANT] Simulation Info: Simulation ended at tick 2.",
        "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
        "[IMPORTANT] Simulation Statistics: Farm 0 collected 2815370 g of harvest.",
        "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 2815370 g.",
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
