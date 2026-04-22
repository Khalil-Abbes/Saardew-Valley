package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**

 */
class AAPlantationDroughtPrevTick : ExampleSystemTestExtension() {
    override val name = "AAPlantationDroughtPrevTick"
    override val description = "Plantation with plant killed on previous tick is not affected by Animal Attack."
    override val farms = "AAPlantationDroughtPrevTick/farms.json"
    override val scenario = "AAPlantationDroughtPrevTick/scenario.json"
    override val map = "AAPlantationDroughtPrevTick/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 7 // APR1

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 1.")
        skipUntilString("[IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 1.")
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
