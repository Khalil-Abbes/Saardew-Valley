package de.unisaarland.cs.se.selab.systemtest.selab25.incidentFull

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Verifies that a machine loaded with harvest cannot traverse VILLAGE tiles:
 * - Empty machine reaches the APPLE plantation through a VILLAGE.
 * - After harvesting, the machine is loaded and fails to return because
 *   the only path back crosses the same VILLAGE tile.
 */
class MachineNoGoVillage : ExampleSystemTestExtension() {
    override val name = "MachineNoGoVillage_Machine cant go through village when loaded with harvest"
    override val description = "Loaded machine cannot traverse village; must fail to return."
    override val farms = "farmaction/MachineGoNoVillage/farms.json"
    override val scenario = "farmaction/MachineGoNoVillage/scenario.json"
    override val map = "farmaction/MachineGoNoVillage/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 17 // SEP1 (APPLE harvest window)

    override suspend fun run() {
        //  Initialization success lines
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json successfully parsed and validated.")

        //  Tick 0 header
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 17 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 17 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below " +
                "threshold in 0 FIELD and 0 PLANTATION tiles."
        )

        //  Farm phase, harvesting APPLE, then failing to return due to VILLAGE blocking loaded path
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 starts" +
                " its actions."
        )
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active" +
                " sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 4 for 2 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished but failed to return.")
        assertNextLine(
            "[IMPORTANT] Farm: Farm 0 " +
                "finished its actions."
        )

        //  Tick 1 header (no more actions possible)
        assertNextLine("[INFO] Simulation Info: Tick 1 started at tick 18 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following" +
                " active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        //  Tick 2 header (still nothing to do)
        assertNextLine("[INFO] Simulation Info: Tick 2 started at tick 19 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active" +
                " sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        //  Simulation end
        assertNextLine("[IMPORTANT] Simulation Info: Simulation ended at tick 3.")
        assertNextLine("[IMPORTANT] Simulation Info: Simulation statistics are calculated.")

        //  Statistics: 1,700,000 g APPLE collected, nothing else; nothing left in fields/plantations
        assertNextLine("[IMPORTANT] Simulation Statistics: Farm 0 collected 1700000 g of harvest.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of POTATO harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of WHEAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of OAT harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of PUMPKIN harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of APPLE harvested: 1700000 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of GRAPE harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of ALMOND harvested: 0 g.")
        assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of CHERRY harvested: 0 g.")
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total harvest" +
                " estimate still in fields and plantations: 0 g."
        )
    }
}
