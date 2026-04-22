package de.unisaarland.cs.se.selab.logger

import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.simulation.YearTick
import java.io.PrintWriter

/**
 * Info Logger
 */
open class InfoLogger(file: PrintWriter) : ImportantLogger(file) {

    override fun logParseSuccess(filename: String) {
        val log = "Initialization Info: $filename successfully parsed and validated."
        printLog(log)
    }
    override fun logSimulationStart(yearTick: YearTick) {
        val log = "Simulation Info: Simulation started at tick ${yearTick.tick} within the year."
        printLog(log)
    }
    override fun logTickStart(tick: Int, yearTick: YearTick) {
        val log = "Simulation Info: Tick $tick started at tick ${yearTick.tick} within the year."
        printLog(log)
    }
    override fun logSoilMoisture(amountField: Int, amountPlantation: Int) {
        val log = "Soil Moisture: The soil moisture is below threshold in $amountField FIELD" +
            " and $amountPlantation PLANTATION tiles."
        printLog(log)
    }
    override fun logCloudMovement(cloudID: Int, amountFluid: Int, startTileID: Int, endTileID: Int) {
        val log = "Cloud Movement: Cloud $cloudID with $amountFluid L water" +
            " moved from tile $startTileID to tile $endTileID."
        printLog(log)
    }
    override fun logCloudStuck(cloudID: Int, tileID: Int) {
        val log = "Cloud Dissipation: Cloud $cloudID got stuck on tile $tileID."
        printLog(log)
    }
    override fun logCloudDissipation(cloudID: Int, tileID: Int) {
        val log = "Cloud Dissipation: Cloud $cloudID dissipates on tile $tileID."
        printLog(log)
    }
    override fun logHarvestEstimateChange(tileID: Int, amount: Int, plant: PlantType) {
        val log = "Harvest Estimate: Harvest estimate on tile $tileID changed to $amount g of $plant."
        printLog(log)
    }

    private fun printLog(log: String) {
        val newLog = "[INFO] $log"
        file.println(newLog)
        file.flush()
    }
}
