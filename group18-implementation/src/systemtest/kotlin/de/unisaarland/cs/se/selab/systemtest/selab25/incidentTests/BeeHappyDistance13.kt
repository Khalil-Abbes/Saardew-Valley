package de.unisaarland.cs.se.selab.systemtest.selab25.incidentTests

import de.unisaarland.cs.se.selab.systemtest.api.SystemTestAssertionError
import de.unisaarland.cs.se.selab.systemtest.selab25.utils.ExampleSystemTestExtension

const val HARVEST_ESTIMATE = "[INFO] Harvest Estimate:"

/**
 * Bee-Happy range test (meadow swarm radius = 2; incident radius).
 *
 * startYearTick = 8 (APR2, APPLE bloom active). One simulation tick only.
 * Layout (octagons at even coords, squares at odd):
 *  - APPLE at (2,2)=id2, (4,2)=id6, (8,2)=id14
 *  - MEADOW A at (3,3)=id4, MEADOW B at (7,3)=id8
 *
 * Incidents (both at tick 0):
 *  - #1 loc=4 radius=0 (activates MEADOW A) -> affects tiles 2,6
 *  - #2 loc=8 radius=0 (activates MEADOW B) -> affects tiles 6,14
 *
 * Expected end-of-tick estimates (APR2 sunlight 140h → 0.9^3 first, then bee bonuses):
 *  Base after sunlight: 1_700_000 * 0.9^3 = 1_239_300
 *   - tile 2:  +20% once  => 1_487_160
 *   - tile 6:  +20% twice => 1_784_592
 *   - tile 14: +20% once  => 1_487_160
 * Total remaining = 4_758_912 g.
 */
class BeeHappyDistance13 : ExampleSystemTestExtension() {
    override val name = "BeeHappyDistance[BUT WITH NO HOLES]13"
    override val description = "[BUT WITH NO HOLES] Bee-Happy range: two meadows, radius=2 from each;" +
        " verify inside vs outside & incident radius."
    override val farms = "farmaction/BeeHappyDistance/farms.json"
    override val scenario = "farmaction/BeeHappyDistance/scenario.json"
    override val map = "farmaction/BeeHappyDistance/map2.json"

    override val logLevel = "DEBUG"
    override val maxTicks = 1
    override val startYearTick = 8 // APR2

    private companion object {
        const val APPLE = "APPLE"
    }

    override suspend fun run() {
        skipUntilString(HARVEST_ESTIMATE)
        assertCurrentLine(
            "[INFO] Harvest Estimate: " +
                "Harvest estimate on tile 2 changed to 1487160 g of APPLE."
        )
        skipUntilString(HARVEST_ESTIMATE)
        assertCurrentLine("[INFO] Harvest Estimate: Harvest estimate on tile 6 changed to 1784592 g of APPLE.")
        skipUntilString(HARVEST_ESTIMATE)
        assertCurrentLine("[INFO] Harvest Estimate: Harvest estimate on tile 14 changed to 1487160 g of APPLE.")
    }
    private suspend fun skipUntilString(startString: String): String {
        val line: String = getNextLine()
            ?: throw SystemTestAssertionError("End of log reached when there should be more.")
        return if (line.startsWith(startString)) {
            line
        } else {
            skipUntilString(startString)
        }
    }
}
