package chess.old;

public interface Move {
	
	public boolean isEnPassant();
	public Side getCastling();
	public boolean isPromote();
	
}
