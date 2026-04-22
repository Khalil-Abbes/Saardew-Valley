package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**

 */
class BeeHappyPlantationDeadPlantPrevTick : ExampleSystemTestExtension() {
    override val name = "BeeHappyPlantationDeadPlantPrevTick"
    override val description = "Plantation with plant killed on previous tick is not affected by BeeHappy."
    override val farms = "BeeHappyPlantationDeadPlantPrevTick/farms.json"
    override val scenario = "BeeHappyPlantationDeadPlantPrevTick/scenario.json"
    override val map = "BeeHappyPlantationDeadPlantPrevTick/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 7 // APR1

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 1.")
        skipUntilString("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles .")
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
