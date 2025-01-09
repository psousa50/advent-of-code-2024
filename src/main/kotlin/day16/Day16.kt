package day16

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution
import day16.Direction.Companion.EAST

class Day16 : AdventOfCode {
    override val day = 16

    override fun partOne(input: SolutionInput): SolutionResult {
        val grid = Grid(input.lines.map { it.map { it }.toTypedArray() }.toTypedArray())

        val (start, end) = findStartAndEnd(grid)
        val solution = findSolutions(grid, Heading(start, EAST), end).firstOrNull()

        val cost = solution?.actions()?.sumOf { it.cost() } ?: -1

        return cost.asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val grid = Grid(input.lines.map { it.map { it }.toTypedArray() }.toTypedArray())

        val (start, end) = findStartAndEnd(grid)
        val solutions = findSolutions(grid, Heading(start, EAST), end)
        val minCost = solutions.minOfOrNull { it.f() } ?: Int.MAX_VALUE
        val paths = solutions.filter { it.f() == minCost }.map { it.path() }
        val pointsMap = paths.map { it.map { it.position } }.flatten().toSet()

        return pointsMap.size.asSolution()
    }

    private fun findStartAndEnd(grid: Grid<Char>): Pair<GridCoord, GridCoord> {
        val start = grid.entries().find { it.second == START }!!.first
        val end = grid.entries().find { it.second == END }!!.first

        return Pair(start, end)
    }

    private fun findSolutions(grid: Grid<Char>, start: Heading, end: GridCoord) = sequence {
        val queue = mutableListOf<BaseState>(InitialState(start))
        val visited = mutableMapOf<Heading, Int>()

        while (queue.isNotEmpty()) {
            val state = queue.removeFirst()
            if (state.heading.position == end) {
                yield(state)
            } else {
                val successors = successors(state, grid, end).filter {
                    it.heading !in visited || it.g <= visited.getValue(it.heading)
                }
                queue.addAll(successors)
                successors.forEach { visited[it.heading] = it.g }
                queue.sortBy { it.f() }
            }
        }
    }

    private fun successors(state: BaseState, grid: Grid<Char>, end: GridCoord) =
        actions.map { action ->
            State(
                heading = state.heading.apply(action),
                g = state.g + action.cost(),
                h = state.heading.apply(action).position manhattan end,
                parent = state,
                action = action
            )
        }.filter { grid.at(it.heading.position) != WALL }


    companion object {
        const val WALL = '#'
        const val START = 'S'
        const val END = 'E'

        val actions = listOf(Action.MOVE_FORWARD, Action.TURN_LEFT, Action.TURN_RIGHT)
    }
}

enum class Action {
    MOVE_FORWARD,
    TURN_LEFT,
    TURN_RIGHT;

    fun cost() = when (this) {
        MOVE_FORWARD -> 1
        TURN_LEFT, TURN_RIGHT -> 1000
    }
}

sealed class BaseState(
    open val heading: Heading,
    open val g: Int,
    open val h: Int,
) {
    fun f() = g + h
    abstract fun actions(): List<Action>
    fun path() = actions().fold(listOf(heading)) { p, action ->
        p + p.last().apply(action)
    }
}

data class InitialState(
    override val heading: Heading,
    override val g: Int = 0,
    override val h: Int = 0,
) : BaseState(heading, g, h) {
    override fun actions() = emptyList<Action>()
}

data class State(
    override val heading: Heading,
    override val g: Int,
    override val h: Int,
    val parent: BaseState,
    val action: Action
) : BaseState(heading, g, h) {
    override fun actions() = when (parent) {
        is InitialState -> listOf(action)
        else -> parent.actions() + action
    }
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

    operator fun contains(p: GridCoord) = p.row in 0..<maxRow && p.col in 0..<maxCol

    private fun coords() = sequence {
        for (row in 0..<maxRow) {
            for (col in 0..<maxCol) {
                yield(GridCoord(row, col))
            }
        }
    }

    fun entries() = coords().map { it to at(it) }

    fun at(p: GridCoord) = if (p in this) cells[p.row][p.col] else null
}

data class GridCoord(val row: Int, val col: Int) {
    override fun toString() = "[$row,$col]"

    fun move(direction: Direction) = GridCoord(row + direction.vertical, col + direction.horizontal)
    infix fun manhattan(position: GridCoord) =
        Math.abs(row - position.row) + Math.abs(col - position.col)
}

data class Heading(val position: GridCoord, val direction: Direction) {
    override fun toString() = "[$position-$direction]"

    fun move() = copy(position = position.move(direction))
}

fun Heading.apply(action: Action): Heading {
    return when (action) {
        Action.MOVE_FORWARD -> move()
        Action.TURN_LEFT -> copy(direction = direction.left())
        Action.TURN_RIGHT -> copy(direction = direction.right())
    }
}

data class Direction(val vertical: Int, val horizontal: Int, val c: Char) {
    override fun toString() = c.toString()

    fun left() = directions[(directions.indexOf(this) - 1 + directions.size) % directions.size]
    fun right() = directions[(directions.indexOf(this) + 1) % directions.size]

    companion object {
        val NORTH = Direction(-1, 0, '^')
        val SOUTH = Direction(+1, 0, 'v')
        val WEST = Direction(0, -1, '<')
        val EAST = Direction(0, +1, '>')

        val directions = listOf(NORTH, EAST, SOUTH, WEST)
    }
}

