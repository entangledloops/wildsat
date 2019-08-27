import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.measureTimeMillis

data class Literal(val index: Int, var negated: Boolean)
data class Clause(var literals: List<Literal>)

class SATSolver(numLiterals: Int, val clauses: List<Clause>)
{
    val random = Random(0)
    val literals = Array(numLiterals) { random.nextBoolean() }
    var iterations = 0

    override fun toString(): String = buildString {
        literals.forEach {
            when (it) {
                true -> append('T')
                else -> append('F')
            }
        }
    }

    fun satisfied(): Boolean = clauses.all { satisfied(it) }
    fun satisfied(clause: Clause): Boolean {
        clause.literals.forEach { literal ->
            val value = literals[literal.index]
            if ((value && !literal.negated) || (!value && literal.negated)) return true
        }
        return false
    }

    fun numSatisfied(): Int = clauses.count { satisfied(it) }
    fun numUnsatisfied(): Int = clauses.size - numSatisfied()

    fun solve(algorithm: SATSolver.() -> Boolean): Boolean {
        iterations = 0
        var solved = false
        val elapsedMillis = measureTimeMillis {
            solved = algorithm()
        }
        println("solved in: $elapsedMillis ms")
        return solved
    }
}

/**
 * This is meant to be a simple-to-read and correct implementation; it is not efficient when
 * the literal count is high. Converting the state to a string is unnecessary and expensive,
 * but makes debugging easier. It prints the best known distance to go as it searches.
 * @return true if satisfied, false if unsatisfiable
 */
fun SATSolver.greedyBFS(): Boolean {
    data class Move(val index: Int, val value: Boolean)

    val history = Stack<Move>()
    val closed = HashSet<String>()

    fun move(move: Move) {
        history.add(Move(move.index, literals[move.index])) // add current state to history
        literals[move.index] = move.value // apply move
    }

    fun undo() {
        move(history.pop()) // undo previous move
        history.pop() // remove the undo move from history
    }

    // filter available moves down to those participating in unsatisfied clauses
    fun getLiterals(): Set<Int> = HashSet<Int>().apply {
        clauses.filterNot { satisfied(it) }.forEach { clause ->
            clause.literals.forEach { add(it.index) }
        }
    }

    fun getMoves(): List<Move> = ArrayList<Move>().apply {
        getLiterals().forEach { add(Move(it, !literals[it])) }
    }

    var distance = numUnsatisfied()
    println("$distance ${toString()}")

    while (distance != 0) {
        ++iterations
        closed.add(toString()) // add the current state to the closed list

        val prevDistance = distance
        val nextMove = getMoves()
            .asSequence()
            .filter {
                move(it)
                val ret = !closed.contains(toString())
                undo()
                ret
            }.minBy {
                move(it)
                val dist = numUnsatisfied()
                if (dist < distance) distance = dist
                undo()
                dist
            }

        if (nextMove != null) {
            move(nextMove)
            if (prevDistance != distance) println("$distance ${toString()}")
        } else { // no moves remaining?
            if (history.isEmpty()) return false // backtrack impossible?
            undo()
        }
    }

    return true
}

fun SATSolver.random(): Boolean {
    while (!satisfied()) {
        ++iterations
        literals[random.nextInt(literals.size)] = random.nextBoolean()
    }
    return true
}

fun parseFile(file: String): SATSolver {
    println("parsing: $file")
    var vars = 0
    var numClauses = 0
    val clauses = ArrayList<Clause>()
    val elapsedMillis = measureTimeMillis {
        try {
            File(file).forEachLine { rawLine ->
                val line = rawLine.trim()
                when {
                    line.startsWith('c') -> return@forEachLine
                    line.startsWith('p') -> {
                        val parts = line.split(' ')
                        require(parts[1] == "cnf") { "file format unknown: ${parts[1]}" }
                        vars = parts[2].toInt()
                        numClauses = parts[3].toInt()
                    }
                    else -> {
                        val literals = ArrayList<Literal>()
                        line.split(' ').filter { it.isNotBlank() }.forEach { literal ->
                            val negated = literal.startsWith('-')
                            val index = literal.removePrefix("-").toInt()
                            if (index == 0) return@forEach
                            literals.add(Literal(index - 1, negated))
                        }
                        if (literals.isNotEmpty()) clauses.add(Clause(literals))
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    println("parsed in: $elapsedMillis ms")

    require(numClauses == clauses.size) {
        "cnf claims $numClauses clauses, but file contains ${clauses.size}"
    }

    return SATSolver(vars, clauses)
}

fun main(args: Array<String>) {
    val solver = parseFile(args.first())
    println("""
        literals: ${solver.literals.size}
        clauses: ${solver.clauses.size}
        distance to goal:""".trimIndent()
    )
    if (solver.solve(SATSolver::greedyBFS)) {
        println("solution found:\n$solver")
    } else {
        println("unsatisfiable")
    }
    println("search iterations: ${solver.iterations}")
}
