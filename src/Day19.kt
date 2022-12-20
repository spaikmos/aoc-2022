fun main() {
    fun parseInput(input: List<String>): MutableList<List<Int>> {
        val output = mutableListOf<List<Int>>()
        for (i in input) {
            val regex = """Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian""".toRegex()
            val (num, ore, clay, obOre, obClay, geodeOre, geodeClay) = regex.find(i)!!.destructured
            output.add(listOf(num, ore, clay, obOre, obClay, geodeOre, geodeClay).map{it.toInt()})
        }
        return output
    }

    //Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 4 ore. Each obsidian robot costs 2 ore and 7 clay. Each geode robot costs 4 ore and 13 obsidian.
    fun part1(input: MutableList<List<Int>>): Int {
        val END_TIME = 24
        var scores = mutableListOf<Int>()
        for (bp in input) {
            val (num, ore, clay, obOre, obClay) = bp
            val geoOre = bp[5]
            val geoObs = bp[6]
            //println("num: $num, ore: $ore, clay: $clay, obOre: $obOre, obClay: $obClay, geoOre: $geoOre, geoObs: $geoObs")
            var state = mutableListOf(1, 0, 0, 0, 0, 0, 0, 0)  // robots(ore, clay, obsidian, geode) resources(o, c, o, g)
            val q = ArrayDeque<MutableList<Int>>()
            q.add(state)
            for (i in 1..END_TIME) {
                val numInQ = q.size
                repeat(numInQ) {
                    state = q.removeFirst()
                    val robots = state.subList(0, 4)
                    var oldResources = state.subList(4, 8)
                    var newResources = robots.zip(oldResources) { a, b -> a + b }
                    // Use resources to build new robots
                    while ((oldResources[0] > geoOre) && (oldResources[2] > geoObs)) {

                    }


                    // Calculate resources built by current robots


                }

            }



        }



        return 0
    }

    fun part2(input: List<String>): Int {
        val state = mutableListOf(1, 2, 3, 4, 5, 6, 7,8)  // robots(ore, clay, obsidian, geode) resources(o, c, o, g)
        val sub1 = state.subList(0, 4)
        val sub2 = state.subList(4, 8)
        val sub3 = sub1.zip(sub2) { a, b -> a + b }
        println(sub1)
        println(sub2)
        println(sub3)
        return 0
    }

    val input = readInput("../input/Day19")
    val blueprint = parseInput(input)
    println(part1(blueprint))
    println(part2(input))
}
