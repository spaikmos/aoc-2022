import java.util.LinkedList


fun main() {
    // Ingest the list of input commands to generate file structure tree
    fun buildTree(input: List<String>): TreeNode {
        val rootDir = TreeNode("root", 0, FileType.DIR)
        var curDir = rootDir
        val it = input.iterator()

        while (it.hasNext()) {
            val i = it.next()
            if (i.startsWith("$ cd")) {
                val name = i.split("$ cd ")[1]
                if (name == "..") {
                    curDir = curDir.parent!!
                } else {
                    // Find the child directory node
                    for (node in curDir.children) {
                        if (node.name == name) {
                            curDir = node
                            break
                        }
                    }
                }
            } else if (i.startsWith("$ ls")) {
                // Do nothing.  Process the ls contents in the 'else' clause below
                continue
            } else {
                val entry = i.split(" ")
                if (entry[0] == "dir") {
                    // Add a new directory entry
                    curDir.addChild(entry[1], 0, FileType.DIR)
                } else {
                    // Add a new file entry
                    curDir.addChild(entry[1], entry[0].toInt(), FileType.FILE)
                }
            }
        }
        return rootDir
    }

    // Find the sum of all directories that have a size < 100000
    fun part1(root: TreeNode): Int {
        var totalSize = 0
        for (node in TreeNode.dirs) {
            if (node.size < 100000) {
                totalSize += node.size
            }
        }
        return totalSize
    }

    // Find the smallest directory that can be deleted to free up 30000000 bytes total
    fun part2(root: TreeNode): Int {
        val totalSize = 70000000
        val needSize = 30000000
        val deleteSize = needSize - (totalSize - root.size)
        var minDelete = root.size

        for (node in TreeNode.dirs) {
            if ((node.size < minDelete) && (node.size > deleteSize)) {
                minDelete = node.size
            }
        }
        return minDelete
    }

    val input = readInput("../input/Day07")
    val rootNode = buildTree(input)
    println(part1(rootNode))
    println(part2(rootNode))
}

enum class FileType {
    FILE, DIR
}

class TreeNode(var name: String, var size: Int, var type: FileType) {
    // Create class static variables
    companion object {
        var files = ArrayList<TreeNode>()
        var dirs = ArrayList<TreeNode>()
    }

    // Make this nullable only because the root node doesn't have a parent.  This can be avoided
    //  by defining a new FileType: ROOT and pointing the parent back to root, but that's a hack
    //  too...
    var parent: TreeNode? = null
    var children: MutableList<TreeNode>

    init {
        children = LinkedList()
    }

    fun addChild(name: String, size: Int, type: FileType) {
        // Check if child already exists
        for (node in children) {
            if (node.name == name) {
                val rootname = this.name
                println("ERROR:  child $name already exists in dir: $rootname, size: $size")
                return
            }
        }
        val childNode = TreeNode(name, size, type)
        childNode.parent = this
        children.add(childNode)
        if (type == FileType.FILE) {
            files.add(childNode)

            // Walk the dir structure to add filesize to each parent dir
            var curParent: TreeNode? = this
            while (curParent != null) {
                curParent.size += size
                curParent = curParent.parent
            }
        } else {
            dirs.add(childNode)
        }
    }
}
