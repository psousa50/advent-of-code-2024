package day11

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution

class Day11 : AdventOfCode {
    override val day = 11

    override fun partOne(input: SolutionInput): SolutionResult {
        val stones = input.parseStones()

        val distribution = repeatBlink(25, stones.associateWith { 1L })

        return distribution.values.sum().asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val stones = input.parseStones()

        val distribution = repeatBlink(75, stones.associateWith { 1L })

        return distribution.values.sum().asSolution()
    }
}

tailrec fun repeatBlink(count: Int, distribution: Map<Long, Long>): Map<Long, Long> {
    return if (count == 0) distribution else repeatBlink(count - 1, blink(distribution))
}

fun blink(distribution: Map<Long, Long>): Map<Long, Long> {
    val newDistribution = mutableMapOf<Long, Long>().withDefault { 0L }
    for ((stone, count) in distribution) {
        val newStones = stone.blink()
        for (newStone in newStones) {
            newDistribution[newStone] = newDistribution.getValue(newStone) + count
        }
    }
    return newDistribution.toMap()
}

fun SolutionInput.parseStones() = lines.first().split(" ").map { it.toLong() }

fun Long.blink(): List<Long> = this.toString().length.let { length ->
    when {
        this == 0L -> listOf(1)
        length % 2 == 0 -> this.toString().let {
            listOf(
                it.take(length / 2).toLong(),
                it.takeLast(length / 2).toLong()
            )
        }

        else -> listOf(this * 2024)
    }
}
