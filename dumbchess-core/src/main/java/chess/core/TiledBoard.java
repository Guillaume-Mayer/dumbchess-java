package chess.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

import chess.core.Board;
import chess.core.Tile;

public class TiledBoard implements Board {
	
	private static final int SIZE = 120;
	private static final int START = 21;
	private static final int END = 99;
	private static final int VUNIT = 10;
	private static final int HUNIT = 1;
	private static final String INITTIAL =
			"8888888888" +
			"8888888888" +
			"8ABCDECBA8" +
			"8FFFFFFFF8" +
			"8000000008" +
			"8000000008" +
			"8000000008" +
			"8000000008" +
			"8666666668" +
			"8123453218" +
			"8888888888" +
			"8888888888" ;
	
	private Tile[] tiles;
	private transient int king;
	
	public static TiledBoard initial() {
		TiledBoard initial = new TiledBoard(INITTIAL);
		initial.king = 25;
		return initial;
	}
	
	private TiledBoard(String hex) {
		this.tiles = new Tile[SIZE];
		for (int i = 0; i < SIZE; i ++) {
			this.tiles[i] = new ByteTile(Character.digit(hex.charAt(i), 16));
		}
	}
	
	public TiledBoard(byte[] bytes) {
		tiles = new Tile[SIZE];
		for (int i = 0; i < START; i++) {
			tiles[i] = new ByteTile(ByteTile.OUT);
		}
		int index = 0;
		for (int i = START; i < END; i += VUNIT) {
			tiles[i + 0] = new ByteTile((bytes[index + 0] & 0xf0) >> 4); 
			tiles[i + 1] = new ByteTile(bytes[index + 0] & 0x0f); 
			tiles[i + 2] = new ByteTile((bytes[index + 1] & 0xf0) >> 4); 
			tiles[i + 3] = new ByteTile(bytes[index + 1] & 0x0f); 
			tiles[i + 4] = new ByteTile((bytes[index + 2] & 0xf0) >> 4); 
			tiles[i + 5] = new ByteTile(bytes[index + 2] & 0x0f); 
			tiles[i + 6] = new ByteTile((bytes[index + 3] & 0xf0) >> 4); 
			tiles[i + 7] = new ByteTile(bytes[index + 3] & 0x0f);
			tiles[i + 8] = new ByteTile(ByteTile.OUT);
			tiles[i + 9] = new ByteTile(ByteTile.OUT);
			index += 4;
		}
		for (int i = END + 2; i < SIZE; i++) {
			tiles[i] = new ByteTile(ByteTile.OUT);
		}
		// Find the white king
		for (int i = START; i < END; i ++) {
			if (tiles[i].hasPiece(Piece.KING) && tiles[i].isWhite()) {
				king = i;
				break;
			}
		}
	}
	
	private TiledBoard(TiledBoard original, TiledMove move) {
		this.tiles = original.tiles.clone();
		int index1 = move.getIndex1();
		int index2 = move.getIndex2();
		boolean wasEmpty = tiles[index2].isEmpty();
		tiles[index2] = tiles[index1];
		tiles[index1] = new ByteTile(ByteTile.EMPTY);
		if (index1 == original.king) {
			king = index2;
		} else {
			king = original.king;
			if ((tiles[index2].getPiece() == Piece.PAWN) && wasEmpty && ((index2 == index1 + HUNIT + VUNIT) || (index2 == index1 - HUNIT + VUNIT))) {
				// En passant
				tiles[index2 - VUNIT] = new ByteTile(ByteTile.EMPTY);
			}
		}
	}
	
