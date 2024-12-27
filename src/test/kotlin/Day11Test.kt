import day11.Day11
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day11Test : FunSpec({
    context("Day 11") {
        test("Part 1") {
            Day11().runPart(1).asInt shouldBe 209412
        }
        test("Part 2") {
            Day11().runPart(2).asLong shouldBe 248967696501656
        }
    }
})