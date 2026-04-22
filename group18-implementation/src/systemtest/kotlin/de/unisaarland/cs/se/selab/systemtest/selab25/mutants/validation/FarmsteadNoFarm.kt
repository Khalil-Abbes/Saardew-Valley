package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.validation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class FarmsteadNoFarm : ExampleSystemTestExtension() {
    override val name = "FarmsteadNotInFarm"
    override val description = "farmstead doesn't show up in farms farmsteads"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "mutants/farms/farmNoFarmstead.json"
    override val scenario = "mutants/scenarios/theScenario.json"
    override val map = "mutants/maps/fieldNotInFarm.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        skipLines(1)
        assertNextLine("[IMPORTANT] Initialization Info: farmNoFarmstead.json is invalid.")
    }
}
