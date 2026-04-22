package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val INCIDENT = "[IMPORTANT] Incident"

/**
 * Example system test
 */
class PotatoBeeHappyTest17 : ExampleSystemTestExtension() {
    override val name = "PotatoBeeHappyTest17"
    override val description = "Test the interaction of BeeHappy and Potato"

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallFieldPotatoFarm.json"
    override val scenario = "FieldAndPlantation/smallFieldPotatoBeeHappyScenarioAll.json"
    override val map = "FieldAndPlantation/smallFieldMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 5
    override val startYearTick = 7

    override suspend fun run() {
        skipUntilString(INCIDENT)
        assertCurrentLine(emptyIncident(2))
        skipUntilString(INCIDENT)
        assertCurrentLine(emptyIncident(1))
        skipUntilString(INCIDENT)
        assertCurrentLine(emptyIncident(3))
    }
    private fun emptyIncident(id: Int): String {
        return "[IMPORTANT] Incident: Incident $id of type BEE_HAPPY happened and affected tiles ."
    }
    private suspend fun skipUntilString(startString: String): String {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        return if (line.startsWith(startString)) {
            line
        } else {
            skipUntilString(startString)
        }
    }
}
