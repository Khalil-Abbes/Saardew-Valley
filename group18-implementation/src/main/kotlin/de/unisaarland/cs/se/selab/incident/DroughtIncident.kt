package de.unisaarland.cs.se.selab.incident

import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation

const val HUNDRED_PERCENT = 100

/**
 * [Drought incident class]
 * Droughts affect all plantations and fields within a specified radius from a given location.
 * For Plantations: ML and HE set to 0, and the plantation is marked as permanently disabled.
 * For Fields: ML and HE set to 0, the current plant is killed, and
 * the field is set to fallow starting from the current tick.
 */
class DroughtIncident(
    id: Int,
    tick: Int,
    val tileManager: TileManager,
    val location: Int,
    val radius: Int
) :
    Incident(id, tick) {

    // perform the drought incident
    override fun perform(currentTick: Int, currentYearTick: YearTick) {
        // collect impacted tile ids
        val impactedIds =
            mutableSetOf<Int>()

        // all tiles in radius affected by drought (does it include the center?)
        val affected =
            tileManager.getAllNeighborTilesInRadius(
                location,
                radius
            )

        for (t in affected) {
            when (t) {
                is Plantation -> {
                    // set current HE to 0
                    t.currentMoisture = 0

                    // kill plant and disable permanently (in Plantation class)
                    t.killPlant(currentTick)

                    // add id to impacted
                    impactedIds.add(t.id)

                    // add incident effect -100% (killing plant basically)
                    t.addIncidentEffect(
                        -HUNDRED_PERCENT
                    )
                }
                is Field -> {
                    // if not already fallow, set lastFallow to current tick
                    if (!t.isFallow(currentTick)) {
                        t.lastFallow = currentTick
                    }

                    // drought happened this tick set to current tick
                    t.droughtHappenedTick = currentTick

                    // set ML 0
                    t.currentMoisture = 0

                    // add id to impacted
                    impactedIds.add(t.id)

                    // add incident effect -100% (killing plant basically)
                    t.addIncidentEffect(
                        -HUNDRED_PERCENT
                    )
                }
            }
        }

        // log impacted tiles (ascending)
        Logger
            .logIncident(
                id,
                this,
                impactedIds.sorted()
            )
    }

    // string representation of the incident
    override fun toString(): String {
        return "DROUGHT"
    }
}
