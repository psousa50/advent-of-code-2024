package day11

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution

class Day11 : AdventOfCode {
    override val day = 11

    override fun partOne(input: SolutionInput): SolutionResult {
        val stones = input.parseStones()

        return StonesCounter(25, stones).count().asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val stones = input.parseStones()

        return StonesCounter(75, stones).count().asSolution()
    }
}

data class StonesCounter(val blinks: Int, val stones: List<Long>) {
    fun count(): Long {
        val distribution = mutableMapOf<Long, Long>().withDefault { 0L }
        for (stone in stones) {
            distribution[stone] = 1L
        }
        val newDistribution = (1..blinks).fold(distribution) { acc, b -> blink(acc) }
        return newDistribution.values.sum()
    }

    private fun blink(distribution: MutableMap<Long, Long>): MutableMap<Long, Long> {
        val newDistribution = mutableMapOf<Long, Long>().withDefault { 0L }
        for ((stone, count) in distribution) {
            val newStones = stone.blink()
            for (newStone in newStones) {
                newDistribution[newStone] = newDistribution.getValue(newStone) + count
            }
        }
        return newDistribution
    }
}

fun SolutionInput.parseStones() = lines.first().split(" ").map { it.toLong() }

fun Long.blink(): List<Long> = when {
    this == 0L -> listOf(1)
    this.toString().length % 2 == 0 -> listOf(
        this.toString().take(this.toString().length / 2).toLong(),
        this.toString().takeLast(this.toString().length / 2).toLong()
    )

    else -> listOf(this * 2024)
}