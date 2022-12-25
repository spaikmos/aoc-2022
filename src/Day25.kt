fun main() {
    fun snafuToDecimal(snafu: String): Long {
        var num = 0L
        for (c in snafu) {
            num *= 5
            when (c) {
                '2' -> num += 2
                '1' -> num += 1
                '0' -> num += 0
                '-' -> num -= 1
                '=' -> num -= 2
            }
        }
        return num
    }
    fun decimalToSnafu(num: Long): String {
        var snafu = ""
        var total = 0L
        var digit = 5L
        // The max number that can be represented is 3 times the MSB - 1
        while ((digit * 3L) < num) {
            digit *= 5L
        }
        val snafuChars = "=-012"
        while (digit != 0L) {
            // Generate list of values for this digit
            val values = arrayListOf(-2L, -1L, 0L, 1L, 2L).map{ it * digit }
            // Calculate the values when this digit is added to the string
            val diff = values.map{ it -> Math.abs(it + total - num) }
            // Find the minimum difference from the desired number.  This is the digit to use.
            val minIdx = diff.indexOf(diff.min())
            snafu += snafuChars[minIdx]
            total += values[minIdx]
            digit /= 5L
        }
        return snafu
   }
    fun part1(input: List<String>): String {
        var total = 0L
        for (i in input) {
            val num = snafuToDecimal(i)
            total += num
        }
        return decimalToSnafu(total)
    }

    val input = readInput("../input/Day25")
    println(part1(input))   // 2=112--220-=-00=-=20
}
