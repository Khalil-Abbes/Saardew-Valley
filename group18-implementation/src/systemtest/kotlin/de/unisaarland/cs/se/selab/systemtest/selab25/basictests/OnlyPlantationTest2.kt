package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class OnlyPlantationTest2 : ExampleSystemTestExtension() {
    override val name = "OnlyPlantationTest2"
    override val description = "Tests for plantation and machine only for planation from OCT2 to NOV1. " +
        "(specific version: logging harvest)"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallPlantationFarm.json"
    override val scenario = "FieldAndPlantation/smallPlantationScenario.json"
    override val map = "FieldAndPlantation/smallPlantationMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 20

    override suspend fun run() {
        skipLines(9)
        assertNextLine("[IMPORTANT] Farm Harvest: Machine 2 has collected 720000 g of ALMOND harvest.")
    }
}
