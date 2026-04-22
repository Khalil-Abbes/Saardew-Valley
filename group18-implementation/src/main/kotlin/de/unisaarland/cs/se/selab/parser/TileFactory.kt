package de.unisaarland.cs.se.selab.parser

import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.tile.Direction
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory

/**
 * Factory method for Tile
 */
object TileFactory {
    /**
     * create the Village tile
     */
    fun createVillageTile(id: Int, coordinate: Coordinate): Tile {
        return Tile(
            id,
            coordinate,
            TileCategory.VILLAGE,
            false,
            null,
            null
        )
    }

    /**
     * create the Meadow tile
     */
    fun createMeadowTile(id: Int, coordinate: Coordinate, direction: Direction?): Tile {
        return Tile(
            id,
            coordinate,
            TileCategory.MEADOW,
            false,
            null,
            direction
        )
    }

    /**
     * create the Road tile
     */
    fun createRoadTile(id: Int, coordinate: Coordinate, direction: Direction?): Tile {
        return Tile(
            id,
            coordinate,
            TileCategory.ROAD,
            false,
            null,
            direction
        )
    }

    /**
     * create the Farmstead tile
     */
    fun createFarmsteadTile(
        id: Int,
        coordinate: Coordinate,
        shed: Boolean,
        farm: Int,
        direction: Direction?
    ): Tile {
        return Tile(
            id,
            coordinate,
            TileCategory.FARMSTEAD,
            shed,
            farm,
            direction
        )
    }

    /**
     * create the Plantation tile
     */
    fun createPlantationTile(
        id: Int,
        coordinate: Coordinate,
        farm: Int,
        direction: Direction?,
        moistureCapacity: Int,
        plant: PlantationPlant
    ): Plantation {
        return Plantation(
            id,
            coordinate,
            farm,
            direction,
            moistureCapacity,
            plant
        )
    }

    /**
     * create the Field tile
     */
    fun createFieldTile(
        id: Int,
        coordinate: Coordinate,
        farm: Int,
        direction: Direction?,
        moistureCapacity: Int,
        possiblePlant: List<PlantType>
    ): Field {
        return Field(
            id,
            coordinate,
            farm,
            direction,
            moistureCapacity,
            possiblePlant
        )
    }

    /**
     *  create forest tile
     */
    fun createForestTile(id: Int, coordinate: Coordinate, direction: Direction?): Tile {
        return Tile(
            id,
            coordinate,
            TileCategory.FOREST,
            false,
            null,
            direction
        )
    }
}
