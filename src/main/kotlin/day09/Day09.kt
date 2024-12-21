package day09

import AdventOfCode
import SolutionInput
import SolutionResult
import asSolution

class Day09 : AdventOfCode {
    override val day = 9

    override fun partOne(input: SolutionInput): SolutionResult {
        val blocks = input.parse()

        return generateSequence(blocks) {
            it.movePartialFileFromTail()
        }.takeUntil {
            it.isContiguous()
        }.last()
            .checkSum()
            .asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val blocks = input.parse()

        return generateSequence(blocks) {
            it.moveWholeFileFromTail()
        }.takeWhile {
            it.containsUncheckedFiles()
        }.last()
            .checkSum()
            .asSolution()
    }
}

private fun SolutionInput.parse() =
    Blocks(lines.first().map { it.digitToInt() }.mapIndexed { index, id ->
        if (index.isEven()) File((index / 2).toString(), id) else FreeSpace(id)
    })

private fun Int.isEven() = this % 2 == 0

data class Blocks(val blocks: List<Block>) : List<Block> by blocks {
    override fun toString() = blocks.joinToString("") { it.str.repeat(it.size) }

    fun movePartialFileFromTail(): Blocks {
        val lastFile = lastFile()
        val freeSpace = firstFreeSpace { it is FreeSpace }
        return when {
            !lastFile.exists || !freeSpace.exists || lastFile.index < freeSpace.index -> Blocks(blocks = blocks)

            freeSpace.size > lastFile.size -> replaceAt(
                freeSpace.index,
                listOf(
                    lastFile.block,
                    FreeSpace(freeSpace.size - lastFile.size)
                )
            ).replaceAt(
                lastFile.index + 1,
                FreeSpace(lastFile.size)
            )

            freeSpace.size < lastFile.size -> replaceAt(
                freeSpace.index,
                lastFile.file.copy(size = freeSpace.size)
            ).replaceAt(
                lastFile.index,
                listOf(
                    lastFile.file.copy(size = lastFile.size - freeSpace.size),
                    FreeSpace(freeSpace.size)
                )
            )

            else -> swap(freeSpace, lastFile)
        }
    }

    fun moveWholeFileFromTail(): Blocks {
        val lastFile = lastFile { !it.checked }
        val freeSpaceThatFitsFile =
            if (lastFile.exists)
                firstFreeSpace { it.size >= lastFile.size }
            else
                IndexedFreeSpace(-1, FreeSpace(0))
        return when {
            !lastFile.exists -> Blocks(blocks = blocks)
            !freeSpaceThatFitsFile.exists || freeSpaceThatFitsFile.index > lastFile.index -> replaceAt(
                lastFile.index,
                lastFile.file.copy(checked = true)
            )

            freeSpaceThatFitsFile.size == lastFile.size -> swap(
                freeSpaceThatFitsFile,
                lastFile
            )

            freeSpaceThatFitsFile.size > lastFile.size ->
                replaceAt(
                    freeSpaceThatFitsFile.index,
                    listOf(
                        lastFile.block,
                        FreeSpace(freeSpaceThatFitsFile.size - lastFile.size)
                    )
                ).replaceAt(
                    lastFile.index + 1,
                    FreeSpace(lastFile.size)
                )

            else -> swap(
                freeSpaceThatFitsFile,
                lastFile
            )

        }
    }

    private fun firstFreeSpace(predicate: (Block) -> Boolean = { true }) =
        indexOfFirst { predicate(it) && it is FreeSpace }.let { index ->
            if (index < 0) IndexedFreeSpace.empty else IndexedFreeSpace(index, blocks[index])
        }

    private fun lastFile(predicate: (File) -> Boolean = { true }) =
        indexOfLast { it is File && predicate(it) }.let { index ->
            if (index < 0) IndexedFile.empty else IndexedFile(index, blocks[index] as File)
        }

    private fun replaceAt(index: Int, blockToReplace: Block) = replaceAt(index, listOf(blockToReplace))

    private fun replaceAt(index: Int, blocksToReplace: List<Block>): Blocks {
        return Blocks(
            blocks = blocks.toMutableList()
                .apply { removeAt(index) }
                .apply { addAll(index, blocksToReplace) })
    }

    private fun swap(block1: IndexedBlock, block2: IndexedBlock) =
        Blocks(
            blocks = blocks.toMutableList()
                .apply { removeAt(block1.index) }
                .apply { add(block1.index, block2.block) }
                .apply { removeAt(block2.index) }
                .apply { add(block2.index, block1.block) }
        )


    fun isContiguous() = lastFile().index < firstFreeSpace().index
    fun containsUncheckedFiles() = blocks.any { it is File && !it.checked }

    fun checkSum() = blocks
        .fold(0 to 0L) { (pos, sum), file ->
            pos + file.size to sum + file.value * file.size * (pos + file.size + pos - 1) / 2
        }.second
}

interface Block {
    val size: Int
    val str: String
    val value: Long
}

data class File(
    val id: String,
    override val size: Int,
    val checked: Boolean = false
) : Block {
    override fun toString() = "F($id, $size)"
    override val str get() = id
    override val value get() = id.toLong()
}

data class FreeSpace(
    override val size: Int
) : Block {
    override fun toString() = "S($size)"
    override val str get() = "."
    override val value get() = 0L
}

interface IndexedBlock {
    val index: Int
    val block: Block
    val exists get() = index >= 0
    val size get() = block.size
}

data class IndexedFreeSpace(override val index: Int, override val block: Block) : IndexedBlock {
    companion object {
        val empty = IndexedFreeSpace(-1, FreeSpace(0))
    }
}

data class IndexedFile(override val index: Int, val file: File) : IndexedBlock {
    override val block = file

    companion object {
        val empty = IndexedFile(-1, File("", 0))
    }
}

fun <T> Sequence<T>.takeUntil(predicate: (T) -> Boolean): Sequence<T> = sequence {
    for (element in this@takeUntil) {
        yield(element)
        if (predicate(element)) break
    }
}