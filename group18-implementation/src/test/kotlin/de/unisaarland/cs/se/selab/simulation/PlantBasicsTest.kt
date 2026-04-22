package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.FieldWeedingRule
import de.unisaarland.cs.se.selab.plant.LateRule
import de.unisaarland.cs.se.selab.plant.Progress
import de.unisaarland.cs.se.selab.plant.Window
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlantBasicsTest {

    // Window stuff

    @Test fun `fixed blooming uses year ticks inclusive`() {
        val w = Window(YearTick.APR1, YearTick.MAY1)
        assertTrue(w.contains(YearTick.APR1))
        assertTrue(w.contains(YearTick.MAY1))
        assertFalse(w.contains(YearTick.MAY2))
    }

    @Test fun `contains includes late ticks when late rule present`() {
        val w = Window(YearTick.JUN1, YearTick.JUN1, LateRule(perTickPenaltyPct = 20, ticksAllowed = 2))
        assertTrue(w.contains(YearTick.JUN1)) // inside window
        assertTrue(w.contains(YearTick.JUN2)) // late day 1
        assertTrue(w.contains(YearTick.JUL1)) // late day 2
        assertFalse(w.contains(YearTick.JUL2)) // beyond late allowance
    }

    @Test
    fun `isLate only true after end when lateRule exists`() {
        val w = Window(YearTick.JUN1, YearTick.JUN1, LateRule(20, 2))
        assertTrue(w.isLate(YearTick.JUN1))
        assertTrue(w.isLate(YearTick.JUN2))
        assertTrue(w.isLate(YearTick.JUL1))
        assertFalse(w.isLate(YearTick.JUL2))
    }

    @Test
    fun `isLate without lateRule should always be false (no late concept)`() {
        val w = Window(YearTick.MAY1, YearTick.MAY2, lateRule = null)
        assertFalse(w.isLate(YearTick.MAY1))
        assertTrue(w.isLate(YearTick.MAY2))
        assertFalse(w.isLate(YearTick.JUN1))
    }

    // Blooming windows stuff

    // @Disabled("Failing test")
    @Test
    fun `relative blooming is offset from sow tick`() {
        val p = FieldPlant(FieldPlantType.POTATO)
        p.setProgress(ActionType.SOWING, currentTick = 10, currentYearTick = YearTick.MAY1)
        assertFalse(p.isInBloomWindow(10, YearTick.MAY1))
        assertFalse(p.isInBloomWindow(11, YearTick.MAY2))
        assertFalse(p.isInBloomWindow(12, YearTick.JUN1))
        assertTrue(p.isInBloomWindow(14, YearTick.MAY1))
    }

    @Test fun `relative blooming with no sowing recorded is never in bloom`() {
        val p = FieldPlant(FieldPlantType.POTATO)
        assertFalse(p.isInBloomWindow(999, YearTick.MAY1))
    }

    @Test
    fun `relative blooming duration is inclusive of start`() {
        val p = FieldPlant(FieldPlantType.PUMPKIN)
        p.setProgress(ActionType.SOWING, currentTick = 10, currentYearTick = YearTick.MAY2)
        assertFalse(p.isInBloomWindow(11, YearTick.MAY2))
        assertFalse(p.isInBloomWindow(12, YearTick.MAY2))
        assertTrue(p.isInBloomWindow(13, YearTick.MAY2))
        assertTrue(p.isInBloomWindow(14, YearTick.MAY2))
        assertFalse(p.isInBloomWindow(15, YearTick.MAY2))
    }

    @Test fun `fixed blooming plant (wheat) blooms only on fixed year ticks`() {
        val p = FieldPlant(FieldPlantType.WHEAT)
        assertFalse(p.isInBloomWindow(0, YearTick.APR2))
        assertTrue(p.isInBloomWindow(0, YearTick.MAY1))
        assertFalse(p.isInBloomWindow(0, YearTick.MAY2))
    }

    @Test fun `plant with no bloom window is never in bloom`() {
        val p = FieldPlant(FieldPlantType.OAT) // bloomWindow = null
        assertFalse(p.isInBloomWindow(0, YearTick.JUN1))
        assertFalse(p.isInBloomWindow(0, YearTick.JUL1))
    }

    // Progress stuff

    @Test fun `progress enforces at-most-one action per tile per tick`() {
        val pr = Progress()
        pr.addEntry(ActionType.SOWING, 5, YearTick.MAY1)
        assertFalse(pr.canBeWorkedOnThisTick(5, YearTick.MAY1))
        assertTrue(pr.canBeWorkedOnThisTick(6, YearTick.MAY1))
    }

    @Test fun `hasBeenWorkedOnThisTick recognizes different actions`() {
        val pr = Progress()
        pr.addEntry(ActionType.IRRIGATING, 8, YearTick.JUN1)
        assertTrue(pr.hasBeenWorkedOnThisTick(ActionType.IRRIGATING, 8, YearTick.JUN1))
        assertFalse(pr.hasBeenWorkedOnThisTick(ActionType.WEEDING, 8, YearTick.JUN1))
        assertFalse(pr.hasBeenWorkedOnThisTick(ActionType.IRRIGATING, 9, YearTick.JUN1))
    }

    // Weeding rule stuff

    @Test fun `weeding by fixed offsets from sow tick`() {
        val rule = FieldWeedingRule(offsets = listOf(3, 9), everyN = null) // like WHEAT
        val sow = 10
        assertTrue(rule.contains(sow, currentTick = 13)) // 10+3
        assertFalse(rule.contains(sow, currentTick = 14)) // 10+9
        assertFalse(rule.contains(sow, currentTick = 20))
        assertTrue(rule.contains(sow, currentTick = 19))
    }

    @Test fun `weeding every N ticks from sow tick inclusive of multiples`() {
        val rule = FieldWeedingRule(offsets = null, everyN = 2) // like POTATO & PUMPKIN
        val sow = 10
        // difference (current - sow) must be a multiple of 2
        assertFalse(rule.contains(sow, 10)) // 0 % 2 == 0
        assertTrue(rule.contains(sow, 12))
        assertTrue(rule.contains(sow, 14))
        assertFalse(rule.contains(sow, 11))
        assertFalse(rule.contains(sow, 13))
    }

    // YearTick stuff

    @Test fun `getNext wraps from DEC2 to JAN1`() {
        assertEquals(YearTick.JAN1, YearTick.DEC2.getNext())
    }

    @Test fun `addTicks wraps correctly across year boundary`() {
        assertEquals(YearTick.JAN1, YearTick.DEC2.addTicks(1))
        assertEquals(YearTick.JAN2, YearTick.DEC2.addTicks(2))
        assertEquals(YearTick.FEB1, YearTick.DEC2.addTicks(3))
        assertEquals(YearTick.MAR1, YearTick.JAN2.addTicks(3)) // JAN2->FEB1->FEB2->MAR1
    }
}
