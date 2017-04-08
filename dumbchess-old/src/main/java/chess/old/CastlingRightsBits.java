package chess.old;

public class CastlingRightsBits implements CastlingRights {
	
	private int castlingRights;
	
	private static final int NONE = 0b0000;
	private static final int FULL = 0b1111;
		
	public CastlingRightsBits() {
	}

	private CastlingRightsBits(int castlingRights) {
		this.castlingRights = castlingRights;
	}

	@Override
	public boolean has(Color color, Side side) {
		int bitMask = ((side.ordinal() + 1) << (2*color.ordinal()));
		return ((castlingRights & bitMask) == bitMask);
	}

	@Override
	public void set(Color color, Side side) {
		int bitMask = ((side.ordinal() + 1) << (2*color.ordinal()));
		castlingRights |= bitMask;
	}

	@Override
	public boolean unset(Color color, Side side) {
		if (has(color, side)) {
			int bitMask = ((side.ordinal() + 1) << (2*color.ordinal()));
			castlingRights &= ~bitMask;
			return true;
		}
		return false;
	}
	
	@Override
	public void setFull() {
		castlingRights = FULL;
	}
	
	@Override
	public CastlingRights copy() {
		return new CastlingRightsBits(castlingRights);
	}

	@Override
	public void toFen(StringBuilder fen) {
		if (castlingRights == 0) {
			fen.append("-");
		} else {
			if (has(Color.WHITE, Side.KING)) fen.append("K");
			if (has(Color.WHITE, Side.QUEEN)) fen.append("Q");
			if (has(Color.BLACK, Side.KING)) fen.append("k");
			if (has(Color.BLACK, Side.QUEEN)) fen.append("q");
		}
	}

	@Override
	public void initFromFen(String fen) throws InvalidFenException {
		castlingRights = NONE;
		if (!"-".equals(fen)) {
			for (char ch : fen.toCharArray()) {
				switch (ch) {
				case 'K':
					set(Color.WHITE, Side.KING);
					break;
				case 'Q':
					set(Color.WHITE, Side.QUEEN);
					break;
				case 'k':
					set(Color.BLACK, Side.KING);
					break;
				case 'q':
					set(Color.BLACK, Side.QUEEN);
					break;
				default:
					throw new InvalidFenException();
				}
			}
		}
	}
	
}
