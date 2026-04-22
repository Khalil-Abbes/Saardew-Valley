package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Test sowing a single WHEAT field at OCT1, machine returns to shed,
 * stats reflect remaining harvest.
 */
class SowPlanNoField : ExampleSystemTestExtension() {
    override val name = "sow plan but no possible tile, invalid farm parse"
    override val description = ""
    override val farms = "FieldAndPlantation/potatoFarm.json"
    override val scenario = "FieldAndPlantation/smallFieldScenario.json"
    override val map = "FieldAndPlantation/smallPotatoMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 7

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: smallPotatoMap.json successfully parsed and validated.")
        assertNextLine("[IMPORTANT] Initialization Info: potatoFarm.json is invalid.")
    }
}
