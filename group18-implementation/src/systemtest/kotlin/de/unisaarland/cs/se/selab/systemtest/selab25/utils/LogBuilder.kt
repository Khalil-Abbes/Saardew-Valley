package de.unisaarland.cs.se.selab.systemtest.selab25.utils

/**
 * log builder for easier log line creation in tests
 */
object LogBuilder {

    /**
     * test
     */
    object info {
        fun parseSuccess(filename: String) =
            "[INFO] Initialization Info: $filename successfully parsed and validated."
        fun simulationStart(yearTick: Int) =
            "[INFO] Simulation Info: Simulation started at tick $yearTick within the year."
        fun tickStart(simTick: Int, yearTick: Int) =
            "[INFO] Simulation Info: Tick $simTick started at tick $yearTick within the year."
        fun soilMoisture(fieldBelow: Int, plantationBelow: Int) =
            "[INFO] Soil Moisture: The soil moisture is below threshold in $fieldBelow FIELD" +
                " and $plantationBelow PLANTATION tiles."
        fun cloudMove(cloudId: Int, amountL: Int, from: Int, to: Int) =
            "[INFO] Cloud Movement: Cloud $cloudId with $amountL L water moved from tile $from to tile $to."
        fun cloudStuck(cloudId: Int, tileId: Int) =
            "[INFO] Cloud Dissipation: Cloud $cloudId got stuck on tile $tileId."
        fun cloudDissipates(cloudId: Int, tileId: Int) =
            "[INFO] Cloud Dissipation: Cloud $cloudId dissipates on tile $tileId."
        fun harvestEstimate(tileId: Int, amount: Int, plant: String) =
            "[INFO] Harvest Estimate: Harvest estimate on tile $tileId changed to $amount g of $plant."
    }

    /**
     * test
     */
    object debug {
        fun cloudSunOnMove(startTileId: Int, sunlight: Int) =
            "[DEBUG] Cloud Movement: On tile $startTileId, the amount of sunlight is $sunlight."
        fun cloudPosition(cloudId: Int, tileId: Int, sunlight: Int) =
            "[DEBUG] Cloud Position: Cloud $cloudId is on tile $tileId, where the amount of sunlight is $sunlight."
        fun missedActions(tileId: Int, vararg actions: String) =
            "[DEBUG] Harvest Estimate: Required actions on tile $tileId were not performed:" +
                " ${actions.joinToString(",")}."
        fun farmActiveSowingPlans(farmId: Int, ids: List<Int>) =
            "[DEBUG] Farm: Farm $farmId has the following active sowing plans it" +
                " intends to pursue in this tick: ${ids.joinToString(",")}."
    }

    /**
     * test
     */
    object important {
        fun invalidFile(filename: String) =
            "[IMPORTANT] Initialization Info: $filename is invalid."

        // commented out statsCalculating because of detekt stuff
        // fun statsCalculating() = "[IMPORTANT] Simulation Info: Simulation statistics are calculated."

        fun farmStart(farmId: Int) =
            "[IMPORTANT] Farm: Farm $farmId starts its actions."
        fun farmAction(machineId: Int, action: String, tileId: Int, durationDays: Int) =
            "[IMPORTANT] Farm Action: Machine $machineId performs $action on tile $tileId for $durationDays days."
        fun farmSowing(machineId: Int, plant: String, planId: Int) =
            "[IMPORTANT] Farm Sowing: Machine $machineId has sowed $plant according to sowing plan $planId."
        fun farmHarvest(machineId: Int, amount: Int, plant: String) =
            "[IMPORTANT] Farm Harvest: Machine $machineId has collected $amount g of $plant harvest."
        fun machineReturn(machineId: Int, shedTileId: Int) =
            "[IMPORTANT] Farm Machine: Machine $machineId is finished and returns to the shed at $shedTileId."
        fun machineUnload(machineId: Int, amount: Int, plant: String) =
            "[IMPORTANT] Farm Machine: Machine $machineId unloads $amount g of $plant harvest in the shed."
        fun machineReturnFail(machineId: Int) =
            "[IMPORTANT] Farm Machine: Machine $machineId is finished but failed to return."
        fun farmFinish(farmId: Int) =
            "[IMPORTANT] Farm: Farm $farmId finished its actions."
        fun incident(incidentId: Int, type: String, affectedTiles: List<Int>) =
            "[IMPORTANT] Incident: Incident $incidentId of type $type happened" +
                " and affected tiles ${affectedTiles.joinToString(",")}."
        fun cloudRain(cloudId: Int, tileId: Int, amountL: Int) =
            "[IMPORTANT] Cloud Rain: Cloud $cloudId on tile $tileId rained down $amountL L water."
        fun cloudUnion(fromId: Int, movingToId: Int, newId: Int, amountL: Int, duration: Int, tileId: Int) =
            "[IMPORTANT] Cloud Union: Clouds $fromId and $movingToId united to cloud $newId" +
                " with $amountL L water and duration $duration on tile $tileId."
        fun statsFarmCollected(farmId: Int, amount: Int) =
            "[IMPORTANT] Simulation Statistics: Farm $farmId collected $amount g of harvest."
        fun statsTotalPlant(plant: String, amount: Int) =
            "[IMPORTANT] Simulation Statistics: Total amount of $plant harvested: $amount g."
        fun statsRemaining(amount: Int) =
            "[IMPORTANT] Simulation Statistics: Total harvest estimate still in fields and plantations: $amount g."
        fun simEnd(tick: Int) =
            "[IMPORTANT] Simulation Info: Simulation ended at tick $tick."
    }
}
