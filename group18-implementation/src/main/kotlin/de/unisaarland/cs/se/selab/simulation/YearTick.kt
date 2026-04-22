package de.unisaarland.cs.se.selab.simulation

// Sunlight constants for each month
const val JANUARY_SUNLIGHT = 98
const val FEBRUARY_SUNLIGHT = 112
const val MARCH_SUNLIGHT = 126
const val APRIL_SUNLIGHT = 140
const val MAY_SUNLIGHT = 168
const val JUNE_SUNLIGHT = 168
const val JULY_SUNLIGHT = 168
const val AUGUST_SUNLIGHT = 154
const val SEPTEMBER_SUNLIGHT = 126
const val OCTOBER_SUNLIGHT = 112
const val NOVEMBER_SUNLIGHT = 98
const val DECEMBER_SUNLIGHT = 84

// Tick number constants
const val JAN1_TICK = 1
const val JAN2_TICK = 2
const val FEB1_TICK = 3
const val FEB2_TICK = 4
const val MAR1_TICK = 5
const val MAR2_TICK = 6
const val APR1_TICK = 7
const val APR2_TICK = 8
const val MAY1_TICK = 9
const val MAY2_TICK = 10
const val JUN1_TICK = 11
const val JUN2_TICK = 12
const val JUL1_TICK = 13
const val JUL2_TICK = 14
const val AUG1_TICK = 15
const val AUG2_TICK = 16
const val SEP1_TICK = 17
const val SEP2_TICK = 18
const val OCT1_TICK = 19
const val OCT2_TICK = 20
const val NOV1_TICK = 21
const val NOV2_TICK = 22
const val DEC1_TICK = 23
const val DEC2_TICK = 24

/**
 * enum for yearTicks with hard-coded sunlight
 */
enum class YearTick(val tick: Int, val sunlight: Int) {
    JAN1(JAN1_TICK, JANUARY_SUNLIGHT),
    JAN2(JAN2_TICK, JANUARY_SUNLIGHT),

    FEB1(FEB1_TICK, FEBRUARY_SUNLIGHT),
    FEB2(FEB2_TICK, FEBRUARY_SUNLIGHT),

    MAR1(MAR1_TICK, MARCH_SUNLIGHT),
    MAR2(MAR2_TICK, MARCH_SUNLIGHT),

    APR1(APR1_TICK, APRIL_SUNLIGHT),
    APR2(APR2_TICK, APRIL_SUNLIGHT),

    MAY1(MAY1_TICK, MAY_SUNLIGHT),
    MAY2(MAY2_TICK, MAY_SUNLIGHT),

    JUN1(JUN1_TICK, JUNE_SUNLIGHT),
    JUN2(JUN2_TICK, JUNE_SUNLIGHT),

    JUL1(JUL1_TICK, JULY_SUNLIGHT),
    JUL2(JUL2_TICK, JULY_SUNLIGHT),

    AUG1(AUG1_TICK, AUGUST_SUNLIGHT),
    AUG2(AUG2_TICK, AUGUST_SUNLIGHT),

    SEP1(SEP1_TICK, SEPTEMBER_SUNLIGHT),
    SEP2(SEP2_TICK, SEPTEMBER_SUNLIGHT),

    OCT1(OCT1_TICK, OCTOBER_SUNLIGHT),
    OCT2(OCT2_TICK, OCTOBER_SUNLIGHT),

    NOV1(NOV1_TICK, NOVEMBER_SUNLIGHT),
    NOV2(NOV2_TICK, NOVEMBER_SUNLIGHT),

    DEC1(DEC1_TICK, DECEMBER_SUNLIGHT),
    DEC2(DEC2_TICK, DECEMBER_SUNLIGHT);

    /**
     * returns the next yearTick
     */
    fun getNext(): YearTick {
        return entries.find { it.tick == this.tick + 1 } ?: JAN1
    }

    /**
     * does the tick addition without overflow
     */
    fun addTicks(ticksToAdd: Int): YearTick {
        if (ticksToAdd == 0) return this
        return getNext().addTicks(ticksToAdd - 1)
    }

    /**
     *
     */
    fun getNextNovember(): Int {
        if (this == NOV1) return 24
        var tick = 0
        var yearTick = this
        while (yearTick != NOV1) {
            tick += 1
            yearTick = yearTick.getNext()
        }
        return tick
    }
}
