package co.uk.tmgergo.cronparser

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

data class SimulatedTime(
    val hour: Int,
    val minute: Int,
)

object SimulatedTimeParser {
    private val PATTERN: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun parse(timeString: String): SimulatedTime? =
        try {
            val localTime = LocalTime.parse(timeString, PATTERN)
            SimulatedTime(localTime.hour, localTime.minute)
        } catch (e: DateTimeParseException) {
            null
        }
}
