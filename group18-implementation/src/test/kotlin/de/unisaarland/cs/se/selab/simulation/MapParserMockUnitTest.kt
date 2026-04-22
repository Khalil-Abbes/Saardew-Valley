package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.parser.MapParser
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

class MapParserMockUnitTest {

    private lateinit var mockTileManager: TileManager
    private lateinit var parser: MapParser

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        mockTileManager = Mockito.mock(TileManager::class.java)
        parser = MapParser(mockTileManager)

        // Default mock behavior: return empty list for all tiles (no neighbors)
        val mockTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(Mockito.anyInt(), Mockito.eq(1)))
            .thenReturn(listOf(mockTile))
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

    // MOCK INTEGRATION TESTS

    @Test
    fun `parseMap should return true with mocked tileManager for valid farmstead`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") {
                put("farm", 0)
                put("shed", false)
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true with mocked tileManager for valid field with plants`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") {
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("WHEAT", "POTATO")))
                put("airflow", true)
                put("direction", "90")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for field with invalid plant types`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") {
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("APPLE")))
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true with mocked tileManager for valid plantation`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "PLANTATION") {
                put("farm", 1)
                put("capacity", 200)
                put("plant", "APPLE")
                put("airflow", true)
                put("direction", "180")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for plantation with invalid plant type`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "PLANTATION") {
                put("farm", 1)
                put("capacity", 100)
                put("plant", "WHEAT")
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for square tile with diagonal direction`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "MEADOW") {
                put("airflow", true)
                put("direction", "45")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for square tile with non-diagonal direction`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "MEADOW") {
                put("airflow", true)
                put("direction", "0")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for octagonal tile with any valid direction`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "ROAD") {
                put("airflow", true)
                put("direction", "0")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for invalid direction value`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "ROAD") {
                put("airflow", true)
                put("direction", "123")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for tile without direction when airflow is false`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") {
                put("farm", 0)
                put("shed", false)
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for farmstead with invalid farm value`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") {
                put("farm", -1)
                put("shed", false)
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for field with zero capacity`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") {
                put("farm", 1)
                put("capacity", 0)
                put("possiblePlants", JSONArray(listOf("WHEAT")))
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for plantation with null plant`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "PLANTATION") {
                put("farm", 1)
                put("capacity", 100)
                put("airflow", false)
                // Missing plant field
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for mixed coordinate types in different tiles`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE"),
            createTileJson(2, 0, 0, "FIELD") {
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("WHEAT")))
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for farmstead on octagonal coordinates`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FARMSTEAD") {
                put("farm", 0)
                put("shed", false)
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for field on square coordinates`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FIELD") {
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("WHEAT")))
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for comprehensive valid map with mocked neighbors`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE"),
            createTileJson(2, 0, 0, "FIELD") {
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("WHEAT", "POTATO")))
                put("airflow", false)
            },
            createTileJson(3, 3, 3, "FARMSTEAD") {
                put("farm", 0)
                put("shed", true)
                put("airflow", false)
            },
            createTileJson(4, 2, 4, "PLANTATION") {
                put("farm", 1)
                put("capacity", 200)
                put("plant", "APPLE")
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }
}
