object Parsers {
    fun readLinesFromResource(fileName: String): List<String> {
        val inputStream = javaClass.getResourceAsStream(fileName) ?: throw Exception("File $fileName not found")
        return inputStream.bufferedReader().readLines()
    }

    fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> =
        fold(emptyList()) { acc, line ->
            if (predicate(line)) {
                acc + listOf(emptyList())
            } else {
                if (acc.isEmpty()) {
                    listOf(listOf(line))
                } else {
                    acc.dropLast(1) + listOf(acc.last() + listOf(line))
                }
            }
        }
}

fun Collection<Int>.product() = reduce { acc, i -> acc * i }
fun Collection<Long>.product() = reduce { acc, i -> acc * i }
