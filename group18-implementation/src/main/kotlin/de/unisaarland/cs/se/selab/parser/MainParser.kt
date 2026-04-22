package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.Simulation
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick

/**
 * Main parser
 */
class MainParser(val startYearTick: YearTick, val maxTicks: Int) {
    var farms = emptyMap<Int, Farm>()

    /**
     * parse all the files and return the simulation
     */
    fun parse(
        mapFile: String,
        farmsFile: String,
        scenarioFile: String
    ): Simulation? {
        val tileManager = TileManager()
        val mapParser = MapParser(tileManager)
        if (mapParser.parseMap(mapFile)) {
            Logger.logParseSuccess(mapFile)
            return parseFarms(tileManager, farmsFile, scenarioFile)
        } else {
            Logger.logInvalidFile(mapFile)
            return null
        }
    }

    private fun parseFarms(tileManager: TileManager, farmsFile: String, scenarioFile: String): Simulation? {
        val farmsAndMachines = FarmsParser(tileManager).parseFarmsAndMachines(farmsFile)
        if (farmsAndMachines != null) {
            Logger.logParseSuccess(farmsFile)
            this.farms = farmsAndMachines.first
            val machines = farmsAndMachines.second
            return parseScenario(tileManager, scenarioFile, machines)
        } else {
            Logger.logInvalidFile(farmsFile)
            return null
        }
    }
    private fun parseScenario(
        tileManager: TileManager,
        scenarioFile: String,
        machines: MutableMap<Int, Machine>
    ): Simulation? {
        val scenarioParser = ScenarioParser(tileManager, machines, farms)
        val clouds = scenarioParser.parseClouds(scenarioFile)
        if (clouds != null) {
            val cloudHandler = CloudHandler(tileManager, clouds)
            val incidents = scenarioParser.parseIncidents(scenarioFile, cloudHandler)
            if (incidents != null) {
                Logger.logParseSuccess(scenarioFile)
                return Simulation(tileManager, cloudHandler, farms, incidents, 0, startYearTick, maxTicks)
            } else {
                Logger.logInvalidFile(scenarioFile)
                return null
            }
        } else {
            Logger.logInvalidFile(scenarioFile)
            return null
        }
    }
}
