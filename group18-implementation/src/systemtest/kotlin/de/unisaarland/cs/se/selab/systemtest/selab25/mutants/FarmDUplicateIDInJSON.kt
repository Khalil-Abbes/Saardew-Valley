package de.unisaarland.cs.se.selab.systemtest.selab25.mutants

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogLevel
// import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogType

/**
 * Example system test
 */
class FarmDUplicateIDInJSON : ExampleSystemTestExtension() {
    override val name = "duplicateID in Json"
    override val description = ""

    // Paths are relative from the `src/systemtest/resources` directory.
    override val farms = "FieldAndPlantation/smallPlantationFarmDuplicateID.json"
    override val scenario = "FieldAndPlantation/plantationDrought.json"
    override val map = "FieldAndPlantation/smallPlantationMap.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 0
    override val startYearTick = 20

    override suspend fun run() {
        skipLines(1)
        assertNextLine(
            "[INFO] Initialization Info: smallPlantationFarmDuplicateID.json successfully parsed and validated."
        )
    }
}
