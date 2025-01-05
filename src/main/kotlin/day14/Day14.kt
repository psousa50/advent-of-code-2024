package day14

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution
import product

val width = 101
val height = 103

class Day14 : AdventOfCode {
    override val day = 14

    override fun partOne(input: SolutionInput): SolutionResult {
        val robots = input.lines.map { it.parseRobot() }

        val finalRobots = robots.map { it.move(100) }

        return finalRobots
            .groupBy { it.quadrant() }
            .filter { it.key != 0 }
            .mapValues { it.value.size }
            .values.product()
            .asSolution()

    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val robots = input.lines.map { it.parseRobot() }

        var stepsForChristmasTree = 0
        for (i in 7500..10000) {
            val newRobots = robots.map { it.move(i) }
            val gridStr = robotsToGridStr(newRobots)
            if (gridStr.contains("###########")) {
//                println("After $i seconds:")
//                println(gridStr)
                stepsForChristmasTree = i
                break
            }
        }

        return stepsForChristmasTree.asSolution()
    }
}

data class Robot(val x: Int, val y: Int, val vx: Int, val vy: Int) {
    fun move(steps: Int) =
        Robot((x + vx * steps) fmod width, (y + vy * steps) fmod height, vx, vy)

    fun quadrant() = when {
        x < width / 2 && y < height / 2 -> 1
        x > width / 2 && y < height / 2 -> 2
        x < width / 2 && y > height / 2 -> 3
        x > width / 2 && y > height / 2 -> 4
        else -> 0
    }

}

fun String.parseRobot(): Robot {
    val regex = Regex("-?\\d+")
    val numbers = regex.findAll(this).map { it.value }.toList()
    return Robot(numbers[0].toInt(), numbers[1].toInt(), numbers[2].toInt(), numbers[3].toInt())
}

infix fun Int.fmod(other: Int) = ((this % other) + other) % other

fun robotsToGridStr(robots: List<Robot>) =
    (0 until height).joinToString("\n") { y ->
        (0 until width).joinToString("") { x ->
            robots.find { it.x == x && it.y == y }?.let { "#" } ?: " "
        }
    }