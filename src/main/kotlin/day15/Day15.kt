package day15

import AdventOfCode
import Parsers.split
import SolutionInput
import SolutionResult
import asSolution
import day15.Day15.Companion.BOX_CHAR
import day15.Day15.Companion.BOX_LEFT_CHAR
import day15.Day15.Companion.BOX_RIGHT_CHAR
import day15.Day15.Companion.DOWN_CHAR
import day15.Day15.Companion.EMPTY_CHAR
import day15.Day15.Companion.LEFT_CHAR
import day15.Day15.Companion.RIGHT_CHAR
import day15.Day15.Companion.ROBOT_CHAR
import day15.Day15.Companion.UP_CHAR
import day15.Day15.Companion.WALL_CHAR
import day15.Direction.Companion.UP
import day15.Direction.Companion.DOWN
import day15.Direction.Companion.LEFT
import day15.Direction.Companion.RIGHT
import java.io.File

class Day15 : AdventOfCode {
    override val day = 15

    override fun partOne(input: SolutionInput): SolutionResult {
        val parts = input.lines.split { it.isBlank() }
        val grid = Grid(parts[0].map { it.map { it }.toTypedArray() }.toTypedArray())
        val moves = parts[1].map { it.map { it.toDirection() } }.flatten()

        var robotCoord = grid.entries().find { it.second == ROBOT_CHAR }!!.first

        moves.forEach { move ->
            if (move(grid, robotCoord, move)) {
                robotCoord = robotCoord.move(move)
            }
        }

        return grid.entries().filter { it.second == BOX_CHAR }.map { it.first.gps() }.sum().asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val parts = input.lines.split { it.isBlank() }
        val expanded = parts[0].map { it.wider().map { it }.toTypedArray() }.toTypedArray()
        val grid = Grid(expanded)
        val moves = parts[1].map { it.map { it.toDirection() } }.flatten()

        var robotCoord = grid.entries().find { it.second == ROBOT_CHAR }!!.first


        var cs = grid.entries().count { it.second == WALL_CHAR }
        moves.forEachIndexed { index, move ->
            if (index == 85) {
                val a = 1
            }
//            val filename = "grids/grid_$index.txt"
//            grid.saveToFile(filename)
            if (moveWider(grid, robotCoord, move)) {
                robotCoord = robotCoord.move(move)
            }
            if (grid.entries().count { it.second == WALL_CHAR } != cs) {
                println("Step $index")
//                println(grid)
                cs = grid.entries().count { it.second == WALL_CHAR }
            }
        }

        return grid.entries().filter { it.second == BOX_LEFT_CHAR }.map { it.first.gps() }.sum().asSolution()
    }

    companion object {
        val UP_CHAR = '^'
        val DOWN_CHAR = 'v'
        val LEFT_CHAR = '<'
        val RIGHT_CHAR = '>'
        val EMPTY_CHAR = '.'
        val ROBOT_CHAR = '@'
        val WALL_CHAR = '#'
        val BOX_CHAR = 'O'
        val BOX_LEFT_CHAR = '['
        val BOX_RIGHT_CHAR = ']'
    }
}

fun move(grid: Grid<Char>, currentCoord: GridCoord, direction: Direction): Boolean {
    val newCoord = currentCoord.move(direction)
    val targetObj = grid.at(newCoord)
    return when {
        targetObj == EMPTY_CHAR || (targetObj.isBox() && move(grid, newCoord, direction)) -> {
            grid.move(from = currentCoord, to = newCoord)
            true
        }

        else -> false
    }
}

fun moveWider(grid: Grid<Char>, currentCoord: GridCoord, direction: Direction): Boolean {
    if (!grid.contains(currentCoord)) return false
    val newCoord = currentCoord.move(direction)
    val targetObj = grid.at(newCoord)
    return when {
        targetObj == EMPTY_CHAR || (targetObj.isBox() && moveBox(grid, newCoord, direction)) -> {
            grid.move(from = currentCoord, to = newCoord)
            true
        }

        else -> false
    }
}

fun moveBox(grid: Grid<Char>, currentCoord: GridCoord, direction: Direction) =
    when {
        direction.isHorizontal -> moveBoxHorizontal(grid, currentCoord, direction)
        else -> moveBoxVertical(grid, grid.at(currentCoord).box(currentCoord), direction)
    }

fun moveBoxHorizontal(grid: Grid<Char>, currentCoord: GridCoord, direction: Direction): Boolean {
    val newCoord = currentCoord.move(direction).move(direction)
    val targetObj = grid.at(newCoord)
    return when {
        targetObj == EMPTY_CHAR || (targetObj.isBox() && moveBox(grid, newCoord, direction)) -> {
            grid.move(currentCoord.move(direction), newCoord)
            grid.move(currentCoord, currentCoord.move(direction))
            true
        }

        else -> false
    }
}

