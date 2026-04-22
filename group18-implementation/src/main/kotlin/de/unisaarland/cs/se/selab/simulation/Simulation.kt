package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.incident.Incident
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.tile.FarmableTile
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.NINETY_FIVE_PERCENT
import de.unisaarland.cs.se.selab.tile.NINETY_PERCENT
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.THIRTY_PERCENT

/**
 * Simulation main class
 */
class Simulation(
    val tileManager: TileManager,
    val cloudHandler: CloudHandler,
    val farms: Map<Int, Farm>,
    val incidents: List<Incident>,
    var currentTick: Int,
    var currentYearTick: YearTick,
    val maxTick: Int
) {
    /**
     * function that loops the ticks
     */
    fun run() {
        reflectPenalties()
        Logger.logSimulationStart(currentYearTick)
        while (currentTick < maxTick) {
            runTick(currentTick, currentYearTick)
            currentTick += 1
            currentYearTick = currentYearTick.getNext()
        }
        Logger.logSimulationEnd(maxTick)
        Logger.logSimulationInfo(farms.values.toList())
    }

    /**
     * runs the currentTick
     */
    private fun runTick(currentTick: Int, currentYearTick: YearTick) {
        Logger.logTickStart(currentTick, currentYearTick)
        reduceSoilMoisture(currentYearTick)
        cloudHandler.run()
        farmAction(currentTick, currentYearTick)
        incidentStart(currentTick, currentYearTick)
        computeHarvest()
    }
    private fun computeHarvest() {
        val tiles = tileManager.getAllTiles().filter { it is Plantation || it is Field }
        val fieldsAndPlantations: MutableList<FarmableTile<*>> = mutableListOf()
        tiles.forEach { tile -> if (tile is Plantation || tile is Field) fieldsAndPlantations.add(tile) }
        fieldsAndPlantations.sortedBy { it.id }.forEach { tile ->
            tile.updateHarvestEstimate(
                currentTick,
                currentYearTick
            )
        }
    }

    /**
     * first step of the tick
     */
    private fun reduceSoilMoisture(currentYearTick: YearTick) {
        val fields = tileManager.getFields()
        fields.forEach {
                field ->
            field.updateSunlight(currentYearTick)
        }
        fields.forEach {
                field ->
            field.reduceSoilMoisture()
        }
        val fieldsBelowThreshold = fields.count {
            it.isMoistureBelowThreshold()
        }
        val plantations = tileManager.getPlantations()
        plantations.forEach {
            it.updateSunlight(currentYearTick)
        }
        plantations.forEach {
            it.reduceSoilMoisture()
        }
        val plantationsBelowThreshold = plantations.count {
            it.isMoistureBelowThreshold()
        }
        Logger.logSoilMoisture(fieldsBelowThreshold, plantationsBelowThreshold)
    }

    private fun farmAction(currentTick: Int, currentYearTick: YearTick) {
        farms.values.sortedBy { it.id }.forEach { farm -> farm.run(currentTick, currentYearTick) }
    }

    private fun incidentStart(currentTick: Int, currentYearTick: YearTick) {
        incidents.filter { it.tick == currentTick }.sortedBy { it.id }.forEach {
                incident ->
            incident.perform(
                currentTick,
                currentYearTick
            )
        }
    }

    private fun reflectPenalties() {
        tileManager.getPlantations().filter { it.plant.type == PlantationPlantType.APPLE }.forEach {
            reflectOnApple(
                it
            )
        }
        tileManager.getPlantations().filter { it.plant.type == PlantationPlantType.ALMOND }.forEach {
            reflectOnAlmond(
                it
            )
        }
        tileManager.getPlantations().filter { it.plant.type == PlantationPlantType.CHERRY }.forEach {
            reflectOnCherry(it)
        }
        tileManager.getPlantations().filter { it.plant.type == PlantationPlantType.GRAPE }.forEach {
            reflectOnGrape(it)
        }
    }
    private fun reflectOnApple(plantation: Plantation) {
        if (currentYearTick == YearTick.OCT2) {
            plantation.currentHarvestEstimate /= 2
        }
    }
    private fun reflectOnAlmond(plantation: Plantation) {
        if (currentYearTick == YearTick.OCT2) {
            plantation.currentHarvestEstimate = (plantation.currentHarvestEstimate * NINETY_PERCENT).toInt()
        }
    }

    private fun reflectOnCherry(plantation: Plantation) {
        if (currentYearTick == YearTick.AUG1) {
            plantation.currentHarvestEstimate = (plantation.currentHarvestEstimate * THIRTY_PERCENT).toInt()
        } else if (currentYearTick.tick in YearTick.AUG2.tick..YearTick.OCT2.tick) {
            plantation.currentHarvestEstimate = 0
        }
    }
    private fun reflectOnGrape(plantation: Plantation) {
        if (currentYearTick.tick in YearTick.SEP2.tick..YearTick.OCT2.tick) {
            var difference = currentYearTick.tick - YearTick.SEP1.tick
            while (difference > 0) {
                plantation.currentHarvestEstimate = (plantation.currentHarvestEstimate * NINETY_FIVE_PERCENT).toInt()
                difference -= 1
            }
        }
    }
}
