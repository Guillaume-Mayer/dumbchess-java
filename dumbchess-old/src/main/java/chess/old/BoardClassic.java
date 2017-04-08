package chess.old;

import java.util.ArrayList;
import java.util.List;

public final class BoardClassic {

	private Piece map[][];
	private int kingPos[][];
	
	public Piece get(int row, int col) {
		return map[row][col];
	}
	
	public void set(int row, int col, Piece piece) {
		map[row][col] = piece;
	}

	public boolean isEmpty(int row, int col) {
		if (row < 0) return false;
		if (row > 7) return false;
		if (col < 0) return false;
		if (col > 7) return false;
		return (get(row, col) == null);
	}

	public boolean isColor(int row, int col, Color color) {
		if (row < 0) return false;
		if (row > 7) return false;
		if (col < 0) return false;
		if (col > 7) return false;
		Piece piece = get(row, col);
		if (piece == null) return false;
		return (piece.getColor() == color);
	}
	
	public boolean isCapturable(int row, int col, Color color) {
		if (row < 0) return false;
		if (row > 7) return false;
		if (col < 0) return false;
		if (col > 7) return false;
		Piece piece = get(row, col);
		if (piece == null) return false;
		if (piece.getColor() != color) return false;
		if (piece.getType() == PieceType.KING) return false;
		return true;
	}

	public boolean isCheck(Color color) {
		assert get(kingPos[color.ordinal()][0], kingPos[color.ordinal()][1]).getColor() == color;
		assert get(kingPos[color.ordinal()][0], kingPos[color.ordinal()][1]).getType() == PieceType.KING;
		return isAttacked(kingPos[color.ordinal()][0], kingPos[color.ordinal()][1], color);
	}

	public boolean isAttacked(int row, int col, Color color) {
		color = color.swap();
		int row2, col2;
		// Check for knights
		int directions[][] = {
				{+1, +2},
				{+2, +1},
				{-1, +2},
				{-2, +1},
				{-1, -2},
				{-2, -1},
				{+1, -2},
				{+2, -1}
			};
		for (int direction[] : directions) {
			row2 = row + direction[0];
			col2 = col + direction[1];
			if (isColor(row2, col2, color)) {
				if (get(row2, col2).getType() == PieceType.KNIGHT) return true;
			}
		}
		// Check for bishops, queen, king and pawns
		directions = new int[][] {
				{+1, +1},
				{+1, -1},
				{-1, +1},
				{-1, -1}
			};
		for (int direction[] : directions) {
			row2 = row + direction[0];
			col2 = col + direction[1];
			boolean first = true;
			while (isEmpty(row2, col2)) {
				row2 += direction[0];
				col2 += direction[1];
				first = false;
			}
			if (isColor(row2, col2, color)) {
				if (get(row2, col2).getType() == PieceType.BISHOP) return true;
				if (get(row2, col2).getType() == PieceType.QUEEN) return true;
				if (first) {
					if (get(row2, col2).getType() == PieceType.KING) return true;
					if (row2 == row - color.getSens() && get(row2, col2).getType() == PieceType.PAWN) return true;
				}
			}
		}
		// Check for rooks, quuen and king
		directions = new int[][] {
				{+1, 0},
				{-1, 0},
				{0, +1},
				{0, -1}
			};
		for (int direction[] : directions) {
			row2 = row + direction[0];
			col2 = col + direction[1];
			boolean first = true;
			while (isEmpty(row2, col2)) {
				row2 += direction[0];
				col2 += direction[1];
				first = false;
			}
			if (isColor(row2, col2, color)) {
				if (get(row2, col2).getType() == PieceType.ROOK) return true;
				if (get(row2, col2).getType() == PieceType.QUEEN) return true;
				if (first && get(row2, col2).getType() == PieceType.KING) return true;
			}
		}		
		return false;
	}
	
