fun main() {
    fun calcAns(numRuns: Int, isPart1: Boolean): Long {
        repeat(numRuns) {
            Monkey.monkeys.map{ it.Run(isPart1) }
        }
        return Monkey.monkeys.map{ it.numItemsInspected }.sortedDescending().take(2).reduce{ tot, it -> it * tot }
    }

    val input = readInput("../input/Day11")
    // Parse input into monkey class
    parseInput(input)
    println(calcAns(20, true))  // 101436
    println(calcAns(10_000, false))  // 19754471646

}

fun parseInput(input: List<String>) {
    for (i in input.chunked(7)) {
        val items = i[1].substring(18).split(", ").map { it.toLong() }
        val opChar = i[2][23]
        val opNum = i[2].substring(25)
        val test = i[3].filter{ it.isDigit() }.toInt()
        val ifTrue = i[4][29] - '0'
        val ifFalse = i[5][30] - '0'
        var op = Operation.SQUARE
        var num = 0

        when (opChar) {
            '*' -> op = Operation.MULTIPLY
            '+' -> op = Operation.PLUS
        }
        if (opNum[0].isDigit()) {
            num = opNum.toInt()
        } else {
            op = Operation.SQUARE
        }
        //println(i)
        //println("items: $items opChar: $opChar opNum: $opNum test: $test true: $ifTrue false: $ifFalse op: $op num: $num")
        Monkey(op, num, test, ifTrue, ifFalse, items.toMutableList())
    }
}

class Monkey(var op: Operation, var num: Int, var test: Int, var ifTrue: Int, var ifFalse: Int, var items: MutableList<Long>) {
    companion object {
        var monkeys = ArrayList<Monkey>()
        var divider = 1L
    }
    var numItemsInspected = 0L

    init {
        monkeys.add(this)
        divider *= test
    }

    fun Run (isPart1: Boolean) {
        for (a in items) {
            var i = a
            when (op) {
                Operation.PLUS -> i += num
                Operation.MULTIPLY -> i *= num
                Operation.SQUARE -> i *= a
            }

            // Hack to perform different worry operations based on part 1 vs part 2
            if (isPart1) {
                // In part 1, worry function is item score floor(item.mod(3)).  Integer divide!
                i /= 3
            } else {
                // In part 2, worry function divides item score by the product of all the tests
                i = i.mod(divider)
            }

            when (i.mod(test)) {
                0 -> monkeys[ifTrue].items.add(i)
                else -> monkeys[ifFalse].items.add(i)
            }
            numItemsInspected++
        }
        items.clear()
    }
}

enum class Operation {
    PLUS, MULTIPLY, SQUARE
}