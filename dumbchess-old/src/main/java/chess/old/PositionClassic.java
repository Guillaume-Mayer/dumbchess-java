package chess.old;

import java.util.ArrayList;
import java.util.List;

public final class PositionClassic implements Position {

	private BoardClassic board;
	private CastlingRights castlingRights;
	
	private Color colorToPlay;

	private int twoPushCol = TWO_PUSH_DEFAULT;

	private static final int TWO_PUSH_DEFAULT = 9;
	
	private int halfMoveCount;
	private int moveNum;
	
	public PositionClassic() {
		this.board = new BoardClassic();
		this.castlingRights = new CastlingRightsBits();
		try {
			initFromFen(Const.INITIAL_FEN);
		} catch (InvalidFenException e) {
			e.printStackTrace();
		}
	}
	
	public PositionClassic(String fen) throws InvalidFenException {
		this.board = new BoardClassic();
		this.castlingRights = new CastlingRightsBits();
		initFromFen(fen);
	}
	
	@Override
	public Color getColorToPlay() {
		return colorToPlay;
	}
	
	private void initFromFen(String fen) throws InvalidFenException {
		String fenTokens[] = fen.split(" ");
		if (fenTokens.length != 6) throw new InvalidFenException();
		// Board
		board.initFromFen(fenTokens[0]);
		// Color to play
		switch (fenTokens[1]) {
		case "w":
			colorToPlay = Color.WHITE;
			break;
		case "b":
			colorToPlay = Color.BLACK;
			break;
		default:
			throw new InvalidFenException();
		}
		// Castling flags
		castlingRights.initFromFen(fenTokens[2]);
		// Two push column
		if (fenTokens[3].equals("-")) {
			twoPushCol = TWO_PUSH_DEFAULT;
		} else {
			twoPushCol = Const.LETTERS.indexOf(fenTokens[3].charAt(0));
		}			
		// Half-move count
		halfMoveCount = Integer.parseInt(fenTokens[4]);
		// Move number
		moveNum = Integer.parseInt(fenTokens[5]);
	}
	
	@Override
	public String toFen() {
		StringBuilder fen = new StringBuilder();
		board.toFen(fen);
		fen.append(" ");
		fen.append(colorToPlay == Color.WHITE ? "w" : "b");
		fen.append(" ");
		castlingRights.toFen(fen);
		fen.append(" ");
		if (twoPushCol == TWO_PUSH_DEFAULT) {
			fen.append("-");
		} else {
			fen.append(Const.LETTERS.charAt(twoPushCol));
			fen.append(colorToPlay.getEnPassantRow() + colorToPlay.getSens() + 1);
		}
		fen.append(" ");
		fen.append(halfMoveCount);
		fen.append(" ");
		fen.append(moveNum);
		return fen.toString();
	}
	
	@Override
	public Position play(Move move) {
		MoveClassic m = (MoveClassic) move;
		// Coordinates
		int row1 = m.getRow1();
		int col1 = m.getCol1();
		int row2 = m.getRow2();
		int col2 = m.getCol2();
		// Apply the move and get moving color
		Piece piece = board.play(m);
		Color color = piece.getColor();
		// Special stuff
		m.setTwoPushColWas(twoPushCol);
		m.setHalfMoveCountWas(halfMoveCount);
		m.setCastlingRightsBefore(castlingRights.copy());
		twoPushCol = TWO_PUSH_DEFAULT;
		halfMoveCount ++;
		switch (piece.getType()) {
		case PAWN:
			// Update two push column
			if (row2 - row1 == 2*color.getSens()) twoPushCol = col1;
			// Reset half move count
			halfMoveCount = 0;
			break;
		case ROOK:
			// Update castling rights
			if ((color == Color.WHITE && row1 == 0) || (color == Color.BLACK && row1 == 7)) {
				if (col1 == 0) {
					castlingRights.unset(color, Side.QUEEN);
				} else if (col1 == 7) {
					castlingRights.unset(color, Side.KING);
				}
			}
			break;
		case KING:
			// Update castling rights
			castlingRights.unset(color);
			break;
		default:
			break;
		}
		// Reset half move count on capture
		if (m.isCapture()) {
			halfMoveCount = 0;
			// Update castling rights on rook capture
			if (m.getCapture().getType() == PieceType.ROOK && row2 == color.getPromoteRow() + color.getSens()) {
				if (col2 == 0 ) {
					castlingRights.unset(color.swap(), Side.QUEEN);
				} else if (col2 == 7) {
					castlingRights.unset(color.swap(), Side.KING);
				}
			}
		}
		// Update move number
		if (color == Color.BLACK) moveNum ++;
		// Swap color to play
		colorToPlay = colorToPlay.swap();
		return this;
	}
	
	public Position unplay(Move move) {
		MoveClassic m = (MoveClassic) move;
		// Unapply the move and get moving color
		Color color = board.unplay(m).getColor();
		// Restore twoPushCol and halfMoveCOunt
		twoPushCol = m.getTwoPushColWas();
		halfMoveCount = m.getHalfMoveCountWas();
		castlingRights = m.getCastlingRightsBefore();
		// Update move number
		if (color == Color.BLACK) moveNum --;
		// Swap color to play
		colorToPlay = colorToPlay.swap();
		return this;
	}
	
	@Override
	public List<Move> getLegalMoves() {
		return getLegalMoves(colorToPlay);
	}
	
	public List<Move> getLegalMoves(Color color) {
		return getLegalMoves(color, true);
	}
		
	private List<Move> getLegalMoves(Color color, boolean allowStupidPromotion) {
		List<MoveClassic> pseudo = board.getPseudoLegalMoves(color, allowStupidPromotion, colorToPlay, twoPushCol, castlingRights);
		List<Move> legal = new ArrayList<>(pseudo.size());
		for (Move m : pseudo) {
			play(m);
			if (!board.isCheck(color)) legal.add(m);
			unplay((MoveClassic) m);
		}
		return legal;
	}
	
	@Override
	public int countLegalMoves(Color color) {
		List<MoveClassic> pseudo = board.getPseudoLegalMoves(color, false, colorToPlay, twoPushCol, castlingRights);
		int count = pseudo.size();
		for (Move m : pseudo) {
			play(m);
			if (board.isCheck(color)) count --;
			unplay((MoveClassic) m);
		}
		return count;
	}
	
	@Override
	public Position play(String alg) {
		Move m = moveFromAlg(alg);
		play(m);
		return this;
	}
	
	@Override
	public MoveClassic moveFromAlg(String alg) {
		PieceType pieceType = PieceType.getFromAlgRepr(alg.substring(0, 1));
		int start = (pieceType == PieceType.PAWN ? 0 : 1);
		MoveClassic m = new MoveClassic(
			Character.getNumericValue(alg.charAt(start + 1)) - 1,
			Const.LETTERS.indexOf(alg.charAt(start)),
			Character.getNumericValue(alg.charAt(start + 4)) - 1,
			Const.LETTERS.indexOf(alg.charAt(start + 3))
			);
		return m;
	}	
	
	@Override
	public String moveToAlg(Move move) {
		String sMove = board.moveToAlg((MoveClassic) move);
		play(move);
		if (isCheck()) sMove += "+";
		unplay(move);
		return sMove;
	}

	@Override
	public int getMaterialScore() {
		return board.getMaterialScore(colorToPlay);
	}

	@Override
	public boolean isCheck() {
		return board.isCheck(colorToPlay);
	}

	@Override
	public boolean isDraw() {
		// Fifty moves rule
        if (halfMoveCount >= 100) return true;
        // TODO Impossible checkmate
        // TODO Threefold repetition
        return false;
	}
	
}
