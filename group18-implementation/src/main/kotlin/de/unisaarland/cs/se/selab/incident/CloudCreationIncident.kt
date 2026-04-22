package de.unisaarland.cs.se.selab.incident

import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.TileCategory

/**
 * [Cloud Creation incident class]
 * Creates clouds at a specified location with a given radius, duration, and amount of water.
 * All tiles within the radius that are not villages will be affected by the cloud creation.
 */
class CloudCreationIncident(
    id: Int,
    tick: Int,
    val cloudHandler: CloudHandler,
    val duration: Int,
    val location: Int,
    val radius: Int,
    val amount: Int // amount of water in the clouds
) :
    Incident(id, tick) {
    override fun perform(currentTick: Int, currentYearTick: YearTick) {
        // get all tiles in radius excluding villages, sorted by id
        val tiles = cloudHandler
            .tileManager
            .getAllNeighborTilesInRadius(
                location,
                radius
            )
            .filter {
                it.categoryType != TileCategory.VILLAGE
            }
            .sortedBy {
                it.id
            }

        // log incident with affected tile ids
        Logger
            .logIncident(
                id,
                this,
                tiles.map {
                    it.id
                }
            )

        // create clouds on all affected
        tiles
            .forEach {
                cloudHandler.createCloud(
                    it.id,
                    duration,
                    amount
                )
            }
    }

    // string representation of the incident
    override fun toString(): String {
        return "CLOUD_CREATION"
    }
}
