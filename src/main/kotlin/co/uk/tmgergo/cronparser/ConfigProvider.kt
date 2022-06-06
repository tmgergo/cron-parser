package co.uk.tmgergo.cronparser

class ConfigProvider(
    private val configReader: ConfigReader,
    private val taskParser: ((String) -> Task?),
) {
    fun provideTasks() : Result<List<Task>> {
        return configReader.readConfigLines()
            .mapCatching { configLines ->
                configLines.mapNotNull { line -> taskParser(line) }
            }
    }
}