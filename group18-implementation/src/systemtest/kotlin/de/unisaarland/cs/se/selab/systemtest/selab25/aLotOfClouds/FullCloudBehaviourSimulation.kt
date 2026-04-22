package de.unisaarland.cs.se.selab.systemtest.selab25.aLotOfClouds

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Test default behaviour of clouds - all rain, move and merge cases + cloud creation incident in >1 ticks
 * */
class FullCloudBehaviourSimulation : ExampleSystemTestExtension() {
    override val name = "Full cloud simulation"
    override val description = "this is for 4 ticks log"
    override val farms = "CloudFullSimulation/farms.json"
    override val scenario = "CloudFullSimulation/scenario.json"
    override val map = "CloudFullSimulation/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 1

    val logs = listOf(
        "[INFO] Initialization Info: src/systemtest/resources/" +
            "CloudFullSimulation/map.json successfully parsed and validated.",
        "[INFO] Initialization Info: " +
            "src/systemtest/resources/CloudFullSimulation/farms.json successfully parsed and validated.",
        "[INFO] Initialization Info: " +
            "src/systemtest/resources/CloudFullSimulation/scenario.json successfully parsed and validated.",
        "[INFO] Simulation Info: Simulation started at tick 1 within the year.",
        "[INFO] Simulation Info: Tick 0 started at tick 1 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[INFO] Cloud Movement: Cloud 1 with 1450 L water moved from tile 1 to tile 2.",
        "[DEBUG] Cloud Movement: On tile 1, the amount of sunlight is 95.",
        "[INFO] Cloud Movement: Cloud 1 with 1450 L water moved from tile 2 to tile 7.",
        "[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Union: Clouds 3 and 1 united to cloud 18 with 6860 L water and duration 2 on tile 7.",
        "[INFO] Cloud Movement: Cloud 2 with 1870 L water moved from tile 9 to tile 1.",
        "[INFO] Cloud Movement: Cloud 2 with 1870 L water moved from tile 1 to tile 2.",
        "[DEBUG] Cloud Movement: On tile 1, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 2 with 1870 L water moved from tile 2 to tile 7.",
        "[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 92.",
        "[IMPORTANT] Cloud Union: Clouds 18 and 2 united to cloud 19 with 8730 L water and duration 2 on tile 7.",
        "[INFO] Cloud Movement: Cloud 4 with 1810 L water moved from tile 11 to tile 2.",
        "[INFO] Cloud Movement: Cloud 4 with 1810 L water moved from tile 2 to tile 7.",
        "[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 89.",
        "[IMPORTANT] Cloud Union: Clouds 19 and 4 united to cloud 20 with 10540 L water and duration 1 on tile 7.",
        "[IMPORTANT] Cloud Rain: Cloud 20 on tile 7 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 20 with 10440 L water moved from tile 7 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 7, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 20 on tile 4 rained down 10440 L water.",
        "[INFO] Cloud Dissipation: Cloud 20 dissipates on tile 4.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Incident: Incident 1 of type CLOUD_CREATION happened and affected tiles 2,3,8,11,12,13.",
        "[IMPORTANT] Cloud Union: Clouds 5 and 22 united to cloud 23 with 7090 L water and duration 2 on tile 3.",
        "[IMPORTANT] Cloud Union: Clouds 6 and 24 united to cloud 25 with 6650 L water and duration 2 on tile 8.",
        "[IMPORTANT] Incident: Incident 2 of type CLOUD_CREATION happened and affected tiles 1,9.",
        "[INFO] Simulation Info: Tick 1 started at tick 2 within the year.",
        "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
        "[INFO] Cloud Dissipation: Cloud 17 dissipates on tile 6.",
        "[IMPORTANT] Cloud Rain: Cloud 21 on tile 2 rained down 200 L water.",
        "[INFO] Cloud Movement: Cloud 21 with 5050 L water moved from tile 2 to tile 7.",
        "[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 95.",
        "[IMPORTANT] Cloud Rain: Cloud 21 on tile 7 rained down 100 L water.",
        "[INFO] Cloud Movement: Cloud 21 with 4950 " +
            "L water moved from tile 7 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 7, the amount of sunlight is 95.",
        "[INFO] Cloud Movement: Cloud 21 with 4950 " +
            "L water moved from tile 4 to tile 2.",
        "[INFO] Cloud Movement: Cloud 21 with 4950 L " +
            "water moved from tile 2 to tile 7.",
        "[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 21 with 4950 L water moved from tile 7 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 7, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 21 with 4950 L water moved from tile 4 to tile 2.",
        "[INFO] Cloud Movement: Cloud 21 with 4950 L water moved from tile 2 to tile 7.",
        "[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 89.",
        "[INFO] Cloud Movement: Cloud 21 with 4950 L water moved from tile 7 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 7, the amount of sunlight is 89.",
        "[INFO] Cloud Movement: Cloud 21 with 4950 L water moved from tile 4 to tile 2.",
        "[INFO] Cloud Movement: Cloud 21 with 4950 L water moved from tile 2 to tile 7.",
        "[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 86.",
        "[IMPORTANT] Cloud Rain: Cloud 23 on tile 3 rained down 7090 L water.",
        "[INFO] Cloud Dissipation: Cloud 23 dissipates on tile 3.",
        "[IMPORTANT] Cloud Rain: Cloud 25 on tile 8 rained down 6650 L water.",
        "[INFO] Cloud Dissipation: Cloud 25 dissipates on tile 8.",
        "[IMPORTANT] Cloud Rain: Cloud 26 on tile 11 rained down 5250 L water.",
        "[INFO] Cloud Dissipation: Cloud 26 dissipates on tile 11.",
        "[IMPORTANT] Cloud Rain: Cloud 27 on tile 12 rained down 140 L water.",
        "[INFO] Cloud Movement: Cloud 27 with 5110 L water moved from tile 12 to tile 10.",
        "[DEBUG] Cloud Movement: On tile 12, the amount of sunlight is 95.",
        "[INFO] Cloud Dissipation: Cloud 27 got stuck on tile 10.",
        "[IMPORTANT] Cloud Rain: Cloud 28 on tile 13 rained down 5250 L water.",
        "[INFO] Cloud Dissipation: Cloud 28 dissipates on tile 13.",
        "[INFO] Cloud Movement: Cloud 29 with 4700 L water moved from tile 1 to tile 2.",
        "[DEBUG] Cloud Movement: On tile 1, the amount of sunlight is 95.",
        "[INFO] Cloud Movement: Cloud 29 with 4700 L water moved from tile 2 to tile 7.",
        "[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 83.",
        "[IMPORTANT] Cloud Union: Clouds 21 and 29 united to cloud 31 with 9650 L water and duration 1 on tile 7.",
        "[INFO] Cloud Movement: Cloud 30 with 4700 L water moved from tile 9 to tile 1.",
        "[INFO] Cloud Movement: Cloud 30 with 4700 L water moved from tile 1 to tile 2.",
        "[DEBUG] Cloud Movement: On tile 1, the amount of sunlight is 92.",
        "[INFO] Cloud Movement: Cloud 30 with 4700 L water moved from tile 2 to tile 7.",
        "[DEBUG] Cloud Movement: On tile 2, the amount of sunlight is 80.",
        "[IMPORTANT] Cloud Union: Clouds 31 and 30 united to cloud 32 with 14350 L water and duration 1 on tile 7.",
        "[INFO] Cloud Movement: Cloud 32 with 14350 L water moved from tile 7 to tile 4.",
        "[DEBUG] Cloud Movement: On tile 7, the amount of sunlight is 86.",
        "[IMPORTANT] Cloud Rain: Cloud 32 on tile 4 rained down 14350 L water.",
        "[INFO] Cloud Dissipation: Cloud 32 dissipates on tile 4.",
        "[IMPORTANT] Farm: Farm 1 starts its actions.",
        "[DEBUG] Farm: Farm 1 has the following active sowing plans it intends to pursue in this tick: .",
        "[IMPORTANT] Farm: Farm 1 finished its actions.",
        "[IMPORTANT] Incident: Incident 4 of type DROUGHT happened and affected tiles 1,2,7,12.",
        "[IMPORTANT] Incident: Incident 7 of type CITY_EXPANSION happened and affected tiles 8.",
        "[IMPORTANT] Incident: Incident 9 of type DROUGHT happened and affected tiles 1,2.",
        "[IMPORTANT] Incident: Incident 11 of type CLOUD_CREATION happened and affected tiles 1,2,3,4,6,7,11.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 0 g of CHERRY.",
        "[INFO] Harvest Estimate: Harvest estimate on tile 7 changed to 0 g of CHERRY.",
        "[IMPORTANT] Simulation Info: Simulation ended at tick 2.",
        "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
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
        for (log in logs) {
            assertNextLine(log)
        }
    }
}
