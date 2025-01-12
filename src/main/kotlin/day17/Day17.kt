package day17

import AdventOfCode
import Parsers.split
import SolutionInput
import SolutionResult
import asSolution
import kotlin.math.pow

class Day17 : AdventOfCode {
    override val day = 17

    override fun partOne(input: SolutionInput): SolutionResult {
        val parts = input.lines.split { it.isBlank() }
        val registers = parts[0].parseRegisters()
        val program = parts[1].parseProgram()

        val computer = Computer(program, CPU(registers[0], registers[1], registers[2]))

        return computer.run().mapNotNull { it.output }.joinToString(",").asSolution()
    }

    override fun partTwo(input: SolutionInput): SolutionResult {
        val parts = input.lines.split { it.isBlank() }
        val program = parts[1].parseProgram()

        val a = findA(0, program, program.dropLast(2)) ?: throw IllegalArgumentException("No solution found")

        return a.asSolution()
    }

    private fun findA(currentA: Long, programToMatch: List<Int>, program: List<Int>): Long? {
        return if (programToMatch.isEmpty()) {
            currentA
        } else {
            (8 * currentA..8 * currentA + 7).map { newA ->
                Computer(program, CPU(newA, 0, 0)).run().mapNotNull { s -> s.output }.first()
            }.mapIndexedNotNull { valueForA, output -> if (output == programToMatch.last()) valueForA else null }
                .firstNotNullOfOrNull { valueForA ->
                    findA(
                        currentA = valueForA + (8 * currentA),
                        programToMatch = programToMatch.dropLast(1),
                        program = program
                    )
                }
        }
    }
}

enum class OpCode(val v: Int) {
    adv(0),
    bxl(1),
    bst(2),
    jnz(3),
    bxc(4),
    `out`(5),
    bdv(6),
    cdv(7),
}

data class Instruction(val opCode: OpCode, val operand: Int)

data class Computer(private val program: List<Int>, private val cpu: CPU) {
    fun run() =
        generateSequence(cpu) { cpu ->
            if (cpu.programCounter >= program.size)
                null
            else {
                cpu.execute(program.nextInstruction(cpu.programCounter))
            }
        }
}

data class CPU(
    val registerA: Long,
    val registerB: Long,
    val registerC: Long,
    val programCounter: Int = 0,
    val output: Int? = null
) {
    fun execute(instruction: Instruction): CPU {
        val (opcode, operand) = instruction
        return copy(output = null)
            .applyOpcode(opcode, operand)
            .let { if (opcode == OpCode.jnz) it else it.move(2) }
    }

    private fun applyOpcode(opcode: OpCode, operand: Int) = when (opcode) {
        OpCode.adv -> copy(registerA = divideAByPow2OnOperand(operand))
        OpCode.bxl -> copy(registerB = registerB xor operand.toLong())
        OpCode.bst -> copy(registerB = combo(operand) % 8)
        OpCode.jnz -> if (registerA != 0L) copy(programCounter = operand) else move(2)
        OpCode.bxc -> copy(registerB = registerB xor registerC)
        OpCode.`out` -> copy(output = (combo(operand) % 8).toInt())
        OpCode.bdv -> copy(registerB = divideAByPow2OnOperand(operand))
        OpCode.cdv -> copy(registerC = divideAByPow2OnOperand(operand))
    }

    private fun divideAByPow2OnOperand(operand: Int) =
        (registerA.toDouble() / 2.0.pow(combo(operand).toDouble())).toLong()

    private fun move(steps: Int): CPU {
        return copy(programCounter = programCounter + steps)
    }

    private fun combo(operand: Int): Long {
        return when (operand) {
            in 0..3 -> operand.toLong()
            4 -> registerA
            5 -> registerB
            6 -> registerC
            7 -> throw IllegalArgumentException("7 is reserved")
            else -> throw IllegalArgumentException("Invalid operand")
        }
    }
}

fun List<String>.parseRegisters(): List<Long> {
    val regex = Regex("-?\\d+")
    val parts = split { it.isBlank() }
    return parts[0].map({ regex.findAll(it).first().value.toLong() })
}

fun List<String>.parseProgram() =
    first().split(" ")[1].split(",").map { it.toInt() }

fun List<Int>.nextInstruction(p: Int) = Instruction(OpCode.entries[this[p]], this[p + 1])