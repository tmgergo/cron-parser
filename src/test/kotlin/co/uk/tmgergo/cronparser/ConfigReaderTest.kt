package co.uk.tmgergo.cronparser

import co.uk.tmgergo.cronparser.utils.ResourceReader
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import java.io.IOException
import java.io.InputStream
import kotlin.test.*

internal class ConfigReaderTest {
    private lateinit var closableMocks: AutoCloseable

    private lateinit var configReader: InputStreamConfigReader
    private lateinit var inputStream: InputStream
    @Mock private lateinit var mockInputStream: InputStream

    @BeforeTest
    fun setup() {
        closableMocks = MockitoAnnotations.openMocks(this)
    }

    @AfterTest
    fun teardown() {
        closableMocks.close()
    }

    @Test
    fun `should return all config lines when all are valid`() {
        `given the config contains valid data`("example_config.txt")
        `and the config reader is created`(inputStream)

        val result = `when the config is read`()

        `then the correct config lines are provided`(result, expectedNumOfLines = 4)
    }


    @Test
    fun `should return empty config when no data is available`() {
        `given the config contains valid data`("empty_config.txt")
        `and the config reader is created`(inputStream)

        val result = `when the config is read`()

        `then the correct config lines are provided`(result, expectedNumOfLines = 0)
    }

    @Test
    fun `should return failure when reading fails`() {
        `given reading the config will fail`()
        `and the config reader is created`(mockInputStream)

        val result = `when the config is read`()

        `then a failure is provided`(result)
    }

    private fun `given the config contains valid data`(configResource: String) {
        inputStream = ResourceReader.resourceAsInputStream(configResource)
    }

    private fun `given reading the config will fail`() {
        whenever(mockInputStream.available()).thenThrow(IOException())
    }

    private fun `and the config reader is created`(inputStream: InputStream) {
        configReader = InputStreamConfigReader(object : InputStreamProvider {
            override fun provideInputStream(): InputStream {
                return inputStream
            }
        })
    }

    private fun `when the config is read`() =
        configReader.readConfigLines()

    private fun `then the correct config lines are provided`(result: Result<List<String>>, expectedNumOfLines: Int) {
        assertTrue(result.isSuccess)
        assertEquals(result.getOrNull()?.size, expectedNumOfLines)
    }

    private fun `then a failure is provided`(result: Result<List<String>>) {
        assertTrue(result.isFailure)
    }
}