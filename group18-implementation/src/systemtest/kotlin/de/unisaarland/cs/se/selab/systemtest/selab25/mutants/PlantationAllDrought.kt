package de.unisaarland.cs.se.selab.systemtest.selab25.mutants

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class PlantationAllDrought : ExampleSystemTestExtension() {
    override val name = "PlantationAllDrought"
    override val description = "Tests for all plantation hit with drought should have 0 harvest estimate left."

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallPlantationFarm.json"
    override val scenario = "FieldAndPlantation/plantationDrought.json"
    override val map = "FieldAndPlantation/smallPlantationMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 2
    override val startYearTick = 20

    override suspend fun run() {
        skipUntilLogType(LogLevel.IMPORTANT, LogType.SIMULATION_STATISTICS)
        skipLines(8)
        assertNextLine(
            "[IMPORTANT] Simulation Statistics: " +
                "Total harvest estimate still in fields and plantations: 0 g."
        )
    }
}
