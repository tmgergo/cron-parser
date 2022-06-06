package co.uk.tmgergo.cronparser

import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.PrintStream
import kotlin.test.*


class NextRunServiceTests {
    private lateinit var closableMocks: AutoCloseable

    private lateinit var nextRunService: NextRunService
    @Mock private lateinit var mockConfigProvider: ConfigProvider
    @Mock private lateinit var mockPrintStream: PrintStream

    @BeforeTest
    fun setup() {
        closableMocks = MockitoAnnotations.openMocks(this)
    }

    @AfterTest
    fun teardown() {
        closableMocks.close()
    }

    @Test
    fun `should print invalid current time message`() {
        `given the service is created`()

        `when next tasks are requested with invalid current time`()

        `then an error message is printed`("Invalid current time data provided: \"oops\". The valid format is HH:mm, e.g. \"16:40\".")
    }

    @Test
    fun `should print error message on failure`() {
        `given the service is created`()
        `and querying the config will fail`("oops")

        `when next tasks are requested`("16:10")

        `then an error message is printed`("Failed to calculate next runs: oops")
    }

    @Test
    fun `should print empty message for empty task list`() {
        `given the service is created`()
        `and querying the config will succeed`(emptyList())

        `when next tasks are requested`("16:10")

        `then an error message is printed`("No valid tasks found in config.")
    }

    @Test
    fun `should print next run list for tasks`() {
        `given the service is created`()
        `and querying the config will succeed`(listOf(
            Task(30, 1, "/bin/run_me_daily"),
            Task(TaskUtils.ALL_VALUES, 19, "/bin/run_me_sixty_times"),
        ))

        `when next tasks are requested`("16:10")

        `then the next run list is printed`(listOf("1:30 tomorrow /bin/run_me_daily", "19:00 today /bin/run_me_sixty_times"))
    }

    private fun `and querying the config will succeed`(tasks: List<Task>) {
        whenever(mockConfigProvider.provideTasks()).thenReturn(Result.success(tasks))
    }

    private fun `when next tasks are requested`(currentTime: String) {
        nextRunService.printNextRuns(currentTime, mockPrintStream)
    }

    private fun `and querying the config will fail`(errorMessage: String) {
        whenever(mockConfigProvider.provideTasks()).thenReturn(Result.failure(Throwable(errorMessage)))
    }

    private fun `then an error message is printed`(message: String) {
        verify(mockPrintStream).println(message)
    }

    private fun `then the next run list is printed`(messages: List<String>) {
        val messageCaptor = argumentCaptor<(String)>()
        verify(mockPrintStream, times(messages.size)).println(messageCaptor.capture())
        assertEquals(messages, messageCaptor.allValues)
    }

    private fun `given the service is created`() {
        nextRunService = NextRunService(SimulatedTimeParser::parse, mockConfigProvider, TaskUtils::calculateNextRun)
    }

    private fun `when next tasks are requested with invalid current time`() {
        nextRunService.printNextRuns("oops", mockPrintStream)
    }

}
