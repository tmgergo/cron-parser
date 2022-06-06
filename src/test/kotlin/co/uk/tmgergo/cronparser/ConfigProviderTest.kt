package co.uk.tmgergo.cronparser

import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import kotlin.test.*

class ConfigProviderTest {
    private lateinit var closableMocks: AutoCloseable

    private lateinit var configProvider: ConfigProvider
    @Mock private lateinit var mockConfigReader: ConfigReader

    @BeforeTest
    fun setup() {
        closableMocks = MockitoAnnotations.openMocks(this)
    }

    @AfterTest
    fun teardown() {
        closableMocks.close()
    }

    @Test
    fun `should provide a failure when reading fails`() {
        `given reading the config will fail`()
        `and the config provider is created`()

        val result = `when the config is queried`()

        `then a failure is provided`(result)
    }

    @Test
    fun `should provide empty task list for empty config`() {
        `given the read config is empty`()
        `and the config provider is created`()

        val result = `when the config is queried`()

        `then an empty config is provided`(result)
    }

    @Test
    fun `should provide correct task list for non-empty config`() {
        `given config lines will be read`(listOf("1 1 cmd", "2 2 cmd2"))
        `and the config provider is created`()

        val result = `when the config is queried`()

        `then the correct tasks are provided`(expected = listOf(Task(1, 1, "cmd"), Task(2, 2, "cmd2")), result)
    }

    @Test
    fun `should filter out null tasks`() {
        `given config lines will be read`(listOf("* 1 cmd", "invalid 2 cmd2"))
        `and the config provider is created`()

        val result = `when the config is queried`()

        `then the correct tasks are provided`(expected = listOf(Task(TaskUtils.ALL_VALUES, 1, "cmd")), result)
    }

    private fun `given config lines will be read`(lines: List<String>) {
        whenever(mockConfigReader.readConfigLines()).thenReturn(Result.success(lines))
    }

    private fun `given the read config is empty`() {
        whenever(mockConfigReader.readConfigLines()).thenReturn(Result.success(emptyList()))
    }

    private fun `given reading the config will fail`() {
        whenever(mockConfigReader.readConfigLines()).thenReturn(Result.failure(Throwable()))
    }

    private fun `and the config provider is created`() {
        configProvider = ConfigProvider(mockConfigReader, TaskUtils::parse)
    }

    private fun `when the config is queried`() =
        configProvider.provideTasks()

    private fun `then the correct tasks are provided`(expected: List<Task>, result: Result<List<Task>>) {
        assertTrue(result.isSuccess)
        assertEquals(expected, result.getOrNull())
    }

    private fun `then an empty config is provided`(result: Result<List<Task>>) {
        assertTrue(result.isSuccess)
        assertEquals(0, result.getOrNull()?.size)
    }

    private fun `then a failure is provided`(result: Result<List<Task>>) {
        assertTrue(result.isFailure)
    }
}