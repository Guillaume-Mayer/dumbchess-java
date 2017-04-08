package chess.old;

public class MoveClassic implements Move {
	
	// This is needed to define a move
	private int row1, col1, row2, col2;
	private PieceType promote;
	
	// This is needed to unplay a move
	private Piece capture;
	private int twoPushColWas;
	private int halfMoveCountWas;
	private CastlingRights castlingRightsBefore;
	
	// This is convenient to play and unplay the move
	private boolean enPassant;
	private Side castling;
		
	public MoveClassic(int row1, int col1, int row2, int col2) {
		this.row1 = row1;
		this.col1 = col1;
		this.row2 = row2;
		this.col2 = col2;
	}
	
	public MoveClassic(int row1, int col1, int row2, int col2, PieceType promote) {
		this(row1, col1, row2, col2);
		this.promote = promote;
	}
	
	public int getRow1() {
		return row1;
	}

	public int getCol1() {
		return col1;
	}

	public int getRow2() {
		return row2;
	}

	public int getCol2() {
		return col2;
	}

	public Piece getCapture() {
		return capture;
	}

	public MoveClassic setCapture(Piece capture) {
		this.capture = capture;
		return this;
	}
	
	public boolean isCapture() {
		return (capture != null);
	}

	public PieceType getPromote() {
		return promote;
	}

	public MoveClassic setPromote(PieceType promote) {
		this.promote = promote;
		return this;
	}
	
	@Override
	public boolean isPromote() {
		return (promote != null);
	}

	public int getTwoPushColWas() {
		return twoPushColWas;
	}

	public void setTwoPushColWas(int twoPushColWas) {
		this.twoPushColWas = twoPushColWas;
	}

	public int getHalfMoveCountWas() {
		return halfMoveCountWas;
	}

	public void setHalfMoveCountWas(int halfMoveCountWas) {
		this.halfMoveCountWas = halfMoveCountWas;
	}
	
	@Override
	public boolean isEnPassant() {
		return enPassant;
	}

	public MoveClassic setEnPassant() {
		this.enPassant = true;
		return this;
	}

	public boolean isCastling() {
		return (castling != null);
	}
	
	@Override
	public Side getCastling() {
		return castling;
	}

	public MoveClassic setCastling(Side castling) {
		this.castling = castling;
		return this;
	}

	public CastlingRights getCastlingRightsBefore() {
		return castlingRightsBefore;
	}

	public void setCastlingRightsBefore(CastlingRights castlingRightsBefore) {
		this.castlingRightsBefore = castlingRightsBefore;
	}

	@Override
	public String toString() {
		StringBuilder alg = new StringBuilder();
		if (getCastling() == null) {
			alg.append(Const.LETTERS.charAt(getCol1()));
			alg.append(getRow1() + 1);
			alg.append(isCapture() ? "x" : "-");
			alg.append(Const.LETTERS.charAt(getCol2()));
			alg.append(getRow2() + 1);
			if (isEnPassant()) {
				alg.append("ep");
			} else if (isPromote()) {
				alg.append(getPromote().getAlgRepr());
			}
		} else {
			alg.append(getCastling() == Side.KING ? "O-O" : "O-O-O");
		}		
		return alg.toString();
	}
	
}
