import day17.Day17
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day17Test : FunSpec({
    context("Day 17") {
        test("Part 1") {
            Day17().runPart(1).asString shouldBe "1,2,3,1,3,2,5,3,1"
        }
        test("Part 2") {
            Day17().runPart(2).asLong shouldBe 105706277661082
        }
    }
})