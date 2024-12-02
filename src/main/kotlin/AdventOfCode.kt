interface AdventOfCode {
    val day: Int

    fun partOne(input: SolutionInput): SolutionResult {
        TODO("Not yet implemented")
    }

    fun partTwo(input: SolutionInput): SolutionResult {
        TODO("Not yet implemented")
    }

    companion object {
        val days = mapOf(
            1 to day01.Day01(),
2 to day02.Day02(),
            // {{ NextDay }}
        )
    }
}

data class SolutionInput(
    val lines: List<String>
)

data class SolutionResult(val value: String) {
    val asInt get() = value.toInt()
    val asLong get() = value.toLong()
    override fun toString() = value

}

fun Int.asSolution() = SolutionResult(this.toString())
fun Long.asSolution() = SolutionResult(this.toString())
