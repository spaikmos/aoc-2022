fun main() {
    fun parseInput(input: List<String>): ArrayDeque<Pair<Int, Long>> {
        return ArrayDeque(input.mapIndexed{ idx, it -> Pair(idx, it.toLong())}.toCollection(ArrayDeque()))
    }
    fun part1(input: ArrayDeque<Pair<Int, Long>>): Long {
        // Iterate over each element in the list
        for (i in 0 until input.size){
            // Find the element that has the original idx (i)
            val elem = input.find { it.first == i }
            var idx = input.indexOf(elem).toLong()
            val cur = input.removeAt(idx.toInt())
            idx += cur.second
            idx = idx.mod(input.size.toLong())
            if (idx == 0L) {
                idx = input.size.toLong()
            }
            input.add(idx.toInt(), cur)
        }

        val zeroElement = input.find { it.second == 0L }
        val zeroIdx = input.indexOf(zeroElement)
        var ans = input.get((1000+zeroIdx).mod(input.size)).second
        ans += input.get((2000+zeroIdx).mod(input.size)).second
        ans += input.get((3000+zeroIdx).mod(input.size)).second
        return ans
    }

    fun part2(input: ArrayDeque<Pair<Int, Long>>): Long {
        val decryptKey = 811589153L
        for (i in 0 until input.size) {
            val cur = input.removeAt(i)
            val newCur = Pair(cur.first, cur.second*decryptKey)
            input.add(i, newCur)
        }
        for (j in 0 until 10) {
            for (i in 0 until input.size){
                // Find the element that has the original idx (i)
                val elem = input.find { it.first == i }
                var idx = input.indexOf(elem).toLong()
                val cur = input.removeAt(idx.toInt())
                idx += cur.second
                idx = idx.mod(input.size.toLong())
                if (idx == 0L) {
                    idx = input.size.toLong()
                }
                input.add(idx.toInt(), cur)
            }
        }

        val zeroElement = input.find { it.second == 0L }
        val zeroIdx = input.indexOf(zeroElement)
        var ans = input.get((1000+zeroIdx).mod(input.size)).second
        ans += input.get((2000+zeroIdx).mod(input.size)).second
        ans += input.get((3000+zeroIdx).mod(input.size)).second
        return ans
    }

    val input = readInput("../input/Day20")
    var inputArray = parseInput(input)
    println(part1(inputArray)) // 2275
    inputArray = parseInput(input)
    println(part2(inputArray)) // 4090409331120
}
