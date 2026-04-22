package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class MediumMapParserTest : ExampleSystemTestExtension() {
    override val name = "MediumMapParserTest"
    override val description = "Test parsing of medium map with 2 farms and multiple fields and plantations"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/mediumFarm.json"
    override val scenario = "parser/mediumScenario.json"
    override val map = "parser/mediumMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 7

    override suspend fun run() {
        assertNextLine(
            "[INFO] Initialization Info: smallPlantationMap.json successfully parsed and validated."
        )
        assertNextLine(
            "[INFO] Initialization Info: smallPlantationFarm.json successfully parsed and validated."
        )
        assertNextLine(
            "[INFO] Initialization Info: smallPlantationScenario.json successfully parsed and validated."
        )
    }
}
