package de.unisaarland.cs.se.selab.systemtest.selab25.cloudCreation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * A simulation cannot begin with a cloud on an empty tile
 */
class CloudBeginsFromEmpty : ExampleSystemTestExtension() {
    override val name = "CloudOnEmpty_ScenarioFail"
    override val description = "Cloud cannot be created on an empty tile"
    override val farms = "parser/clouds/farms.json"
    override val scenario = "parser/clouds/cloudEmptyTile.json"
    override val map = "parser/clouds/tiles.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 5 // MAR1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: tiles.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: cloudEmptyTile.json is invalid.")
    }
}
