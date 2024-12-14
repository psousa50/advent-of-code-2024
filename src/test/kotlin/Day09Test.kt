import day09.Day09
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day09Test : FunSpec({
    context("Day 9") {
        test("Part 1") {
            Day09().runPart(1).asLong shouldBe 6283404590840
        }
        test("Part 2") {
            Day09().runPart(2).asLong shouldBe 0
        }
    }
})