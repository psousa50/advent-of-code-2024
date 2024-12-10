import day06.Day06
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day06Test : FunSpec({
    context("Day 6") {
        test("Part 1") {
            Day06().runPart(1).asInt shouldBe 0
        }
        test("Part 2") {
            Day06().runPart(2).asInt shouldBe 0
        }
    }
})