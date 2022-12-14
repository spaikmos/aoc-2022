fun main() {
    fun parseInput(input: List<String>): MutableSet<Pair<Int, Int>> {
        // Parse the input into a set of points
        val m = mutableSetOf<Pair<Int, Int>>()
        for (i in input) {
            val parts = i.split(" -> ")
            val it = parts.iterator()
            var tmp = (it.next().split(',').map{it -> it.toInt()})
            var prev = Pair(tmp[0], tmp[1])
            while (it.hasNext()) {
                tmp = it.next().split(',').map{it -> it.toInt()}
                var cur = Pair(tmp[0], tmp[1])
                if (cur.first == prev.first) {
                    // this is a vertical wall
                    if (cur.second < prev.second) {
                        for (y in cur.second..prev.second) {
                            m.add(Pair(cur.first, y))
                        }
                    } else {
                        for (y in cur.second downTo prev.second) {
                            m.add(Pair(cur.first, y))
                        }
                    }
                } else if (cur.second == prev.second) {
                    // this is a horizontal wall
                    if (cur.first < prev.first) {
                        for (x in cur.first..prev.first) {
                            m.add(Pair(x, cur.second))
                        }
                    } else {
                        for (x in cur.first downTo prev.first) {
                            m.add(Pair(x, cur.second))
                        }
                    }
                }
                prev = cur
            }
        }
        return m
    }

    fun part1(m: MutableSet<Pair<Int, Int>>): Int {
        //var m = parseInput(input)
        // run the simulation:
        val maxY = m.maxBy { it.second }.second
        val numRock = m.size
        do {
            var cur = Pair(500, 0)
            var stop = false
            while ((stop == false) && (cur.second < maxY)) {
                // check area directly below
                if (m.contains(Pair(cur.first, cur.second+1)) == false) {
                    cur = Pair(cur.first, cur.second+1)
                } else if (m.contains(Pair(cur.first-1, cur.second+1)) == false) {
                    cur = Pair(cur.first-1, cur.second+1)
                } else if (m.contains(Pair(cur.first+1, cur.second+1)) == false) {
                    cur = Pair(cur.first+1, cur.second+1)
                } else {
                    // Sand stopped moving
                    stop = true
                    m.add(cur)
                }
            }
        } while (cur.second < maxY)
        return m.size - numRock
    }

    fun part2(m: MutableSet<Pair<Int, Int>>): Int {
        val minX = m.minBy { it.first }.first
        val maxX = m.maxBy { it.first }.first
        val maxY = m.maxBy { it.second }.second
        // Add floor to map
        for (x in (minX - 200)..(maxX + 200)) {
            m.add(Pair(x, maxY+2))
        }

        // Run simulation
        val numRock = m.size
        do {
            var cur = Pair(500, 0)
            var stop = false
            while (stop == false) {
                // check area directly below
                if (m.contains(Pair(cur.first, cur.second+1)) == false) {
                    cur = Pair(cur.first, cur.second+1)
                } else if (m.contains(Pair(cur.first-1, cur.second+1)) == false) {
                    cur = Pair(cur.first-1, cur.second+1)
                } else if (m.contains(Pair(cur.first+1, cur.second+1)) == false) {
                    cur = Pair(cur.first+1, cur.second+1)
                } else {
                    // Sand stopped moving
                    stop = true
                    m.add(cur)
                }
            }
         } while (cur != Pair(500, 0))  // Stop when sand doesn't move
        return m.size - numRock
    }

    val input = readInput("../input/Day14")
    println(part1(parseInput(input)))   // 728
    println(part2(parseInput(input)))   // 27623
}