package chess.core;

import java.util.Collection;
import java.util.Optional;

public class ColorPosition implements Position {
	
	private CorePosition corePosition;
	private Color colorToPlay;
	
	public ColorPosition(CorePosition corePosition, Color colorToPlay) {
		this.corePosition = corePosition;
		this.colorToPlay = colorToPlay;
	}
	
	public static ColorPosition initial() {
		return new ColorPosition(CorePosition.initial(), Color.WHITE);
	}
	
	@Override
	public Collection<Move> getLegalMoves() {
		return corePosition.getLegalMoves();
	}
	
	@Override
	public ColorPosition play(Move move) {
		return new ColorPosition(corePosition.play(move), colorToPlay == Color.WHITE ? Color.BLACK : Color.WHITE);
	}
	
	@Override
	public boolean isCheck() {
		return corePosition.isCheck();
	}

	@Override
	public boolean isDraw() {
		return corePosition.isDraw();
	}

	@Override
	public boolean isMate() {
		return corePosition.isMate();
	}

	@Override
	public String toHex() {
		return corePosition.toHex();
	}

	@Override
	public String toBase64() {
		return corePosition.toBase64();
	}
	
	public String moveToAlgeb(Move move) {
		StringBuilder sb;
		if (corePosition.isCastleKing(move)) {
			sb = new StringBuilder("O-O");
		} else if (corePosition.isCastleQueen(move)) {
			sb = new StringBuilder("O-O-O");
		} else {
			sb = new StringBuilder();
			sb.append(corePosition.getPiece(move));
			sb.append(corePosition.getMoveColumn1(move, colorToPlay));
			sb.append(corePosition.getMoveRow1(move, colorToPlay));
			sb.append(corePosition.isCapture(move) ? 'x' : '-');
			sb.append(corePosition.getMoveColumn2(move, colorToPlay));
			sb.append(corePosition.getMoveRow2(move, colorToPlay));
			if (move.isPromotion()) {
				switch (move.getPromotion()) {
				case QUEEN:
					sb.append('Q');
					break;
				case KNIGHT:
					sb.append('N');
					break;
				case ROOK:
					sb.append('R');
					break;
				case BISHOP:
					sb.append('B');
					break;
				default:
					throw new IllegalStateException("Illegal promotion");
				}
			} else if (corePosition.isEnPassant(move)) {
				sb.append("ep");
			}
		}
		if (corePosition.play(move).isCheck()) {
			sb.append('+');
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(corePosition.toString());
		sb.append("\nColor to play: ");
		sb.append(colorToPlay);
		return sb.toString();
	}

	public ColorPosition play(String sMove) {
		Optional<Move> oMove = getLegalMoves().stream().filter(m -> moveToAlgeb(m).equals(sMove)).findAny();
		if (oMove.isPresent()) return play(oMove.get());
		throw new IllegalMoveException("Move \"" + sMove + "\" is not legal");
	}
}
