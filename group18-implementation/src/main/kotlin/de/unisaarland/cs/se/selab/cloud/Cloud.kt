package de.unisaarland.cs.se.selab.cloud

const val CLOUD_MOVES_LEFT = 10

/**
 * Cloud class that represents the cloud data
 */
data class Cloud(
    var id: Int,
    var duration: Int,
    var location: Int,
    var amount: Int
) {

    var availableMovesLeft: Int = CLOUD_MOVES_LEFT
}
