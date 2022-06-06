package co.uk.tmgergo.cronparser

fun main(args: Array<String>) {
    val configReader = InputStreamConfigReader(StdinProvider())
    ConfigProvider(configReader, TaskUtils::parse).provideTasks()
        .fold(
            {
                it.forEach{ task -> println(task) }
            },
            {
                println(it.message)
            }
        )

    val currentTimeSting = if (args.isEmpty()) null else args[0]

    currentTimeSting?.let {
        SimulatedTimeParser.parse(it)?.let { currentTime ->
            println("Current time: $currentTime")
        } ?: run {
            println("Invalid current time data provided.")
        }
    } ?: run {
        println("No current time data provided.")
    }
}