package de.unisaarland.cs.se.selab.plants

import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import java.io.PrintWriter
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FieldTest {

    @BeforeEach
    fun setUp() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    private fun newFieldAllType(): Field =
        Field(
            3,
            Coordinate(0, 0),
            1,
            null,
            300,
            listOf(FieldPlantType.WHEAT, FieldPlantType.POTATO, FieldPlantType.PUMPKIN, FieldPlantType.OAT)
        )

    @Test
    fun `killPlant sets plant to null and updates lastFallow`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = 100
        field.killPlant(5)
        assertNull(field.plant)
        assertEquals(5, field.lastFallow)
    }

    @Test
    fun `reduceSoilMoisture with plant reduces moisture by MOISTURE_REDUCTION_WITH_PLANTS`() {
        val field = newFieldAllType()
        field.plant = FieldPlant(FieldPlantType.WHEAT)
        field.currentMoisture = 250
        field.reduceSoilMoisture()
        assertEquals(150, field.currentMoisture)
    }

    @Test
    fun `reduceSoilMoisture without plant reduces moisture by MOISTURE_REDUCTION_WITHOUT_PLANTS`() {
        val field = newFieldAllType()
        field.plant = null
        field.currentMoisture = 250
        field.reduceSoilMoisture()
        assertEquals(180, field.currentMoisture)
    }

    @Test
    fun `reduceSoilMoisture with plant doesn't go below zero`() {
        val field = newFieldAllType()
        field.plant = FieldPlant(FieldPlantType.WHEAT)
        field.currentMoisture = 50
        field.reduceSoilMoisture()
        assertEquals(0, field.currentMoisture)
    }

    @Test
    fun `reduceSoilMoisture without plant doesn't go below zero`() {
        val field = newFieldAllType()
        field.plant = null
        field.currentMoisture = 50
        field.reduceSoilMoisture()
        assertEquals(0, field.currentMoisture)
    }

    @Test
    fun `isMoistureBelowThreshold returns false when no plant`() {
        val field = newFieldAllType()
        field.plant = null
        field.currentMoisture = 100
        val result = field.isMoistureBelowThreshold()
        assertFalse(result)
    }

    @Test
    fun `isMoistureBelowThreshold returns true when moisture below required`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentMoisture = 400
        val result = field.isMoistureBelowThreshold()
        assertTrue(result)
    }

    @Test
    fun `isMoistureBelowThreshold returns false when moisture above required`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentMoisture = 500

        val result = field.isMoistureBelowThreshold()

        assertFalse(result)
    }

    @Test
    fun `canBeHarvested returns false when no plant`() {
        val field = newFieldAllType()
        field.plant = null
        val result = field.canBeHarvested(1, YearTick.JUN1)
        assertFalse(result)
    }

    @Test
    fun `canBeHarvested returns false when harvest estimate is zero`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = 0
        val result = field.canBeHarvested(1, YearTick.JUN1)
        assertFalse(result)
    }

    @Test
    fun `canBeHarvested returns true when all conditions met`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = 1000
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        val result = field.canBeHarvested(1, YearTick.JUN1)
        assertTrue(result)
    }

    @Test
    fun `canBeHarvested returns false when outside harvest window`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = 1000
        plant.progress.sowing.add(Pair(0, YearTick.OCT1)) // Sow wheat

        val result = field.canBeHarvested(1, YearTick.MAY1) // Outside wheat's harvest window

        assertFalse(result)
    }

    @Test
    fun `canBeSowed returns false when field already has plant`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant

        val result = field.canBeSowed(1, FieldPlantType.WHEAT)

        assertFalse(result)
    }

    @Test
    fun `canBeSowed returns false when plant type is not in possible plants`() {
        val field = newFieldAllType()
        field.plant = FieldPlant(FieldPlantType.WHEAT)
        val result = field.canBeSowed(
            1,
            FieldPlantType.POTATO
        )
        assertFalse(result)
    }

    @Test
    fun `canBeSowed returns false when still in fallow period`() {
        val field = newFieldAllType()
        field.plant = null
        field.lastFallow = 5
        val result = field.canBeSowed(7, FieldPlantType.WHEAT)
        assertFalse(result)
    }

    @Test
    fun `canBeSowed returns true when all conditions met`() {
        val field = newFieldAllType()
        field.plant = null
        field.lastFallow = 5
        val result = field.canBeSowed(10, FieldPlantType.WHEAT)
        assertTrue(result)
    }

    @Test
    fun `canBeIrrigated returns false when no plant`() {
        val field = newFieldAllType()
        field.plant = null
        val result = field.canBeIrrigated(1, YearTick.JUN1, listOf(FieldPlantType.WHEAT))
        assertFalse(result)
    }

    @Test
    fun `canBeIrrigated returns true when all conditions met`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentMoisture = 400
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        val result = field.canBeIrrigated(1, YearTick.JUN1, listOf(FieldPlantType.WHEAT))
        assertTrue(result)
    }

    @Test
    fun `canBeIrrigated returns false when moisture above required`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentMoisture = 500
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        val result = field.canBeIrrigated(1, YearTick.JUN1, listOf(FieldPlantType.WHEAT))
        assertFalse(result)
    }

    @Test
    fun `canBeIrrigated returns false when plant not in machine plants`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentMoisture = 400
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        val result = field.canBeIrrigated(1, YearTick.JUN1, listOf(FieldPlantType.POTATO))
        assertFalse(result)
    }

    @Test
    fun `updateHarvestEstimate with no plant clears incident effects`() {
        val field = newFieldAllType()
        field.plant = null
        field.addIncidentEffect(10)
        field.updateHarvestEstimate(1, YearTick.JUN1)
        assertTrue(field.incidentOrderListThisTick.isEmpty())
    }

    @Test
    fun `updateHarvestEstimate kills plant if harvest estimate becomes zero`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = 1000
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        field.currentMoisture = 0
        field.updateHarvestEstimate(1, YearTick.JUN1)
        assertNull(field.plant)
    }

    @Test
    fun `missedIrrigation returns false when no plant`() {
        val field = newFieldAllType()
        field.plant = null
        val result = field.missedIrrigation(1, YearTick.NOV1)
        assertFalse(result)
    }

    @Test
    fun `missedIrrigation returns true when moisture is below requirement and penalty threshold met`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentMoisture = 300
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        val result = field.missedIrrigation(1, YearTick.NOV1)
        assertTrue(result)
    }

    @Test
    fun `missedIrrigation returns false when moisture difference is below penalty threshold`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentMoisture = 400
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        val result = field.missedIrrigation(1, YearTick.NOV1)
        assertFalse(result)
    }

    @Test
    fun `canBeWed returns false when no plant`() {
        val field = newFieldAllType()
        field.plant = null
        val result = field.canBeWed(1, YearTick.MAY1, listOf(FieldPlantType.WHEAT))
        assertFalse(result)
    }

    @Test
    fun `canBeWed returns true when not weeding window`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = 1000
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        val result = field.canBeWed(5, YearTick.MAY1, listOf(FieldPlantType.WHEAT))
        assertFalse(result)
    }

    @Test
    fun `canBeWed returns false when plant not in machine plants`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = 1000
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        val result = field.canBeWed(5, YearTick.MAY1, listOf(FieldPlantType.POTATO))
        assertFalse(result)
    }

    @Test
    fun `canBeWed returns false when harvest estimate is zero`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = 0
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        val result = field.canBeWed(5, YearTick.MAY1, listOf(FieldPlantType.WHEAT))
        assertFalse(result)
    }

    @Test
    fun `canBePollinated returns false when no plant`() {
        val field = newFieldAllType()
        field.plant = null
        val result = field.canBePollinated(1, YearTick.MAY1)
        assertFalse(result)
    }

    @Test
    fun `canBePollinated returns true for insect-pollinated plant in bloom window (even potatoes)`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.POTATO)
        field.plant = plant
        plant.progress.sowing.add(Pair(0, YearTick.APR1))
        val result = field.canBePollinated(4, YearTick.MAY1)
        assertTrue(result)
    }

    @Test
    fun `canBePollinated returns false for self-pollinated plant`() {
        val field = newFieldAllType()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        plant.progress.sowing.add(Pair(0, YearTick.OCT1))
        val result = field.canBePollinated(1, YearTick.MAY1)
        assertFalse(result)
    }

    @Test
    fun `isFallow returns true when in fallow period`() {
        val field = newFieldAllType()
        field.lastFallow = 5
        val result = field.isFallow(7)

        assertTrue(result)
    }

    @Test
    fun `isFallow returns false when outside fallow period`() {
        val field = newFieldAllType()
        field.lastFallow = 5
        val result = field.isFallow(10)
        assertFalse(result)
    }

    @Test
    fun `field check late sowing with no plant`() {
        val field = newFieldAllType()
        field.plant = null
        val result = field.missedIrrigation(1, YearTick.NOV1)
        assertFalse(result)
    }

    @Test
    fun `field check late sowing with plant with small moisture`() {
        val field = newFieldAllType()
        field.plant = FieldPlant(FieldPlantType.WHEAT)
        field.currentMoisture = 50
        val result = field.missedIrrigation(1, YearTick.NOV1)
        assertTrue(result)
    }

    @Test
    fun `field check late sowing with plant with no moisture`() {
        val field = newFieldAllType()
        field.plant = FieldPlant(FieldPlantType.WHEAT)
        field.currentMoisture = 50
        val result = field.missedIrrigation(1, YearTick.NOV1)
        assertTrue(result)
    }
}