	public void getMovesForKnight(Color color, int row, int col, List<MoveClassic> moves) {
		int directions[][] = {
				{+1, +2},
				{+2, +1},
				{-1, +2},
				{-2, +1},
				{-1, -2},
				{-2, -1},
				{+1, -2},
				{+2, -1}
			};
		int row2, col2;
		for (int direction[] : directions) {
			row2 = row + direction[0];
			col2 = col + direction[1];
			if (isEmpty(row2, col2)) {
				moves.add(new MoveClassic(row, col, row2, col2));
			} else if (isCapturable(row2, col2, color.swap())) {
				moves.add(new MoveClassic(row, col, row2, col2).setCapture(get(row2, col2)));
			}
		}
	}
	
	public void getMovesForBishop(Color color, int row, int col, List<MoveClassic> moves) {
		int directions[][] = {
				{+1, +1},
				{+1, -1},
				{-1, +1},
				{-1, -1}
		};
		int row2, col2;
		for (int direction[] : directions) {
			row2 = row + direction[0];
			col2 = col + direction[1];
			while (isEmpty(row2, col2)) {
				moves.add(new MoveClassic(row, col, row2, col2));
				row2 += direction[0];
				col2 += direction[1];
			}
			if (isCapturable(row2, col2, color.swap())) {
				moves.add(new MoveClassic(row, col, row2, col2).setCapture(get(row2, col2)));
			}
		}
	}
	
	public void getMovesForRook(Color color, int row, int col, List<MoveClassic> moves) {
		int directions[][] = {
				{+1, 0},
				{-1, 0},
				{0, +1},
				{0, -1}
		};
		int row2, col2;
		for (int direction[] : directions) {
			row2 = row + direction[0];
			col2 = col + direction[1];
			while (isEmpty(row2, col2)) {
				moves.add(new MoveClassic(row, col, row2, col2));
				row2 += direction[0];
				col2 += direction[1];
			}
			if (isCapturable(row2, col2, color.swap())) {
				moves.add(new MoveClassic(row, col, row2, col2).setCapture(get(row2, col2)));
			}
		}
	}
	
	public void getMovesForQueen(Color color, int row, int col, List<MoveClassic> moves) {
		getMovesForBishop(color, row, col, moves);
		getMovesForRook(color, row, col, moves);
	}
	
	public void getMovesForKing(Color color, int row, int col, List<MoveClassic> moves, CastlingRights castlingRights) {
		int directions[][] = {
				{+1, +1},
				{+1, -1},
				{-1, +1},
				{-1, -1},
				{+1, 0},
				{-1, 0},
				{0, +1},
				{0, -1}
		};
		int row2, col2;
		for (int direction[] : directions) {
			row2 = row + direction[0];
			col2 = col + direction[1];
			if (isEmpty(row2, col2)) {
				moves.add(new MoveClassic(row, col, row2, col2));
			} else if (isCapturable(row2, col2, color.swap())) {
				moves.add(new MoveClassic(row, col, row2, col2).setCapture(get(row2, col2)));
			}
		}
		// Castling
		if (castlingRights.has(color, Side.QUEEN)) {
			if (isEmpty(row, col - 1) && isEmpty(row, col - 2)) {
				if (!isAttacked(row, col, color) && !isAttacked(row, col - 1, color) && !isAttacked(row, col - 2, color)) {
					moves.add(new MoveClassic(row, col, row, col - 2).setCastling(Side.QUEEN));
				}
			}
		} else if (castlingRights.has(color, Side.KING)) {
			if (isEmpty(row, col + 1) && isEmpty(row, col + 2)) {
				if (!isAttacked(row, col, color) && !isAttacked(row, col + 1, color) && !isAttacked(row, col + 2, color)) {
					moves.add(new MoveClassic(row, col, row, col + 2).setCastling(Side.KING));
				}
			}
		}
	}
	
