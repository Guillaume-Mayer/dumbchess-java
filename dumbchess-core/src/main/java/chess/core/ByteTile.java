package chess.core;

class ByteTile implements Tile {
	
	public static final byte EMPTY			= 0x0;
	public static final byte OUT			= 0x8;
	public static final byte BLACK_ROOK		= 0x1;
	public static final byte BLACK_KNIGHT	= 0x2;
	public static final byte BLACK_BISHOP	= 0x3;
	public static final byte BLACK_QUEEN	= 0x4;
	public static final byte BLACK_KING		= 0x5;
	public static final byte BLACK_PAWN		= 0x6;
	public static final byte WHITE_ROOK		= 0xA;
	public static final byte WHITE_KNIGHT	= 0xB;
	public static final byte WHITE_BISHOP	= 0xC;
	public static final byte WHITE_QUEEN	= 0xD;
	public static final byte WHITE_KING		= 0xE;
	public static final byte WHITE_PAWN		= 0xF;
	
	private byte tile;
	
	public ByteTile(final byte tile) {
		this.tile = tile;
	}

	public ByteTile(final int tile) {
		this((byte) tile);
	}
	
	public ByteTile(final Piece piece, final Color color) {
		switch (piece) {
		case PAWN:
			this.tile = (color == Color.WHITE ? WHITE_PAWN : BLACK_PAWN);
			break;
		case ROOK:
			this.tile = (color == Color.WHITE ? WHITE_ROOK : BLACK_ROOK);
			break;
		case BISHOP:
			this.tile = (color == Color.WHITE ? WHITE_BISHOP : BLACK_BISHOP);
			break;
		case KNIGHT:
			this.tile = (color == Color.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT);
			break;
		case KING:
			this.tile = (color == Color.WHITE ? WHITE_KING : BLACK_KING);
			break;
		case QUEEN:
			this.tile = (color == Color.WHITE ? WHITE_QUEEN : BLACK_QUEEN);
			break;
		default:
			throw new IllegalArgumentException("Illegal Piece argument");
		}
	}
	
	@Override
	public ByteTile swapped() {
		if (isWhite()) {
			return new ByteTile(tile - 9);
		} else if (isBlack()) {
			return new ByteTile(tile + 9);
		}
		return new ByteTile(tile);
	}
	
	@Override
	public boolean isEmpty() {
		return (tile == EMPTY);
	}

	@Override
	public boolean hasPiece() {
		return (tile != EMPTY && tile != OUT);
	}

	@Override
	public boolean hasPiece(final Piece piece) {
		switch (piece) {
		case PAWN:
			return (tile == WHITE_PAWN || tile == BLACK_PAWN);
		case ROOK:
			return (tile == WHITE_ROOK || tile == BLACK_ROOK);
		case BISHOP:
			return (tile == WHITE_BISHOP || tile == BLACK_BISHOP);
		case KNIGHT:
			return (tile == WHITE_KNIGHT || tile == BLACK_KNIGHT);
		case KING:
			return (tile == WHITE_KING || tile == BLACK_KING);
		case QUEEN:
			return (tile == WHITE_QUEEN || tile == BLACK_QUEEN);
		default:
			throw new IllegalArgumentException("Illegal Piece argument");
		}
	}

	@Override
	public boolean isColor(final Color color) {
		switch (color) {
		case WHITE:
			return isWhite();
		case BLACK:
			return isBlack();
		default:
			throw new IllegalArgumentException("Illegal Color argument");
		}
	}

	@Override
	public byte toByte() {
		return tile;
	}

	@Override
	public Piece getPiece() {
		switch (tile) {
		case WHITE_PAWN:
		case BLACK_PAWN:
			return Piece.PAWN;
		case WHITE_ROOK:
		case BLACK_ROOK:
			return Piece.ROOK;
		case WHITE_BISHOP:
		case BLACK_BISHOP:
			return Piece.BISHOP;
		case WHITE_KNIGHT:
		case BLACK_KNIGHT:
			return Piece.KNIGHT;
		case WHITE_KING:
		case BLACK_KING:
			return Piece.KING;
		case WHITE_QUEEN:
		case BLACK_QUEEN:
			return Piece.QUEEN;
		default:
			throw new IllegalStateException("This tile has no piece");
		}
	}

	@Override
	public boolean isOut() {
		return (tile == OUT);
	}

	@Override
	public boolean isWhite() {
		return (tile >= WHITE_ROOK && tile <= WHITE_PAWN);
	}

	@Override
	public boolean isBlack() {
		return (tile >= BLACK_ROOK && tile <= BLACK_PAWN);
	}
	
	@Override
	public boolean is(final byte b) {
		return (tile == b);
	}
}
