package chess.core;

interface CastlingFlags {
	
	boolean kingSide(Color color);
	boolean queenSide(Color color);
	byte toByte();

}
