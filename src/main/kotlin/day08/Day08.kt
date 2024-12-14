package day08

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution

class Day08 : AdventOfCode {
    override val day = 8

    override fun partOne(input: SolutionInput): SolutionResult {
        val antennasMap = AntennasMap(input.lines)

        val antiNodesMap = AntennasMap(input.lines).let {
            antennasMap.antennasPairs().mapValues { (key, antennas) ->
                antennas.map { (antenna1, antenna2) ->
                    antennasMap.nodesAtSameDistance(antenna1, antenna2)
                }.flatten()
            }
        }

        return antiNodesMap.insideMap(antennasMap).size.asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val antennasMap = AntennasMap(input.lines)

        val antiNodesMap = antennasMap.antennasPairs().mapValues { (key, antennas) ->
            antennas.map { (antenna1, antenna2) ->
                antennasMap.nodesOnSameLine(antenna2, antenna1)
            }.flatten()
        }

        return antiNodesMap.insideMap(antennasMap).size.asSolution()
    }

}

class AntennasMap(lines: List<String>) {

    val maxRow = lines.size
    val maxCol = lines.first().length

    val antennas = List(lines.size) { row ->
        lines[row].mapIndexedNotNull() { col, char ->
            if (char == '.') null else Antenna(Coords(row, col), char)
        }
    }.flatten()

    fun antennasPairs() = antennas.groupBy { it.id }.mapValues { (_, antennas) ->
        antennas.flatMapIndexed { index, antenna1 ->
            antennas.drop(index + 1).map { antenna2 -> antenna1 to antenna2 }
        }

    }

    private fun nodeAtSameDistance(antenna1: Antenna, antenna2: Antenna) =
        antenna1.coords + antenna1.coords - antenna2.coords


    fun nodesAtSameDistance(antenna1: Antenna, antenna2: Antenna) =
        listOf(nodeAtSameDistance(antenna1, antenna2), nodeAtSameDistance(antenna2, antenna1))


    fun nodesOnSameLine(antenna1: Antenna, antenna2: Antenna): List<Coords> {
        val diffVector = antenna1.coords - antenna2.coords
        val s1 = generateSequence(antenna1.coords) { it + diffVector }
            .takeWhile { isInsideMap(it) }
        val s2 = generateSequence(antenna2.coords) { it - diffVector }
            .takeWhile { isInsideMap(it) }
        return (s1 + s2).toList()
    }

    fun isInsideMap(coords: Coords) =
        coords.row in 0..<maxRow && coords.col in 0..<maxCol
}

data class AntiNodesMapDisplay(
    val antennasMap: AntennasMap,
    val antiNodes: Set<Coords>
) {
    override fun toString() =
        (0..<antennasMap.maxRow).map { row ->
            (0..<antennasMap.maxCol).map { col ->
                val coords = Coords(row, col)
                val antenna = antennasMap.antennas.find { it.coords == coords }
                val antiNodes = antiNodes.find { it == coords }
                when {
                    antiNodes != null -> "#"
                    antenna != null -> antenna.id
                    else -> "."
                }
            }.joinToString("") + "\n"
        }.joinToString("")
}

data class Antenna(val coords: Coords, val id: Char) {
    override fun toString() = "${id}->${coords}"
}

data class Coords(val row: Int, val col: Int) {
    override fun toString() = "($row, $col)"

    operator fun plus(other: Coords) = Coords(row + other.row, col + other.col)
    operator fun minus(other: Coords) = Coords(row - other.row, col - other.col)
}

fun Map<Char, List<Coords>>.insideMap(antennasMap: AntennasMap) =
    values.flatten().filter { antennasMap.isInsideMap(it) }.toSet()