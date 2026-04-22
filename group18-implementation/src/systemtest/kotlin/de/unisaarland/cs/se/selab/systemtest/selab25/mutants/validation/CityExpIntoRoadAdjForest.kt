package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.validation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class CityExpIntoRoadAdjForest : ExampleSystemTestExtension() {
    override val name = "CityExpIntoRoadAdjForest"
    override val description = "test village near forest"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "mutants/CityExpIntoRoadAdjForest/farms.json"
    override val scenario = "mutants/CityExpIntoRoadAdjForest/scenario.json"
    override val map = "mutants/CityExpIntoRoadAdjForest/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenario.json is invalid.")
    }
}
