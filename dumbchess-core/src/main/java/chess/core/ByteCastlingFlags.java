package chess.core;

public class ByteCastlingFlags implements CastlingFlags {
	
	private static final byte WHITE_KING_SIDE	= 0b1000;
	private static final byte WHITE_QUEEN_SIDE	= 0b0100;
	private static final byte BLACK_KING_SIDE	= 0b0010;
	private static final byte BLACK_QUEEN_SIDE	= 0b0001;
	private static final byte FULL				= 0b1111;

	private byte flags;
	
	public ByteCastlingFlags(final byte flags) {
		this.flags = flags;
	}
	
	public ByteCastlingFlags(final boolean whiteKingSide, final boolean whiteQueenSide, final boolean blackKingSide, final boolean blackQueenSide) {
		if (whiteKingSide) flags |= WHITE_KING_SIDE;
		if (whiteQueenSide) flags |= WHITE_QUEEN_SIDE;
		if (blackKingSide) flags |= BLACK_KING_SIDE;
		if (blackQueenSide) flags |= BLACK_QUEEN_SIDE;
	}
	
	public static ByteCastlingFlags initial() {
		return new ByteCastlingFlags(FULL);
	}
	
	@Override
	public byte toByte() {
		return flags;
	}

	@Override
	public boolean kingSide(final Color color) {
		switch (color) {
		case WHITE:
			return ((flags & WHITE_KING_SIDE) == WHITE_KING_SIDE);
		case BLACK:
			return ((flags & BLACK_KING_SIDE) == BLACK_KING_SIDE);
		default:
			throw new IllegalArgumentException("Illegal Color argument");
		}
	}

	@Override
	public boolean queenSide(final Color color) {
		switch (color) {
		case WHITE:
			return ((flags & WHITE_QUEEN_SIDE) == WHITE_QUEEN_SIDE);
		case BLACK:
			return ((flags & BLACK_QUEEN_SIDE) == BLACK_QUEEN_SIDE);
		default:
			throw new IllegalArgumentException("Illegal Color argument");
		}
	}

}
