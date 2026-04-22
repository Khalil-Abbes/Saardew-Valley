package de.unisaarland.cs.se.selab.farm

import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`

class SowingPlanTest {

    @Test
    fun getFieldsWithFieldsList() {
        val mockTileManager = mock(TileManager::class.java)
        val mockPlantType = mock(FieldPlantType::class.java)
        val field1 = mock(Field::class.java)
        val field2 = mock(Field::class.java)
        val fields = listOf(field1, field2)

        val sowingPlan = SowingPlan(
            id = 1,
            tick = 5,
            plantType = mockPlantType,
            fields = fields,
            location = null,
            radius = null,
            tileManager = mockTileManager
        )

        val result = sowingPlan.getFields()

        assertEquals(fields, result)
        verifyNoInteractions(mockTileManager)
    }

    @Test
    fun getFieldsWithLocationAndRadius() {
        val mockTileManager = mock(TileManager::class.java)
        val mockPlantType = mock(FieldPlantType::class.java)
        val field1 = mock(Field::class.java)
        val field2 = mock(Field::class.java)
        val nonFieldTile = mock(Tile::class.java)
        val neighborTiles = listOf(field1, nonFieldTile, field2)

        `when`(mockTileManager.getAllNeighborTilesInRadius(10, 3)).thenReturn(neighborTiles)

        val sowingPlan = SowingPlan(
            id = 1,
            tick = 5,
            plantType = mockPlantType,
            fields = null,
            location = 10,
            radius = 3,
            tileManager = mockTileManager
        )

        val result = sowingPlan.getFields()

        assertEquals(listOf(field1, field2), result)
        verify(mockTileManager).getAllNeighborTilesInRadius(10, 3)
    }

    @Test
    fun getFieldsReturnsEmptyListWhenAllNull() {
        val mockTileManager = mock(TileManager::class.java)
        val mockPlantType = mock(FieldPlantType::class.java)

        val sowingPlan = SowingPlan(
            id = 1,
            tick = 5,
            plantType = mockPlantType,
            fields = null,
            location = null,
            radius = null,
            tileManager = mockTileManager
        )

        val result = sowingPlan.getFields()

        assertTrue(result.isEmpty())
        verifyNoInteractions(mockTileManager)
    }

    @Test
    fun getFieldsReturnsEmptyListWhenLocationWithoutRadius() {
        val mockTileManager = mock(TileManager::class.java)
        val mockPlantType = mock(FieldPlantType::class.java)

        val sowingPlan = SowingPlan(
            id = 1,
            tick = 5,
            plantType = mockPlantType,
            fields = null,
            location = 10,
            radius = null,
            tileManager = mockTileManager
        )

        val result = sowingPlan.getFields()

        assertTrue(result.isEmpty())
        verifyNoInteractions(mockTileManager)
    }

    @Test
    fun getFieldsReturnsEmptyListWhenRadiusWithoutLocation() {
        val mockTileManager = mock(TileManager::class.java)
        val mockPlantType = mock(FieldPlantType::class.java)

        val sowingPlan = SowingPlan(
            id = 1,
            tick = 5,
            plantType = mockPlantType,
            fields = null,
            location = null,
            radius = 3,
            tileManager = mockTileManager
        )

        val result = sowingPlan.getFields()

        assertTrue(result.isEmpty())
        verifyNoInteractions(mockTileManager)
    }

    @Test
    fun getFieldsFiltersNonFieldTiles() {
        val mockTileManager = mock(TileManager::class.java)
        val mockPlantType = mock(FieldPlantType::class.java)
        val field = mock(Field::class.java)
        val nonFieldTile1 = mock(Tile::class.java)
        val nonFieldTile2 = mock(Tile::class.java)
        val neighborTiles = listOf(nonFieldTile1, field, nonFieldTile2)

        `when`(mockTileManager.getAllNeighborTilesInRadius(5, 2)).thenReturn(neighborTiles)

        val sowingPlan = SowingPlan(
            id = 1,
            tick = 5,
            plantType = mockPlantType,
            fields = null,
            location = 5,
            radius = 2,
            tileManager = mockTileManager
        )

        val result = sowingPlan.getFields()

        assertEquals(listOf(field), result)
        verify(mockTileManager).getAllNeighborTilesInRadius(5, 2)
    }

    @Test
    fun sowingPlanPropertiesAreCorrectlySet() {
        val mockTileManager = mock(TileManager::class.java)
        val mockPlantType = mock(FieldPlantType::class.java)

        val sowingPlan = SowingPlan(
            id = 42,
            tick = 15,
            plantType = mockPlantType,
            fields = emptyList(),
            fulfilled = true,
            location = 5,
            radius = 2,
            tileManager = mockTileManager
        )

        assertEquals(42, sowingPlan.id)
        assertEquals(15, sowingPlan.tick)
        assertEquals(mockPlantType, sowingPlan.plantType)
        assertTrue(sowingPlan.fulfilled)
    }

    @Test
    fun fulfilledCanBeModified() {
        val mockTileManager = mock(TileManager::class.java)
        val mockPlantType = mock(FieldPlantType::class.java)

        val sowingPlan = SowingPlan(
            id = 1,
            tick = 5,
            plantType = mockPlantType,
            fields = emptyList(),
            location = null,
            radius = null,
            tileManager = mockTileManager
        )

        assertFalse(sowingPlan.fulfilled)
        sowingPlan.fulfilled = true
        assertTrue(sowingPlan.fulfilled)
    }

    @Test
    fun fieldsListTakesPrecedenceOverLocationAndRadius() {
        val mockTileManager = mock(TileManager::class.java)
        val mockPlantType = mock(FieldPlantType::class.java)
        val field1 = mock(Field::class.java)
        val field2 = mock(Field::class.java)
        val providedFields = listOf(field1, field2)

        val sowingPlan = SowingPlan(
            id = 1,
            tick = 5,
            plantType = mockPlantType,
            fields = providedFields,
            location = 10,
            radius = 3,
            tileManager = mockTileManager
        )

        val result = sowingPlan.getFields()

        assertEquals(providedFields, result)
        verifyNoInteractions(mockTileManager)
    }
}
