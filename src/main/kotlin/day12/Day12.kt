package day12

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution
import day12.GridCoord.Companion.DOWN
import day12.GridCoord.Companion.LEFT
import day12.GridCoord.Companion.RIGHT
import day12.GridCoord.Companion.UP

class Day12 : AdventOfCode {
    override val day = 12

    override fun partOne(input: SolutionInput): SolutionResult {
        val plantsGrid = input.parse()

        val areasGrid = findAreas(plantsGrid)

        val areasMap = areasGrid.entries()
            .groupBy { it.second }
            .mapValues { it.value.map { it.first }.toSet() }

        val perimeterMap = areasGrid.coords().map { point ->
            areasGrid.at(point).let { area ->
                area to 4 - areasGrid.neighbours(point).count { areasGrid.at(it).plant == area.plant }
            }
        }.groupBy { it.first }.mapValues { (_, values) -> values.sumOf { it.second } }

        return areasMap.map { it.value.size * perimeterMap.getValue(it.key) }.sum().asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val plantsGrid = input.parse()

        val areasGrid = findAreas(plantsGrid)

        val areasMap =
            areasGrid.entries().groupBy { it.second }.mapValues { it.value.map { it.first }.toSet() }

        val areas = areasGrid.entries().map { it.second }.toSet()
        val sidesMap = areas.associateWith { countSides(areasGrid, it) }

        println(sidesMap)

        return areasMap.map { it.value.size * sidesMap.getValue(it.key) }.sum().asSolution()
    }
}

fun findAreas(plants: Grid<Char>): Grid<Area> {
    val areas = Grid(plants.cells.map { it.map { Area(it, 0) }.toTypedArray() }.toTypedArray())
    var areaIndex = 1
    for (row in 0..<plants.maxRow) {
        for (col in 0..<plants.maxCol) {
            if (areas.cells[row][col].index == 0) {
                assignArea(plants, areas, GridCoord(row, col), areaIndex)
                areaIndex++
            }
        }
    }
    return areas
}

private fun assignArea(plants: Grid<Char>, areasMap: Grid<Area>, gridCoord: GridCoord, areaIndex: Int) {
    val area = areasMap.at(gridCoord)
    if (area.index == 0) {
        areasMap.cells[gridCoord.row][gridCoord.col] = area.assignArea(areaIndex)
        val neighbours = plants.neighbours(gridCoord).filter { plants.at(it) == plants.at(gridCoord) }
        neighbours.forEach { assignArea(plants, areasMap, it, areaIndex) }
    }
}

fun countSides(areasGrid: Grid<Area>, area: Area): Int {
    val horizontalSides = (0..<areasGrid.maxRow).sumOf { row ->
        countSides(areasGrid, area, areasGrid.maxCol, { c -> GridCoord(row, c) }, UP) +
            countSides(areasGrid, area, areasGrid.maxCol, { c -> GridCoord(row, c) }, DOWN)
    }
    val verticalSides = (0..<areasGrid.maxCol).sumOf { col ->
        countSides(areasGrid, area, areasGrid.maxRow, { r -> GridCoord(r, col) }, LEFT) +
            countSides(areasGrid, area, areasGrid.maxRow, { r -> GridCoord(r, col) }, RIGHT)
    }
    return horizontalSides + verticalSides
}

private fun countSides(
    areasGrid: Grid<Area>,
    area: Area,
    maxDistance: Int,
    current: (p: Int) -> GridCoord,
    direction: GridCoord
) = (0..<maxDistance).fold(Pair(0, false)) { (sides, inStream), i ->
    val here = current(i)
    val aNeighbour = here.move(direction)
    val isSide = areasGrid.at(here) == area &&
        (!areasGrid.contains(aNeighbour) || areasGrid.at(here) != areasGrid.at(aNeighbour))
    Pair(if (isSide && !inStream) sides + 1 else sides, isSide)
}.first

fun SolutionInput.parse() = Grid(lines.map { it.map { it }.toTypedArray() }.toTypedArray())

data class GridCoord(val row: Int, val col: Int) {
    override fun toString() = "[$row,$col]"

    fun move(direction: GridCoord) = GridCoord(row + direction.row, col + direction.col)

    companion object {
        val UP = GridCoord(-1, 0)
        val DOWN = GridCoord(+1, 0)
        val LEFT = GridCoord(0, -1)
        val RIGHT = GridCoord(0, +1)
    }
}

data class Area(val plant: Char, val index: Int) {
    override fun toString() = "$plant$index"

    fun assignArea(newArea: Int) = copy(index = newArea)
}

class Grid<T>(val cells: Array<Array<T>>) {
    val maxRow = cells.size
    val maxCol = cells.first().size

    override fun toString() = cells.joinToString("\n") { it.joinToString(" ") { it.toString() } }

    fun contains(p: GridCoord) = p.row in 0..<maxRow && p.col in 0..<maxCol

    fun coords() = sequence {
        for (row in 0..<maxRow) {
            for (col in 0..<maxCol) {
                yield(GridCoord(row, col))
            }
        }
    }

    fun entries() = coords().map { it to at(it) }

    fun at(p: GridCoord) = cells[p.row][p.col]

    fun neighbours(p: GridCoord) = setOf(
        p.move(LEFT),
        p.move(RIGHT),
        p.move(UP),
        p.move(DOWN)
    ).filter { contains(it) }.toSet()
}
