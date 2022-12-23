fun main() {
    fun parseInput(input: List<String>): Triple<MutableList<String>, MutableList<String>, MutableList<Pair<Int, Char>>> {
        val cols = mutableListOf<String>()
        val rows = input.subList(0, 200).toMutableList()

        for (i in 0 until 150) {
            var col = ""
            for (row in rows) {
                if (row.length > i) {
                    col = col + row[i]
                }
            }
            cols.add(col)
        }
        // Create pairs of instructions
        val regex = """(\d+)([RLE])""".toRegex()
        val moves = mutableListOf<Pair<Int, Char>>()
        // Add a termination instruction to the string.  Use 'E'
        val values = regex.findAll(input[201] + "E")
        val it = values.iterator()
        while (it.hasNext()) {
            val (len, dir) = it.next().destructured
            moves.add(Pair(len.toInt(), dir[0]))
        }
        return Triple(rows, cols, moves)
    }

    fun part1(input: Triple<MutableList<String>, MutableList<String>, MutableList<Pair<Int, Char>>>): Int {
        val (rows, cols, moves) = input
        var curX = rows[0].indexOf('.')
        var curY = 0
        var curDir = DIR.RIGHT

        for (move in moves) {
            val (len, dir) = move
            for (i in 0 until len) {
                var nextX = curX
                var nextY = curY
                when (curDir) {
                    DIR.RIGHT -> nextX = curX + 1
                    DIR.LEFT -> nextX = curX - 1
                    DIR.UP -> nextY = curY - 1
                    DIR.DOWN -> nextY = curY + 1
                }
                var nextSpace = ' '
                try {
                    nextSpace = rows[nextY][nextX]
                } catch (e: IndexOutOfBoundsException) {
                    nextSpace = ' '
                }
                if (nextSpace == ' ') {
                    // Wrap the row
                    if (curDir == DIR.RIGHT) {
                        val line = rows[nextY]
                        for (i in 0 until line.length) {
                            if (line[i] != ' ') {
                                nextX = i
                                break
                            }
                        }
                    } else if (curDir == DIR.LEFT) {
                        val line = rows[nextY]
                        for (i in line.length-1 downTo 0) {
                            if (line[i] != ' ') {
                                nextX = i
                                break
                            }
                        }
                    } else if (curDir == DIR.DOWN) {
                        val line = cols[nextX]
                        for (i in 0 until line.length) {
                            if (line[i] != ' ') {
                                nextY = i
                                break
                            }
                        }
                    } else if (curDir == DIR.UP) {
                        val line = cols[nextX]
                        for (i in line.length-1 downTo 0) {
                            if (line[i] != ' ') {
                                nextY = i
                                break
                            }
                        }
                    }
                    nextSpace = rows[nextY][nextX]
                }

                if (nextSpace == '.') {
                    curX = nextX
                    curY = nextY
                    continue
                } else if (nextSpace == '#') {
                    // Need to stop
                    break
                }
            }
            // Stopped moving.  Process the direction
            if (dir == 'E') {
                println("End of moves found!")
                break
            }
            when (curDir) {
                DIR.RIGHT -> curDir = if (dir == 'R') DIR.DOWN else DIR.UP
                DIR.LEFT -> curDir = if (dir == 'R') DIR.UP else DIR.DOWN
                DIR.UP -> curDir = if (dir == 'R') DIR.RIGHT else DIR.LEFT
                DIR.DOWN -> curDir = if (dir == 'R') DIR.LEFT else DIR.RIGHT
            }
        }
        println("curX: $curX, curY: $curY, curDir: $curDir")
        var facing = 0
        when (curDir) {
            DIR.RIGHT -> facing = 0
            DIR.DOWN -> facing = 1
            DIR.LEFT -> facing = 2
            DIR.UP -> facing = 3
        }

        return 1000 * (curY+1) + 4 * (curX+1) + facing
    }

    fun part2(input: Triple<MutableList<String>, MutableList<String>, MutableList<Pair<Int, Char>>>): Int {
        val (rows, cols, moves) = input
        var curX = rows[0].indexOf('.')
        var curY = 0
        var curDir = DIR.RIGHT

        for (move in moves) {
            var nextDir = curDir
            val (len, dir) = move
            for (i in 0 until len) {
                var nextX = curX
                var nextY = curY
                when (curDir) {
                    DIR.RIGHT -> nextX = curX + 1
                    DIR.LEFT -> nextX = curX - 1
                    DIR.UP -> nextY = curY - 1
                    DIR.DOWN -> nextY = curY + 1
                }
                var nextSpace = ' '
                try {
                    nextSpace = rows[nextY][nextX]
                } catch (e: IndexOutOfBoundsException) {
                    nextSpace = ' '
                }
                if (nextSpace == ' ') {
                    // Wrap the row
                    if (curDir == DIR.RIGHT) {
                        if ((nextX == 50) && (nextY >=150) && (nextY < 200)) {
                            // 4R->6U
                            nextX = nextY - 100
                            nextY = 149
                            nextDir = DIR.UP
                        } else if (nextX == 100) {
                            if ((nextY >= 50) && (nextY < 100)) {
                                // 3R->2U
                                nextX = nextY + 50
                                nextY = 49
                                nextDir = DIR.UP
                            } else if ((nextY >= 100) && (nextY < 150)) {
                                // 6R->2L
                                nextX = 149
                                nextY = 149 - nextY
                                nextDir = DIR.LEFT
                            }
                        } else if ((nextX == 150) && (nextY >= 0) && (nextY < 50)) {
                            // 2R->6L
                            nextX = 99
                            nextY = 149 - nextY
                            nextDir = DIR.LEFT
                        }
                    } else if (curDir == DIR.LEFT) {
                        if (nextX == 49) {
                            if ((nextY >= 0) && (nextY < 50)) {
                                // 1L->5R
                                nextX = 0
                                nextY = 149 - nextY
                                nextDir = DIR.RIGHT
                            } else if ((nextY >= 50) && (nextY < 100)){
                                // 3L->5D
                                nextX = nextY - 50
                                nextY = 100
                                nextDir = DIR.DOWN
                            }
                        } else if ((nextX == -1) && (nextY >= 100) && (nextY < 150)) {
                            // 5L->1R
                            nextX = 50
                            nextY = 149 - nextY
                            nextDir = DIR.RIGHT
                        } else if ((nextX == -1) && (nextY >= 150) && (nextY < 200)){
                            // 4L->1D
                            nextX = nextY - 100
                            nextY = 0
                            nextDir = DIR.DOWN
                        } else {
                            println("INVALID WRAP: curDir: $curDir, x: $nextX, y: $nextY")
                        }
                    } else if (curDir == DIR.DOWN) {
                        if ((nextY == 50) && (nextX >= 100) && (nextX < 150)) {
                            // 2D->3L
                            nextY = nextX - 50
                            nextX = 99
                            nextDir = DIR.LEFT
                        } else if ((nextY == 150) && (nextX >= 50) && (nextX < 100)) {
                            // 6D->4L
                            nextY = nextX + 100
                            nextX = 49
                            nextDir = DIR.LEFT
                        } else if ((nextY == 200) && (nextX >= 0) && (nextX < 50)) {
                            // 4D->2D
                            nextX += 100
                            nextY = 0
                        }
                    } else if (curDir == DIR.UP) {
                        if (nextY == -1) {
                            if ((nextX >= 50) && (nextX < 100)) {
                                // 1U->4R
                                nextY = nextX + 100
                                nextX = 0
                                nextDir = DIR.RIGHT
                            } else if ((nextX >= 100) && (nextX < 150)){
                                // 2U->4U
                                nextX -= 100
                                nextY = 199
                            }
                        } else if ((nextX >= 0) && (nextX < 50) && (nextY == 99)){
                            // 5U->3R
                            nextY = nextX + 50
                            nextX = 50
                            nextDir = DIR.RIGHT
                        }
                    }
                    nextSpace = rows[nextY][nextX]
                }

                if (nextSpace == '.') {
                    curX = nextX
                    curY = nextY
                    curDir = nextDir
                    continue
                } else if (nextSpace == '#') {
                    // Need to stop
                    break
                }
            }
            // Stopped moving.  Process the direction
            if (dir == 'E') {
                println("End of moves found!")
                break
            }
            when (curDir) {
                DIR.RIGHT -> curDir = if (dir == 'R') DIR.DOWN else DIR.UP
                DIR.LEFT -> curDir = if (dir == 'R') DIR.UP else DIR.DOWN
                DIR.UP -> curDir = if (dir == 'R') DIR.RIGHT else DIR.LEFT
                DIR.DOWN -> curDir = if (dir == 'R') DIR.LEFT else DIR.RIGHT
            }
        }
        println("curX: $curX, curY: $curY, curDir: $curDir")
        var facing = 0
        when (curDir) {
            DIR.RIGHT -> facing = 0
            DIR.DOWN -> facing = 1
            DIR.LEFT -> facing = 2
            DIR.UP -> facing = 3
        }

        return 1000 * (curY+1) + 4 * (curX+1) + facing
    }

    val input = readInput("../input/Day22")
    val input2 = parseInput(input)
    println(part1(input2))  // 75254
    println(part2(input2))  // 108311
}

