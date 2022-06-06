package co.uk.tmgergo.cronparser

fun main(args: Array<String>) {
    val currentTimeSting = if (args.isEmpty()) null else args[0]

    currentTimeSting?.let {
        val nextRunService = NextRunService(
            SimulatedTimeParser::parse,
            ConfigProvider(InputStreamConfigReader(StdinProvider()), TaskUtils::parse),
            TaskUtils::calculateNextRun
        )
        nextRunService.printNextRuns(it, System.out)
    } ?: run {
        println("No current time data provided.")
    }
}