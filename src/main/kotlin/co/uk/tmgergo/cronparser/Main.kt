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
}