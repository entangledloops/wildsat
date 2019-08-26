import java.io.File
import java.util.*
import kotlin.collections.ArrayList

data class Literal(val index: Int, var negated: Boolean)
data class Clause(var literals: List<Literal>)

class SATSolver(numLiterals: Int, val clauses: List<Clause>)
{
    val random = Random(0)
    val literals = Array(numLiterals) { random.nextBoolean() }
    var iterations = 0

    override fun toString(): String = buildString {
        for (literal in literals) {
            append(when (literal) {
                true -> 'T'
                else -> 'F'
            })
        }
    }

    fun satisfied(): Boolean = clauses.all { satisfied(it) }
    fun satisfied(clause: Clause): Boolean {
        for (literal in clause.literals) {
            val value = literals[literal.index]
            if ((value && !literal.negated) || (!value && literal.negated)) return true
        }
        return false
    }

    fun numSatisfied(): Int = clauses.count { satisfied(it) }
    fun numUnsatisfied(): Int = clauses.size - numSatisfied()

    fun solve(algorithm: SATSolver.() -> Boolean): Boolean {
        iterations = 0
        return algorithm()
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
    val history: Stack<Move> = Stack()
    val closed: MutableSet<String> = HashSet()

    fun move(move: Move) {
        history.add(Move(move.index, literals[move.index])) // add current state to history
        literals[move.index] = move.value // apply move
    }

    fun undo() {
        move(history.pop()) // undo previous move
        history.pop() // remove the undo move from history
    }

    // filter available moves down to those participating in unsatisfied clauses
    fun getLiterals(): List<Int> {
        val literals: MutableList<Int> = ArrayList()
        for (clause in clauses) {
            if (!satisfied(clause)) {
                for (literal in clause.literals) literals.add(literal.index)
            }
        }
        return literals
    }

    fun getMoves(): List<Move> {
        val literals = getLiterals()
        val moves: MutableList<Move> = ArrayList()
        for (i in literals) {
            when(this.literals[i]) {
                true -> moves.add(Move(i, false))
                else -> moves.add(Move(i, true))
            }
        }
        return moves
    }

    var bestDistance = numUnsatisfied()
    println(bestDistance)

    while (!satisfied()) {
        ++iterations
        closed.add(toString()) // add the current state to the closed list

        val prevDistance = bestDistance
        val nextMove = getMoves().filter {
            move(it)
            val ret = !closed.contains(toString())
            undo()
            ret
        }.minBy {
            move(it)
            val dist = numUnsatisfied()
            if (dist < bestDistance) bestDistance = dist
            undo()
            dist
        }
        if (prevDistance != bestDistance) println(bestDistance)

        if (nextMove == null) { // no moves remaining?
            if (history.isEmpty()) return false // backtrack impossible?
            undo()
        } else {
            move(nextMove)
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
    var vars = 0
    var numClauses = 0
    val clauses: MutableList<Clause> = ArrayList()
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
                    val literals: MutableList<Literal> = ArrayList()
                    line.split(' ').filter { it.isNotBlank() }.forEach { literal ->
                        val negated = literal.startsWith('-')
                        val index = literal.removePrefix("-").toInt()
                        if (index == 0) return@forEach
                        literals.add(Literal(index-1, negated))
                    }
                    if (literals.isNotEmpty()) clauses.add(Clause(literals))
                }
            }
        }
    }
    catch (e: Exception) {
        e.printStackTrace()
    }

    require(numClauses == clauses.size) {
        "cnf claims $numClauses clauses, but file contains ${clauses.size}"
    }

    return SATSolver(vars, clauses)
}

fun main(args: Array<String>) {
    val solver = parseFile(args.first())
    if (solver.solve(SATSolver::greedyBFS)) {
        println(solver)
    } else {
        println("unsatisfiable")
    }
    println("search iterations: ${solver.iterations}")
}
