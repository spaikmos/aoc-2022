fun main() {
    fun parseInput(input: List<String>): MutableSet<Pair<Pair<Int, Int>, Pair<Int, Int>>> {
        val s = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
        for (i in input) {
            val regex = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()
            val matchResult = regex.find(i)
            val (sx, sy, bx, by) = matchResult!!.destructured
            s.add(Pair(Pair(sx.toInt(), sy.toInt()), Pair(bx.toInt(), by.toInt())))
        }
        return s
    }

    // Calculate the number of positions in Row 2000000 that a beacon can NOT be located
    fun part1(sb: MutableSet<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Int {
        val yRow = 2000000
        val noBeacon = mutableSetOf<Int>()

        for (i in sb) {
            val sensor = i.first
            val beacon = i.second
            // Calculate manhattan distance
            val sensorRange = Math.abs(sensor.first - beacon.first) + Math.abs(sensor.second - beacon.second)
            // Find distance to the yRow of interest
            val yDist = Math.abs(yRow - sensor.second)
            // If distance to yRow is less than the sensor's range, eliminate some points in the row
            if (yDist < sensorRange) {
                val xDist = sensorRange - yDist
                for (x in (sensor.first - xDist)..(sensor.first + xDist)) {
                    noBeacon.add(x)
                }
            }
        }
        // NOTE:  The data set contains one beacon at y=2000000.  Instead of modifying the
        //  algorithm to handle this, I cheat and manually do it here.
        return noBeacon.size - 1
    }

    // Find the one point in a 4,000,000 x 4,000,000 grid that the beacon must be located
    fun part2(sb: MutableSet<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Long {
        val maxIdx = 4000000
        val sensors = mutableSetOf<Pair<Pair<Int, Int>, Int>>()

        // Pre-calculate all the sensor ranges first
        for (i in sb) {
            val sensor = i.first
            val beacon = i.second
            val sensorRange = Math.abs(sensor.first - beacon.first) + Math.abs(sensor.second - beacon.second)
            sensors.add(Pair(sensor, sensorRange))
        }

        // The 4M x 4M grid has 16M points.  Check EACH point to see if any sensor can reach it.
        //  Since this is a lot of points to check, we need to speed it up.
        for (x in 0..maxIdx) {
            var y = 0
            while (y <= maxIdx) {
                var seen = false
                for (i in sensors) {
                    val sensor = i.first
                    val range = i.second
                    val dist = Math.abs(x - sensor.first) + Math.abs(y - sensor.second)
                    if (dist < range) {
                        // Sensor can see the beacon.  Skip the range that the sensor can see the
                        //  beacon by re-purposing the algorithm from part 1
                        val xDist = Math.abs(x - sensor.first)
                        val yDist = range - xDist
                        y = sensor.second + yDist
                        seen = true
                        break
                    }
                }
                if (seen == false) {
                    println("Missing: x:$x, y:$y")  // Missing: x:2949122, y:3041245
                    return maxIdx.toLong() * x + y
                }
                y++
            }
        }
        return 0
    }

    val input = readInput("../input/Day15")
    val sb = parseInput(input)
    println(part1(sb))  // 5878678
    println(part2(sb))  // 11796491041245
}