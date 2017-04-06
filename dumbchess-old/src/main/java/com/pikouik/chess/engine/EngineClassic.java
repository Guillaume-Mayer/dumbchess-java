package com.pikouik.chess.engine;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.pikouik.chess.core.Color;
import com.pikouik.chess.core.Move;
import com.pikouik.chess.core.Position;

public class EngineClassic {
	
	private Position position;
	private int maxDepth = 4;
	private Details details;
	
	private static final int MATE = -999999999;
	private static final int DRAW = 0;
	private static final int COEF_MATERIAL = 1;
	private static final int COEF_MOBILITY = 7;
	private static final int COEF_POSITIONAL = 1;
	
	public EngineClassic(Position position, int maxDepth) {
		this.position = position;
		this.maxDepth = maxDepth;
		this.details = new Details(maxDepth);
	}
	
	class Details {
		private Move[] best;
		private int score;
		
		private Details(int maxDepth) {
			best = new Move[maxDepth];
		}
		
		int getScore() {
			return score;
		}
		
		String getMoves() {
			return Stream.of(best)
					.map(m -> position.moveToAlg(m))
					.collect(Collectors.joining(", "));
		}
	}
	
	public Move getBestMove() {		
		long start = System.nanoTime();
		details.score = iterativeDeepening();
		long time = System.nanoTime() - start;
		StringBuilder result = new StringBuilder();
		result.append("iterativeDeepening: ");
		result.append(new DecimalFormat("#,##0").format(time / 1_000_000D));
		result.append(" ms\n");
		result.append("Best move: ");
		result.append(details.score);
		result.append(" [");
		result.append(
			Stream.of(details.best)
				.map(m -> position.moveToAlg(m))
				.collect(Collectors.joining(", ")));
		result.append("]");
		System.out.println(result);
		return details.best[0];
	}
	
	Details getDetails() {
		return details;
	}
	
	private int iterativeDeepening() {
		List<Move> moves = null;
		Map<Move, Integer> scores = new HashMap<>();
		// Reset scores
		for (int depth = 1; depth < maxDepth; depth ++) {
			// Negamax for each depth		
			int score = negaMax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, moves, scores);
			if (score == -MATE) return -MATE;
			// Order the moves by score descending
			moves = scores.entrySet()
				.stream()
				.sorted((a, b) -> b.getValue() - a.getValue())
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
			// Reset scores
			scores.clear();
		}
		// Final negamax
		return negaMax(maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, moves, null);
	}
	
	private int negaMax(int depth, int alpha, int beta, List<Move> moves, Map<Move, Integer> scores) {
        // Stop conditions
        if (depth == 0) return evaluate();
        if (moves == null) moves = position.getLegalMoves();
        if (moves.isEmpty()) return (position.isCheck() ? MATE : DRAW);
        // Set the worst for best score
        int bestScore = Integer.MIN_VALUE;        
        // Loop moves
        for (Move m : moves) {
            // Play the move
            position.play(m);
            System.out.println(String.format("%" + (10 - depth*4) + "s", "") + m);
            // Negative recursive evaluation
            int score = -negaMax(depth - 1, -beta, -alpha, null, null);
            System.out.println(String.format("%" + (10 - depth*4) + "s", "") + " -> " + score);
            // Unplay the move
            position.unplay(m);
            // Keep best move and score
            if (score > bestScore) {
                bestScore = score;
                details.best[maxDepth - depth] = m;
            }
            // Keep the score for each move (for reordering)
            if (scores != null) scores.put(m, score);
            // Alpha-beta pruning
            if (score > alpha) {
                alpha = score;
                if (alpha >= beta) break;
            }
        }
        return bestScore;
 	}

	public int evaluate() {
		Color color = position.getColorToPlay();
		int mobility = position.countLegalMoves(color);
		if (mobility == 0) {
			if (position.isCheck()) return MATE;
			return DRAW;
		}
		mobility -= position.countLegalMoves(color.swap());
		int material = position.getMaterialScore();
		int positional = 0; //position.getPositionalScore();
		return (COEF_MATERIAL * material) + (COEF_MOBILITY * mobility) + (COEF_POSITIONAL * positional);
	}
		
}
