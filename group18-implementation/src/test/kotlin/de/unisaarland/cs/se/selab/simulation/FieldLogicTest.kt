package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.tile.Field
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import kotlin.math.pow
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FieldLogicTest {

    @BeforeEach
    fun setLogger() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    private fun newField(): Field =
        Field(
            id = 1,
            coordinate = Coordinate(0, 0),
            farm = 1,
            direction = null,
            moistureCapacity = 400,
            possiblePlants = listOf(FieldPlantType.WHEAT, FieldPlantType.POTATO, FieldPlantType.PUMPKIN)
        )

    @Test fun `sowing allowed only when empty and fallow done and plant allowed`() {
        val f = newField()
        assertTrue(f.canBeSowed(currentTick = 10, plantType = FieldPlantType.WHEAT))
        f.plant = FieldPlant(FieldPlantType.WHEAT)
        assertFalse(f.canBeSowed(11, FieldPlantType.WHEAT))
    }

    // @Disabled
    @Test fun `fallow period blocks immediate re-sow after kill`() {
        val f = newField()
        // put a plant, then kill it at tick 20
        f.plant = FieldPlant(FieldPlantType.POTATO)
        f.killPlant(currentTick = 20)
        // f.updateHarvestEstimate(21, FieldPlantType.) // lastFallow set to 21 internally
        // FALLOW_PERIOD = 4 -> the earliest sow tick 25
        assertFalse(f.canBeSowed(24, FieldPlantType.POTATO))
        assertTrue(f.canBeSowed(25, FieldPlantType.POTATO))
    }

    @Test fun `reduceSoilMoisture reduces by 100 with plant and 70 without`() {
        // with plant
        val f1 = newField()
        f1.plant = FieldPlant(FieldPlantType.WHEAT)
        assertEquals(400, f1.currentMoisture)
        f1.reduceSoilMoisture()
        assertEquals(300, f1.currentMoisture)

        // without plant
        val f2 = newField()
        assertEquals(400, f2.currentMoisture)
        f2.reduceSoilMoisture()
        assertEquals(330, f2.currentMoisture)
    }

    @Test fun `isMoistureBelowThreshold true only when below requirement`() {
        val f = newField()
        val p = FieldPlant(FieldPlantType.OAT) // requirement 300
        f.plant = p
        // starts at 400 ≥ 300 ⇒ false
        assertFalse(f.isMoistureBelowThreshold())
        f.reduceSoilMoisture() // -100 -> 300
        assertFalse(f.isMoistureBelowThreshold()) // 300 < 300 ? false
        f.reduceSoilMoisture() // -100 -> 200
        assertTrue(f.isMoistureBelowThreshold())
    }

    @Test fun `canBeIrrigated only if below threshold, plant allowed and tile free this tick`() {
        val f = newField()
        val p = FieldPlant(FieldPlantType.POTATO) // requirement 500
        f.plant = p
        f.currentMoisture = 400 // below threshold
        // ok: plant matches & no other action this tick
        assertTrue(
            f.canBeIrrigated(
                currentTick = 5,
                currentYearTick = YearTick.MAY1,
                machinePlants = listOf(FieldPlantType.POTATO)
            )
        )

        // wrong machine plants
        assertFalse(f.canBeIrrigated(5, YearTick.MAY1, machinePlants = listOf(FieldPlantType.WHEAT)))

        // already worked this tick
        p.setProgress(ActionType.IRRIGATING, 6, YearTick.MAY1)
        assertFalse(f.canBeIrrigated(6, YearTick.MAY1, machinePlants = listOf(FieldPlantType.POTATO)))

        // not below threshold
        f.currentMoisture = 600
        assertFalse(f.canBeIrrigated(7, YearTick.MAY1, machinePlants = listOf(FieldPlantType.POTATO)))
    }

    // @Disabled("")
    @Test
    fun `missed weeding reduces to 90 percent`() {
        val f = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT) // weeding offsets [3, 9]
        f.plant = plant
        f.currentHarvestEstimate = plant.type.initialHarvestEstimate
        plant.setProgress(ActionType.SOWING, 0, YearTick.OCT1)

        // neutralize environment
        f.sunlightThisTick = plant.type.sunlightComfort
        f.currentMoisture = plant.type.moistureRequired
        // simulate tick 3 (weeding window) without weeding
        f.updateHarvestEstimate(3, YearTick.NOV1)
        assertEquals((plant.type.initialHarvestEstimate * 0.9).toInt(), f.currentHarvestEstimate)
    }

    // @Disabled("")
    @Test
    fun `late sowing applies 0_8 power difference only when sowing done late`() {
        val f = newField()
        val plant = FieldPlant(FieldPlantType.POTATO) // sow window APR1...MAY2, “late” up to +2
        f.plant = plant
        f.currentHarvestEstimate = plant.type.initialHarvestEstimate

        // sowed at MAY2+1 (1 late)
        plant.setProgress(ActionType.SOWING, 100, YearTick.JUN1)

        // neutralize environment
        f.sunlightThisTick = plant.type.sunlightComfort
        f.currentMoisture = plant.type.moistureRequired

        f.updateHarvestEstimate(100, YearTick.JUN1)
        assertEquals((plant.type.initialHarvestEstimate * 0.8).toInt(), f.currentHarvestEstimate)
    }

    // @Disabled("")
    @Test
    fun `sunlight penalty is 10 percent per full 25 above comfort`() {
        val f = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT) // comfort 90
        f.plant = plant
        val base = 1_000_000
        f.currentHarvestEstimate = base

        // choose JUN1 sunlight = 168 -> diff 78 -> 78/25 = 3 full steps -> 0.9^3
        f.sunlightThisTick = YearTick.JUN1.sunlight
        f.currentMoisture = plant.type.moistureRequired // avoid moisture penalty
        plant.setProgress(ActionType.SOWING, 1, YearTick.OCT1) // any sowing so plant exists

        f.updateHarvestEstimate(2, YearTick.JUN1)
        val expected = (base * 0.9.pow(3)).toInt()
        assertEquals(expected, f.currentHarvestEstimate)
    }

    // @Disabled("This test is to double check, (1, MAY2) can't sync with (5, JUN1), (2, JUN1) is correct")
    // @Disabled("")
    @Test
    fun `moisture penalty subtracts 50 per full 100 deficit after sunlight`() {
        val f = newField()
        val plant = FieldPlant(FieldPlantType.PUMPKIN) // requirement 600, comfort 120
        f.plant = plant
        val base = 1_000_000
        f.currentHarvestEstimate = base

        // one full 25-step sunlight penalty (168-120=48 -> 1 step)
        f.sunlightThisTick = YearTick.JUN1.sunlight // 168
        // deficit 200 -> 200/100 = 2 -> 2*50 = 100 to subtract after sunlight
        f.currentMoisture = 400

        // any sowing to avoid NPE in some flows
        plant.setProgress(ActionType.SOWING, 1, YearTick.MAY2)

        f.updateHarvestEstimate(2, YearTick.JUN1)
        val afterSun = (base * 0.9).toInt()
        val expected = (afterSun - 100).coerceAtLeast(0)
        assertEquals(expected, f.currentHarvestEstimate)
    }

    // @Disabled("To check ticks")
    @Test
    fun `missed harvesting- end day has no penalty, 1st late day 80 percent, 2nd late day zero for wheat`() {
        val f = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT) // harvest window JUN1...JUL1, late up to +2
        f.plant = plant
        val base = 1000
        plant.setProgress(ActionType.SOWING, 1, YearTick.OCT1)

        // neutralize environment
        f.sunlightThisTick = plant.type.sunlightComfort
        f.currentMoisture = plant.type.moistureRequired

        // end day (JUL1): first penalty -20%
        f.currentHarvestEstimate = base
        f.updateHarvestEstimate(19, YearTick.JUL1)
        assertEquals((base * 0.8).toInt(), f.currentHarvestEstimate)

        // first late day (JUL2): another -20%%
        // f.currentHarvestEstimate = (base*0.8)
        f.updateHarvestEstimate(20, YearTick.JUL2)
        assertEquals((base * 0.8.pow(2)).toInt(), f.currentHarvestEstimate)

        // second late day (AUG1): 0
        f.updateHarvestEstimate(21, YearTick.AUG1)
        assertEquals(0, f.currentHarvestEstimate)
    }

    // @Disabled("Failing test")
    // @Disabled("")
    @Test
    fun `missed harvesting - for potato and pumpkin any late day becomes 0`() {
        // POTATO
        run {
            val f = newField()
            val plant = FieldPlant(FieldPlantType.POTATO) // harvest SEP1...OCT2 (no late rule)
            f.plant = plant
            val base = 1000
            plant.setProgress(ActionType.SOWING, 1, YearTick.MAY1)
            f.sunlightThisTick = plant.type.sunlightComfort
            f.currentMoisture = plant.type.moistureRequired

            f.currentHarvestEstimate = base
            f.updateHarvestEstimate(12, YearTick.OCT2)
            assertEquals(0, f.currentHarvestEstimate)
        }
        // PUMPKIN
        run {
            val f = newField()
            val plant = FieldPlant(FieldPlantType.PUMPKIN)
            f.plant = plant
            val base = 1000
            plant.setProgress(ActionType.SOWING, 1, YearTick.JUN1)
            f.sunlightThisTick = plant.type.sunlightComfort
            f.currentMoisture = plant.type.moistureRequired

            f.currentHarvestEstimate = base
            f.updateHarvestEstimate(10, YearTick.OCT2)
            assertEquals(0, f.currentHarvestEstimate)
        }
    }

    @Test fun `canBeHarvested respects window and one action per tick`() {
        val f = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        f.plant = plant
        f.currentHarvestEstimate = 500
        plant.setProgress(ActionType.SOWING, 1, YearTick.OCT1)

        // neutralize environment so estimate stays > 0
        f.sunlightThisTick = plant.type.sunlightComfort
        f.currentMoisture = plant.type.moistureRequired

        // In-window: JUN1...JUL1 (use JUN1)
        assertTrue(f.canBeHarvested(currentTick = 20, currentYearTick = YearTick.JUN1))

        // mark harvested action this tick -> should block
        plant.setProgress(ActionType.HARVESTING, 21, YearTick.JUN1)
        assertFalse(f.canBeHarvested(currentTick = 21, currentYearTick = YearTick.JUN1))
    }

    @Test fun `canBeHarvested respects window for potato and one action per tick`() {
        val f = newField()
        val plant = FieldPlant(FieldPlantType.POTATO)
        f.plant = plant
        f.currentHarvestEstimate = 500
        plant.setProgress(ActionType.SOWING, 0, YearTick.MAY2)

        // neutralize environment so estimate stays > 0
        f.sunlightThisTick = plant.type.sunlightComfort
        f.currentMoisture = plant.type.moistureRequired

        // In-window: JUN1...JUL1 (use JUN1)
        assertFalse(f.canBeHarvested(currentTick = 3, currentYearTick = YearTick.NOV1))
    }

    @Test
    fun `canBeWed is in weeding window and in machine actions`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.WHEAT.initialHarvestEstimate

        plant.setProgress(ActionType.SOWING, 7, YearTick.OCT1)

        val machinePlants = listOf(FieldPlantType.WHEAT, FieldPlantType.OAT)
        val currentTick = 10
        val currentYearTick = YearTick.NOV2

        assertTrue(field.canBeWed(currentTick, currentYearTick, machinePlants))
    }

    @Test
    fun `canBeWed is in weeding window 2`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.WHEAT.initialHarvestEstimate

        plant.setProgress(ActionType.SOWING, 7, YearTick.OCT1)

        val machinePlants = listOf(FieldPlantType.WHEAT, FieldPlantType.OAT)
        val currentTick = 16
        val currentYearTick = YearTick.FEB2

        assertTrue(field.canBeWed(currentTick, currentYearTick, machinePlants))
    }

    @Test
    fun `canBeWed is in weeding window but NOT in machine actions`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.WHEAT.initialHarvestEstimate

        plant.setProgress(ActionType.SOWING, 7, YearTick.OCT1)

        val machinePlants = listOf(FieldPlantType.PUMPKIN, FieldPlantType.OAT)
        val currentTick = 10
        val currentYearTick = YearTick.NOV2

        assertFalse(field.canBeWed(currentTick, currentYearTick, machinePlants))
    }

    @Test
    fun `canBeWed is in weeding window but wasn't sowed`() {
        val field = newField()

        val machinePlants = listOf(FieldPlantType.PUMPKIN, FieldPlantType.OAT)
        val currentTick = 10
        val currentYearTick = YearTick.NOV2

        assertFalse(field.canBeWed(currentTick, currentYearTick, machinePlants))
    }

    @Test
    fun `canBeWed is not in weeding window by one`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.WHEAT.initialHarvestEstimate

        plant.setProgress(ActionType.SOWING, 7, YearTick.OCT1)

        val machinePlants = listOf(FieldPlantType.WHEAT, FieldPlantType.OAT)
        val currentTick = 11
        val currentYearTick = YearTick.DEC1

        assertFalse(field.canBeWed(currentTick, currentYearTick, machinePlants))
    }

    @Test
    fun `canBeWed harvest estimate ZERO`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = 0

        plant.setProgress(ActionType.SOWING, 7, YearTick.OCT1)

        val machinePlants = listOf(FieldPlantType.WHEAT, FieldPlantType.OAT)
        val currentTick = 10
        val currentYearTick = YearTick.NOV2

        assertFalse(field.canBeWed(currentTick, currentYearTick, machinePlants))
    }

    @Test
    fun `canBeWed Potato everyN rule`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.POTATO)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.POTATO.initialHarvestEstimate

        plant.setProgress(ActionType.SOWING, 1, YearTick.MAY2)

        val machinePlants = listOf(FieldPlantType.WHEAT, FieldPlantType.POTATO)

        assertTrue(field.canBeWed(3, YearTick.JUN2, machinePlants))
        assertTrue(field.canBeWed(5, YearTick.JUL2, machinePlants))
        assertTrue(field.canBeWed(7, YearTick.AUG2, machinePlants))
    }

    @Test
    fun `canBeWed Potato everyN rule off by one error`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.POTATO)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.POTATO.initialHarvestEstimate

        plant.setProgress(ActionType.SOWING, 1, YearTick.MAY2)

        val machinePlants = listOf(FieldPlantType.WHEAT, FieldPlantType.POTATO)

        assertFalse(field.canBeWed(4, YearTick.JUN1, machinePlants))
        assertFalse(field.canBeWed(6, YearTick.JUL1, machinePlants))
    }

    @Test
    fun `missedIrrigation basic - moisture difference less than 100`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.WHEAT.initialHarvestEstimate
        field.currentMoisture = FieldPlantType.WHEAT.moistureRequired - 1 // one less than required

        plant.setProgress(ActionType.SOWING, 7, YearTick.OCT1)

        val currentTick = 10
        val currentYearTick = YearTick.NOV2

        assertFalse(field.missedIrrigation(currentTick, currentYearTick))
    }

    @Test
    fun `missedIrrigation basic - moisture difference exactly 100`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.WHEAT.initialHarvestEstimate
        field.currentMoisture = FieldPlantType.WHEAT.moistureRequired - 100 // 100 less than required

        plant.setProgress(ActionType.SOWING, 7, YearTick.OCT1)

        val currentTick = 10
        val currentYearTick = YearTick.NOV2

        assertTrue(field.missedIrrigation(currentTick, currentYearTick))
    }

    @Test
    fun `missedIrrigation basic - moisture difference greater 100`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.WHEAT.initialHarvestEstimate
        field.currentMoisture = FieldPlantType.WHEAT.moistureRequired - 300 // 300 less than required

        plant.setProgress(ActionType.SOWING, 7, YearTick.OCT1)

        val currentTick = 10
        val currentYearTick = YearTick.NOV2

        assertTrue(field.missedIrrigation(currentTick, currentYearTick))
    }

    @Test
    fun `missedIrrigation basic - irrigation this tick`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.WHEAT.initialHarvestEstimate
        field.currentMoisture = FieldPlantType.WHEAT.moistureRequired - 300 // 300 less than required

        plant.setProgress(ActionType.SOWING, 7, YearTick.OCT1)

        val currentTick = 10
        val currentYearTick = YearTick.NOV2

        plant.setProgress(ActionType.IRRIGATING, currentTick, currentYearTick)

        assertFalse(field.missedIrrigation(currentTick, currentYearTick))
    }

    @Test
    fun `missedIrrigation basic - harvested this tick`() {
        val field = newField()
        val plant = FieldPlant(FieldPlantType.WHEAT)
        field.plant = plant
        field.currentHarvestEstimate = FieldPlantType.WHEAT.initialHarvestEstimate
        field.currentMoisture = FieldPlantType.WHEAT.moistureRequired - 300 // 300 less than required

        val currentTick = 15
        val currentYearTick = YearTick.JUN1
        plant.setProgress(ActionType.HARVESTING, currentTick, currentYearTick)
        assertFalse(field.missedIrrigation(currentTick, currentYearTick))
    }
}
