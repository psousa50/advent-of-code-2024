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

class Day06 : AdventOfCode {
    override val day = 6

    override fun partOne(input: SolutionInput): SolutionResult {
        val map = Map.fromInput(input)
        val guard = input.lines.findAllObjects('^').first()

        val path = map.findPath(guard)

        val visited =
            path.zipWithNext { s1, s2 -> vector(s1.guard, s2.guard, s1.direction) }.map { it.coords().toList() }
                .flatten().toSet()

        return visited.count().asSolution()
    }


    override fun partTwo(input: SolutionInput): SolutionResult {
        val map = Map.fromInput(input)
        val guard = input.lines.findAllObjects('^').first()

        val path = map.findPath(guard)

        val visited =
            path.zipWithNext { s1, s2 -> vector(s1.guard, s2.guard, s1.direction) }.map { it.coords().toList() }
                .flatten().toSet()

        val newObstacles = mutableListOf<Coordinate>()
        visited.forEach { coord ->
            val newMap = map.addObstacle(coord)
            val newPath = newMap.findPath(guard).toList()
            if (newPath.last().inLoop) {
                newObstacles.add(coord)
            }
        }

        return newObstacles.count().asSolution()
    }
}

data class PathPoint(val coordinate: Coordinate, val direction: Direction) {
    override fun toString() = "${coordinate} ${direction}"
}

data class State(
    val guard: Coordinate,
    val direction: Direction,
    val visited: Set<PathPoint> = emptySet(),
    val inLoop: Boolean = false,
    val obstacleAhead: Coordinate? = null,
)

data class Map(
    val maxRow: Int,
    val maxCol: Int,
    val obstacles: List<Coordinate>,
) {
    fun findPath(guard: Coordinate): Sequence<State> {
        val state = State(guard = guard, direction = UP)
        return generateSequence(state) { nextState(it) }
    }

    private fun nextState(state: State) =
        if (atEdge(state.guard) or state.inLoop) null else next(state)

    fun atEdge(guard: Coordinate) =
        guard.row == 0 || guard.row == maxRow || guard.col == 0 || guard.col == maxCol

    private fun next(state: State): State {
        val obstacleAhead = when (state.direction) {
            UP -> obstacles.filter { it.row < state.guard.row && it.col == state.guard.col }.maxByOrNull { it.row }
            DOWN -> obstacles.filter { it.row > state.guard.row && it.col == state.guard.col }.minByOrNull { it.row }
            LEFT -> obstacles.filter { it.col < state.guard.col && it.row == state.guard.row }.maxByOrNull { it.col }
            RIGHT -> obstacles.filter { it.col > state.guard.col && it.row == state.guard.row }.minByOrNull { it.col }
        }
        val guard =
            when (state.direction) {
                UP -> Coordinate(obstacleAhead?.row?.plus(1) ?: 0, state.guard.col)
                DOWN -> Coordinate(obstacleAhead?.row?.plus(-1) ?: maxRow, state.guard.col)
                LEFT -> Coordinate(state.guard.row, obstacleAhead?.col?.plus(1) ?: 0)
                RIGHT -> Coordinate(state.guard.row, obstacleAhead?.col?.plus(-1) ?: maxCol)
            }
        val pathPoint = PathPoint(state.guard, state.direction)
        val inLoop = pathPoint in state.visited
        return State(
            guard = guard,
            direction = state.direction.rotate90Right(),
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
        paths: List<State>,
        guard: Coordinate,
        withColor: Boolean = true,
    ) =
        (0..maxRow).map { row ->
            ((0..maxCol).map { col ->
                val coordinate = Coordinate(row, col)
                when {
                    coordinate == guard -> color(ANSI_YELLOW, "^", withColor)
                    paths.contains(coordinate) -> color(ANSI_GREEN, "X", withColor)
                    obstacles.contains(coordinate) -> color(ANSI_RED, "#", withColor)
                    else -> color(ANSI_CYAN, ".", withColor)
                }
            } + "\n").joinToString("")
        }.joinToString("")


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
    zipWithNext { s1, s2 -> vector(s1.guard, s2.guard, s1.direction).contains(c) }.any { it }

data class Vector(val start: Coordinate, val end: Coordinate, val direction: Direction) {
    fun contains(c: Coordinate) = start.row <= c.row && start.col <= c.col && end.row >= c.row && end.col >= c.col

    fun coords() = generateSequence(start) { it.move(direction) }.takeWhile { it != end }.plus(end)
}

fun vector(s1: Coordinate, s2: Coordinate, direction: Direction) = Vector(s1, s2, direction)


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

