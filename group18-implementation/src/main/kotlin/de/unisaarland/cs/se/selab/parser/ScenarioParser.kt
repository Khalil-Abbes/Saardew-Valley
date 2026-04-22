package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.farm.SowingPlan
import de.unisaarland.cs.se.selab.incident.AnimalAttackIncident
import de.unisaarland.cs.se.selab.incident.BeeHappyIncident
import de.unisaarland.cs.se.selab.incident.BrokenMachineIncident
import de.unisaarland.cs.se.selab.incident.CityExpansionIncident
import de.unisaarland.cs.se.selab.incident.CloudCreationIncident
import de.unisaarland.cs.se.selab.incident.DroughtIncident
import de.unisaarland.cs.se.selab.incident.Incident
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.json.JSONObject
import java.io.File

const val ID = "id"
const val LOCATION = "location"
const val RADIUS = "radius"
const val TICK = "tick"
const val DURATION = "duration"

/**
 * parser for the scenario file
 */
class ScenarioParser(val tileManager: TileManager, val machineByID: Map<Int, Machine>, val farmsByID: Map<Int, Farm>) {

    val idToCloud = mutableMapOf<Int, Cloud>()
    val locationToCloud = mutableMapOf<Int, Cloud>()
    val idToIncident = mutableMapOf<Int, Incident>()
    val tilesToVillage = mutableMapOf<Int, Int>()
    val tickToCloudsCreated = mutableMapOf<Int, List<Int>>()

    /**
     * parses the clouds
     */
    fun parseClouds(scenarioFile: String): MutableList<Cloud>? {
        if (!Validator.validateFile(scenarioFile, "classpath://schema/scenario.schema")) return null
        val clouds = JSONObject(File(scenarioFile).readText()).getJSONArray("clouds")
        for (i in 0 until clouds.length()) {
            val cloud = clouds.getJSONObject(i)
            if (validateCloud(cloud)) {
                createCloud(cloud)
            } else {
                return null
            }
        }
        return idToCloud.values.toMutableList()
    }

    private fun validateCloud(cloud: JSONObject): Boolean {
        val cloudID = cloud.optInt(ID)
        val cloudLocation = cloud.optInt(LOCATION)
        val checkLocation = locationToCloud[cloudLocation]
        val checkDuplicateID = idToCloud[cloudID]
        val cloudTile = tileManager.getTileById(cloudLocation)
        if (cloudTile != null) {
            return checkLocation == null && checkDuplicateID == null && cloudTile.categoryType != TileCategory.VILLAGE
        }
        return false
    }

    private fun createCloud(cloud: JSONObject) {
        val cloudID = cloud.optInt(ID)
        val cloudLocation = cloud.optInt(LOCATION)
        val cloudDuration = cloud.optInt(DURATION)
        val cloudAmount = cloud.optInt("amount")
        val cloudResult = Cloud(cloudID, cloudDuration, cloudLocation, cloudAmount)
        idToCloud[cloudID] = cloudResult
        locationToCloud[cloudLocation] = cloudResult
    }

    /**
     * parses the incidents
     */
    fun parseIncidents(scenarioFile: String, cloudHandler: CloudHandler): List<Incident>? {
        val incidents = JSONObject(File(scenarioFile).readText()).getJSONArray("incidents")
        val incidentsSorted = (0 until incidents.length()).map { i -> incidents.getJSONObject(i) }
            .sortedWith(
                compareBy<JSONObject> { it.optInt(TICK) }
                    .thenBy { it.optInt(ID) }
            )
        incidentsSorted.forEach {
            if (!validateIncident(it, cloudHandler)) {
                return null
            }
        }
        farmsByID.values.forEach {
            if (!crossValidateSowingPlans(it.sowingPlans)) {
                return null
            }
        }
        return idToIncident.values.toList()
    }

