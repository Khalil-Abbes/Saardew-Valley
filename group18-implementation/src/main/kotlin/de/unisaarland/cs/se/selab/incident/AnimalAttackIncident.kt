package de.unisaarland.cs.se.selab.incident

import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.FarmableTile
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation

const val FIFTY = 50
const val TEN = 10

/**
 * [Animal attack incident class]
 * Animal attacks reduce the HE of adjacent fields from affected forests by 50%.
 * For plantations, Grape plantations HE reduced by 50%.
 * The rest have their mowing done for the current and next tick and 10% reduced HE.
 */
class AnimalAttackIncident(
    id: Int,
    tick: Int,
    val tileManager: TileManager,
    val location: Int,
    val radius: Int
) :
    Incident(id, tick) {

    override fun perform(currentTick: Int, currentYearTick: YearTick) {
        // find all forests in radius
        val forests = tileManager
            .getForestsInRadius(
                location,
                radius
            )

        // from forests, collect all adjacent fields and plantations
        val fieldsAndPlantations:
            MutableSet<FarmableTile<*>> =
            mutableSetOf()

        // for each forest, get all neighbors and filter for fields and plantations
        forests
            .forEach { forest ->
                tileManager
                    .getAllNeighborTilesInRadius(
                        forest.id,
                        1
                    )
                    .forEach {
                        if (it is Plantation || it is Field) {
                            fieldsAndPlantations.add(it)
                        }
                    }
            }

        // apply effects
        fieldsAndPlantations
            .sortedBy {
                it.id
            }
            .forEach { ft ->
                when (ft) {
                    is Field -> {
                        ft
                            .addIncidentEffect(-FIFTY)
                    }
                    is Plantation -> {
                        when (ft.plant.type) {
                            PlantationPlantType
                                .GRAPE -> { ft.addIncidentEffect(-FIFTY) }
                            PlantationPlantType
                                .APPLE,
                            PlantationPlantType
                                .ALMOND,
                            PlantationPlantType
                                .CHERRY -> {
                                // mowing done for current and next tick
                                ft.mowingDoneTicks.add(currentTick)
                                ft.mowingDoneTicks.add(currentTick + 1)

                                ft.addIncidentEffect(-TEN)
                            }
                        }
                    }
                }
            }

        // log impacted tiles (ascending)
        val ids = fieldsAndPlantations
            .map { it.id }
            .sorted()

        // log impacted tiles (ascending)
        Logger.logIncident(
            id,
            this,
            ids
        )
    }

    override fun toString(): String {
        return "ANIMAL_ATTACK"
    }
}
