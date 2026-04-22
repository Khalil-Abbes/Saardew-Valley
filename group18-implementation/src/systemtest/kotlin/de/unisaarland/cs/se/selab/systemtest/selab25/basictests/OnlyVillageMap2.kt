package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Example system test
 */
class OnlyVillageMap2 : ExampleSystemTestExtension() {
    override val name = "OnlyVillageMap2"
    override val description = "Test map only 1 tile = village tile. Fail in farm parse."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/onlyVillageNoFarm.json"
    override val scenario = "example/scenario.json"
    override val map = "parser/onlyVillage.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 10

    override suspend fun run() {
        assertNextLine(
            "[INFO] Initialization Info: " +
                "onlyVillage.json successfully parsed and validated."
        )
        assertNextLine(
            "[IMPORTANT] Initialization Info: " +
                "onlyVillageNoFarm.json is invalid."
        )
    }
}
