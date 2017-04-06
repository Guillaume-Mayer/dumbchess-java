package com.pikouik.chess.core;

import java.util.List;

public interface Position {
	
	public List<Move> getLegalMoves();
	public Color getColorToPlay();
	public Position play(Move move);
	public Position unplay(Move move);
	public Position play(String alg);
	public Move moveFromAlg(String alg);
	public String moveToAlg(Move move); 
	public String toFen();
	public boolean isCheck();
	public boolean isDraw();
	public int countLegalMoves(Color color);
	public int getMaterialScore();
	
}
