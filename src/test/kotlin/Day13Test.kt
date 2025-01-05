import day13.Day13
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day13Test : FunSpec({
    context("Day 13") {
        test("Part 1") {
            Day13().runPart(1).asInt shouldBe 27157
        }
        test("Part 2") {
            Day13().runPart(2).asLong shouldBe 104015411578548
        }
    }
})