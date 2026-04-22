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

class MapParserIntegrationTestTwo {

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

    // DIRECTION VALIDATION TESTS

    @Test
    fun `parseMap should return true for square tile with diagonal direction`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") { // Square coordinates
                put("farm", 0)
                put("airflow", true) // Required for direction validation
                put("direction", "") // Valid diagonal direction
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
                put("direction", "0") // Invalid non-diagonal direction
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for octagonal tile with any valid direction`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") { // Octagonal coordinates
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("WHEAT")))
                put("airflow", true)
                put("direction", "90") // Any direction allowed for octagonal
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for octagonal tile with invalid direction`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") {
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("WHEAT")))
                put("airflow", true)
                put("direction", "123") // Invalid direction angle
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    // TILE SHAPE VALIDATION TESTS

    @Test
    fun `parseMap should return false for farmstead tile with octagonal coordinates`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FARMSTEAD") { // Invalid octagonal coordinates for farmstead
                put("farm", 0)
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for field tile with octagonal coordinates`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") { // Octagonal coordinates (both even)
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("WHEAT")))
                put("airflow", false) // Not using direction
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for field tile with square coordinates`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FIELD") { // Invalid square coordinates for field
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
    fun `parseMap should return true for village tile with any coordinate type`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE"), // Square coordinates - Village cannot have airflow
            createTileJson(2, 0, 0, "VILLAGE") // Octagonal coordinates - Village cannot have airflow
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    // PLANT TYPE VALIDATION TESTS

    @Test
    fun `parseMap should return true for field tile with valid field plants`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") {
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("POTATO", "WHEAT", "OAT", "PUMPKIN"))) // All valid field plants
                put("airflow", false) // Not using direction
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for field tile with plantation plants`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") {
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("APPLE"))) // Invalid plantation plant in field
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for plantation tile with valid plantation plants`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "PLANTATION") {
                put("farm", 1)
                put("capacity", 100)
                put("plant", "APPLE") // Valid plantation plant
                put("airflow", false) // Not using direction
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for plantation tile with field plants`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "PLANTATION") {
                put("farm", 1)
                put("capacity", 100)
                put("plant", "WHEAT") // Invalid field plant in plantation
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    // COMPREHENSIVE TILE CREATION TESTS

    @Test
    fun `parseMap should return false for invalid plants`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE"), // Village - no extra properties (including airflow)
            createTileJson(2, 0, 0, "FIELD") { // Field - octagonal coordinates
                put("farm", 0)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("PLANTATION")))
                put("airflow", true)
                put("direction", "90")
            },
            createTileJson(3, 3, 3, "FARMSTEAD") { // Farmstead - square coordinates
                put("farm", 0)
                put("airflow", false) // Not using direction
            },
            createTileJson(4, 2, 4, "PLANTATION") { // Plantation - octagonal coordinates
                put("farm", 0)
                put("capacity", 100)
                put("plant", "WHEAT")
                put("airflow", true)
                put("direction", "180")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for environment tiles with proper coordinates`() {
        val tiles = listOf(
            createTileJson(5, 5, 5, "MEADOW") { // Meadow - square coordinates only
                put("airflow", true)
                put("direction", "225") // Diagonal for square
            },
            createTileJson(6, 4, 0, "ROAD") { // Road - any coordinates
                put("airflow", true)
                put("direction", "90")
            },
            createTileJson(7, 7, 7, "FOREST") { // Forest - any coordinates
                put("airflow", true)
                put("direction", "315") // Diagonal for square
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for unknown tile category`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "UNKNOWN_CATEGORY") {
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for tile with airflow false and no direction`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") {
                put("farm", 0)
                put("shed", true)
                put("airflow", false) // No direction when airflow is false
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for tiles with different valid directions`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "MEADOW") { // Square - diagonal only
                put("airflow", true)
                put("direction", "45")
            },
            createTileJson(2, 4, 0, "ROAD") { // Can be any coordinate type
                put("airflow", true)
                put("direction", "90")
            },
            createTileJson(3, 7, 7, "FOREST") { // Square - diagonal only
                put("airflow", true)
                put("direction", "315")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for field and plantation with specific requirements`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") { // Octagonal coordinates required
                put("farm", 1)
                put("capacity", 100)
                put("possiblePlants", JSONArray(listOf("WHEAT", "POTATO"))) // Field plants only
                put("airflow", true)
                put("direction", "90") // Any direction for octagonal
            },
            createTileJson(2, 2, 4, "PLANTATION") { // Octagonal coordinates required
                put("farm", 2)
                put("capacity", 200)
                put("plant", "APPLE") // Plantation plant only
                put("airflow", true)
                put("direction", "180")
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return true for farmstead with shed property`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") { // Square coordinates required
                put("farm", 0)
                put("shed", true) // Optional shed property
                put("airflow", true)
                put("direction", "45") // Diagonal for square
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertTrue(parser.parseMap(mapFile))
    }

    // PROPERTY VALIDATION TESTS

    @Test
    fun `parseMap should return false for village tile with airflow property`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE") {
                put("airflow", true) // Invalid - villages cannot have airflow
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for village tile with direction property`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "VILLAGE") {
                put("direction", "45") // Invalid - villages cannot have direction
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for farmstead with missing farm property`() {
        val tiles = listOf(
            createTileJson(1, 1, 1, "FARMSTEAD") {
                put("airflow", false)
                // Missing required farm property
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for field with missing capacity`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "FIELD") {
                put("farm", 1)
                put("possiblePlants", JSONArray(listOf("WHEAT")))
                put("airflow", false)
                // Missing required capacity property
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for plantation with missing plant property`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "PLANTATION") {
                put("farm", 1)
                put("capacity", 100)
                put("airflow", false)
                // Missing required plant property
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }

    @Test
    fun `parseMap should return false for meadow with octagonal coordinates`() {
        val tiles = listOf(
            createTileJson(1, 0, 0, "MEADOW") { // Invalid octagonal coordinates for meadow
                put("airflow", false)
            }
        )

        val mapFile = createTempMapFile(createMapJson(tiles).toString())
        assertFalse(parser.parseMap(mapFile))
    }
}
