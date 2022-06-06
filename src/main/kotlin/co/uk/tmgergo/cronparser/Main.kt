package co.uk.tmgergo.cronparser

fun main(args: Array<String>) {
    InputStreamConfigReader(StdinProvider()).readConfigLines()
        .fold(
            {
                it.forEach{ line -> println(line) }
            },
            {
                println(it.message)
            }
        )
}