enum class DIR {
    UP, DOWN, LEFT, RIGHT
}
/*
                    1  11  1
               45  90  45  9
            0  90  90  90  9
0               11112222
                11112222
                11112222
49              11112222
50              3333
                3333
                3333
99              3333
100         55556666
            55556666
            55556666
149         55556666
150         4444
            4444
            4444
199         4444
x 149-> 150 when y[0-49] --> x[199] y[149-199

2D->3L  x=100-149, y=49    D maps to x=99,   y=50-99   L
2R->6L  x=149,     y=0-49  R maps to x=99,   y=149-100 L
3L->5D  x=50,      y=50-99 L maps to x=0-49, y=100     D
1L->5R  x=50,      y=0-49  L maps to x=0,    y=149-100 R
1U->4R  x=50-99,   y=0     U maps to x=0,    y=150-199 R
2U->4U  x=100-149, y=0     U maps to x=0-49  y=199     U
6D->4L  x=50-99,   y=149   D maps to x=49    y=150-199 L
*/
/*
RIGHT:
2D->3L  x=100-149, y=49    D maps to x=99,   y=50-99   L
2R->6L  x=149,     y=0-49  R maps to x=99,   y=149-100 L
6D->4L  x=50-99,   y=149   D maps to x=49    y=150-199 L
*/
/*
LEFT:
3L->5D  x=50,      y=50-99 L maps to x=0-49, y=100     D
1L->5R  x=50,      y=0-49  L maps to x=0,    y=149-100 R
1U->4R  x=50-99,   y=0     U maps to x=0,    y=150-199 R
*/
/*
UP:
3L->5D  x=50,      y=50-99 L maps to x=0-49, y=100     D
1U->4R  x=50-99,   y=0     U maps to x=0,    y=150-199 R
2U->4U  x=100-149, y=0     U maps to x=0-49  y=199     U
*/
/*
DOWN:
2D->3L  x=100-149, y=49    D maps to x=99,   y=50-99   L
2U->4U  x=100-149, y=0     U maps to x=0-49  y=199     U
6D->4L  x=50-99,   y=149   D maps to x=49    y=150-199 L
*/


