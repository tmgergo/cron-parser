package co.uk.tmgergo.cronparser

data class NextRun(
    val task: Task,
    val hour: Int,
    val minute: Int,
    val isToday: Boolean,
)
