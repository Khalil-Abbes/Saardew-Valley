package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Example system test
 */
class TripleExpansionSuccess : ExampleSystemTestExtension() {
    override val name = "TripleExpansionTest"
    override val description = "Test expansion on same tick"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallFieldCityExpansionFarm.json"
    override val scenario = "FieldAndPlantation/smallFieldCityExpansionScenarioSuccess.json"
    override val map = "FieldAndPlantation/smallFieldMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 10

    override suspend fun run() {
        skipLines(2)
        assertNextLine(
            "[IMPORTANT] Initialization Info: " +
                "smallFieldCityExpansionScenario.json is invalid."
        )
    }
}
