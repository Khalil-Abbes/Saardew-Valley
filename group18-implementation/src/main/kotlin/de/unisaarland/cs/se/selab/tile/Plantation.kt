package de.unisaarland.cs.se.selab.tile

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.plant.Pollination
import de.unisaarland.cs.se.selab.plant.Progress
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.YearTick
import kotlin.math.max

const val NINETY_PERCENT = 0.9
const val NINETY_FIVE_PERCENT = 0.95
const val THIRTY_PERCENT = 0.3

/**
 * class for the plantation tile type
 */
class Plantation(
    id: Int,
    coordinate: Coordinate,
    farm: Int,
    direction: Direction?,
    moistureCapacity: Int,
    override var plant: PlantationPlant
) : FarmableTile<PlantationPlant>(
    id,
    coordinate,
    TileCategory.PLANTATION,
    farm,
    direction,
    moistureCapacity
) {

    override var currentHarvestEstimate: Int = this.plant.type.initialHarvestEstimate
    val mowingDoneTicks: MutableSet<Int> = mutableSetOf()
    var permanentDisabled: Int? = null
    var nextCycleToCut: Int = 0

    // kill plants on plantations when drought
    override fun killPlant(currentTick: Int) {
        if (this.permanentDisabled == null) {
            this.permanentDisabled = currentTick
        }
    }

    /**
     * Reduces soil moisture.
     * Should be called at the start of each tick.
     * Takes into account the presence of a plant on the plantation.
     */
    fun reduceSoilMoisture() {
        if (permanentDisabled != null) {
            this.currentMoisture = max(this.currentMoisture - MOISTURE_REDUCTION_WITHOUT_PLANTS, 0)
        } else {
            this.currentMoisture = max(this.currentMoisture - MOISTURE_REDUCTION_WITH_PLANTS, 0)
        }
    }

    /**
     * check if the current moisture is below the plant threshold
     */
    override fun isMoistureBelowThreshold(): Boolean {
        if (permanentDisabled != null) {
            return false
        }
        return this.currentMoisture < this.plant.type.moistureRequired
    }

    /**
     * check if we can harvest this tick
     */
    override fun canBeHarvested(currentTick: Int, currentYearTick: YearTick): Boolean {
        return permanentDisabled == null &&
            plant.type.harvestWindow.contains(currentYearTick) &&
            currentHarvestEstimate > 0 &&
            this.plant.progress.canBeWorkedOnThisTick(currentTick, currentYearTick)
    }

    /**
     * check if the plantation can be cut
     */
    fun canBeCut(currentTick: Int, currentYearTick: YearTick): Boolean {
        if (this.permanentDisabled == null && currentYearTick == YearTick.NOV1) {
            return this.plant.type.cuttingWindows.any {
                it.contains(currentYearTick)
            } &&
                this.plant.progress.canBeWorkedOnThisTick(currentTick, currentYearTick) &&
                currentTick >= nextCycleToCut
        }
        return this.plant.type.cuttingWindows.any {
            it.contains(currentYearTick)
        } &&
            permanentDisabled == null &&
            this.plant.progress.canBeWorkedOnThisTick(currentTick, currentYearTick) &&
            currentTick >= nextCycleToCut &&
            currentHarvestEstimate > 0
    }

    /**
     * check if the plantation can be irrigated
     */
    override fun canBeIrrigated(currentTick: Int, currentYearTick: YearTick, machinePlants: List<PlantType>): Boolean {
        return this.isMoistureBelowThreshold() &&
            this.permanentDisabled == null &&
            this.plant.progress.canBeWorkedOnThisTick(currentTick, currentYearTick)
    }

    /**
     * update the harvest estimate by applying each penalty and effect
     */
    override fun updateHarvestEstimate(currentTick: Int, currentYearTick: YearTick) {
        val originalAmount = this.currentHarvestEstimate

        if (currentYearTick == YearTick.NOV1 && permanentDisabled == null) {
            this.currentHarvestEstimate = this.plant.type.initialHarvestEstimate
            val oldProgress = this.plant.progress
            this.plant.progress = Progress()

            if (oldProgress.cutting.contains(Pair(currentTick, currentYearTick))) {
                this.plant.progress.addEntry(ActionType.CUTTING, currentTick, currentYearTick)
            }

            applyEnvironmentPenalty()
            applyMissedActionPenalty(currentTick, currentYearTick)
            applyIncidentEffects()
        } else {
            applyEnvironmentPenalty()
            applyMissedActionPenalty(currentTick, currentYearTick)
            applyIncidentEffects()
        }
        if (originalAmount != this.currentHarvestEstimate) {
            Logger.logHarvestEstimateChange(this.id, this.currentHarvestEstimate, this.plant.type)
        }
    }

    /**
     * applying the penalty for each missed action
     */
    fun applyMissedActionPenalty(currentTick: Int, currentYearTick: YearTick) {
        if (this.permanentDisabled != null) {
            return
        }

        val missedActionList = this.getMissedActions(currentTick, currentYearTick)

        for (missedAction in missedActionList) {
            this.currentHarvestEstimate = when (missedAction) {
                ActionType.CUTTING -> {
                    max(0, this.currentHarvestEstimate / 2)
                }
                ActionType.MOWING -> {
                    max(0, (this.currentHarvestEstimate * NINETY_PERCENT).toInt())
                }
                ActionType.HARVESTING -> {
                    applyMissedHarvestPenalty(currentYearTick)
                }
                else -> {
                    this.currentHarvestEstimate
                }
            }
        }

        if (missedActionList.isNotEmpty()) {
            Logger.logMissedActions(this.id, missedActionList)
        }
    }

    /**
     * apply the missed harvesting penalty that is unique for each plant type
     */
    fun applyMissedHarvestPenalty(currentYearTick: YearTick): Int {
        val totalMissedTick = currentYearTick.tick - this.plant.type.harvestWindow.endTick.tick

        val result = when (this.plant.type) {
            PlantationPlantType.APPLE -> {
                if (totalMissedTick == 0) {
                    this.currentHarvestEstimate / 2
                } else {
                    0
                }
            }
            PlantationPlantType.ALMOND -> {
                if (totalMissedTick == 0) {
                    (this.currentHarvestEstimate * NINETY_PERCENT).toInt()
                } else {
                    0
                }
            }
            PlantationPlantType.CHERRY -> {
                if (totalMissedTick == 0) {
                    (this.currentHarvestEstimate * THIRTY_PERCENT).toInt()
                } else {
                    0
                }
            }
            PlantationPlantType.GRAPE -> {
                if (totalMissedTick < 3) {
                    (this.currentHarvestEstimate * NINETY_FIVE_PERCENT).toInt()
                } else {
                    0
                }
            }
        }
        return result
    }

    /**
     * check if we missed Harvesting and add it to the list if we did
     */
    private fun missedHarvesting(
        currentYearTick: YearTick,
        missedActionList: MutableList<ActionType>
    ) {
        if (this.plant.type.harvestWindow.isLate(currentYearTick) &&
            this.plant.progress.harvesting.isEmpty() &&
            this.currentHarvestEstimate > 0
        ) {
            missedActionList.add(ActionType.HARVESTING)
        }
    }

    private fun getMissedActions(
        currentTick: Int,
        currentYearTick: YearTick
    ): List<ActionType> {
        val missedActionsList = mutableListOf<ActionType>()

        missedCutting(currentTick, currentYearTick, missedActionsList)
        missedMowing(currentTick, currentYearTick, missedActionsList)
        if (missedIrrigation(currentTick, currentYearTick)) {
            missedActionsList.add(ActionType.IRRIGATING)
        }
        missedHarvesting(currentYearTick, missedActionsList)

        return missedActionsList
    }

    private fun missedIrrigation(currentTick: Int, currentYearTick: YearTick): Boolean {
        if (this.isMoistureBelowThreshold() &&
            !this.plant.progress.hasBeenWorkedOnThisTick(ActionType.IRRIGATING, currentTick, currentYearTick)
        ) {
            return this.plant.progress.harvesting.isEmpty() &&
                (
                    this.permanentDisabled == null ||
                        this.permanentDisabled == currentTick
                    ) &&
                plant.type.moistureRequired - currentMoisture >= MOISTURE_PENALTY_CHECK
        }
        return false
    }

    /**
     * check if we missed cutting and add it to the list if we did
     */
    private fun missedCutting(
        currentTick: Int,
        currentYearTick: YearTick,
        missedActionList: MutableList<ActionType>
    ) {
        when (this.plant.type) {
            PlantationPlantType.GRAPE ->
                if (currentYearTick.tick == YearTick.AUG2.tick &&
                    currentTick >= nextCycleToCut &&
                    this.currentHarvestEstimate > 0
                ) {
                    nextCycleToCut = currentYearTick.getNextNovember() + currentTick
                    missedActionList.add(ActionType.CUTTING)
                }
            else ->
                if (currentYearTick.tick == YearTick.FEB2.tick &&
                    currentTick >= nextCycleToCut &&
                    this.currentHarvestEstimate > 0
                ) {
                    missedActionList.add(ActionType.CUTTING)
                    nextCycleToCut = currentYearTick.getNextNovember() + currentTick
                }
        }
    }

    /**
     * check if we missed Mowing and add it to the list if we did
     */
    private fun missedMowing(
        currentTick: Int,
        currentYearTick: YearTick,
        missedActionList: MutableList<ActionType>
    ) {
        if (this.plant.type.mowingWindows.any {
                it.contains(currentYearTick)
            } &&
            (
                this.plant.progress.hasBeenWorkedOnThisTick(ActionType.MOWING, currentTick, currentYearTick) ||
                    this.mowingDoneTicks.contains(currentTick)
                )
        ) {
            return
        } else if (this.plant.type.mowingWindows.any {
                it.contains(currentYearTick)
            } &&
            this.currentHarvestEstimate > 0
        ) {
            missedActionList.add(ActionType.MOWING)
        }
    }

    /**
     * check if the plantation can be mowed
     */
    fun canBeMowed(currentTick: Int, currentYearTick: YearTick, machinePlants: List<PlantType>): Boolean {
        return this.plant.type.mowingWindows.any {
            it.contains(currentYearTick)
        } &&
            this.plant.type in machinePlants &&
            permanentDisabled == null &&
            this.plant.progress.canBeWorkedOnThisTick(currentTick, currentYearTick) &&
            !mowingDoneTicks.contains(currentTick) &&
            currentHarvestEstimate > 0
    }

    override fun canBePollinated(currentTick: Int, currentYearTick: YearTick): Boolean {
        val disabledTick = permanentDisabled
        if (disabledTick != null &&
            disabledTick < currentTick
        ) {
            return false
        }
        val p = plant
        val needsInsect = p.type.pollination == Pollination.INSECTS
        val isBloomingRN = p.isInBloomWindow(currentTick, currentYearTick)

        return needsInsect && isBloomingRN
    }
}
