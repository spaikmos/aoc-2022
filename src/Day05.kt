fun main() {
    fun parseInput(input: List<String>): List<ArrayDeque<Char>> {
        val blocks = List(9) { ArrayDeque<Char>()}

        // This function is hardcoded to the input.  The first 8 lines provide the inputs
        for (i in input) {
            if (i[1] == '1') {
                // Exit when the row with the column indices appears
                break
            }
            for (j in 0..8) {
                // Pick the block letter out of the respective column
                val block = i[4*j+1]
                if (block != ' ') {
                    blocks[j].addFirst(block)
                }
            }
        }
        return blocks
    }

    fun part1(input: List<String>): String {
        val blocks = parseInput(input)

        for (i in input) {
            try {
                val cmd = i.split("move ", " from ", " to ")
                val numToMove = cmd[1].toInt()
                val from = cmd[2].toInt() - 1   // Array index starts from 0
                val to = cmd[3].toInt() - 1     // Array index starts from 0

                for (j in 1..numToMove) {
                    blocks[to].addLast(blocks[from].removeLast())
                }
            } catch (e: Exception) {
                // Skip the lines that do not have the delimeters
            }
        }

        // Concatenate the top answer for each block
        var answer = ""
        for (i in blocks) {
            answer += i.last()
        }
        return answer
    }

    fun part2(input: List<String>): String {
        val blocks = parseInput(input)

        for (i in input) {
            try {
                val cmd = i.split("move ", " from ", " to ")
                val numToMove = cmd[1].toInt()
                val from = cmd[2].toInt() - 1   // Array index starts from 0
                val to = cmd[3].toInt() - 1     // Array index starts from 0
                val idx = blocks[from].size - numToMove

                for (j in 1..numToMove) {
                    blocks[to].addLast(blocks[from].removeAt(idx))
                }
            } catch (e: Exception) {
                // Skip the lines that do not have the delimeters
            }
        }

        // Concatenate the top answer for each block
        var answer = ""
        for (i in blocks) {
            answer += i.last()
        }
        return answer
    }

    val input = readInput("../input/Day05")
    println(part1(input))
    println(part2(input))
}
