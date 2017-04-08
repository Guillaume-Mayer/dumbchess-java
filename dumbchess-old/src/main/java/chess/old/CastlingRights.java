package chess.old;

public interface CastlingRights {

	public boolean has(Color color, Side side);
	public void set(Color color, Side side);
	public boolean unset(Color color, Side side);
	
	public default void unset(Color color) {
		unset(color, Side.KING);
		unset(color, Side.QUEEN);
	}
	
	public default void setFull() {
		set(Color.WHITE, Side.KING);
		set(Color.WHITE, Side.QUEEN);
		set(Color.BLACK, Side.KING);
		set(Color.BLACK, Side.QUEEN);
	}
	
	public CastlingRights copy();
	
	public void initFromFen(String fen) throws InvalidFenException;
	public void toFen(StringBuilder fen);
	
}