	public void getMovesForPawn(Color color, int row, int col, List<MoveClassic> moves, boolean allowStupidPromotion, Color colorToPlay, int twoPushCol) {
		int sens = color.getSens();
		boolean promote = (row == color.getPromoteRow());
		// One forward
		if (isEmpty(row + sens, col)) {
			moves.add(new MoveClassic(row, col, row + sens, col));
			// Two push
			if (row == color.getTwoPushRow() && isEmpty(row + 2*sens, col)) {
				moves.add(new MoveClassic(row, col, row + 2*sens, col));
			} else if (promote) {
				addPromoteMoves(moves, row, col, row + sens, col, allowStupidPromotion);
			}
		}
		// Capture on left
		if (isCapturable(row + sens, col - 1, color.swap())) {
			moves.add(new MoveClassic(row, col, row + sens, col - 1).setCapture(get(row + sens, col - 1)));
			if (promote) addPromoteMoves(moves, row, col, row + sens, col - 1, allowStupidPromotion);
		}
		// Capture on right
		if (isCapturable(row + sens, col + 1, color.swap())) {
			moves.add(new MoveClassic(row, col, row + sens, col + 1).setCapture(get(row + sens, col + 1)));
			if (promote) addPromoteMoves(moves, row, col, row + sens, col + 1, allowStupidPromotion);
		}
		// En passant
		if (row == color.getEnPassantRow() && color == colorToPlay) {
			// On left
			if (twoPushCol == col - 1) {
				moves.add(new MoveClassic(row, col, row + sens, col - 1).setCapture(get(row, col - 1)).setEnPassant());
			} else if (twoPushCol == col + 1) {
				moves.add(new MoveClassic(row, col, row + sens, col + 1).setCapture(get(row, col + 1)).setEnPassant());
			}
		}
	}
	
	private void addPromoteMoves(List<MoveClassic> moves, int row1, int col1, int row2, int col2, boolean allowStupid) {
		MoveClassic m = moves.get(moves.size() - 1);
		m.setPromote(PieceType.QUEEN);
		moves.add(new MoveClassic(row1, col1, row2, col2).setCapture(m.getCapture()).setPromote(PieceType.KNIGHT));
		if (allowStupid) {
			moves.add(new MoveClassic(row1, col1, row2, col2).setCapture(m.getCapture()).setPromote(PieceType.ROOK));
			moves.add(new MoveClassic(row1, col1, row2, col2).setCapture(m.getCapture()).setPromote(PieceType.BISHOP));
		}
	}
	
	public void getMovesForTile(Color color, int row, int col, List<MoveClassic> moves, boolean allowStupidPromotion, Color colorToPlay, int twoPushCol, CastlingRights castlingRights) {
		Piece piece = get(row, col);
		if (piece == null) return;
		if (piece.getColor() != color) return;
		switch (piece.getType()) {
		case PAWN:
			getMovesForPawn(color, row, col, moves, allowStupidPromotion, colorToPlay, twoPushCol);
			break;
		case ROOK:
			getMovesForRook(color, row, col, moves);
			break;
		case BISHOP:
			getMovesForBishop(color, row, col, moves);
			break;
		case KNIGHT:
			getMovesForKnight(color, row, col, moves);
			break;
		case KING:
			getMovesForKing(color, row, col, moves, castlingRights);
			break;
		case QUEEN:
			getMovesForQueen(color, row, col, moves);
			break;
		}
	}
	
	public List<MoveClassic> getPseudoLegalMoves(Color color, boolean allowStupidPromotion, Color colorToPlay, int twoPushCol, CastlingRights castlingRights) {
		List<MoveClassic> moves = new ArrayList<>();
		for (int row = 0; row < 8; row ++) {
			for (int col = 0; col < 8; col ++) {
				getMovesForTile(color, row, col, moves, allowStupidPromotion, colorToPlay, twoPushCol, castlingRights);
			}
		}
		return moves;
	}

	private Piece move(int row1, int col1, int row2, int col2) {
		Piece piece = get(row1, col1);
		set(row2, col2, piece);
		set(row1, col1, null);
		return piece;
	}
	
	public Piece play(MoveClassic move) {
		int row2 = move.getRow2();
		int col2 = move.getCol2();
		Piece piece = move(move.getRow1(), move.getCol1(), row2, col2);
		assert piece != null;
		// Special moves
		if (move.isEnPassant()) {
			set(piece.getColor().getEnPassantRow(), col2, null);
		} else if (move.getCastling() == Side.KING) {
			move(row2, 7, row2, 5);
			kingPos[piece.getColor().ordinal()][1] = 6;
		} else if (move.getCastling() == Side.QUEEN) {
			move(row2, 0, row2, 3);
			kingPos[piece.getColor().ordinal()][1] = 2;
		} else if (move.isPromote()) {
			piece.promote(move.getPromote());
		} else if (piece.getType() == PieceType.KING) {
			kingPos[piece.getColor().ordinal()][0] = row2;
			kingPos[piece.getColor().ordinal()][1] = col2;
		}
		return piece;
	}
	
