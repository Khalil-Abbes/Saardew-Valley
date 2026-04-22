package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.parser.ScenarioParser
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.mockito.Mockito
import java.io.File
import java.nio.file.Path

class ScenarioParserUnitTest {

    private lateinit var parser: ScenarioParser
    private val mockFarmsByID: MutableMap<Int, Farm> = mutableMapOf()
    private val mockMachineByID: MutableMap<Int, Machine> = mutableMapOf()
    private lateinit var mockTileManager: TileManager
    private lateinit var cloudHandler: CloudHandler
    private val idToIncident: MutableMap<Int, JSONObject> = mutableMapOf()
    private val idToCloud: MutableMap<Int, JSONObject> = mutableMapOf()

    @TempDir
    lateinit var tempDir: Path

    @BeforeEach
    fun setUp() {
        mockFarmsByID.clear()
        mockMachineByID.clear()
        mockTileManager = Mockito.mock(TileManager::class.java)
        idToIncident.clear()
        idToCloud.clear()

        // Setup mock tiles for cloud and incident locations
        setupMockTiles()

        // Setup mock machines
        setupMockMachines()

        val emptyClouds = mutableListOf<Cloud>()
        cloudHandler = CloudHandler(mockTileManager, emptyClouds)

        // Create fresh parser instance for each test
        parser = ScenarioParser(mockTileManager, mockMachineByID, mockFarmsByID)
    }

    private fun setupMockTiles() {
        // Create mock tiles for various locations used in tests
        val validLocations = listOf(15, 25, 30, 40, 50, 60, 70, 80, 90)

        validLocations.forEach { location ->
            val mockTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
            Mockito.`when`(mockTile.categoryType).thenReturn(TileCategory.FIELD)
            Mockito.`when`(mockTileManager.getTileById(location)).thenReturn(mockTile)
        }

        // Setup a village tile for testing cloud placement restrictions
        val villageTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(villageTile.categoryType).thenReturn(TileCategory.VILLAGE)
        Mockito.`when`(mockTileManager.getTileById(100)).thenReturn(villageTile)

        // Setup specific tile types for incident validation
        setupSpecificTileTypes()

        // Setup getTilesInRadius for incidents
        setupLocationBasedIncidentRadiusTiles()
        setupCloudCreationRadiusTiles()
    }

    private fun setupSpecificTileTypes() {
        // For ANIMAL_ATTACK - needs at least one FOREST tile in radius
        val forestTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(forestTile.categoryType).thenReturn(TileCategory.FOREST)
        Mockito.`when`(mockTileManager.getTileById(26)).thenReturn(forestTile)

        // For BEE_HAPPY - needs at least one MEADOW tile in radius
        val meadowTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(meadowTile.categoryType).thenReturn(TileCategory.MEADOW)
        Mockito.`when`(mockTileManager.getTileById(31)).thenReturn(meadowTile)

        // For DROUGHT - needs at least one FIELD or PLANTATION tile in radius
        val plantationTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(plantationTile.categoryType).thenReturn(TileCategory.PLANTATION)
        Mockito.`when`(mockTileManager.getTileById(41)).thenReturn(plantationTile)
    }