	@Override
	public TiledBoard play(Move move) {
		TiledMove tMove = (TiledMove) move;
		TiledBoard newBoard = new TiledBoard(this, tMove);
		if (move.isPromotion()) {
			newBoard.tiles[tMove.getIndex2()] = new ByteTile(move.getPromotion(), Color.WHITE);
		} else if (isCastleKing(move)) {
			if (king == 25) {
				newBoard.tiles[26] = newBoard.tiles[28];
				newBoard.tiles[28] = new ByteTile(ByteTile.EMPTY);
			} else {
				newBoard.tiles[23] = newBoard.tiles[21];
				newBoard.tiles[21] = new ByteTile(ByteTile.EMPTY);
			}
		} else if (isCastleQueen(move)) {
			if (king == 25) {
				newBoard.tiles[24] = newBoard.tiles[21];
				newBoard.tiles[21] = new ByteTile(ByteTile.EMPTY);
			} else {
				newBoard.tiles[25] = newBoard.tiles[28];
				newBoard.tiles[28] = new ByteTile(ByteTile.EMPTY);
			}
		}
		// Reverse the array
		newBoard.tiles = 
				IntStream.rangeClosed(1, SIZE)
					.mapToObj(index -> ((ByteTile) newBoard.tiles[SIZE - index]).swapped())
					.toArray(size -> new Tile[size]);
		// Find the white king
		for (int i = START; i < END; i ++) {
			if (newBoard.tiles[i].hasPiece(Piece.KING) && newBoard.tiles[i].isWhite()) {
				newBoard.king = i;
				break;
			}
		}
		return newBoard;
	}
	
	@Override
	public byte[] toByteArray() {
		byte[] bytes = new byte[32];
		int index = 0;
		for (int i = START; i < END; i += VUNIT) {
			bytes[index + 0] = (byte) ((tiles[i + 0].toByte() << 4) | tiles[i + 1].toByte());
			bytes[index + 1] = (byte) ((tiles[i + 2].toByte() << 4) | tiles[i + 3].toByte());
			bytes[index + 2] = (byte) ((tiles[i + 4].toByte() << 4) | tiles[i + 5].toByte());
			bytes[index + 3] = (byte) ((tiles[i + 6].toByte() << 4) | tiles[i + 7].toByte());
			index += 4;
		}
		return bytes;
	}

	@Override
	public Collection<Move> getMoves(boolean castleKingSide, boolean castleQueenSide, int enPassant) {
		Collection<Move> moves = new ArrayList<>();
		for (int i = START; i < END; i++) {
			if (tiles[i].isWhite()) {
				switch (tiles[i].getPiece()) {
				case PAWN:
					moves.addAll(getPawnMoves(i, enPassant));
					break;
				case ROOK:
					moves.addAll(getRookMoves(i));
					break;
				case BISHOP:
					moves.addAll(getBishopMoves(i));
					break;
				case KNIGHT:
					moves.addAll(getKnightMoves(i));
					break;
				case KING:
					moves.addAll(getKingMoves(i, castleKingSide, castleQueenSide));
					break;
				case QUEEN:
					moves.addAll(getRookMoves(i));
					moves.addAll(getBishopMoves(i));
					break;
				}
			}
		}
		if (!moves.isEmpty()) {
			Collection<Move> legalMoves = new ArrayList<>(moves.size());
			for (Move m : moves) {
				if (!new TiledBoard(this, (TiledMove) m).isCheck()) {
					legalMoves.add(m);
				}
			}
			return legalMoves;
		}
		return moves;
	}

