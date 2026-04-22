package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.incident.BeeHappyIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.parser.TileFactory
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

class BeeHappyIncidentTest {

    private lateinit var tm: TileManager

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        tm = TileManager()
    }

    @Test
    fun `bee happy increases by effect only for insect-pollinated plants that are blooming`() {
        val tiles = mutableMapOf<Int, Tile>()

        // Two MEADOWS within incident radius (to ensure "only once per impacted tile")
        tiles[200] = TileFactory.createMeadowTile(200, Coordinate(0, 0), null)
        tiles[201] = TileFactory.createMeadowTile(201, Coordinate(2, 0), null)

        // FIELD within 2 tiles of meadows, POTATO (INSECTS, Relative bloom after sowing 3, dur 1)
        val field = Field(
            10,
            Coordinate(0, 2),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.POTATO)
        )
        field.plant = FieldPlant(FieldPlantType.POTATO)
        field.currentHarvestEstimate = 1000
        // Sowing 3 ticks before currentTick=100 -> blooming window hits at tick 100...101
        field.plant!!.setProgress(
            ActionType.SOWING,
            97,
            YearTick.MAR2
        )
        tiles[10] = field

        // APPLE plantation also within 2 tiles and in bloom in APR2...MAY1
        val apple = Plantation(
            20,
            Coordinate(2, 2),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.APPLE)
        )
        apple.currentHarvestEstimate = 1000
        tiles[20] = apple

        tm.setTiles(tiles)

        val incident =
            BeeHappyIncident(
                id = 1,
                tick = 100,
                tileManager = tm,
                location = 200,
                radius = 1,
                effect = 20
            )

        // Perform (collects affected farmables) then update estimates to apply effect
        incident.perform(currentTick = 100, currentYearTick = YearTick.MAY1)

        // Neutralize environment
        field.sunlightThisTick = field.plant!!.type.sunlightComfort
        field.currentMoisture = field.plant!!.type.moistureRequired
        apple.sunlightThisTick = apple.plant.type.sunlightComfort
        apple.currentMoisture = apple.plant.type.moistureRequired

        field.updateHarvestEstimate(100, YearTick.MAY1)
        apple.updateHarvestEstimate(100, YearTick.MAY1)

        // +20% once per tile (Bee Happy)
        assertEquals(1000, field.currentHarvestEstimate)
        assertEquals(1200, apple.currentHarvestEstimate)
    }
}
