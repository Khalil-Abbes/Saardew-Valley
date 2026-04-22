package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.farm.SowingPlan
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

const val MACHINE_MAXIMUM_DURATION = 14
const val NAME = "name"

/**
 * parser for the farm file
 */
class FarmsParser(val tileManager: TileManager) {
    val idToFarm = mutableMapOf<Int, Farm>()
    val idToMachine = mutableMapOf<Int, Machine>()
    val idToSowingPlan = mutableMapOf<Int, SowingPlan>()
    val nameToMachine = mutableMapOf<String, Machine>()
    val nameToFarm = mutableMapOf<String, Farm>()

    /**
     * parses the machines and farms and returns them
     */
    fun parseFarmsAndMachines(farmsFile: String): Pair<MutableMap<Int, Farm>, MutableMap<Int, Machine>>? {
        if (!Validator.validateFile(farmsFile, "classpath://schema/farms.schema")) return null
        val farms = JSONObject(File(farmsFile).readText()).getJSONArray("farms")
        for (i in 0 until farms.length()) {
            validateFarm(farms.getJSONObject(i)) ?: return null
        }
        if (validateTileBelongings()) {
            return Pair(idToFarm, idToMachine)
        }
        return null
    }

    private fun validateTileBelongings(): Boolean {
        val fieldsValid = checkFields()
        val plantationsValid = checkPlantations()
        val farmsteadsValid = checkFarmsteads()
        return fieldsValid && plantationsValid && farmsteadsValid
    }
    private fun checkFields(): Boolean {
        tileManager.getFields().forEach {
            val tileFarm = idToFarm[it.farm]
            if (tileFarm != null) {
                val fields = tileFarm.fields
                if (it !in fields) {
                    return false
                }
            } else {
                return false
            }
        }
        return true
    }
    private fun checkPlantations(): Boolean {
        tileManager.getPlantations().forEach {
            val tileFarm = idToFarm[it.farm]
            if (tileFarm != null) {
                val plantations = tileFarm.plantations
                if (it !in plantations) {
                    return false
                }
            } else {
                return false
            }
        }
        return true
    }
    private fun checkFarmsteads(): Boolean {
        tileManager.getAllTiles().filter { it.categoryType == TileCategory.FARMSTEAD }.forEach {
            val tileFarm = idToFarm[it.farm]
            if (tileFarm != null) {
                val farmsteads = tileFarm.farmsteads
                if (it !in farmsteads) {
                    return false
                }
            } else {
                return false
            }
        }
        return true
    }

    /**
     * function that validates all conditions for the farm object
     */
    fun validateFarm(farm: JSONObject): Farm? {
        val farmsteads = validateFarmsteads(farm.getJSONArray("farmsteads"), farm.optInt(ID))
        val fields = validateFields(farm.getJSONArray("fields"), farm.optInt(ID))
        val plantations = validatePlantations(farm.getJSONArray("plantations"), farm.optInt(ID))
        if (farmsteads == null || fields == null || plantations == null) {
            return null
        }

        val machines = validateMachines(farm.getJSONArray("machines"), farm.optInt(ID)) ?: return null
        if (machines.isEmpty() || farmsteads.isEmpty() || (plantations + fields).isEmpty()) return null
        // val fieldPlants = fields.flatMap { it.possiblePlants }.toSet()
        val sowingPlans = validateSowingPlans(
            farm.getJSONArray("sowingPlans"),
            farm.optInt(ID)
        ) ?: return null
        return createFarm(farm, farmsteads, fields, plantations, machines, sowingPlans)
    }

