package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.simulation.YearTick

/**
 * Plant class with common logic
 */
open class Plant(
    open val type: PlantType
) {
    var progress: Progress = Progress()

    /**
     * checks if the plant is in blooming window
     */
    fun isInBloomWindow(currentTick: Int, yearTick: YearTick): Boolean {
        when (val bloomer = type.bloomWindow) {
            is FixedBlooming -> {
                return bloomer.window.contains(yearTick)
            }
            is RelativeBlooming -> {
                if (progress.sowing.isEmpty()) return false
                val bloomingStart = bloomer.afterSowingTicks + progress.sowing.last().first + 1
                val bloomingEnd = bloomingStart + bloomer.durationTicks
                return currentTick in bloomingStart..<bloomingEnd
            }
            else -> {
                return false
            }
        }
    }

    /**
     * sets the progress of the plant
     */
    open fun setProgress(action: ActionType, currentTick: Int, currentYearTick: YearTick) {
        progress.addEntry(action, currentTick, currentYearTick)
    }
}
