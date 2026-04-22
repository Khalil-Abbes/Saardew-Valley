package test.kotlin.de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.parser.TileFactory
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.Direction
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TileManagerTest {
    lateinit var tileManager: TileManager

    /**
     * Sets up an extension of grid from figure 9 of the specification.
     */
    @BeforeEach
    fun setup() {
        val idToTile = mutableMapOf(
            // Farmstead (id=12, at -1,-1)
            12 to TileFactory.createFarmsteadTile(
                id = 12,
                coordinate = Coordinate(-1, -1),
                shed = false,
                farm = 1,
                direction = null
            ),
        )

        idToTile.putAll(createRoads())
        idToTile.putAll(createFieldsOriginal())
        idToTile.putAll(createAdditionalFields())
        idToTile.putAll(createMeadowTiles())
        idToTile.putAll(createForestTiles())
        idToTile.putAll(createPlantationTiles())
        idToTile[450] = TileFactory.createVillageTile(450, Coordinate(6, 6))

        tileManager = TileManager()
        tileManager.setTiles(idToTile)
    }

    private fun createRoads(): Map<Int, Tile> {
        return mapOf(
            100 to TileFactory.createRoadTile(100, Coordinate(0, 0), null),
            101 to TileFactory.createRoadTile(101, Coordinate(4, 0), null),
            102 to TileFactory.createRoadTile(102, Coordinate(8, 0), null),
            103 to TileFactory.createRoadTile(103, Coordinate(0, 2), null),
            104 to TileFactory.createRoadTile(104, Coordinate(2, 2), null),
            105 to TileFactory.createRoadTile(105, Coordinate(4, 2), null),
        )
    }

    private fun createFieldsOriginal(): Map<Int, Tile> {
        return mapOf(
            40 to Field(
                40, Coordinate(0, 4), farm = 1, direction = null,
                moistureCapacity = 400, possiblePlants = listOf(FieldPlantType.PUMPKIN)
            ),
            42 to Field(
                42, Coordinate(2, 4), farm = 1, direction = null,
                moistureCapacity = 400, possiblePlants = listOf(FieldPlantType.PUMPKIN)
            ),
            43 to Field(
                43, Coordinate(8, 4), farm = 1, direction = Direction.ANGLE_180,
                moistureCapacity = 400, possiblePlants = listOf(FieldPlantType.PUMPKIN)
            ),
            44 to Field(
                44, Coordinate(4, 4), farm = 1, direction = null,
                moistureCapacity = 400, possiblePlants = listOf(FieldPlantType.PUMPKIN)
            ),
            46 to Field(
                46, Coordinate(6, 4), farm = 1, direction = null,
                moistureCapacity = 400, possiblePlants = listOf(FieldPlantType.PUMPKIN)
            ),
            48 to Field(
                48, Coordinate(6, 2), farm = 1, direction = null,
                moistureCapacity = 400, possiblePlants = listOf(FieldPlantType.PUMPKIN)
            ),
            50 to Field(
                50, Coordinate(8, 2), farm = 1, direction = null,
                moistureCapacity = 400, possiblePlants = listOf(FieldPlantType.PUMPKIN)
            ),
            52 to Field(
                52,
                Coordinate(6, 0),
                farm = 1,
                direction = null,
                moistureCapacity = 400,
                possiblePlants = listOf(FieldPlantType.PUMPKIN) // all allowed
            ),
            54 to Field(
                54, Coordinate(2, 0), farm = 1, direction = null,
                moistureCapacity = 400, possiblePlants = listOf(FieldPlantType.PUMPKIN)
            )
        )
    }

    private fun createAdditionalFields(): Map<Int, Tile> {
        return mapOf( // create additional fields belonging to farm 2
            350 to Field(
                350,
                Coordinate(4, 6),
                farm = 2,
                direction = Direction.ANGLE_0,
                400,
                listOf(FieldPlantType.PUMPKIN)
            ),
            351 to Field(
                351,
                Coordinate(8, 6),
                farm = 2,
                direction = Direction.ANGLE_90,
                400,
                listOf(FieldPlantType.PUMPKIN)
            )
        )
    }

    private fun createMeadowTiles(): Map<Int, Tile> {
        return mutableMapOf(
            200 to TileFactory.createMeadowTile(200, Coordinate(1, -1), null),
            201 to TileFactory.createMeadowTile(201, Coordinate(3, -1), null),
            202 to TileFactory.createMeadowTile(202, Coordinate(5, -1), null),
            203 to TileFactory.createMeadowTile(203, Coordinate(7, -1), null),
            204 to TileFactory.createMeadowTile(204, Coordinate(9, -1), null),

            205 to TileFactory.createMeadowTile(205, Coordinate(-1, 1), null),
            206 to TileFactory.createMeadowTile(206, Coordinate(1, 1), null),
            207 to TileFactory.createMeadowTile(207, Coordinate(3, 1), null),
            208 to TileFactory.createMeadowTile(208, Coordinate(5, 1), Direction.ANGLE_45),
            209 to TileFactory.createMeadowTile(209, Coordinate(7, 1), null),
            210 to TileFactory.createMeadowTile(210, Coordinate(9, 1), null),

            211 to TileFactory.createMeadowTile(211, Coordinate(-1, 3), null),
            212 to TileFactory.createMeadowTile(212, Coordinate(1, 3), null),
            213 to TileFactory.createMeadowTile(213, Coordinate(3, 3), null),
            214 to TileFactory.createMeadowTile(214, Coordinate(5, 3), null),
            215 to TileFactory.createMeadowTile(215, Coordinate(7, 3), null),
            216 to TileFactory.createMeadowTile(216, Coordinate(9, 3), null),

            217 to TileFactory.createMeadowTile(217, Coordinate(-1, 5), null),
            218 to TileFactory.createMeadowTile(218, Coordinate(1, 5), null),
            219 to TileFactory.createMeadowTile(219, Coordinate(3, 5), null),
            220 to TileFactory.createMeadowTile(220, Coordinate(5, 5), null),
            221 to TileFactory.createMeadowTile(221, Coordinate(7, 5), null),
            222 to TileFactory.createMeadowTile(222, Coordinate(9, 5), null)
        )
    }

    private fun createForestTiles(): Map<Int, Tile> {
        return mapOf(
            501 to TileFactory.createForestTile(501, Coordinate(-1, 7), null),
            502 to TileFactory.createForestTile(502, Coordinate(0, 6), null),
            503 to TileFactory.createForestTile(503, Coordinate(1, 7), null),
            504 to TileFactory.createForestTile(504, Coordinate(2, 6), null),
            505 to TileFactory.createForestTile(505, Coordinate(3, 7), null),
            506 to TileFactory.createForestTile(506, Coordinate(8, 8), null),
        )
    }

    private fun createPlantationTiles(): Map<Int, Tile> {
        return mapOf(
            601 to Plantation(
                601, Coordinate(8, 10), 2, Direction.ANGLE_315, 300,
                PlantationPlant(
                    PlantationPlantType.APPLE
                )
            )
        )
    }

    @Test
    fun `Get neighbor in direction when there is no direction`() {
        val field42 = tileManager.getTileById(42)
        assertNotNull(field42)
        assertNull(tileManager.getNeighborInDirection(field42))
    }

    @Test
    fun `Get neighbor of a square tile in direction ANGLE_45`() {
        val meadow = tileManager.getTileByCoordinate(Coordinate(5, 1))
        assertNotNull(meadow)
        assertEquals(52, tileManager.getNeighborInDirection(meadow)?.id)
    }

    @Test
    fun `Get neighbor of a octagonal tile in direction ANGLE_180 where there is no specified tile`() {
        val field351 = tileManager.getTileByCoordinate(Coordinate(8, 6))
        assertNotNull(field351)
        assertNull(tileManager.getNeighborInDirection(field351)?.id)
    }

    @Test
    fun `Get all tiles within the radius 1 of a square tile`() {
        val centerTile = tileManager.getTileByCoordinate(Coordinate(3, 3))
        assertNotNull(centerTile)
        val tilesInRadius = tileManager.getAllNeighborTilesInRadius(centerTile.id, 1)
        assertEquals(5, tilesInRadius.size)
        assertContains(tilesInRadius.map { it.id }, centerTile.id)
        assertContains(tilesInRadius.map { it.id }, 104)
        assertContains(tilesInRadius.map { it.id }, 105)
        assertContains(tilesInRadius.map { it.id }, 44)
        assertContains(tilesInRadius.map { it.id }, 42)
    }

    @Test
    fun getFarmFieldsInRadiusTest() {
        val meadow = tileManager.getTileByCoordinate(Coordinate(5, 3))
        assertNotNull(meadow)
        val fields = tileManager.getFarmFieldsInRadius(1, meadow, 2).map { it.id }.toSet()
        val expectedFields = setOf(44, 46, 48, 42, 43, 50, 52)
        assertEquals(expectedFields, fields)
    }

    @Test
    fun `getFarmFieldsInRadiusTest with center field in radius 1`() {
        val field54 = tileManager.getTileById(54)
        assertNotNull(field54)
        val fields = tileManager.getFarmFieldsInRadius(1, field54, 1).map { it.id }.toSet()
        val expectedFields = setOf(54)
        assertEquals(expectedFields, fields)
    }

    @Test
    fun `getFarmPlantationsInRadius on a grid with no plantations with huge radius`() {
        val meadow = tileManager.getTileByCoordinate(Coordinate(5, 3))
        assertNotNull(meadow)
        val plantations = tileManager.getFarmPlantationsInRadius(1, meadow, 200)
        assertEquals(0, plantations.size)
    }

    @Test
    fun `Update tile with correct village`() {
        val fieldToBeReplaced = tileManager.getTileById(42)
        assertNotNull(fieldToBeReplaced)
        val newVillage =
            TileFactory.createVillageTile(fieldToBeReplaced.id, fieldToBeReplaced.coordinate)
        tileManager.updateTile(newVillage)
        val retrievedTile = tileManager.getTileById(42)
        assertEquals(newVillage, retrievedTile)
    }

    @Test
    fun `Shortest path length between two fields without harvest`() {
        val start = tileManager.getTileById(54)
        assertNotNull(start)
        val target = tileManager.getTileById(50)
        assertNotNull(target)
        val pathLength =
            tileManager.shortestPathLength(start, target, farmID = start.farm, hasHarvest = false)
        assertNotNull(pathLength)
        assertTrue(tileManager.existsPath(start, target, farmID = start.farm, hasHarvest = false))
        assertEquals(4, pathLength)
    }

    @Test
    fun `Shortest path length between two fields with village in between and no harvest`() {
        val start = tileManager.getTileById(350)
        assertNotNull(start)
        val target = tileManager.getTileById(351)
        assertNotNull(target)
        val pathLength =
            tileManager.shortestPathLength(start, target, farmID = start.farm, hasHarvest = false)
        assertNotNull(pathLength)
        assertEquals(2, pathLength)
        assertTrue(tileManager.existsPath(start, target, farmID = start.farm, hasHarvest = false))
    }

    @Test
    fun `Shortest path length between two fields with village in between and harvest`() {
        val start = tileManager.getTileById(350)
        assertNotNull(start)
        val target = tileManager.getTileById(351)
        assertNotNull(target)
        val pathLength =
            tileManager.shortestPathLength(start, target, farmID = start.farm, hasHarvest = true)
        assertNull(pathLength)
        assertFalse(tileManager.existsPath(start, target, farmID = start.farm, hasHarvest = true))
    }

    @Test
    fun `Shortest path length between field and plantation  with forest in between and no harvest`() {
        val start = tileManager.getTileById(351)
        assertNotNull(start)
        val target = tileManager.getTileById(601)
        assertNotNull(target)
        val pathLength =
            tileManager.shortestPathLength(start, target, farmID = start.farm, hasHarvest = false)
        assertNull(pathLength)
        assertFalse(tileManager.existsPath(start, target, farmID = start.farm, hasHarvest = false))
    }
}
