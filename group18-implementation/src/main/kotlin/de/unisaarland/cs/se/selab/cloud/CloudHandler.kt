package de.unisaarland.cs.se.selab.cloud

import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.FarmableTile
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import kotlin.math.max
import kotlin.math.min

const val RAIN_THRESHOLD = 5000
const val CLOUD_AVAILABLE_MOVES = 10
const val REDUCE_SUNLIGHT_PER_TRAVERSE = 3
const val REDUCE_SUNLIGHT_CLOUDY_TILES = 50
const val CLOUD_ID_MAX_VALUE = Int.MAX_VALUE
const val CLOUD_AMOUNT_MAX_VALUE = Int.MAX_VALUE

/**
 * Cloud Handler is the main class that does the cloud phase
 */
class CloudHandler(val tileManager: TileManager, val clouds: MutableList<Cloud>) {
    var highestID: Int = clouds.maxOfOrNull { it.id }?.plus(1) ?: 0

    /**
     * main cloud handler function
     */
    fun run() {
        resetCloudMovements()
        handleClouds(mutableListOf())
        reduceSunlightAfterMovements()
    }

    private fun resetCloudMovements() {
        for (cloud in clouds) {
            cloud.availableMovesLeft = CLOUD_AVAILABLE_MOVES
        }
    }

    /**
     *  Handle logic of all clouds, reducing their duration after movement and dissipating it if equals 1
     * **/
    private fun handleClouds(processedClouds: MutableList<Cloud>) {
        val cloudsToProcess = clouds.filter {
            !processedClouds.contains(it)
        }.sortedBy { it.id }
        if (cloudsToProcess.isNotEmpty()) {
            val cloud = cloudsToProcess.first()
            val tile = tileManager.getTileById(cloud.location)
            if (tile?.categoryType == TileCategory.VILLAGE) {
                Logger.logCloudStuck(
                    cloud.id,
                    cloud.location
                )
                clouds.remove(cloud)
                handleClouds(processedClouds)
            }
            handleCloud(
                cloud,
                cloud.availableMovesLeft
            )
            processedClouds.add(cloud)
            if (cloud.duration == 1 && clouds.contains(cloud)) {
                Logger.logCloudDissipation(
                    cloud.id,
                    cloud.location
                )
                clouds.remove(cloud)
            } else if (clouds.contains(cloud) && cloud.duration != -1) {
                cloud.duration -= 1
            }
            handleClouds(processedClouds)
        }
    }

    /**
     * Handle single cloud until one of these happens
     * - the cloud got stuck / dissipate
     * - the cloud moved 10 times
     * - the cloud merge
     * */
    private fun handleCloud(cloud: Cloud, movesLeft: Int) {
        cloud.availableMovesLeft = movesLeft
        val dissipated = rain(cloud = cloud)
        if (dissipated) {
            clouds.remove(cloud)
            return
        }
        if (cloud.availableMovesLeft == 0) {
            return
        }
        val cloudMoved = moveCloud(movingCloud = cloud)
        if (cloudMoved) {
            val collisionCloud = checkCollision(cloud)
            if (collisionCloud != null) {
                merge(
                    collisionCloud,
                    cloud
                )
            } else {
                handleCloud(cloud, movesLeft - 1)
            }
        }
    }

    /**
     * cloud creation - used for incidents
     */
    fun createCloud(location: Int, duration: Int, amount: Int) {
        val newCloud = Cloud(
            highestID,
            duration,
            location,
            amount
        )
        highestID = run {
            check(
                highestID < CLOUD_ID_MAX_VALUE
            ) {
                "Cloud id must be less than max int"
            }
            highestID + 1
        }
        clouds.add(newCloud)

        val collidingCloud = checkCollision(newCloud)
        if (collidingCloud != null) {
            merge(collidingCloud, newCloud)
        }
    }

    /**
     * returns false if the cloud hasn't moved or moved to a village tile, else true.
     */
    private fun moveCloud(movingCloud: Cloud): Boolean {
        val startTile = tileManager
            .getTileById(movingCloud.location) ?: return false
        val destinationTile = tileManager
            .getNeighborInDirection(startTile) ?: return false
        movingCloud.location = destinationTile.id
        Logger.logCloudMovement(
            movingCloud.id,
            amountFluid = movingCloud.amount,
            startTileID = startTile.id,
            endTileID = destinationTile.id
        )

        if (startTile is FarmableTile<*>) {
            reduceSunlightAfterMovement(startTile)
        }
        if (destinationTile.categoryType == TileCategory.VILLAGE) {
            Logger.logCloudStuck(
                movingCloud.id,
                movingCloud.location
            )
            clouds.remove(movingCloud)
            return false
        }
        return true
    }

