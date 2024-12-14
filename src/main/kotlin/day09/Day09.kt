package day09

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution

class Day09 : AdventOfCode {
    override val day = 9

    override fun partOne(input: SolutionInput): SolutionResult {
        val blocks = input.lines.first().map { it.digitToInt() }.mapIndexed { index, id ->
            if (index.isEven()) File((index / 2).toString(), id) else FreeSpace(id)
        }

        val state = State(blocks)
        val states = generateSequence(state) { it.next() }.takeWhile { it.containsFreeSpaces() }
        val lastState = states.last()

//        states.forEach {
//            val s = blocksToStr(it.blocks)
//            println("${s.take(50)} --- ${s.takeLast(50)}")
//        }

//        println(blocksToStr(blocks))
//        println(blocksToStr(lastState.blocks))

        return lastState.blocks.filterIsInstance<File>()
            .fold(0 to 0L) { (pos, sum), file ->
                pos + file.size to sum + file.id.toLong() * file.size * (pos + file.size + pos - 1) / 2
            }.second.asSolution()

    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        TODO("Not yet implemented")
    }
}

private fun Int.isEven() = this % 2 == 0


data class Blocks(val blocks: List<Block>) {
    override fun toString() = blocks.map { it.str.repeat(it.size) }.joinToString("")
}

interface Block {
    val size: Int
    val str: String
}

data class File(
    val id: String,
    override val size: Int
) : Block {
    override fun toString() = "F($id, $size)"
    override val str get() = id
}

data class FreeSpace(
    override val size: Int
) : Block {
    override fun toString() = "S($size)"
    override val str get() = "."
}

data class State(val blocks: List<Block>) {

    fun next(): State {
        val freeSpaceIndex = blocks.indexOfFirst { it is FreeSpace }

        val (newBlocks, removed) = removeFilesFromTail(blocks, blocks[freeSpaceIndex].size)
        return State(
            blocks = newBlocks.take(freeSpaceIndex) + removed + newBlocks.drop(freeSpaceIndex + 1),
        )
    }

    fun containsFreeSpaces() = blocks.any { it is FreeSpace }
}

fun removeFilesFromTail(
    blocks: List<Block>,
    size: Int,
    removed: List<Block> = emptyList()
): Pair<List<Block>, List<Block>> {
    val (newBlocks, newRemoved) = removeFilesFromHead(blocks.reversed(), size, removed)
    return newBlocks.reversed() to newRemoved
}

fun removeFilesFromHead(
    blocks: List<Block>,
    size: Int,
    removed: List<Block> = emptyList()
): Pair<List<Block>, List<Block>> {
    if (size == 0) return blocks to removed
    val first = blocks.firstOrNull { it is File } ?: return blocks to removed
    val file = first as File
    if (size < file.size)
        return listOf(File(file.id, file.size - size)) + blocks.drop(1) to removed + File(file.id, size)
    return removeFilesFromHead(blocks.drop(2), size - file.size, removed + file)
}
