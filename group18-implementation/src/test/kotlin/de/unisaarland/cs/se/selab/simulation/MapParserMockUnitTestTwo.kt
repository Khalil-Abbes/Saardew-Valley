package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.parser.MapParser
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.mockito.Mockito
import java.io.File
import java.nio.file.Path

class MapParserMockUnitTestTwo {

    private lateinit var mockTileManager: TileManager
    private lateinit var parser: MapParser

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        mockTileManager = Mockito.mock(TileManager::class.java)
        parser = MapParser(mockTileManager)
    }

    // HELPER METHODS

    private fun createTileJson(
        id: Int,
        x: Int,
        y: Int,
        category: String,
        additionalProps: JSONObject.() -> Unit = {}
    ) = JSONObject().apply {
        put("id", id)
        put(
            "coordinates",
            JSONObject().apply {
                put("x", x)
                put("y", y)
            }
        )
        put("category", category)
        additionalProps()
    }

    private fun createMapJson(tiles: List<JSONObject>) = JSONObject().apply {
        put(
            "tiles",
            JSONArray().apply {
                tiles.forEach { put(it) }
            }
        )
    }

    private fun createTempMapFile(content: String): String {
        val tempFile = File(tempDir.toFile(), "test_map.json")
        tempFile.writeText(content)
        return tempFile.absolutePath
    }

    private fun setupMockNeighborsForSuccess(
        tileId: Int,
        neighborCategories: List<TileCategory> = listOf(TileCategory.ROAD)
    ) {
        val mockTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        val mockNeighbors = neighborCategories.map { category ->
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(category)
            }
        }
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(tileId, 1))
            .thenReturn(listOf(mockTile) + mockNeighbors)
    }

    private fun setupMockNeighborsForFailure(
        tileId: Int,
        mainCategory: TileCategory,
        conflictingNeighborCategory: TileCategory
    ) {
        val mockTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        val conflictingNeighbor = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        val normalNeighbor = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)

        Mockito.`when`(mockTile.categoryType).thenReturn(mainCategory)
        Mockito.`when`(conflictingNeighbor.categoryType).thenReturn(conflictingNeighborCategory)
        Mockito.`when`(normalNeighbor.categoryType).thenReturn(TileCategory.ROAD)

        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(tileId, 1))
            .thenReturn(listOf(mockTile, conflictingNeighbor, normalNeighbor))
    }

    // NEIGHBOR VALIDATION TESTS

    @Test
    fun `parseMap should return true for farmstead with valid neighbors`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") {
                put("farm", 1)
                put("shed", false) // Required shed property
                put("airflow", false) // Required airflow property
            }
        )

        setupMockNeighborsForSuccess(1, listOf(TileCategory.ROAD, TileCategory.VILLAGE))

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for forest with valid neighbors`() {
        val tiles = listOf(
            createTileJson(1, 5, 5, "FOREST") {
                put("airflow", true) // Required airflow property
                put("direction", "225")
            }
        )

        setupMockNeighborsForSuccess(1, listOf(TileCategory.ROAD, TileCategory.MEADOW, TileCategory.FIELD))

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for forest with village neighbor`() {
        val tiles = listOf(
            createTileJson(1, 5, 5, "FOREST") {
                put("airflow", false) // Required airflow property
            }
        )

        setupMockNeighborsForFailure(1, TileCategory.FOREST, TileCategory.VILLAGE)

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for village with valid neighbors`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE") // Village cannot have airflow property
        )

        setupMockNeighborsForSuccess(1, listOf(TileCategory.ROAD, TileCategory.MEADOW, TileCategory.FARMSTEAD))

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for village with forest neighbor`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE") // Village cannot have airflow property
        )

        setupMockNeighborsForFailure(1, TileCategory.VILLAGE, TileCategory.FOREST)

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for isolated tile with no neighbors`() {
        val tiles = listOf(
            createTileJson(1, 4, 0, "ROAD") {
                put("airflow", true) // Required airflow property
                put("direction", "90")
            }
        )

        // Setup mock to return only the tile itself (no neighbors)
        val mockTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(1, 1))
            .thenReturn(listOf(mockTile))

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    // TILE CREATION TESTS

    @Test
    fun `parseMap should return true for village tile creation`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE") // Village cannot have airflow property
        )

        setupMockNeighborsForSuccess(1)

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for field tile with direction`() {
        val tiles = listOf(
            createTileJson(5, 0, 0, "FIELD") {
                put("farm", 2)
                put("capacity", 150)
                put("possiblePlants", JSONArray(listOf("WHEAT", "POTATO")))
                put("airflow", true) // Required airflow property
                put("direction", "90")
            }
        )

        setupMockNeighborsForSuccess(5)

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for farmstead with shed`() {
        val tiles = listOf(
            createTileJson(7, -1, -1, "FARMSTEAD") {
                put("farm", 3)
                put("shed", true) // Required shed property
                put("airflow", true) // Required airflow property
                put("direction", "135")
            }
        )

        setupMockNeighborsForSuccess(7)

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for plantation with plant types`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "PLANTATION") {
                put("farm", 1)
                put("capacity", 100)
                put("plant", "APPLE")
                put("airflow", true) // Required airflow property
                put("direction", "90")
            }
        )

        setupMockNeighborsForSuccess(1)

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for meadow tile`() {
        val tiles = listOf(
            createTileJson(9, 3, 3, "MEADOW") {
                put("airflow", true) // Required airflow property
                put("direction", "225")
            }
        )

        setupMockNeighborsForSuccess(9)

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for road tile`() {
        val tiles = listOf(
            createTileJson(10, 2, 4, "ROAD") {
                put("airflow", true) // Required airflow property
                put("direction", "0")
            }
        )

        setupMockNeighborsForSuccess(10)

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for forest tile`() {
        val tiles = listOf(
            createTileJson(11, 1, 3, "FOREST") {
                put("airflow", true) // Required airflow property
                put("direction", "315")
            }
        )

        setupMockNeighborsForSuccess(11)

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    // ADVANCED NEIGHBOR VALIDATION TESTS

    @Test
    fun `parseMap should return false for farmstead with meadow neighbor`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") {
                put("farm", 0)
                put("shed", false) // Required shed property
                put("airflow", false) // Required airflow property
            }
        )

        setupMockNeighborsForFailure(1, TileCategory.FARMSTEAD, TileCategory.MEADOW)

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for farmstead with different farm field neighbor`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") {
                put("farm", 1)
                put("shed", false) // Required shed property
                put("airflow", false) // Required airflow property
            }
        )

        // Setup specific mock for farm validation failure
        val mockFarmsteadTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        val mockFieldNeighbor = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        val mockRoadNeighbor = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)

        Mockito.`when`(mockFarmsteadTile.categoryType).thenReturn(TileCategory.FARMSTEAD)
        Mockito.`when`(mockFarmsteadTile.farm).thenReturn(1)
        Mockito.`when`(mockFieldNeighbor.categoryType).thenReturn(TileCategory.FIELD)
        Mockito.`when`(mockFieldNeighbor.farm).thenReturn(2) // Different farm
        Mockito.`when`(mockRoadNeighbor.categoryType).thenReturn(TileCategory.ROAD)

        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(1, 1))
            .thenReturn(listOf(mockFarmsteadTile, mockFieldNeighbor, mockRoadNeighbor))

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for comprehensive map with different tile categories`() {
        val tiles = listOf(
            createTileJson(100, 1, 1, "VILLAGE"), // Village cannot have airflow
            createTileJson(101, 0, 0, "ROAD") {
                put("airflow", true) // Required airflow property
                put("direction", "90")
            },
            createTileJson(102, 5, 5, "FOREST") {
                put("airflow", true) // Required airflow property
                put("direction", "45")
            },
            createTileJson(103, 3, 3, "FARMSTEAD") {
                put("farm", 2)
                put("shed", true) // Required shed property
                put("airflow", false) // Required airflow property
            },
            createTileJson(104, 2, 4, "FIELD") {
                put("farm", 1)
                put("capacity", 150)
                put("possiblePlants", JSONArray(listOf("WHEAT", "OAT")))
                put("airflow", false) // Required airflow property
            },
            createTileJson(105, 0, 2, "PLANTATION") {
                put("farm", 3)
                put("capacity", 100)
                put("plant", "CHERRY")
                put("airflow", true) // Required airflow property
                put("direction", "270")
            },
            createTileJson(106, 7, 7, "MEADOW") {
                put("airflow", true) // Required airflow property
                put("direction", "135")
            }
        )
        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for invalid tile creation due to coordinate mismatch`() {
        val tiles = listOf(
            createTileJson(1, 0, 1, "FARMSTEAD") { // Mixed coordinates - invalid for farmstead
                put("farm", 0)
                put("shed", false) // Required shed property
                put("airflow", false) // Required airflow property
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }
}
