package de.unisaarland.cs.se.selab.logger

import de.unisaarland.cs.se.selab.farm.ActionType
import java.io.PrintWriter

/**
 * debug logs class
 */
class DebugLogger(file: PrintWriter) : InfoLogger(file) {
    override fun logCloudMovement(startTileID: Int, amountSunLight: Int) {
        val log = "Cloud Movement: On tile $startTileID, the amount of sunlight is $amountSunLight."
        printLog(log)
    }
    override fun logCloudPosition(cloudID: Int, tileID: Int, amountSunLight: Int) {
        val log = "Cloud Position: Cloud $cloudID is on tile $tileID, where the amount of sunlight is $amountSunLight."
        printLog(log)
    }
    override fun logMissedActions(tileID: Int, actions: List<ActionType>) {
        val log = "Harvest Estimate: Required actions on tile $tileID were not performed: ${actions.joinToString(
            ","
        )}."
        printLog(log)
    }
    override fun logFarmActiveSowingPlans(farmID: Int, sowingPlansIDS: List<Int>) {
        val log = "Farm: Farm $farmID has the following active sowing plans it " +
            "intends to pursue in this tick: ${sowingPlansIDS.joinToString(",")}."
        printLog(log)
    }

    private fun printLog(log: String) {
        val newLog = "[DEBUG] $log"
        file.println(newLog)
        file.flush()
    }
}
