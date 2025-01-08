import day15.Day15
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day15Test : FunSpec({
    context("Day 15") {
        test("Part 1") {
            Day15().runPart(1).asInt shouldBe 1414416
        }
        test("Part 2") {
            Day15().runPart(2).asInt shouldBe 1386070
        }
    }
})