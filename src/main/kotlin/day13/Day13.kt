package day13

import AdventOfCode
import Parsers.split
import SolutionInput
import SolutionResult
import asSolution

class Day13 : AdventOfCode {
    override val day = 13

    override fun partOne(input: SolutionInput): SolutionResult {
        val machines = input.lines.split { it.isBlank() }.map { it.parseMachine() }

        val steps = machines.map { calcSteps(it) }

        return steps.mapNotNull { it?.cost() }.sum().asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val machines = input.lines.split { it.isBlank() }.map { it.parseMachine() }

        val steps = machines.map { it.increasePrizeBy(10000000000000) }.map { calcSteps(it) }

        return steps.mapNotNull { it?.cost() }.sum().asSolution()
    }

    private fun calcSteps(machine: Machine): Pair<Long, Long>? {
        return machine.let {
            calcSteps(it.buttonA, it.buttonB, it.prize)
        }
    }

    fun calcSteps(buttonA: Coordinate, buttonB: Coordinate, prize: Coordinate): Pair<Long, Long>? {
        val ax = buttonA.x
        val ay = buttonA.y
        val bx = buttonB.x
        val by = buttonB.y
        val px = prize.x
        val py = prize.y

        val a = intDivision(px * by - py * bx, ax * by - ay * bx)
        val b = if (a == null) null else intDivision(px - ax * a, bx)
        return if (a != null && b != null) {
            Pair(a, b)
        } else {
            null
        }
    }

    private fun intDivision(a: Long, b: Long) =
        if (a % b == 0L) {
            a / b
        } else {
            null
        }

}

data class Coordinate(val x: Long, val y: Long) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

data class Machine(val buttonA: Coordinate, val buttonB: Coordinate, val prize: Coordinate) {
    override fun toString(): String {
        return "Machine(buttonA=$buttonA, buttonB=$buttonB, prize=$prize)"
    }

    fun increasePrizeBy(l: Long) = Machine(buttonA, buttonB, Coordinate(prize.x + l, prize.y + l))
}

fun List<String>.parseMachine(): Machine {
    val (buttonA, buttonB) = take(2).map { it.parseButton() }
    val prize = last().parsePrize()
    return Machine(buttonA, buttonB, prize)
}

fun String.parseButton(): Coordinate {
    val parts = split(" ")
    return Coordinate(parts[2].parseValue("+"), parts[3].parseValue("+"))
}

fun String.parsePrize(): Coordinate {
    val parts = split(" ")
    return Coordinate(parts[1].parseValue("="), parts[2].parseValue("="))
}

fun String.parseValue(sep: String): Long {
    return this.replace(",", "").split(sep)[1].toLong()
}

fun Pair<Long, Long>.cost(): Long {
    return 3 * first + second
}


