package de.unisaarland.cs.se.selab.tile

const val ANGLE0: Int = 0
const val ANGLE45: Int = 45
const val ANGLE90: Int = 90
const val ANGLE135: Int = 135
const val ANGLE180: Int = 180
const val ANGLE225: Int = 225
const val ANGLE270: Int = 270
const val ANGLE315: Int = 315

/**
 * Represents the possible directions with their corresponding angles in degrees.
 */
enum class Direction(var angle: Int) {

    ANGLE_0(ANGLE0),
    ANGLE_45(ANGLE45),
    ANGLE_90(ANGLE90),
    ANGLE_135(ANGLE135),
    ANGLE_180(ANGLE180),
    ANGLE_225(ANGLE225),
    ANGLE_270(ANGLE270),
    ANGLE_315(ANGLE315)
}
