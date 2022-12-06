fun main() {
    fun part1(input: List<String>): Int {
        val chars = input[0]
        for (i in 0..chars.length-4) {
            var sub = chars.subSequence(i, i+4).toSet()
            if (sub.size == 4) {
                // Find the first set that has 4 unique elements
                return i + 4
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val chars = input[0]
        for (i in 0..chars.length-14) {
            var sub = chars.subSequence(i, i+14).toSet()
            if (sub.size == 14) {
                // Find the first set that has 14 unique elements
                return i + 14
            }
        }
        return 0
    }

    val input = readInput("../input/Day06")
    println(part1(input))
    println(part2(input))
}
