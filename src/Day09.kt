fun main() {
    val DIM = 1000
    val START = DIM/2

    fun part1(input: List<String>): Int {
        val head = intArrayOf(START, START)
        var tail = intArrayOf(START, START)
        val seen = mutableSetOf(Pair(START, START))

        for (i in input) {
            val parts = i.split(" ")
            val dir = parts[0][0]
            val dist = parts[1].toInt()

            for (j in 1..dist) {
                val prev = head.clone()
                // Move the head
                when (dir) {
                    'U' -> head[1]++
                    'D' -> head[1]--
                    'L' -> head[0]--
                    'R' -> head[0]++
                }

                // Tail only moves if head is more than 2 spaces away in any direction
                if ((kotlin.math.abs(head[0] - tail[0]) > 1) ||
                    (kotlin.math.abs(head[1] - tail[1]) > 1)) {
                    // If the tail moves, it occupies the previous space that head was at
                    tail = prev
                    // Add the position to the set of visited positions
                    seen.add(Pair(tail[0], tail[1]))
                }
            }
        }
        return seen.size
    }

    fun part2(input: List<String>): Int {
        val knot = Array(10){Array(2){START}}
        val seen = mutableSetOf(Pair(START, START))

        for (i in input) {
            val parts = i.split(" ")
            val dir = parts[0][0]
            val dist = parts[1].toInt()

            for (j in 1..dist) {
                // Move the head
                when (dir) {
                    'U' -> knot[0][1]++
                    'D' -> knot[0][1]--
                    'L' -> knot[0][0]--
                    'R' -> knot[0][0]++
                }

                for (k in 1..9) {
                    if ((kotlin.math.abs(knot[k-1][0] - knot[k][0]) > 1) &&
                        (kotlin.math.abs(knot[k-1][1] - knot[k][1]) > 1)) {
                        // Previous knot moved diagonally.  This needs to move in same direction
                        knot[k][0] = (knot[k-1][0] + knot[k][0]) / 2
                        knot[k][1] = (knot[k-1][1] + knot[k][1]) / 2
                    } else if (kotlin.math.abs(knot[k-1][0] - knot[k][0]) > 1) {
                        knot[k][0] = (knot[k-1][0] + knot[k][0]) / 2
                        knot[k][1] = knot[k-1][1]
                    } else if (kotlin.math.abs(knot[k-1][1] - knot[k][1]) > 1) {
                        knot[k][1] = (knot[k-1][1] + knot[k][1]) / 2
                        knot[k][0] = knot[k-1][0]
                    } else {
                        // Chain has stopped moving, no need to process the rest of the knots
                        break
                    }
                }
                // Add the position to the set of visited positions
                seen.add(Pair(knot[9][0], knot[9][1]))
            }
        }
        return seen.size
    }

    val input = readInput("../input/Day09")
    println(part1(input))
    println(part2(input))
}