    private fun createFarm(
        farm: JSONObject,
        farmsteads: List<Tile>,
        fields: List<Field>,
        plantations: List<Plantation>,
        machines: List<Machine>,
        sowingPlans: List<SowingPlan>
    ): Farm? {
        val farmID = farm.optInt(ID)
        val farmName = farm.optString(NAME)
        val checkDuplicate = idToFarm[farmID] == null && nameToFarm[farmName] == null
        if (checkDuplicate) {
            val createdFarm = Farm(
                farmID,
                farmName,
                farmsteads,
                fields,
                plantations.toMutableList(),
                machines.toMutableList(),
                sowingPlans.toMutableList()
            )
            idToFarm[farmID] = createdFarm
            nameToFarm[farmName] = createdFarm
            return createdFarm
        }
        return null
    }
    private fun validateSowingPlans(
        sowingPlans: JSONArray?,
        farmID: Int
    ): List<SowingPlan>? {
        sowingPlans ?: return null
        val createdSowingPlans = mutableListOf<SowingPlan>()
        for (i in 0 until sowingPlans.length()) {
            val sowingPlan = validateSowingPlan(sowingPlans.getJSONObject(i), farmID)
            if (sowingPlan != null) {
                createdSowingPlans.add(sowingPlan)
            } else {
                return null
            }
        }
        return createdSowingPlans
    }
    private fun validateSowingPlan(
        sowingPlan: JSONObject,
        farmID: Int,
    ): SowingPlan? {
        val sowingID = sowingPlan.optInt(ID)
        if (idToSowingPlan[sowingID] == null) {
            val sowingPlanPlant = when (sowingPlan.getString("plant")) {
                "POTATO" -> FieldPlantType.POTATO
                "WHEAT" -> FieldPlantType.WHEAT
                "OAT" -> FieldPlantType.OAT
                "PUMPKIN" -> FieldPlantType.PUMPKIN
                else -> return null
            }
            return createSowingPlan(sowingPlan, farmID, sowingPlanPlant)
            // return validateHasMachines(sowingPlan, farmID, sowingPlanPlant, machines)
        }
        return null
    }
    private fun createSowingPlan(
        sowingPlan: JSONObject,
        farmID: Int,
        sowingPlanPlant: FieldPlantType
    ): SowingPlan? {
        val sowingPlanID = sowingPlan.optInt(ID)
        val sowingPlanTick = sowingPlan.optInt("tick")
        val sowingFields = sowingPlan.optJSONArray("fields")

        if (sowingFields == null) {
            val sowingPlanLocation = sowingPlan.optInt(LOCATION)
            val sowingPlanRadius = sowingPlan.optInt(RADIUS)
            return validateLocRadiusSowingPlan(
                sowingPlanID,
                sowingPlanTick,
                sowingPlanPlant,
                sowingPlanLocation,
                sowingPlanRadius,
                farmID
            )
        } else if (sowingFields.length() > 0) {
            val affectedTiles = mutableListOf<Field>()
            for (i in 0 until sowingFields.length()) {
                val checkTile = tileManager.getTileById(sowingFields.optInt(i))
                if (checkTile != null && checkTile is Field) {
                    affectedTiles.add(checkTile)
                } else {
                    return null
                }
            }
            if (validFieldForSowingPlan(farmID, affectedTiles)) {
                val createdSowingPlan = SowingPlan(
                    sowingPlanID,
                    sowingPlanTick,
                    sowingPlanPlant,
                    affectedTiles,
                    location = null,
                    radius = null,
                    tileManager = tileManager
                )
                idToSowingPlan[sowingPlanID] = createdSowingPlan
                return createdSowingPlan
            }
        }
        return null
    }

    private fun validateLocRadiusSowingPlan(
        id: Int,
        tick: Int,
        plant: FieldPlantType,
        location: Int,
        radius: Int,
        farmID: Int
    ): SowingPlan? {
        tileManager.getTileById(location) ?: return null
        val fields = tileManager.getAllNeighborTilesInRadius(location, radius).filterIsInstance<Field>()
        if (fields.isNotEmpty() && fields.any { it.farm == farmID }) {
            val createdSowingPlan = SowingPlan(
                id,
                tick,
                plant,
                null,
                location = location,
                radius = radius,
                tileManager = tileManager
            )
            idToSowingPlan[id] = createdSowingPlan
            return createdSowingPlan
        }
        return null
    }
    private fun validFieldForSowingPlan(
        farmID: Int,
        affectedTiles: List<Field>
    ): Boolean {
        return affectedTiles.any { it.farm == farmID }
    }
    private fun validateMachines(machines: JSONArray?, farmID: Int): List<Machine>? {
        machines ?: return null
        val createdMachines = mutableListOf<Machine>()
        for (i in 0 until machines.length()) {
            val machine = validateMachine(machines.getJSONObject(i), farmID)
            if (machine != null) {
                createdMachines.add(machine)
            } else {
                return null
            }
        }
        return createdMachines
    }

