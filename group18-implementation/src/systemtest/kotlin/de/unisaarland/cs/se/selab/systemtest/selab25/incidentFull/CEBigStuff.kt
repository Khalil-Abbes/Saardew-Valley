package de.unisaarland.cs.se.selab.systemtest.selab25.incidentFull

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * test
 */
class CEBigStuff : ExampleSystemTestExtension() {
    override val name = "CityExpansion_BigStuff"
    override val description = "a lot city expansions for sowing, grape late harvest, cloud on road."

    override val farms = "farmaction/CCBigStuff/farms.json"
    override val scenario = "farmaction/CCBigStuff/scenario.json"
    override val map = "farmaction/CCBigStuff/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 19

    private val expectedLogs = listOf(
        "[INFO] Initialization Info: map.json successfully parsed and validated.",
        "[INFO] Initialization Info: farms.json successfully parsed and validated.",
        "[INFO] Initialization Info: scenario.json successfully parsed and validated.",
        "[INFO] Simulation Info: Simulation started at tick 19 within the year.",
        "[INFO] Simulation Info: Tick 0 started at tick 19 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below" +
            " threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 0" +
            " starts its actions.",
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 1.",
        "[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 20 for 4 days.",
        "[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 1.",
        "[IMPORTANT] Farm Action: Machine 1 performs SOWING on tile 22 for 4 days.",
        "[IMPORTANT] Farm Sowing: Machine 1 has sowed WHEAT according to sowing plan 1.",
        "[IMPORTANT] Farm Machine: Machine 1 is finished and returns to the shed at 100.",
        "[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 30 for 4 days.",
        "[IMPORTANT] Farm Harvest: Machine 0 has collected 1083000 g of GRAPE harvest.",
        "[IMPORTANT] Farm Machine: Machine 0 is finished and returns to the shed at 100.",
        "[IMPORTANT] Farm Machine: Machine 0 unloads 1083000 g of GRAPE harvest in the shed.",
        "[IMPORTANT] Farm: Farm 0 finished" +
            " its actions.",
        "[IMPORTANT] Incident: Incident 1 of type CITY_EXPANSION happened and affected tiles 111.",
        "[IMPORTANT] Incident: Incident 2 of type CITY_EXPANSION happened and affected tiles 110.",
        "[IMPORTANT] Incident: Incident 3 of type CITY_EXPANSION happened and affected tiles 20.",
        "[IMPORTANT] Incident: Incident 4 of type CITY_EXPANSION happened and affected tiles 203.",
        "[IMPORTANT] Incident: Incident 5 of type CITY_EXPANSION happened and affected tiles 32.",
        "[IMPORTANT] Incident: Incident 6 of type CITY_EXPANSION happened and affected tiles 125.",
        "[INFO] Simulation Info: Tick 1 started at tick 20 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 0 starts its actions.",
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm: Farm 0 finished its actions.",
        "[IMPORTANT] Incident: Incident 7 of type CITY_EXPANSION happened and affected tiles 115.",
        "[IMPORTANT] Incident: Incident 8 of type CITY_EXPANSION happened and affected tiles 24.",
        "[IMPORTANT] Incident: Incident 9 of type CITY_EXPANSION happened and affected tiles 121.",
        "[IMPORTANT] Incident: Incident 10 of type CITY_EXPANSION happened and affected tiles 40.",
        "[IMPORTANT] Incident: Incident 100 of type CLOUD_CREATION happened and affected tiles 112.",
        "[INFO] Simulation Info: Tick 2 started at tick 21 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Cloud Rain: Cloud 0 on tile 112 rained down 6000 L water.",
        "[INFO] Cloud Dissipation: Cloud 0 dissipates on tile 112.",
        "[IMPORTANT] Farm: Farm 0 starts its actions.",
        "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm: Farm 0 finished its actions.",
        "[IMPORTANT] Incident: Incident 11 of type CITY_EXPANSION happened and affected tiles 26.",
        "[IMPORTANT] Incident: Incident 12 of type CITY_EXPANSION happened and affected tiles 44.",
        "[IMPORTANT] Incident: Incident 13 of type CITY_EXPANSION happened and affected tiles 123.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 30 changed to 1200000 g of GRAPE.",
        "[IMPORTANT] Simulation Info: Simulation ended at tick 3.",
        "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
        "[IMPORTANT] Simulation Statistics: Farm 0 collected 1083000 g of harvest.",
        "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 1083000 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 2700000 g."
    )

    override suspend fun run() {
        for (line in expectedLogs) {
            assertNextLine(line)
        }
    }
}
