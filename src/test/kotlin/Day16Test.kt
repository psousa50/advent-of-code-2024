import day16.Day16
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day16Test : FunSpec({
    context("Day 16") {
        test("Part 1") {
            Day16().runPart(1).asInt shouldBe 85432
        }
        test("Part 2") {
            Day16().runPart(2).asInt shouldBe 465
        }
    }
})