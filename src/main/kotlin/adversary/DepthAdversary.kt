package adversary

import model.Board
import model.Move

class DepthAdversary(white: Boolean) : Adversary, AbstractAdversary(white, 3) {
    override fun pickMove(board: Board): Move {
        val moves = board.getMoves()
        val startTime = System.nanoTime()
        val decision = search(board = board)
        println("Took " + ((System.nanoTime() - startTime) / 1000000000.0) + " seconds")
        println("Decided move was " + decision.third + ", score of " + decision.first + ", Positions evaluated: " + decision.second)
        println(decision.toString())
        val move = decision.third
        return move ?: moves.random()
    }

    private fun search(depth: Int = 0, board: Board): Triple<Int, Int, Move?> {
        val depthOrDone = checkDepthAndStatus(depth, board)
        if (depthOrDone.first) {
            return Triple(depthOrDone.second, 1, null)
        }
        val moves = board.getMoves()
        var bestScore = Int.MIN_VALUE
        var bestMove: Move? = null
        var positionsEvaluated = 0
        for (move in moves) {
            val boardTest = board.clone()
            boardTest.performMoveNoCheck(move)
            val decision = search(depth + 1, boardTest)
            val score = -1 * decision.first
            positionsEvaluated += decision.second
            if (score > bestScore) {
                bestMove = move
                bestScore = score
            }
        }
        return Triple(bestScore, positionsEvaluated, bestMove)
    }

}