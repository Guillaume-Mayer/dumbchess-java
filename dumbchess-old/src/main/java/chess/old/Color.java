package chess.old;

public enum Color {
	BLACK(-1, 6, 3, 1),
	WHITE(+1, 1, 4, 6);
	
	int sens;
	int twoPushRow;
	int enPassantRow;
	int promoteRow;
	
	Color(int sens, int twoPushRow, int enPassantRow, int promoteRow) {
		this.sens = sens;
		this.twoPushRow = twoPushRow;
		this.enPassantRow = enPassantRow;
		this.promoteRow = promoteRow;
	}
	
	public Color swap() {
		if (this == BLACK) return WHITE;
		return BLACK;
	}

	public int getSens() {
		return sens;
	}

	public int getTwoPushRow() {
		return twoPushRow;
	}

	public int getEnPassantRow() {
		return enPassantRow;
	}

	public int getPromoteRow() {
		return promoteRow;
	}
	
}
