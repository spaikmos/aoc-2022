fun main() {
    // Borders of the valley are at x=0, x=XMAX, y=0, and y=YMAX
    val XMAX = 121
    val YMAX = 26
    fun parseInput(input: List<String>): MutableSet<Triple<Int, Int, Char>> {
        // Each wind unit is an x,y point and wind direction
        val wind = mutableSetOf<Triple<Int, Int, Char>>()
        for ((y, line) in input.withIndex()) {
            for ((x, point) in line.withIndex()) {
                // Input consists of ^>v< directions and '#' and '.'.  Filter out the '#' and '.'
                if ((point != '#') && (point != '.')) {
                    wind.add(Triple(x, y, point))
                }
            }
        }
        return wind
    }
    // Calculate every point an elf can move.  Each elf can move up, down, left, and right.
    //  Calculate every point an elf can be, making sure they can't go into the borders.  Need to
    //  to check the y-coordinates because the first elf will be at the entrance/exit of the valley.
    fun newElves(elves: MutableSet<Pair<Int, Int>>): MutableSet<Pair<Int, Int>> {
        val newElves = mutableSetOf<Pair<Int, Int>>()
        for (elf in elves) {
            val (x, y) = elf
            if (((x - 1) > 0) && (y > 0) && (y < YMAX)) {
                newElves.add(Pair(x-1, y))
            }
            if (((x + 1) < XMAX) && (y > 0) && (y < YMAX)) {
                newElves.add(Pair(x+1, y))
            }
            if ((y - 1) > 0) {
                newElves.add(Pair(x, y-1))
            }
            if ((y + 1) < YMAX) {
                newElves.add(Pair(x, y+1))
            }
        }
        return newElves
    }
    fun moveWind(wind: MutableSet<Triple<Int, Int, Char>>): MutableSet<Triple<Int, Int, Char>> {
        val newWind = mutableSetOf<Triple<Int, Int, Char>>()
        for (point in wind) {
            var (x, y, w) = point
            when (w) {
                '^' -> y = if (y-1 == 0) YMAX-1 else y-1
                'v' -> y = if (y+1 == YMAX) 1 else y+1
                '>' -> x = if (x+1 == XMAX) 1 else x+1
                '<' -> x = if (x-1 == 0) XMAX-1 else x-1
            }
            newWind.add(Triple(x, y, w))
        }
        return newWind
    }
    fun killElves(elves: MutableSet<Pair<Int, Int>>, wind: MutableSet<Triple<Int, Int, Char>>): MutableSet<Pair<Int, Int>> {
        val deadElves = mutableSetOf<Pair<Int, Int>>()
        for (elf in elves) {
            val (x, y) = elf
            if (wind.filter() { it -> (x == it.first) && (y == it.second) }.size > 0) {
                deadElves.add(Pair(x, y))
            }
        }
        return deadElves
    }
    fun travel(start: Pair<Int, Int>, end: Pair<Int, Int>, i: MutableSet<Triple<Int, Int, Char>>): Pair<Int, MutableSet<Triple<Int, Int, Char>>> {
        var elves = mutableSetOf<Pair<Int, Int>>()
        var wind = i
        var numRound = 0
        elves.add(start)
        do {
            elves.addAll(newElves(elves))
            wind = moveWind(wind)
            val deadElves = killElves(elves, wind)
            elves.removeAll(deadElves)
            numRound++
            //println("Round $numRound - wind: ${wind.size}, elves: ${elves.size}, dead: ${deadElves.size}")
        } while (!elves.contains(end))
        // End destination is right before the entrance/exit of the valley.  Need to run one more round
        wind = moveWind(wind)
        numRound++
        return Pair(numRound, wind)
    }
    fun part1(i: MutableSet<Triple<Int, Int, Char>>): Int {
        val ans = travel(Pair(1, 0), Pair(120, 25), i)
        return ans.first
    }

    fun part2(i: MutableSet<Triple<Int, Int, Char>>): Int {
        var totalRounds = 0
        var ans = travel(Pair(1, 0), Pair(120, 25), i)
        totalRounds += ans.first
        println("Travel to end in $totalRounds rounds")
        ans = travel(Pair(120, 26), Pair(1, 1), ans.second)
        totalRounds += ans.first
        println("Travel back to start in ${ans.first} rounds, totalRounds: $totalRounds")
        ans = travel(Pair(1, 0), Pair(120, 25), ans.second)
        totalRounds += ans.first
        println("Travel to end again ${ans.first} rounds, totalRounds: $totalRounds")
        return totalRounds
    }

    val input = readInput("../input/Day24")
    val wind = parseInput(input)
    println(part1(wind))    // 297
    println(part2(wind))    // 856
}