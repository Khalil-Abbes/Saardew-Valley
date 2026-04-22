package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.validation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class NoMachineToSow : ExampleSystemTestExtension() {
    override val name = "sowing plan, but with no machine to it"
    override val description = "sowing plan, but with no machine to it"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/sowinPlanPotato.json"
    override val scenario = "example/scenario.json"
    override val map = "FieldAndPlantation/onePotatoTile.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        skipLines(1)
        assertNextLine("[IMPORTANT] Initialization Info: sowinPlanPotato.json is invalid.")
    }
}
