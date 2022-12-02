fun main() {
    fun part1(input: List<String>): Int {
        var max: Int = 0
        var tmp: Int = 0
        for (i in input) {
            try {
                var num: Int = i.toInt()
                tmp += num
            } catch (e: NumberFormatException) {
                if (tmp > max) {
                    max = tmp
                    println("new max = $max")
                }
                tmp = 0
            }
        }
        return max
    }

    fun part2(input: List<String>): Int {
        var max0: Int = 0
        var max1: Int = 0
        var max2: Int = 0
        var tmp: Int = 0

        for (i in input) {
            try {
                var num: Int = i.toInt()
                tmp += num
            } catch (e: NumberFormatException) {
                if (tmp > max0) {
                    max2 = max1
                    max1 = max0
                    max0 = tmp
                    println("new max0 = $max0 $max1 $max2")
                } else if (tmp > max1) {
                    max2 = max1
                    max1 = tmp
                    println("new max1 = $max0 $max1 $max2")
                } else if (tmp > max2) {
                    max2 = tmp
                    println("new max1 = $max0 $max1 $max2")
                }
                tmp = 0
            }
        }
        return max0 + max1 + max2
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("../input/Day01_test")
    //check(part1(testInput) == 1)

    val input = readInput("../input/Day01")
    println(part1(input))
    println(part2(input))
}
