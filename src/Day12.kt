import java.util.Deque

fun main() {
    fun part1(input: List<String>): Int {
        val yLen = input[0].length
        val xLen = input.size

        val visited = Array(xLen) { IntArray(yLen){0} }
        val nodes = input.map{it.toCharArray()}
        val end = nodes.find { it.contains('E') }
        val endX = nodes.indexOf(end)
        val endY = end!!.indexOf('E')
        val start = nodes.find { it.contains('S') }
        val startX = nodes.indexOf(start)
        val startY = start!!.indexOf('S')
        println("startX: $startX, startY: $startY")
        println("xLen: $xLen, yLen: $yLen")

        var numSteps = 0
        var numNodes = 0
        val q = ArrayDeque<Pair<Int, Int>>()
        q.add(Pair(startX, startY))
        nodes[startX][startY] = 'a'
        visited[startX][startY] = 1

        fun checkNode(x: Int, y: Int, nextVal: Char) {
            if ((x >= 0) && (x < xLen) && (y >=0) && (y < yLen)) {
                val nodeVal = nodes[x][y]
                if (visited[x][y] == 0) {
                    if ((nextVal == 'z' && nodeVal == 'E') || (nodeVal <= nextVal)) {
                        q.addLast(Pair(x, y))
                        visited[x][y] = 1
                    }
                }
            }
        }

        while (q.size > 0) {
            val numElem = q.size
            repeat(numElem) {
                var node = q.removeFirst()
                val nextVal = nodes[node.first][node.second] + 1
                if (nextVal == 'F') {
                    return numSteps
                }
                // Check if adjacent nodes can be added to queue
                checkNode(node.first+1, node.second, nextVal)
                checkNode(node.first-1, node.second, nextVal)
                checkNode(node.first, node.second+1, nextVal)
                checkNode(node.first, node.second-1, nextVal)
                numNodes++
            }
            numSteps++
            var numQ = q.size
            println("step: $numSteps nodes: $numNodes qSize: $numQ")
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        val yLen = input[0].length
        val xLen = input.size

        val nodes = input.map{it.toCharArray()}
        val start = nodes.find { it.contains('S') }
        var startX = nodes.indexOf(start)
        var startY = start!!.indexOf('S')
        nodes[startX][startY] = 'a'
        var minSteps = 1000

        startX = 0
        startY = 0
        repeat (xLen) {
            repeat (yLen) {
                if (nodes[startX][startY] == 'a') {
                    val visited = Array(xLen) { IntArray(yLen){0} }
                    var numSteps = 0
                    var numNodes = 0
                    val q = ArrayDeque<Pair<Int, Int>>()
                    q.add(Pair(startX, startY))
                    visited[startX][startY] = 1

                    fun checkNode(x: Int, y: Int, nextVal: Char) {
                        if ((x >= 0) && (x < xLen) && (y >=0) && (y < yLen)) {
                            val nodeVal = nodes[x][y]
                            if (visited[x][y] == 0) {
                                if ((nextVal == 'z' && nodeVal == 'E') || (nodeVal <= nextVal)) {
                                    q.addLast(Pair(x, y))
                                    visited[x][y] = 1
                                }
                            }
                        }
                    }

                    //println("startX: $startX startY: $startY")
                    while (q.size > 0) {
                        var numElem = q.size
                        var exit = false
                        while ((numElem > 0) && (exit == false )) {
                            var node = q.removeFirst()
                            val nextVal = nodes[node.first][node.second] + 1
                            if (nextVal == 'F') {
                                if (numSteps < minSteps) {
                                    minSteps = numSteps
                                    println("minSteps: $minSteps")
                                }
                                //q.clear()
                                exit = true
                            } else {
                                // Check if adjacent nodes can be added to queue
                                checkNode(node.first + 1, node.second, nextVal)
                                checkNode(node.first - 1, node.second, nextVal)
                                checkNode(node.first, node.second + 1, nextVal)
                                checkNode(node.first, node.second - 1, nextVal)
                                numNodes++
                            }
                            numElem--
                        }
                        numSteps++
                        var numQ = q.size
                        //println("step: $numSteps nodes: $numNodes qSize: $numQ")
                    }
                }
                startY++
            }
            startY = 0
            startX++
        }
        return minSteps
    }

    val input = readInput("../input/Day12")
    println(part1(input))  // 481
    println(part2(input))  // 480
}