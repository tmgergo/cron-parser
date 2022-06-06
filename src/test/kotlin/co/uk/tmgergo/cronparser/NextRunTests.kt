package co.uk.tmgergo.cronparser

import kotlin.test.*

class NextRunTests {

    @Test
    fun `should calculate next run for a task later today`() {
        // no pre-requisites

        val nextRun = `when the next run is calculated`(
            Task(33, 19, "cmd"),
            SimulatedTime(16, 45)
        )

        `then the correct next run is provided`(expected = NextRun(Task(33, 19, "cmd"), 19, 33, isToday = true), nextRun)
    }

    @Test
    fun `should calculate next run for a task tomorrow`() {
        // no pre-requisites

        val nextRun = `when the next run is calculated`(
            Task(33, 5, "cmd"),
            SimulatedTime(16, 45)
        )

        `then the correct next run is provided`(expected = NextRun(Task(33, 5, "cmd"), 5, 33, isToday = false), nextRun)
    }

    @Test
    fun `should calculate next run for a task tomorrow within the same hour`() {
        // no pre-requisites

        val nextRun = `when the next run is calculated`(
            Task(33, 5, "cmd"),
            SimulatedTime(5, 45)
        )

        `then the correct next run is provided`(expected = NextRun(Task(33, 5, "cmd"), 5, 33, isToday = false), nextRun)
    }

    @Test
    fun `should calculate next run for a task right now`() {
        // no pre-requisites

        val nextRun = `when the next run is calculated`(
            Task(33, 5, "cmd"),
            SimulatedTime(5, 33)
        )

        `then the correct next run is provided`(expected = NextRun(Task(33, 5, "cmd"), 5, 33, isToday = true), nextRun)
    }

    @Test
    fun `should calculate next run for a task with all-values hour within an hour`() {
        // no pre-requisites

        val nextRun = `when the next run is calculated`(
            Task(33, TaskUtils.ALL_VALUES, "cmd"),
            SimulatedTime(6, 30)
        )

        `then the correct next run is provided`(expected = NextRun(Task(33, TaskUtils.ALL_VALUES, "cmd"), 6, 33, isToday = true), nextRun)
    }

    @Test
    fun `should calculate next run for a task with all-values hour in the next hour`() {
        // no pre-requisites

        val nextRun = `when the next run is calculated`(
            Task(27, TaskUtils.ALL_VALUES, "cmd"),
            SimulatedTime(6, 30)
        )

        `then the correct next run is provided`(expected = NextRun(Task(27, TaskUtils.ALL_VALUES, "cmd"), 7, 27, isToday = true), nextRun)
    }

    @Test
    fun `should calculate next run for a task with all-values hour in the next hour tomorrow`() {
        // no pre-requisites

        val nextRun = `when the next run is calculated`(
            Task(27, TaskUtils.ALL_VALUES, "cmd"),
            SimulatedTime(23, 30)
        )

        `then the correct next run is provided`(expected = NextRun(Task(27, TaskUtils.ALL_VALUES, "cmd"), 0, 27, isToday = false), nextRun)
    }

    @Test
    fun `should calculate next run for a task with all-values hour and all-values minute`() {
        // no pre-requisites

        val nextRun = `when the next run is calculated`(
            Task(TaskUtils.ALL_VALUES, TaskUtils.ALL_VALUES, "cmd"),
            SimulatedTime(6, 33)
        )

        `then the correct next run is provided`(expected = NextRun(Task(TaskUtils.ALL_VALUES, TaskUtils.ALL_VALUES, "cmd"), 6, 33, isToday = true), nextRun)
    }

    @Test
    fun `should calculate next run for a task with all-values minute in the same hour`() {
        // no pre-requisites

        val nextRun = `when the next run is calculated`(
            Task(TaskUtils.ALL_VALUES, 6, "cmd"),
            SimulatedTime(6, 33)
        )

        `then the correct next run is provided`(expected = NextRun(Task(TaskUtils.ALL_VALUES, 6, "cmd"), 6, 33, isToday = true), nextRun)
    }

    @Test
    fun `should calculate next run for a task with all-values minute in a different hour`() {
        // no pre-requisites

        val nextRun = `when the next run is calculated`(
            Task(TaskUtils.ALL_VALUES, 0, "cmd"),
            SimulatedTime(23, 33)
        )

        `then the correct next run is provided`(expected = NextRun(Task(TaskUtils.ALL_VALUES, 0, "cmd"), 0, 0, isToday = false), nextRun)
    }

    private fun `then the correct next run is provided`(expected: NextRun, actual: NextRun) {
        assertEquals(expected, actual)
    }

    private fun `when the next run is calculated`(task: Task, now: SimulatedTime) =
        TaskUtils.calculateNextRun(task, now)
}