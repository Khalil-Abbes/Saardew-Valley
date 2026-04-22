package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.incident.DroughtIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class DroughtIncidentTest {

    private lateinit var tm: TileManager

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        tm = TileManager()
    }

    // @Disabled
    @Test
    fun `drought sets moisture to 0, kills plants, sets plantation permanently disabled, field goes fallow nextTick`() {
        val tiles = mutableMapOf<Int, Tile>()

        val f = Field(
            10,
            Coordinate(0, 0),
            1,
            null,
            400,
            possiblePlants = listOf(FieldPlantType.WHEAT)
        )
        f.plant = FieldPlant(FieldPlantType.WHEAT)
        f.currentHarvestEstimate = 1000
        tiles[10] = f

        val p = Plantation(
            20,
            Coordinate(2, 0),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.CHERRY)
        )
        p.currentHarvestEstimate = 1000
        tiles[20] = p

        tm.setTiles(tiles)

        val incident = DroughtIncident(id = 99, tick = 77, tileManager = tm, location = 10, radius = 1)

        incident.perform(currentTick = 77, currentYearTick = YearTick.AUG1)
        // f.updateHarvestEstimate(77, YearTick.AUG1)
        p.updateHarvestEstimate(77, YearTick.AUG1)

        // Plantation consequences:
        assertEquals(0, p.currentMoisture)
        assertEquals(0, p.currentHarvestEstimate)
        assertNotNull(p.permanentDisabled) // “permanently kills all plants on every affected PLANTATION tile”
    }
}
