package day12

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution

class Day12 : AdventOfCode {
    override val day = 12

    override fun partOne(input: SolutionInput): SolutionResult {
        val farm = input.parse()

        val areas = farm.assignAreas()

        val areasMap =
            areas.entries().groupBy { it.second }.mapValues { it.value.map { it.first }.toSet() }

        areasMap.forEach { (area, points) ->
            println("$area: ${points}")
        }

        val perimeterMap = areas.coords().map { point ->
            areas.at(point)
                .let { area -> area to 4 - areas.insideNeighbours(point).count { areas.at(it).plant == area.plant } }
        }.groupBy { it.first }.mapValues { (_, values) -> values.sumOf { it.second } }

        return areasMap.map { it.value.size * perimeterMap.getValue(it.key) }.sum().asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val farm = input.parse()

        val areasGrid = farm.assignAreas()

        val areasMap =
            areasGrid.entries().groupBy { it.second }.mapValues { it.value.map { it.first }.toSet() }

        val areasFarm = AreasFarm(areasGrid)

        val areas = areasFarm.entries().map { it.second }.toSet()
        val sidesMap = areas.associateWith { areasFarm.countSides(it) }

        return areasMap.map { it.value.size * sidesMap.getValue(it.key) }.sum().asSolution()

    }
}

data class GridCoord(val row: Int, val col: Int) {
    override fun toString() = "[$row,$col]"
}

data class Area(val plant: Char, val index: Int) {
    override fun toString() = "$plant$index"

    fun assignArea(newArea: Int) = this.copy(index = newArea)
}

data class Farm(val grid: Grid<Char>) : Grid<Char> by grid {
    fun assignAreas(): Grid<Area> {
        val areas = GridMap(grid.values.map { it.map { Area(it, 0) }.toTypedArray() }.toTypedArray())
        var areaIndex = 1
        coords().forEach { point ->
            if (areas.at(point).index == 0) {
                assignArea(areas, point, areaIndex)
                areaIndex++
            }
        }
        return areas
    }

    private fun assignArea(areasMap: GridMap<Area>, gridCoord: GridCoord, areaIndex: Int) {
        val area = areasMap.at(gridCoord)
        if (area.index == 0) {
            areasMap.values[gridCoord.row][gridCoord.col] = area.assignArea(areaIndex)
            val neighbours = insideNeighbours(gridCoord).filter { at(it) == at(gridCoord) }
            neighbours.forEach { assignArea(areasMap, it, areaIndex) }
        }
    }
}

data class AreasFarm(val grid: Grid<Area>) : Grid<Area> by grid {

    fun countSides(area: Area) =
        (0..<maxRow).map { countSidesHorizontal(it, area) }.sum().also { println("$area = H: $it") } +
            (0..<maxCol).map { countSidesVertical(it, area) }.sum().also { println("$area = V: $it") }

    private fun countSidesHorizontal(row: Int, area: Area) =
        countSidesHorizontal(row, area, -1) + countSidesHorizontal(row, area, +1)

    private fun countSidesHorizontal(row: Int, area: Area, dir: Int): Int {
        var sides = 0
        var inStream = false
        for (c in 0..<maxCol) {
            val p = GridCoord(row, c)
            val nearP = GridCoord(row + dir, c)
            val isSide = at(p) == area && (!isInside(nearP) || at(p) != at(nearP))
            if (isSide) {
                if (!inStream) {
                    sides += 1
                }
                inStream = true
            } else {
                inStream = false
            }
        }
        return sides
    }

    private fun countSidesVertical(col: Int, area: Area) =
        countSidesVertical(col, area, -1) + countSidesVertical(col, area, +1)

    private fun countSidesVertical(col: Int, area: Area, dir: Int): Int {
        var sides = 0
        var inStream = false
        for (r in 0..<maxRow) {
            val p = GridCoord(r, col)
            val nearP = GridCoord(r, col+ dir)
            val isSide = at(p) == area && (!isInside(nearP) || at(p) != at(nearP))
            if (isSide) {
                if (!inStream) {
                    sides += 1
                }
                inStream = true
            } else {
                inStream = false
            }
        }
        return sides
    }
}

fun SolutionInput.parse() = Farm(GridMap(lines.map { it.map { it }.toTypedArray() }.toTypedArray()))

interface Grid<T> {
    val maxRow: Int
    val maxCol: Int
    fun isInside(p: GridCoord): Boolean
    fun coords(): Sequence<GridCoord>
    fun entries(): Sequence<Pair<GridCoord, T>>
    fun at(p: GridCoord): T
    fun at(row: Int, col: Int): T
    fun verticalNeighbours(p: GridCoord): Set<GridCoord>
    fun horizontalNeighbours(p: GridCoord): Set<GridCoord>
    fun neighbours(p: GridCoord): Set<GridCoord>
    fun insideNeighbours(p: GridCoord): Set<GridCoord>
    val values: Array<Array<T>>
}

class GridMap<T>(override val values: Array<Array<T>>) : Grid<T> {
    override val maxRow = values.size
    override val maxCol = values.first().size

    override fun toString() = values.joinToString("\n") { it.joinToString(" ") { it.toString() } }

    override fun isInside(p: GridCoord) = p.row in 0..<maxRow && p.col in 0..<maxCol

    override fun coords() = sequence {
        for (row in 0..<maxRow) {
            for (col in 0..<maxCol) {
                yield(GridCoord(row, col))
            }
        }
    }

    override fun entries() = coords().map { it to at(it) }

    override fun at(p: GridCoord) = values[p.row][p.col]

    override fun at(row: Int, col: Int) = at(GridCoord(row, col))

    override fun verticalNeighbours(p: GridCoord) = setOf(
        p.copy(row = p.row - 1),
        p.copy(row = p.row + 1)
    )

    override fun horizontalNeighbours(p: GridCoord) = setOf(
        p.copy(col = p.col - 1),
        p.copy(col = p.col + 1)
    )

    override fun neighbours(p: GridCoord) = setOf(
        p.copy(row = p.row - 1, col = p.col),
        p.copy(row = p.row + 1, col = p.col),
        p.copy(row = p.row, col = p.col - 1),
        p.copy(row = p.row, col = p.col + 1)
    )

    override fun insideNeighbours(p: GridCoord) = neighbours(p).filter { isInside(it) }.toSet()
}

