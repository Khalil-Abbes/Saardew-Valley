package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.Field

/**
 * Sowing plan data class
 */
class SowingPlan(
    val id: Int,
    val tick: Int,
    val plantType: FieldPlantType,
    private val fields: List<Field>?,
    var fulfilled: Boolean = false,
    private val location: Int?,
    private val radius: Int?,
    private val tileManager: TileManager
) {

    /**
     * get affected tiles dynamically
     */
    fun getFields(): List<Field> {
        if (fields != null) {
            return fields
        } else if (location != null && radius != null) {
            return tileManager.getAllNeighborTilesInRadius(location, radius).filterIsInstance<Field>()
        }
        return emptyList()
    }
}
