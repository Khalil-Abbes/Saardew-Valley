package de.unisaarland.cs.se.selab.plant

import de.unisaarland.cs.se.selab.simulation.YearTick

const val TWENTY = 20
const val TWO = 2
const val THREE = 3
const val FIFTY = 50
const val FIVE = 5
const val NINE = 9
const val TEN = 10
const val THIRTY = 30
val LATE_RULE_TWENTY_TWO: LateRule = LateRule(TWENTY, TWO)

/**
 * Plant types with hard-coded information
 */

/**
 * Represents a general type of plant with common properties.
 * This sealed interface is implemented by specific plant types, such as field plants
 * and plantation plants, to define their unique characteristics.
 */
sealed interface PlantType {
    val initialHarvestEstimate: Int
    val moistureRequired: Int
    val sunlightComfort: Int
    val pollination: Pollination
    val harvestWindow: Window
    val bloomWindow: Blooming?
}

/**
 * Enum representing different types of field plants.
 * Each field plant type has specific properties such as sowing window and weeding rules.
 */
enum class FieldPlantType(
    override val initialHarvestEstimate: Int,
    override val moistureRequired: Int,
    override val sunlightComfort: Int,
    override val pollination: Pollination,
    override val harvestWindow: Window,
    override val bloomWindow: Blooming?,
    val sowingWindow: Window,
    val weedingRule: FieldWeedingRule
) : PlantType {
    POTATO(
        initialHarvestEstimate = 1_000_000,
        moistureRequired = 500,
        sunlightComfort = 130,
        pollination = Pollination.INSECTS,
        harvestWindow = Window(YearTick.SEP1, YearTick.OCT2, null),
        bloomWindow = RelativeBlooming(afterSowingTicks = 3, durationTicks = 1),
        sowingWindow = Window(YearTick.APR1, YearTick.MAY2, LATE_RULE_TWENTY_TWO),
        weedingRule = FieldWeedingRule(null, 2)
    ),
    WHEAT(

        initialHarvestEstimate = 1_500_000,
        moistureRequired = 450,
        sunlightComfort = 90,
        pollination = Pollination.SELF,
        harvestWindow = Window(YearTick.JUN1, YearTick.JUL1, LATE_RULE_TWENTY_TWO),
        bloomWindow = FixedBlooming(Window(YearTick.MAY1, YearTick.MAY1)),
        sowingWindow = Window(YearTick.OCT1, YearTick.OCT2, LATE_RULE_TWENTY_TWO),
        weedingRule = FieldWeedingRule(listOf(THREE, NINE), null)
    ),
    OAT(
        initialHarvestEstimate = 1_200_000,
        moistureRequired = 300,
        sunlightComfort = 90,
        pollination = Pollination.SELF,
        harvestWindow = Window(YearTick.JUL1, YearTick.AUG2, LATE_RULE_TWENTY_TWO),
        bloomWindow = null,
        sowingWindow = Window(YearTick.MAR2, YearTick.MAR2, LATE_RULE_TWENTY_TWO),
        weedingRule = FieldWeedingRule(listOf(1, 2, THREE), null)
    ),
    PUMPKIN(

        initialHarvestEstimate = 500_000,
        moistureRequired = 600,
        sunlightComfort = 120,
        pollination = Pollination.INSECTS,
        harvestWindow = Window(YearTick.SEP1, YearTick.OCT2, null),
        bloomWindow = RelativeBlooming(afterSowingTicks = 2, durationTicks = 2),
        sowingWindow = Window(YearTick.MAY2, YearTick.JUN2, LATE_RULE_TWENTY_TWO),
        weedingRule = FieldWeedingRule(null, 2)
    );

    override fun toString(): String {
        return when (this) {
            PUMPKIN -> "PUMPKIN"
            WHEAT -> "WHEAT"
            OAT -> "OAT"
            POTATO -> "POTATO"
        }
    }
}

/**
 * Enum representing different types of plantation plants.
 * Each plantation plant type has specific properties such as cutting and mowing windows.
 */
enum class PlantationPlantType(
    override val initialHarvestEstimate: Int,
    override val moistureRequired: Int,
    override val sunlightComfort: Int,
    override val pollination: Pollination,
    override val harvestWindow: Window,
    override val bloomWindow: Blooming?,
    val cuttingWindows: List<Window>,
    val mowingWindows: List<Window>
) : PlantType {
    APPLE(

        initialHarvestEstimate = 1_700_000,
        moistureRequired = 100,
        sunlightComfort = 50,
        pollination = Pollination.INSECTS,
        harvestWindow = Window(YearTick.SEP1, YearTick.OCT1, LateRule(FIFTY, 1)),
        bloomWindow = FixedBlooming(Window(YearTick.APR2, YearTick.MAY1)),
        cuttingWindows = listOf(
            Window(YearTick.NOV1, YearTick.NOV2),
            Window(YearTick.FEB1, YearTick.FEB2)
        ),
        mowingWindows = listOf(
            Window(YearTick.JUN1, YearTick.JUN1),
            Window(YearTick.SEP1, YearTick.SEP1)
        )
    ),
    ALMOND(

        initialHarvestEstimate = 800_000,
        moistureRequired = 400,
        sunlightComfort = 130,
        pollination = Pollination.INSECTS,
        harvestWindow = Window(YearTick.AUG2, YearTick.OCT1, LateRule(TEN, 1)),
        bloomWindow = FixedBlooming(Window(YearTick.FEB2, YearTick.MAR1)),
        cuttingWindows = listOf(
            Window(YearTick.NOV1, YearTick.NOV2),
            Window(YearTick.FEB1, YearTick.FEB2)
        ),
        mowingWindows = listOf(
            Window(YearTick.JUN1, YearTick.JUN1),
            Window(YearTick.SEP1, YearTick.SEP1)
        )
    ),
    CHERRY(

        initialHarvestEstimate = 1_200_000,
        moistureRequired = 150,
        sunlightComfort = 120,
        pollination = Pollination.INSECTS,
        harvestWindow = Window(YearTick.JUL1, YearTick.JUL2, LateRule(THIRTY, 1)),
        bloomWindow = FixedBlooming(Window(YearTick.APR2, YearTick.MAY1)),
        cuttingWindows = listOf(
            Window(YearTick.NOV1, YearTick.NOV2),
            Window(YearTick.FEB1, YearTick.FEB2)
        ),
        mowingWindows = listOf(Window(YearTick.JUN1, YearTick.JUN1))
    ),
    GRAPE(
        initialHarvestEstimate = 1_200_000,
        moistureRequired = 250,
        sunlightComfort = 150,
        pollination = Pollination.SELF,
        harvestWindow = Window(YearTick.SEP1, YearTick.SEP1, LateRule(FIVE, THREE)),
        bloomWindow = FixedBlooming(Window(YearTick.JUN2, YearTick.JUL1)),
        cuttingWindows = listOf(Window(YearTick.JUL2, YearTick.AUG2)),
        mowingWindows = listOf(
            Window(YearTick.APR1, YearTick.APR1),
            Window(YearTick.JUL1, YearTick.JUL1)
        )
    );

    override fun toString(): String {
        return when (this) {
            APPLE -> "APPLE"
            GRAPE -> "GRAPE"
            CHERRY -> "CHERRY"
            ALMOND -> "ALMOND"
        }
    }
}
