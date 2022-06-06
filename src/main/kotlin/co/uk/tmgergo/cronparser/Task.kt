package co.uk.tmgergo.cronparser

import java.util.regex.Pattern

data class Task(
    val minute: Int,
    val hour: Int,
    val command: String,
)

object TaskUtils {
    private val WHITESPACES: Pattern = Pattern.compile("\\s+")
    private const val ALL_VALUES_STRING: String = "*"
    const val ALL_VALUES: Int = Int.MIN_VALUE

    fun parse(taskConfig: String): Task? {
        val words = splitToWords(taskConfig)
        if (words.size < 3) return null

        val minute = parseMinute(words[0]) ?: return null
        val hour = parseHour(words[1]) ?: return null
        val command = joinConfigWordsAsCommand(words)

        return Task(minute, hour, command)
    }

    private fun splitToWords(taskConfig: String): List<String> =
        WHITESPACES.split(taskConfig.trim()).toList()

    private fun parseMinute(minuteStr: String): Int? {
        if (isAllValues(minuteStr)) return ALL_VALUES
        val minute = try { minuteStr.toInt() } catch (e: NumberFormatException) { return null }
        if (minute !in 0..59) return null
        return minute
    }

    private fun parseHour(hourStr: String): Int? {
        if (isAllValues(hourStr)) return ALL_VALUES
        val hour = try { hourStr.toInt() } catch (e: NumberFormatException) { return null }
        if (hour !in 0..23) return null
        return hour
    }

    private fun joinConfigWordsAsCommand(words: Collection<String>): String =
        words.filterIndexed { index, _ -> index >= 2 }.joinToString(separator = " ")

    private fun isAllValues(minuteStr: String): Boolean =
        minuteStr == ALL_VALUES_STRING
}
