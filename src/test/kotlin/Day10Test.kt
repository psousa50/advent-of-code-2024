import day10.Day10
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day10Test : FunSpec({
    context("Day 10") {
        test("Part 1") {
            Day10().runPart(1).asInt shouldBe 789
        }
        test("Part 2") {
            Day10().runPart(2).asInt shouldBe 1735
        }
    }
})