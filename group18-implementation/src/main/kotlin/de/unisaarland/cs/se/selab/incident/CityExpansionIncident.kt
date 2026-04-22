package de.unisaarland.cs.se.selab.incident

import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.parser.TileFactory
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory

/**
 * [City Expansion incident class]
 * Converts a road or field tile into a village tile at the specified location.
 * If a field is converted, it is removed from its farm's field list.
 * Any machines on the converted tile remain there.
 * If a cloud is involved in city expansion, the cloud handler is used.
 */
class CityExpansionIncident(
    id: Int,
    tick: Int,
    val farms: List<Farm>,
    val tileManager: TileManager,
    val location: Int,
    val cloudHandler: CloudHandler // cloud handler to handle city expansion effects
) :
    Incident(id, tick) {
    override fun perform(currentTick: Int, currentYearTick: YearTick) {
        // check if location is farm land
        val t: Tile =
            tileManager.getTileById(location) ?: return

        // only proceed if tile is road or field
        when (t.categoryType) {
            // convert to village
            TileCategory.ROAD -> {
                handleRoad(t)
            }
            TileCategory.FIELD -> {
                handleField(t)
            }
            // other tile categories are not affected
            else -> { }
        }

        // for cloud handler to handle city expansion effects
        cloudHandler
            .handleCityExpansion(t)
    }

    // helper function to convert road to village tile
    private fun handleRoad(t: Tile) {
        t.categoryType = TileCategory.VILLAGE
        t.direction = null // we remove direction as village has no direction

        // log incident
        Logger
            .logIncident(
                id,
                this,
                listOf(t.id)
            )
    }

    // helper function to convert field to village tile
    private fun handleField(t: Tile) {
        // check if tile is field
        val field =
            t as? Field ?: return

        // log incident with field id
        Logger
            .logIncident(
                id,
                this,
                listOf(field.id)
            )

        // removed killPlant as we just replace the field tile with village

        // replace field with village tile
        val newVillageTile =
            TileFactory.createVillageTile(
                field.id,
                field.coordinate
            )

        // update tile in tile manager
        tileManager.updateTile(newVillageTile)

        // remove field from farm's owning list
        farms
            .forEach { farm ->
                if (farm.id == field.farm) {
                    farm.removeField(field)
                }
            }
        // if any machine is currently stuck on this tile, keep it there by updating its location there
        farms.forEach { farm ->
            farm.machines
                .forEach { machine ->
                    if (machine.location.id == location) {
                        machine.location = newVillageTile
                    }
                }
        }
    }

    // string representation of the incident
    override fun toString(): String {
        return "CITY_EXPANSION"
    }
}
