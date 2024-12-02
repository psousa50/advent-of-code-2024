package day01

import AdventOfCode
import Parsers.toLongs
import SolutionInput
import SolutionResult
import asSolution
import kotlin.math.abs
import kotlin.math.min

class Day01 : AdventOfCode {
    override val day = 1

    override fun partOne(input: SolutionInput): SolutionResult {
        val numbers = input.lines.map { it.toLongs() }
        val (first, second) = List(min(numbers.maxOf { it.size }, numbers.size)) { i ->
            numbers.mapNotNull { line -> line.getOrNull(i) }
        }.map { l -> l.sorted() }

        val differences = List(first.size) { i -> abs(first[i] - second[i]) }
        return differences.sum().asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val numbers = input.lines.map { it.toLongs() }
        val (first, second) = List(min(numbers.maxOf { it.size }, numbers.size)) { i ->
            numbers.mapNotNull { line -> line.getOrNull(i) }
        }

        val similarityScore = first.map { n -> n * second.count { it == n } }
        return similarityScore.sum().asSolution()
    }
}
