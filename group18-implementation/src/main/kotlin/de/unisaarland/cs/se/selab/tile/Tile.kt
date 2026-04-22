package de.unisaarland.cs.se.selab.tile

import de.unisaarland.cs.se.selab.simulation.Coordinate

/**
 * superclass for all tiles
 */
open class Tile(
    val id: Int,
    val coordinate: Coordinate,
    var categoryType: TileCategory,
    val shed: Boolean,
    var farm: Int?,
    var direction: Direction?
)
