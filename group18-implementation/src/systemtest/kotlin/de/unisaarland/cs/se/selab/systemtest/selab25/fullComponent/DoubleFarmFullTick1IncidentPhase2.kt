package de.unisaarland.cs.se.selab.systemtest.selab25.fullComponent

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class DoubleFarmFullTick1IncidentPhase2 : ExampleSystemTestExtension() {
    override val name = "test all component, double farm, tick 1, incident phase city expansion cloud dissipate first"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "fullComponent/mutipleFarm.json"
    override val scenario = "fullComponent/multiFarmAllScenarioAtOnce.json"
    override val map = "fullComponent/biggerSmallCircularDirectionMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 18
    private val expectedLogs = listOf(
        "[IMPORTANT] Incident: Incident 1 of type ANIMAL_ATTACK happened and affected tiles 10,14.",
        "[IMPORTANT] Incident: Incident 2 of type BEE_HAPPY happened and affected tiles .",
        "[IMPORTANT] Incident: Incident 3 of type CLOUD_CREATION happened and affected tiles 5,7,8,9,10,13,14.",
        "[IMPORTANT] Cloud Union: Clouds 3 and 8 united to cloud 9 with 29150 L water and duration 10 on tile 10.",
        "[IMPORTANT] Incident: Incident 4 of type CITY_EXPANSION happened and affected tiles 16.",
        "[IMPORTANT] Incident: Incident 5 of type BROKEN_MACHINE happened and affected tiles 1.",
        "[IMPORTANT] Incident: Incident 6 of type DROUGHT happened and affected tiles 5.",
        "[IMPORTANT] Incident: Incident 44 of type CITY_EXPANSION happened and affected tiles 8.",
        "[INFO] Cloud Dissipation: Cloud 6 dissipates on tile 8."
    )

    override suspend fun run() {
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipUntilLogType(LogLevel.INFO, LogType.SIMULATION_INFO)
        skipLines(63)
        for (expectedLog in expectedLogs) {
            assertNextLine(expectedLog)
        }
    }
}
