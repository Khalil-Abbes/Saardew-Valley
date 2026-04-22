package de.unisaarland.cs.se.selab.systemtest.selab25.incidentFull

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * test
 */
class IncidentMix : ExampleSystemTestExtension() {
    override val name = "Incident_Mix_includes_BH,AA,CC,DR"
    override val description = "BeeHappy, AnimalAttack, CloudCreation->Move->Merge->Rain->Dissipate," +
        " Drought, Plantation Mowing; including sowing priority."

    // Paths are relative to src/systemtest/resources
    override val farms = "farmaction/IncidentMix/farms.json"
    override val scenario = "farmaction/IncidentMix/scenario.json"
    override val map = "farmaction/IncidentMix/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 20

    override suspend fun run() {
        val expectedLogs = listOf(
            // Init successes
            "[INFO] Initialization Info: map.json successfully" +
                " parsed and validated.",
            "[INFO] Initialization Info: farms.json" +
                " successfully parsed and validated.",
            "[INFO] Initialization Info: scenario.json" +
                " successfully parsed and validated.",

            // Start + Tick 0
            "[INFO] Simulation Info: Simulation started at tick 20 within the year.",
            "[INFO] Simulation Info: Tick 0 started at tick 20 within the year.",
            "[INFO] Soil Moisture: The soil moisture is below" +
                " threshold in 0 FIELD and 0 PLANTATION tiles.",
            "[IMPORTANT] Farm: Farm 0" +
                " starts its actions.",
            "[DEBUG] Farm: Farm 0 has the following active sowing" +
                " plans it intends to pursue in this tick: 5.",
            "[IMPORTANT] Farm: Farm" +
                " 0 finished its actions.",
            "[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles .",
            "[DEBUG] Harvest Estimate: Required actions on tile 2 were not performed: HARVESTING.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 0 g of APPLE.",
            "[DEBUG] Harvest Estimate: Required actions on tile 6 were not performed: HARVESTING.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 0 g of GRAPE.",
            "[DEBUG] Harvest Estimate: Required actions on tile 10 were not performed: HARVESTING.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 0 g of ALMOND.",

            // Tick 1
            "[INFO] Simulation Info: Tick 1 started at tick 21 within the year.",
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
            "[IMPORTANT] Farm: Farm 0 starts its actions.",
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 5.",
            "[IMPORTANT] Farm: Farm 0 finished its actions.",
            "[IMPORTANT] Incident: Incident 2 of type ANIMAL_ATTACK happened and affected tiles 6,10,20.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1530000 g of APPLE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 600000 g of GRAPE.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 10 changed to 720000 g of ALMOND.",

            // Tick 2
            "[INFO] Simulation Info: Tick 2 started at tick 22 within the year.",
            "[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.",
            "[IMPORTANT] Farm: Farm 0 starts its actions.",
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: 5.",
            "[IMPORTANT] Farm: Farm 0 finished its actions.",
            "[IMPORTANT] Incident: Incident 3 of type CLOUD_CREATION happened and affected tiles 2.",
            "[IMPORTANT] Incident: Incident 4 of type CLOUD_CREATION happened and affected tiles 6.",
            "[INFO] Harvest Estimate: Harvest estimate on tile 2 changed to 1377000 g of APPLE.",

            // End + Stats
            "[IMPORTANT] Simulation Info: Simulation ended at tick 3.",
            "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
            "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest.",
            "[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.",
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: 2697000 g."
        )

        for (line in expectedLogs) {
            assertNextLine(line)
        }
    }
}
