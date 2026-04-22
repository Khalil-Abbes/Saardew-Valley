package de.unisaarland.cs.se.selab.tile

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.plant.Pollination
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.YearTick
import kotlin.math.max

const val EIGHTY_PERCENT = 0.8

/**
 * class for the field tile type
 */
class Field(
    id: Int,
    coordinate: Coordinate,
    farm: Int,
    direction: Direction?,
    moistureCapacity: Int,
    val possiblePlants: List<PlantType>
) : FarmableTile<FieldPlant>(
    id,
    coordinate,
    TileCategory.FIELD,
    farm,
    direction,
    moistureCapacity
) {
    override var plant: FieldPlant? = null

    override var currentHarvestEstimate: Int = plant?.type?.initialHarvestEstimate ?: 0
    var lastFallow: Int = -(FALLOW_PERIOD + 1)
    var irrigationMissed = false
    var droughtHappenedTick: Int? = null

    override fun killPlant(currentTick: Int) {
        this.plant = null
        this.lastFallow = currentTick
    }

    /**
     * reduce the soil moisture of the tile
     */
    fun reduceSoilMoisture() {
        if (this.plant != null) {
            this.currentMoisture = max(this.currentMoisture - MOISTURE_REDUCTION_WITH_PLANTS, 0)
        } else {
            this.currentMoisture = max(this.currentMoisture - MOISTURE_REDUCTION_WITHOUT_PLANTS, 0)
        }
    }

    /**
     * check if the tile moisture is below the plant's threshold
     */
    override fun isMoistureBelowThreshold(): Boolean {
        val currentPlant = this.plant
        if (currentPlant != null) {
            return this.currentMoisture < currentPlant.type.moistureRequired
        }
        return false
    }

    /**
     * checks if field can be harvested
     */
    override fun canBeHarvested(currentTick: Int, currentYearTick: YearTick): Boolean {
        val p = this.plant ?: return false
        return p.type.harvestWindow.contains(currentYearTick) &&
            currentHarvestEstimate > 0 &&
            p.progress.canBeWorkedOnThisTick(currentTick, currentYearTick)
    }

    /**
     * checks if this field can be sowed in the specific tick with the specific plant
     */
    fun canBeSowed(currentTick: Int, plantType: PlantType): Boolean {
        return this.plant == null &&
            possiblePlants.contains(plantType) &&
            lastFallow + FALLOW_PERIOD + 1 <= currentTick
    }

    override fun canBeIrrigated(
        currentTick: Int,
        currentYearTick: YearTick,
        machinePlants: List<PlantType>
    ): Boolean {
        val currentPlant = plant ?: return false
        return this.currentMoisture < currentPlant.type.moistureRequired &&
            currentPlant.type in machinePlants &&
            currentPlant.progress.canBeWorkedOnThisTick(currentTick, currentYearTick)
    }

    override fun updateHarvestEstimate(currentTick: Int, currentYearTick: YearTick) {
        val p = this.plant ?: return clearIncidentEffects()
        val originalAmount = this.currentHarvestEstimate
        val missedActions = mutableListOf<ActionType>()

        checkLateSowing(currentTick, currentYearTick)
        applyEnvironmentPenalty()
        applyMissedWeeding(currentTick, currentYearTick, missedActions)
        if (irrigationMissed) {
            missedActions.add(ActionType.IRRIGATING)
        }
        applyMissedHarvesting(currentYearTick, missedActions)
        applyIncidentEffects()

        if (droughtHappenedTick != currentTick) {
            if (missedActions.isNotEmpty()) {
                Logger.logMissedActions(id, missedActions)
            }
        }

        if (originalAmount != this.currentHarvestEstimate) {
            Logger.logHarvestEstimateChange(this.id, this.currentHarvestEstimate, p.type)
        }

        if (this.currentHarvestEstimate == 0) {
            killPlant(currentTick)
        }
    }

    /**
     * check if irrigation was missed
     */
    fun missedIrrigation(currentTick: Int, currentYearTick: YearTick): Boolean {
        val plant = this.plant ?: return false
        return this.currentMoisture < plant.type.moistureRequired &&
            plant.type.moistureRequired - currentMoisture >= MOISTURE_PENALTY_CHECK &&
            !plant.progress.irrigating.contains(Pair(currentTick, currentYearTick)) &&
            !plant.progress.harvesting.contains(Pair(currentTick, currentYearTick))
    }

    private fun applyMissedHarvesting(
        currentYearTick: YearTick,
        missedActions: MutableList<ActionType>
    ) {
        val currentPlant = this.plant ?: return
        if (currentPlant.type.harvestWindow.isLate(currentYearTick) &&
            currentPlant.progress.harvesting.isEmpty()
        ) {
            currentHarvestEstimate = when (currentPlant.type) {
                FieldPlantType.OAT, FieldPlantType.WHEAT -> {
                    val difference =
                        currentYearTick.tick - currentPlant.type.harvestWindow.endTick.tick
                    if (difference < 2) {
                        max(0, (currentHarvestEstimate * EIGHTY_PERCENT).toInt())
                    } else {
                        0
                    }
                }
                FieldPlantType.POTATO, FieldPlantType.PUMPKIN -> {
                    0
                }
            }
            missedActions.add(ActionType.HARVESTING)
        }
    }

    private fun applyMissedWeeding(
        currentTick: Int,
        currentYearTick: YearTick,
        missedActions: MutableList<ActionType>
    ) {
        val currentPlant = this.plant ?: return
        val sowingTick: Pair<Int, YearTick> = currentPlant.progress.sowing.last()
        if (currentPlant.isInWeedingWindow(sowingTick.first, currentTick)) {
            if (!currentPlant.progress.hasBeenWorkedOnThisTick(ActionType.WEEDING, currentTick, currentYearTick) &&
                !currentPlant.progress.harvesting.contains(Pair(currentTick, currentYearTick))
            ) {
                currentHarvestEstimate = max(0, (currentHarvestEstimate * NINETY_PERCENT).toInt())
                missedActions.add(ActionType.WEEDING)
            }
        }
    }

    private fun checkLateSowing(currentTick: Int, currentYearTick: YearTick) {
        val fieldPlant = this.plant ?: return
        if (fieldPlant.progress.sowing.contains(Pair(currentTick, currentYearTick)) &&
            (
                currentYearTick.tick == fieldPlant.type.sowingWindow.endTick.tick + 1 ||
                    currentYearTick.tick == fieldPlant.type.sowingWindow.endTick.tick + 2
                )
        ) {
            var difference = currentYearTick.tick - fieldPlant.type.sowingWindow.endTick.tick
            while (difference > 0) {
                currentHarvestEstimate = (currentHarvestEstimate * EIGHTY_PERCENT).toInt()
                difference -= 1
            }
        }
    }

    /**
     * checks if weeding can be done on field
     */
    fun canBeWed(
        currentTick: Int,
        currentYearTick: YearTick,
        machinePlants: List<PlantType>
    ): Boolean {
        val currentPlant = plant ?: return false
        val sowingTick: Pair<Int, YearTick> = currentPlant.progress.sowing.last()
        return currentPlant.isInWeedingWindow(sowingTick.first, currentTick) &&
            currentPlant.progress.canBeWorkedOnThisTick(currentTick, currentYearTick) &&
            currentPlant.type in machinePlants && currentHarvestEstimate > 0
    }

    override fun canBePollinated(currentTick: Int, currentYearTick: YearTick): Boolean {
        val p = plant ?: return false
        val needsInsect = p.type.pollination == Pollination.INSECTS
        val isBloomingRN = p.isInBloomWindow(currentTick, currentYearTick)
        return needsInsect &&
            isBloomingRN
    }

    /**
     * checks if the field is currently in a fallow period
     */
    fun isFallow(currentTick: Int): Boolean {
        return currentTick in lastFallow..lastFallow + FALLOW_PERIOD
    }

    /**
     * Field related constants.
     */
    companion object {
        private const val FALLOW_PERIOD = 4
    }
}
