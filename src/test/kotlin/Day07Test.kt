import day07.Day07
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class Day07Test : FunSpec({
    context("Day 7") {
        test("Part 1") {
            Day07().runPart(1).asLong shouldBe 1430271835320
        }
        test("Part 2") {
            Day07().runPart(2).asLong shouldBe 456565678667482
        }
    }
})