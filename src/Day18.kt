fun main() {
    fun parseInput(input: List<String>): MutableList<List<Int>> {
        val lava = mutableListOf<List<Int>>()
        for (i in input) {
            val a = i.split(',').map{ it -> it.toInt()}
            lava.add(a)
        }
        return lava
    }

    fun part1(input: MutableList<List<Int>>): Int {
        var exposedFaces = 0
        val points = mutableSetOf<List<Int>>()

        fun checkPoint(x: Int, y: Int, z: Int) {
            if (points.contains(mutableListOf(x, y, z)) == true) {
                // Rock is detected, remove two faces from total
                exposedFaces -= 2
            }
        }

        for (i in input) {
            val (x, y, z) = i
            // Each new block initially adds 6 faces.  When a block is next to another block, two
            //  faces are touching and need to be removed.
            exposedFaces += 6
            checkPoint(x+1, y, z)
            checkPoint(x-1, y, z)
            checkPoint(x, y+1, z)
            checkPoint(x, y-1, z)
            checkPoint(x, y, z+1)
            checkPoint(x, y, z-1)
            points.add(i)
        }
        return exposedFaces
    }

    fun part2(input: MutableList<List<Int>>): Int {
        val points = input.toList()
        val empty = mutableSetOf<List<Int>>()

        // Start at (0,0,0) and do a BFS to find all faces accessible from outside the lava
        val q = ArrayDeque<List<Int>>()
        var numEmptyFaces = 0

        // Helper function that checks each new point and adds to the q as needed
        fun checkPoint(x:Int, y:Int, z:Int) {
            val MIN = -1
            val MAX = 20
            if ((x in MIN..MAX) && (y in MIN..MAX) && (z in MIN..MAX)) {
                val newPoint = listOf(x,y,z)
                when (newPoint) {
                  in points -> numEmptyFaces++
                  !in empty -> {q.add(newPoint); empty.add(newPoint)}
                }
            }
        }

        q.add(listOf(0,0,0))
        while (q.size > 0) {
            val (x,y,z) = q.removeFirst()
            checkPoint(x+1, y, z)
            checkPoint(x-1, y, z)
            checkPoint(x, y+1, z)
            checkPoint(x, y-1, z)
            checkPoint(x, y, z+1)
            checkPoint(x, y, z-1)
        }
        return numEmptyFaces
    }

    val input = readInput("../input/Day18")
    val points = parseInput(input)
    println(part1(points))  // 3390
    println(part2(points))  // 2058
}