	private Collection<Move> getKingMoves(int i, boolean castleKingSide, boolean castleQueenSide) {
		Collection<Move> moves = new ArrayList<>();
		int i2;
		i2 = i + VUNIT;
		if (isEmptyOrBlack(i2)) moves.add(new TiledMove(i, i2));
		i2 = i + HUNIT;
		if (isEmptyOrBlack(i2)) moves.add(new TiledMove(i, i2));
		i2 = i - VUNIT;
		if (isEmptyOrBlack(i2)) moves.add(new TiledMove(i, i2));
		i2 = i - HUNIT;
		if (isEmptyOrBlack(i2)) moves.add(new TiledMove(i, i2));
		i2 = i + VUNIT + HUNIT;
		if (isEmptyOrBlack(i2)) moves.add(new TiledMove(i, i2));
		i2 = i + VUNIT - HUNIT;
		if (isEmptyOrBlack(i2)) moves.add(new TiledMove(i, i2));
		i2 = i - VUNIT + HUNIT;
		if (isEmptyOrBlack(i2)) moves.add(new TiledMove(i, i2));
		i2 = i - VUNIT - HUNIT;
		if (isEmptyOrBlack(i2)) moves.add(new TiledMove(i, i2));
		if (castleKingSide) {
			if (i % 10 == 5) {
				if (isEmpty(i + HUNIT) && isEmpty(i + 2*HUNIT)) {
					if (!isAttacked(i) && !isAttacked(i + HUNIT)) {
						moves.add(new TiledMove(i, i + 2*HUNIT));
					}
				}
			} else {
				if (isEmpty(i - HUNIT) && isEmpty(i - 2*HUNIT)) {
					if (!isAttacked(i) && !isAttacked(i - HUNIT)) {
						moves.add(new TiledMove(i, i - 2*HUNIT));
					}
				}
			}
		}
		if (castleQueenSide) {
			if (i % 10 == 5) {
				if (isEmpty(i - HUNIT) && isEmpty(i - 2*HUNIT) && isEmpty(i - 3*HUNIT)) {
					if (!isAttacked(i) && !isAttacked(i - HUNIT)) {
						moves.add(new TiledMove(i, i - 2*HUNIT));
					}
				}
			} else {
				if (isEmpty(i + HUNIT) && isEmpty(i + 2*HUNIT) && isEmpty(i + 3*HUNIT)) {
					if (!isAttacked(i) && !isAttacked(i + HUNIT)) {
						moves.add(new TiledMove(i, i + 2*HUNIT));
					}
				}
			}
		}
		return moves;
	}

