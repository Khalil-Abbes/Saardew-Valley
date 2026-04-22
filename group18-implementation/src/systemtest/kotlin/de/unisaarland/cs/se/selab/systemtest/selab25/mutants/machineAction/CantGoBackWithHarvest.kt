package de.unisaarland.cs.se.selab.systemtest.selab25.mutants.machineAction

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class CantGoBackWithHarvest : ExampleSystemTestExtension() {
    override val name = "No Path With harvest"
    override val description = "machine no come back to shed"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "mutants/farms/MachineHarvestApple.json"
    override val scenario = "mutants/scenarios/theScenario.json"
    override val map = "mutants/maps/baseWithVillage.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 17

    override suspend fun run() {
        skipLines(8)
        assertNextLine("[IMPORTANT] Farm Action: Machine 0 performs HARVESTING on tile 1 for 4 days.")
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 0 has collected 1700000 g of APPLE harvest.")
        assertNextLine("[IMPORTANT] Farm Machine: Machine 0 is finished but failed to return.")
    }
}
