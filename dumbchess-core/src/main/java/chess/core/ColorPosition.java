package chess.core;

import java.util.Collection;

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
		if (corePosition.isCastleKing(move)) {
			return "O-O";
		} else if (corePosition.isCastleQueen(move)) {
			return "O-O-O";
		}
		StringBuilder sb = new StringBuilder();
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
				sb.append('Q');
				break;
			case ROOK:
				sb.append('Q');
				break;
			case BISHOP:
				sb.append('Q');
				break;
			default:
				throw new IllegalStateException("Illegal promotion");
			}
		} else if (corePosition.isEnPassant(move)) {
			sb.append("ep");
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
}
