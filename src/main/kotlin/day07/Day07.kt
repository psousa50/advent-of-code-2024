package day07

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution
import com.sun.org.apache.xalan.internal.lib.ExsltMath
import jdk.internal.org.jline.utils.Colors.s

class Day07 : AdventOfCode {
    override val day = 7

    override fun partOne(input: SolutionInput): SolutionResult {
        val calibrations = input.lines.map { it.toCalibration() }

        val operators = listOf(Operator.PLUS, Operator.TIMES)
        return calibrations.sumOf { it.canBeMadeEqual(operators) }.asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val calibrations = input.lines.map { it.toCalibration() }

        val operators = listOf(Operator.PLUS, Operator.TIMES, Operator.CONCAT)
        return calibrations.sumOf { it.canBeMadeEqual(operators) }.asSolution()
    }
}

data class Calibration(
    val testValue: Long,
    val numbers: List<Long>
)

enum class Operator {
    PLUS,
    TIMES,
    CONCAT,
}

fun Calibration.canBeMadeEqual(operators: List<Operator>): Long {
    val numberOfPermutations = Math.pow(operators.size.toDouble(), numbers.size.minus(1).toDouble()).toInt()

    val results = (0..<numberOfPermutations).map {
        val s = it.toString(operators.size).padStart(numbers.size - 1, '0')
        numbers.reduceIndexed { index, acc, n1 ->
            val operator = operators[s[index - 1].code - '0'.code]
            operation(acc, operator, n1)
        }
    }

    return if (results.contains(testValue)) testValue else 0

}

fun operation(n1: Long, op: Operator, n2: Long) =
    when (op) {
        Operator.PLUS -> n1 + n2
        Operator.TIMES -> n1 * n2
        Operator.CONCAT -> "${n1}${n2}".toLong()
    }


fun String.toCalibration(): Calibration {
    val splitted = split(":").map { it.trim() }
    val testValue = splitted[0].toLong()
    val operators = splitted[1].split(" ").map { it.toLong() }
    return Calibration(testValue, operators)
}