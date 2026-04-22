package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * CLOUD_CREATION with radius=1 around FIELD tile 2.
 * - A VILLAGE neighbor is excluded from creation (spec rule).
 * - New clouds are created only on non-VILLAGE tiles in ascending tile-id order (2,4).
 * - Creation tick: no rain/movement/sunlight impact.
 * - Next tick (duration=1): clouds process by id: 0 (FIELD, rains 70 L to cap then dissipates due to duration),
 *   then 1 (MEADOW, rains all 6000 L and dissipates immediately).
 * - No harvest / remaining estimate = 0 (no planted tiles / no plantations).
 */
class CloudCreationRadius : ExampleSystemTestExtension() {
    override val name = "CloudCreation_Radius_Basic"
    override val description = "Radius creation on non-VILLAGE tiles only;" +
        " no movement/rain on creation tick; duration=1."
    override val farms = "farmaction/CloudCreationRadius/farms.json"
    override val scenario = "farmaction/CloudCreationRadius/scenario.json"
    override val map = "farmaction/CloudCreationRadius/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 7 // APR1 (arbitrary; we avoid mowing windows etc.)

    override suspend fun run() {
        // Tick 0 header
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Simulation started at tick 7 within the year."
        )
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 7 within the year.")
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        // Farm phase (no actions)
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // Incident (creates clouds on tiles 2 and 4; VILLAGE 6 excluded)
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type CLOUD_CREATION happened and affected tiles 0,2,4.")

        // Tick 1 header
        assert(
            skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
                == "[INFO] Simulation Info: Tick 1 started at tick 8 within the year."
        )
        assertNextLine("[INFO] Soil Moisture: The soil moisture is below threshold in 0 FIELD and 0 PLANTATION tiles.")

        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 0 on tile 0 rained down 6000 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 0 dissipates on tile 0.")

        // FIELD at tick start dropped 70 L (fallow) -> cloud rains 70 L to cap, then dissipates due to duration=1.
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 1 on tile 2 rained down 140 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 1 dissipates on tile 2.")

        // MEADOW (no capacity): whole 6000 L rains down, immediate dissipation.
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 2 on tile 4 rained down 6000 L water.")
        assertNextLine("[INFO] Cloud Dissipation: Cloud 2 dissipates on tile 4.")

//        [IMPORTANT] Cloud Rain: Cloud 0 on tile 0 rained down 6000 L water.
//        [INFO] Cloud Dissipation: Cloud 0 dissipates on tile 0.
//        [IMPORTANT] Cloud Rain: Cloud 1 on tile 2 rained down 70 L water.
//        [INFO] Cloud Dissipation: Cloud 1 dissipates on tile 2.
//        [IMPORTANT] Cloud Rain: Cloud 2 on tile 4 rained down 6000 L water.
//        [INFO] Cloud Dissipation: Cloud 2 dissipates on tile 4.

        // Farm phase (again no actions)
        assertNextLine("[IMPORTANT] Farm: Farm 0 starts its actions.")
        assertNextLine(
            "[DEBUG] Farm: Farm 0 has the following active " +
                "sowing plans it intends to pursue in this tick: ."
        )
        assertNextLine("[IMPORTANT] Farm: Farm 0 finished its actions.")

        // Statistics: zero everything
        assert(
            skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS)
                == "[IMPORTANT] Simulation Statistics: Farm 0 collected 0 g of harvest."
        )
        listOf("POTATO", "WHEAT", "OAT", "PUMPKIN", "APPLE", "GRAPE", "ALMOND", "CHERRY").forEach {
            assertNextLine("[IMPORTANT] Simulation Statistics: Total amount of $it harvested: 0 g.")
        }
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: Total " +
                "harvest estimate still in fields and plantations: 0 g."
        )
    }
}