    private fun reduceSunlightAfterMovement(tile: FarmableTile<*>) {
        val newSunlight = max(
            tile.sunlightThisTick - REDUCE_SUNLIGHT_PER_TRAVERSE,
            0
        )
        tile.sunlightThisTick = newSunlight
        Logger.logCloudMovement(tile.id, newSunlight)
    }

    /**
     * function that handles raining;
     * returns false if the cloud should not dissipate after raining
     * return true otherwise
     */

    private fun rain(cloud: Cloud): Boolean {
        if (cloud.amount < RAIN_THRESHOLD) return false
        val currentTile = tileManager
            .getTileById(cloud.location) ?: return false
        if (currentTile is FarmableTile<*>) {
            val difference = currentTile.moistureCapacity - currentTile.currentMoisture
            if (difference == 0 || (
                    currentTile.currentMoisture < currentTile.moistureCapacity &&
                        difference < cloud.amount
                    )
            ) {
                rainToFillTile(
                    cloud,
                    difference,
                    currentTile
                )
                return false
            } else if (currentTile.currentMoisture < currentTile.moistureCapacity) {
                rainToEmptyCloudOnFarmable(cloud, currentTile)
                return true
            }
        }
        rainToEmptyCloud(cloud, currentTile)
        return true
    }

    private fun rainToEmptyCloudOnFarmable(cloud: Cloud, currentTile: FarmableTile<*>) {
        currentTile.currentMoisture += cloud.amount
        Logger.logCloudRain(
            cloudID = cloud.id,
            tileID = currentTile.id,
            cloud.amount
        )
        dissipate(cloud)
    }

    /**
     * Rains enough water to maximize the tile moisture
     * **/
    private fun rainToFillTile(cloud: Cloud, amount: Int, currentTile: FarmableTile<*>) {
        if (amount != 0) {
            currentTile.currentMoisture = currentTile.moistureCapacity
            cloud.amount -= amount
            Logger.logCloudRain(cloud.id, currentTile.id, amount)
        }
    }

    private fun rainToEmptyCloud(cloud: Cloud, currentTile: Tile) {
        Logger.logCloudRain(
            cloudID = cloud.id,
            tileID = currentTile.id,
            cloud.amount
        )
        dissipate(cloud)
    }

    private fun dissipate(cloud: Cloud) {
        Logger.logCloudDissipation(
            cloudID = cloud.id,
            tileID = cloud.location
        )
    }

    /**
     * function that returns a cloud collision
     */
    fun checkCollision(cloud: Cloud): Cloud? {
        return clouds.find { it.id != cloud.id && it.location == cloud.location }
    }

    private fun merge(collidingCloud: Cloud, movingCloud: Cloud) {
        val totalAmount = if (
            CLOUD_AMOUNT_MAX_VALUE - collidingCloud.amount >= movingCloud.amount
        ) {
            collidingCloud.amount + movingCloud.amount
        } else {
            CLOUD_AMOUNT_MAX_VALUE
        }
        val newDuration = if (collidingCloud.duration == -1 || movingCloud.duration == -1) {
            max(
                collidingCloud.duration,
                movingCloud.duration
            )
        } else {
            min(
                collidingCloud.duration,
                movingCloud.duration
            )
        }
        val newCloud = Cloud(
            highestID,
            location = collidingCloud.location,
            duration = newDuration,
            amount = totalAmount
        )
        newCloud.availableMovesLeft = max(
            collidingCloud.availableMovesLeft,
            movingCloud.availableMovesLeft
        )
        clouds.add(newCloud)
        highestID++

        Logger.logCloudUnion(
            cloudIDFromTile = collidingCloud.id,
            cloudIDMovingToTile = movingCloud.id,
            cloudIDNew = newCloud.id,
            amount = newCloud.amount,
            duration = newCloud.duration,
            tileID = newCloud.location
        )

        clouds.remove(collidingCloud)
        clouds.remove(movingCloud)
    }

    private fun reduceSunlightAfterMovements() {
        clouds.sortedBy {
            it.location
        }.forEach {
            val currentTile = tileManager.getTileById(id = it.location)
            if (currentTile is FarmableTile<*>) {
                currentTile.sunlightThisTick = max(
                    currentTile.sunlightThisTick - REDUCE_SUNLIGHT_CLOUDY_TILES, 0
                )
                Logger.logCloudPosition(
                    cloudID = it.id,
                    tileID = it.location,
                    amountSunLight = currentTile.sunlightThisTick
                )
            }
        }
    }

    /**
     * handles cloud dissipation in case of city expansion
     */
    fun handleCityExpansion(tile: Tile) {
        val cloud = clouds.find {
            it.location == tile.id
        }
        if (cloud != null) {
            Logger.logCloudStuck(
                cloudID = cloud.id,
                tileID = tile.id
            )
            clouds.remove(cloud)
        }
    }
}
