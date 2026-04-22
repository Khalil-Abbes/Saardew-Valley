package de.unisaarland.cs.se.selab.logger

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.incident.Incident
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.YearTick
import java.io.PrintWriter

/**
 * Superclass for all the loggers, has all logs (not all are implemented as it should be)
 */
open class ImportantLogger(val file: PrintWriter) {

    val collectedHarvest = mutableMapOf<Int, Int>()
    val harvestedPlants = mutableMapOf<PlantType, Int>()

    /**
     * file invalid
     */
    open fun logInvalidFile(filename: String) {
        val log = "Initialization Info: $filename is invalid."
        this.printLog(log)
    }

    /**
     * simulation end
     */
    open fun logSimulationEnd(tick: Int) {
        val log = "Simulation Info: Simulation ended at tick $tick."
        this.printLog(log)
    }

    /**
     * cloud rain
     */
    open fun logCloudRain(cloudID: Int, tileID: Int, amount: Int) {
        val log = "Cloud Rain: Cloud $cloudID on tile $tileID rained down $amount L water."
        this.printLog(log)
    }

    /**
     * cloud union
     */
    open fun logCloudUnion(
        cloudIDFromTile: Int,
        cloudIDMovingToTile: Int,
        cloudIDNew: Int,
        amount: Int,
        duration: Int,
        tileID: Int
    ) {
        var log = "Cloud Union: Clouds $cloudIDFromTile and $cloudIDMovingToTile united to cloud $cloudIDNew"
        log += " with $amount L water and duration $duration on tile $tileID."
        this.printLog(log)
    }

    /**
     * farm start
     */
    open fun logFarmStart(farmID: Int) {
        val log = "Farm: Farm $farmID starts its actions."
        this.printLog(log)
    }

    /**
     * farm action
     */
    open fun logFarmAction(machineID: Int, actionType: ActionType, tileID: Int, duration: Int) {
        val log = "Farm Action: Machine $machineID performs $actionType on tile $tileID for $duration days."
        this.printLog(log)
    }

    /**
     * farm sowing
     */
    open fun logFarmSowing(machineID: Int, plant: PlantType, sowingPlanID: Int) {
        val log = "Farm Sowing: Machine $machineID has sowed $plant according to sowing plan $sowingPlanID."
        this.printLog(log)
    }

    /**
     * farm harvest
     */
    open fun logFarmHarvest(machine: Machine, amount: Int, plant: PlantType) {
        val log = "Farm Harvest: Machine ${machine.id} has collected $amount g of $plant harvest."
        collectedHarvest[machine.farm] = collectedHarvest.getOrDefault(machine.farm, 0) + amount
        harvestedPlants[plant] = harvestedPlants.getOrDefault(plant, 0) + amount
        this.printLog(log)
    }

    /**
     * machine return
     */
    open fun logMachineReturn(machineID: Int, tileID: Int) {
        val log = "Farm Machine: Machine $machineID is finished and returns to the shed at $tileID."
        this.printLog(log)
    }

    /**
     * machine return fail
     */
    open fun logMachineReturnFail(machineID: Int) {
        val log = "Farm Machine: Machine $machineID is finished but failed to return."
        this.printLog(log)
    }

    /**
     * machine unload
     */
    open fun logMachineUnload(machineID: Int, amount: Int, plant: PlantType) {
        val log = "Farm Machine: Machine $machineID unloads $amount g of $plant harvest in the shed."
        this.printLog(log)
    }

    /**
     * farm finish
     */
    open fun logFarmFinish(farmID: Int) {
        val log = "Farm: Farm $farmID finished its actions."
        this.printLog(log)
    }

    /**
     * incident happened
     */
    open fun logIncident(incidentID: Int, incidentType: Incident, tileIDs: List<Int>) {
        var log = "Incident: "
        log += "Incident $incidentID of type $incidentType happened and affected tiles ${tileIDs.joinToString(
            ","
        )}."
        this.printLog(log)
    }

    /**
     * simulation info
     */
    open fun logSimulationInfo(farms: List<Farm>) {
        val log = "Simulation Info: Simulation statistics are calculated."
        this.printLog(log)
        computeStats(farms)
    }

    /**
     * Compute Statistics
     */
    private fun computeStats(farms: List<Farm>) {
        farms.sortedBy { it.id }.forEach {
            logHarvestCollect(it.id, collectedHarvest.getOrDefault(it.id, 0))
        }
        val plants = listOf<PlantType>(
            FieldPlantType.POTATO,
            FieldPlantType.WHEAT,
            FieldPlantType.OAT,
            FieldPlantType.PUMPKIN,
            PlantationPlantType.APPLE,
            PlantationPlantType.GRAPE,
            PlantationPlantType.ALMOND,
            PlantationPlantType.CHERRY,
        )
        plants.forEach { logTotalHarvest(it, harvestedPlants.getOrDefault(it, 0)) }
        var sumLeft = farms.sumOf { it.fields.sumOf { field -> field.currentHarvestEstimate } }
        sumLeft += farms.sumOf { it.plantations.sumOf { plant -> plant.currentHarvestEstimate } }
        leftHarvest(sumLeft)
    }

    /**
     * harvest collect
     */
    open fun logHarvestCollect(farmID: Int, amount: Int) {
        val log = "Simulation Statistics: Farm $farmID collected $amount g of harvest."
        this.printLog(log)
    }

    /**
     * total harvest
     */
    open fun logTotalHarvest(plant: PlantType, amount: Int) {
        val log = "Simulation Statistics: Total amount of $plant harvested: $amount g."
        this.printLog(log)
    }

    /**
     * left harvest
     */
    open fun leftHarvest(amount: Int) {
        val log = "Simulation Statistics: Total harvest estimate still in fields and plantations: $amount g."
        this.printLog(log)
    }

    /**
     * parse success
     */
    open fun logParseSuccess(filename: String) {}

    /**
     * simulation start
     */
    open fun logSimulationStart(yearTick: YearTick) {}

    /**
     * tick start
     */
    open fun logTickStart(tick: Int, yearTick: YearTick) {}

    /**
     * soil moisture
     */
    open fun logSoilMoisture(amountField: Int, amountPlantation: Int) {}

    /**
     * cloud movement
     */
    open fun logCloudMovement(cloudID: Int, amountFluid: Int, startTileID: Int, endTileID: Int) {}

    /**
     * cloud stuck
     */
    open fun logCloudStuck(cloudID: Int, tileID: Int) {}

    /**
     * cloud dissipation
     */
    open fun logCloudDissipation(cloudID: Int, tileID: Int) {}

    /**
     * harvest estimate change
     */
    open fun logHarvestEstimateChange(tileID: Int, amount: Int, plant: PlantType) {}

    /**
     * cloud sunlight movement
     */
    open fun logCloudMovement(startTileID: Int, amountSunLight: Int) {}

    /**
     * cloud position
     */
    open fun logCloudPosition(cloudID: Int, tileID: Int, amountSunLight: Int) {}

    /**
     * missed actions
     */
    open fun logMissedActions(tileID: Int, actions: List<ActionType>) {}

    /**
     * farm sowing plans
     */
    open fun logFarmActiveSowingPlans(farmID: Int, sowingPlansIDS: List<Int>) {}

    /**
     * print log
     */
    private fun printLog(log: String) {
        val newLog = "[IMPORTANT] $log"
        file.println(newLog)
        file.flush()
    }
}
