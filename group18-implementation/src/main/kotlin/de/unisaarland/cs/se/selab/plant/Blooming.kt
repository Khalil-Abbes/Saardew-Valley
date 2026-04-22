package de.unisaarland.cs.se.selab.plant

/**
 * data classes for blooming action
 */
sealed interface Blooming

/**
 * blooming with fixed window
 */
data class FixedBlooming(val window: Window) : Blooming

/**
 * blooming relative to ticks
 */
data class RelativeBlooming(val afterSowingTicks: Int, val durationTicks: Int) : Blooming
