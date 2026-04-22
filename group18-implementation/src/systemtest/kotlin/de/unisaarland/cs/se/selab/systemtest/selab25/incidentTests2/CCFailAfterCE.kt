package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * CITY_EXPANSION turns a FIELD into a VILLAGE at tick 0 and then
 * a cloud creation incident attempts to create a cloud only there.
 * Expect: scenario parser fail
 *
 * startYearTick = 19 (OCT1)
 */
class CCFailAfterCE : ExampleSystemTestExtension() {
    override val name = "cloud starting on village fail scenario"
    override val description = "creation of clouds where the affected tiles is village and we create a cloud on it"

    override val farms = "parser/CloudCreation/farms.json"
    override val scenario = "parser/CloudCreation/scenario.json"
    override val map = "parser/CloudCreation/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 7

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenario.json is invalid.")
    }
}
