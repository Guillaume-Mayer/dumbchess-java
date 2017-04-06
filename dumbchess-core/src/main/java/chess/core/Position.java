package chess.core;

import java.util.Collection;

public interface Position {

	static final int EN_PASSANT_NONE = 0;
	
	Collection<Move> getLegalMoves();
	Position play(Move move);
	
	boolean isCheck();
	boolean isDraw();
	boolean isMate();

	String toHex();
	String toBase64();
		
}
