fun main() {
    fun part1(input: List<String>): Int {
        var tick = 1
        var x = 1
        var score = 0
        val signal = setOf(20, 60, 100, 140, 180, 220)

        for (i in input) {
            // Whether nop or addx, increment the tick and calculate the score
            tick++
            if (signal.contains(tick)) {
                score += x * tick
            }
            if (i.contains("addx")) {
                x += i.split(' ')[1].toInt()
                tick++
                if (signal.contains(tick)) {
                    score += x * tick
                }
            }

        }
        return score
    }

    fun part2(input: List<String>): Int {
        val crt = Array(6){ CharArray(40){'.'} }
        var tick = 0
        var x = 1

        for (i in input) {
            if (tick.mod(40) in (x-1)..(x+1)) {
                crt[tick / 40][tick.mod(40)] = '#'
            }
            tick++
            if (i.contains("addx")) {
                if (tick.mod(40) in (x-1)..(x+1)) {
                    crt[tick / 40][tick.mod(40)] = '#'
                }
                x += i.split(' ')[1].toInt()
                tick++
            }
        }
        for (i in crt) {
            println(String(i))
        }
        return 0
    }

    val input = readInput("../input/Day10")
    println(part1(input))
    println(part2(input))
}
