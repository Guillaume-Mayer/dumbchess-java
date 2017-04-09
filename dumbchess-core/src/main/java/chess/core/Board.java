package chess.core;

import java.util.Collection;

interface Board {

	Collection<Move> getMoves(boolean castleKingSide, boolean castleQueenSide, int enPassant);
	Board play(Move move);
	boolean isCheck();	
	byte[] toByteArray();
	char getMoveColumn1(Move move, Color colorToPlay);
	char getMoveColumn2(Move move, Color colorToPlay);
	char getMoveRow1(Move move, Color colorToPlay);
	char getMoveRow2(Move move, Color colorToPlay);
	String getPiece(Move move);
	boolean isCapture(Move move);
	boolean isEnPassant(Move move);
	boolean isCastleKing(Move move);
	boolean isCastleQueen(Move move);
	int getTwoPushColumn(Move move);
	boolean preventCastleKing(Color color, Move move);
	boolean preventCastleQueen(Color color, Move move);
	
}
