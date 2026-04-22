package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.incident.AnimalAttackIncident
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
import kotlin.test.assertTrue

class AnimalAttackIncidentTest {

    private lateinit var tm: TileManager

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        tm = TileManager()
    }

    private fun baseMapForAnimalAttack(): Triple<Field, Plantation, Plantation> {
        val tiles = mutableMapOf<Int, Tile>()

        // A FOREST at (0,0) – attack origin
        tiles[300] = TileFactory.createForestTile(300, Coordinate(0, 0), null)
        // Add a second FOREST also adjoining the same FIELD to ensure "only once per tile" effect
        tiles[301] = TileFactory.createForestTile(301, Coordinate(0, -2), null)

        // FIELD adjacent to forests
        val field =
            Field(
                10,
                Coordinate(0, 2),
                farm = 1,
                direction = null,
                moistureCapacity = 400,
                possiblePlants = listOf(FieldPlantType.WHEAT)
            )
        field.plant = FieldPlant(FieldPlantType.WHEAT)
        field.currentHarvestEstimate = 1000
        field.plant!!.setProgress(ActionType.SOWING, /*any tick*/ 1, YearTick.OCT1)
        tiles[10] = field

        // GRAPE plantation adjacent (should be -50%)
        val grape = Plantation(
            20,
            Coordinate(2, 0),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.GRAPE)
        )
        grape.currentHarvestEstimate = 1000
        tiles[20] = grape

        // APPLE plantation adjacent (mowing resets this & next tick + -10%)
        val apple = Plantation(
            21,
            Coordinate(-2, 0),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.APPLE)
        )
        apple.currentHarvestEstimate = 1000
        tiles[21] = apple

        tm.setTiles(tiles)
        return Triple(field, grape, apple)
    }

    @Test
    fun `animal attack halves field and grape harvest, apple gets -10pct and mowing reset for this and next tick`() {
        val (field, grape, apple) = baseMapForAnimalAttack()

        val incident = AnimalAttackIncident(
            id = 1,
            tick = 50,
            tileManager = tm,
            location = 300,
            radius = 1
        )

        // Perform at tick 50
        incident.perform(currentTick = 50, currentYearTick = YearTick.MAY1)

        // Apply effects via regular estimate update (one-time per incident per tile)
        // Neutralize environment
        field.sunlightThisTick = field.plant!!.type.sunlightComfort
        field.currentMoisture = field.plant!!.type.moistureRequired
        grape.sunlightThisTick = grape.plant.type.sunlightComfort
        grape.currentMoisture = grape.plant.type.moistureRequired
        apple.sunlightThisTick = apple.plant.type.sunlightComfort
        apple.currentMoisture = apple.plant.type.moistureRequired

        field.updateHarvestEstimate(50, YearTick.MAY1)
        grape.updateHarvestEstimate(50, YearTick.MAY1)
        apple.updateHarvestEstimate(50, YearTick.MAY1)

        // FIELD: “reduced to half”
        assertEquals(500, field.currentHarvestEstimate)

        // GRAPE: also “reduced to half”
        assertEquals(500, grape.currentHarvestEstimate)

        // APPLE: mowing reset for this tick & next + eat 10% -> −10% once
        assertEquals(900, apple.currentHarvestEstimate)
        assertTrue(apple.mowingDoneTicks.contains(50))
        assertTrue(apple.mowingDoneTicks.contains(51))
    }
}
