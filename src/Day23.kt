fun main() {
    // Use a static array to keep track of elf positions.  This requires an offset of the origin
    //  (0, 0) so that elves can move in negative directions.  I started with much larger values
    //  and tuned this to be smaller.
    val OFFSET = 20
    val DIM = 150
    // Create a set of (x,y) points for each '#' that denotes elf position
    fun parseInput(input: List<String>): MutableSet<Pair<Int, Int>> {
        val elves = mutableSetOf<Pair<Int, Int>>()
        for (string in input) {
            val y = input.indexOf(string) + OFFSET
            for (x in string.indices) {
                if (string[x] == '#') {
                    elves.add(Pair(x + OFFSET, y))
                }
            }
        }
        return elves
    }
    // Originally used set.intersect() to check elf moves, but it's inefficient.  Instead, use
    //  a map of current elf positions to make each checkX function faster.
    fun createElfMap(elves: MutableSet<Pair<Int, Int>>): Array<Array<Boolean>> {
        val elfMap = Array(DIM) {Array(DIM) {false} }
        for (elf in elves) {
            elfMap[elf.first][elf.second] = true
        }
        return elfMap
    }
    fun findVoids(elves: MutableSet<Pair<Int, Int>>, round: Int): Int {
        // Calculate the minimum rectangle dimensions the elves are in
        var (minX, minY, maxX, maxY) = arrayOf(DIM, DIM, 0, 0)
        for (elf in elves) {
            minX = kotlin.math.min(minX, elf.first)
            maxX = kotlin.math.max(maxX, elf.first)
            minY = kotlin.math.min(minY, elf.second)
            maxY = kotlin.math.max(maxY, elf.second)
        }
        val area = (maxX - minX + 1) * (maxY - minY + 1)
        val ans = area - elves.size
        //println("Round: $round, minX: $minX, maxX: $maxX, minY: $minY, maxY: $maxY, area: $area, ans: $ans")
        return ans
    }
    fun checkElves(checkEmpty: Array<Pair<Int, Int>>, elfMap: Array<Array<Boolean>>): Pair<Int, Int>? {
        for (pos in checkEmpty) {
            if (elfMap[pos.first][pos.second]) {
                return null
            }
        }
        // The first element contains the position that the elf will move to
        return checkEmpty[0]
    }
    fun moveN(elf: Pair<Int, Int>, elfMap: Array<Array<Boolean>>): Pair<Int, Int>? {
        val n = Pair(elf.first, elf.second-1)
        val nw = Pair(elf.first-1, elf.second-1)
        val ne = Pair(elf.first+1, elf.second-1)
        return checkElves(arrayOf(n, nw, ne), elfMap)
    }
    fun moveE(elf: Pair<Int, Int>, elfMap: Array<Array<Boolean>>): Pair<Int, Int>? {
        val e = Pair(elf.first+1, elf.second)
        val ne = Pair(elf.first+1, elf.second+1)
        val se = Pair(elf.first+1, elf.second-1)
        return checkElves(arrayOf(e, ne, se), elfMap)
    }
    fun moveS(elf: Pair<Int, Int>, elfMap: Array<Array<Boolean>>): Pair<Int, Int>? {
        val s = Pair(elf.first, elf.second+1)
        val sw = Pair(elf.first-1, elf.second+1)
        val se = Pair(elf.first+1, elf.second+1)
        return checkElves(arrayOf(s, sw, se), elfMap)
    }
    fun moveW(elf: Pair<Int, Int>, elfMap: Array<Array<Boolean>>): Pair<Int, Int>? {
        val w = Pair(elf.first-1, elf.second)
        val nw = Pair(elf.first-1, elf.second+1)
        val sw = Pair(elf.first-1, elf.second-1)
        return checkElves(arrayOf(w, nw, sw), elfMap)
    }

    fun part1(elves: MutableSet<Pair<Int, Int>>): Int {
        val elfMap = createElfMap(elves)
        val nextMoves = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        var numRounds = 0
        val numVoids = mutableListOf<Int>()
        do {
            nextMoves.clear()
            // Determine where each elf will move (if possible)
            for (elf in elves) {
                val moves = listOf(moveN(elf, elfMap), moveS(elf, elfMap), moveW(elf, elfMap), moveE(elf, elfMap))
                if (moves.contains(null) && moves.filterNotNull().isNotEmpty()) {
                    // Elf moves when there's at least 1 elf nearby and a valid move is possible.
                    //  The move list must have at least 1 null (this means there's another elf
                    //  nearby that prevents our current elf from moving) and there must also be a
                    //  not null move so the current elf CAN move to a new square.
                    for (j in 0 until 4) {
                        // Elf starts looking from a different direction each round.  Add the offset
                        //  based on numRounds and use modulo to determine the order of directions
                        //  to check.  Pick the first valid move.
                        val idx = (numRounds + j).mod(4)
                        if (moves[idx] != null) {
                            if (nextMoves.contains(moves[idx])) {
                                // Another elf plans to move here.  They will collide so delete the
                                //  move and neither elf will move.  Note that only two elves can
                                //  try to move to the same location; it's impossible for 3 or 4
                                //  elves to attempt to move into the same square.  Thus, an "XOR"
                                //  is sufficient for tracking (and removing) elf moves.
                                nextMoves.remove(moves[idx])
                            } else {
                                // This elf wants to move to the new position
                                nextMoves[moves[idx]!!] = elf
                            }
                            break
                        }
                    }
                }
            }
            // Resolve the moves by updating the elfMap and elf positions
            for (move in nextMoves) {
                elves.remove(move.value)
                elves.add(move.key)
                // Update the elfMap
                elfMap[move.value.first][move.value.second] = false
                elfMap[move.key.first][move.key.second] = true
            }
            // Increment the starting looking direction
            numRounds++
            numVoids.add(findVoids(elves, numRounds))
        } while (nextMoves.isNotEmpty())
        println("Part 1:  Empty spaces after 10 rounds - ${numVoids[9]}")  // 4005
        println("Part 2:  Rounds until no elf moves    - $numRounds")      // 1008
        return numRounds
    }
    val input = readInput("../input/Day23")
    val elves = parseInput(input)
    part1(elves)   // 4005, 1008
}