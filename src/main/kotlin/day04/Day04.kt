package day04

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution
import day04.Direction.Companion.DOWN_LEFT
import day04.Direction.Companion.DOWN_RIGHT
import day04.Direction.Companion.UP_LEFT
import day04.Direction.Companion.UP_RIGHT

class Day04 : AdventOfCode {
    override val day = 4

    val XMAS = "XMAS"
    val MAS = "MAS"

    override fun partOne(input: SolutionInput): SolutionResult {
        val board = LettersBoard(input.lines)
        val words = board.coords().map { coord ->
            Direction.ALL.map { direction ->
                hasWord(XMAS, board, coord, direction)
            }
        }.flatten()

        return words.count { it }.asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val board = LettersBoard(input.lines)
        val words = board.coords().map { coord ->
            (hasWord(MAS, board, coord.move(UP_LEFT), DOWN_RIGHT) ||
                hasWord(MAS, board, coord.move(DOWN_RIGHT), UP_LEFT)) &&
                (hasWord(MAS, board, coord.move(DOWN_LEFT), UP_RIGHT) ||
                    hasWord(MAS, board, coord.move(UP_RIGHT), DOWN_LEFT))
        }

        return words.count { it }.asSolution()
    }

    private fun findWord(word: String, board: LettersBoard, coord: Coord, direction: Direction): Sequence<Char> =
        word.asSequence().filterIndexed() { i, char ->
            val newCoord = coord.move(direction, step = i)
            board.isValid(newCoord) && board.at(newCoord.row, newCoord.col) == char
        }

    private fun hasWord(word: String, board: LettersBoard, coord: Coord, direction: Direction) =
        findWord(word, board, coord, direction).toList().size == word.length
}

data class Coord(val row: Int, val col: Int) {
    fun move(direction: Direction, step: Int = 1) =
        Coord(row + direction.vertical * step, col + direction.horizontal * step)
}

data class Direction(val vertical: Int, val horizontal: Int) {
    companion object {
        val UP = Direction(-1, 0)
        val DOWN = Direction(1, 0)
        val LEFT = Direction(0, -1)
        val RIGHT = Direction(0, 1)
        val UP_LEFT = Direction(-1, -1)
        val UP_RIGHT = Direction(-1, 1)
        val DOWN_LEFT = Direction(1, -1)
        val DOWN_RIGHT = Direction(1, 1)

        val ALL = listOf(
            UP,
            DOWN,
            LEFT,
            RIGHT,
            UP_LEFT,
            UP_RIGHT,
            DOWN_LEFT,
            DOWN_RIGHT
        )
    }
}

data class LettersBoard(val letters: List<String>) {

    fun at(row: Int, col: Int) = letters[row][col]

    fun coords() = sequence {
        letters.indices.forEach { row ->
            letters[row].indices.forEach { col ->
                yield(Coord(row, col))
            }
        }
    }

    fun isValid(coord: Coord) = coord.row in 0..letters.lastIndex && coord.col in 0..letters.lastIndex
}