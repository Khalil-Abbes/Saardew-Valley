package de.unisaarland.cs.se.selab.incident

import de.unisaarland.cs.se.selab.simulation.YearTick

/**
 * Abstract class to represent incidents in the simulation
 */
abstract class Incident(val id: Int, val tick: Int) {

    /**
     * Performs the incident at the given tick
     */
    abstract fun perform(currentTick: Int, currentYearTick: YearTick)

    /**
     * String representation of the incident
     */
    abstract override fun toString(): String
}
