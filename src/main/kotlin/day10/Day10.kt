package day10

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution

class Day10 : AdventOfCode {
    override val day = 10

    override fun partOne(input: SolutionInput): SolutionResult {
        val board = input.parse()

        val paths = board.points()
            .filter { board.value(it) == 0 }
            .map { board.findPaths(it, setOf(it), listOf(it)) }
            .toSet()

        return paths.sumOf { it.flatten().filter { board.value(it) == 9 }.toSet().count() }.asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val board = input.parse()

        val paths = board.points()
            .filter { board.value(it) == 0 }
            .map { board.findPaths(it, setOf(it), listOf(it)) }
            .toSet()

        return paths.asSequence().flatten().flatten().filter { board.value(it) == 9 }.count().asSolution()
    }
}

data class Point(val row: Int, val col: Int) {
    override fun toString() = "$row,$col"
}

data class Board(val grid: List<List<Int>>) {

    override fun toString() =
        grid.joinToString("\n") { it.joinToString("") { it.toString() } }

    val maxRow get() = grid.size
    val maxCol get() = grid.first().size

    fun value(p: Point) = grid[p.row][p.col]

    fun points() = sequence {
        for (row in 0..<maxRow) {
            for (col in 0..<maxCol) {
                yield(Point(row, col))
            }
        }
    }

    fun neighbors(point: Point) = sequence {
        yield(point.copy(row = point.row - 1, col = point.col))
        yield(point.copy(row = point.row + 1, col = point.col))
        yield(point.copy(row = point.row, col = point.col - 1))
        yield(point.copy(row = point.row, col = point.col + 1))
    }.filter { valid(it) }

    fun valid(point: Point) = point.row in 0..<maxRow && point.col in 0..<maxCol

    fun findPaths(current: Point, visited: Set<Point>, path: List<Point>): Set<List<Point>> {
        return when {
            value(current) == 9 -> return setOf(path)
            else -> neighbors(current)
                .filter { value(it) == value(current) + 1 && it !in visited }
                .flatMap { findPaths(it, visited + current, path + it) }
                .toSet()
        }
    }
}

fun SolutionInput.parse(): Board {
    val grid = lines.map { it.asSequence().map { if (it == '.') 100 else it.digitToInt() }.toList() }
    return Board(grid)
}
