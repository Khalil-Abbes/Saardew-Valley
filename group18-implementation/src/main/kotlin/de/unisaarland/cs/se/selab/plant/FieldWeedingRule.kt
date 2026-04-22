package de.unisaarland.cs.se.selab.plant

/**
 * Specifies the rules for the weeding action on field tiles
 */
class FieldWeedingRule(val offsets: List<Int>?, val everyN: Int?) {

    /**
     * checks the weeding time with the current tick
     */
    fun contains(sowingTick: Int, currentTick: Int): Boolean {
        if (offsets != null) {
            return offsets.any { sowingTick + it == currentTick }
        } else if (everyN != null) {
            return (currentTick - sowingTick) % everyN == 0 && currentTick != sowingTick
        }
        return false
    }
}
