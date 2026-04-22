package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.farm.ActionType.CUTTING
import de.unisaarland.cs.se.selab.farm.ActionType.HARVESTING
import de.unisaarland.cs.se.selab.farm.ActionType.IRRIGATING
import de.unisaarland.cs.se.selab.farm.ActionType.MOWING
import de.unisaarland.cs.se.selab.farm.ActionType.SOWING
import de.unisaarland.cs.se.selab.farm.ActionType.WEEDING
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.parser.TileFactory
import de.unisaarland.cs.se.selab.plant.FieldPlant
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.plant.PlantType
import de.unisaarland.cs.se.selab.plant.PlantationPlant
import de.unisaarland.cs.se.selab.plant.PlantationPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Plantation
import de.unisaarland.cs.se.selab.tile.Tile
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.PrintWriter
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FarmActionsTest {

    @BeforeEach
    fun setupLogger() {
        Logger.concreteLogger = DebugLogger(PrintWriter(System.out))
    }

    private fun tmWith(vararg tiles: Tile): TileManager {
        val tm = TileManager()
        tm.setTiles(tiles.associateBy { it.id }.toMutableMap())
        return tm
    }

    private fun farm(
        id: Int,
        name: String,
        farmsteads: List<Tile>,
        fields: List<Field>,
        plantations: List<Plantation>,
        machines: List<Machine>
    ): Farm = Farm(
        id = id,
        name = name,
        farmsteads = farmsteads,
        fields = fields,
        plantations = plantations,
        machines = machines,
        sowingPlans = mutableListOf()
    )

    private fun newHarvester(
        id: Int,
        shed: Tile,
        tm: TileManager,
        farmId: Int,
        plants: List<PlantType>,
        duration: Int = 1
    ) = Machine(
        id = id,
        name = "H$id",
        actions = listOf(HARVESTING),
        plants = plants,
        duration = duration,
        location = shed,
        tileManager = tm,
        farm = farmId
    )

    private fun newCutter(
        id: Int,
        shed: Tile,
        tm: TileManager,
        farmId: Int,
        plants: List<PlantType>,
        duration: Int = 1
    ) = Machine(
        id = id,
        name = "C$id",
        actions = listOf(CUTTING),
        plants = plants,
        duration = duration,
        location = shed,
        tileManager = tm,
        farm = farmId
    )

    private fun newIrrigatorWeederForFields(
        id: Int,
        shed: Tile,
        tm: TileManager,
        farmId: Int,
        plants: List<FieldPlantType>,
        duration: Int = 1
    ) = Machine(
        id = id,
        name = "IW$id",
        actions = listOf(IRRIGATING, WEEDING),
        plants = plants,
        duration = duration,
        location = shed,
        tileManager = tm,
        farm = farmId
    )

    private fun newIrrigatorMowerForPlantations(
        id: Int,
        shed: Tile,
        tm: TileManager,
        farmId: Int,
        plants: List<PlantationPlantType>,
        duration: Int = 1
    ) = Machine(
        id = id,
        name = "IM$id",
        actions = listOf(IRRIGATING, MOWING),
        plants = plants,
        duration = duration,
        location = shed,
        tileManager = tm,
        farm = farmId
    )

    // Small road helper (all even coords -> oct grid); safe parity.
    private fun road(id: Int, x: Int, y: Int) = TileFactory.createRoadTile(id, Coordinate(x, y), null)

    // private fun village(id: Int, x: Int, y: Int) = TileFactory.createVillageTile(id, Coordinate(x, y))
    private fun farmstead(id: Int, x: Int, y: Int, farm: Int) =
        TileFactory.createFarmsteadTile(id, Coordinate(x, y), shed = true, farm = farm, direction = null)

    // -------------------------
    // PRIORITIZATION & HARVEST
    // -------------------------

    @Test
    fun `harvests plantation before field (single machine) (2_2_3 Actions priority lines 8-9)`() {
        val shed = farmstead(10, 0, 0, farm = 1)
        val r1 = road(11, 2, 0)
        val apple = Plantation(
            20,
            Coordinate(4, 0),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.APPLE)
        )
        apple.currentHarvestEstimate = 500
        val field = Field(
            30,
            Coordinate(6, 0),
            1,
            null,
            400,
            listOf(FieldPlantType.POTATO)
        )
        field.plant = FieldPlant(FieldPlantType.POTATO)
        field.currentHarvestEstimate = 500

        val tm = tmWith(shed, r1, apple, field)
        val harvester = newHarvester(
            1,
            shed,
            tm,
            1,
            plants = listOf(PlantationPlantType.APPLE, FieldPlantType.POTATO)
        )
        val f = farm(
            1,
            "F",
            listOf(shed),
            listOf(field),
            listOf(apple),
            listOf(harvester)
        )

        apple.sunlightThisTick = apple.plant.type.sunlightComfort
        apple.currentMoisture = apple.plant.type.moistureRequired
        field.sunlightThisTick = field.plant!!.type.sunlightComfort
        field.currentMoisture = field.plant!!.type.moistureRequired

        f.run(currentTick = 100, currentYearTick = YearTick.SEP1)

        assertTrue(
            apple.plant.progress.harvesting.isNotEmpty(),
            "Plantation should be harvested first."
        )
        assertTrue(
            field.plant!!.progress.harvesting.isEmpty(),
            "Field must not be harvested by the same machine this tick."
        )
    }

    @Test
    fun `cuts plantation tiles after all possible harvesting (2_2_3 Actions priority lines 10-11)`() {
        val shed = farmstead(10, 0, 0, farm = 1)
        val r1 = road(11, 2, 0)
        val apple = Plantation(
            20,
            Coordinate(4, 0),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.APPLE)
        )
        val tm = tmWith(shed, r1, apple)

        val cutter = newCutter(2, shed, tm, 1, plants = listOf(PlantationPlantType.APPLE))
        val f = farm(
            1,
            "F",
            listOf(shed),
            emptyList(),
            listOf(apple),
            listOf(cutter)
        )

        f.run(currentTick = 10, currentYearTick = YearTick.FEB1)

        assertTrue(
            apple.plant.progress.cutting.isNotEmpty(),
            "Cutting should be performed in valid window when no harvest is pending."
        )
    }

    // -------------------------
    // MACHINE-LEVEL ORDER
    // -------------------------

    @Test
    fun `on fields a machine performs IRRIGATING before WEEDING (2_2_3 Actions lines 11-13)`() {
        val shed = farmstead(10, 0, 0, 1)
        val r1 = road(11, 2, 0)
        val wheat = Field(
            20,
            Coordinate(4, 0),
            1,
            null,
            500,
            listOf(FieldPlantType.WHEAT)
        )
        wheat.plant = FieldPlant(FieldPlantType.WHEAT)
        wheat.plant!!.setProgress(SOWING, 1, YearTick.OCT1)
        val tm = tmWith(shed, r1, wheat)

        wheat.currentMoisture = 300
        wheat.sunlightThisTick = wheat.plant!!.type.sunlightComfort

        val mw = newIrrigatorWeederForFields(
            3,
            shed,
            tm,
            1,
            plants = listOf(FieldPlantType.WHEAT)
        )
        val f = farm(
            1,
            "F",
            listOf(shed),
            listOf(wheat),
            emptyList(),
            listOf(mw)
        )

        f.run(currentTick = 4, currentYearTick = YearTick.NOV1)

        assertTrue(
            wheat.plant!!.progress.irrigating.isNotEmpty(),
            "Irrigation must happen before weeding on fields."
        )
        assertTrue(
            wheat.plant!!.progress.weeding.isEmpty(),
            "Tile already worked -> no parallel weeding this tick."
        )
    }

    @Test
    fun `on plantations a machine performs IRRIGATING before MOWING (2_2_3 Actions lines 11-13)`() {
        val shed = farmstead(10, 0, 0, 1)
        val r1 = road(11, 2, 0)
        val apple = Plantation(
            20,
            Coordinate(4, 0),
            1,
            null,
            500,
            PlantationPlant(PlantationPlantType.APPLE)
        )
        val tm = tmWith(shed, r1, apple)

        apple.currentMoisture = 0
        apple.sunlightThisTick = apple.plant.type.sunlightComfort

        val im = newIrrigatorMowerForPlantations(
            4,
            shed,
            tm,
            1,
            plants = listOf(PlantationPlantType.APPLE)
        )
        val f = farm(
            1,
            "F",
            listOf(shed),
            emptyList(),
            listOf(apple),
            listOf(im)
        )

        f.run(currentTick = 50, currentYearTick = YearTick.JUN1)

        assertTrue(
            apple.plant.progress.irrigating.isNotEmpty(),
            "Irrigation on plantation should come before mowing."
        )
        assertTrue(
            apple.plant.progress.mowing.isEmpty(),
            "No second action on same tile in this tick."
        )
    }

    // -------------------------
    // IRRIGATION PREREQUISITES
    // -------------------------

    @Disabled("")
    @Test
    fun `irrigates deficit field and zero-harvest field, skips PD plantation`() {
        val shed = farmstead(10, 0, 0, 1)
        val r1 = road(11, 2, 0)

        // A: field with moisture deficit
        val fa = Field(
            20,
            Coordinate(4, 0),
            1,
            null,
            600,
            listOf(FieldPlantType.PUMPKIN)
        )
        fa.plant = FieldPlant(FieldPlantType.PUMPKIN)
        fa.currentMoisture = 400
        fa.sunlightThisTick = fa.plant!!.type.sunlightComfort

        // C: field with harvest = 0 but no deficit -> should still irrigate
        val fc = Field(
            22,
            Coordinate(8, 0),
            1,
            null,
            400,
            listOf(FieldPlantType.WHEAT)
        )
        fc.plant = FieldPlant(FieldPlantType.WHEAT)
        fc.currentHarvestEstimate = 0
        fc.sunlightThisTick = fc.plant!!.type.sunlightComfort
        fc.currentMoisture = fc.plant!!.type.moistureRequired

        // Plantation permanently disabled -> never irrigated
        val apple = Plantation(
            30,
            Coordinate(4, 2),
            1,
            null,
            500,
            PlantationPlant(PlantationPlantType.APPLE)
        )
        apple.permanentDisabled = 40

        val tm = tmWith(shed, r1, fa, fc, apple)
        val irrigator = Machine(
            id = 5,
            name = "I5",
            actions = listOf(IRRIGATING),
            plants = listOf(FieldPlantType.PUMPKIN, FieldPlantType.WHEAT, PlantationPlantType.APPLE),
            duration = 1,
            location = shed,
            tileManager = tm,
            farm = 1
        )
        val f = farm(
            1,
            "F",
            listOf(shed),
            listOf(fa, fc),
            listOf(apple),
            listOf(irrigator)
        )

        f.run(currentTick = 60, currentYearTick = YearTick.APR1)

        // A: irrigated because deficit  (§2.2.3 “Prerequisites”: moisture < needed)
        assertTrue(fa.plant!!.progress.irrigating.isNotEmpty())
        // C: irrigated because harvest = 0 “It is also required if the harvest has been reduced to 0 g”
        assertTrue(fc.plant!!.progress.irrigating.isNotEmpty())
        // PD plantation is excluded “…unless the tile is a PLANTATION tile permanently disabled...”
        assertTrue(apple.plant.progress.irrigating.isEmpty())
    }

    @Test
    fun `fallow field is irrigated to capacity`() {
        val shed = farmstead(10, 0, 0, 1)
        val r1 = road(11, 2, 0)

        // B: fallow field (no plant) -> should be irrigated and filled to capacity
        val fb = Field(
            21,
            Coordinate(6, 0),
            1,
            null,
            400,
            listOf(FieldPlantType.WHEAT)
        )

        val tm = tmWith(shed, r1, fb)
        val irrigator = Machine(
            id = 6,
            name = "I6",
            actions = listOf(IRRIGATING),
            plants = listOf(FieldPlantType.WHEAT),
            duration = 1,
            location = shed,
            tileManager = tm,
            farm = 1
        )
        val f = farm(
            1,
            "F",
            listOf(shed),
            listOf(fb),
            emptyList(),
            listOf(irrigator)
        )

        f.run(currentTick = 61, currentYearTick = YearTick.APR1)

        // or a FIELD tile is fallow … Once on the tile, they fill it with moisture until the maximum capacity.
        assertEquals(fb.moistureCapacity, fb.currentMoisture)
    }

    // -------------------------
    // TILE & MACHINE SELECTION
    // -------------------------

    @Test
    fun `machine selection filters non-reachable first then picks lowest duration among reachable`() {
        val shed = farmstead(10, 0, 0, 1)
        val r1 = road(11, 2, 0)
        val apple = Plantation(
            20,
            Coordinate(4, 0),
            1,
            null,
            300,
            PlantationPlant(PlantationPlantType.APPLE)
        )
        apple.currentHarvestEstimate = 500

        val forestBlock = TileFactory.createForestTile(99, Coordinate(100, 100), null)

        val tm = tmWith(shed, r1, apple, forestBlock)

        val fastButBlocked = newHarvester(
            1,
            shed,
            tm,
            1,
            plants = listOf(PlantationPlantType.APPLE),
            duration = 1
        )
        val otherShed = TileFactory.createFarmsteadTile(
            50,
            Coordinate(0, 2),
            shed = true,
            farm = 2,
            direction = null
        )
        tm.updateTile(otherShed)
        fastButBlocked.location = otherShed

        val slowReachable = newHarvester(
            2,
            shed,
            tm,
            1,
            plants = listOf(PlantationPlantType.APPLE),
            duration = 2
        )

        val f = farm(
            1,
            "F",
            listOf(shed),
            emptyList(),
            listOf(apple),
            listOf(fastButBlocked, slowReachable)
        )

        apple.sunlightThisTick = apple.plant.type.sunlightComfort
        apple.currentMoisture = apple.plant.type.moistureRequired

        f.run(currentTick = 70, currentYearTick = YearTick.SEP1)

        assertTrue(
            apple.plant.progress.harvesting.isNotEmpty(),
            "Farm must ignore unreachable faster machine and use reachable one."
        )
    }

    // -------------------------
    // ONE ACTION PER TILE & POST-ACTION RETURN
    // -------------------------

    // -------------------------
    // SOWING GUARDRAILS (no plan -> no sowing)
    // -------------------------

    @Test
    fun `farm does NOT sow without sowing plans even if window open and machine available`() {
        val shed = farmstead(10, 0, 0, 1)
        val r1 = road(11, 2, 0)
        val field = Field(
            20,
            Coordinate(4, 0),
            1,
            null,
            500,
            listOf(FieldPlantType.WHEAT)
        )
        val tm = tmWith(shed, r1, field)

        val sower = Machine(
            id = 15,
            name = "S15",
            actions = listOf(SOWING),
            plants = listOf(FieldPlantType.WHEAT),
            duration = 1,
            location = shed,
            tileManager = tm,
            farm = 1
        )
        val f = farm(
            1,
            "F",
            listOf(shed),
            listOf(field),
            emptyList(),
            listOf(sower)
        )

        f.run(currentTick = 5, currentYearTick = YearTick.OCT1)

        assertEquals(
            null,
            field.plant,
            "Sowing must not occur without an active sowing plan."
        )
        // no field.progress.* here: Field has no progress, progress belongs to Plant.
    }
}
