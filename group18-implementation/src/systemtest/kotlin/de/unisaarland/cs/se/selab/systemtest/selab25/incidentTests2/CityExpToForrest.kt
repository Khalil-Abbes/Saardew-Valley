package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * CITY_EXPANSION is tried where the target tile borders with a forest
 */
class CityExpToForrest : ExampleSystemTestExtension() {
    override val name = "CityExpNexToForrest_FAILS"
    override val description = "City expansion product tile cannot border with forest"
    override val farms = "parser/cityExpansion/farms.json"
    override val scenario = "parser/cityExpansion/scenario.json"
    override val map = "parser/cityExpansion/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 16 // AUG21

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: scenario.json is invalid.")
    }
}
