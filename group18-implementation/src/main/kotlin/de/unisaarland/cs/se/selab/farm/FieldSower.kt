package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field

/**
 * class responsible for field sowing
 */
class FieldSower(val farm: Farm) {

    val sowingPlansFulfilled = mutableSetOf<SowingPlan>()

    /**
     * main function for sowing logic
     */
    fun sowFields(currentTick: Int, currentYearTick: YearTick) {
        val plansToFulfill = farm.sowingPlans
            .filter { it.tick <= currentTick }
            .sortedWith(
                compareBy<SowingPlan> { it.tick }
                    .thenBy { it.id }
            )
        Logger.logFarmActiveSowingPlans(farm.id, plansToFulfill.map { it.id }.sorted())
        plansToFulfill.forEach {
                plan ->
            executePlan(plan, currentTick, currentYearTick)
        }
        sowingPlansFulfilled.forEach {
                sowingPlan ->
            farm.sowingPlans.remove(sowingPlan)
        }
    }

    private fun executePlan(plan: SowingPlan, currentTick: Int, currentYearTick: YearTick) {
        val plant = plan.plantType
        if (!plant.sowingWindow.contains(currentYearTick)) {
            return
        }
        val fields = plan.getFields().intersect(farm.fields.toSet())
            .filter {
                it.canBeSowed(currentTick, plant)
            }.sortedBy {
                it.id
            }
        fields.forEach {
                field ->
            if (field.canBeSowed(currentTick, plant) && sowField(currentTick, currentYearTick, field, plant, plan)) {
                sowingPlansFulfilled.add(plan)
            }
        }
    }

    private fun sowField(
        currentTick: Int,
        currentYearTick: YearTick,
        field: Field,
        plant: FieldPlantType,
        sowingPlan: SowingPlan
    ): Boolean {
        val sowingMachines = farm.machines.filter {
            it.isAvailable(currentTick) && ActionType.SOWING in it.actions &&
                it.canHandle(plant, field)
        }.sortedWith(
            compareBy<Machine> { it.duration }
                .thenBy { it.id }
        )
        if (sowingMachines.isNotEmpty()) {
            val machine = sowingMachines.first()
            machine.doSowing(field, currentTick, currentYearTick, plant)
            Logger.logFarmAction(machine.id, ActionType.SOWING, field.id, machine.duration)
            Logger.logFarmSowing(machine.id, plant, sowingPlan.id)
            sowingPlan.fulfilled = true
            continueSowing(machine, currentTick, currentYearTick, plant, sowingPlan)
            farm.handleMachineReturn(machine)
            return true
        }
        return false
    }
    private fun continueSowing(
        machine: Machine,
        currentTick: Int,
        currentYearTick: YearTick,
        plant: FieldPlantType,
        sowingPlan: SowingPlan
    ) {
        val tilesInRadius: List<Field> = machine.tileManager.getFarmFieldsInRadius(farm.id, machine.location, 2)
        val tilesInRadiusCanBeSowed = tilesInRadius.intersect(sowingPlan.getFields().toSet())
            .intersect(farm.fields.toSet())
            .filter { machine.canHandle(plant, it) && it.canBeSowed(currentTick, plant) }.sortedBy { it.id }
        if (tilesInRadiusCanBeSowed.isNotEmpty()) {
            val tile = tilesInRadiusCanBeSowed.first()
            machine.doSowing(tile, currentTick, currentYearTick, plant)
            Logger.logFarmAction(machine.id, ActionType.SOWING, tile.id, machine.duration)
            Logger.logFarmSowing(machine.id, plant, sowingPlan.id)
            continueSowing(machine, currentTick, currentYearTick, plant, sowingPlan)
        }
    }
}
