package de.unisaarland.cs.se.selab.incident

import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.FarmableTile
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory

const val FARMABLE_RADIUS = 2

/**
 * [Bee Happy Incident class]
 * Bees make meadows happy and help adjacent farmable tiles to bloom.
 * All farmable tiles (fields and plantations) that can be pollinated within a distance of 2
 * from a meadow within the effect radius of the incident will receive the incident's effect.
 * Note: Potatoes are not affected by bee pollination.
 */
class BeeHappyIncident(
    id: Int,
    tick: Int,
    val tileManager: TileManager,
    val location: Int,
    val radius: Int,
    val effect: Int
) :
    Incident(id, tick) {

    override fun perform(currentTick: Int, currentYearTick: YearTick) {
        // get center tile
        val center =
            tileManager
                .getTileById(location) ?: return

        // meadows within radius
        val meadows: Set<Tile> =
            tileManager
                .getAllNeighborTilesInRadius(
                    center.id,
                    radius
                )
                .filter {
                    it.categoryType == TileCategory.MEADOW // only meadows
                }
                .toSet()

        // from meadows, collect farmable that can be pollinated within distance 2
        val toBloom = meadows
            .flatMap { meadow ->
                tileManager
                    .getAllNeighborTilesInRadius(
                        meadow.id, // for each meadow
                        FARMABLE_RADIUS // get neighbors within distance 2
                    )
            }
            .mapNotNull {
                it as? FarmableTile<*>
            }
            .toSet()
            .filter {
                it.canBePollinated(
                    currentTick,
                    currentYearTick
                )
            }
            .sortedBy {
                it.id
            }

        // apply effect if not potato and collect impacted ids
        val impacted =
            mutableListOf<Int>()

        // apply effect to all collected farmable tiles
        for (ft in toBloom) {
            val p = ft.plant

            // only if not potato
            if (p != null &&
                p.type != FieldPlantType.POTATO
            ) {
                ft.addIncidentEffect(effect)
            }

            // collect impacted ids
            impacted.add(ft.id)
        }

        // 4) log impacted tiles (ascending)
        Logger
            .logIncident(
                id,
                this,
                impacted.sorted()
            )
    }

    // string representation of the incident
    override fun toString(): String {
        return "BEE_HAPPY"
    }
}