fun moveBoxVertical(
    grid: Grid<Char>,
    currentCoords: Pair<GridCoord, GridCoord>,
    direction: Direction,
): Boolean {
    val newCoord1 = currentCoords.first.move(direction)
    val newCoord2 = currentCoords.second.move(direction)
    val targetObj1 = grid.at(newCoord1)
    val targetObj2 = grid.at(newCoord2)
    if (targetObj1 == WALL_CHAR || targetObj2 == WALL_CHAR) return false
    return ((targetObj1 == EMPTY_CHAR && targetObj2 == EMPTY_CHAR) || ((targetObj1.isBox() || targetObj2.isBox()) && canMoveBoxVertical(
        grid,
        newCoord1 to newCoord2,
        direction
    ))).also { moved ->
        if (moved) {
            grid.move(currentCoords.first, currentCoords.first.move(direction))
            grid.move(currentCoords.second, currentCoords.second.move(direction))
        }
    }
}

fun canMoveBoxVertical(grid: Grid<Char>, currentCoords: Pair<GridCoord, GridCoord>, direction: Direction): Boolean {
    val targetObj1 = grid.at(currentCoords.first)
    val targetObj2 = grid.at(currentCoords.second)
    if (targetObj1 == EMPTY_CHAR && targetObj2 == EMPTY_CHAR) {
        grid.move(currentCoords.first, currentCoords.first.move(direction))
        grid.move(currentCoords.second, currentCoords.second.move(direction))
        return true
    }
    val coords = setOf(
        targetObj1.maybeBox(currentCoords.first),
        targetObj2.maybeBox(currentCoords.second)
    ).filterNotNull()
    val copyGrid = Grid(grid.cells.map { it.copyOf() }.toTypedArray())
    if (coords.size > 0 && coords.all { moveBoxVertical(grid, it, direction) }) {
        return true
    } else {
        grid.copyFrom(copyGrid)
        return false
    }
}

fun Char.toDirection() = when (this) {
    UP_CHAR -> UP
    DOWN_CHAR -> DOWN
    LEFT_CHAR -> LEFT
    RIGHT_CHAR -> RIGHT
    else -> throw IllegalArgumentException("Invalid direction: $this")
}

data class Direction(val vertical: Int, val horizontal: Int, val c: Char) {
    override fun toString() = c.toString()

    val isHorizontal get() = horizontal != 0

    companion object {
        val UP = Direction(-1, 0, UP_CHAR)
        val DOWN = Direction(+1, 0, DOWN_CHAR)
        val LEFT = Direction(0, -1, LEFT_CHAR)
        val RIGHT = Direction(0, +1, RIGHT_CHAR)
    }
}

data class GridCoord(val row: Int, val col: Int) {
    override fun toString() = "[$row,$col]"

    fun move(direction: Direction) = GridCoord(row + direction.vertical, col + direction.horizontal)

    fun gps() = row * 100 + col
}

class Grid<T>(val cells: Array<Array<T>>) {
    val maxRow = cells.size
    val maxCol = cells.first().size

    override fun toString(): String {
        val builder = StringBuilder()
        for (row in cells) {
            for (cell in row) {
                builder.append(cell.toString())
            }
            builder.append("\n")
        }
        return builder.toString().trimEnd() //
    }

    fun contains(p: GridCoord) = p.row in 0..<maxRow && p.col in 0..<maxCol

    fun copyFrom(other: Grid<T>) {
        for (row in 0 until maxRow) {
            for (col in 0 until maxCol) {
                cells[row][col] = other.cells[row][col]
            }
        }
    }

    private fun coords() = sequence {
        for (row in 0..<maxRow) {
            for (col in 0..<maxCol) {
                yield(GridCoord(row, col))
            }
        }
    }

    fun entries() = coords().map { it to at(it) }

    fun at(p: GridCoord) = cells[p.row][p.col]

    fun saveToFile(filename: String) {
        File(filename).bufferedWriter().use { writer ->
            cells.forEach { row ->
                writer.write(row.joinToString("") { it.toString() })
                writer.newLine()
            }
        }
    }
}

fun String.wider() =
    replace("$ROBOT_CHAR", "${ROBOT_CHAR}$ROBOT_CHAR")
        .replace("$WALL_CHAR", "$WALL_CHAR$WALL_CHAR")
        .replace("$BOX_CHAR", "$BOX_LEFT_CHAR$BOX_RIGHT_CHAR")
        .replace("$EMPTY_CHAR", "$EMPTY_CHAR$EMPTY_CHAR")
        .replace("${ROBOT_CHAR}$ROBOT_CHAR", "$ROBOT_CHAR$EMPTY_CHAR")

fun Grid<Char>.move(from: GridCoord, to: GridCoord) {
    val c = cells[from.row][from.col]
    cells[from.row][from.col] = EMPTY_CHAR
    cells[to.row][to.col] = c
}

fun Char.isBox() = this == BOX_CHAR || this == BOX_LEFT_CHAR || this == BOX_RIGHT_CHAR

fun Char.box(coord: GridCoord) = when (this) {
    BOX_LEFT_CHAR -> coord to coord.move(RIGHT)
    BOX_RIGHT_CHAR -> coord to coord.move(LEFT)
    else -> throw IllegalArgumentException("Not a box: $this")
}

fun Char.maybeBox(coord: GridCoord) = when (this) {
    BOX_LEFT_CHAR -> listOf(coord, coord.move(RIGHT)).sortedBy { it.col }.let { it[0] to it[1] }
    BOX_RIGHT_CHAR -> listOf(coord, coord.move(LEFT)).sortedBy { it.col }.let { it[0] to it[1] }
    else -> null
}
