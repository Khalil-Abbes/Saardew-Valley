package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * CLOUD_CREATION centered on a VILLAGE tile with radius=1.
 * This asserts (WRONGLY) that the VILLAGE tile is included in affected tiles.
 * Correct behavior excludes VILLAGE from affected tiles, so this test should FAIL.
 */
class CCVillageLogAdded : ExampleSystemTestExtension() {
    override val name = "CloudCreation_AffectedTiles_includes_Village"
    override val description = "asserts incident includes VILLAGE in affected tiles (should fail)."
    override val farms = "farmaction/CloudCreationVillageTest/farms.json"
    override val scenario = "farmaction/CloudCreationVillageTest/scenario.json"
    override val map = "farmaction/CloudCreationVillageTest/map.json"

    override val logLevel = "IMPORTANT"
    override val maxTicks = 1
    override val startYearTick = 7

    override suspend fun run() {
        // Jump straight to the incident line and assert the WRONG expectation.
        skipUntilLogType(LogLevel.IMPORTANT, LogType.INCIDENT)
        assertCurrentLine(
            "[IMPORTANT] Incident: Incident 1 of type CLOUD_CREATION happened and affected tiles 2,4."
        )
    }
}
