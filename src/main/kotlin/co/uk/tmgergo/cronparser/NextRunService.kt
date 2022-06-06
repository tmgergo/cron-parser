package co.uk.tmgergo.cronparser

import java.io.PrintStream

class NextRunService(
    private val simulatedTimeParser: ((String) -> SimulatedTime?),
    private val configProvider: ConfigProvider,
    private val nextRunCalculator: ((Task, SimulatedTime) -> NextRun),
) {
    fun printNextRuns(currentTimeString: String, printer: PrintStream) {
        val simulatedTime = simulatedTimeParser(currentTimeString)

        simulatedTime?.let { now ->
            getNextRuns(now).fold(
                {
                    printNextRunList(it, printer)
                }, {
                    printer.println("Failed to calculate next runs: ${it.message}")
                }
            )
        } ?: run {
            printer.println("Invalid current time data provided: \"$currentTimeString\". The valid format is HH:mm, e.g. \"16:40\".")
        }
    }

    private fun getNextRuns(now: SimulatedTime): Result<List<NextRun>>
            = configProvider.provideTasks()
                .mapCatching { taskList ->
                    taskList.map { nextRunCalculator(it, now) }
                }

    private fun printNextRunList(nextRuns: List<NextRun>, printer: PrintStream) {
        if (nextRuns.isEmpty()) {
            printer.println("No valid tasks found in config.")
        } else {
            nextRuns.forEach { nextRun ->
                printer.println(
                    "${nextRun.hour}:${"%02d".format(nextRun.minute)} "
                            + (if (nextRun.isToday) "today" else "tomorrow")
                            + " ${nextRun.task.command}"
                )
            }
        }
    }
}