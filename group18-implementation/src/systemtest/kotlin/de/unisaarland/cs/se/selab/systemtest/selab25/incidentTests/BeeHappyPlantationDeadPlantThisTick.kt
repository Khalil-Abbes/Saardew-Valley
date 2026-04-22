package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**

 */
class BeeHappyPlantationDeadPlantThisTick : ExampleSystemTestExtension() {
    override val name = "BeeHappyPlantationDroughtThisTick"
    override val description = "Plantation with plant killed on the same tick by a drought is affected by BeeHappy."
    override val farms = "BeeHappyPlantationDroughtThisTick/farms.json"
    override val scenario = "BeeHappyPlantationDroughtThisTick/scenario.json"
    override val map = "BeeHappyPlantationDroughtThisTick/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 8 // APR2

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Incident: Incident 0 of type DROUGHT happened and affected tiles 1.")
        assertNextLine("[IMPORTANT] Incident: Incident 1 of type BEE_HAPPY happened and affected tiles 1.")
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
