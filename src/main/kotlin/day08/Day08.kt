package day08

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution
import jdk.internal.org.jline.utils.Colors.s

class Day08 : AdventOfCode {
    override val day = 8

    override fun partOne(input: SolutionInput): SolutionResult {
        val map = Map(input.lines)

        val antennasMap = map.antennas.groupBy { it.id }

        val antennasPairs = antennasMap.mapValues { (_, antennas) ->
            antennas.flatMapIndexed { index, antenna1 ->
                antennas.drop(index + 1).map { antenna2 -> antenna1 to antenna2 }
            }
        }

        val antiNodesMap = antennasPairs.mapValues { (key, antennas) ->
            antennas.map { (antenna1, antenna2) ->
                listOf(map.antiNode(antenna1, antenna2), map.antiNode(antenna2, antenna1))
            }.flatten()
        }

        val antiNodes = antiNodesMap.values.flatten().filter { map.contains(it) }.toSet()

        val finalMap = FinalMap(map.maxRow, map.maxCol, map.antennas, antiNodes)
        println(finalMap)

        return antiNodes.size.asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val map = Map(input.lines)

        val antennasMap = map.antennas.groupBy { it.id }

        val antennasPairs = antennasMap.mapValues { (_, antennas) ->
            antennas.flatMapIndexed { index, antenna1 ->
                antennas.drop(index + 1).map { antenna2 -> antenna1 to antenna2 }
            }
        }

        val antiNodesMap = antennasPairs.mapValues { (key, antennas) ->
            antennas.map { (antenna1, antenna2) ->
                map.antiNodes(antenna2, antenna1)
            }.flatten()
        }

        val antiNodes = antiNodesMap.values.flatten().filter { map.contains(it) }.toSet()

        val finalMap = FinalMap(map.maxRow, map.maxCol, map.antennas, antiNodes)
        println(finalMap)

        return antiNodes.size.asSolution()
    }

}

class Map(lines: List<String>) {

    val maxRow = lines.size
    val maxCol = lines.first().length

    val antennas = lines.mapIndexed { row, _ ->
        lines[row].mapIndexedNotNull() { col, char ->
            if (char == '.') null else Antenna(Coords(row, col), char)
        }
    }.flatten()

    fun antiNode(antenna1: Antenna, antenna2: Antenna): Coords {
        val diffVector =
            Coords(antenna1.coords.row - antenna2.coords.row, antenna1.coords.col - antenna2.coords.col)
        return Coords(antenna1.coords.row + diffVector.row, antenna1.coords.col + diffVector.col)
    }

    fun antiNodes(antenna1: Antenna, antenna2: Antenna): List<Coords> {
        val diffVector =
            Coords(antenna1.coords.row - antenna2.coords.row, antenna1.coords.col - antenna2.coords.col)
        val s1 = generateSequence(antenna1.coords) {
            Coords(
                it.row + diffVector.row,
                it.col + diffVector.col
            )
        }.takeWhile { contains(it) }.toList()
        val s2 = generateSequence(antenna2.coords) {
            Coords(
                it.row - diffVector.row,
                it.col - diffVector.col
            )
        }.takeWhile { contains(it) }.toList()
        return s1 + s2
    }

    fun contains(coords: Coords) =
        coords.row in 0..<maxRow && coords.col in 0..<maxCol
}

data class FinalMap(
    val maxRow: Int,
    val maxCol: Int,
    val antennas: List<Antenna>,
    val antiNodes: Set<Coords>
) {
    override fun toString() =
        (0..<maxRow).map { row ->
            (0..<maxCol).map { col ->
                val coords = Coords(row, col)
                val antenna = antennas.find { it.coords == coords }
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
}