fun main() {
    fun getScore(input: Char) : Int {
        val value: Int
        if(input > 'Z') {
            value = input - 'a' + 1
        } else {
            value = input - 'A' + 27
        }
        return value
    }

    fun part1(input: List<String>): Int {
        var total = 0
        for (i in input) {
            val str1 = i.substring(0, i.length/2)
            val str2 = i.substring(i.length/2, i.length)
            val inter = str1.toCharArray().intersect(str2.toCharArray().toList()).toCharArray()[0]
            total += getScore(inter)
        }
        return total
    }

    fun part2(input: List<String>): Int {
        var it = input.iterator()
        var total = 0
        while (it.hasNext()) {
            val str1 = it.next()
            val str2 = it.next()
            val str3 = it.next()
            var inter = str1.toCharArray().intersect(str2.toCharArray().toList())
            inter = (inter.toSet()).intersect(str3.toCharArray().toList())
            total += getScore(inter.toCharArray()[0])
        }
        return total
    }

    val input = readInput("../input/Day03")
    println(part1(input))
    println(part2(input))
}