	private Collection<Move> getBishopMoves(int i) {
		Collection<Move> moves = new ArrayList<>();
		int i2;
		i2 = i + VUNIT + HUNIT;
		while (isEmpty(i2)) {
			moves.add(new TiledMove(i, i2));
			i2 +=  VUNIT + HUNIT;
		}
		if (isBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i + VUNIT - HUNIT;
		while (isEmpty(i2)) {
			moves.add(new TiledMove(i, i2));
			i2 +=  VUNIT - HUNIT;
		}
		if (isBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i - VUNIT + HUNIT;
		while (isEmpty(i2)) {
			moves.add(new TiledMove(i, i2));
			i2 -=  VUNIT - HUNIT;
		}
		if (isBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i - VUNIT - HUNIT;
		while (isEmpty(i2)) {
			moves.add(new TiledMove(i, i2));
			i2 -=  VUNIT + HUNIT;
		}
		if (isBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		return moves;
	}

	private Collection<Move> getRookMoves(int i) {
		Collection<Move> moves = new ArrayList<>();
		int i2;
		i2 = i + VUNIT;
		while (isEmpty(i2)) {
			moves.add(new TiledMove(i, i2));
			i2 +=  VUNIT;
		}
		if (isBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i + HUNIT;
		while (isEmpty(i2)) {
			moves.add(new TiledMove(i, i2));
			i2 +=  HUNIT;
		}
		if (isBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i - VUNIT;
		while (isEmpty(i2)) {
			moves.add(new TiledMove(i, i2));
			i2 -= VUNIT;
		}
		if (isBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i - HUNIT;
		while (isEmpty(i2)) {
			moves.add(new TiledMove(i, i2));
			i2 -= HUNIT;
		}
		if (isBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		return moves;
	}

	private Collection<Move> getPawnMoves(int i, int enPassant) {
		Collection<Move> moves = new ArrayList<>();
		if (isEmpty(i + VUNIT)) {
			moves.add(new TiledMove(i, i + VUNIT));
			if (i / VUNIT == 3) {
				if (tiles[i + 2*VUNIT].isEmpty()) {
					moves.add(new TiledMove(i, i + 2*VUNIT));
				}
			}
		}
		if (isBlack(i + VUNIT + HUNIT)) {
			moves.add(new TiledMove(i, i + VUNIT + HUNIT));
		} else if (enPassant != Position.EN_PASSANT_NONE && (i / VUNIT == 6) && ((i + HUNIT) % VUNIT) == enPassant) {
			moves.add(new TiledMove(i, i + VUNIT + HUNIT));
		}
		if (isBlack(i + VUNIT - HUNIT)) {
			moves.add(new TiledMove(i, i + VUNIT - HUNIT));
		} else if (enPassant != Position.EN_PASSANT_NONE && (i / VUNIT == 6) && ((i - HUNIT) % VUNIT) == enPassant) {
			moves.add(new TiledMove(i, i + VUNIT - HUNIT));
		}
		if (i / VUNIT == 8 && !moves.isEmpty()) {
			Collection<Move> promotions = new ArrayList<>();
			for (Move m : moves) {
				promotions.add(new TiledMove((TiledMove) m, Piece.QUEEN));
				promotions.add(new TiledMove((TiledMove) m, Piece.ROOK));
				promotions.add(new TiledMove((TiledMove) m, Piece.BISHOP));
				promotions.add(new TiledMove((TiledMove) m, Piece.KNIGHT));
			}
			return promotions;
		}
		return moves;
	}
	
	private Collection<Move> getKnightMoves(int i) {
		Collection<Move> moves = new ArrayList<>();
		int i2;
		i2 = i + VUNIT + 2*HUNIT;
		if (isEmptyOrBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i + VUNIT - 2*HUNIT;
		if (isEmptyOrBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i + 2*VUNIT + HUNIT;
		if (isEmptyOrBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i + 2*VUNIT - HUNIT;
		if (isEmptyOrBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i - VUNIT + 2*HUNIT;
		if (isEmptyOrBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i - VUNIT - 2*HUNIT;
		if (isEmptyOrBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i - 2*VUNIT + HUNIT;
		if (isEmptyOrBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		i2 = i - 2*VUNIT - HUNIT;
		if (isEmptyOrBlack(i2)) {
			moves.add(new TiledMove(i, i2));
		}
		return moves;
	}
	
	@Override
	public boolean isCheck() {
		return isAttacked(king);
	}
	
	private boolean isAttacked(int i) {
		int i2;
		int distance;
		// Check diagonals (queen, bishop, king, pawn)
		i2 = i + VUNIT + HUNIT;
		distance = 1;
		while (isEmpty(i2)) {
			i2 +=  VUNIT + HUNIT;
			distance ++;
		}
		if (isBlack(i2)) {
			if (isQueen(i2) || isBishop(i2)) return true;
			if (distance == 1 && (isPawn(i2) || isKing(i2))) return true;
		}
		i2 = i + VUNIT - HUNIT;
		distance = 1;
		while (isEmpty(i2)) {
			i2 +=  VUNIT - HUNIT;
			distance ++;
		}
		if (isBlack(i2)) {
			if (isQueen(i2) || isBishop(i2)) return true;
			if (distance == 1 && (isPawn(i2) || isKing(i2))) return true;
		}
		i2 = i - VUNIT - HUNIT;
		distance = 1;
		while (isEmpty(i2)) {
			i2 -=  VUNIT + HUNIT;
			distance ++;
		}
		if (isBlack(i2)) {
			if (isQueen(i2) || isBishop(i2)) return true;
			if (distance == 1 && isKing(i2)) return true;
		}
		i2 = i - VUNIT + HUNIT;
		distance = 1;
		while (isEmpty(i2)) {
			i2 -=  VUNIT - HUNIT;
			distance ++;
		}
		if (isBlack(i2)) {
			if (isQueen(i2) || isBishop(i2)) return true;
			if (distance == 1 && isKing(i2)) return true;
		}
		// Check lines (queen, rook, king)
		i2 = i + VUNIT;
		distance = 1;
		while (isEmpty(i2)) {
			i2 +=  VUNIT;
			distance ++;
		}
		if (isBlack(i2)) {
			if (isQueen(i2) || isRook(i2)) return true;
			if (distance == 1 && isKing(i2)) return true;
		}
		i2 = i + HUNIT;
		distance = 1;
		while (isEmpty(i2)) {
			i2 +=  HUNIT;
			distance ++;
		}
		if (isBlack(i2)) {
			if (isQueen(i2) || isRook(i2)) return true;
			if (distance == 1 && isKing(i2)) return true;
		}
		i2 = i - VUNIT;
		distance = 1;
		while (isEmpty(i2)) {
			i2 -= VUNIT;
			distance ++;
		}
		if (isBlack(i2)) {
			if (isQueen(i2) || isRook(i2)) return true;
			if (distance == 1 && isKing(i2)) return true;
		}
		i2 = i - HUNIT;
		distance = 1;
		while (isEmpty(i2)) {
			i2 -= HUNIT;
			distance ++;
		}
		if (isBlack(i2)) {
			if (isQueen(i2) || isRook(i2)) return true;
			if (distance == 1 && isKing(i2)) return true;
		}
		// Check knights
		if (isKnight(i + VUNIT + 2*HUNIT)) return true;
		if (isKnight(i + VUNIT - 2*HUNIT)) return true;
		if (isKnight(i + 2*VUNIT + HUNIT)) return true;
		if (isKnight(i + 2*VUNIT - HUNIT)) return true;
		if (isKnight(i - VUNIT + 2*HUNIT)) return true;
		if (isKnight(i - VUNIT - 2*HUNIT)) return true;
		if (isKnight(i - 2*VUNIT + HUNIT)) return true;
		if (isKnight(i - 2*VUNIT - HUNIT)) return true;
		return false;
	}

	private boolean isKing(int i2) {
		return tiles[i2].is(ByteTile.BLACK_KING);
	}

	private boolean isPawn(int i2) {
		return tiles[i2].is(ByteTile.BLACK_PAWN);
	}

	private boolean isBishop(int i2) {
		return tiles[i2].is(ByteTile.BLACK_BISHOP);
	}

	private boolean isQueen(int i2) {
		return tiles[i2].is(ByteTile.BLACK_QUEEN);
	}
	
	private boolean isRook(int i2) {
		return tiles[i2].is(ByteTile.BLACK_ROOK);
	}
	
	private boolean isKnight(int i2) {
		return tiles[i2].is(ByteTile.BLACK_KNIGHT);
	}

	private boolean isEmpty(int i) {
		return tiles[i].isEmpty();
	}
	
	private boolean isBlack(int i) {
		return tiles[i].isBlack();
	}
	
	private boolean isEmptyOrBlack(int i) {
		return (tiles[i].isEmpty() || tiles[i].isBlack());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append('\n');
		for (int i = 0; i < SIZE; i ++) {
			sb.append(Integer.toHexString(tiles[i].toByte()));
			if (i % 10 == 9) sb.append('\n');
		}
		sb.append("White King Index: ");
		sb.append(king);
		return sb.toString();
	}

	@Override
	public char getMoveColumn1(Move move, Color colorToPlay) {
		if (colorToPlay == Color.WHITE) {
			return "abcdefgh".charAt((((TiledMove) move).getIndex1() % VUNIT) - 1);
		} else {
			return "hgfedcba".charAt((((TiledMove) move).getIndex1() % VUNIT) - 1);
		}
	}
	
	@Override
	public char getMoveColumn2(Move move, Color colorToPlay) {
		if (colorToPlay == Color.WHITE) {
			return "abcdefgh".charAt((((TiledMove) move).getIndex2() % VUNIT) - 1);
		} else {
			return "hgfedcba".charAt((((TiledMove) move).getIndex2() % VUNIT) - 1);
		}
	}
	
	@Override
	public char getMoveRow1(Move move, Color colorToPlay) {
		if (colorToPlay == Color.WHITE) {
			return "12345678".charAt((((TiledMove) move).getIndex1() / VUNIT) - 2);
		} else {
			return "87654321".charAt((((TiledMove) move).getIndex1() / VUNIT) - 2);
		}
	}
	
	@Override
	public char getMoveRow2(Move move, Color colorToPlay) {
		if (colorToPlay == Color.WHITE) {
			return "12345678".charAt((((TiledMove) move).getIndex2() / VUNIT) - 2);
		} else {
			return "87654321".charAt((((TiledMove) move).getIndex2() / VUNIT) - 2);
		}
	}
	
	@Override
	public boolean isCapture(Move move) {
		return (tiles[((TiledMove) move).getIndex2()].hasPiece() || isEnPassant(move));
	}
	
	@Override
	public boolean isEnPassant(Move move) {
		TiledMove tMove = (TiledMove) move;
		int index1 = tMove.getIndex1();
		if (!tiles[index1].hasPiece(Piece.PAWN)) return false;
		int index2 = tMove.getIndex2();
		if (tiles[index2].hasPiece()) return false;
		return (index2 == index1 + HUNIT + VUNIT || index2 == index1 - HUNIT + VUNIT);
	}

	@Override
	public boolean isCastleKing(Move move) {
		TiledMove tMove = (TiledMove) move;
		if (tMove.getIndex1() != king) return false;
		if (king == 25) {
			return (tMove.getIndex2() == 27);
		} else if (king == 24 ){
			return (tMove.getIndex2() == 22);
		} else {
			return false;
		}
	}

	@Override
	public boolean isCastleQueen(Move move) {
		TiledMove tMove = (TiledMove) move;
		if (tMove.getIndex1() != king) return false;
		if (king == 25) {
			return (tMove.getIndex2() == 23);
		} else if (king == 24 ){
			return (tMove.getIndex2() == 26);
		} else {
			return false;
		}
	}

	@Override
	public int getTwoPushColumn(Move move) {
		TiledMove tMove = (TiledMove) move;
		int index1 = tMove.getIndex1();
		if (!tiles[index1].hasPiece(Piece.PAWN)) return Position.EN_PASSANT_NONE;
		if (tMove.getIndex2() != index1 + 2*VUNIT) return Position.EN_PASSANT_NONE;
		return index1 % VUNIT;
	}

	@Override
	public boolean preventCastleKing(Color color, Move move) {
		TiledMove tMove = (TiledMove) move;
		switch (color) {
		case WHITE:
			int index1 = tMove.getIndex1();
			return (index1 == king || (king == 25 && index1 == 28) || (king == 24 && index1 == 21));
		case BLACK:
			if (tiles[95].hasPiece(Piece.KING)) {
				return (tMove.getIndex2() == 98);
			} else {
				return (tMove.getIndex2() == 91);
			}
		default:
			throw new IllegalArgumentException("Illegal color argument");
		}
	}

	@Override
	public boolean preventCastleQueen(Color color, Move move) {
		TiledMove tMove = (TiledMove) move;
		int index1 = tMove.getIndex1();
		switch (color) {
		case WHITE:			
			return (index1 == king || (king == 25 && index1 == 21) || (king == 24 && index1 == 28));
		case BLACK:
			if (tiles[95].hasPiece(Piece.KING)) {
				return (tMove.getIndex2() == 91);
			} else {
				return (tMove.getIndex2() == 98);
			}
		default:
			throw new IllegalArgumentException("Illegal color argument");
		}
	}

	@Override
	public String getPiece(Move move) {
		switch (tiles[((TiledMove) move).getIndex1()].getPiece()) {
		case KING:
			return "K";
		case QUEEN:
			return "Q";
		case ROOK:
			return "R";
		case BISHOP:
			return "B";
		case KNIGHT:
			return "N";
		case PAWN:
			return "";
		default:
			throw new IllegalStateException("Illegal piece");
		}
	}
}
