package chess.core;

interface Tile {

	boolean isEmpty();
	boolean isOut();
	boolean hasPiece();
	boolean hasPiece(Piece piece);
	boolean isColor(Color color);
	boolean isWhite();
	boolean isBlack();
	Piece getPiece();
	byte toByte();
	boolean is(byte b);
	Tile swapped();
	
}
