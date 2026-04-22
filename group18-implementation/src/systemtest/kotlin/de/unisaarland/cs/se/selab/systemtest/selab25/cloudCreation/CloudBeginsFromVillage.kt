package de.unisaarland.cs.se.selab.systemtest.selab25.cloudCreation

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * A simulation cannot begin with a cloud on a village
 */
class CloudBeginsFromVillage : ExampleSystemTestExtension() {
    override val name = "CloudOnVillage_ScenarioFail"
    override val description = "Cloud cannot be created on a village"
    override val farms = "parser/clouds/farms.json"
    override val scenario = "parser/clouds/cloudOnVillage.json"
    override val map = "parser/clouds/tiles.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1 // JAN1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: tiles.json successfully parsed and validated.")
        assertNextLine("[INFO] Initialization Info: farms.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: cloudOnVillage.json is invalid.")
    }
}
