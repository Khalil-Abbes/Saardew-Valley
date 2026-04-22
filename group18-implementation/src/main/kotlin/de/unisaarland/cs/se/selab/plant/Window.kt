package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.simulation.YearTick

/**
 * Class to represent action intervals for plants
 */
class Window(val startTick: YearTick, val endTick: YearTick, val lateRule: LateRule? = null) {

    /**
     * check if the yearTick is in the window interval
     */
    fun contains(yearTick: YearTick): Boolean {
        val lateTick = lateRule?.ticksAllowed ?: 0
        return yearTick.tick in startTick.tick..(endTick.tick + lateTick)
    }

    /**
     * check if the yearTick is late
     */
    fun isLate(yearTick: YearTick): Boolean {
        lateRule ?: return yearTick.tick == endTick.tick
        return yearTick.tick in endTick.tick..endTick.tick + lateRule.ticksAllowed
    }
}
