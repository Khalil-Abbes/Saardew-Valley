package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * CITY_EXPANSION cannot happen because of active sowing plan because
 * it will affect the only tile in it
 */
class CityExpSowingPlan : ExampleSystemTestExtension() {
    override val name = "CityExpSowingPlanTest_ScenarioFail"
    override val description = "City expansion must not happen before fulfilling the sowing plan"
    override val farms = "farmaction/CEwithSowingPlan/farms.json"
    override val scenario = "farmaction/CEwithSowingPlan/scenario.json"
    override val map = "farmaction/CEwithSowingPlan/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 19 // OCT1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenario.json is invalid.")
    }
}
