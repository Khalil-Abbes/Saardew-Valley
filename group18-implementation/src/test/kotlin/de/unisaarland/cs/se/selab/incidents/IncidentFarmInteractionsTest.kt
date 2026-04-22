package de.unisaarland.cs.se.selab.incidents

import de.unisaarland.cs.se.selab.cloud.Cloud
import de.unisaarland.cs.se.selab.cloud.CloudHandler
import de.unisaarland.cs.se.selab.farm.ActionType
import de.unisaarland.cs.se.selab.farm.Farm
import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.farm.SowingPlan
import de.unisaarland.cs.se.selab.incident.CityExpansionIncident
import de.unisaarland.cs.se.selab.incident.CloudCreationIncident
import de.unisaarland.cs.se.selab.incident.DroughtIncident
import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.plant.FieldPlantType
import de.unisaarland.cs.se.selab.simulation.Coordinate
import de.unisaarland.cs.se.selab.simulation.Simulation
import de.unisaarland.cs.se.selab.simulation.TileManager
import de.unisaarland.cs.se.selab.simulation.YearTick
import de.unisaarland.cs.se.selab.tile.Direction
import de.unisaarland.cs.se.selab.tile.Field
import de.unisaarland.cs.se.selab.tile.Tile
import de.unisaarland.cs.se.selab.tile.TileCategory
import org.junit.jupiter.api.Test
import java.io.PrintWriter

private val tile1 = Field(
    id = 1,
    coordinate = Coordinate(0, 0),
    direction = Direction.ANGLE_90,
    possiblePlants = listOf(FieldPlantType.WHEAT),
    farm = 1,
    moistureCapacity = 4000
)

private val tile2 = Field(
    id = 2,
    coordinate = Coordinate(2, 0),
    direction = Direction.ANGLE_135,
    possiblePlants = listOf(FieldPlantType.OAT),
    farm = 1,
    moistureCapacity = 2000
)

private val tile3 = Field(
    id = 3,
    coordinate = Coordinate(4, 0),
    direction = null,
    possiblePlants = listOf(FieldPlantType.POTATO),
    farm = 1,
    moistureCapacity = 3000
)

private val tile4 = Field(
    id = 4,
    coordinate = Coordinate(0, 2),
    direction = Direction.ANGLE_0,
    possiblePlants = listOf(FieldPlantType.WHEAT),
    farm = 1,
    moistureCapacity = 500
)

private val tile5 = Field(
    id = 5,
    coordinate = Coordinate(2, 2),
    direction = Direction.ANGLE_270,
    possiblePlants = listOf(FieldPlantType.PUMPKIN),
    farm = 1,
    moistureCapacity = 1500
)

private val tile6 = Field(
    id = 6,
    coordinate = Coordinate(4, 2),
    direction = Direction.ANGLE_270,
    possiblePlants = listOf(FieldPlantType.OAT),
    farm = 2,
    moistureCapacity = 3000
)

private val tile7 = Tile(
    id = 7,
    coordinate = Coordinate(3, 1),
    direction = Direction.ANGLE_135,
    categoryType = TileCategory.MEADOW,
    shed = true,
    farm = 2,
)

private val tile8 = Tile(
    id = 8,
    coordinate = Coordinate(3, 3),
    direction = null,
    categoryType = TileCategory.MEADOW,
    shed = false,
    farm = 1,
)

private val tile9 = Tile(
    id = 9,
    coordinate = Coordinate(1, 1),
    direction = null,
    categoryType = TileCategory.VILLAGE,
    shed = false,
    farm = 2
)

private val tileManager = TileManager()
private val tiles =
    mutableMapOf(
        1 to tile1, 2 to tile2, 3 to tile3, 4 to tile4,
        5 to tile5, 6 to tile6, 7 to tile7, 8 to tile8,
        9 to tile9
    )

private val cloud1 = Cloud(
    id = 1,
    duration = 1,
    location = 1,
    amount = 3000
)
private val cloud2 = Cloud(
    id = 2,
    duration = -1,
    location = 5,
    amount = 4000
)
private val cloud3 = Cloud(
    id = 3,
    duration = 2,
    location = 3,
    amount = 7000
)

