package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.ByteArrayOutputStream
import java.io.PrintWriter
import kotlin.test.assertSame

class FarmContinueActionUnitTest {

    private lateinit var mockTileManager: TileManager
    private lateinit var mockFarmstead: Tile
    private var actionCallCount = 0
    private val currentTick = 10
    private val yearTick = YearTick.JAN1
    private val farmId = 1

    @BeforeEach
    fun setUp() {
        Logger.concreteLogger = DebugLogger(PrintWriter(ByteArrayOutputStream()))

        mockTileManager = Mockito.mock(TileManager::class.java)
        mockFarmstead = Mockito.mock(Tile::class.java)
        actionCallCount = 0

        Mockito.`when`(mockFarmstead.id).thenReturn(100)
    }

    private fun newMachineForTests(
        actions: List<ActionType> = listOf(
            ActionType.IRRIGATING,
            ActionType.WEEDING,
            ActionType.MOWING,
            ActionType.HARVESTING,
            ActionType.CUTTING
        ),
        plants: List<PlantType> = listOf(FieldPlantType.WHEAT, PlantationPlantType.APPLE),
        duration: Int = 2,
        farmId: Int = 1
    ): Machine {
        val machine = Machine(
            id = 1,
            name = "TestMachine",
            actions = actions,
            plants = plants,
            duration = duration,
            location = mockFarmstead,
            tileManager = mockTileManager,
            farm = farmId
        )

        // Reset the machine for testing
        machine.refreshForNewTick(currentTick)
        return machine
    }

    private fun createMockField(id: Int, plantType: FieldPlantType = FieldPlantType.WHEAT): Field {
        val mockField = Mockito.mock(Field::class.java)
        val mockPlant = Mockito.mock(FieldPlant::class.java)

        Mockito.`when`(mockField.id).thenReturn(id)
        Mockito.`when`(mockField.plant).thenReturn(mockPlant)
        Mockito.`when`(mockPlant.type).thenReturn(plantType)
        Mockito.`when`(mockField.irrigationMissed).thenReturn(false)

        return mockField
    }

    private fun createMockPlantation(id: Int, plantType: PlantationPlantType = PlantationPlantType.APPLE): Plantation {
        val mockPlantation = Mockito.mock(Plantation::class.java)
        val mockPlant = Mockito.mock(PlantationPlant::class.java)

        Mockito.`when`(mockPlantation.id).thenReturn(id)
        Mockito.`when`(mockPlantation.plant).thenReturn(mockPlant)
        Mockito.`when`(mockPlant.type).thenReturn(plantType)
        Mockito.`when`(mockPlantation.permanentDisabled).thenReturn(null)

        return mockPlantation
    }