    private fun crossValidateSowingPlans(sowingPlans: List<SowingPlan>): Boolean {
        sowingPlans.forEach {
            if (!validateSowingPlan(it)) {
                return false
            }
        }
        return true
    }
    private fun validateSowingPlan(sowingPlan: SowingPlan): Boolean {
        val fields = sowingPlan.getFields()
        // val plant = sowingPlan.plantType
        return fields.any { !tilesToVillage.containsKey(it.id) }
    }
    private fun validateIncident(incident: JSONObject, cloudHandler: CloudHandler): Boolean {
        val incidentID = incident.optInt(ID)
        val incidentType = incident.optString("type")
        idToIncident[incidentID] ?: return when (incidentType) {
            "CLOUD_CREATION" -> validateCloudCreation(incident, cloudHandler)
            "ANIMAL_ATTACK" -> validateAnimalAttack(incident)
            "BEE_HAPPY" -> validateBeeHappy(incident)
            "DROUGHT" -> validateDrought(incident)
            "BROKEN_MACHINE" -> validateBrokenMachine(incident)
            "CITY_EXPANSION" -> validateCityExpansion(incident, cloudHandler)
            else -> false
        }
        return false
    }
    private fun validateCloudCreation(incident: JSONObject, cloudHandler: CloudHandler): Boolean {
        val incidentLocation = incident.optInt(LOCATION)
        val incidentRadius = incident.optInt(RADIUS)
        val incidentTick = incident.optInt(TICK)
        val incidentTile = tileManager.getTileById(incidentLocation) ?: return false

        val affectedTiles = tileManager.getAllNeighborTilesInRadius(incidentTile, incidentRadius)
        if (affectedTiles.any { it.categoryType != TileCategory.VILLAGE && tilesToVillage[it.id] == null }) {
            val cloudsCreated = tickToCloudsCreated[incidentTick]
            if (cloudsCreated != null && cloudsCreated.intersect(
                    affectedTiles.filter { it.categoryType != TileCategory.VILLAGE }
                        .map { it.id }.toSet()
                ).isNotEmpty()
            ) {
                return false
            } else {
                tickToCloudsCreated[incidentTick] = tickToCloudsCreated.getOrDefault(incidentTick, emptyList()) +
                    affectedTiles.map { it.id }
                createCloudIncident(incident, incidentLocation, incidentRadius, cloudHandler)
                return true
            }
        }
        return false
    }

