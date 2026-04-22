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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

class MachineWeedingMowingUnitTest {

    private lateinit var mockTileManager: TileManager
    private lateinit var mockStartTile: Tile

    @BeforeEach
    fun setUp() {
        Logger.concreteLogger = DebugLogger(PrintWriter(ByteArrayOutputStream()))

        mockTileManager = Mockito.mock(TileManager::class.java)
        mockStartTile = Mockito.mock(Tile::class.java)
        Mockito.`when`(mockStartTile.id).thenReturn(100)
    }

    private fun newMachineForTests(
        actions: List<ActionType> = listOf(ActionType.WEEDING, ActionType.MOWING),
        plants: List<PlantType> = listOf(FieldPlantType.WHEAT, PlantationPlantType.APPLE),
        duration: Int = 4,
        farmId: Int = 0
    ): Machine {
        return Machine(
            id = 1,
            name = "TestMachine",
            actions = actions,
            plants = plants,
            duration = duration,
            location = mockStartTile,
            tileManager = mockTileManager,
            farm = farmId
        )
    }

    @Test
    fun `doWeeding marks machine busy, increments handledThisTick, sets location, and updates plant progress`() {
        val machine = newMachineForTests()
        assertTrue(machine.isAvailable)
        assertEquals(0, machine.handledThisTick)

        val mockField = Mockito.mock(Field::class.java)
        Mockito.`when`(mockField.id).thenReturn(42)

        val mockFieldPlant = Mockito.mock(FieldPlant::class.java)
        Mockito.`when`(mockField.plant).thenReturn(mockFieldPlant)

        val currentTick = 7
        val yearTick = YearTick.OCT1

        machine.doWeeding(mockField, currentTick, yearTick)

        assertFalse(machine.isAvailable)
        assertEquals(1, machine.handledThisTick)
        assertSame(mockField, machine.location)

        Mockito.verify(mockFieldPlant, Mockito.times(1))
            .setProgress(ActionType.WEEDING, currentTick, yearTick)
    }

    @Test
    fun `doWeeding does nothing when field has no plant`() {
        val machine = newMachineForTests()
        assertTrue(machine.isAvailable)
        assertEquals(0, machine.handledThisTick)
        assertSame(mockStartTile, machine.location)

        val mockField = Mockito.mock(Field::class.java)
        Mockito.`when`(mockField.id).thenReturn(43)
        Mockito.`when`(mockField.plant).thenReturn(null)

        val currentTick = 8
        val yearTick = YearTick.OCT1

        machine.doWeeding(mockField, currentTick, yearTick)

        assertTrue(machine.isAvailable)
        assertEquals(0, machine.handledThisTick)
        assertSame(mockStartTile, machine.location)
    }

    @Test
    fun `doMowing marks machine busy, increments, sets location, and updates plantation plant progress`() {
        val machine = newMachineForTests()
        assertTrue(machine.isAvailable)
        assertEquals(0, machine.handledThisTick)

        val mockPlantation = Mockito.mock(Plantation::class.java)
        Mockito.`when`(mockPlantation.id).thenReturn(55)

        val mockPlantationPlant = Mockito.mock(PlantationPlant::class.java)
        Mockito.`when`(mockPlantation.plant).thenReturn(mockPlantationPlant)

        val currentTick = 9
        val yearTick = YearTick.OCT1

        machine.doMowing(mockPlantation, currentTick, yearTick)

        assertFalse(machine.isAvailable)
        assertEquals(1, machine.handledThisTick)
        assertSame(mockPlantation, machine.location)

        Mockito.verify(mockPlantationPlant, Mockito.times(1))
            .setProgress(ActionType.MOWING, currentTick, yearTick)
    }
}
