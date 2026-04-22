package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class FarmParserUnit {

    val myTiles: TileManager = Mockito.mock(TileManager::class.java)

    @BeforeEach
    fun setUp() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    @Test
    fun `FarmParser Plant Creation`() {
        val farmsParser = FarmsParser(myTiles)
        val jsonPlants = JSONArray(
            listOf(
                "WHEAT", "GRAPE", "CHERRY", "OAT",
                "APPLE", "ALMOND", "PUMPKIN", "WHEAT", "POTATO"
            )
        )
        val expectedPlants: List<PlantType> =
            listOf(
                FieldPlantType.WHEAT, PlantationPlantType.GRAPE, PlantationPlantType.CHERRY,
                FieldPlantType.OAT,
                PlantationPlantType.APPLE,
                PlantationPlantType.ALMOND,
                FieldPlantType.PUMPKIN,
                FieldPlantType.WHEAT,
                FieldPlantType.POTATO
            )
        assertEquals(expectedPlants, farmsParser.createPlantsList(jsonPlants))
    }

    @Test
    fun `FarmParser Plant Creation with Empty Array`() {
        val farmsParser = FarmsParser(myTiles)
        val jsonPlants = JSONArray()
        val expectedPlants: List<PlantType> = emptyList()
        assertEquals(expectedPlants, farmsParser.createPlantsList(jsonPlants))
    }

    @Test
    fun `FarmParser Plant Creation with Null Array`() {
        val farmsParser = FarmsParser(myTiles)
        val expectedPlants: List<PlantType> = emptyList()
        assertEquals(expectedPlants, farmsParser.createPlantsList(null))
    }

    @Test
    fun `FarmParser Action Creation`() {
        val farmsParser = FarmsParser(myTiles)
        val jsonActions = JSONArray(listOf("SOWING", "CUTTING", "MOWING", "WEEDING", "IRRIGATING", "HARVESTING"))
        val expectedActions: List<ActionType> = listOf(
            ActionType.SOWING,
            ActionType.CUTTING,
            ActionType.MOWING,
            ActionType.WEEDING,
            ActionType.IRRIGATING,
            ActionType.HARVESTING
        )
        assertEquals(expectedActions, farmsParser.createActionList(jsonActions))
    }

    @Test
    fun `FarmParser Action Creation with Empty Array`() {
        val farmsParser = FarmsParser(myTiles)
        val jsonActions = JSONArray()
        val expectedActions: List<ActionType> = emptyList()
        assertEquals(expectedActions, farmsParser.createActionList(jsonActions))
    }

    @Test
    fun `FarmParser Action Creation with Null Array`() {
        val farmsParser = FarmsParser(myTiles)
        val expectedActions: List<ActionType> = emptyList()
        assertEquals(expectedActions, farmsParser.createActionList(null))
    }

    @Test
    fun `FarmParser Machine Validation with Valid Data`() {
        val farmsParser = FarmsParser(myTiles)
        val farmstead = TileFactory.createFarmsteadTile(1, Coordinate(4, 4), true, 1, null)
        Mockito.`when`(myTiles.getTileById(1)).thenReturn(farmstead)
        val machineJson = JSONObject(
            """{
                "id": 100,
                "name": "TestMachine",
                "location": 1,
                "actions": ["SOWING"],
                "plants": ["WHEAT"],
                "duration": 7
            }"""
        )
        val result = farmsParser.validateMachine(machineJson, 1)
        assertNotNull(result)
    }

    @Test
    fun `FarmParser Machine Validation with Invalid Location`() {
        val farmsParser = FarmsParser(myTiles)
        // val farmstead = TileFactory.createFarmsteadTile(1, Coordinate(4, 4), true, 1, null)
        Mockito.`when`(myTiles.getTileById(999)).thenReturn(null)
        val machineJson = JSONObject(
            """{
                "id": 101,
                "name": "TestMachineInvalid",
                "location": 999,
                "actions": ["SOWING"],
                "plants": ["WHEAT"],
                "duration": 7
            }"""
        )

        val result = farmsParser.validateMachine(machineJson, 1)
        assertNull(result)
    }

    @Test
    fun `FarmParser Machine Validation with Invalid Duration`() {
        val farmsParser = FarmsParser(myTiles)
        val farmstead = TileFactory.createFarmsteadTile(1, Coordinate(4, 4), true, 1, null)
        Mockito.`when`(myTiles.getTileById(1)).thenReturn(farmstead)
        val machineJson = JSONObject(
            """{
                "id": 102,
                "name": "TestMachineInvalidDuration",
                "location": 1,
                "actions": ["SOWING"],
                "plants": ["WHEAT"],
                "duration": 20
            }"""
        )

        val result = farmsParser.validateMachine(machineJson, 1)
        assertNull(result)
    }

    @Test
    fun `FarmParser Machine Validation with Zero Duration`() {
        val farmsParser = FarmsParser(myTiles)
        val farmstead = TileFactory.createFarmsteadTile(1, Coordinate(4, 4), true, 1, null)
        Mockito.`when`(myTiles.getTileById(1)).thenReturn(farmstead)
        val machineJson = JSONObject(
            """{
                "id": 103,
                "name": "TestMachineZeroDuration",
                "location": 1,
                "actions": ["SOWING"],
                "plants": ["WHEAT"],
                "duration": 0
            }"""
        )

        val result = farmsParser.validateMachine(machineJson, 1)
        assertNull(result)
    }

    @Test
    fun `FarmParser Machine Validation with Empty Actions`() {
        val farmsParser = FarmsParser(myTiles)
        val farmstead = TileFactory.createFarmsteadTile(1, Coordinate(4, 4), true, 1, null)
        Mockito.`when`(myTiles.getTileById(1)).thenReturn(farmstead)
        val machineJson = JSONObject(
            """{
                "id": 104,
                "name": "TestMachineEmptyActions",
                "location": 1,
                "actions": [],
                "plants": ["WHEAT"],
                "duration": 7
            }"""
        )

        val result = farmsParser.validateMachine(machineJson, 1)
        assertNull(result)
    }

    @Test
    fun `FarmParser Machine Validation with Empty Plants`() {
        val farmsParser = FarmsParser(myTiles)
        val farmstead = TileFactory.createFarmsteadTile(1, Coordinate(4, 4), true, 1, null)
        Mockito.`when`(myTiles.getTileById(1)).thenReturn(farmstead)
        val machineJson = JSONObject(
            """{
                "id": 105,
                "name": "TestMachineEmptyPlants",
                "location": 1,
                "actions": ["SOWING"],
                "plants": [],
                "duration": 7
            }"""
        )
        val result = farmsParser.validateMachine(machineJson, 1)
        assertNull(result)
    }
}