    private fun setupLocationBasedIncidentRadiusTiles() {
        // ANIMAL_ATTACK at location 25 with radius 3 - must include FOREST tile
        val animalAttackTiles = listOf(
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.FIELD)
            },
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.FOREST) // Required FOREST tile
            },
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.ROAD)
            }
        )
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(25, 3)).thenReturn(animalAttackTiles)

        // BEE_HAPPY at location 30 with radius 1 - must include MEADOW tile
        val beeHappyTiles = listOf(
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.MEADOW) // Required MEADOW tile
            },
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.ROAD)
            }
        )
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(30, 1)).thenReturn(beeHappyTiles)

        // DROUGHT at location 40 with radius 2 - must include FIELD or PLANTATION tile
        val droughtTiles = listOf(
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.FIELD) // Required FIELD tile
            },
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.PLANTATION) // Also valid
            },
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.ROAD)
            }
        )
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(40, 2)).thenReturn(droughtTiles)

        // CITY_EXPANSION at location 50
        val cityExpansionTiles = listOf(
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.FIELD)
            }
        )
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(50, 0)).thenReturn(cityExpansionTiles)
    }

    private fun setupCloudCreationRadiusTiles() {
        // CLOUD_CREATION at location 15 with radius 2 - needs FIELD tiles
        val cloudCreationTiles15 = listOf(
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.FIELD)
            },
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.ROAD)
            }
        )
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(15, 2)).thenReturn(cloudCreationTiles15)

        // CLOUD_CREATION at location 30 with radius 2 - needs FIELD tiles
        val cloudCreationTiles30 = listOf(
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.FIELD)
            },
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.MEADOW)
            },
            Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java).apply {
                Mockito.`when`(categoryType).thenReturn(TileCategory.ROAD)
            }
        )
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(30, 2)).thenReturn(cloudCreationTiles30)
    }

    private fun setupMockMachines() {
        // Setup mock machines for BROKEN_MACHINE incident validation
        val mockMachine = Mockito.mock(Machine::class.java)
        Mockito.`when`(mockMachine.id).thenReturn(1)
        mockMachineByID[1] = mockMachine
    }

    // HELPER METHODS

    private fun createCloudJson(
        id: Int,
        location: Int,
        duration: Int,
        amount: Int
    ): JSONObject {
        val cloudJson = JSONObject().apply {
            put("id", id)
            put("location", location)
            put("duration", duration)
            put("amount", amount)
        }

        idToCloud[id] = cloudJson
        return cloudJson
    }

    private fun createIncidentJson(
        id: Int,
        type: String,
        tick: Int,
        additionalProps: JSONObject.() -> Unit = {}
    ): JSONObject {
        val incidentJson = JSONObject().apply {
            put("id", id)
            put("type", type)
            put("tick", tick)
            additionalProps()
        }

        idToIncident[id] = incidentJson
        return incidentJson
    }

    private fun createScenarioJson(
        clouds: List<JSONObject> = emptyList(),
        incidents: List<JSONObject> = emptyList()
    ) = JSONObject().apply {
        put(
            "clouds",
            JSONArray().apply {
                clouds.forEach { put(it) }
            }
        )
        put(
            "incidents",
            JSONArray().apply {
                incidents.forEach { put(it) }
            }
        )
    }

    private fun createTempScenarioFile(content: String): String {
        val tempFile = File(tempDir.toFile(), "test_scenario.json")
        tempFile.writeText(content)
        return tempFile.absolutePath
    }

    // -- parseClouds Tests (returns MutableList<Cloud>?) --

    @Test
    fun `parseClouds should return valid clouds list for valid unlimited duration`() {
        val clouds = listOf(
            createCloudJson(1, 15, -1, 1000),
            createCloudJson(2, 25, 5, 2000)
        )
        val scenario = createScenarioJson(clouds = clouds)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseClouds(scenarioFile)

        assertNotNull(result)
        assertTrue(result!!.isNotEmpty())
        assertTrue(result.size == 2)
    }

    @Test
    fun `parseClouds should return empty list for valid empty clouds array`() {
        val scenario = createScenarioJson()

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseClouds(scenarioFile)

        assertNotNull(result)
        assertTrue(result!!.isEmpty())
    }

    @Test
    fun `parseClouds should return null for duplicate cloud ids`() {
        val clouds = listOf(
            createCloudJson(3, 15, 8, 1000),
            createCloudJson(3, 25, 5, 2000) // Duplicate id
        )
        val scenario = createScenarioJson(clouds = clouds)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseClouds(scenarioFile)

        assertNull(result)
    }

    @Test
    fun `parseClouds should return null for cloud on village tile`() {
        val clouds = listOf(
            createCloudJson(4, 100, 8, 1000) // location 100 = village tile (invalid)
        )
        val scenario = createScenarioJson(clouds = clouds)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseClouds(scenarioFile)

        assertNull(result)
    }

    @Test
    fun `parseClouds should return null for cloud on non-existent tile`() {
        val clouds = listOf(
            createCloudJson(5, 999, 8, 1000) // location 999 has no tile
        )
        val scenario = createScenarioJson(clouds = clouds)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseClouds(scenarioFile)

        assertNull(result)
    }

    @Test
    fun `parseClouds should return null for cloud with zero duration`() {
        val clouds = listOf(
            createCloudJson(6, 15, 0, 1000) // Invalid: duration must be -1 or >= 1
        )
        val scenario = createScenarioJson(clouds = clouds)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseClouds(scenarioFile)

        assertNull(result)
    }

    @Test
    fun `parseClouds should return null for cloud with zero amount`() {
        val clouds = listOf(
            createCloudJson(7, 15, 8, 0) // Invalid: amount must be >= 1
        )
        val scenario = createScenarioJson(clouds = clouds)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseClouds(scenarioFile)

        assertNull(result)
    }

    @Test
    fun `parseClouds should return null for cloud with negative duration other than -1`() {
        val clouds = listOf(
            createCloudJson(8, 15, -5, 1000) // Invalid: only -1 allowed for negative duration
        )
        val scenario = createScenarioJson(clouds = clouds)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseClouds(scenarioFile)

        assertNull(result)
    }

    // -- parseIncidents Tests (returns List<Incident>?) --
    @Test
    fun `parseIncidents should return valid incidents list for animal attack incident`() {
        val incidents = listOf(
            createIncidentJson(102, "ANIMAL_ATTACK", 15) {
                put("location", 25)
                put("radius", 3)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNotNull(result)
        assertTrue(result!!.isNotEmpty())
        assertTrue(result.size == 1)
    }

    @Test
    fun `parseIncidents should return valid incidents list for bee happy incident`() {
        val incidents = listOf(
            createIncidentJson(103, "BEE_HAPPY", 10) {
                put("location", 30)
                put("radius", 1)
                put("effect", 25)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNotNull(result)
        assertTrue(result!!.isNotEmpty())
        assertTrue(result.size == 1)
    }

    @Test
    fun `parseIncidents should return valid incidents list for drought incident`() {
        val incidents = listOf(
            createIncidentJson(104, "DROUGHT", 20) {
                put("location", 40)
                put("radius", 2)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNotNull(result)
        assertTrue(result!!.isNotEmpty())
        assertTrue(result.size == 1)
    }

    @Test
    fun `parseIncidents should return valid incidents list for broken machine incident`() {
        val incidents = listOf(
            createIncidentJson(105, "BROKEN_MACHINE", 25) {
                put("machineId", 1)
                put("duration", 5)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNotNull(result)
        assertTrue(result!!.isNotEmpty())
        assertTrue(result.size == 1)
    }

    @Test
    fun `parseIncidents should return empty list for valid empty incidents array`() {
        val scenario = createScenarioJson()

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNotNull(result)
        assertTrue(result!!.isEmpty())
    }

    @Test
    fun `parseIncidents should return null for duplicate incident ids`() {
        val incidents = listOf(
            createIncidentJson(107, "ANIMAL_ATTACK", 15) {
                put("location", 25)
                put("radius", 3)
            }
        )

        val scenario1 = createScenarioJson(incidents = incidents)
        val scenarioFile1 = createTempScenarioFile(scenario1.toString())

        // First parse should succeed
        val result1 = parser.parseIncidents(scenarioFile1, cloudHandler)
        assertNotNull(result1)

        // Second parse with same ID should fail (parser's internal duplicate check)
        val result2 = parser.parseIncidents(scenarioFile1, cloudHandler)
        assertNull(result2)
    }

    @Test
    fun `parseIncidents should return null for incident with invalid type`() {
        val incidents = listOf(
            createIncidentJson(108, "INVALID_TYPE", 8)
        )
        val scenario = createScenarioJson(incidents = incidents)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNull(result)
    }

    @Test
    fun `parseIncidents should return null for incident on non-existent tile`() {
        val incidents = listOf(
            createIncidentJson(109, "ANIMAL_ATTACK", 8) {
                put("location", 999)
                put("radius", 2)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNull(result)
    }

    @Test
    fun `parseIncidents should return null for broken machine with non-existent machine`() {
        val incidents = listOf(
            createIncidentJson(110, "BROKEN_MACHINE", 25) {
                put("machineId", 999)
                put("duration", 5)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNull(result)
    }

    @Test
    fun `parseIncidents should return null for cloud creation incident with missing properties`() {
        val incidents = listOf(
            createIncidentJson(111, "CLOUD_CREATION", 8) {
                put("location", 15)
                // Missing radius, duration, amount
            }
        )
        val scenario = createScenarioJson(incidents = incidents)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNull(result)
    }

    @Test
    fun `parseIncidents should return valid list for bee happy incident with valid effect`() {
        val incidents = listOf(
            createIncidentJson(112, "BEE_HAPPY", 10) {
                put("location", 30)
                put("radius", 1)
                put("effect", 100)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)

        val scenarioFile = createTempScenarioFile(scenario.toString())
        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNotNull(result)
    }

    // Add these tests to your existing ScenarioParserUnitTest class

    @Test
    fun `parseIncidents should return valid list for city expansion incident with valid field tile`() {
        // Setup field tile at location 50 (expansion location)
        val fieldTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(fieldTile.categoryType).thenReturn(TileCategory.FIELD)
        Mockito.`when`(mockTileManager.getTileById(50)).thenReturn(fieldTile)

        // Setup adjacent village tile in radius 1 (required for city expansion)
        val villageTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(villageTile.categoryType).thenReturn(TileCategory.VILLAGE)
        Mockito.`when`(villageTile.id).thenReturn(51)

        val roadTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(roadTile.categoryType).thenReturn(TileCategory.ROAD)
        Mockito.`when`(roadTile.id).thenReturn(52)

        val cityExpansionAdjacent = listOf(villageTile, roadTile)
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(50, 1)).thenReturn(cityExpansionAdjacent)

        val incidents = listOf(
            createIncidentJson(113, "CITY_EXPANSION", 30) {
                put("location", 50)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)
        val scenarioFile = createTempScenarioFile(scenario.toString())

        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNotNull(result)
        assertTrue(result!!.isNotEmpty())
        assertEquals(1, result.size)
    }

    @Test
    fun `parseIncidents should return valid list for city expansion incident with valid road tile`() {
        // Setup road tile at location 55 (expansion location)
        val roadTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(roadTile.categoryType).thenReturn(TileCategory.ROAD)
        Mockito.`when`(mockTileManager.getTileById(55)).thenReturn(roadTile)

        // Setup adjacent village tile in radius 1 (required for city expansion)
        val villageTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(villageTile.categoryType).thenReturn(TileCategory.VILLAGE)
        Mockito.`when`(villageTile.id).thenReturn(56)

        val fieldTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(fieldTile.categoryType).thenReturn(TileCategory.FIELD)
        Mockito.`when`(fieldTile.id).thenReturn(57)

        val cityExpansionAdjacent = listOf(villageTile, fieldTile)
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(55, 1)).thenReturn(cityExpansionAdjacent)

        val incidents = listOf(
            createIncidentJson(114, "CITY_EXPANSION", 35) {
                put("location", 55)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)
        val scenarioFile = createTempScenarioFile(scenario.toString())

        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNotNull(result)
        assertTrue(result!!.isNotEmpty())
        assertEquals(1, result.size)
    }

    @Test
    fun `parseIncidents should return null for city expansion incident on village tile`() {
        // Setup village tile at expansion location (invalid - cannot expand village on village)
        val villageTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(villageTile.categoryType).thenReturn(TileCategory.VILLAGE)
        Mockito.`when`(mockTileManager.getTileById(60)).thenReturn(villageTile)

        val incidents = listOf(
            createIncidentJson(115, "CITY_EXPANSION", 40) {
                put("location", 60)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)
        val scenarioFile = createTempScenarioFile(scenario.toString())

        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNull(result)
    }

    @Test
    fun `parseIncidents should return null for city expansion incident on forest tile`() {
        // Setup forest tile at expansion location (invalid - cannot expand city on forest)
        val forestTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(forestTile.categoryType).thenReturn(TileCategory.FOREST)
        Mockito.`when`(mockTileManager.getTileById(65)).thenReturn(forestTile)

        val incidents = listOf(
            createIncidentJson(116, "CITY_EXPANSION", 45) {
                put("location", 65)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)
        val scenarioFile = createTempScenarioFile(scenario.toString())

        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNull(result)
    }

    @Test
    fun `parseIncidents should return null for city expansion incident without adjacent village`() {
        // Setup field tile at expansion location
        val fieldTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(fieldTile.categoryType).thenReturn(TileCategory.FIELD)
        Mockito.`when`(mockTileManager.getTileById(70)).thenReturn(fieldTile)

        // Setup adjacent tiles without village (invalid - needs adjacent village)
        val roadTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(roadTile.categoryType).thenReturn(TileCategory.ROAD)
        Mockito.`when`(roadTile.id).thenReturn(71)

        val meadowTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(meadowTile.categoryType).thenReturn(TileCategory.MEADOW)
        Mockito.`when`(meadowTile.id).thenReturn(72)

        val cityExpansionAdjacent = listOf(roadTile, meadowTile)
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(70, 1)).thenReturn(cityExpansionAdjacent)

        val incidents = listOf(
            createIncidentJson(117, "CITY_EXPANSION", 50) {
                put("location", 70)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)
        val scenarioFile = createTempScenarioFile(scenario.toString())

        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNull(result)
    }

    @Test
    fun `parseIncidents should return null for city expansion incident with adjacent forest`() {
        // Setup field tile at expansion location
        val fieldTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(fieldTile.categoryType).thenReturn(TileCategory.FIELD)
        Mockito.`when`(mockTileManager.getTileById(75)).thenReturn(fieldTile)

        // Setup adjacent tiles with village AND forest (invalid - cannot have forest adjacent)
        val villageTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(villageTile.categoryType).thenReturn(TileCategory.VILLAGE)
        Mockito.`when`(villageTile.id).thenReturn(76)

        val forestTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(forestTile.categoryType).thenReturn(TileCategory.FOREST)
        Mockito.`when`(forestTile.id).thenReturn(77)

        val cityExpansionAdjacent = listOf(villageTile, forestTile)
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(75, 1)).thenReturn(cityExpansionAdjacent)

        val incidents = listOf(
            createIncidentJson(118, "CITY_EXPANSION", 55) {
                put("location", 75)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)
        val scenarioFile = createTempScenarioFile(scenario.toString())

        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNull(result)
    }

    @Test
    fun `parseIncidents should return null for city expansion incident on already converted village tile`() {
        // Setup field tile at expansion location
        val fieldTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(fieldTile.categoryType).thenReturn(TileCategory.FIELD)
        Mockito.`when`(mockTileManager.getTileById(80)).thenReturn(fieldTile)

        // Simulate this tile was already converted to village in previous expansion
        parser.tilesToVillage[80] = 10 // Previously converted at tick 10

        // Setup adjacent village tile
        val villageTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(villageTile.categoryType).thenReturn(TileCategory.VILLAGE)
        Mockito.`when`(villageTile.id).thenReturn(81)

        val cityExpansionAdjacent = listOf(villageTile)
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(80, 1)).thenReturn(cityExpansionAdjacent)

        val incidents = listOf(
            createIncidentJson(119, "CITY_EXPANSION", 60) {
                put("location", 80)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)
        val scenarioFile = createTempScenarioFile(scenario.toString())

        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNull(result)
    }

    @Test
    fun `parseIncidents should return valid list for cloud creation incident with valid parameters`() {
        // Setup field tile at location 15 (cloud creation location)
        val fieldTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(fieldTile.categoryType).thenReturn(TileCategory.FIELD)
        Mockito.`when`(fieldTile.id).thenReturn(15)
        Mockito.`when`(mockTileManager.getTileById(15)).thenReturn(fieldTile)

        // Setup tiles in radius that are not village and not in tilesToVillage
        val affectedFieldTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(affectedFieldTile.categoryType).thenReturn(TileCategory.FIELD)
        Mockito.`when`(affectedFieldTile.id).thenReturn(16)

        val affectedRoadTile = Mockito.mock(de.unisaarland.cs.se.selab.tile.Tile::class.java)
        Mockito.`when`(affectedRoadTile.categoryType).thenReturn(TileCategory.ROAD)
        Mockito.`when`(affectedRoadTile.id).thenReturn(17)

        val cloudCreationAffected = listOf(affectedFieldTile, affectedRoadTile)
        Mockito.`when`(mockTileManager.getAllNeighborTilesInRadius(fieldTile, 2)).thenReturn(cloudCreationAffected)

        val incidents = listOf(
            createIncidentJson(120, "CLOUD_CREATION", 25) {
                put("location", 15)
                put("radius", 2)
                put("duration", 5)
                put("amount", 1000)
            }
        )
        val scenario = createScenarioJson(incidents = incidents)
        val scenarioFile = createTempScenarioFile(scenario.toString())

        val result = parser.parseIncidents(scenarioFile, cloudHandler)

        assertNotNull(result)
        assertTrue(result!!.isNotEmpty())
        assertEquals(1, result.size)
    }
}
