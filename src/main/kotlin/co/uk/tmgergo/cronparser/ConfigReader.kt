package co.uk.tmgergo.cronparser

import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

interface ConfigReader {
    fun readConfigLines() : Result<List<String>>
}

class InputStreamConfigReader(private val inputStreamProvider: InputStreamProvider) : ConfigReader {
    override fun readConfigLines() : Result<List<String>>  {
        return try {
            val inputStream = inputStreamProvider.provideInputStream()
            Result.success(readLines(inputStream))
        } catch (e: IOException) {
            Result.failure(Throwable("Failed to read config from source.", e))
        }
    }

    private fun readLines(inputStream: InputStream): List<String> {
        val lines = mutableListOf<String>()
        if (inputStream.available() > 0) {
            inputStream.reader(StandardCharsets.UTF_8).useLines { linesSequence ->
                linesSequence.forEach { line ->
                    lines.add(line)
                }
            }
        }
        return lines
    }
}