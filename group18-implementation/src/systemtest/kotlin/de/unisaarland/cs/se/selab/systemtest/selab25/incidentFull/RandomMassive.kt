package de.unisaarland.cs.se.selab.systemtest.selab25.incidentFull

import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.debug
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.important
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.LogBuilder.info

/**
 * test
 */
class RandomMassive : ExampleSystemTestExtension() {
    override val name = "RandomMassive_includes_CC,BH,CM,DR,CE,AA,CC"
    override val description = "Bunch of incidents on a large map with many farms and plantations." +
        " Incidents: CloudCreation->Move->Rain->Dissipate, BeeHappy, CloudMerge, Drought," +
        " CityExpansion, AnimalAttack, CloudCreation->Move->Merge->Rain->Dissipate"

    override val farms = "farmaction/RandomMassive/farms.json"
    override val scenario = "farmaction/RandomMassive/scenario.json"
    override val map = "farmaction/RandomMassive/map.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 10
    override val startYearTick = 1

    private companion object {
        const val APPLE = "APPLE"
        const val CUTTING = "CUTTING"
        const val SOWING = "SOWING"
        const val OAT = "OAT"
        const val CHERRY = "CHERRY"
        const val PUMPKIN = "PUMPKIN"
        const val ALMOND = "ALMOND"
        const val WEEDING = "WEEDING"
        const val IRRIGATING = "IRRIGATING"
    }

    override suspend fun run() {
        assertInit()
        assertTick0()
        assertTick1()
        assertTick2()
        assertTick3()
        assertTick4()
        assertTick5()
        assertTick6()
        assertTick7()
        assertTick8()
        assertTick9AndEnd()
    }

    // helper functions below

    private suspend fun assertAll(vararg lines: String) {
        for (line in lines) assertNextLine(line)
    }

    private suspend fun assertInit() = assertAll(
        info.parseSuccess("map.json"),
        info.parseSuccess("farms.json"),
        info.parseSuccess("scenario.json"),
        info.simulationStart(1),
        info.tickStart(0, 1),
        info.soilMoisture(0, 0)
    )

    private suspend fun assertTick0() = assertAll(
        important.cloudRain(0, 10, 70),
        info.cloudMove(0, 5930, 10, 12),
        debug.cloudSunOnMove(10, 95),
        important.cloudRain(0, 12, 100),
        info.cloudMove(0, 5830, 12, 14),
        debug.cloudSunOnMove(12, 95),
        important.cloudRain(0, 14, 5830),
        info.cloudDissipates(0, 14),

        important.cloudRain(1, 20, 70),
        info.cloudMove(1, 5930, 20, 22),
        debug.cloudSunOnMove(20, 95),
        important.cloudRain(1, 22, 100),
        info.cloudMove(1, 5830, 22, 24),
        debug.cloudSunOnMove(22, 95),
        important.cloudRain(1, 24, 70),
        info.cloudMove(1, 5760, 24, 9),
        debug.cloudSunOnMove(24, 95),
        info.cloudStuck(1, 9),

        important.farmStart(0),
        debug.farmActiveSowingPlans(0, emptyList()),
        important.farmFinish(0),

        important.farmStart(1),
        debug.farmActiveSowingPlans(1, listOf(102)),
        important.farmFinish(1),

        info.harvestEstimate(12, 1_530_000, APPLE)
    )

    private suspend fun assertTick1() = assertAll(
        info.tickStart(1, 2),
        info.soilMoisture(0, 0),

        important.farmStart(0),
        debug.farmActiveSowingPlans(0, listOf(100)),
        important.farmFinish(0),

        important.farmStart(1),
        debug.farmActiveSowingPlans(1, listOf(102)),
        important.farmFinish(1),

        important.incident(1, "CLOUD_CREATION", listOf(14)),
        info.harvestEstimate(12, 1_377_000, APPLE)
    )

    private suspend fun assertTick2() = assertAll(
        info.tickStart(2, 3),
        info.soilMoisture(0, 0),

        important.farmStart(0),
        debug.farmActiveSowingPlans(0, listOf(100)),
        important.farmAction(4, CUTTING, 12, 4),
        important.machineReturn(4, 1),
        important.farmFinish(0),

        important.farmStart(1),
        debug.farmActiveSowingPlans(1, listOf(102)),
        important.farmAction(7, CUTTING, 30, 3),
        important.farmAction(7, CUTTING, 34, 3),
        important.machineReturn(7, 17),
        important.farmFinish(1),

        important.incident(2, "BEE_HAPPY", emptyList()),
        important.incident(4, "BROKEN_MACHINE", listOf(1)),
        info.harvestEstimate(12, 1_115_370, APPLE)
    )

    private suspend fun assertTick3() = assertAll(
        info.tickStart(3, 4),
        info.soilMoisture(0, 0),

        important.farmStart(0),
        debug.farmActiveSowingPlans(0, listOf(100, 101)),
        important.farmFinish(0),

        important.farmStart(1),
        debug.farmActiveSowingPlans(1, listOf(102)),
        important.farmFinish(1),

        important.incident(3, "DROUGHT", listOf(12)),
        info.harvestEstimate(12, 0, APPLE)
    )

    private suspend fun assertTick4() = assertAll(
        info.tickStart(4, 5),
        info.soilMoisture(0, 0),
        info.cloudDissipates(2, 14),

        important.farmStart(0),
        debug.farmActiveSowingPlans(0, listOf(100, 101)),
        important.farmFinish(0),

        important.farmStart(1),
        debug.farmActiveSowingPlans(1, listOf(102)),
        important.farmFinish(1),

        important.incident(5, "CITY_EXPANSION", listOf(24))
    )

