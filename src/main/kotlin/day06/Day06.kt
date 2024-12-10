package day06

import ANSI_CYAN
import ANSI_GREEN
import ANSI_RED
import ANSI_RESET
import ANSI_YELLOW
import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution
import day06.Direction.*
import jdk.internal.org.jline.utils.Colors.s

class Day06 : AdventOfCode {
    override val day = 6

    override fun partOne(input: SolutionInput): SolutionResult {
        val map = Map.fromInput(input)
        val guard = input.lines.findAllObjects('^').first()

        val points = map.findPath(guard).last().visited.points()

//        println(map.toStr(map.findPath(guard).last().visited, guard))

        return points.count().asSolution()
    }


    override fun partTwo(input: SolutionInput): SolutionResult {
        val map = Map.fromInput(input)
        val guard = input.lines.findAllObjects('^').first()

        val points = map.findPath(guard).last().visited.points()

        val newObstacles = points.filter {
            map.addObstacle(it).findPath(guard).last().inLoop
        }

        return newObstacles.count().asSolution()
    }
}

data class PathPoint(val coordinate: Coordinate, val direction: Direction) {
    override fun toString() = "${coordinate} ${direction}"
}

fun Set<PathPoint>.points() =
    zipWithNext { s1, s2 -> Vector(s1.coordinate, s2.coordinate, s1.direction) }.map { it.coords().toList() }
        .flatten().toSet()

data class State(
    val guard: Coordinate,
    val direction: Direction,
    val inLoop: Boolean = false,
    val visited: Set<PathPoint> = emptySet(),
    val obstacleAhead: Coordinate? = null,
)

data class Map(
    val maxRow: Int,
    val maxCol: Int,
    val obstacles: List<Coordinate>,
) {
    fun findPath(guard: Coordinate): Sequence<State> {
        val state = State(guard = guard, direction = UP, visited = setOf(PathPoint(guard, UP)))
        return generateSequence(state) { nextState(it) }
    }

    private fun nextState(state: State) =
        if (atEdge(state.guard) or state.inLoop) null else next(state)

    private fun atEdge(guard: Coordinate) =
        guard.row == 0 || guard.row == maxRow || guard.col == 0 || guard.col == maxCol

    private fun next(state: State): State {
        val obstacleAhead = when (state.direction) {
            UP -> obstacles.filter { it.row < state.guard.row && it.col == state.guard.col }.maxByOrNull { it.row }
            DOWN -> obstacles.filter { it.row > state.guard.row && it.col == state.guard.col }.minByOrNull { it.row }
            LEFT -> obstacles.filter { it.col < state.guard.col && it.row == state.guard.row }.maxByOrNull { it.col }
            RIGHT -> obstacles.filter { it.col > state.guard.col && it.row == state.guard.row }.minByOrNull { it.col }
        }
        val newGuard =
            when (state.direction) {
                UP -> Coordinate(obstacleAhead?.row?.plus(1) ?: 0, state.guard.col)
                DOWN -> Coordinate(obstacleAhead?.row?.plus(-1) ?: maxRow, state.guard.col)
                LEFT -> Coordinate(state.guard.row, obstacleAhead?.col?.plus(1) ?: 0)
                RIGHT -> Coordinate(state.guard.row, obstacleAhead?.col?.plus(-1) ?: maxCol)
            }
        val newDirection = state.direction.rotate90Right()
        val pathPoint = PathPoint(newGuard, newDirection)
        val inLoop = pathPoint in state.visited
        return State(
            guard = newGuard,
            direction = newDirection,
            visited = state.visited + pathPoint,
            inLoop = inLoop,
            obstacleAhead = obstacleAhead
        )
    }

    fun addObstacle(obstacle: Coordinate) =
        Map(
            maxRow = maxRow,
            maxCol = maxCol,
            obstacles = obstacles + obstacle,
        )

    fun toStr(
        path: Set<PathPoint>,
        guard: Coordinate,
        withColor: Boolean = true,
    ): String {
        val points = path.points()
        return (0..maxRow).map { row ->
            ((0..maxCol).map { col ->
                val coordinate = Coordinate(row, col)
                when {
                    coordinate == guard -> color(ANSI_YELLOW, "^", withColor)
                    points.contains(coordinate) -> color(ANSI_GREEN, "X", withColor)
                    obstacles.contains(coordinate) -> color(ANSI_RED, "#", withColor)
                    else -> color(ANSI_CYAN, ".", withColor)
                }
            } + "\n").joinToString("")
        }.joinToString("")
    }


    private fun color(color: String, s: String, withColor: Boolean) =
        if (withColor) "$color$s$ANSI_RESET" else s

    companion object {
        fun fromInput(input: SolutionInput): Map {
            val obstacles = input.lines.findAllObjects('#')
            val maxRow = input.lines.size - 1
            val maxCol = input.lines.first().length - 1
            return Map(maxRow, maxCol, obstacles)
        }
    }
}

fun List<State>.contains(c: Coordinate) =
    zipWithNext { s1, s2 -> Vector(s1.guard, s2.guard, s1.direction).contains(c) }.any { it }

data class Vector(val start: Coordinate, val end: Coordinate, val direction: Direction) {
    fun contains(c: Coordinate) = start.row <= c.row && start.col <= c.col && end.row >= c.row && end.col >= c.col

    fun coords() = generateSequence(start) { it.move(direction) }.takeWhile { it != end }.plus(end)
}

data class Coordinate(val row: Int, val col: Int) {
    override fun toString() = "($row, $col)"

    fun move(direction: Direction) = Coordinate(row + direction.vertical, col + direction.horizontal)
}

enum class Direction(val vertical: Int, val horizontal: Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
}

fun Direction.rotate90Right() = when (this) {
    UP -> RIGHT
    RIGHT -> DOWN
    DOWN -> LEFT
    LEFT -> UP
}

fun List<String>.findAllObjects(toFind: Char) =
    mapIndexed { row, line ->
        line.mapIndexed { col, char ->
            if (char == toFind) Coordinate(row, col) else null
        }.filterNotNull()
    }.flatten()

