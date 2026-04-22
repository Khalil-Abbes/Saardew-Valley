package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class EmptyMapParseFail : ExampleSystemTestExtension() {
    override val name = "Empty Map Parse Fail"
    override val description = "Map parsing should fail when parsing empty map."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/empty-map/emptyFarms.json"
    override val scenario = "parser/empty-map/emptyScenario.json"
    override val map = "parser/empty-map/emptyMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 10

    override suspend fun run() {
        assertNextLine(
            "[IMPORTANT] Initialization Info: " +
                "emptyMap.json is invalid."
        )
    }
}
