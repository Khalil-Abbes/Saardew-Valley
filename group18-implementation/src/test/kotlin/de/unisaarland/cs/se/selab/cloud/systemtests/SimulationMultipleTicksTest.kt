package de.unisaarland.cs.se.selab.cloud.systemtests

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.incident.Incident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.Simulation
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Direction
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.PrintWriter

private val tileField = Field(
    id = 1,
    coordinate = Coordinate(0, 0),
    farm = 1,
    direction = Direction.ANGLE_90,
    moistureCapacity = 5000,
    possiblePlants = emptyList()
)

private val tilePlantation = Plantation(
    id = 2,
    coordinate = Coordinate(2, 0),
    farm = 1,
    direction = Direction.ANGLE_270,
    plant = PlantationPlant(PlantationPlantType.APPLE),
    moistureCapacity = 5000
)

private val cloud = Cloud(id = 1, duration = 4, location = 1, amount = 8000)

class SimulationMultipleTicksTest {
    @Test
    fun `run many ticks from APR1 with empty farms and incidents`() {
        // Arrange: real TileManager and CloudHandler with no tiles/clouds
        val tileManager = TileManager()
        tileField.sunlightThisTick = 100
        tileField.currentMoisture = 4500
        tilePlantation.sunlightThisTick = 40
        tilePlantation.currentMoisture = 4200

        tileManager.setTiles(mutableMapOf(1 to tileField, 2 to tilePlantation))
        val cloudHandler = CloudHandler(tileManager, mutableListOf(cloud))

        // Make sure logging doesn't write to disk or fail
        Logger.concreteLogger = DebugLogger(
            PrintWriter(".\\src\\test\\kotlin\\de\\unisaarland\\cs\\se\\selab\\cloud\\systemtests\\logSimulation.txt")
        )

        // Empty farms and incidents
        val farms = emptyMap<Int, Farm>()
        val incidents = emptyList<Incident>()

        val startTick = 0
        val startYearTick = YearTick.APR1
        val maxTick = 2

        val sim = Simulation(
            tileManager = tileManager,
            cloudHandler = cloudHandler,
            farms = farms,
            incidents = incidents,
            currentTick = startTick,
            currentYearTick = startYearTick,
            maxTick = maxTick
        )

        // Act
        sim.run()

        // Assert: currentTick advanced by 25 iterations (0..24), currentYearTick advanced by 25 getNext() calls
        assertEquals(
            maxTick,
            sim.currentTick,
            "Simulation should advance currentTick by 2"
        )

        var expectedYearTick = startYearTick
        repeat(maxTick) {
            expectedYearTick = expectedYearTick.getNext()
        }
        assertEquals(
            expectedYearTick,
            sim.currentYearTick,
            "YearTick should advance by 25 steps"
        )
        assertEquals(
            cloudHandler.clouds.first().availableMovesLeft,
            0,
            "cloud must have no moves at the end of the tick"
        )
    }
}
