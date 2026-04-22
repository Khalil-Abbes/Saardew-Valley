package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.incident.CloudCreationIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.parser.TileFactory
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CloudCreationIncidentTest {

    private lateinit var tm: TileManager
    private lateinit var handler: CloudHandler

    @BeforeEach
    fun setup() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))

        val tiles = mutableMapOf<Int, Tile>()
        // Center and two neighbors (all NON-VILLAGE)
        tiles[10] = TileFactory.createFieldTile(
            10, Coordinate(0, 0), farm = 1, direction = null,
            moistureCapacity = 400, possiblePlant = emptyList()
        )
        tiles[12] = TileFactory.createMeadowTile(12, Coordinate(2, 0), direction = null)
        tiles[13] = TileFactory.createRoadTile(13, Coordinate(0, 2), direction = null)

        tm = TileManager()
        tm.setTiles(tiles)

        // Existing cloud sitting on tile 12 Meadow (to test immediate merge on creation)
        val initialClouds = mutableListOf(Cloud(id = 5, duration = 7, location = 12, amount = 700))
        handler = CloudHandler(tm, initialClouds)
    }

    @Test
    fun `creates clouds on all impacted non-village tiles in ascending tileId order and merges if needed`() {
        val startHighest = handler.highestID // will be 6 based on initial clouds
        val incident = CloudCreationIncident(
            id = 1,
            tick = 100,
            cloudHandler = handler,
            duration = 2,
            location = 10,
            radius = 1,
            amount = 1000
        )

        // perform the incident (no movement this tick by spec)
        incident.perform(currentTick = 100, currentYearTick = YearTick.MAY1)

        // Then: clouds created for tiles 10, 12, 13 (but 12 merges with the existing cloud)
        val byLoc = handler.clouds.associateBy { it.location }
        assertTrue(byLoc.containsKey(10))
        assertTrue(byLoc.containsKey(12))
        assertTrue(byLoc.containsKey(13))

        // IDs should be >= previous highest
        val newIds = handler.clouds.map { it.id }
        assertTrue(newIds.all { it >= startHighest })

        // Amounts & durations
        val c10 = byLoc.getValue(10)
        val c12 = byLoc.getValue(12)
        val c13 = byLoc.getValue(13)

        assertEquals(1000, c10.amount)
        assertEquals(2, c10.duration)

        // merge: 700 + 1000 = 1700; duration min(7,2) = 2
        assertEquals(1700, c12.amount)
        assertEquals(2, c12.duration)

        assertEquals(1000, c13.amount)
        assertEquals(2, c13.duration)

        // Sanity: clouds exist, but no environment effect this tick yet (tests be by Stefan!)
        assertNotNull(c10)
    }

    @Test
    fun `cloud IDs assigned in ascending tileId order starting from previous highest`() {
        val incident = CloudCreationIncident(
            id = 1,
            tick = 200,
            cloudHandler = handler,
            duration = 3,
            location = 10,
            radius = 1,
            amount = 500
        )

        incident.perform(currentTick = 200, currentYearTick = YearTick.JUN1)

        val newClouds = handler.clouds.filter { it.id >= 6 }.sortedBy { it.id }

        // We expect three IDs allocated: 6, 8, 9
        val ids = newClouds.map { it.id }
        assertEquals(listOf(6, 8, 9), ids)

        // Order corresponds to ascend tile IDs: 10, 12, 13
        val locations = newClouds.map { it.location }
        assertEquals(listOf(10, 12, 13), locations)

        // And after merging, tile 12 should have exactly one cloud
        val byLoc = handler.clouds.groupBy { it.location }
        assertEquals(1, byLoc.getValue(12).size)
    }
}
