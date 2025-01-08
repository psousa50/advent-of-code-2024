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

typealias MoveBoxFunction = (grid: Grid<Char>, currentCoord: GridCoord, direction: Direction) -> Boolean

class Day15 : AdventOfCode {
    override val day = 15

    override fun partOne(input: SolutionInput): SolutionResult {
        val parts = input.lines.split { it.isBlank() }
        val grid = Grid(parts[0].map { it.map { it }.toTypedArray() }.toTypedArray())
        val moves = parts[1].parseMoves()

        applyMoves(grid, moves, ::moveSmallBox)

        return grid.entries().filter { it.second.isBox() }.map { it.first.gps() }.sum().asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val parts = input.lines.split { it.isBlank() }
        val expanded = parts[0].map { it.wider().map { it }.toTypedArray() }.toTypedArray()
        val grid = Grid(expanded)
        val moves = parts[1].parseMoves()

        applyMoves(grid, moves, ::moveWiderBox)

        return grid.entries().filter { it.second.isLeftBox() }.map { it.first.gps() }.sum().asSolution()
    }

    private fun applyMoves(grid: Grid<Char>, moves: List<Direction>, moveBox: MoveBoxFunction) {
        val robotCoord = grid.entries().find { it.second.isRobot() }!!.first

        moves.fold(robotCoord) { coord, direction ->
            if (move(grid, coord, direction, moveBox)) {
                coord.move(direction)
            } else {
                coord
            }
        }
    }

    companion object {
        const val UP_CHAR = '^'
        const val DOWN_CHAR = 'v'
        const val LEFT_CHAR = '<'
        const val RIGHT_CHAR = '>'
        const val EMPTY_CHAR = '.'
        const val ROBOT_CHAR = '@'
        const val WALL_CHAR = '#'
        const val BOX_CHAR = 'O'
        const val BOX_LEFT_CHAR = '['
        const val BOX_RIGHT_CHAR = ']'
    }
}

private fun move(
    grid: Grid<Char>,
    currentCoord: GridCoord,
    direction: Direction,
    moveBoxFunction: MoveBoxFunction
): Boolean {
    val newCoord = currentCoord.move(direction)
    val targetObj = grid.at(newCoord)
    return !targetObj.isWall() &&
        (targetObj.isEmpty() || moveBoxFunction(grid, newCoord, direction))
            .also { moved ->
                if (moved) {
                    grid.move(from = currentCoord, to = newCoord)
                }
            }
}

private fun moveSmallBox(grid: Grid<Char>, currentCoord: GridCoord, direction: Direction): Boolean =
    move(grid, currentCoord, direction, ::moveSmallBox)

private fun moveWiderBox(grid: Grid<Char>, currentCoord: GridCoord, direction: Direction) =
    if (direction.isHorizontal) {
        moveBoxHorizontal(grid, currentCoord, direction)
    } else {
        moveBoxVertical(grid, grid.at(currentCoord).box(currentCoord), direction)
    }

private fun moveBoxHorizontal(grid: Grid<Char>, currentCoord: GridCoord, direction: Direction): Boolean {
    val newCoord = currentCoord.move(direction).move(direction)
    val targetObj = grid.at(newCoord)
    return !targetObj.isWall() &&
        (targetObj.isEmpty() || moveWiderBox(grid, newCoord, direction)).also { moved ->
            if (moved) {
                grid.move(currentCoord.move(direction), newCoord)
                grid.move(currentCoord, currentCoord.move(direction))
            }
        }
}

private fun moveBoxVertical(
    grid: Grid<Char>,
    currentCoords: Pair<GridCoord, GridCoord>,
    direction: Direction,
): Boolean {
    val newCoords = currentCoords.move(direction)
    val targetObj = grid.at(newCoords)
    return (!targetObj.isWall() &&
        (targetObj.isEmpty() || canMoveBoxVertical(grid, newCoords, direction))
            .also { moved ->
                if (moved) {
                    grid.move(currentCoords.first, currentCoords.first.move(direction))
                    grid.move(currentCoords.second, currentCoords.second.move(direction))
                }
            }
        )
}

private fun canMoveBoxVertical(
    grid: Grid<Char>,
    currentCoords: Pair<GridCoord, GridCoord>,
    direction: Direction
): Boolean {
    val targetObj = grid.at(currentCoords)
    return targetObj.isEmpty() || canMoveBoxVertical(grid, currentCoords, direction, targetObj)
}

private fun canMoveBoxVertical(
    grid: Grid<Char>,
    currentCoords: Pair<GridCoord, GridCoord>,
    direction: Direction,
    targetObj: Pair<Char, Char>,
): Boolean {
    val coords = setOf(
        targetObj.first.maybeBox(currentCoords.first),
        targetObj.second.maybeBox(currentCoords.second)
    ).filterNotNull()
    val copyGrid = Grid(grid.cells.map { it.copyOf() }.toTypedArray())
    return targetObj.isEmpty() || (coords.isNotEmpty() && coords.all {
        moveBoxVertical(grid, it, direction)
    }).also { moved ->
        if (!moved) {
            grid.copyFrom(copyGrid)
        }
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
    fun at(ps: Pair<GridCoord, GridCoord>) = at(ps.first) to at(ps.second)

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
fun Char.isLeftBox() = this == BOX_LEFT_CHAR
fun Char.isWall() = this == WALL_CHAR
fun Char.isEmpty() = this == EMPTY_CHAR
fun Char.isRobot() = this == ROBOT_CHAR

fun Char.maybeBox(coord: GridCoord) = when (this) {
    BOX_LEFT_CHAR -> listOf(coord, coord.move(RIGHT)).sortedBy { it.col }.let { it[0] to it[1] }
    BOX_RIGHT_CHAR -> listOf(coord, coord.move(LEFT)).sortedBy { it.col }.let { it[0] to it[1] }
    else -> null
}

fun Char.box(coord: GridCoord) = maybeBox(coord) ?: throw IllegalArgumentException("Not a box: $this")

fun Pair<GridCoord, GridCoord>.move(direction: Direction) = first.move(direction) to second.move(direction)
fun Pair<Char, Char>.isEmpty() = first.isEmpty() && second.isEmpty()
fun Pair<Char, Char>.isWall() = first.isWall() || second.isWall()

private fun List<String>.parseMoves() =
    map { it.map { it.toDirection() } }.flatten()

