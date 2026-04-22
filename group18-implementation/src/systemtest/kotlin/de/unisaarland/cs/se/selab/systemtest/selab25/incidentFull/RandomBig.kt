package de.unisaarland.cs.se.selab.systemtest.selab25.incidentFull

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * test
 */
class RandomBig : ExampleSystemTestExtension() {
    override val name = "RandomBig"
    override val description = "Random_buncha_incidents"
    override val farms = "farmaction/EdgeCase/farms.json"
    override val scenario = "farmaction/EdgeCase/scenario.json"
    override val map = "farmaction/EdgeCase/map.json"
    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 8

    private val initLogs = listOf(
        "[INFO] Initialization Info: map.json successfully parsed and validated.",
        "[INFO] Initialization Info: farms.json successfully parsed and validated.",
        "[INFO] Initialization Info: scenario.json successfully parsed and validated."
    )

    private val tick0Logs = listOf(
        "[INFO] Simulation Info: Simulation started at tick 8 within the year.",
        "[INFO] Simulation Info: Tick 0 started at tick 8 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below" +
            " threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 0" +
            " starts its actions.",
        "[DEBUG] Farm: Farm 0 has the following active sowing" +
            " plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm: Farm 0 finished" +
            " its actions.",
        "[IMPORTANT] Farm: Farm 1 starts its" +
            " actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing" +
            " plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm: Farm 1 finished" +
            " its actions.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 22 changed to 1239300 g of APPLE."
    )

    private val tick1Logs = listOf(
        "[INFO] Simulation Info: Tick 1 started at tick 9 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 0 starts its actions.",
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm: Farm 0 finished its actions.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Incident: Incident 1 of type CITY_EXPANSION happened and affected tiles 24.",
        "[IMPORTANT] Incident: Incident 2 of type CITY_EXPANSION happened and affected tiles 3.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 22 changed to 813104 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 30 changed to 1080000 g of CHERRY."
    )

    private val tick2Logs = listOf(
        "[INFO] Simulation Info: Tick 2 started at tick 10 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 0 starts its actions.",
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm: Farm 0 finished its actions.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Incident: Incident 3 of type BEE_HAPPY happened and affected tiles .",
        "[IMPORTANT] Incident: Incident 4 of type BEE_HAPPY happened and affected tiles .",
        "[IMPORTANT] Incident: Incident 5 of type BEE_HAPPY happened and affected tiles .",
        "[INFO] Harvest Estimate: Harvest estimate on tile 22 changed to 533475 g of APPLE.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 30 changed to 972000 g of CHERRY."
    )

    private val endLogs = listOf(
        "[IMPORTANT] Simulation Info: Simulation ended at tick 3.",
        "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
        "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.",
        "[IMPORTANT] Simulation Statistics: Farm 1 collected 0 g of harvest.",
        "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 2705475 g."
    )

    private val expectedLogs: List<String> =
        initLogs + tick0Logs + tick1Logs + tick2Logs + endLogs

    override suspend fun run() {
        for (line in expectedLogs) {
            assertNextLine(line)
        }
    }
}
