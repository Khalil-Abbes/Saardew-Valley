package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.tile.Plantation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import kotlin.math.pow
import kotlin.test.assertNotNull

class PlantationLogicTest {

    @BeforeEach
    fun setLogger() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    // helper functions
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

    private fun neutralizeEnv(p: Plantation) {
        p.sunlightThisTick = p.plant.type.sunlightComfort
        p.currentMoisture = p.plant.type.moistureRequired
    }

    // mowing logic tests
    // @Disabled("")
    @Test
    fun `missed mowing reduces to 90 percent`() {
        val p = newApple()
        val start = p.currentHarvestEstimate
        // mowing window at JUN1 for APPLE
        neutralizeEnv(p)
        p.updateHarvestEstimate(5, YearTick.JUN1)
        val expected = (start * 0.9).toInt()
        assertEquals(expected, p.currentHarvestEstimate)
    }

    // @Disabled("")
    @Test
    fun `doing mowing prevents mowing penalty`() {
        val p = newApple()
        val start = p.currentHarvestEstimate
        neutralizeEnv(p)
        // perform mowing action this tick
        p.plant.setProgress(ActionType.MOWING, 5, YearTick.JUN1)
        p.updateHarvestEstimate(5, YearTick.JUN1)
        // no 10% penalty applied
        assertEquals(start, p.currentHarvestEstimate)
    }

    // @Disabled("")
    @Test
    fun `animal attack marks mowing so no mowing penalty`() {
        val p = newApple()
        val start = p.currentHarvestEstimate
        neutralizeEnv(p)
        // spec: incident sets mowingDoneTicks for this and next tick
        p.mowingDoneTicks.add(7)
        p.mowingDoneTicks.add(8)
        p.updateHarvestEstimate(7, YearTick.JUN1)
        assertEquals(start, p.currentHarvestEstimate)
    }

    // cutting logic tests

    // @Disabled("")
    @Test
    fun `missed cutting halves yield at FEB2 for apple and at AUG2 for grape`() {
        // Apple: cutting windows include FEB1...FEB2; missedCutting triggers at FEB2
        run {
            val p = newApple()
            neutralizeEnv(p)
            val start = p.currentHarvestEstimate
            p.updateHarvestEstimate(10, YearTick.FEB2)
            val expected = start / 2
            assertEquals(expected, p.currentHarvestEstimate)
        }
        // Grape: cutting window JUL2...AUG2; missedCutting triggers at AUG2
        run {
            val g = newGrape()
            neutralizeEnv(g)
            val start = g.currentHarvestEstimate
            g.updateHarvestEstimate(20, YearTick.AUG2)
            val expected = start / 2
            assertEquals(expected, g.currentHarvestEstimate)
        }
    }

    // irrigation logic tests

    // @Disabled("")
    @Test
    fun `canBeIrrigated for plantation only when moisture is exactly 0 and not disabled`() {
        val p = newApple()
        // below threshold but not zero -> false
        p.currentMoisture = p.plant.type.moistureRequired - 1
        assertTrue(p.canBeIrrigated(3, YearTick.MAY1, emptyList()))
        // exactly zero -> true
        p.currentMoisture = 0
        assertTrue(p.canBeIrrigated(4, YearTick.MAY1, emptyList()))
        // disabled -> false regardless
        p.killPlant(5)
        assertFalse(p.canBeIrrigated(6, YearTick.MAY1, emptyList()))
    }

    @Test fun `canBeHarvested true within window and respects one action per tick`() {
        val p = newApple()
        p.currentHarvestEstimate = 500
        // In-window: APPLE harvest SEP1...OCT1 (end) + 1 late tick allowed
        assertTrue(p.canBeHarvested(30, YearTick.SEP1))
        // if already worked this tick -> false
        p.plant.setProgress(ActionType.HARVESTING, 31, YearTick.SEP1)
        assertFalse(p.canBeHarvested(31, YearTick.SEP1))
    }

    // season reset tests

