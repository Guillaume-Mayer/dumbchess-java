package com.pikouik.chess.core;

public interface Move {
	
	public boolean isEnPassant();
	public Side getCastling();
	public boolean isPromote();
	
}
