package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.Plant
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.FarmableTile
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import kotlin.math.max

const val DAYS_PER_TICK = 14

/**
 * Class that handles machines
 */
class Machine(
    val id: Int,
    val name: String,
    val actions: List<ActionType>,
    val plants: List<PlantType>,
    val duration: Int,
    var location: Tile,
    val tileManager: TileManager,
    val farm: Int
) {
    var isAvailable: Boolean = true
    val cargo: MutableMap<PlantType, Int> = mutableMapOf()
    var handledThisTick: Int = 0
    var brokenReleaseTick: Int? = null

    /**
     * refresh the machine for the new tick
     */
    fun refreshForNewTick(currentTick: Int) {
        handledThisTick = 0
        isAvailable = when (val rel = brokenReleaseTick) {
            null -> true
            Int.MAX_VALUE -> false
            else -> currentTick >= rel
        }
    }

    /**
     * mark the machine as broken
     */
    fun markBroken(duration: Int, incidentTick: Int) {
        val release = brokenReleaseTick
        brokenReleaseTick = if (duration == -1) {
            Int.MAX_VALUE
        } else if (release == null) {
            incidentTick + 1 + duration
        } else {
            max(incidentTick + 1 + duration, release)
        }
    }

    /**
     * checks the machine availability
     */
    fun isAvailable(currentTick: Int): Boolean {
        val rel = brokenReleaseTick
        val notBrokenNow = rel == null || (rel != Int.MAX_VALUE && currentTick >= rel)
        return isAvailable && notBrokenNow
    }

    /**
     * Machine does sowing on the tile
     */
    fun doSowing(tile: Field, currentTick: Int, yearTick: YearTick, plant: FieldPlantType) {
        this.isAvailable = false
        this.handledThisTick += 1
        this.location = tile
        val createdPlant = FieldPlant(plant)
        createdPlant.setProgress(ActionType.SOWING, currentTick, yearTick)
        tile.plant = createdPlant
        tile.currentHarvestEstimate = createdPlant.type.initialHarvestEstimate
    }

    /**
     * irrigation action
     */
    fun <P : Plant> doIrrigation(field: FarmableTile<P>, currentTick: Int, currentYearTick: YearTick) {
        this.isAvailable = false
        this.handledThisTick += 1
        this.location = field
        field.plant?.progress?.addEntry(ActionType.IRRIGATING, currentTick, currentYearTick)
        field.currentMoisture = field.moistureCapacity
        Logger.logFarmAction(id, ActionType.IRRIGATING, field.id, duration)
    }

    /**
     * mowing action
     */
    fun doMowing(tile: Plantation, currentTick: Int, currentYearTick: YearTick) {
        this.isAvailable = false
        this.handledThisTick += 1
        this.location = tile
        tile.plant.setProgress(ActionType.MOWING, currentTick, currentYearTick)
        Logger.logFarmAction(id, ActionType.MOWING, tile.id, duration)
    }

    /**
     * harvesting action
     */
    fun <P : Plant> doHarvesting(tile: FarmableTile<P>, currentTick: Int, currentYearTick: YearTick) {
        //  If tile is plantation, think about deleting
        //  and creating a new plant to avoid the restoring progress logic
        this.isAvailable = false
        this.handledThisTick += 1
        this.location = tile
        val currentEstimate = tile.currentHarvestEstimate
        val p = tile.plant ?: return
        cargo[p.type] = cargo.getOrDefault(p.type, 0) + currentEstimate
        p.setProgress(ActionType.HARVESTING, currentTick, currentYearTick)
        tile.currentHarvestEstimate = 0
        if (tile is Field) {
            tile.lastFallow = currentTick
            tile.plant = null
        }
        Logger.logFarmAction(id, ActionType.HARVESTING, tile.id, duration)
        Logger.logFarmHarvest(this, currentEstimate, p.type)
    }

    /**
     * function that handles cutting plantations
     */
    fun doCutting(plantation: Plantation, currentTick: Int, currentYearTick: YearTick) {
        this.isAvailable = false
        this.handledThisTick += 1
        this.location = plantation
        plantation.nextCycleToCut = currentYearTick.getNextNovember() + currentTick
        plantation.plant.setProgress(ActionType.CUTTING, currentTick, currentYearTick)
        Logger.logFarmAction(id, ActionType.CUTTING, plantation.id, duration)
    }

    /**
     * do the weeding on the field
     */
    fun doWeeding(field: Field, currentTick: Int, currentYearTick: YearTick) {
        val currentPlant = field.plant ?: return
        this.isAvailable = false
        this.handledThisTick += 1
        this.location = field
        currentPlant.setProgress(ActionType.WEEDING, currentTick, currentYearTick)
        Logger.logFarmAction(id, ActionType.WEEDING, field.id, duration)
    }

    /**
     * checks if the machine can handle the tile
     */
    fun <P : Plant> canHandle(plantType: PlantType?, tile: FarmableTile<P>): Boolean {
        val hasHarvest = cargo.values.sum() > 0
        return if (plantType == null) {
            tileManager.existsPath(location, tile, farm, hasHarvest) &&
                DAYS_PER_TICK >= (handledThisTick + 1) * duration
        } else {
            plantType in plants &&
                tileManager.existsPath(location, tile, farm, hasHarvest) &&
                DAYS_PER_TICK >= (handledThisTick + 1) * duration
        }
    }

    /**
     * check if field can be handled
     */
    fun canHandleField(plant: FieldPlantType, field: Field): Boolean {
        val hasHarvest = cargo.values.sum() > 0
        return tileManager.existsPath(location, field, farm, hasHarvest) &&
            DAYS_PER_TICK >= (handledThisTick + 1) * duration &&
            plant in plants
    }

    /**
     * return to the shed after action finish
     */
    fun returnToFarmstead(farmstead: Tile?, farmsteads: MutableList<Tile>): Boolean {
        if (farmstead != null) {
            val hasHarvest = cargo.values.sum() > 0
            if (tileManager.existsPath(location, farmstead, farm, hasHarvest)) {
                this.location = farmstead
                return true
            } else {
                val otherShed = farmsteads.minByOrNull { it.id } ?: return false
                farmsteads.remove(otherShed)
                return returnToFarmstead(otherShed, farmsteads)
            }
        }
        return false
    }

    /**
     * check if the machine can irrigate field tiles
     */
    fun canIrrigateField(): Boolean {
        return ActionType.IRRIGATING in actions &&
            (
                FieldPlantType.PUMPKIN in plants ||
                    FieldPlantType.WHEAT in plants ||
                    FieldPlantType.POTATO in plants ||
                    FieldPlantType.OAT in plants
                )
    }

    /**
     * check if the machine can irrigate plantation tiles
     */
    fun canIrrigatePlantation(): Boolean {
        return ActionType.IRRIGATING in actions &&
            (
                PlantationPlantType.CHERRY in plants ||
                    PlantationPlantType.GRAPE in plants ||
                    PlantationPlantType.APPLE in plants ||
                    PlantationPlantType.ALMOND in plants
                )
    }
}