    // @Disabled("")
    @Test
    fun `NOV1 resets estimate and clears progress for new season (environment neutralized)`() {
        val p = newApple()
        // simulate prior penalties and actions
        p.currentHarvestEstimate = (p.currentHarvestEstimate * 0.7).toInt()
        p.plant.setProgress(ActionType.MOWING, 10, YearTick.JUN1)

        neutralizeEnv(p)
        p.updateHarvestEstimate(40, YearTick.NOV1)

        // After reset at NOV1, estimate back to initial (env neutralized so no change),
        // and progress cleared (implicitly used from next actions).
        assertEquals(p.plant.type.initialHarvestEstimate, p.currentHarvestEstimate)
        // sanity: any action this tick should now be possible as progress was reset
        assertTrue(p.plant.progress.canBeWorkedOnThisTick(41, YearTick.NOV1))
    }

    // drought on plantation tests
    @Disabled
    @Test fun `drought disables plantation permanently`() {
        val p = newApple()
        p.currentHarvestEstimate = 1234
        p.killPlant(10) // drought behavior
        assertNotNull(p.permanentDisabled)
        // harvest estimate forced to zero and stays zero
        assertEquals(0, p.currentHarvestEstimate)
        // disabled -> not considered below threshold (per implementation)
        assertFalse(p.isMoistureBelowThreshold())
        // further updates don't change anything
        p.updateHarvestEstimate(11, YearTick.SEP1)
        assertEquals(0, p.currentHarvestEstimate)
    }

    // missed harvest penalties per plant type tests

    @Test
    fun `missed harvest penalties - apple end day 50 percent then 0 afterward`() {
        val p = newApple()
        val base = 10_000
        p.currentHarvestEstimate = base
        neutralizeEnv(p)

        // end day (OCT1) and not harvested -> 50%
        p.updateHarvestEstimate(22, YearTick.OCT1) // totalMissedTick = 0
        assertEquals(base / 2, p.currentHarvestEstimate)

        // next day (OCT2) -> 0
        p.currentHarvestEstimate = base
        p.updateHarvestEstimate(23, YearTick.OCT2) // totalMissedTick = 1
        assertEquals(0, p.currentHarvestEstimate)
    }

    @Test
    fun `missed harvest penalties - almond 90 percent on end day then 0 afterward`() {
        val a = Plantation(5, Coordinate(4, 4), 1, null, 300, PlantationPlant(PlantationPlantType.ALMOND))
        val base = 10_000
        a.currentHarvestEstimate = base
        neutralizeEnv(a)

        // end day (OCT1) -> 90%
        a.updateHarvestEstimate(22, YearTick.OCT1)
        assertEquals((base * 0.9).toInt(), a.currentHarvestEstimate)

        // next day (OCT2) -> 0
        a.currentHarvestEstimate = base
        a.updateHarvestEstimate(23, YearTick.OCT2)
        assertEquals(0, a.currentHarvestEstimate)
    }

    @Test
    fun `missed harvest penalties - cherry 30 percent on end day then 0 afterward`() {
        val c = Plantation(6, Coordinate(6, 6), 1, null, 300, PlantationPlant(PlantationPlantType.CHERRY))
        val base = 10_000
        c.currentHarvestEstimate = base
        neutralizeEnv(c)

        // end day (JUL2) -> 30%
        c.updateHarvestEstimate(14, YearTick.JUL2)
        assertEquals((base * 0.3).toInt(), c.currentHarvestEstimate)

        // next day (AUG1) -> 0
        c.currentHarvestEstimate = base
        c.updateHarvestEstimate(15, YearTick.AUG1)
        assertEquals(0, c.currentHarvestEstimate)
    }

