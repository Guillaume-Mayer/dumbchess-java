package chess.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Base64;
import java.util.Collection;
import java.util.Optional;

public class CorePosition implements Position {
		
	private Board board;
	private CastlingFlags castlingFlags;
	private int enPassant;
	
	private transient Collection<Move> legalMoves;
	private transient Optional<Boolean> check = Optional.empty();
	
	private static CorePosition initialPosition;
	
	public CorePosition(Board board, CastlingFlags castlingFlags, int enPassant) {
		this.board = board;
		this.castlingFlags = castlingFlags;
		this.enPassant = enPassant;
	}
	
	public static CorePosition loadFromResource(String resource) throws IOException {
		byte[] bytes = new byte[33];
		CorePosition.class.getClassLoader().getResourceAsStream(resource).read(bytes);
		return newInstance(bytes); 
	}
	
	public static CorePosition loadFromFile(String fileName) throws IOException {
		try (FileInputStream fos = new FileInputStream(fileName)) {
			byte[] bytes = new byte[33];
			fos.read(bytes);
			return newInstance(bytes); 
		}
	}
	
	public static CorePosition initial() {
		if (initialPosition == null) {
			initialPosition = new CorePosition(
					TiledBoard.initial(),
					ByteCastlingFlags.initial(),
					EN_PASSANT_NONE
					);
		}
		return initialPosition;
	}
	
	public static CorePosition newInstance(byte[] bytes) {
		return new CorePosition(
				new TiledBoard(bytes),
				new ByteCastlingFlags((byte) ((bytes[32] & 0xf0) >> 4)),
				(bytes[32] & 0x0f)
				);
	}

	@Override
	public Collection<Move> getLegalMoves() {
		if (legalMoves == null) {
			legalMoves = board.getMoves(
					castlingFlags.kingSide(Color.WHITE),
					castlingFlags.queenSide(Color.WHITE),
					enPassant
					);
		}
		return legalMoves;
	}
	
	@Override
	public CorePosition play(Move move) {
		int newEnPassant = board.getTwoPushColumn(move);
		return new CorePosition(
				board.play(move),
				new ByteCastlingFlags(
						castlingFlags.kingSide(Color.BLACK) && !board.preventCastleKing(Color.BLACK, move),
						castlingFlags.queenSide(Color.BLACK) && !board.preventCastleQueen(Color.BLACK, move),
						castlingFlags.kingSide(Color.WHITE) && !board.preventCastleKing(Color.WHITE, move),
						castlingFlags.queenSide(Color.WHITE) && !board.preventCastleQueen(Color.WHITE, move)
						),
				newEnPassant == EN_PASSANT_NONE ? newEnPassant : 9 - newEnPassant
				);
	}
	
	@Override
	public boolean isCheck() {
		if (!check.isPresent()) {
			check = Optional.of(board.isCheck());
		}
		return check.get();
	}

	@Override
	public boolean isDraw() {
		return (getLegalMoves().isEmpty() && !isCheck());
	}

	@Override
	public boolean isMate() {
		return (getLegalMoves().isEmpty() && isCheck());
	}

	@Override
	public String toHex() {
		byte[] bytes = new byte[34];
		System.arraycopy(toByteArray(), 0, bytes, 1, 33);
		return new BigInteger(bytes).toString(16);
	}

	@Override
	public String toBase64() {
		return Base64.getUrlEncoder().encodeToString(toByteArray());
	}

	private byte[] toByteArray() {
		byte[] bytes = new byte[33];
		System.arraycopy(board.toByteArray(), 0, bytes, 0, 32);
		bytes[32]= (byte) ((castlingFlags.toByte() << 4) | enPassant);
		return bytes;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(board.toString());
		sb.append("\nWhite right to castle king-side: ");
		sb.append(castlingFlags.kingSide(Color.WHITE) ? "Yes" : "No");
		sb.append("\nWhite right to castle queen-side: ");
		sb.append(castlingFlags.queenSide(Color.WHITE) ? "Yes" : "No");
		sb.append("\nBlack right to castle king-side: ");
		sb.append(castlingFlags.kingSide(Color.BLACK) ? "Yes" : "No");
		sb.append("\nBlack right to castle queen-side: ");
		sb.append(castlingFlags.queenSide(Color.BLACK) ? "Yes" : "No");
		sb.append("\nEn-passant column: ");
		sb.append(enPassant);
		sb.append("\nIs Check: ");
		sb.append(isCheck());
		sb.append("\nIs Draw: ");
		sb.append(isDraw());
		sb.append("\nIs Mate: ");
		sb.append(isMate());
		return sb.toString();
	}
	
	public void write(OutputStream out) throws IOException {
		out.write(toByteArray());
		out.flush();
	}

	public char getMoveColumn1(Move move, Color colorToPlay) {
		return board.getMoveColumn1(move, colorToPlay);
	}
	
	public char getMoveColumn2(Move move, Color colorToPlay) {
		return board.getMoveColumn2(move, colorToPlay);
	}
	
	public char getMoveRow1(Move move, Color colorToPlay) {
		return board.getMoveRow1(move, colorToPlay);
	}
	
	public char getMoveRow2(Move move, Color colorToPlay) {
		return board.getMoveRow2(move, colorToPlay);
	}
	
	public boolean isCapture(Move move) {
		return (board.isCapture(move) || isEnPassant(move));
	}

	public boolean isEnPassant(Move move) {
		if (enPassant == EN_PASSANT_NONE) return false;
		return board.isEnPassant(move);
	}

	public boolean isCastleKing(Move move) {
		if (!castlingFlags.kingSide(Color.WHITE)) return false;
		return board.isCastleKing(move);
	}

	public boolean isCastleQueen(Move move) {
		if (!castlingFlags.queenSide(Color.WHITE)) return false;
		return board.isCastleQueen(move);
	}

	public String getPiece(Move move) {
		return board.getPiece(move);
	}
	
}
