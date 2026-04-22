package de.unisaarland.cs.se.selab.simulation

import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.parser.FarmsParser
import de.unisaarland.cs.se.selab.parser.MainParser
import de.unisaarland.cs.se.selab.parser.MapParser
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MapParserTest {

    @Disabled
    @Test
    fun testMap() {
        val mapFile = "src/test/resources/tiles.json"
        val mapParser = MapParser(TileManager())
        val result = mapParser.parseMap(mapFile)
        assertTrue(result)
    }

    @Disabled("Not implemented yet")
    @Test
    fun testFarms() {
        val mapFile = "src/test/resources/tiles.json"
        val farmsFile = "src/test/resources/farms.json"
        val tileManager = TileManager()
        val mapParser = MapParser(tileManager)
        mapParser.parseMap(mapFile)
        val farmsParser = FarmsParser(tileManager)
        val result = farmsParser.parseFarmsAndMachines(farmsFile)
        assertNotNull(result)
    }

    @Disabled
    @Test
    fun testScenario() {
        val mainParser = MainParser(YearTick.JAN1, 0)
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
        val mapFile = "src/test/resources/tiles.json"
        val farmsFile = "src/test/resources/farms.json"
        val scenarioFile = "src/test/resources/scenario.json"
        val result = mainParser.parse(mapFile, farmsFile, scenarioFile)
        assertNotNull(result)
    }
}
