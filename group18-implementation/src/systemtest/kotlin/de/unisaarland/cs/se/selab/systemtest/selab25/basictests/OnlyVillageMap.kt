package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Example system test
 */
class OnlyVillageMap : ExampleSystemTestExtension() {
    override val name = "OnlyVillageMap"
    override val description = "Test map with only village, fail in map parse."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/onlyVillageNoFarm.json"
    override val scenario = "example/scenario.json"
    override val map = "parser/onlyVillage.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 10

    override suspend fun run() {
        assertNextLine(
            "[IMPORTANT] Initialization Info: " +
                "onlyVillage.json is invalid."
        )
    }
}
