package de.unisaarland.cs.se.selab.plant

/**
 * class for plants that can be on plantations
 */
class PlantationPlant(
    override val type: PlantationPlantType,
) : Plant(type)
