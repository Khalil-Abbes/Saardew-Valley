package de.unisaarland.cs.se.selab.incident

import de.unisaarland.cs.se.selab.farm.Machine
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.simulation.YearTick

/**
 * [Broken machine incident class]
 * Marks a machine as broken for a certain duration
 * and logs the incident with the affected machine's location
 */
class BrokenMachineIncident(
    id: Int,
    tick: Int,
    val duration: Int,
    val machine: Machine
) :
    Incident(id, tick) {

    override fun perform(currentTick: Int, currentYearTick: YearTick) {
        // mark machine as broken (in machine class)
        machine
            .markBroken(
                duration,
                currentTick
            )

        // log incident with affected machine's location
        Logger
            .logIncident(
                id,
                this,
                listOf(machine.location.id) // affected tile is the machine's location id
            )
    }

    // string representation of the incident
    override fun toString(): String {
        return "BROKEN_MACHINE"
    }
}
