package chess;

import java.util.List;

public class ChessResponse {
	
	private String colorToPlay;
	private String fen;
	private List<String> legalMoves;
	private List<String> actions;
	private List<String> history;
	
	public ChessResponse() {
	}
	
	public String getColorToPlay() {
		return colorToPlay;
	}
		
	public void setColorToPlay(String colorToPlay) {
		this.colorToPlay = colorToPlay;
	}

	public String getFen() {
		return fen;
	}
		
	public void setFen(String fen) {
		this.fen = fen;
	}

	public List<String> getLegalMoves() {
		return legalMoves;
	}
		
	public void setLegalMoves(List<String> legalMoves) {
		this.legalMoves = legalMoves;
	}

	public List<String> getActions() {
		return actions;
	}
		
	public void setActions(List<String> actions) {
		this.actions = actions;
	}

	public List<String> getHistory() {
		return history;
	}

	public void setHistory(List<String> history) {
		this.history = history;
	}
		
}
