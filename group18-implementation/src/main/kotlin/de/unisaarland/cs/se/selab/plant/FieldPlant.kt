package de.unisaarland.cs.se.selab.plant

/**
 * plants that can be on field tiles
 */
class FieldPlant(
    override val type: FieldPlantType
) : Plant(type) {

    /**
     * check if the plant can be wed in the currentTick
     */
    fun isInWeedingWindow(sowingTick: Int, currentTick: Int): Boolean {
        return type.weedingRule.contains(sowingTick, currentTick)
    }
}
