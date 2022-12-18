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
        var nodesWithFlow = Node.nodes.values.filter{it -> it.flowRate > 0}.toMutableList()
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
                        m.put(curNode.name, i)
                        val startName = startNode.name
                        val destName = curNode.name
                        val flow = curNode.flowRate
                        println("start: $startName, dest: $destName, dist: $i, flow: $flow")
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
            val name = startNode.name
            val num = m.size
            //println("name: $name, num: $num")
        }
    }

    fun part1(input: List<String>): Int {
        // Do a BFS to maximize amount of pressure released
        var numNodesExamined = 0
        var maxScore = 0
        val q = ArrayDeque<Pair<MutableList<Node>, Array<Int>>>()
        q.add(Pair(mutableListOf(Node.nodes["AA"]!!), arrayOf(1, 0)))
        while (q.size > 0) {
            numNodesExamined++
            val curElem = q.removeFirst()
            val visitedNodes = curElem.first
            val curNode = curElem.first.last()
            val curTime = curElem.second[0]
            val curScore = curElem.second[1] + curNode.flowRate * (31 - curTime)
            val neighbors = Node.flowMap[curNode.name]
            var routeEnd = true

            for (nextNode in Node.flowMap.keys.map{it -> Node.nodes[it]!!}) {
                if (visitedNodes.contains(nextNode) == false) {
                    // try visiting this node
                    val newTime = curTime + neighbors!![nextNode.name]!!
                    if (newTime <= 30) {
                        val newList = visitedNodes.toMutableList()
                        newList.add(nextNode)
                        q.add(Pair(newList, arrayOf(newTime, curScore)))
                        routeEnd = false
                    }
                }
            }

            if (routeEnd && (maxScore < curScore)) {
                maxScore = curScore
                println(visitedNodes.map { it.name })
            }
        }
        println("Examined: $numNodesExamined")
        return maxScore
    }

    fun part2(input: List<String>): Int {
        // Now do a BFS to maximize amount of pressure released for two people
        var numNodesExamined = 0
        var maxScore = 0
        val q = ArrayDeque<Pair<MutableList<Node>, Array<Int>>>()
        q.add(Pair(mutableListOf(Node.nodes["AA"]!!), arrayOf(1, 1, 0)))
        while (q.size > 0) {
            numNodesExamined++
            val curElem = q.removeFirst()
            val visitedNodes = curElem.first
            val curA = curElem.first.first()
            val curB = curElem.first.last()
            var timeA = curElem.second[0]
            var timeB = curElem.second[1]
            val isNodeA = timeA < timeB
            var curNode = curB
            var curTime = timeB
            var routeEnd = true

            if (isNodeA) {
                curNode = curA
                curTime = timeA
            }
            val curScore = curElem.second[2] + curNode.flowRate * (27 - curTime)
            val neighbors = Node.flowMap.get(curNode.name)

            for (nextNode in Node.flowMap.keys.map{it -> Node.nodes.get(it)!!}) {
                if (visitedNodes.contains(nextNode) == false) {
                    // try visiting this node
                    val newTime = curTime + neighbors!!.get(nextNode.name)!!
                    if (newTime <= 26) {
                        val newList = visitedNodes.toMutableList()
                        if (isNodeA) {
                            newList.add(0, nextNode)
                            timeA = newTime
                        } else {
                            newList.add(nextNode)
                            timeB = newTime
                        }
                        q.add(Pair(newList, arrayOf(timeA, timeB, curScore)))
                        routeEnd = false
                    }
                }
            }
            if (routeEnd) {
                if (isNodeA) {
                    curElem.second[0] = 30
                } else {
                    curElem.second[1] = 30
                }
                if ((curElem.second[0] == 30) && (curElem.second[1] == 30)) {
                    if (maxScore < curScore) {
                        maxScore = curScore
                        println("maxScore: $maxScore")
                        println(visitedNodes.map{it.name})
                    }
                } else {
                    // The other path hasn't finished so add it to the queue.
                    q.add(curElem)
                }

            }
        }
        println("Examined: $numNodesExamined")
        return maxScore
    }

    val input = readInput("../input/Day16e")
    parseInput(input)
    println(part1(input))   // 1647
    println(part2(input))   // Still haven't completed
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

    val neighborMap: MutableMap<String, Node>
    var seen = false

    init {
        neighborMap = mutableMapOf()
        nodes.put(name, this)
        for (n in neighborList) {
            val nNode = nodes.get(n)
            if (nNode != null) {
                neighborMap.put(n, nNode)
                nNode.neighborMap.put(name, this)
            }
        }
    }
}