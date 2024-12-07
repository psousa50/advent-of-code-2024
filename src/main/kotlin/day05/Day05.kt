package day05

import AdventOfCode
import Parsers.split
import SolutionInput
import SolutionResult
import asSolution
import java.util.Collections.addAll

typealias Graph = MutableMap<Int, MutableList<Int>>

class Day05 : AdventOfCode {
    override val day = 5

    override fun partOne(input: SolutionInput): SolutionResult {
        val (rulesList, updatesList) = input.lines.split { it.isBlank() }

        val rules = Rules(rulesList.map { it.toRule() })

        val updates = updatesList.map { Update(it.split(",").map { it.toInt() }) }

        return updates.filter { inOrder(it.pages, rules) }.sumOf { it.middle() }.asSolution()

    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val (rulesList, updatesList) = input.lines.split { it.isBlank() }

        val rules = Rules(rulesList.map { it.toRule() })

        val updates = updatesList.map { Update(it.split(",").map { it.toInt() }) }

        return updates.mapNotNull { it.wasOutOfOrder(rules) }.sumOf { it.middle() }.asSolution()
    }
}

fun String.toRule() = this.split("|").let { Rule(it.first().toInt(), it.last().toInt()) }

data class Rule(val before: Int, val after: Int)

data class Rules(val rules: List<Rule>) {

    val graph: Graph

    init {
        val allNumbers = rules.map { it.before }.plus(rules.map { it.after }).toSet()
        graph = allNumbers.associateWith { mutableListOf<Int>() }.toMutableMap()
        rules.forEach { (before, after) ->
            graph.computeIfAbsent(before) { mutableListOf() }.add(after)
        }
    }

    fun isBefore(firstPage: Int, secondPage: Int): Boolean {
        return graph[firstPage]?.contains(secondPage) ?: false
    }
}

data class Update(val pages: List<Int>) {

    fun middle() = pages[pages.size / 2]

    fun wasOutOfOrder(rules: Rules): Update? {
        val fixed = fixOrder(rules)
        return if (fixed != this) fixed else null
    }

    fun fixOrder(rules: Rules): Update {
        val orderedPages = pages.toMutableList()
        var done = false
        while (!done) {
            var changed = false
            for (i in 0 until orderedPages.size - 1) {
                if (rules.isBefore(orderedPages[i+1], orderedPages[i])) {
                    val temp = orderedPages[i]
                    orderedPages[i] = orderedPages[i + 1]
                    orderedPages[i + 1] = temp
                    changed = true
                }
            }
            done = !changed
        }

        println(orderedPages)
        return Update(orderedPages)
    }
}

fun inOrder(pages: List<Int>, rules: Rules) =
    List(pages.size) { index ->
        pages.indices.all {
            when {
                it < index -> rules.isBefore(pages[it], pages[index])
                it > index -> rules.isBefore(pages[index], pages[it])
                else -> true
            }
        }

    }.all { it }

