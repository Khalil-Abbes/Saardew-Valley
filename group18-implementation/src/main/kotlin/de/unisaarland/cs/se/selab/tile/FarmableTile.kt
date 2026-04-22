package de.unisaarland.cs.se.selab.tile

import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.YearTick
import kotlin.math.max

const val MOISTURE_REDUCTION_WITH_PLANTS = 100
const val MOISTURE_REDUCTION_WITHOUT_PLANTS = 70
const val SUNLIGHT_PENALTY_CHECK = 25
const val MOISTURE_PENALTY = 50
const val MOISTURE_PENALTY_CHECK = 100
const val DIVIDE_BY_100 = 100

/**
 * Abstract class that implements the common logic of fields and plantations
 */
abstract class FarmableTile<P : Plant>(
    id: Int,
    coordinate: Coordinate,
    category: TileCategory,
    farm: Int,
    direction: Direction?,
    val moistureCapacity: Int
) : Tile(
    id,
    coordinate,
    category,
    shed = false,
    farm,
    direction
) {
    abstract val plant: P?
    abstract var currentHarvestEstimate: Int
    var currentMoisture: Int = moistureCapacity
    var sunlightThisTick: Int = 0

    // percentage effects queued for this tick (ie [−50, +10, +20])
    val incidentOrderListThisTick: MutableList<Int> = mutableListOf()

    /**
     * helper for incidents
     */
    open fun addIncidentEffect(pct: Int) {
        incidentOrderListThisTick.add(pct)
    }

    /**
     * delete incidents
     */
    open fun clearIncidentEffects() {
        incidentOrderListThisTick.clear()
    }

    /**
     * to kill the plant in animal attack or drought
     */
    abstract fun killPlant(currentTick: Int)

    /**
     * update the sunlight at the start of the tick
     */
    fun updateSunlight(currentYearTick: YearTick) {
        this.sunlightThisTick = currentYearTick.sunlight
    }

    /**
     * apply the penalty for environment such as sunlight and moisture
     */
    fun applyEnvironmentPenalty() {
        if (this.currentHarvestEstimate == 0) {
            return
        }
        val plantType = plant?.type ?: return
        val comfort = plantType.sunlightComfort
        val harvestAfterSunlight = if (sunlightThisTick > comfort) {
            // Sunlight penalty: 10% reduction per full 25h away from comfort
            val sunlightDifference = sunlightThisTick - comfort
            var multiplier = sunlightDifference / SUNLIGHT_PENALTY_CHECK
            while (multiplier > 0) {
                currentHarvestEstimate = (currentHarvestEstimate * NINETY_PERCENT).toInt()
                multiplier -= 1
            }
            currentHarvestEstimate
        } else {
            currentHarvestEstimate
        }
        val moisturePenalty =
            if (currentMoisture < plantType.moistureRequired) {
                // Moisture penalty: only for a deficit (too little moisture)
                val moistureDeficit = plantType.moistureRequired - currentMoisture
                moistureDeficit / MOISTURE_PENALTY_CHECK * MOISTURE_PENALTY
            } else {
                0
            }

        // Apply both penalties and remove the harvest directly if current moisture is 0
        currentHarvestEstimate =
            if (currentMoisture == 0) {
                0
            } else {
                max(0, harvestAfterSunlight - moisturePenalty)
            }
    }

    /**
     * apply all the incident effect to current harvest
     */
    fun applyIncidentEffects() {
        if (this.currentHarvestEstimate == 0) {
            clearIncidentEffects()
        }
        for (incident in this.incidentOrderListThisTick) {
            val modifierEffect = 1 + (incident.toDouble() / DIVIDE_BY_100.toDouble())
            currentHarvestEstimate = max(0.toDouble(), currentHarvestEstimate * modifierEffect).toInt()
        }
        clearIncidentEffects()
    }

    /**
     * check if tile can be harvested
     */
    abstract fun canBeHarvested(currentTick: Int, currentYearTick: YearTick): Boolean

    /**
     * checks if the tile moisture is below a plant threshold
     */
    abstract fun isMoistureBelowThreshold(): Boolean

    /**
     * check if the tile needs irrigation
     */
    abstract fun canBeIrrigated(currentTick: Int, currentYearTick: YearTick, machinePlants: List<PlantType>): Boolean

    /**
     * last simulation step
     */
    abstract fun updateHarvestEstimate(currentTick: Int, currentYearTick: YearTick)

    /**
     * Checks if the tile can be pollinated by bees
     */
    abstract fun canBePollinated(currentTick: Int, currentYearTick: YearTick): Boolean
}
