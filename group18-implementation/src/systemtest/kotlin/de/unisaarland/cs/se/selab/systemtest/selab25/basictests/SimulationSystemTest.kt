package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class SimulationSystemTest : ExampleSystemTestExtension() {
    override val name = "ParserTest"
    override val description = "Tests statistics after 0 ticks."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "parser/farmsFigure9.json"
    override val scenario = "parser/scenaroFigure9.json"
    override val map = "parser/tileFigure9.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 1

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, LogType.INITIALIZATION_INFO)
        assertCurrentLine("[IMPORTANT] Initialization Info: tileFigure9.json is invalid.")
    }
}
