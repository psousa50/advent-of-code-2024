import day01.Day01
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day01Test : FunSpec({
    context("Day 01") {
        test("Part 1") {
            Day01().runPart(1).asInt shouldBe 1651298
        }
        test("Part 2") {
            Day01().runPart(2).asInt shouldBe 21306195
        }
    }
})