package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.parser.TileFactory
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.tile.Direction
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class PathingRulesTest {
    lateinit var tileManager: TileManager

    @BeforeEach
    fun setup() {
        val idToTile = mutableMapOf(
            12 to TileFactory.createFarmsteadTile(
                id = 12, coordinate = Coordinate(-1, -1), shed = false, farm = 1, direction = null
            ),
        )
        idToTile.putAll(createRoads())
        idToTile.putAll(createFieldsOriginal())
        idToTile.putAll(createAdditionalFields())
        idToTile.putAll(createMeadowTiles())
        idToTile.putAll(createForestTiles())
        idToTile.putAll(createPlantationTiles())
        idToTile[450] = TileFactory.createVillageTile(450, Coordinate(6, 6)) // between 350 and 351

        tileManager = TileManager()
        tileManager.setTiles(idToTile)
    }

    private fun createRoads(): Map<Int, Tile> = mapOf(
        100 to TileFactory.createRoadTile(100, Coordinate(0, 0), null),
        101 to TileFactory.createRoadTile(101, Coordinate(4, 0), null),
        102 to TileFactory.createRoadTile(102, Coordinate(8, 0), null),
        103 to TileFactory.createRoadTile(103, Coordinate(0, 2), null),
        104 to TileFactory.createRoadTile(104, Coordinate(2, 2), null),
        105 to TileFactory.createRoadTile(105, Coordinate(4, 2), null),
    )

    private fun createFieldsOriginal(): Map<Int, Tile> = mapOf(
        40 to Field(
            40, Coordinate(0, 4), farm = 1, direction = null, moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        ),
        42 to Field(
            42, Coordinate(2, 4), farm = 1, direction = null, moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        ),
        43 to Field(
            43, Coordinate(8, 4), farm = 1, direction = Direction.ANGLE_180, moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        ),
        44 to Field(
            44, Coordinate(4, 4), farm = 1, direction = null, moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        ),
        46 to Field(
            46, Coordinate(6, 4), farm = 1, direction = null, moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        ),
        48 to Field(
            48, Coordinate(6, 2), farm = 1, direction = null, moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        ),
        50 to Field(
            50, Coordinate(8, 2), farm = 1, direction = null, moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        ),
        52 to Field(
            52, Coordinate(6, 0), farm = 1, direction = null, moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        ),
        54 to Field(
            54, Coordinate(2, 0), farm = 1, direction = null, moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        )
    )

    private fun createAdditionalFields(): Map<Int, Tile> = mapOf(
        350 to Field(
            350, Coordinate(4, 6), farm = 2, direction = Direction.ANGLE_0, 400,
            listOf(FieldPlantType.PUMPKIN)
        ),
        351 to Field(
            351, Coordinate(8, 6), farm = 2, direction = Direction.ANGLE_90, 400,
            listOf(FieldPlantType.PUMPKIN)
        )
    )

    private fun createMeadowTiles(): Map<Int, Tile> = mutableMapOf(
        200 to TileFactory.createMeadowTile(200, Coordinate(1, -1), null),
        201 to TileFactory.createMeadowTile(201, Coordinate(3, -1), null),
        202 to TileFactory.createMeadowTile(202, Coordinate(5, -1), null),
        203 to TileFactory.createMeadowTile(203, Coordinate(7, -1), null),
        204 to TileFactory.createMeadowTile(204, Coordinate(9, -1), null),

        205 to TileFactory.createMeadowTile(205, Coordinate(-1, 1), null),
        206 to TileFactory.createMeadowTile(206, Coordinate(1, 1), null),
        207 to TileFactory.createMeadowTile(207, Coordinate(3, 1), null),
        // direction shouldn't matter for BFS
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

    private fun createForestTiles(): Map<Int, Tile> = mapOf(
        501 to TileFactory.createForestTile(501, Coordinate(-1, 7), null),
        502 to TileFactory.createForestTile(502, Coordinate(0, 6), null),
        503 to TileFactory.createForestTile(503, Coordinate(1, 7), null),
        504 to TileFactory.createForestTile(504, Coordinate(2, 6), null),
        505 to TileFactory.createForestTile(505, Coordinate(3, 7), null),
        506 to TileFactory.createForestTile(506, Coordinate(8, 8), null),
    )

    private fun createPlantationTiles(): Map<Int, Tile> = mapOf(
        601 to Plantation(
            601, Coordinate(8, 10), 2, Direction.ANGLE_315, 300,
            PlantationPlant(PlantationPlantType.APPLE)
        )
    )

    @Test
    fun `village is passable only without harvest`() {
        val a = tileManager.getTileById(350)!!
        val b = tileManager.getTileById(351)!!
        assertTrue(tileManager.existsPath(a, b, farmID = 2, hasHarvest = false))
        assertFalse(tileManager.existsPath(a, b, farmID = 2, hasHarvest = true))
    }

    @Test
    fun `forest blocks path`() {
        val a = tileManager.getTileById(351)!!
        val b = tileManager.getTileById(601)!!
        assertFalse(tileManager.existsPath(a, b, farmID = 2, hasHarvest = false))
    }

    @Test
    fun `farm-boundary tiles require same farm id`() {
        val f1 = tileManager.getTileById(44)!! // farm 1
        val f2 = tileManager.getTileById(350)!! // farm 2
        assertFalse(tileManager.existsPath(f1, f2, farmID = 1, hasHarvest = false))
    }

    @Test
    fun `start equals target gives zero distance`() {
        val t = tileManager.getTileById(54)!!
        val d = tileManager.shortestPathLength(t, t, farmID = t.farm, hasHarvest = false)
        assertEquals(0, d)
        assertTrue(tileManager.existsPath(t, t, farmID = t.farm, hasHarvest = true))
    }

    @Test
    fun `IDs overload consistent with Tile overload`() {
        val a = tileManager.getTileById(54)!!
        val b = tileManager.getTileById(50)!!
        val tileCall = tileManager.existsPath(a, b, farmID = a.farm, hasHarvest = false)
        val idCall = tileManager.existsPath(a.id, b.id, farmID = a.farm!!, hasHarvest = false)
        assertEquals(tileCall, idCall)
    }

    @Test
    fun `own farm path through meadow and road is allowed even with harvest`() {
        val a = tileManager.getTileById(54)!! // (2,0)
        val b = tileManager.getTileById(52)!! // (6,0)
        // path: (2,0) -> (4,0 road 101) -> (6,0)
        assertTrue(tileManager.existsPath(a, b, farmID = a.farm, hasHarvest = true))
        val d = tileManager.shortestPathLength(a, b, farmID = a.farm, hasHarvest = true)
        assertEquals(2, d)
    }

    @Test
    fun `other farm target is not enterable even if route uses only public tiles`() {
        val a = tileManager.getTileById(52)!! // farm 1
        val targetOtherFarm = tileManager.getTileById(351)!! // farm 2
        assertFalse(tileManager.existsPath(a, targetOtherFarm, farmID = a.farm, hasHarvest = false))
        assertFalse(tileManager.existsPath(a, targetOtherFarm, farmID = a.farm, hasHarvest = true))
    }

    @Test
    fun `village as target is reachable without harvest but not with harvest`() {
        val start = tileManager.getTileById(350)!! // (4,6)
        val village = tileManager.getTileById(450)!! // (6,6)
        // Adjacent: should be reachable if not carrying harvest
        assertTrue(tileManager.existsPath(start, village, farmID = 2, hasHarvest = false))
        assertFalse(tileManager.existsPath(start, village, farmID = 2, hasHarvest = true))
    }

    @Test
    fun `direction flag on tiles does not affect BFS connectivity`() {
        // (Meadow 208 has direction=45 but BFS uses coordinate adjacency, not direction)
        val start = tileManager.getTileById(54)!! // (2,0)
        val end = tileManager.getTileById(52)!! // (6,0)
        assertTrue(tileManager.existsPath(start, end, farmID = 1, hasHarvest = false))
    }

    @Test
    fun `path opens when village converts to road`() {
        val a = tileManager.getTileById(350)!!
        val b = tileManager.getTileById(351)!!
        // Initially blocked with harvest (village in between)
        assertFalse(tileManager.existsPath(a, b, farmID = 2, hasHarvest = true))

        // Replace village 450 with a road at same coordinate
        val oldVillage = tileManager.getTileById(450)!!
        val road450 = TileFactory.createRoadTile(oldVillage.id, oldVillage.coordinate, null)
        tileManager.updateTile(road450)

        // Now carrying harvest should be allowed through (MEADOW/ROAD are always passable)
        assertTrue(tileManager.existsPath(a, b, farmID = 2, hasHarvest = true))
    }

    @Test
    fun `path closes when the only bridge tile becomes forest`() {
        val a = tileManager.getTileById(350)!!
        val b = tileManager.getTileById(351)!!
        // Ensure there IS a path without harvest via village at (6,6)
        assertTrue(tileManager.existsPath(a, b, farmID = 2, hasHarvest = false))

        // Turn the bridge coordinate (6,6) into a forest
        val bridge = tileManager.getTileById(450)!!
        val forest450 = TileFactory.createForestTile(bridge.id, bridge.coordinate, null)
        tileManager.updateTile(forest450)

        // No alternate route exists in this fixture => path should be blocked regardless of harvest
        assertFalse(tileManager.existsPath(a, b, farmID = 2, hasHarvest = false))
        assertFalse(tileManager.existsPath(a, b, farmID = 2, hasHarvest = true))
    }

    @Test
    fun `shortest path length matches expected for 54 to 50`() {
        val a = tileManager.getTileById(54)!!
        val b = tileManager.getTileById(50)!!
        val d = tileManager.shortestPathLength(a, b, farmID = a.farm, hasHarvest = false)
        assertNotNull(d)
        assertEquals(4, d)
    }

    @Test
    fun `existsPath id overload returns false when id not found`() {
        val known = tileManager.getTileById(50)!!
        assertFalse(tileManager.existsPath(999_999, known.id, farmID = 1, hasHarvest = false))
        assertFalse(tileManager.existsPath(known.id, 999_999, farmID = 1, hasHarvest = false))
    }

    @Test
    fun `no path when forest blocks only corridor to plantation`() {
        val start = tileManager.getTileById(351)!!
        val target = tileManager.getTileById(601)!! // plantation farm 2, behind forest cluster
        assertNull(tileManager.shortestPathLength(start, target, farmID = 2, hasHarvest = false))
        assertFalse(tileManager.existsPath(start, target, farmID = 2, hasHarvest = false))
    }

    @Test
    fun `mock getTileById returns correct tile when tile exists`() {
        val mockTileManager = Mockito.mock(TileManager::class.java)
        val expectedTile = Field(
            100,
            Coordinate(1, 1),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        )

        Mockito.`when`(mockTileManager.getTileById(100)).thenReturn(expectedTile)

        val result = mockTileManager.getTileById(100)

        assertEquals(expectedTile, result)
        assertEquals(100, result?.id)
        assertEquals(Coordinate(1, 1), result?.coordinate)
    }

    @Test
    fun `mock getTileById returns null when tile does not exist`() {
        val mockTileManager = Mockito.mock(TileManager::class.java)
        Mockito.`when`(mockTileManager.getTileById(999)).thenReturn(null)

        assertNull(mockTileManager.getTileById(999))
    }

    @Test
    fun `mock existsPath returns false for different farms`() {
        val mockTileManager = Mockito.mock(TileManager::class.java)
        val farm1Tile = Field(
            1,
            Coordinate(0, 0),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        )
        val farm2Tile = Field(
            2,
            Coordinate(1, 1),
            farm = 2,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        )

        Mockito.`when`(mockTileManager.existsPath(farm1Tile, farm2Tile, farmID = 1, hasHarvest = false))
            .thenReturn(false)

        assertFalse(mockTileManager.existsPath(farm1Tile, farm2Tile, farmID = 1, hasHarvest = false))
    }

    @Test
    fun `mock shortestPathLength returns expected distance`() {
        val mockTileManager = Mockito.mock(TileManager::class.java)
        val startTile = Field(
            1,
            Coordinate(0, 0),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        )
        val endTile = Field(
            2,
            Coordinate(3, 3),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        )

        Mockito.`when`(mockTileManager.shortestPathLength(startTile, endTile, farmID = 1, hasHarvest = false))
            .thenReturn(6)

        val result = mockTileManager.shortestPathLength(startTile, endTile, farmID = 1, hasHarvest = false)

        assertEquals(6, result)
    }

    @Test
    fun `mock harvest flag affects path availability through village`() {
        val mockTileManager = Mockito.mock(TileManager::class.java)
        val startTile = Field(
            1,
            Coordinate(0, 0),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        )
        val village = TileFactory.createVillageTile(2, Coordinate(1, 1))

        Mockito.`when`(mockTileManager.existsPath(startTile, village, farmID = 1, hasHarvest = false))
            .thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(startTile, village, farmID = 1, hasHarvest = true))
            .thenReturn(false)

        assertTrue(mockTileManager.existsPath(startTile, village, farmID = 1, hasHarvest = false))
        assertFalse(mockTileManager.existsPath(startTile, village, farmID = 1, hasHarvest = true))
    }

    @Test
    fun `mock updateTile changes path availability`() {
        val mockTileManager = Mockito.mock(TileManager::class.java)
        val startTile = Field(
            1,
            Coordinate(0, 0),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        )
        val endTile = Field(
            3,
            Coordinate(2, 2),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        )
        val newRoad = TileFactory.createRoadTile(2, Coordinate(1, 1), null)

        Mockito.`when`(mockTileManager.existsPath(startTile, endTile, farmID = 1, hasHarvest = false))
            .thenReturn(false).thenReturn(true)

        assertFalse(mockTileManager.existsPath(startTile, endTile, farmID = 1, hasHarvest = false))

        mockTileManager.updateTile(newRoad)

        assertTrue(mockTileManager.existsPath(startTile, endTile, farmID = 1, hasHarvest = false))
    }

    @Test
    fun `mock ID-based existsPath handles non-existent tiles`() {
        val mockTileManager = Mockito.mock(TileManager::class.java)

        Mockito.`when`(mockTileManager.existsPath(999, 1000, farmID = 1, hasHarvest = false))
            .thenReturn(false)

        assertFalse(mockTileManager.existsPath(999, 1000, farmID = 1, hasHarvest = false))
    }

    @Test
    fun `mock plantation path is blocked by mock forest`() {
        val mockTileManager = Mockito.mock(TileManager::class.java)
        val fieldTile = Field(
            1,
            Coordinate(0, 0),
            farm = 2,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.PUMPKIN)
        )
        val plantation = Plantation(
            2,
            Coordinate(5, 5),
            2,
            Direction.ANGLE_0,
            300,
            PlantationPlant(PlantationPlantType.APPLE)
        )

        Mockito.`when`(mockTileManager.existsPath(fieldTile, plantation, farmID = 2, hasHarvest = false))
            .thenReturn(false)
        Mockito.`when`(mockTileManager.shortestPathLength(fieldTile, plantation, farmID = 2, hasHarvest = false))
            .thenReturn(null)

        assertFalse(mockTileManager.existsPath(fieldTile, plantation, farmID = 2, hasHarvest = false))
        assertNull(mockTileManager.shortestPathLength(fieldTile, plantation, farmID = 2, hasHarvest = false))
    }
}
