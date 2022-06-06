package co.uk.tmgergo.cronparser.utils

import java.io.InputStream

object ResourceReader {
    fun resourceAsInputStream(resourceFileName: String): InputStream
        = this.javaClass.classLoader.getResourceAsStream(resourceFileName)
}
