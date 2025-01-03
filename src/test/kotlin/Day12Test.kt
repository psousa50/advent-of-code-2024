import day12.Day12
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day12Test : FunSpec({
    context("Day 12") {
        test("Part 1") {
            Day12().runPart(1).asInt shouldBe 1437300
        }
        test("Part 2") {
            Day12().runPart(2).asInt shouldBe 849332
        }
    }
})