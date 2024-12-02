package day02

import AdventOfCode
import Parsers.toLongs
import SolutionInput
import SolutionResult
import asSolution
import kotlin.math.abs

class Day02 : AdventOfCode {
    override val day = 2

    override fun partOne(input: SolutionInput): SolutionResult {
        val reports = input.lines.map { it.toLongs() }
        return reports.count { it.safe() }.asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val reports = input.lines.map { it.toLongs() }
        return reports.count { it.safeWithOneDropped() }.asSolution()
    }
}

fun List<Long>.safe(): Boolean {
    val differences = zipWithNext { a, b -> b - a }
    return (differences.all { it > 0 } || differences.all { it < 0 }) && differences.all { abs(it) <= 3 }
}

fun List<Long>.safeWithOneDropped() =
    indices.any { indexToRemove ->
        filterIndexed { index, _ -> index != indexToRemove }.safe()
    }


