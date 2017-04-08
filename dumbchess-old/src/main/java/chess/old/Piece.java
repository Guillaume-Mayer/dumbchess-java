package chess.old;

public final class Piece {

	PieceType type;
	Color color;
		
	public Piece(PieceType type, Color color) {
		this.type = type;
		this.color = color;
	}

	public PieceType getType() {
		return type;
	}
	
	public Color getColor() {
		return color;
	}

	public char getFenRepr() {
		if (color == Color.WHITE) {
			return Character.toUpperCase(type.getFenRepr());
		} else {
			return Character.toLowerCase(type.getFenRepr());
		}
	}
	
	public void promote(PieceType promotion) {
		type = promotion;
	}
	
	public void unpromote() {
		type = PieceType.PAWN;
	}
	
	public static Piece newFromFen(char fen) throws InvalidFenException {
		switch (fen) {
		case 'K':
			return new Piece(PieceType.KING, Color.WHITE);
		case 'k':
			return new Piece(PieceType.KING, Color.BLACK);
		case 'Q':
			return new Piece(PieceType.QUEEN, Color.WHITE);
		case 'q':
			return new Piece(PieceType.QUEEN, Color.BLACK);
		case 'R':
			return new Piece(PieceType.ROOK, Color.WHITE);
		case 'r':
			return new Piece(PieceType.ROOK, Color.BLACK);
		case 'B':
			return new Piece(PieceType.BISHOP, Color.WHITE);
		case 'b':
			return new Piece(PieceType.BISHOP, Color.BLACK);
		case 'N':
			return new Piece(PieceType.KNIGHT, Color.WHITE);
		case 'n':
			return new Piece(PieceType.KNIGHT, Color.BLACK);
		case 'P':
			return new Piece(PieceType.PAWN, Color.WHITE);
		case 'p':
			return new Piece(PieceType.PAWN, Color.BLACK);
		default:
			throw new InvalidFenException();
		}
	}
		
}
