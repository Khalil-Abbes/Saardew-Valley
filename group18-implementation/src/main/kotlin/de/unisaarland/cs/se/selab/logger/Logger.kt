package de.unisaarland.cs.se.selab.logger

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.incident.Incident
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.simulation.YearTick

/**
 * Logger object that delegates the call
 */
object Logger {
    lateinit var concreteLogger: ImportantLogger

    /** file invalid */
    fun logInvalidFile(filename: String) =
        this.concreteLogger.logInvalidFile(filename)

    /** simulation end */
    fun logSimulationEnd(tick: Int) =
        this.concreteLogger.logSimulationEnd(tick)

    /** cloud rain */
    fun logCloudRain(cloudID: Int, tileID: Int, amount: Int) =
        this.concreteLogger.logCloudRain(cloudID, tileID, amount)

    /** cloud union */
    fun logCloudUnion(
        cloudIDFromTile: Int,
        cloudIDMovingToTile: Int,
        cloudIDNew: Int,
        amount: Int,
        duration: Int,
        tileID: Int
    ) = this.concreteLogger.logCloudUnion(
        cloudIDFromTile,
        cloudIDMovingToTile,
        cloudIDNew,
        amount,
        duration,
        tileID
    )

    /** farm start */
    fun logFarmStart(farmID: Int) =
        this.concreteLogger.logFarmStart(farmID)

    /** farm action */
    fun logFarmAction(machineID: Int, actionType: ActionType, tileID: Int, duration: Int) =
        this.concreteLogger.logFarmAction(machineID, actionType, tileID, duration)

    /** farm sowing */
    fun logFarmSowing(machineID: Int, plant: PlantType, sowingPlanID: Int) =
        this.concreteLogger.logFarmSowing(machineID, plant, sowingPlanID)

    /** farm harvest */
    fun logFarmHarvest(machine: Machine, amount: Int, plant: PlantType) =
        this.concreteLogger.logFarmHarvest(machine, amount, plant)

    /** machine return */
    fun logMachineReturn(machineID: Int, tileID: Int) =
        this.concreteLogger.logMachineReturn(machineID, tileID)

    /** machine unload */
    fun logMachineUnload(machineID: Int, amount: Int, plant: PlantType) =
        this.concreteLogger.logMachineUnload(machineID, amount, plant)

    /** farm finish */
    fun logFarmFinish(farmID: Int) =
        this.concreteLogger.logFarmFinish(farmID)

    /** incident log */
    fun logIncident(incidentID: Int, incidentType: Incident, tileIDs: List<Int>) =
        this.concreteLogger.logIncident(incidentID, incidentType, tileIDs)

    /** simulation info */
    fun logSimulationInfo(farms: List<Farm>) =
        this.concreteLogger.logSimulationInfo(farms)

    /** harvest collect */
    fun logHarvestCollect(farmID: Int, amount: Int) =
        this.concreteLogger.logHarvestCollect(farmID, amount)

    /** total harvest */
    fun logTotalHarvest(plant: PlantType, amount: Int) =
        this.concreteLogger.logTotalHarvest(plant, amount)

    /** harvest estimate */
    fun logMissedActions(tileID: Int, actions: List<ActionType>) =
        this.concreteLogger.logMissedActions(tileID, actions)

    /** parse success */
    fun logParseSuccess(filename: String) =
        this.concreteLogger.logParseSuccess(filename)

    /** simulation start */
    fun logSimulationStart(yearTick: YearTick) =
        this.concreteLogger.logSimulationStart(yearTick)

    /** tick start */
    fun logTickStart(tick: Int, yearTick: YearTick) =
        this.concreteLogger.logTickStart(tick, yearTick)

    /** soil moisture */
    fun logSoilMoisture(amountField: Int, amountPlantation: Int) =
        this.concreteLogger.logSoilMoisture(amountField, amountPlantation)

    /** cloud movement */
    fun logCloudMovement(cloudID: Int, amountFluid: Int, startTileID: Int, endTileID: Int) =
        this.concreteLogger.logCloudMovement(cloudID, amountFluid, startTileID, endTileID)

    /** cloud stuck */
    fun logCloudStuck(cloudID: Int, tileID: Int) =
        this.concreteLogger.logCloudStuck(cloudID, tileID)

    /** cloud dissipation */
    fun logCloudDissipation(cloudID: Int, tileID: Int) =
        this.concreteLogger.logCloudDissipation(cloudID, tileID)

    /** harvest change */
    fun logHarvestEstimateChange(tileID: Int, amount: Int, plant: PlantType) =
        this.concreteLogger.logHarvestEstimateChange(tileID, amount, plant)

    /** sunlight movement */
    fun logCloudMovement(startTileID: Int, amountSunLight: Int) =
        this.concreteLogger.logCloudMovement(startTileID, amountSunLight)

    /** cloud position */
    fun logCloudPosition(cloudID: Int, tileID: Int, amountSunLight: Int) =
        this.concreteLogger.logCloudPosition(cloudID, tileID, amountSunLight)

    /** machine fail */
    fun logMachineReturnFail(machineID: Int) =
        this.concreteLogger.logMachineReturnFail(machineID)

    /** active sowing */
    fun logFarmActiveSowingPlans(farmID: Int, sowingPlansIDS: List<Int>) =
        this.concreteLogger.logFarmActiveSowingPlans(farmID, sowingPlansIDS)
}
