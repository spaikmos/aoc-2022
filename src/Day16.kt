fun main() {
    fun parseInput(input: List<String>) {
        for (i in input) {
            val regexNum = """rate=(\d+)""".toRegex()
            val regexNames = """([A-Z]{2})""".toRegex()
            val (flowRate) = regexNum.find(i)!!.destructured
            val names = regexNames.findAll(i)
            val it = names.iterator()
            val (curName) = it.next().destructured
            val neighbors = mutableListOf<String>()
            while (it.hasNext()) {
                val (n) = it.next().destructured
                neighbors.add(n)
            }
            Node(curName, flowRate.toInt(), neighbors)
        }

        // Find the nodes that have flowRate > 0
        val nodesWithFlow = Node.nodes.values.filter{it -> it.flowRate > 0}.toMutableList()
        nodesWithFlow.add(Node.nodes.get("AA")!!)

        // Build a map of each node with flowRate > 1
        val numDest = nodesWithFlow.size - 1
        for (startNode in nodesWithFlow) {
            // Do a BFS for each start node
            Node.clearSeen()
            startNode.seen = true
            val q = ArrayDeque<Node>()
            q.add(startNode)
            val m = mutableMapOf<String, Int>()
            var nodeCnt = 1
            if (startNode.name == "AA") {
                nodeCnt = 0
            }
            var i = 0
            while (nodeCnt < numDest) {
                i++
                val numQ = q.size
                // Iterate over each node in the current queue
                repeat(numQ) {
                    val curNode = q.removeFirst()
                    if (curNode.flowRate > 0 && i > 1) {
                        m[curNode.name] = i
                        //println("start: ${startNode.name}, dest: ${curNode.name}, dist: $i, flow: ${curNode.flowRate}")
                        nodeCnt++
                    }
                    for (nextNode in curNode.neighborMap.values) {
                        if (nextNode.seen == false) {
                            nextNode.seen = true
                            q.add(nextNode)
                        }
                    }
                }
            }
            Node.flowMap.put(startNode.name, m)
        }
    }

    fun part1(input: List<String>): Int {
        // Do a BFS to maximize amount of pressure released
        var numNodesExamined = 0
        var maxScore = 0
        val q = ArrayDeque<Pair<MutableList<Node>, Array<Int>>>()
        q.add(Pair(mutableListOf(Node.nodes["AA"]!!), arrayOf(0, 0)))
        while (q.size > 0) {
            numNodesExamined++
            val curElem = q.removeFirst()
            val visitedNodes = curElem.first
            val curNode = curElem.first.last()
            val curTime = curElem.second[0]
            val curScore = curElem.second[1] + curNode.flowRate * (30 - curTime)
            val neighbors = Node.flowMap[curNode.name]
            var routeEnd = true

            for (nextNode in Node.flowMap.keys.map{it -> Node.nodes[it]!!}) {
                if (!visitedNodes.contains(nextNode)) {
                    // try visiting this node
                    val newTime = curTime + neighbors!![nextNode.name]!!
                    if (newTime < 30) {
                        val newList = visitedNodes.toMutableList()
                        newList.add(nextNode)
                        q.add(Pair(newList, arrayOf(newTime, curScore)))
                        routeEnd = false
                    }
                }
            }

            if (routeEnd && (maxScore < curScore)) {
                maxScore = curScore
                //println(visitedNodes.map { it.name })
            }
        }
        println("Examined: $numNodesExamined nodes")
        return maxScore
    }
    fun part2(input: List<String>): Int {
        // Now do a BFS to maximize amount of pressure released for two people
        var numNodesExamined = 0
        var maxScore = 0
        val q = ArrayDeque<Pair<String, Array<Int>>>()
        q.add(Pair("AA", arrayOf(0, 0, 0)))
        while (q.size > 0) {
            numNodesExamined++
            val curElem = q.removeFirst()
            val visitedNodes = curElem.first.chunked(2)
            val curA = Node.nodes[visitedNodes.first()]
            val curB = Node.nodes[visitedNodes.last()]
            var (timeA, timeB, curScore) = curElem.second
            val isNodeA = timeA < timeB
            var curNode = curB
            var curTime = timeB
            var routeEnd = true

            if (isNodeA) {
                curNode = curA
                curTime = timeA
            }
            curScore += curNode!!.flowRate.times((26 - curTime))
            val neighbors = Node.flowMap.get(curNode?.name)

            for (nextNode in Node.flowMap.keys.map{it -> Node.nodes[it]!!}) {
                if (!visitedNodes.contains(nextNode.name)) {
                    // try visiting this node
                    val destTime = neighbors!![nextNode.name]!!
                    val newTime = curTime + destTime
                    // The BFS grows quickly and runs out of memory.  To mitigate this, do not
                    //  follow any paths where the destTime takes 10+ minutes.  This cuts down the
                    //  BFS queue size and allows the computer to compute the answer.  I chose 10
                    //  minutes because the furthest node from the origin (AA) is 9 min away.
                    if ((newTime < 26) && (destTime < 10)) {
                        var newString: String
                        if (isNodeA) {
                            newString = nextNode.name + curElem.first
                            timeA = newTime
                        } else {
                            newString = curElem.first + nextNode.name
                            timeB = newTime
                        }
                        q.add(Pair(newString, arrayOf(timeA, timeB, curScore)))
                        routeEnd = false
                    }
                }
            }
            if (routeEnd) {
                if (isNodeA) {
                    timeA = 30
                } else {
                    timeB = 30
                }
                if ((timeA == 30) && (timeB == 30)) {
                    if (maxScore < curScore) {
                        maxScore = curScore
                        println("maxScore: $maxScore, queue size: ${q.size}")
                    }
                } else {
                    // The other path hasn't finished so add it to the queue.
                    q.add(Pair(curElem.first, arrayOf(timeA, timeB, curScore)))
                }
            }
        }
        println("Examined: $numNodesExamined nodes")
        return maxScore
    }


    val input = readInput("../input/Day16")
    parseInput(input)
    println(part1(input))   // 1647
    println(part2(input))   // 2169
}

class Node(val name: String, val flowRate: Int, val neighborList: List<String>) {
    // Create class static variables
    companion object {
        val flowMap = mutableMapOf<String, Map<String, Int>>()
        val nodes = mutableMapOf<String, Node>()
        fun clearSeen() {
            for (n in nodes.values) {
                n.seen = false
            }
        }
    }

    val neighborMap: MutableMap<String, Node> = mutableMapOf()
    var seen = false

    init {
        nodes[name] = this
        for (n in neighborList) {
            val nNode = nodes[n]
            if (nNode != null) {
                neighborMap[n] = nNode
                nNode.neighborMap[name] = this
            }
        }
    }
}