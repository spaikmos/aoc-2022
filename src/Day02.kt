fun main() {
    fun part1(input: List<String>): Int {
        var score: Int = 0

        for (i in input) {
            val chars = i.toCharArray()

            if (chars[0] == 'A') {
                if (chars[2] == 'X') {
                    score += 4
                } else if (chars[2] == 'Y') {
                    score += 8
                } else if (chars[2] == 'Z') {
                    score += 3
                }
            } else if (chars[0] == 'B') {
                if (chars[2] == 'X') {
                    score += 1
                } else if (chars[2] == 'Y') {
                    score += 5
                } else if (chars[2] == 'Z') {
                    score += 9
                }
            } else if (chars[0] == 'C') {
                if (chars[2] == 'X') {
                    score += 7
                } else if (chars[2] == 'Y') {
                    score += 2
                } else if (chars[2] == 'Z') {
                    score += 6
                }
            }
        }
        return score
    }

    fun part2(input: List<String>): Int {
        var score: Int = 0
        for (i in input) {
            val chars = i.toCharArray()

            if (chars[0] == 'A') {
                if (chars[2] == 'X') {
                    score += 3
                } else if (chars[2] == 'Y') {
                    score += 4
                } else if (chars[2] == 'Z') {
                    score += 8
                }
            } else if (chars[0] == 'B') {
                if (chars[2] == 'X') {
                    score += 1
                } else if (chars[2] == 'Y') {
                    score += 5
                } else if (chars[2] == 'Z') {
                    score += 9
                }
            } else if (chars[0] == 'C') {
                if (chars[2] == 'X') {
                    score += 2
                } else if (chars[2] == 'Y') {
                    score += 6
                } else if (chars[2] == 'Z') {
                    score += 7
                }
            }
        }
        return score
    }

    // These two functions are the same as part1 and part2 above, except I use a 2D look-up table
    //  to calculate the score.
    fun part3(input: List<String>): Int {
        var score = 0
        val table: Array<IntArray> = arrayOf(
            intArrayOf(4, 8, 3),
            intArrayOf(1, 5, 9),
            intArrayOf(7, 2, 6)
        )

        for (i in input) {
            val chars = i.toCharArray()
            val idx1 = chars[0] - 'A'
            val idx2 = chars[2] - 'X'

            score += table[idx1][idx2]
        }
        return score
    }

    fun part4(input: List<String>): Int {
        var score = 0
        val table: Array<IntArray> = arrayOf(
            intArrayOf(3, 4, 8),
            intArrayOf(1, 5, 9),
            intArrayOf(2, 6, 7)
        )

        for (i in input) {
            val chars = i.toCharArray()
            val idx1 = chars[0] - 'A'
            val idx2 = chars[2] - 'X'

            score += table[idx1][idx2]
        }
        return score
    }

    val input = readInput("../input/Day02")
    println(part1(input))
    println(part2(input))
    println(part3(input))
    println(part4(input))
}
