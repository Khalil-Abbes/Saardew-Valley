package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.simulation.YearTick

/**
 * data class for progress
 */
class Progress {
    val sowing: MutableList<Pair<Int, YearTick>> = mutableListOf()
    val weeding: MutableList<Pair<Int, YearTick>> = mutableListOf()
    val mowing: MutableList<Pair<Int, YearTick>> = mutableListOf()
    val cutting: MutableList<Pair<Int, YearTick>> = mutableListOf()
    val harvesting: MutableList<Pair<Int, YearTick>> = mutableListOf()
    val irrigating: MutableList<Pair<Int, YearTick>> = mutableListOf()

    /**
     * adds the corresponding fields
     */
    fun addEntry(actionType: ActionType, currentTick: Int, currentYearTick: YearTick) {
        when (actionType) {
            ActionType.SOWING -> sowing.add(Pair(currentTick, currentYearTick))
            ActionType.MOWING -> mowing.add(Pair(currentTick, currentYearTick))
            ActionType.WEEDING -> weeding.add(Pair(currentTick, currentYearTick))
            ActionType.CUTTING -> cutting.add(Pair(currentTick, currentYearTick))
            ActionType.HARVESTING -> harvesting.add(Pair(currentTick, currentYearTick))
            ActionType.IRRIGATING -> irrigating.add(Pair(currentTick, currentYearTick))
        }
    }

    /**
     * checks if the tile haven't had any action in the tick
     */
    fun canBeWorkedOnThisTick(currentTick: Int, currentYearTick: YearTick): Boolean {
        return !(
            sowing.contains(Pair(currentTick, currentYearTick)) ||
                cutting.contains(Pair(currentTick, currentYearTick)) ||
                harvesting.contains(Pair(currentTick, currentYearTick)) ||
                irrigating.contains(Pair(currentTick, currentYearTick)) ||
                weeding.contains(Pair(currentTick, currentYearTick)) ||
                mowing.contains(Pair(currentTick, currentYearTick))
            )
    }

    /**
     * checks if the tile haven't had any action in the tick
     */
    fun hasBeenWorkedOnThisTick(actionType: ActionType, currentTick: Int, currentYearTick: YearTick): Boolean {
        val result = when (actionType) {
            ActionType.SOWING -> sowing.contains(Pair(currentTick, currentYearTick))
            ActionType.MOWING -> mowing.contains(Pair(currentTick, currentYearTick))
            ActionType.WEEDING -> weeding.contains(Pair(currentTick, currentYearTick))
            ActionType.CUTTING -> cutting.contains(Pair(currentTick, currentYearTick))
            ActionType.IRRIGATING -> irrigating.contains(Pair(currentTick, currentYearTick))
            ActionType.HARVESTING -> harvesting.contains(Pair(currentTick, currentYearTick))
        }
        return result
    }
}
