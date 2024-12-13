import day08.Day08
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day08Test : FunSpec({
    context("Day 8") {
        test("Part 1") {
            Day08().runPart(1).asInt shouldBe 0
        }
        test("Part 2") {
            Day08().runPart(2).asInt shouldBe 0
        }
    }
})