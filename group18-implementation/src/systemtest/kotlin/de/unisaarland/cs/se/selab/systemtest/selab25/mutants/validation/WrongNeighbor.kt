package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.validation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class WrongNeighbor : ExampleSystemTestExtension() {
    override val name = "WrongNeighbor"
    override val description = "test village near forest"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "mutants/farms/theFarm.json"
    override val scenario = "mutants/scenarios/theScenario.json"
    override val map = "mutants/maps/VillageNearForest.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 1

    override suspend fun run() {
        assertNextLine("[IMPORTANT] Initialization Info: VillageNearForest.json is invalid.")
    }
}
