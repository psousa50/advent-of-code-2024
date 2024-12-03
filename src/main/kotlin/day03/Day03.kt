package day03

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution

class Day03 : AdventOfCode {
    override val day = 3

    override fun partOne(input: SolutionInput): SolutionResult {
        val multiplications = input.lines.map { it.extractMultiplications() }.flatten()

        val values = multiplications.mapNotNull { it.extractXY() }

        return values.sumOf { it.first * it.second }.asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val multiplicationsWithEnablers = input.lines.map { it.extractMultiplications() }.flatten()

        val values = multiplicationsWithEnablers.fold(
            MultiplicationsState(isEnabled = true, enabledMultiplications = emptyList()),
            ::extractEnabledMultiplications
        ).enabledMultiplications.mapNotNull { it.extractXY() }

        return values.sumOf { it.first * it.second }.asSolution()
    }

    private fun extractEnabledMultiplications(
        state: MultiplicationsState,
        multiplication: String
    ) =
        when {
            multiplication == "do()" -> state.copy(isEnabled = true)
            multiplication == "don't()" -> state.copy(isEnabled = false)
            state.isEnabled -> state.copy(enabledMultiplications = state.enabledMultiplications + multiplication)
            else -> state
        }
}

data class MultiplicationsState(val isEnabled: Boolean, val enabledMultiplications: List<String>)

fun String.extractMultiplications(): List<String> {
    val regex = """mul\(\d{1,3},\d{1,3}\)|do\(\)|don't\(\)""".toRegex()
    return regex.findAll(this).map { it.value }.toList()
}

fun String.extractXY(): Pair<Int, Int>? {
    val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
    val matchResult = regex.find(this)
    return matchResult?.destructured?.let { (x, y) -> x.toInt() to y.toInt() }
}
