package chess;

import java.util.ArrayList;
import java.util.List;

public class ChessRequest {
	
	private List<String> history;
	
	public ChessRequest() {
		history = new ArrayList<>();
	}
	
	public List<String> getHistory() {
		return history;
	}

	public void setHistory(List<String> history) {
		this.history = history;
	}
	
	public void clear() {
		this.history.clear();
	}
}
