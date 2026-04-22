package de.unisaarland.cs.se.selab.systemtest.selab25.mutants

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class FarmsteadAdjoinDifferentFarm : ExampleSystemTestExtension() {
    override val name = "2 farmsteads with different farm tiles adjoining it"
    override val description = " should fail in parsing farm"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "mutants/farms/2farmswrongfarmstead.json"
    override val scenario = "mutants/scenarios/theScenario.json"
    override val map = "mutants/maps/2farmswrongfarmsteadmap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 20

    override suspend fun run() {
        assertNextLine(
            "[IMPORTANT] Initialization Info: 2farmswrongfarmsteadmap.json is invalid."
        )
    }
}
