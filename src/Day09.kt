fun main() {
    fun part1(input: List<String>): Int {
        val DIM = 1000
        val START = DIM/2
        var headX = START
        var headY = START
        var tailX = START
        var tailY = START
        var seen = Array(DIM) {Array(DIM) {0} }

        for (i in input) {
            val parts = i.split(" ")
            val dir = parts[0][0]
            val dist = parts[1].toInt()

            for (j in 1..dist) {
                // Move the head
                if (dir == 'U') {
                    headY++
                } else if (dir == 'D') {
                    headY--
                } else if (dir == 'L') {
                    headX--
                } else if (dir == 'R') {
                    headX++
                }

                // Move the tail (if needed)
                if ((headX == tailX) && kotlin.math.abs(headY - tailY) > 1) {
                    tailY = (headY + tailY) / 2
                } else if ((headY == tailY) && kotlin.math.abs(headX - tailX) > 1) {
                    tailX = (headX + tailX) / 2
                } else if (kotlin.math.abs(headX - tailX) > 1) {
                    tailX = (headX + tailX) / 2
                    tailY = headY
                } else if (kotlin.math.abs(headY - tailY) > 1) {
                    tailY = (headY + tailY) / 2
                    tailX = headX
                }
                // Mark the position on the map
                seen[tailX][tailY] = 1
            }
        }

        // Count the total number of trees
        var total = 0
        for (i in seen) {
            total += i.sum()
        }
        return total
    }

    fun part2(input: List<String>): Int {
        val DIM = 1000
        val START = DIM/2
        var headX = IntArray(10){START}
        var headY = IntArray(10){START}
        var seen = Array(DIM) {Array(DIM) {0} }

        for (i in input) {
            val parts = i.split(" ")
            val dir = parts[0][0]
            val dist = parts[1].toInt()

            for (j in 1..dist) {
                // Move the head
                if (dir == 'U') {
                    headY[0]++
                } else if (dir == 'D') {
                    headY[0]--
                } else if (dir == 'L') {
                    headX[0]--
                } else if (dir == 'R') {
                    headX[0]++
                }

                for (k in 1..9) {
                    // Move the tail (if needed)
                    if ((headX[k-1] == headX[k]) && kotlin.math.abs(headY[k-1] - headY[k]) > 1){
                        headY[k] = (headY[k-1] + headY[k]) / 2
                    } else if ((headY[k-1] == headY[k]) && kotlin.math.abs(headX[k-1] - headX[k]) > 1){
                        headX[k] = (headX[k-1] + headX[k]) / 2
                    } else if ((kotlin.math.abs(headX[k-1] - headX[k]) > 1) &&
                        (kotlin.math.abs(headY[k-1] - headY[k]) > 1)) {
                        // Previous knot moved diagonally.  This needs to move in same direction
                        headX[k] = (headX[k-1] + headX[k]) / 2
                        headY[k] = (headY[k-1] + headY[k]) / 2
                    } else if (kotlin.math.abs(headX[k-1] - headX[k]) > 1) {
                        headX[k] = (headX[k-1] + headX[k]) / 2
                        headY[k] = headY[k-1]
                    } else if (kotlin.math.abs(headY[k-1] - headY[k]) > 1) {
                        headY[k] = (headY[k-1] + headY[k]) / 2
                        headX[k] = headX[k-1]
                    }
                }
                // Mark the position on the map
                seen[headX[9]][headY[9]] = 1
            }
        }

        // Count the total number of trees
        var total = 0
        for (i in seen) {
            total += i.sum()
        }
        return total
    }

    val input = readInput("../input/Day09")
    println(part1(input))
    println(part2(input))
}
