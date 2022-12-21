fun main() {
    fun parseInput(input: List<String>): Pair<MutableMap<String, Long>, MutableMap<String, List<String>>> {
        val nums = mutableMapOf<String, Long>()
        val ops = mutableMapOf<String, List<String>>()
        val regexNum = """(.{4}): (\d+)""".toRegex()
        val regexString = """(.{4}): (.{4}) (.) (.{4})""".toRegex()
        for (i in input) {
            // Length less than 16 means it's a value and not an operation
            if (i.length < 16) {
                val (name, value) = regexNum.find(i)!!.destructured
                nums.put(name, value.toLong())
            } else {
                val (name, arg1, arg2, arg3) = regexString.find(i)!!.destructured
                ops.put(name, listOf(arg1, arg2, arg3))
            }
        }
        return Pair(nums, ops)
    }

    fun part1(input: Pair<MutableMap<String, Long>, MutableMap<String, List<String>>>): Long {
        val nums = input.first
        val ops = input.second
        while (ops.size > 0) {
            for (op in ops) {
                val name = op.key
                val (arg1, operator, arg2) = op.value
                if (nums.contains(arg1) && nums.contains(arg2)) {
                    var value = 0L
                    val num1 = nums.get(arg1)!!
                    val num2 = nums.get(arg2)!!
                    when (operator) {
                        "+" -> value = num1 + num2
                        "-" -> value = num1 - num2
                        "*" -> value = num1 * num2
                        "/" -> value = num1 / num2
                    }
                    // Insert the new value into the nums map
                    nums[name] = value
                    // Ideally remove the element from this map so we don't continue to process the
                    //  same operation over and over again....
                    if (name == "root") {
                        return value
                    }
                }
            }
        }
        return 0L
    }

    fun part2(input: Pair<MutableMap<String, Long>, MutableMap<String, List<String>>>): Long {
        val nums = input.first
        val ops = input.second
        val humanList = ArrayDeque<String>()
        // Build a list of variables that are calculated using the humn value
        humanList.add("humn")
        var curString = "humn"
        while (curString != "root") {
            for (op in ops) {
                val name = op.key
                val (arg1, operator, arg2) = op.value
                if ((arg1 == curString) || (arg2 == curString)) {
                    humanList.add(name)
                    curString = name
                }
            }
        }
        // Unwind the operations.  From trial and error I know mwrd is the correct value.
        //  The last operation is "root: hsdb + mwrd" so start with hsdb as the prevString
        var value = nums.get("mwrd")!!
        curString = humanList.removeLast() // Remove root
        curString = humanList.removeLast() // Remove hsdb
        while (curString != "mnfl") {
            val prevString = curString
            curString = humanList.removeLast()
            val (arg1, operator, arg2) = ops.get(prevString)!!
            if (curString == arg1) {
                val num2 = nums.get(arg2)!!
                when (operator) {
                    "+" -> value -= num2
                    "-" -> value += num2
                    "*" -> value /= num2
                    "/" -> value *= num2
                }
            } else {
                val num1 = nums.get(arg1)!!
                when (operator) {
                    "+" -> value -= num1
                    "-" -> value = num1 - value
                    "*" -> value /= num1
                    "/" -> value = num1 / value
                }
            }
        }
        // The last operation using humn is "mnfl: cqgp + humn".  Undo this one too
        return value - nums.get("cqgp")!!
    }

    val input = readInput("../input/Day21")
    var input2 = parseInput(input)
    println(part1(input2))  // 124765768589550
    println(part2(input2))  // 3059361893920
}