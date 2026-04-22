package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * CLOUD_CREATION centered on a VILLAGE tile with radius=1.
 * Correctly asserts that only NON-VILLAGE tiles are listed as affected.
 */
class CCVillageLogNotAdded : ExampleSystemTestExtension() {
    override val name = "CloudCreation_AffectedTiles_DOESNOT_include_Village"
    override val description = "incident excludes VILLAGE from affected tiles (should pass)."
    override val farms = "farmaction/CloudCreationVillageTest/farms.json"
    override val scenario = "farmaction/CloudCreationVillageTest/scenario.json"
    override val map = "farmaction/CloudCreationVillageTest/map.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 1
    override val startYearTick = 7

    override suspend fun run() {
        // Jump straight to the incident line and assert the CORRECT expectation.
        skipUntilLogType(LogLevel.IMPORTANT, LogType.INCIDENT)
        assertCurrentLine(
            "[IMPORTANT] Incident: Incident 1 of type CLOUD_CREATION happened and affected tiles 4."
        )
    }
}
