package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * city expansion with unmatching location
 */
class CityExpansionNotVillageOrRoad1 : ExampleSystemTestExtension() {
    override val name = "CityExpansionNotVillageOrRoad1"
    override val description = "location not field or road for city exp"
    override val farms = "farmaction/CityExp/farms.json"
    override val scenario = "farmaction/CityExp/scenario.json"
    override val map = "farmaction/CityExp/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 19 // OCT1

    override suspend fun run() {
        assertNextLine("[INFO] Initialization Info: map.json successfully parsed and validated.")
    }
}
