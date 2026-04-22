package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * wrong neighbor adjacent tiles
 */
class EmptyMapParseSuccessful : ExampleSystemTestExtension() {
    override val name = "Empty Map Parse Successful"
    override val description = "Map parsing should successfully finish when map is empty."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/empty-map/emptyFarms.json"
    override val scenario = "parser/empty-map/emptyScenario.json"
    override val map = "parser/empty-map/emptyMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 10

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: emptyMap.json successfully parsed and validated.")
    }
}
