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
            3 to day03.Day03(),
            4 to day04.Day04(),
            5 to day05.Day05(),
            6 to day06.Day06(),
            6 to day06.Day06(),
            7 to day07.Day07(),
            8 to day08.Day08(),
            9 to day09.Day09(),
            10 to day10.Day10(),
            11 to day11.Day11(),
            12 to day12.Day12(),
            13 to day13.Day13(),
            14 to day14.Day14(),
            15 to day15.Day15(),
            16 to day16.Day16(),
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