    private suspend fun assertTick5() = assertAll(
        info.tickStart(5, 6),
        info.soilMoisture(0, 0),

        important.farmStart(0),
        debug.farmActiveSowingPlans(0, listOf(100, 101)),
        important.farmFinish(0),

        important.farmStart(1),
        debug.farmActiveSowingPlans(1, listOf(102)),
        important.farmAction(11, SOWING, 32, 3),
        important.farmSowing(11, OAT, 102),
        important.machineReturn(11, 17),
        important.farmFinish(1),

        important.incident(6, "ANIMAL_ATTACK", listOf(32, 34)),
        info.harvestEstimate(32, 540_000, OAT),
        info.harvestEstimate(34, 1_080_000, CHERRY)
    )

    private suspend fun assertTick6() = assertAll(
        info.tickStart(6, 7),
        info.soilMoisture(0, 0),

        important.farmStart(0),
        debug.farmActiveSowingPlans(0, listOf(100, 101)),
        important.farmAction(5, "MOWING", 22, 2),
        important.machineReturn(5, 1),
        important.farmFinish(0),

        important.farmStart(1),
        debug.farmActiveSowingPlans(1, emptyList()),
        important.farmFinish(1),

        important.incident(7, "CLOUD_CREATION", listOf(1, 3, 5, 7, 12, 20, 22, 32)),
        debug.missedActions(32, WEEDING),
        info.harvestEstimate(32, 393_660, OAT)
    )

    private suspend fun assertTick7() = assertAll(
        info.tickStart(7, 8),
        info.soilMoisture(0, 0),

        important.cloudRain(3, 1, 6_000),
        info.cloudDissipates(3, 1),
        important.cloudRain(4, 3, 6_000),
        info.cloudDissipates(4, 3),
        important.cloudRain(5, 5, 6_000),
        info.cloudDissipates(5, 5),
        important.cloudRain(6, 7, 6_000),
        info.cloudDissipates(6, 7),

        important.cloudRain(7, 12, 1_200),
        info.cloudMove(7, 4_800, 12, 14),
        debug.cloudSunOnMove(12, 137),
        info.cloudDissipates(7, 14),

        important.cloudRain(8, 20, 490),
        info.cloudMove(8, 5_510, 20, 22),
        debug.cloudSunOnMove(20, 137),
        important.cloudUnion(9, 8, 11, 11_510, 1, 22),

        important.cloudRain(10, 32, 620),
        info.cloudDissipates(10, 32),

        important.cloudRain(11, 22, 700),
        info.cloudMove(11, 10_810, 22, 24),
        debug.cloudSunOnMove(22, 137),
        info.cloudStuck(11, 24),

        important.farmStart(0),
        debug.farmActiveSowingPlans(0, listOf(100, 101)),
        important.farmFinish(0),

        important.farmStart(1),
        debug.farmActiveSowingPlans(1, emptyList()),
        important.farmFinish(1),

        debug.missedActions(32, WEEDING),
        info.harvestEstimate(32, 286_977, OAT)
    )

    private suspend fun assertTick8() = assertAll(
        info.tickStart(8, 9),
        info.soilMoisture(0, 1),

        important.farmStart(0),
        debug.farmActiveSowingPlans(0, listOf(100, 101)),
        important.farmFinish(0),

        important.farmStart(1),
        debug.farmActiveSowingPlans(1, emptyList()),
        important.farmAction(9, IRRIGATING, 30, 2),
        important.machineReturn(9, 17),
        important.farmFinish(1),

        info.harvestEstimate(30, 720_000, ALMOND),
        debug.missedActions(32, WEEDING),
        info.harvestEstimate(32, 188_284, OAT),
        info.harvestEstimate(34, 972_000, CHERRY)
    )

    private suspend fun assertTick9AndEnd() = assertAll(
        info.tickStart(9, 10),
        info.soilMoisture(0, 0),

        important.farmStart(0),
        debug.farmActiveSowingPlans(0, listOf(100, 101)),
        important.farmAction(0, SOWING, 10, 4),
        important.farmSowing(0, PUMPKIN, 101),
        important.farmAction(0, SOWING, 20, 4),
        important.farmSowing(0, PUMPKIN, 101),
        important.machineReturn(0, 1),
        important.farmFinish(0),

        important.farmStart(1),
        debug.farmActiveSowingPlans(1, emptyList()),
        important.farmFinish(1),

        // debug.missedActions(10, IRRIGATING),
        info.harvestEstimate(10, 450_000, PUMPKIN),
        info.harvestEstimate(20, 450_000, PUMPKIN),
        info.harvestEstimate(30, 648_000, ALMOND),
        info.harvestEstimate(32, 137_258, OAT),
        info.harvestEstimate(34, 874_800, CHERRY),

        important.simEnd(10),
        "[IMPORTANT] Simulation Info: Simulation statistics are calculated.",
        important.statsFarmCollected(0, 0),
        important.statsFarmCollected(1, 0),
        important.statsTotalPlant("POTATO", 0),
        important.statsTotalPlant("WHEAT", 0),
        important.statsTotalPlant(OAT, 0),
        important.statsTotalPlant("PUMPKIN", 0),
        important.statsTotalPlant(APPLE, 0),
        important.statsTotalPlant("GRAPE", 0),
        important.statsTotalPlant(ALMOND, 0),
        important.statsTotalPlant(CHERRY, 0),
        important.statsRemaining(3_760_058)
    )
}
