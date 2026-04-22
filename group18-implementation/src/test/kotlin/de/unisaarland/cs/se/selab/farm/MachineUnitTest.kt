package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.logger.ImportantLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.plant.Progress
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MachineUnitTest {

    private val mockTileManager: TileManager = mock()
    private val mockField: Field = mock()
    private val mockPlantation: Plantation = mock()
    private val mockTile: Tile = mock()
    private val mockYearTick: YearTick = mock()
    private val mockFieldPlant: FieldPlant = mock()
    private val mockProgress: Progress = mock()

    private lateinit var machine: Machine
    private val farmId = 1

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        val dummyWriter = java.io.PrintWriter(java.io.StringWriter())
        Logger.concreteLogger = ImportantLogger(dummyWriter)

        whenever(mockFieldPlant.progress).thenReturn(mockProgress)

        // Create a machine with specific capabilities for testing
        val actions = listOf(
            ActionType.SOWING,
            ActionType.IRRIGATING,
            ActionType.HARVESTING,
            ActionType.WEEDING,
            ActionType.MOWING,
            ActionType.CUTTING
        )
        val plants = listOf<PlantType>(
            FieldPlantType.WHEAT,
            FieldPlantType.POTATO,
            FieldPlantType.OAT,
            PlantationPlantType.APPLE,
            PlantationPlantType.CHERRY
        )

        machine = Machine(
            id = 1,
            name = "Test Machine",
            actions = actions,
            plants = plants,
            duration = 2,
            location = mockTile,
            tileManager = mockTileManager,
            farm = farmId
        )

        // Setup common mock behaviors
        whenever(mockField.id).thenReturn(100)
        whenever(mockPlantation.id).thenReturn(200)
        whenever(mockTile.id).thenReturn(300)
    }

    @Test
    fun `refreshForNewTick should reset handledThisTick and maintain availability when not broken`() {
        machine.handledThisTick = 3
        machine.isAvailable = true
        machine.brokenReleaseTick = null

        machine.refreshForNewTick(5)

        assertEquals(0, machine.handledThisTick)
        assertTrue(machine.isAvailable)
    }

    @Test
    fun `refreshForNewTick should make machine available after broken period ends`() {
        machine.brokenReleaseTick = 5
        val currentTick = 6

        machine.refreshForNewTick(currentTick)

        assertTrue(machine.isAvailable)
    }

    @Test
    fun `refreshForNewTick should keep machine unavailable during broken period`() {
        machine.brokenReleaseTick = 10
        val currentTick = 5

        machine.refreshForNewTick(currentTick)

        assertFalse(machine.isAvailable)
    }

    @Test
    fun `markBroken should set finite broken period`() {
        val duration = 3
        val incidentTick = 5

        machine.markBroken(duration, incidentTick)

        assertEquals(9, machine.brokenReleaseTick)
    }

    @Test
    fun `markBroken should set permanent break with duration -1`() {
        val duration = -1
        val incidentTick = 5

        machine.markBroken(duration, incidentTick)

        assertEquals(Int.MAX_VALUE, machine.brokenReleaseTick)
    }

    @Test
    fun `markBroken should extend existing broken period`() {
        machine.brokenReleaseTick = 8
        val duration = 4
        val incidentTick = 6

        machine.markBroken(duration, incidentTick)

        assertEquals(11, machine.brokenReleaseTick)
    }

    @Test
    fun `isAvailable should consider both availability flag and broken status`() {
        machine.isAvailable = true
        machine.brokenReleaseTick = null
        assertTrue(machine.isAvailable(5))

        machine.isAvailable = true
        machine.brokenReleaseTick = 10
        assertFalse(machine.isAvailable(5))

        machine.isAvailable = false
        machine.brokenReleaseTick = null
        assertFalse(machine.isAvailable(5))

        machine.isAvailable = false
        machine.brokenReleaseTick = 10
        assertFalse(machine.isAvailable(5))
    }

    @Test
    fun `doSowing should plant on field and update machine state`() {
        val currentTick = 10
        machine.handledThisTick = currentTick
        val plantType = FieldPlantType.WHEAT
        whenever(mockField.plant).thenReturn(null)

        machine.doSowing(mockField, currentTick, mockYearTick, plantType)

        assertFalse(machine.isAvailable)
        assertEquals(11, machine.handledThisTick)
        assertEquals(mockField, machine.location)
        verify(mockField).plant = any<FieldPlant>()
    }

    @Test
    fun `doIrrigation should update plant progress and field moisture`() {
        val currentTick = 5
        machine.handledThisTick = currentTick
        whenever(mockField.plant).thenReturn(mockFieldPlant)
        whenever(mockField.moistureCapacity).thenReturn(100)

        machine.doIrrigation(mockField, currentTick, mockYearTick)

        assertFalse(machine.isAvailable)
        assertEquals(6, machine.handledThisTick)
        assertEquals(mockField, machine.location)
        verify(mockField).currentMoisture = 100
        verify(mockFieldPlant.progress).addEntry(ActionType.IRRIGATING, currentTick, mockYearTick)
    }

    @Test
    fun `doHarvesting should add to cargo and reset field`() {
        val currentTick = 5
        val harvestAmount = 75
        whenever(mockField.plant).thenReturn(mockFieldPlant)
        whenever(mockFieldPlant.type).thenReturn(FieldPlantType.WHEAT)
        whenever(mockField.currentHarvestEstimate).thenReturn(harvestAmount)

        machine.doHarvesting(mockField, currentTick, mockYearTick)

        assertFalse(machine.isAvailable)
        assertEquals(1, machine.handledThisTick)
        assertEquals(mockField, machine.location)
        assertEquals(harvestAmount, machine.cargo[FieldPlantType.WHEAT])
        verify(mockField).currentHarvestEstimate = 0
        verify(mockField).lastFallow = currentTick
        verify(mockField).plant = null
        verify(mockFieldPlant).setProgress(ActionType.HARVESTING, currentTick, mockYearTick)
    }

    @Test
    fun `doWeeding should update field plant progress`() {
        val currentTick = 5
        whenever(mockField.plant).thenReturn(mockFieldPlant)

        machine.doWeeding(mockField, currentTick, mockYearTick)

        assertFalse(machine.isAvailable)
        assertEquals(1, machine.handledThisTick)
        assertEquals(mockField, machine.location)
        verify(mockFieldPlant).setProgress(ActionType.WEEDING, currentTick, mockYearTick)
    }

    @Test
    fun `doWeeding should do nothing when field has no plant`() {
        val currentTick = 5
        whenever(mockField.plant).thenReturn(null)

        machine.doWeeding(mockField, currentTick, mockYearTick)

        assertTrue(machine.isAvailable)
        assertEquals(0, machine.handledThisTick)
    }

    @Test
    fun `canHandle should return true for supported plant with available path and time`() {
        val plantType = FieldPlantType.WHEAT
        whenever(mockTileManager.existsPath(mockTile, mockField, farmId, false)).thenReturn(true)
        machine.handledThisTick = 0

        val result = machine.canHandle(plantType, mockField)

        assertTrue(result)
    }

    @Test
    fun `canHandle should return false for unsupported plant type`() {
        val unsupportedPlant = FieldPlantType.PUMPKIN
        whenever(mockTileManager.existsPath(mockTile, mockField, farmId, false)).thenReturn(true)
        machine.handledThisTick = 0

        val result = machine.canHandle(unsupportedPlant, mockField)

        assertFalse(result)
    }

    @Test
    fun `canHandle should return false when no path exists`() {
        val plantType = FieldPlantType.WHEAT
        whenever(mockTileManager.existsPath(mockTile, mockField, farmId, false)).thenReturn(false)
        machine.handledThisTick = 0

        val result = machine.canHandle(plantType, mockField)

        assertFalse(result)
    }

    @Test
    fun `canHandle should return false when insufficient time remaining`() {
        val plantType = FieldPlantType.WHEAT
        whenever(mockTileManager.existsPath(mockTile, mockField, farmId, false)).thenReturn(true)
        machine.handledThisTick = 6

        val result = machine.canHandle(plantType, mockField)

        assertTrue(result)
    }

    @Test
    fun `canHandle should consider cargo when checking path`() {
        val plantType = FieldPlantType.WHEAT
        machine.cargo[FieldPlantType.WHEAT] = 50
        whenever(mockTileManager.existsPath(mockTile, mockField, farmId, true)).thenReturn(true)
        machine.handledThisTick = 0

        val result = machine.canHandle(plantType, mockField)

        assertTrue(result)
        verify(mockTileManager).existsPath(mockTile, mockField, farmId, true)
    }

    @Test
    fun `returnToFarmstead should update location when path exists`() {
        val mockFarmstead: Tile = mock()
        whenever(mockTileManager.existsPath(mockTile, mockFarmstead, farmId, false)).thenReturn(true)

        val result = machine.returnToFarmstead(mockFarmstead, mutableListOf())

        assertTrue(result)
        assertEquals(mockFarmstead, machine.location)
    }

    @Test
    fun `returnToFarmstead should try alternative farmsteads when primary fails`() {
        val primaryFarmstead: Tile = mock()
        val alternativeFarmstead: Tile = mock()
        whenever(mockTileManager.existsPath(mockTile, primaryFarmstead, farmId, false)).thenReturn(false)
        whenever(mockTileManager.existsPath(mockTile, alternativeFarmstead, farmId, false)).thenReturn(true)
        val farmsteads = mutableListOf(alternativeFarmstead)

        val result = machine.returnToFarmstead(primaryFarmstead, farmsteads)

        assertTrue(result)
        assertEquals(alternativeFarmstead, machine.location)
        assertTrue(farmsteads.isEmpty())
    }

    @Test
    fun `returnToFarmstead should return false when no path to any farmstead`() {
        val mockFarmstead: Tile = mock()
        whenever(mockTileManager.existsPath(mockTile, mockFarmstead, farmId, false)).thenReturn(false)

        val result = machine.returnToFarmstead(mockFarmstead, mutableListOf())

        assertFalse(result)
        assertEquals(mockTile, machine.location)
    }

    @Test
    fun `canIrrigateField should return true for field irrigation capable machine`() {
        val result = machine.canIrrigateField()

        assertTrue(result)
    }

    @Test
    fun `canIrrigatePlantation should return true for plantation irrigation capable machine`() {
        val result = machine.canIrrigatePlantation()

        assertTrue(result)
    }

    @Test
    fun `cargo should accumulate multiple harvests correctly`() {
        whenever(mockField.plant).thenReturn(mockFieldPlant)
        whenever(mockFieldPlant.type).thenReturn(FieldPlantType.WHEAT)
        whenever(mockField.currentHarvestEstimate).thenReturn(30)

        machine.doHarvesting(mockField, 1, mockYearTick)
        machine.doHarvesting(mockField, 2, mockYearTick)

        assertEquals(60, machine.cargo[FieldPlantType.WHEAT])
    }
}
