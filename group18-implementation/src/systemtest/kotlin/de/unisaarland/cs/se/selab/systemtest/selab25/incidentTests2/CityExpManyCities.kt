package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Many city expansion incidents are created to test how they interact with farming and cloud incidents
 * */
class CityExpManyCities : ExampleSystemTestExtension() {
    override val name = "CityExpDestroyEachFarmAndCloud"
    override val description = "Many city expansions interact with clouds and farming"

    override val farms = "CityExpManyCEs/farms.json"
    override val scenario = "CityExpManyCEs/scenario.json"
    override val map = "CityExpManyCEs/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 5 // MAR2

    private val logs = listOf(
        "[INFO] Initialization Info: " +
            "src/systemtest/resources/CityExpManyCEs/map.json successfully parsed and validated.",
        "[INFO] Initialization Info: src/" +
            "systemtest/resources/CityExpManyCEs/farms.json successfully parsed and validated.",
        "[INFO] Initialization Info: src/systemtest" +
            "/resources/CityExpManyCEs/scenario.json successfully parsed and validated.",
        "[INFO] Simulation Info: Simulation started at tick 5 within the year.",
        "[INFO] Simulation Info: Tick 0 started at tick 5 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 2.",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 5,7.",
        "[IMPORTANT] Incident: Incident 2 of type CITY_EXPANSION happened and affected tiles 2.",
        "[IMPORTANT] Incident: Incident 6 of type CITY_EXPANSION happened and affected tiles 5.",
        "[INFO] Simulation Info: Tick 1 started at tick 6 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: 1,2.",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Incident: Incident 5 of type CITY_EXPANSION happened and affected tiles 3.",
        "[IMPORTANT] Incident: Incident 7 of type CLOUD_CREATION happened and affected tiles 6.",
        "[INFO] Simulation Info: Simulation ended at tick 2.",
        "[INFO] Simulation Info: Simulation statistics are calculated.",
        "[IMPORTANT] Simulation Statistics: Farm 1 collected 0 g of harvest.",
        "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.",
        "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 0 g."
    )

    override suspend fun run() {
        for (line in logs) {
            assertNextLine(line)
        }
    }
}
