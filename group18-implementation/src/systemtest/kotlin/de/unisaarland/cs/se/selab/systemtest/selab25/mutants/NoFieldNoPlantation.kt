package de.unisaarland.cs.se.selab.systemtest.selab25.mutants

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class NoFieldNoPlantation : ExampleSystemTestExtension() {
    override val name = "NoFieldNoPlantation"
    override val description = "map parsing should fail when there are no field and no plantation"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/farmNoFieldsAndPlantation.json"
    override val scenario = "mutants/scenarios/theScenario.json"
    override val map = "FieldAndPlantation/NoFieldOrPlantation.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: NoFieldOrPlantation.json successfully parsed and validated.")
    }
}
