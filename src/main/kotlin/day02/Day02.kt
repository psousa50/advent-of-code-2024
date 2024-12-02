package day02

import AdventOfCode
import Parsers.toLongs
import SolutionInput
import SolutionResult
import asSolution
import jdk.javadoc.internal.doclets.toolkit.util.DocPath.empty
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
    val diffs = List(this.drop(1).size) { i -> this[i + 1] - this[i] }
    return (diffs.all { it > 0 } || diffs.all { it < 0 }) && diffs.all { abs(it) <= 3 }
}

fun List<Long>.safeWithOneDropped() =
    List(size) { toRemove ->
        filterIndexed { i, _ -> i != toRemove }
    }.any { it.safe() }

