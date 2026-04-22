package de.unisaarland.cs.se.selab.systemtest.selab25.basictests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**

 */
class BeeHappyMoistureReductionToZeroThisTick2 : ExampleSystemTestExtension() {
    override val name = "BeeHappyMoistureReductionToZeroThisTick2"
    override val description = "Plantation with plant killed on the same tick " +
        "by moisture reduction to zero is NOT affected by BeeHappy."
    override val farms = "BeeHappyMoistureReductionToZeroThisTick/farms.json"
    override val scenario = "BeeHappyMoistureReductionToZeroThisTick/scenario.json"
    override val map = "BeeHappyMoistureReductionToZeroThisTick/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 8 // APR2

    override suspend fun run() {
        skipUntilString("[IMPORTANT] Incident: Incident 0 of type BEE_HAPPY happened and affected tiles .")
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