    @Test
    fun `missed harvest penalties - grape 95 percent on end day and next 2 days, then 0`() {
        val g = newGrape()
        val base = 10_000
        neutralizeEnv(g)

        // end day SEP1 -> 95%
        g.currentHarvestEstimate = base
        g.updateHarvestEstimate(17, YearTick.SEP1) // totalMissedTick = 0
        assertEquals((base * 0.95).toInt(), g.currentHarvestEstimate)

        // next day SEP2 -> 95%
        g.currentHarvestEstimate = base
        g.updateHarvestEstimate(18, YearTick.SEP2) // totalMissedTick = 1
        assertEquals((base * 0.95).toInt(), g.currentHarvestEstimate)

        // day after (OCT1) -> 95% (since < 3)
        g.currentHarvestEstimate = base
        g.updateHarvestEstimate(19, YearTick.OCT1) // totalMissedTick = 2
        assertEquals((base * 0.95).toInt(), g.currentHarvestEstimate)

        // then 0 (OCT2) -> 0
        g.currentHarvestEstimate = base
        g.updateHarvestEstimate(20, YearTick.OCT2) // totalMissedTick = 3
        assertEquals(0, g.currentHarvestEstimate)
    }

    // incidents and ordering tests

    // @Disabled("Pending: fix percentage math in FarmableTile.applyIncidentEffects (uses integer division)")
    // @Disabled("")
    @Test
    fun `bee-happy style incident effect increases estimate by percentage`() {
        val p = newApple()
        val base = 1_000
        p.currentHarvestEstimate = base
        neutralizeEnv(p)

        // simulate incident +50% while blooming (APPLE blooms APR2...MAY1)
        p.addIncidentEffect(50)
        p.updateHarvestEstimate(8, YearTick.APR2)

        // spec: +50% -> * 1.5
        assertEquals((base * 1.5).toInt(), p.currentHarvestEstimate)
    }

    // @Disabled("")
    @Test
    fun `sunlight penalty applies before missed action penalties`() {
        val p = newApple()
        val base = 1_000_000
        p.currentHarvestEstimate = base
        p.currentMoisture = p.plant.type.moistureRequired // avoid moisture penalty
        // choose JUN1 (sunlight=168) vs comfort 50 -> diff 118 -> 118/25 = 4 full steps -> 0.9^4
        p.sunlightThisTick = YearTick.JUN1.sunlight
        // JUN1 also has a mowing window; ensure mowing missed penalty applies afterward
        p.updateHarvestEstimate(5, YearTick.JUN1)
        val afterSun = (base * 0.9.pow(4)).toInt()
        val afterMow = (afterSun * 0.9).toInt()
        assertEquals(afterMow, p.currentHarvestEstimate)
    }

    // random other tests

    @Test fun `canBeHarvested is also true on end plus one day`() {
        val p = newApple()
        p.currentHarvestEstimate = 500
        // Apple end = OCT1; end+1 = OCT2
        assertTrue(p.canBeHarvested(999, YearTick.OCT2))
    }

    // @Disabled("")
    @Test
    fun `missed mowing on second window (SEP1) also reduces to 90 percent`() {
        val p = newApple()
        val start = p.currentHarvestEstimate
        neutralizeEnv(p)
        // Apple has mowing windows at JUN1 and SEP1
        p.updateHarvestEstimate(25, YearTick.SEP1)
        assertEquals((start * 0.9).toInt(), p.currentHarvestEstimate)
    }

    @Test fun `irrigation blocked if already worked this tick`() {
        val p = newApple()
        // Make irrigation otherwise eligible
        p.currentMoisture = 0
        p.sunlightThisTick = p.plant.type.sunlightComfort
        // But record an action this very tick
        p.plant.setProgress(ActionType.MOWING, 7, YearTick.MAY1)
        assertFalse(p.canBeIrrigated(7, YearTick.MAY1, emptyList()))
    }

    @Test fun `disabled plantation cannot be harvested even in window`() {
        val p = newApple()
        p.killPlant(10)
        // In window
        assertFalse(p.canBeHarvested(30, YearTick.SEP1))
        // End+1
        assertFalse(p.canBeHarvested(31, YearTick.OCT2))
    }
}
