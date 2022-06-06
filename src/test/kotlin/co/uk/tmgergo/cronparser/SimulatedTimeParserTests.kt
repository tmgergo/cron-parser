package co.uk.tmgergo.cronparser

import kotlin.test.*

class SimulatedTimeParserTests {

    @Test
    fun `should parse valid string successfully`() {
        // no pre-requisites

        val simulatedTime = `when a text is parsed`("03:59")

        `then the correct simulated time is provided`(expected = SimulatedTime(3, 59), simulatedTime)
    }

    @Test
    fun `should provide null for invalid time string`() {
        // no pre-requisites

        val simulatedTime = `when a text is parsed`("invalid time string")

        `then the correct simulated time is provided`(expected = null, simulatedTime)
    }

    private fun `then the correct simulated time is provided`(expected: SimulatedTime?, actual: SimulatedTime?) {
        assertEquals(expected, actual)
    }

    private fun `when a text is parsed`(timeString: String) =
        SimulatedTimeParser.parse(timeString)
}