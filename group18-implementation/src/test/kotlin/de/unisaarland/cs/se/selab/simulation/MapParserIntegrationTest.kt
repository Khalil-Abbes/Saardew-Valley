package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.parser.MapParser
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class MapParserIntegrationTest {

    private lateinit var tileManager: TileManager
    private lateinit var parser: MapParser

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        tileManager = TileManager()
        parser = MapParser(tileManager)
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

    // VALID TESTS (Should return TRUE)

    @Test
    fun `parseMap should return true for valid field tile with octagonal coordinates`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") { // Octagonal coordinates (both even)
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("WHEAT", "POTATO")))
                put("airflow", true)
                put("direction", "90") // Valid for octagonal tiles
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for valid plantation tile with octagonal coordinates`() {
        val tiles = listOf(
            createTileJson(1, 2, 4, "PLANTATION") { // Octagonal coordinates (both even)
                put("farm", 2)
                put("capacity", 200)
                put("plant", "APPLE") // Plantation plant
                put("airflow", true)
                put("direction", "180")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for valid village tile with no extra properties`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE") // Village tiles cannot have airflow/direction/etc
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for valid meadow tile with square coordinates`() {
        val tiles = listOf(
            createTileJson(1, 5, 5, "MEADOW") { // Square coordinates only
                put("airflow", true)
                put("direction", "225") // Diagonal direction for square tiles
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for valid road tile with any coordinates`() {
        val tiles = listOf(
            createTileJson(1, 4, 0, "ROAD") { // Roads can be any coordinate type
                put("airflow", true)
                put("direction", "45")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for valid forest tile with any coordinates`() {
        val tiles = listOf(
            createTileJson(1, 7, 7, "FOREST") { // Forests can be any coordinate type
                put("airflow", true)
                put("direction", "315") // Diagonal for square coordinates
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for tiles without airflow`() {
        val tiles = listOf(
            createTileJson(1, 3, 3, "MEADOW") {
                put("airflow", false)
                // No direction needed when airflow is false
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    // INVALID TESTS (Should return FALSE)

    @Test
    fun `parseMap should return false for empty tile array`() {
        val tiles = emptyList<JSONObject>()

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile)) // Schema requires minItems: 1
    }

    @Test
    fun `parseMap should return false for farmstead tile with octagonal coordinates`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FARMSTEAD") { // Invalid: octagonal coordinates for farmstead
                put("farm", 0)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for field tile with square coordinates`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FIELD") { // Invalid: square coordinates for field
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("WHEAT")))
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for plantation tile with square coordinates`() {
        val tiles = listOf(
            createTileJson(1, 3, 3, "PLANTATION") { // Invalid: square coordinates for plantation
                put("farm", 1)
                put("capacity", 100)
                put("plant", "APPLE")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for meadow tile with octagonal coordinates`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "MEADOW") { // Invalid: octagonal coordinates for meadow
                put("airflow", true)
                put("direction", "45")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for mixed coordinates on single tile`() {
        val tiles = listOf(
            createTileJson(1, 0, 1, "FARMSTEAD") { // Mixed coordinates (even x, odd y) - invalid
                put("farm", 0)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for farmstead tile with negative farm`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") {
                put("farm", -1) // Invalid negative farm
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for field tile with zero capacity`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") {
                put("farm", 1)
                put("capacity", 0) // Invalid zero capacity
                put("possiblePlants", JSONArray(listOf("WHEAT")))
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for field tile with plantation plant`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") {
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("APPLE"))) // Invalid: plantation plant in field
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for plantation tile with field plant`() {
        val tiles = listOf(
            createTileJson(1, 2, 4, "PLANTATION") {
                put("farm", 2)
                put("capacity", 200)
                put("plant", "WHEAT") // Invalid: field plant in plantation
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for square tile with non-diagonal direction`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") { // Square coordinates
                put("farm", 0)
                put("airflow", true)
                put("direction", "0") // Non-diagonal direction for square tile - invalid
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for village tile with airflow property`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE") {
                put("airflow", true) // Invalid: villages cannot have airflow
                put("direction", "45")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for duplicate tile IDs`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE"),
            createTileJson(1, 3, 3, "ROAD") // Duplicate ID
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for duplicate coordinates`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE"),
            createTileJson(2, 1, 1, "ROAD") // Duplicate coordinates
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for invalid tile category`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "INVALID_CATEGORY")
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }
}
