fun main() {
    // Dimension of row
    val DIM = 99
    fun part1(input: List<String>): Int {
        // Create a 2D array of the same size as the input
        var seenTrees = Array(DIM) {Array(DIM) {0} }

        for (i in 0..(DIM-1)) {
            val nums = input[i].map{ it - '0' }

            // Count trees from the left.
            var maxHeight = nums[0]
            seenTrees[i][0] = 1
            for (j in 1..(DIM-2)) {
                if (nums[j] > maxHeight) {
                    maxHeight = nums[j]
                    seenTrees[i][j] = 1
                }
            }

            // Count trees from the right
            maxHeight = nums[DIM-1]
            seenTrees[i][DIM-1] = 1
            for (j in (DIM-2) downTo 1) {
                if (nums[j] > maxHeight) {
                    maxHeight = nums[j]
                    seenTrees[i][j] = 1
                }
            }

            // Count trees from the top
            maxHeight = input[0][i] - '0'
            seenTrees[0][i] = 1
            for (j in 1..(DIM-2)) {
                val height = input[j][i] - '0'
                if (height > maxHeight) {
                    maxHeight = height
                    seenTrees[j][i] = 1
                }
            }

            // Count trees from the bottom
            maxHeight = input[DIM-1][i] - '0'
            seenTrees[DIM-1][i] = 1
            for (j in (DIM-2) downTo 1) {
                val height = input[j][i] - '0'
                if (height > maxHeight) {
                    maxHeight = height
                    seenTrees[j][i] = 1
                }
            }

        }

        // Count the total number of trees
        var total = 0
        for (i in seenTrees) {
            total += i.sum()
        }
        return total
    }

    fun part2(input: List<String>): Int {
        var max = 0
        for (i in 1..97) {
            for (j in 1..97) {
                val curHeight = input[i][j] - '0'

                // count north
                var score = 1
                var numTrees = 1
                for (k in j-1 downTo 1) {
                    if ((input[i][k] - '0') < curHeight) {
                        numTrees++
                    } else {
                        break
                    }
                }
                score *= numTrees

                // count south
                numTrees = 1
                for (k in j+1..DIM-2) {
                    if ((input[i][k] - '0') < curHeight) {
                        numTrees++
                    } else {
                        break
                    }
                }
                score *= numTrees

                // count east
                numTrees = 1
                for (k in i+1..DIM-2) {
                    if ((input[k][j] - '0') < curHeight) {
                        numTrees++
                    } else {
                        break
                    }
                }
                score *= numTrees

                // count west
                numTrees = 1
                for (k in i-1 downTo 1) {
                    if ((input[k][j] - '0') < curHeight) {
                        numTrees++
                    } else {
                        break
                    }
                }
                score *= numTrees

                if (score > max) {
                    max = score
                }
            }
        }

        return max
    }

    val input = readInput("../input/Day08")
    println(part1(input))
    println(part2(input))
}
