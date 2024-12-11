package day07

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution

class Day07 : AdventOfCode {
    override val day = 7

    override fun partOne(input: SolutionInput): SolutionResult {
        val calibrations = input.lines.map { it.toCalibration() }

        val operators = listOf(Operator.PLUS, Operator.TIMES)
        return calibrations.filter { it.checkWith(operators).canReachTestValue }
            .sumOf { it.testValue }
            .asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val calibrations = input.lines.map { it.toCalibration() }

        val operators = listOf(Operator.PLUS, Operator.TIMES, Operator.CONCAT)
        return calibrations.filter { it.checkWith(operators).canReachTestValue }
            .sumOf { it.testValue }
            .asSolution()
    }
}

enum class Operator {
    PLUS,
    TIMES,
    CONCAT,
}

data class Calibration(
    val testValue: Long,
    val numbers: List<Long>,
    val canReachTestValue: Boolean = false,
)

fun Calibration.checkWith(operators: List<Operator>): Calibration {
    val numberOfPermutations = Math.pow(operators.size.toDouble(), numbers.size.minus(1).toDouble()).toInt()

    val results = (0..<numberOfPermutations).map {
        val opMap = it.toString(operators.size).padStart(numbers.size - 1, '0').map { it.code - '0'.code }
        numbers.reduceIndexed { index, result, number ->
            val operator = operators[opMap[index - 1]]
            operation(result, operator, number)
        }
    }

    return copy(canReachTestValue = results.contains(testValue))
}

fun operation(n1: Long, op: Operator, n2: Long) =
    when (op) {
        Operator.PLUS -> n1 + n2
        Operator.TIMES -> n1 * n2
        Operator.CONCAT -> "${n1}${n2}".toLong()
    }


fun String.toCalibration(): Calibration {
    val parts = split(":").map { it.trim() }
    val testValue = parts[0].toLong()
    val operators = parts[1].split(" ").map { it.toLong() }
    return Calibration(testValue, operators)
}