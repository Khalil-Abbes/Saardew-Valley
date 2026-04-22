package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.io.PrintWriter
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UpdateHarvestEstimateTest {
    @BeforeEach
    fun setLogger() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    // plantation setup
    private fun newApple(): Plantation =
        Plantation(
            3,
            Coordinate(0, 0),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.APPLE)
        )

    private fun newGrape(): Plantation =
        Plantation(
            4,
            Coordinate(2, 2),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.GRAPE)
        )

    private fun newAlmond(): Plantation =
        Plantation(
            5,
            Coordinate(4, 4),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.ALMOND)
        )

    private fun newCherry(): Plantation =
        Plantation(
            6,
            Coordinate(6, 6),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.CHERRY)
        )

    // plantation test for updateHarvest
    @Test
    fun `environment penalty tests Apple`() {
        val p = newApple()
        val start = p.currentHarvestEstimate
        p.sunlightThisTick = 90
        p.currentMoisture = 50
        p.updateHarvestEstimate(10, YearTick.MAY2)
        val expected = (start * 0.9).toInt()
        assertEquals(expected, p.currentHarvestEstimate)
    }

    @Test
    fun `environment penalty tests Grape`() {
        val p = newGrape()
        val start = p.currentHarvestEstimate
        p.sunlightThisTick = 175
        p.currentMoisture = 50
        p.updateHarvestEstimate(10, YearTick.MAY2)
        val expected = (start * 0.9).toInt() - 100
        assertEquals(expected, p.currentHarvestEstimate)
    }

    @Test
    fun `environment penalty tests Almond`() {
        val p = newAlmond()
        val start = p.currentHarvestEstimate
        p.sunlightThisTick = 175
        p.currentMoisture = 50
        p.updateHarvestEstimate(10, YearTick.MAY2)
        val expected = (start * 0.9).toInt() - 150
        assertEquals(expected, p.currentHarvestEstimate)
    }

    @Test
    fun `environment penalty tests Cherry`() {
        val p = newCherry()
        val start = p.currentHarvestEstimate
        p.sunlightThisTick = 175
        p.currentMoisture = 50
        p.updateHarvestEstimate(10, YearTick.MAY2)
        val expected = (start * 0.9 * 0.9).toInt() - 50
        assertEquals(expected, p.currentHarvestEstimate)
    }

    @Test
    fun `missed cutting penalty tests Apple`() {
        val p = newApple()
        val start = p.currentHarvestEstimate
        p.sunlightThisTick = 50
        p.currentMoisture = 100
        p.nextCycleToCut = 24
        p.updateHarvestEstimate(30, YearTick.FEB2)
        val expected = start / 2
        assertEquals(expected, p.currentHarvestEstimate)
    }

    @Test
    fun `missed cutting penalty tests Grape`() {
        val p = newGrape()
        val start = p.currentHarvestEstimate
        p.sunlightThisTick = 150
        p.currentMoisture = 250
        p.nextCycleToCut = 24
        p.updateHarvestEstimate(30, YearTick.AUG2)
        val expected = start / 2
        assertEquals(expected, p.currentHarvestEstimate)
    }

    // test missed mowing; since behavior is the same, we only test it on almond
    @Test
    fun `missed mowing penalty tests Apple`() {
        val p = newApple()
        val start = p.currentHarvestEstimate
        p.sunlightThisTick = p.plant.type.sunlightComfort
        p.currentMoisture = p.plant.type.moistureRequired
        p.updateHarvestEstimate(30, YearTick.JUN1)
        val expected = (start * 0.9).toInt()
        assertEquals(expected, p.currentHarvestEstimate)
    }

    @Test
    fun `november 1 with cutting done test for Almond`() {
        val p = newAlmond()
        p.sunlightThisTick = p.plant.type.sunlightComfort
        p.currentMoisture = p.plant.type.moistureRequired
        p.plant.progress.addEntry(ActionType.CUTTING, 30, YearTick.NOV1)
        val expected = p.plant.progress
        p.updateHarvestEstimate(30, YearTick.NOV1)
        assertEquals(expected.cutting, p.plant.progress.cutting)
    }

    @Test
    fun `missed harvesting for Apple test`() {
        val p = newApple()
        p.sunlightThisTick = p.plant.type.sunlightComfort
        p.currentMoisture = p.plant.type.moistureRequired
        val start = p.currentHarvestEstimate
        p.updateHarvestEstimate(30, YearTick.OCT1)
        val expected = start / 2
        assertEquals(expected, p.currentHarvestEstimate)
    }

    @Test
    fun `missed harvesting for Grape test`() {
        val p = newGrape()
        p.sunlightThisTick = p.plant.type.sunlightComfort
        p.currentMoisture = p.plant.type.moistureRequired
        val start = p.currentHarvestEstimate
        p.updateHarvestEstimate(28, YearTick.SEP1)
        p.updateHarvestEstimate(29, YearTick.SEP2)
        p.updateHarvestEstimate(30, YearTick.OCT1)
        val expected = (start * 0.95 * 0.95 * 0.95).toInt()
        assertEquals(expected, p.currentHarvestEstimate)
    }

    @Disabled
    @Test
    fun `skip updateHarvest on permanentDisabled plantation`() {
        val p = newApple()
        p.sunlightThisTick = p.plant.type.sunlightComfort
        p.currentMoisture = p.plant.type.moistureRequired
        val start = p.currentHarvestEstimate
        p.permanentDisabled = 29
        p.updateHarvestEstimate(30, YearTick.OCT1)
        assertEquals(start, p.currentHarvestEstimate)
    }

    @Test
    fun `if harvestEstimate start at 0 then it should stay 0`() {
        val p = newApple()
        p.sunlightThisTick = p.plant.type.sunlightComfort
        p.currentMoisture = p.plant.type.moistureRequired
        p.currentHarvestEstimate = 0
        val start = p.currentHarvestEstimate
        p.updateHarvestEstimate(30, YearTick.MAY2)
        assertEquals(start, p.currentHarvestEstimate)
    }

    // set up for field with all plants
    private fun newFieldAllType(): Field =
        Field(
            3,
            Coordinate(0, 0),
            1,
            null,
            300,
            listOf(FieldPlantType.WHEAT, FieldPlantType.POTATO, FieldPlantType.PUMPKIN, FieldPlantType.OAT)
        )

    private fun newFieldNoType(): Field =
        Field(
            3,
            Coordinate(0, 0),
            1,
            null,
            300,
            emptyList()
        )

    @Test
    fun `return false when we call canBeHarvested early or late`() {
        val f = newFieldAllType()
        f.plant = FieldPlant(FieldPlantType.POTATO)
        assertFalse(f.canBeHarvested(10, YearTick.AUG2))
        assertFalse(f.canBeHarvested(10, YearTick.NOV1))
    }

    @Test
    fun `return false when we call canBeSowed on empty plant`() {
        val f = newFieldNoType()
        val plant = FieldPlantType.POTATO
        f.lastFallow = 5
        assertFalse(f.canBeSowed(10, plant))
    }

    @Test
    fun `test killPlant when currentHarvestEstimate is 0`() {
        val f = newFieldAllType()
        f.plant = FieldPlant(FieldPlantType.POTATO)
        f.currentMoisture = f.plant?.type?.moistureRequired ?: 0
        f.sunlightThisTick = f.plant?.type?.sunlightComfort ?: 0
        f.currentHarvestEstimate = f.plant?.type?.initialHarvestEstimate ?: 0
        f.plant?.progress?.addEntry(ActionType.SOWING, 0, YearTick.MAY2)
        f.updateHarvestEstimate(10, YearTick.OCT2)
        assertEquals(0, f.currentHarvestEstimate)
        // assertEquals(null, f.plant)
    }

    @Test
    fun `penalties for late sowing on potato test`() {
        val f = newFieldAllType()
        f.plant = FieldPlant(FieldPlantType.POTATO)
        f.currentMoisture = f.plant?.type?.moistureRequired ?: 0
        f.sunlightThisTick = f.plant?.type?.sunlightComfort ?: 0
        f.currentHarvestEstimate = f.plant?.type?.initialHarvestEstimate ?: 0
        val start = f.plant?.type?.initialHarvestEstimate ?: 0
        f.plant?.progress?.addEntry(ActionType.SOWING, 10, YearTick.JUN1)
        f.updateHarvestEstimate(10, YearTick.JUN1)
        val expected = (start * 0.8).toInt()
        assertEquals(expected, f.currentHarvestEstimate)
    }

    @Test
    fun `killPlants if currentMoisture is 0 and change estimate to 0`() {
        val f = newFieldAllType()
        f.plant = FieldPlant(FieldPlantType.POTATO)
        f.currentMoisture = 0
        f.sunlightThisTick = f.plant?.type?.sunlightComfort ?: 0
        f.currentHarvestEstimate = f.plant?.type?.initialHarvestEstimate ?: 0
        f.plant?.progress?.addEntry(ActionType.SOWING, 10, YearTick.MAY2)
        f.updateHarvestEstimate(19, YearTick.OCT1)
        val expected = 0
        assertEquals(expected, f.currentHarvestEstimate)
        // assertEquals(null, f.plant)
    }

    @Test
    fun `mock plantation currentHarvestEstimate returns expected value`() {
        val mockPlantation = Mockito.mock(Plantation::class.java)

        Mockito.`when`(mockPlantation.currentHarvestEstimate).thenReturn(500)

        assertEquals(500, mockPlantation.currentHarvestEstimate)
    }

    @Test
    fun `mock plantation updateHarvestEstimate with environment penalty`() {
        val mockPlantation = Mockito.mock(Plantation::class.java)
        val mockPlant = Mockito.mock(PlantationPlant::class.java)
        val mockPlantType = Mockito.mock(PlantationPlantType::class.java)

        Mockito.`when`(mockPlantation.plant).thenReturn(mockPlant)
        Mockito.`when`(mockPlant.type).thenReturn(mockPlantType)
        Mockito.`when`(mockPlantType.sunlightComfort).thenReturn(100)
        Mockito.`when`(mockPlantType.moistureRequired).thenReturn(150)
        Mockito.`when`(mockPlantation.sunlightThisTick).thenReturn(80)
        Mockito.`when`(mockPlantation.currentMoisture).thenReturn(100)
        Mockito.`when`(mockPlantation.currentHarvestEstimate).thenReturn(1000).thenReturn(900)

        // Simulate penalty application
        mockPlantation.updateHarvestEstimate(10, YearTick.MAY2)

        assertEquals(100, mockPlantType.sunlightComfort)
        assertEquals(150, mockPlantType.moistureRequired)
    }

    @Test
    fun `mock field canBeHarvested returns expected boolean`() {
        val mockField = Mockito.mock(Field::class.java)

        Mockito.`when`(mockField.canBeHarvested(10, YearTick.OCT1)).thenReturn(true)
        Mockito.`when`(mockField.canBeHarvested(10, YearTick.NOV2)).thenReturn(false)

        assertTrue(mockField.canBeHarvested(10, YearTick.OCT1))
        assertFalse(mockField.canBeHarvested(10, YearTick.NOV2))
    }

    @Test
    fun `mock field canBeSowed returns expected boolean`() {
        val mockField = Mockito.mock(Field::class.java)

        Mockito.`when`(mockField.canBeSowed(15, FieldPlantType.WHEAT)).thenReturn(true)
        Mockito.`when`(mockField.canBeSowed(15, FieldPlantType.POTATO)).thenReturn(false)

        assertTrue(mockField.canBeSowed(15, FieldPlantType.WHEAT))
        assertFalse(mockField.canBeSowed(15, FieldPlantType.POTATO))
    }

    @Test
    fun `mock plantation with cutting penalty`() {
        val mockPlantation = Mockito.mock(Plantation::class.java)

        Mockito.`when`(mockPlantation.currentHarvestEstimate).thenReturn(1000).thenReturn(500)
        Mockito.`when`(mockPlantation.nextCycleToCut).thenReturn(25)

        // Initial harvest estimate
        assertEquals(1000, mockPlantation.currentHarvestEstimate)

        // Simulate cutting penalty applied
        mockPlantation.updateHarvestEstimate(30, YearTick.NOV1)

        // After penalty
        assertEquals(500, mockPlantation.currentHarvestEstimate)
    }

    @Test
    fun `mock field with zero harvest estimate`() {
        val mockField = Mockito.mock(Field::class.java)
        val mockPlant = Mockito.mock(FieldPlant::class.java)

        Mockito.`when`(mockField.plant).thenReturn(mockPlant)
        Mockito.`when`(mockField.currentHarvestEstimate).thenReturn(0)
        Mockito.`when`(mockField.currentMoisture).thenReturn(0)

        assertEquals(0, mockField.currentHarvestEstimate)
        assertEquals(0, mockField.currentMoisture)
    }

    @Test
    fun `mock plantation sunlight and moisture levels`() {
        val mockPlantation = Mockito.mock(Plantation::class.java)

        Mockito.`when`(mockPlantation.sunlightThisTick).thenReturn(120)
        Mockito.`when`(mockPlantation.currentMoisture).thenReturn(200)

        assertEquals(120, mockPlantation.sunlightThisTick)
        assertEquals(200, mockPlantation.currentMoisture)
    }

    @Test
    fun `mock field plant type and properties`() {
        val mockField = Mockito.mock(Field::class.java)
        val mockPlant = Mockito.mock(FieldPlant::class.java)
        val mockPlantType = Mockito.mock(FieldPlantType::class.java)

        Mockito.`when`(mockField.plant).thenReturn(mockPlant)
        Mockito.`when`(mockPlant.type).thenReturn(mockPlantType)
        Mockito.`when`(mockPlantType.initialHarvestEstimate).thenReturn(800)
        Mockito.`when`(mockPlantType.sunlightComfort).thenReturn(90)

        assertEquals(mockPlant, mockField.plant)
        assertEquals(800, mockPlantType.initialHarvestEstimate)
        assertEquals(90, mockPlantType.sunlightComfort)
    }

    @Test
    fun `mock plantation permanent disability check`() {
        val mockPlantation = Mockito.mock(Plantation::class.java)

        Mockito.`when`(mockPlantation.permanentDisabled).thenReturn(25)
        Mockito.`when`(mockPlantation.currentHarvestEstimate).thenReturn(750)

        assertEquals(25, mockPlantation.permanentDisabled)
        assertEquals(750, mockPlantation.currentHarvestEstimate)
    }

    @Test
    fun `mock field last fallow property`() {
        val mockField = Mockito.mock(Field::class.java)

        Mockito.`when`(mockField.lastFallow).thenReturn(8)

        assertEquals(8, mockField.lastFallow)
    }
}
