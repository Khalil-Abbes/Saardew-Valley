package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Plantation
import kotlin.collections.set

/**
 * class for harvesting plants
 */
class PlantationHarvester(val farm: Farm) {

    /**
     * function to harvest plantations
     */
    fun harvestPlantations(currentTick: Int, currentYearTick: YearTick) {
        val harvestablePlantations = farm.plantations.filter {
            it.canBeHarvested(currentTick, currentYearTick)
        }
            .sortedBy {
                it.id
            }
        harvestablePlantations.forEach {
            if (it.canBeHarvested(currentTick, currentYearTick)) {
                harvestPlantation(currentTick, currentYearTick, it)
            }
        }
    }
    private fun harvestPlantation(currentTick: Int, currentYearTick: YearTick, plantation: Plantation) {
        val harvestingMachines = farm.machines.filter {
            it.isAvailable(currentTick) &&
                ActionType.HARVESTING in it.actions &&
                it.canHandle(plantation.plant.type, plantation)
        }.sortedWith(
            compareBy<Machine> {
                it.duration
            }
                .thenBy {
                    it.id
                }
        )
        if (harvestingMachines.isNotEmpty()) {
            val machine = harvestingMachines.first()
            machine.doHarvesting(plantation, currentTick, currentYearTick)
            continuePlantationHarvesting(currentTick, currentYearTick, machine, plantation.plant.type)
            if (farm.handleMachineReturn(machine)) {
                Logger.logMachineUnload(
                    machine.id,
                    machine.cargo.getOrDefault(
                        plantation.plant.type,
                        0
                    ),
                    plantation.plant.type
                )
                machine.cargo[plantation.plant.type] = 0
            }
        }
    }
    private fun continuePlantationHarvesting(
        currentTick: Int,
        currentYearTick: YearTick,
        machine: Machine,
        plant: PlantationPlantType
    ) {
        val tilesInRadius: List<Plantation> =
            machine.tileManager.getFarmPlantationsInRadius(farm.id, machine.location, 2)
        val tilesInRadiusCanBeHarvested = tilesInRadius.intersect(farm.plantations.toSet())
            .filter {
                machine.canHandle(plant, it) &&
                    it.canBeHarvested(currentTick, currentYearTick) && plant == it.plant.type
            }
            .sortedBy { it.id }
        if (tilesInRadiusCanBeHarvested.isNotEmpty()) {
            val tile = tilesInRadiusCanBeHarvested.first()
            machine.doHarvesting(tile, currentTick, currentYearTick)
            continuePlantationHarvesting(currentTick, currentYearTick, machine, plant)
        }
    }
}
