package co.uk.tmgergo.cronparser

import kotlin.test.*

class TaskUtilsTests {
    @Test
    fun `should map to null for empty string`() {
        // no pre-requisites

        val task = `when a config line is parsed`("")

        `then the result is null`(task)
    }

    @Test
    fun `should map to null for string with only whitespaces`() {
        // no pre-requisites

        val task = `when a config line is parsed`("     ")

        `then the result is null`(task)
    }

    @Test
    fun `should map to null for string with no whitespaces`() {
        // no pre-requisites

        val task = `when a config line is parsed`("nowhitespaces")

        `then the result is null`(task)
    }

    @Test
    fun `should map to null for string with only one word (and whitespaces)`() {
        // no pre-requisites

        val task = `when a config line is parsed`("    oneword      ")

        `then the result is null`(task)
    }

    @Test
    fun `should map to null for string with only two words`() {
        // no pre-requisites

        val task = `when a config line is parsed`("first    second   ")

        `then the result is null`(task)
    }

    @Test
    fun `should map to task for numeral minute and hour and a single word command`() {
        // no pre-requisites

        val task = `when a config line is parsed`("33 19 command  ")

        `then the task is successfully parsed`(expected = Task(33, 19, "command"), task)
    }

    @Test
    fun `should map to task for numeral minute and hour and a multi-word command`() {
        // no pre-requisites

        val task = `when a config line is parsed`(" 33   19   multi-word command param1 param2 ")

        `then the task is successfully parsed`(expected = Task(33, 19, "multi-word command param1 param2"), task)
    }

    @Test
    fun `should map to null for invalid minute`() {
        // no pre-requisites

        val task = `when a config line is parsed`("oops 19   multi-word command param1 param2")

        `then the result is null`(task)
    }

    @Test
    fun `should map to null for invalid hour`() {
        // no pre-requisites

        val task = `when a config line is parsed`("33 oops   multi-word command param1 param2")

        `then the result is null`(task)
    }

    @Test
    fun `should map to null for negative minute`() {
        // no pre-requisites

        val task = `when a config line is parsed`("-1 19   multi-word command param1 param2")

        `then the result is null`(task)
    }

    @Test
    fun `should map to null for too high minute`() {
        // no pre-requisites

        val task = `when a config line is parsed`("60 19   multi-word command param1 param2")

        `then the result is null`(task)
    }

    @Test
    fun `should map to null for negative hour`() {
        // no pre-requisites

        val task = `when a config line is parsed`("33 -1   multi-word command param1 param2")

        `then the result is null`(task)
    }

    @Test
    fun `should map to null for too high hour`() {
        // no pre-requisites

        val task = `when a config line is parsed`("33 24   multi-word command param1 param2")

        `then the result is null`(task)
    }

    @Test
    fun `should map to task for all-values minute`() {
        // no pre-requisites

        val task = `when a config line is parsed`("* 19   multi-word command param1 param2 ")

        `then the task is successfully parsed`(expected = Task(TaskUtils.ALL_VALUES, 19, "multi-word command param1 param2"), task)
    }

    @Test
    fun `should map to task for all-values hour`() {
        // no pre-requisites

        val task = `when a config line is parsed`("33 *   multi-word command param1 param2 ")

        `then the task is successfully parsed`(expected = Task(33, TaskUtils.ALL_VALUES, "multi-word command param1 param2"), task)
    }

    private fun `when a config line is parsed`(configLine: String): Task? =
        TaskUtils.parse(configLine)

    private fun `then the result is null`(task: Task?) {
        assertNull(task)
    }

    private fun `then the task is successfully parsed`(expected: Task, actual: Task?) {
        assertEquals(expected, actual)
    }
}