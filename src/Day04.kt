fun main() {
    fun part1(input: List<String>): Int {
        var num = 0
        for (i in input) {
            var parts = i.split(",", "-").map{it.toInt()}.toTypedArray()
            if ((parts[0] <= parts[2]) && (parts[1] >= parts[3])) {
                num++
            } else if ((parts[2] <= parts[0]) && (parts[3] >= parts[1])) {
                num++
            }
        }
        return num
    }

    fun part2(input: List<String>): Int {
        var num = 0
        for (i in input) {
            var parts = i.split(",", "-").map{it.toInt()}.toTypedArray()
            if ((parts[0] <= parts[3]) && (parts[1] >= parts[2])) {
                num++
            } else if ((parts[2] <= parts[1]) && (parts[3] >= parts[0])) {
                num++
            }
        }
        return num
    }

    val input = readInput("../input/Day04")
    println(part1(input))
    println(part2(input))
}
