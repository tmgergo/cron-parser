package co.uk.tmgergo.cronparser

import java.io.InputStream

interface InputStreamProvider {
    fun provideInputStream() : InputStream
}

class StdinProvider : InputStreamProvider {
    override fun provideInputStream(): InputStream =
        System.`in`
}
