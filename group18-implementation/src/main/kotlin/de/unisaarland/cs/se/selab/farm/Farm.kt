package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile

/**
 * Farm class
 */
class Farm(
    val id: Int,
    val name: String,
    val farmsteads: List<Tile>,
    fields: List<Field>,
    plantations: List<Plantation>,
    machines: List<Machine>,
    val sowingPlans: MutableList<SowingPlan>
) {
    private val _fields = fields.toMutableList()
    private val _plantations = plantations.toMutableList()
    private val _machines: MutableList<Machine> = machines.toMutableList()

    val fields: List<Field>
        get() = _fields.toList()

    val plantations: List<Plantation>
        get() = _plantations.toList()

    val machines: List<Machine>
        get() = _machines.toList()

    val machineToShed: MutableMap<Machine, Tile> = mutableMapOf()

    init {
        machines.forEach { machine -> machineToShed[machine] = machine.location }
    }

    /**
     * main function for all farm actions in order
     */
    fun run(currentTick: Int, currentYearTick: YearTick) {
        Logger.logFarmStart(this.id)
        _plantations.removeAll { it.permanentDisabled != null }
        fields.forEach { it.irrigationMissed = false }
        FieldSower(this).sowFields(currentTick, currentYearTick)
        PlantationHarvester(this).harvestPlantations(currentTick, currentYearTick)
        harvestFields(currentTick, currentYearTick)
        cutPlantations(currentTick, currentYearTick)
        doRemaining(currentTick, currentYearTick)
        setMissedIrrigation(currentTick, currentYearTick)
        resetAttributes(currentTick + 1)
        Logger.logFarmFinish(this.id)
    }

    private fun setMissedIrrigation(currentTick: Int, currentYearTick: YearTick) {
        fields.filter {
            it.missedIrrigation(currentTick, currentYearTick)
        }.forEach {
            it.irrigationMissed = true
        }
    }

    /**
     * reset attributes of machines after a tick
     */
    fun resetAttributes(currentTick: Int) {
        machines.forEach {
            it.refreshForNewTick(currentTick)
        }
    }

    /**
     * Removes field from farm (e.g. when city expansion occurs)
     *
     * @param field the field to be removed
     */
    fun removeField(field: Field) {
        _fields.remove(field)
    }

    private fun doRemaining(currentTick: Int, currentYearTick: YearTick) {
        val leftMachines = machines.filter { it.isAvailable(currentTick) }.sortedWith(
            compareBy<Machine> { it.duration }
                .thenBy { it.id }
        )
        leftMachines.forEach { machine ->
            doActionBasedOnMachine(
                machine,
                currentTick,
                currentYearTick
            )
        }
    }

    private fun doActionBasedOnMachine(
        machine: Machine,
        currentTick: Int,
        currentYearTick: YearTick
    ) {
        if (machine.canIrrigateField()) {
            val irrigatableFields = fields.filter {
                it.canBeIrrigated(currentTick, currentYearTick, machine.plants) &&
                    machine.canHandle(it.plant?.type ?: return@filter false, it)
            }
                .sortedBy { it.id }
            if (irrigatableFields.isNotEmpty()) {
                machine.doIrrigation(irrigatableFields.first(), currentTick, currentYearTick)
                continueIrrigation(currentTick, currentYearTick, machine)
                handleMachineReturn(machine)
                return
            }
        }
        if (ActionType.WEEDING in machine.actions) {
            val weedableFields = fields.filter {
                it.canBeWed(currentTick, currentYearTick, machine.plants) &&
                    machine.canHandle(it.plant?.type ?: return@filter false, it)
            }.sortedBy { it.id }
            if (weedableFields.isNotEmpty()) {
                machine.doWeeding(weedableFields.first(), currentTick, currentYearTick)
                continueWeeding(currentTick, currentYearTick, machine)
                handleMachineReturn(machine)
                return
            }
        }
        doActionBasedOnMachineContinue(machine, currentTick, currentYearTick)
    }

    private fun doActionBasedOnMachineContinue(
        machine: Machine,
        currentTick: Int,
        currentYearTick: YearTick
    ) {
        if (machine.canIrrigatePlantation()) {
            val irrigatablePlantations = plantations.filter {
                it.canBeIrrigated(currentTick, currentYearTick, machine.plants) &&
                    machine.canHandle(it.plant.type, it)
            }.sortedBy { it.id }
            if (irrigatablePlantations.isNotEmpty()) {
                machine.doIrrigation(irrigatablePlantations.first(), currentTick, currentYearTick)
                continueIrrigation(currentTick, currentYearTick, machine)
                handleMachineReturn(machine)
                return
            }
        }
        if (ActionType.MOWING in machine.actions) {
            val mowablePlantations = plantations.filter {
                it.canBeMowed(currentTick, currentYearTick, machine.plants) && machine.canHandle(
                    it.plant.type,
                    it
                )
            }.sortedBy { it.id }
            if (mowablePlantations.isNotEmpty()) {
                machine.doMowing(mowablePlantations.first(), currentTick, currentYearTick)
                continueMowing(currentTick, currentYearTick, machine)
                handleMachineReturn(machine)
            }
        }
    }

    private fun continueMowing(currentTick: Int, currentYearTick: YearTick, machine: Machine) {
        val plantationsInRadius =
            machine.tileManager.getFarmPlantationsInRadius(id, machine.location, 2)
        val plantationsInRadiusCanBeMowed = plantationsInRadius.intersect(plantations.toSet())
            .filter {
                it.canBeMowed(currentTick, currentYearTick, machine.plants) &&
                    machine.canHandle(it.plant.type, it)
            }.sortedBy { it.id }
        if (plantationsInRadiusCanBeMowed.isNotEmpty()) {
            machine.doMowing(plantationsInRadiusCanBeMowed.first(), currentTick, currentYearTick)
            continueMowing(currentTick, currentYearTick, machine)
        }
    }

    private fun continueWeeding(currentTick: Int, currentYearTick: YearTick, machine: Machine) {
        val fieldInRadius: List<Field> =
            machine.tileManager.getFarmFieldsInRadius(id, machine.location, 2)
        val tilesInRadiusCanBeWed = fieldInRadius.intersect(fields.toSet())
            .filter {
                it.canBeWed(currentTick, currentYearTick, machine.plants) &&
                    machine.canHandle(it.plant?.type, it)
            }.sortedBy { it.id }
        if (tilesInRadiusCanBeWed.isNotEmpty()) {
            machine.doWeeding(tilesInRadiusCanBeWed.first(), currentTick, currentYearTick)
            continueWeeding(currentTick, currentYearTick, machine)
        }
    }

    private fun continueIrrigation(currentTick: Int, currentYearTick: YearTick, machine: Machine) {
        val tilesInRadius: List<Field> =
            machine.tileManager.getFarmFieldsInRadius(id, machine.location, 2)
        val tilesInRadiusCanBeIrrigated = tilesInRadius.intersect(fields.toSet())
            .filter {
                it.canBeIrrigated(currentTick, currentYearTick, machine.plants) &&
                    machine.canHandle(it.plant?.type, it)
            }
            .sortedBy { it.id }
        if (tilesInRadiusCanBeIrrigated.isNotEmpty()) {
            val tile = tilesInRadiusCanBeIrrigated.first()
            machine.doIrrigation(tile, currentTick, currentYearTick)
            continueIrrigation(currentTick, currentYearTick, machine)
        } else {
            val plantationsInRadius: List<Plantation> =
                machine.tileManager.getFarmPlantationsInRadius(
                    id,
                    machine.location,
                    2
                )
            val plantationsInRadiusCanBeIrrigated =
                plantationsInRadius.intersect(plantations.toSet())
                    .filter {
                        it.canBeIrrigated(currentTick, currentYearTick, machine.plants) &&
                            machine.canHandle(it.plant.type, it)
                    }
                    .sortedBy { it.id }
            if (plantationsInRadiusCanBeIrrigated.isNotEmpty()) {
                val tile = plantationsInRadiusCanBeIrrigated.first()
                machine.doIrrigation(tile, currentTick, currentYearTick)
                continueIrrigation(currentTick, currentYearTick, machine)
            }
        }
    }

    private fun cutPlantations(currentTick: Int, currentYearTick: YearTick) {
        val cuttablePlantations = plantations.filter { it.canBeCut(currentTick, currentYearTick) }.sortedBy { it.id }
        cuttablePlantations.forEach {
            if (it.canBeCut(currentTick, currentYearTick)) {
                cutPlantation(currentTick, currentYearTick, it)
            }
        }
    }

    private fun cutPlantation(currentTick: Int, currentYearTick: YearTick, plantation: Plantation) {
        val cuttingMachines = machines.filter {
            it.isAvailable(currentTick) &&
                ActionType.CUTTING in it.actions &&
                it.canHandle(plantation.plant.type, plantation)
        }.sortedWith(
            compareBy<Machine> { it.duration }
                .thenBy { it.id }
        )
        if (cuttingMachines.isNotEmpty()) {
            val machine = cuttingMachines.first()
            machine.doCutting(plantation, currentTick, currentYearTick)
            continuePlantationCutting(currentTick, currentYearTick, machine)
            handleMachineReturn(machine)
        }
    }

    private fun continuePlantationCutting(
        currentTick: Int,
        currentYearTick: YearTick,
        machine: Machine
    ) {
        val tilesInRadius: List<Plantation> =
            machine.tileManager.getFarmPlantationsInRadius(id, machine.location, 2)
        val tilesInRadiusCanBeCut = tilesInRadius.intersect(plantations.toSet())
            .filter {
                machine.canHandle(
                    it.plant.type,
                    it
                ) && it.canBeCut(
                    currentTick,
                    currentYearTick
                )
            }
            .sortedBy {
                it.id
            }
        if (tilesInRadiusCanBeCut.isNotEmpty()) {
            val tile = tilesInRadiusCanBeCut.first()
            machine.doCutting(tile, currentTick, currentYearTick)
            continuePlantationCutting(currentTick, currentYearTick, machine)
        }
    }

    private fun harvestFields(currentTick: Int, currentYearTick: YearTick) {
        val harvestableFields =
            fields.filter { it.canBeHarvested(currentTick, currentYearTick) }.sortedBy { it.id }
        harvestableFields.forEach {
            if (it.canBeHarvested(currentTick, currentYearTick)) {
                harvestField(currentTick, currentYearTick, it)
            }
        }
    }

    private fun harvestField(currentTick: Int, currentYearTick: YearTick, field: Field) {
        val p = field.plant ?: return
        val harvestingMachines = machines.filter {
            it.isAvailable(currentTick) && ActionType.HARVESTING in it.actions && it.canHandle(
                p.type, field
            )
        }.sortedWith(
            compareBy<Machine> { it.duration }
                .thenBy { it.id }
        )
        if (harvestingMachines.isNotEmpty()) {
            val machine = harvestingMachines.first()
            machine.doHarvesting(field, currentTick, currentYearTick)
            continueFieldHarvesting(currentTick, currentYearTick, machine, p.type)
            if (handleMachineReturn(machine)) {
                Logger.logMachineUnload(machine.id, machine.cargo.getOrDefault(p.type, 0), p.type)
                machine.cargo[p.type] = 0
            }
        }
    }

    private fun continueFieldHarvesting(
        currentTick: Int,
        currentYearTick: YearTick,
        machine: Machine,
        plant: FieldPlantType
    ) {
        val tilesInRadius: List<Field> =
            machine.tileManager.getFarmFieldsInRadius(id, machine.location, 2)

        val tilesInRadiusCanBeHarvested = tilesInRadius.intersect(fields.toSet())
            .filter {
                val p = it.plant ?: return@filter false
                machine.canHandleField(p.type, it) &&
                    it.canBeHarvested(currentTick, currentYearTick) && it.plant != null && plant == p.type
            }
            .sortedBy { it.id }
        if (tilesInRadiusCanBeHarvested.isNotEmpty()) {
            val tile = tilesInRadiusCanBeHarvested.first()
            machine.doHarvesting(tile, currentTick, currentYearTick)
            continueFieldHarvesting(currentTick, currentYearTick, machine, plant)
        }
    }

    /**
     * bring back machine to shed
     */
    fun handleMachineReturn(machine: Machine): Boolean {
        if (machine.returnToFarmstead(machineToShed[machine], farmsteads.toMutableList())) {
            machineToShed[machine] = machine.location
            Logger.logMachineReturn(machine.id, machine.location.id)
            return true
        } else {
            Logger.logMachineReturnFail(machine.id)

            _machines.remove(machine)
        }
        return false
    }
}