    /**
     * validate all machine properties
     */
    fun validateMachine(machine: JSONObject, farmID: Int): Machine? {
        val machineID = machine.optInt(ID)
        val checkDuplicate = idToMachine[machineID]
        val machineName = machine.optString(NAME)
        val checkDuplicateName = nameToMachine[machineName]
        val hasDuplicate = checkDuplicate == null && checkDuplicateName == null
        val machineLocation = machine.optInt(LOCATION)
        val machineTile = tileManager.getTileById(machineLocation) ?: return null
        if (hasDuplicate && machineTile.shed && machineTile.farm == farmID) {
            return createMachine(machine, farmID, machineTile)
        }
        return null
    }
    private fun createMachine(machine: JSONObject, farmID: Int, tile: Tile): Machine? {
        val machineID = machine.optInt(ID)
        val machineName = machine.optString(NAME)
        val actions = createActionList(machine.getJSONArray("actions"))
        val plants = createPlantsList(machine.getJSONArray("plants"))
        val machineDuration = machine.optInt("duration")
        if (actions.isNotEmpty() && plants.isNotEmpty() && machineDuration in 1..MACHINE_MAXIMUM_DURATION) {
            val createdMachine = Machine(
                machineID,
                machineName,
                actions,
                plants,
                machineDuration,
                tile,
                tileManager,
                farmID
            )
            idToMachine[machineID] = createdMachine
            nameToMachine[machineName] = createdMachine
            return createdMachine
        }
        return null
    }

    /**
     * create plantsList list from string array
     */
    fun createPlantsList(plants: JSONArray?): List<PlantType> {
        plants ?: return emptyList()
        val result = mutableListOf<PlantType>()
        for (i in 0 until plants.length()) {
            val plant = plants.optString(i)
            when (plant) {
                "POTATO" -> result.add(FieldPlantType.POTATO)
                "WHEAT" -> result.add(FieldPlantType.WHEAT)
                "OAT" -> result.add(FieldPlantType.OAT)
                "PUMPKIN" -> result.add(FieldPlantType.PUMPKIN)
                "APPLE" -> result.add(PlantationPlantType.APPLE)
                "GRAPE" -> result.add(PlantationPlantType.GRAPE)
                "ALMOND" -> result.add(PlantationPlantType.ALMOND)
                "CHERRY" -> result.add(PlantationPlantType.CHERRY)
            }
        }
        return result
    }

    /**
     * create actionType list from string array
     */
    fun createActionList(actions: JSONArray?): List<ActionType> {
        actions ?: return emptyList()
        val result = mutableListOf<ActionType>()
        for (i in 0 until actions.length()) {
            val action = actions.optString(i)
            when (action) {
                "SOWING" -> result.add(ActionType.SOWING)
                "CUTTING" -> result.add(ActionType.CUTTING)
                "MOWING" -> result.add(ActionType.MOWING)
                "WEEDING" -> result.add(ActionType.WEEDING)
                "IRRIGATING" -> result.add(ActionType.IRRIGATING)
                "HARVESTING" -> result.add(ActionType.HARVESTING)
            }
        }
        return result
    }
    private fun validateFarmsteads(farmsteads: JSONArray?, farmID: Int): List<Tile>? {
        farmsteads ?: return null
        val farmsteadTiles = mutableListOf<Tile>()
        for (i in 0 until farmsteads.length()) {
            val farmstead = farmsteads.optInt(i)
            val checkFarmstead = tileManager.getTileById(farmstead) ?: return null
            if (checkFarmstead.categoryType == TileCategory.FARMSTEAD && checkFarmstead.farm == farmID) {
                checkFarmstead.farm = farmID
                farmsteadTiles.add(checkFarmstead)
            } else {
                return null
            }
        }
        return farmsteadTiles
    }

    private fun validateFields(fields: JSONArray?, farmID: Int): List<Field>? {
        fields ?: return null
        val fieldTiles = mutableListOf<Field>()
        for (i in 0 until fields.length()) {
            val field = fields.optInt(i)
            val checkField = tileManager.getTileById(field) ?: return null
            if (checkField is Field && checkField.farm == farmID) {
                checkField.farm = farmID
                fieldTiles.add(checkField)
            } else {
                return null
            }
        }
        return fieldTiles
    }

    private fun validatePlantations(plantations: JSONArray?, farmID: Int): List<Plantation>? {
        plantations ?: return null
        val plantationTiles = mutableListOf<Plantation>()
        for (i in 0 until plantations.length()) {
            val plantation = plantations.optInt(i)
            val checkPlantation = tileManager.getTileById(plantation) ?: return null
            if (checkPlantation is Plantation && checkPlantation.farm == farmID) {
                checkPlantation.farm = farmID
                plantationTiles.add(checkPlantation)
            } else {
                return null
            }
        }
        return plantationTiles
    }
}
