package de.unisaarland.cs.se.selab.systemtest.selab25.cloudCreation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * A simulation cannot begin with 2 clouds on the same tile
 */
class CloudsBeginWithCollision : ExampleSystemTestExtension() {
    override val name = "CloudsColliding_ScenarioFail"
    override val description = "2 Clouds cannot start from one tile"
    override val farms = "parser/clouds/farms.json"
    override val scenario = "parser/clouds/cloudCollision.json"
    override val map = "parser/clouds/tiles.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 4 // FEB2

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: tiles.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: cloudCollision.json is invalid.")
    }
}
