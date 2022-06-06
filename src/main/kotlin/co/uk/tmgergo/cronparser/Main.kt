package co.uk.tmgergo.cronparser

fun main(args: Array<String>) {
    val simulatedTime = if (args.isEmpty()) null else SimulatedTimeParser.parse(args[0])

    simulatedTime?.let { now ->
        val configReader = InputStreamConfigReader(StdinProvider())
        val configProvider = ConfigProvider(configReader, TaskUtils::parse)
        configProvider.provideTasks().fold(
            {
                println("Next runs:")
                it.forEach { task ->
                    val nextRun = TaskUtils.calculateNextRun(task, now)
                    println("${nextRun.hour}:${nextRun.minute} "
                            + (if (nextRun.isToday) "today" else "tomorrow")
                            + " ${nextRun.task.command}"
                    )
                }
            }, {
                println("Failed to read task config: $it.message")
            }
        )
    } ?: run {
        println("No or invalid current time provided.")
    }
}