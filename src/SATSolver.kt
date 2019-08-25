import java.io.File
import java.util.*
import kotlin.collections.ArrayList

data class Literal(val index: Int, var negated: Boolean)
data class Clause(var literals: List<Literal>)

class SATSolver(numLiterals: Int, val clauses: List<Clause>)
{
    val literals: Array<Boolean?> = Array(numLiterals) { null }
    var iterations = 0

    override fun toString(): String = buildString {
        for (literal in literals) {
            append(when (literal) {
                true -> 'T'
                false -> 'F'
                else -> 'U'
            })
        }
    }

    fun satisfied(): Boolean = clauses.all { satisfied(it) }
    fun satisfied(clause: Clause): Boolean {
        for (literal in clause.literals) {
            val value = literals[literal.index] ?: continue
            if ((value && !literal.negated) || (!value && literal.negated)) return true
        }
        return false
    }

    fun numSatisfied(): Int = clauses.count { satisfied(it) }
    fun numUnsatisfied(): Int = clauses.size - numSatisfied()

    fun print(): SATSolver {
        for (i in 0..literals.size) println("${i+1}: ${literals[i]}")
        return this
    }

    fun solve(algorithm: SATSolver.() -> Boolean): Boolean {
        iterations = 0
        return algorithm()
    }
}

fun SATSolver.greedyBFS(): Boolean {
    data class Move(val index: Int, val value: Boolean?)
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

    fun getMoves(): List<Move> {
        val moves: MutableList<Move> = ArrayList()
        for (i in literals.indices) {
            when(literals[i]) {
                true -> moves.add(Move(i, false))
                false -> moves.add(Move(i, true))
                else -> {
                    moves.add(Move(i, true))
                    moves.add(Move(i, false))
                }
            }
        }
        return moves
    }

    var bestDistance = numUnsatisfied()
    while (!satisfied()) {
        ++iterations

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

        if (nextMove == null) {
            if (history.isEmpty()) return false
            undo()
        } else {
            closed.add(toString()) // add the current state to the closed list
            move(nextMove)
        }
    }

    return true
}

/*// find first clause with an unset literal
val clauseWithUnsetLit = clauses.find { clause -> null != clause.literals.find { lit -> null == literals[lit.index] } } ?: break
val unsetLit: Literal = clauseWithUnsetLit.literals.find { lit -> null == literals[lit.index] } ?: continue
literals[unsetLit.index] = true*/

fun SATSolver.random(): Boolean {
    val random = Random(0)
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
        File(file).forEachLine {
            val line = it.trim()
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
                    line.split(' ').filter { it.isNotBlank() }.forEach { variable ->
                        val negated = variable.startsWith('-')
                        val index = variable.removePrefix("-").toInt()
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
    println(solver.solve(SATSolver::greedyBFS))
    println(solver.iterations)
}
