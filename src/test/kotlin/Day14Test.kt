import day14.Day14
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day14Test : FunSpec({
    context("Day 14") {
        test("Part 1") {
            Day14().runPart(1).asInt shouldBe 0
        }
        test("Part 2") {
            Day14().runPart(2).asInt shouldBe 0
        }
    }
})