private val cloudHandler = CloudHandler(
    tileManager,
    mutableListOf(cloud1, cloud2, cloud3)
)

private val droughtIncident = DroughtIncident(
    id = 1,
    tick = 5,
    tileManager = tileManager,
    location = 8,
    radius = 1,
)

private val cloudIncident = CloudCreationIncident(
    id = 2,
    tick = 3,
    location = 1,
    radius = 1,
    duration = 4,
    amount = 3000,
    cloudHandler = cloudHandler
)

private val machine1 = Machine(
    id = 1,
    name = "m1",
    actions = listOf(
        ActionType.HARVESTING,
        ActionType.SOWING,
        ActionType.MOWING
    ),
    plants = listOf(
        FieldPlantType.POTATO,
        FieldPlantType.PUMPKIN,
        FieldPlantType.OAT
    ),
    duration = 1,
    location = tile2,
    tileManager = tileManager,
    farm = 1
)

private val machine2 = Machine(
    id = 2,
    name = "m2",
    actions = listOf(
        ActionType.MOWING,
        ActionType.HARVESTING
    ),
    plants = listOf(
        FieldPlantType.POTATO,
        FieldPlantType.WHEAT,
        FieldPlantType.OAT
    ),
    duration = 2,
    location = tile6,
    tileManager = tileManager,
    farm = 1
)

private val sowingPlan1 = SowingPlan(
    id = 1,
    tick = 1,
    tileManager = tileManager,
    plantType = FieldPlantType.POTATO,
    fields = listOf(
        tile1,
        tile2,
        tile4,
        tile5,
        tile3,
        tile6
    ),
    fulfilled = false,
    location = 2,
    radius = null
)

private val sowingPlan2 = SowingPlan(
    id = 2,
    tick = 1,
    tileManager = tileManager,
    plantType = FieldPlantType.WHEAT,
    fields = listOf(
        tile1,
        tile2,
        tile4,
        tile5,
        tile3,
        tile6
    ),
    fulfilled = false,
    location = 2,
    radius = 1
)

private val sowingPlan3 = SowingPlan(
    id = 3,
    tick = 2,
    tileManager = tileManager,
    plantType = FieldPlantType.PUMPKIN,
    fields = listOf(
        tile1,
        tile2,
        tile4,
        tile5,
        tile3,
        tile6
    ),
    fulfilled = false,
    location = 2,
    radius = 0
)

private val farm1 = Farm(
    id = 1,
    name = "f1",
    farmsteads = listOf(
        tile1,
        tile2,
        tile3,
        tile4,
        tile5,
        tile8
    ),
    sowingPlans = mutableListOf(
        sowingPlan1,
        sowingPlan2,
        sowingPlan3
    ),
    fields = listOf(
        tile1,
        tile2,
        tile3,
        tile4,
        tile5
    ),
    plantations = emptyList(),
    machines = listOf(machine1, machine2)
)

val farms = mutableListOf(farm1)

private val villageIncident = CityExpansionIncident(
    id = 3,
    tick = 2,
    location = 2,
    cloudHandler = cloudHandler,
    farms = farms,
    tileManager = tileManager,
)
class IncidentFarmInteractionsTest {
    @Test
    fun `test interaction between incident, cloud, farming with FAIL`() {
        tileManager.setTiles(tiles)
        Logger.concreteLogger = DebugLogger(
            PrintWriter(".\\src\\test\\kotlin\\de\\unisaarland\\cs\\se\\selab\\incidents\\logIncident.txt")
        )

        tile1.currentMoisture = 1200
        tile2.currentMoisture = 1150
        tile3.currentMoisture = 1180
        tile4.currentMoisture = 320
        tile5.currentMoisture = 1100
        tile6.currentMoisture = 1300

        val incidents = listOf(
            droughtIncident,
            cloudIncident,
            villageIncident
        )

        val startTick = 0
        val startYearTick = YearTick.APR2
        val maxTick = 24

        val sim = Simulation(
            tileManager = tileManager,
            cloudHandler = cloudHandler,
            farms = mutableMapOf(1 to farm1),
            incidents = incidents,
            currentTick = startTick,
            currentYearTick = startYearTick,
            maxTick = maxTick
        )
        sim.run()
    }
}
