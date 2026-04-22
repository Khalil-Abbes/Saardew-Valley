package de.unisaarland.cs.se.selab

import de.unisaarland.cs.se.selab.logger.DebugLogger
import de.unisaarland.cs.se.selab.logger.ImportantLogger
import de.unisaarland.cs.se.selab.logger.InfoLogger
import de.unisaarland.cs.se.selab.logger.Logger
import de.unisaarland.cs.se.selab.parser.MainParser
import de.unisaarland.cs.se.selab.simulation.YearTick
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

/**
 Main Function
 **/
fun main(args: Array<String>) {
    var mapFile: String? = null
    var farmsFile: String? = null
    var scenarioFile: String? = null
    var outputFile: String? = null
    var startYearTick = 1
    var maxTicks: Int? = null
    var logLevel: String? = null
    var file = PrintWriter(System.out)
    for (i in args.indices) {
        when (args[i]) {
            "--map" -> mapFile = args[i + 1]
            "--farms" -> farmsFile = args[i + 1]
            "--scenario" -> scenarioFile = args[i + 1]
            "--start_year_tick" -> startYearTick = args[i + 1].toInt()
            "--max_ticks" -> maxTicks = args[i + 1].toInt()
            "--log_level" -> logLevel = args[i + 1]
            "--out" -> outputFile = args[i + 1]
            "--help" -> continue
        }
    }
    if (outputFile != null) {
        val output = File(outputFile)
        file = PrintWriter(FileWriter(output))
    }
    val concreteLogger = createLogger(logLevel, file)
    if (concreteLogger != null) {
        Logger.concreteLogger = concreteLogger
    }
    val yearTick = YearTick.entries.find { it.tick == startYearTick }
    val filesNotNull: Boolean = mapFile != null && farmsFile != null && scenarioFile != null
    if (yearTick != null && maxTicks != null && filesNotNull) {
        val simulation = MainParser(yearTick, maxTicks).parse(mapFile, farmsFile, scenarioFile)
        simulation?.run()
    }
    file.close()
}

private fun createLogger(logLevel: String?, file: PrintWriter): ImportantLogger? {
    return when (logLevel) {
        "DEBUG" -> DebugLogger(file)
        "INFO" -> InfoLogger(file)
        "IMPORTANT" -> ImportantLogger(file)
        else -> return null
    }
}
