import Parsers.resourceExists
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    ArgParser(args).parseInto(::AdventOfCodeArgs).run {
        val solution = AdventOfCode.days[day] ?: throw IllegalArgumentException("Day: $day not implemented")
        if (part == 0 || part == 1) {
            solution.showResult(1, sample)
        }
        if (part == 0 || part == 2) {
            solution.showResult(2, sample)
        }
    }
}

fun AdventOfCode.runPart(part: Int, sample: Boolean = false): SolutionResult {
    val inputFilename = if (sample) sampleOf(part) else fileFor("input")
    val input = SolutionInput(Parsers.readLinesFromResource(inputFilename))
    if (input.lines.isEmpty()) {
        throw IllegalArgumentException("No input found for $inputFilename")
    }
    return when (part) {
        1 -> partOne(input)
        2 -> partTwo(input)
        else -> throw IllegalArgumentException("Invalid part $part")
    }
}

fun AdventOfCode.fileFor(name: String) = "Day${"%02d".format(day)}/$name.txt"

fun AdventOfCode.sampleOf(part: Int): String {
    val sample1 = fileFor("sample1")
    val sample2 = fileFor("sample2")
    return if (part == 1) sample1 else
        if (resourceExists(sample2)) sample2 else sample1
}

fun AdventOfCode.showResult(part: Int, sample: Boolean = false) {
    val result = measureExecutionTime { runPart(part, sample) }
    println("${ANSI_YELLOW}Day $day Part $part: ${ANSI_CYAN}${result.first}${ANSI_RESET} (${result.second}ms)")
}

fun <T> measureExecutionTime(block: () -> T): Pair<T, Long> {
    var result: T?
    val executionTime = measureTimeMillis {
        result = block()
    }
    return Pair(result!!, executionTime)
}

class AdventOfCodeArgs(parser: ArgParser) {
    val sample by parser.flagging(
        "-S", "--sample",
        help = "run on sample input"
    )
    val day by parser.storing(
        "-D", "--day",
        help = "day number"
    ) { toInt() }

    val part by parser.storing(
        "-P", "--part",
        help = "part number"
    ) { toInt() }.default(0)
}

const val ANSI_RESET = "\u001B[0m"
const val ANSI_BLACK = "\u001B[90m"
const val ANSI_RED = "\u001B[91m"
const val ANSI_GREEN = "\u001B[92m"
const val ANSI_YELLOW = "\u001B[93m"
const val ANSI_BLUE = "\u001B[94m"
const val ANSI_PURPLE = "\u001B[95m"
const val ANSI_CYAN = "\u001B[96m"
const val ANSI_WHITE = "\u001B[97m"