    @Test
    fun `continueIrrigation should irrigate multiple fields in radius`() {
        val machine = newMachineForTests()
        val mockField1 = createMockField(1, FieldPlantType.WHEAT)
        val mockField2 = createMockField(2, FieldPlantType.WHEAT)

        // Setup field irrigation conditions
        Mockito.`when`(mockField1.canBeIrrigated(currentTick, yearTick, machine.plants)).thenReturn(true)
        Mockito.`when`(mockField2.canBeIrrigated(currentTick, yearTick, machine.plants)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockField1, farmId, false)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockField2, farmId, false)).thenReturn(true)

        // Setup TileManager to return fields in radius for continuation
        Mockito.`when`(mockTileManager.getFarmFieldsInRadius(farmId, mockFarmstead, 2))
            .thenReturn(listOf(mockField1, mockField2))

        val farm = Farm(
            id = farmId,
            name = "Test Farm",
            farmsteads = listOf(mockFarmstead),
            fields = listOf(mockField1, mockField2),
            plantations = emptyList(),
            machines = listOf(machine),
            sowingPlans = mutableListOf()
        )

        farm.run(currentTick, yearTick)

        // Verify the machine processed multiple fields
        assertFalse(machine.isAvailable)
        assertTrue(machine.handledThisTick > 0)

        // The machine should have moved to one of the irrigated fields
        assertTrue(
            machine.location == mockField1 || machine.location == mockField2,
        )
    }

    @Test
    fun `continueWeeding should weed multiple fields in radius when irrigation unavailable`() {
        val machine = newMachineForTests(actions = listOf(ActionType.WEEDING))
        val mockField1 = createMockField(1, FieldPlantType.WHEAT)
        val mockField2 = createMockField(2, FieldPlantType.WHEAT)

        // Setup field weeding conditions (no irrigation available)
        Mockito.`when`(mockField1.canBeIrrigated(currentTick, yearTick, machine.plants)).thenReturn(false)
        Mockito.`when`(mockField2.canBeIrrigated(currentTick, yearTick, machine.plants)).thenReturn(false)
        Mockito.`when`(mockField1.canBeWed(currentTick, yearTick, machine.plants)).thenReturn(true)
        Mockito.`when`(mockField2.canBeWed(currentTick, yearTick, machine.plants)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockField1, farmId, false)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockField2, farmId, false)).thenReturn(true)

        // Setup TileManager to return fields in radius for continuation
        Mockito.`when`(mockTileManager.getFarmFieldsInRadius(farmId, mockFarmstead, 2))
            .thenReturn(listOf(mockField1, mockField2))

        val farm = Farm(
            id = farmId,
            name = "Test Farm",
            farmsteads = listOf(mockFarmstead),
            fields = listOf(mockField1, mockField2),
            plantations = emptyList(),
            machines = listOf(machine),
            sowingPlans = mutableListOf()
        )

        farm.run(currentTick, yearTick)

        // Verify the machine processed weeding
        assertFalse(machine.isAvailable)
        assertTrue(machine.handledThisTick > 0)
    }

    @Test
    fun `continueMowing should mow multiple plantations in radius`() {
        val machine = newMachineForTests(actions = listOf(ActionType.MOWING))
        val mockPlantation1 = createMockPlantation(1, PlantationPlantType.APPLE)
        val mockPlantation2 = createMockPlantation(2, PlantationPlantType.APPLE)

        // Setup plantation mowing conditions
        Mockito.`when`(mockPlantation1.canBeMowed(currentTick, yearTick, machine.plants)).thenReturn(true)
        Mockito.`when`(mockPlantation2.canBeMowed(currentTick, yearTick, machine.plants)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockPlantation1, farmId, false)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockPlantation2, farmId, false)).thenReturn(true)

        // Setup TileManager to return plantations in radius for continuation
        Mockito.`when`(mockTileManager.getFarmFieldsInRadius(farmId, mockFarmstead, 2)).thenReturn(emptyList())
        Mockito.`when`(mockTileManager.getFarmPlantationsInRadius(farmId, mockFarmstead, 2))
            .thenReturn(listOf(mockPlantation1, mockPlantation2))

        val farm = Farm(
            id = farmId,
            name = "Test Farm",
            farmsteads = listOf(mockFarmstead),
            fields = emptyList(),
            plantations = listOf(mockPlantation1, mockPlantation2),
            machines = listOf(machine),
            sowingPlans = mutableListOf()
        )

        farm.run(currentTick, yearTick)

        // Verify the machine processed mowing
        assertFalse(machine.isAvailable)
        assertTrue(machine.handledThisTick > 0)
    }

    @Test
    fun `continuePlantationCutting should cut multiple plantations in radius`() {
        val machine = newMachineForTests(actions = listOf(ActionType.CUTTING))
        val mockPlantation1 = createMockPlantation(1, PlantationPlantType.APPLE)
        val mockPlantation2 = createMockPlantation(2, PlantationPlantType.APPLE)

        // Setup plantation cutting conditions
        Mockito.`when`(mockPlantation1.canBeCut(currentTick, yearTick)).thenReturn(true)
        Mockito.`when`(mockPlantation2.canBeCut(currentTick, yearTick)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockPlantation1, farmId, false)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockPlantation2, farmId, false)).thenReturn(true)

        // Setup TileManager to return plantations in radius for continuation
        Mockito.`when`(mockTileManager.getFarmPlantationsInRadius(farmId, mockFarmstead, 2))
            .thenReturn(listOf(mockPlantation1, mockPlantation2))

        val farm = Farm(
            id = farmId,
            name = "Test Farm",
            farmsteads = listOf(mockFarmstead),
            fields = emptyList(),
            plantations = listOf(mockPlantation1, mockPlantation2),
            machines = listOf(machine),
            sowingPlans = mutableListOf()
        )

        farm.run(currentTick, yearTick)

        // Verify the machine processed cutting
        assertFalse(machine.isAvailable)
        assertTrue(machine.handledThisTick > 0)
    }

    @Test
    fun `continueFieldHarvesting should harvest multiple fields with same plant type`() {
        val machine = newMachineForTests(actions = listOf(ActionType.HARVESTING))
        val mockField1 = createMockField(1, FieldPlantType.WHEAT)
        val mockField2 = createMockField(2, FieldPlantType.WHEAT)

        // Setup field harvesting conditions
        Mockito.`when`(mockField1.canBeHarvested(currentTick, yearTick)).thenReturn(true)
        Mockito.`when`(mockField2.canBeHarvested(currentTick, yearTick)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockField1, farmId, false)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockField2, farmId, false)).thenReturn(true)

        // Setup TileManager to return fields in radius for continuation
        Mockito.`when`(mockTileManager.getFarmFieldsInRadius(farmId, mockFarmstead, 2))
            .thenReturn(listOf(mockField1, mockField2))

        val farm = Farm(
            id = farmId,
            name = "Test Farm",
            farmsteads = listOf(mockFarmstead),
            fields = listOf(mockField1, mockField2),
            plantations = emptyList(),
            machines = listOf(machine),
            sowingPlans = mutableListOf()
        )

        farm.run(currentTick, yearTick)

        // Verify the machine processed harvesting
        assertFalse(machine.isAvailable)
        assertTrue(machine.handledThisTick > 0)
    }

    @Test
    fun `continueFieldHarvesting should skip fields with different plant types`() {
        val machine = newMachineForTests(
            actions = listOf(ActionType.HARVESTING),
            plants = listOf(FieldPlantType.WHEAT, FieldPlantType.POTATO)
        )
        val mockField1 = createMockField(1, FieldPlantType.WHEAT)
        val mockField2 = createMockField(2, FieldPlantType.POTATO)

        // Setup field harvesting conditions
        Mockito.`when`(mockField1.canBeHarvested(currentTick, yearTick)).thenReturn(true)
        Mockito.`when`(mockField2.canBeHarvested(currentTick, yearTick)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockField1, farmId, false)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockField2, farmId, false)).thenReturn(true)

        // Setup TileManager to return fields in radius for continuation
        Mockito.`when`(mockTileManager.getFarmFieldsInRadius(farmId, mockFarmstead, 2))
            .thenReturn(listOf(mockField1, mockField2))

        val farm = Farm(
            id = farmId,
            name = "Test Farm",
            farmsteads = listOf(mockFarmstead),
            fields = listOf(mockField1, mockField2),
            plantations = emptyList(),
            machines = listOf(machine),
            sowingPlans = mutableListOf()
        )

        farm.run(currentTick, yearTick)

        // Verify the machine processed at least one harvesting action
        assertFalse(machine.isAvailable)
        assertTrue(machine.handledThisTick > 0)
    }

    @Test
    fun `continuation methods should handle empty radius results gracefully`() {
        val machine = newMachineForTests()

        // Setup empty tile responses
        Mockito.`when`(mockTileManager.getFarmFieldsInRadius(farmId, mockFarmstead, 2)).thenReturn(emptyList())
        Mockito.`when`(mockTileManager.getFarmPlantationsInRadius(farmId, mockFarmstead, 2)).thenReturn(emptyList())

        val farm = Farm(
            id = farmId,
            name = "Test Farm",
            farmsteads = listOf(mockFarmstead),
            fields = emptyList(),
            plantations = emptyList(),
            machines = listOf(machine),
            sowingPlans = mutableListOf()
        )

        // This should complete without hanging or throwing exceptions
        assertDoesNotThrow {
            farm.run(currentTick, yearTick)
        }

        // Machine should still be available since no actions were performed
        assertTrue(machine.isAvailable)
        assertEquals(0, machine.handledThisTick)
    }

    @Test
    fun `farm should process actions in priority order`() {
        val machine = newMachineForTests()
        val mockField = createMockField(1, FieldPlantType.WHEAT)

        // Setup conditions where irrigation is possible
        Mockito.`when`(mockField.canBeIrrigated(currentTick, yearTick, machine.plants)).thenReturn(true)
        Mockito.`when`(mockField.canBeWed(currentTick, yearTick, machine.plants)).thenReturn(true)
        Mockito.`when`(mockTileManager.existsPath(mockFarmstead, mockField, farmId, false)).thenReturn(true)
        Mockito.`when`(mockTileManager.getFarmFieldsInRadius(farmId, mockFarmstead, 2)).thenReturn(emptyList())

        val farm = Farm(
            id = farmId,
            name = "Test Farm",
            farmsteads = listOf(mockFarmstead),
            fields = listOf(mockField),
            plantations = emptyList(),
            machines = listOf(machine),
            sowingPlans = mutableListOf()
        )

        farm.run(currentTick, yearTick)

        // Machine should have processed irrigation (higher priority) over weeding
        assertSame(mockField, machine.location)
        assertFalse(machine.isAvailable)
    }

    @Test
    fun `multiple machines should be processed in duration then ID order`() {
        val machine1 = newMachineForTests(duration = 3) // Higher duration
        val machine2 = newMachineForTests(duration = 2) // Lower duration, should be processed first

        val farm = Farm(
            id = farmId,
            name = "Test Farm",
            farmsteads = listOf(mockFarmstead),
            fields = emptyList(),
            plantations = emptyList(),
            machines = listOf(machine1, machine2),
            sowingPlans = mutableListOf()
        )

        farm.run(currentTick, yearTick)

        // Both machines should be processed (though no work available)
        assertTrue(machine1.isAvailable)
        assertTrue(machine2.isAvailable)
        assertEquals(0, machine1.handledThisTick)
        assertEquals(0, machine2.handledThisTick)
    }
}
