package chess.core;

public class IllegalMoveException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;
	
	public IllegalMoveException(String message) {
		super(message);
	}
}