	public Piece unplay(MoveClassic move) {
		int row2 = move.getRow2();
		int col2 = move.getCol2();
		Piece piece = get(row2, col2);
		set(move.getRow1(), move.getCol1(), piece);
		// Special moves
		if (move.isEnPassant()) {
			set(row2, col2, null);
			set(row2 - piece.getColor().getSens(), col2, move.getCapture());
		} else if (move.getCastling() == Side.KING) {
			move(row2, 5, row2, 7);
			set(row2, col2, null);
			kingPos[piece.getColor().ordinal()][1] = 4;
		} else if (move.getCastling() == Side.QUEEN) {
			move(row2, 3, row2, 0);
			set(row2, col2, null);
			kingPos[piece.getColor().ordinal()][1] = 4;
		} else {
			if (move.isCapture()) {
				set(row2, col2, move.getCapture());
			} else {
				set(row2, col2, null);
			}
			if (move.isPromote()) {
				piece.unpromote();
			} else if (piece.getType() == PieceType.KING) {
				kingPos[piece.getColor().ordinal()][0] = move.getRow1();
				kingPos[piece.getColor().ordinal()][1] = move.getCol1();
			}
		}
		return piece;
	}

	public void initFromFen(String fen) throws InvalidFenException {
		String rows[] = fen.split("/");
		if (rows.length != 8) throw new InvalidFenException();
		map = new Piece[8][8];
		kingPos = new int[2][2];
		for (int row = 0; row < 8; row ++) {
			int col = 0;
			for (char ch : rows[row].toCharArray()) {
				if (Character.isDigit(ch)) {
					col += Character.getNumericValue(ch);
				} else {
					Piece piece = Piece.newFromFen(ch);
					map[7 - row][col] = piece;
					if (piece.getType() == PieceType.KING) {
						kingPos[piece.getColor().ordinal()][0] = 7 - row;
						kingPos[piece.getColor().ordinal()][1] = col;
					}
					col ++;
				}
			}
		}
	}
	
	public void init() {
		try {
			initFromFen(Const.INITIAL_FEN);
		} catch (InvalidFenException e) {
			e.printStackTrace();
		}
	}

	public void toFen(StringBuilder fen) {
		int countEmpty;
		for (int row = 7; row >= 0; row --) {
			countEmpty = 0;
			for (int col = 0; col < 8; col ++) {
				Piece piece = map[row][col];
				if (piece == null) {
					countEmpty ++;
				} else {
					if (countEmpty > 0) {
						fen.append(countEmpty);
						countEmpty = 0;
					}
					fen.append(piece.getFenRepr());
				}
			}
			if (countEmpty > 0) fen.append(countEmpty);
			if (row > 0) fen.append("/");
		}
	}

	public String moveToAlg(MoveClassic move) {
		PieceType pieceType = get(move.getRow1(), move.getCol1()).getType();
		StringBuilder alg = new StringBuilder();
		if (move.getCastling() == null) {
			alg.append(pieceType.getAlgRepr());
			alg.append(Const.LETTERS.charAt(move.getCol1()));
			alg.append(move.getRow1() + 1);
			alg.append(move.isCapture() ? "x" : "-");
			alg.append(Const.LETTERS.charAt(move.getCol2()));
			alg.append(move.getRow2() + 1);
			if (move.isEnPassant()) {
				alg.append("ep");
			} else if (move.isPromote()) {
				alg.append(move.getPromote().getAlgRepr());
			}
		} else {
			alg.append(move.getCastling() == Side.KING ? "O-O" : "O-O-O");
		}		
		return alg.toString();
	}

	public int getMaterialScore(Color color) {
		int score = 0;
		for (int row = 0; row < 8; row ++) {
			for (int col = 0; col < 8; col ++) {
				Piece piece = get(row, col);
				if (piece != null) {
					if (color == piece.getColor()) {
						score += piece.getType().getValue();
					} else {
						score -= piece.getType().getValue();
					}
				}
			}
		}
		return score;
	}

}
