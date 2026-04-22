package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests2

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

/**
 * CITY_EXPANSION turns a freshly sowed WHEAT FIELD into a VILLAGE at tick 0.
 * Expect: sow logs, incident log, then harvest estimate -> 0 g for that tile.
 *
 * startYearTick = 19 (OCT1) so WHEAT sowing is in-window.
 */
class CloudCreationIncidentWrong : ExampleSystemTestExtension() {
    override val name = "Cloud Creation Incident Wrong"
    override val description = " Cloud should  dissipate on tiles without capacity and < 5000"
    override val farms = "CloudCreationIncident/cloudcreationfarm.json"
    override val scenario = "CloudCreationIncident/cloudcreationscenario.json"
    override val map = "CloudCreationIncident/cloudcreationmap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 3
    override val startYearTick = 7

    override suspend fun run() {
        skipLines(3)
        assertNextLine("[INFO] Simulation Info: Simulation started at tick 7 within the year.")
        assertNextLine("[INFO] Simulation Info: Tick 0 started at tick 7 within the year.")
        assertNextLine(
            "[INFO] Soil Moisture: The soil moisture is below threshold " +
                "in 0 FIELD and 0 PLANTATION tiles."
        )
        assertNextLine("[IMPORTANT] Cloud Rain: Cloud 1 on tile 8 rained down 1 L water.")
    }
}
