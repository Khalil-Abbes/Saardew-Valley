package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.basictests.INCIDENT
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * BROKEN_MACHINE semantics:
 *  - Incident at t0 with duration=1 makes machine unusable at t1, usable again at t2.
 *  - Crop GRAPE: harvest window SEP1; late penalty 5%/tick afterward.
 * Flow:
 *  t0 (AUG2): CUTTING by machine 1; BROKEN_MACHINE on harvester id=0; affected tile = 0 (shed).
 *  t1 (SEP1): harvester disabled => no harvest; estimate reduced to 1_140_000.
 *  t2 (SEP2): harvester works again => harvest 1_140_000; unload; remaining 0.
 */
class SmallDroughtOnField : ExampleSystemTestExtension() { // I still need to check this.
    override val name = "SmallDroughtOnField"
    override val description = "check if unsowed field is affected by drought"
    override val farms = "FieldAndPlantation/smallFieldFarm.json"
    override val scenario = "FieldAndPlantation/droughtOnField.json"
    override val map = "FieldAndPlantation/smallFieldMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1 // t=0..2
    override val startYearTick = 1 // AUG2

    private companion object {
        const val GRAPE = "GRAPE"
    }

    override suspend fun run() {
        skipUntilString(INCIDENT)
        assertCurrentLine("[IMPORTANT] Incident: Incident 1 of type DROUGHT happened and affected tiles 9.")
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
