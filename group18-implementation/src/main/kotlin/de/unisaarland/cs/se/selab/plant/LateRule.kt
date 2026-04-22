package de.unisaarland.cs.se.selab.plant

/**
 * specifies if an action can also be done later than the fixed window
 */
data class LateRule(val perTickPenaltyPct: Int, val ticksAllowed: Int)
