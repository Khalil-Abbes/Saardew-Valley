package de.unisaarland.cs.se.selab.systemtest.selab25.incidentFull

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * Test CloudCreation->Move->Rain->Dissipate,
 *         BeeHappy, CloudMerge, Drought, CityExpansion, AnimalAttack,
 *         CloudCreation->Move->Merge->Rain->Dissipate
 * */
class AllIncidentsInteraction : ExampleSystemTestExtension() {
    override val name = "Incident_Mix_includes_CC_BH_CM_DR_CE_AA_CC"
    override val description = "CloudCreation->Move->Rain->Dissipate, " +
        "BeeHappy, CloudMerge, Drought, CityExpansion, AnimalAttack, " +
        "CloudCreation->Move->Merge->Rain->Dissipate"

    // Paths are relative to src/systemtest/resources
    override val farms = "AllIncidents/farms.json"
    override val scenario = "AllIncidents/scenario.json"
    override val map = "AllIncidents/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 4
    override val startYearTick = 1

    override suspend fun run() {
        // Just for displaying the logs
        assertEnd()
    }
}