    private fun createCloudIncident(
        incident: JSONObject,
        incidentLocation: Int,
        incidentRadius: Int,
        cloudHandler: CloudHandler
    ) {
        val incidentID = incident.optInt(ID)
        val incidentTick = incident.optInt(TICK)
        val incidentDuration = incident.optInt(DURATION)
        val incidentAmount = incident.optInt("amount")
        val cloudCreation = CloudCreationIncident(
            id = incidentID,
            tick = incidentTick,
            cloudHandler = cloudHandler,
            duration = incidentDuration,
            location = incidentLocation,
            radius = incidentRadius,
            amount = incidentAmount
        )
        idToIncident[incidentID] = cloudCreation
    }
    private fun validateAnimalAttack(incident: JSONObject): Boolean {
        val incidentLocation = incident.optInt(LOCATION)
        val incidentRadius = incident.optInt(RADIUS)
        tileManager.getTileById(incidentLocation) ?: return false
        val affectedTiles = tileManager.getAllNeighborTilesInRadius(incidentLocation, incidentRadius)
        if (affectedTiles.any { it.categoryType == TileCategory.FOREST }) {
            createAnimalAttack(incident, incidentLocation, incidentRadius)
            return true
        }
        return false
    }
    private fun createAnimalAttack(incident: JSONObject, incidentLocation: Int, incidentRadius: Int) {
        val incidentID = incident.optInt(ID)
        val incidentTick = incident.optInt(TICK)
        val createdAnimalAttack = AnimalAttackIncident(
            id = incidentID,
            tick = incidentTick,
            tileManager = tileManager,
            location = incidentLocation,
            radius = incidentRadius
        )
        idToIncident[incidentID] = createdAnimalAttack
    }
    private fun validateBeeHappy(incident: JSONObject): Boolean {
        val incidentLocation = incident.optInt(LOCATION)
        val incidentRadius = incident.optInt(RADIUS)
        tileManager.getTileById(incidentLocation) ?: return false
        val affectedTiles = tileManager.getAllNeighborTilesInRadius(incidentLocation, incidentRadius)
        if (affectedTiles.any { it.categoryType == TileCategory.MEADOW }) {
            createBeeHappy(incident, incidentLocation, incidentRadius)
            return true
        }
        return false
    }
    private fun createBeeHappy(incident: JSONObject, incidentLocation: Int, incidentRadius: Int) {
        val incidentID = incident.optInt(ID)
        val incidentTick = incident.optInt(TICK)
        val incidentEffect = incident.optInt("effect")
        val createdBeeHappy = BeeHappyIncident(
            id = incidentID,
            tick = incidentTick,
            tileManager = tileManager,
            location = incidentLocation,
            radius = incidentRadius,
            effect = incidentEffect
        )
        idToIncident[incidentID] = createdBeeHappy
    }
    private fun validateDrought(incident: JSONObject): Boolean {
        val incidentLocation = incident.optInt(LOCATION)
        tileManager.getTileById(incidentLocation) ?: return false
        val incidentRadius = incident.optInt(RADIUS)
        val affectedTiles = tileManager.getAllNeighborTilesInRadius(incidentLocation, incidentRadius)
        if (affectedTiles.any {
                (it.categoryType == TileCategory.FIELD || it.categoryType == TileCategory.PLANTATION) &&
                    !tilesToVillage.containsKey(it.id)
            }
        ) {
            createDrought(incident, incidentLocation, incidentRadius)
            return true
        }
        return false
    }
    private fun createDrought(incident: JSONObject, incidentLocation: Int, incidentRadius: Int) {
        val incidentID = incident.optInt(ID)
        val incidentTick = incident.optInt(TICK)
        val createDrought = DroughtIncident(
            id = incidentID,
            tick = incidentTick,
            tileManager = tileManager,
            location = incidentLocation,
            radius = incidentRadius
        )
        idToIncident[incidentID] = createDrought
    }
    private fun validateBrokenMachine(incident: JSONObject): Boolean {
        val incidentMachine = incident.optInt("machineId")
        val checkMachine = machineByID[incidentMachine]
        if (checkMachine != null) {
            createBrokenMachine(incident, checkMachine)
            return true
        }
        return false
    }
    private fun createBrokenMachine(incident: JSONObject, incidentMachine: Machine) {
        val incidentID = incident.optInt(ID)
        val incidentTick = incident.optInt(TICK)
        val incidentDuration = incident.optInt(DURATION)
        val createdBrokenMachine = BrokenMachineIncident(
            id = incidentID,
            tick = incidentTick,
            machine = incidentMachine,
            duration = incidentDuration
        )
        idToIncident[incidentID] = createdBrokenMachine
    }
    private fun validateCityExpansion(incident: JSONObject, cloudHandler: CloudHandler): Boolean {
        val incidentLocation = incident.optInt(LOCATION)
        val incidentTile = tileManager.getTileById(incidentLocation)
        if (incidentTile == null ||
            incidentTile.categoryType !in listOf(TileCategory.FIELD, TileCategory.ROAD) ||
            tilesToVillage[incidentLocation] != null

        ) {
            return false
        } else {
            val adjoiningTiles = tileManager.getAllNeighborTilesInRadius(incidentLocation, 1)
            if (adjoiningTiles.any { tile ->
                    tile.categoryType == TileCategory.VILLAGE ||
                        tilesToVillage[tile.id] != null
                } && adjoiningTiles.all { it.categoryType != TileCategory.FOREST }
            ) {
                createCityExpansion(incident, incidentLocation, cloudHandler = cloudHandler)
                return true
            }
        }
        return false
    }
    private fun createCityExpansion(incident: JSONObject, incidentLocation: Int, cloudHandler: CloudHandler) {
        val incidentID = incident.optInt(ID)
        val incidentTick = incident.optInt(TICK)
        if (tilesToVillage[incidentLocation] == null) {
            tilesToVillage[incidentLocation] = incidentTick
        }
        val createdCityExpansion = CityExpansionIncident(
            id = incidentID,
            tick = incidentTick,
            farms = farmsByID.values.toList(),
            tileManager = tileManager,
            location = incidentLocation,
            cloudHandler = cloudHandler
        )
        idToIncident[incidentID] = createdCityExpansion
    }
}
