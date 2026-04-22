package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.validation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class CityExpTakeOverOnlyFieldInSowingPlan : ExampleSystemTestExtension() {
    override val name = "CityExpTakeOverOnlyFieldInSowingPlan"
    override val description = "Illegal city expansion on the only field of a sowing plan " +
        "causes parsing error of scenario.json."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "mutants/CityExpTakeOverOnlyFieldInSowingPlan/farms.json"
    override val scenario = "mutants/CityExpTakeOverOnlyFieldInSowingPlan/scenario.json"
    override val map = "mutants/CityExpTakeOverOnlyFieldInSowingPlan/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 10

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: scenario.json is invalid.")
    }
}
