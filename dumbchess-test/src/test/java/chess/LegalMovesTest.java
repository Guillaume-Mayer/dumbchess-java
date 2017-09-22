package chess;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Test;

import chess.core.ColorPosition;
import chess.old.PositionClassic;

public class LegalMovesTest {

	private static final int LOOP = 300;
	private static final int HALF_MOVE_MAX = 300;
	private static final Random random = new Random(1);
	private static final WebTarget nodeJS = ClientBuilder.newClient().target("http://localhost:3000/chess");
	
	private interface IPosition {
		void init();
		List<String> getLegalMoves();
		boolean play(String move);
	}

	private class JavaNew implements IPosition {

		private ColorPosition pos;

		@Override
		public void init() {
			pos = ColorPosition.initial();
		}

		@Override
		public List<String> getLegalMoves() {
			return pos.getLegalMoves().stream().map(m -> pos.moveToAlgeb(m)).sorted().collect(Collectors.toList());
		}

		@Override
		public boolean play(String alg) {
			Optional<chess.core.Move> move = pos.getLegalMoves().stream().filter(m -> pos.moveToAlgeb(m).equals(alg)).findAny();
			if (move.isPresent()) {
				pos = pos.play(move.get());
				return true;
			}
			System.out.println("NEW: Illegal move (" + alg + ")");
			return false;
		}

	}

	private class JavaOld implements IPosition {

		private PositionClassic pos;

		@Override
		public void init() {
			pos = new PositionClassic();
		}

		@Override
		public List<String> getLegalMoves() {
			return pos.getLegalMoves().stream().map(m -> pos.moveToAlg(m)).sorted().collect(Collectors.toList());
		}

		@Override
		public boolean play(String alg) {
			Optional<chess.old.Move> move = pos.getLegalMoves().stream().filter(m -> pos.moveToAlg(m).equals(alg)).findAny();
			if (move.isPresent()) {
				pos.play(move.get());
				return true;
			}
			System.out.println("OLD: Illegal move (" + alg + ")");
			return false;
		}

	}

	private class JavaScript implements IPosition {
		
		List<String> history = new ArrayList<>();
		
		@Override
		public void init() {
			assertEquals(200, nodeJS.path("/restart").request().get().getStatus());
			history.clear();
		}

		@Override
		public List<String> getLegalMoves() {
			ChessRequest req = new ChessRequest();
			req.setHistory(history);
			//nodeJS.request().post(Entity.json(history), ChessResponse.class);
			return nodeJS.request().get(ChessResponse.class).getLegalMoves().stream().map(m -> m.substring(12)).sorted().collect(Collectors.toList());
		}

		@Override
		public boolean play(String alg) {
			if (nodeJS.path("/play/" + alg).request().get().getStatus() == 200) {
				history.add(alg);
				return true;
			}
			return false;
		}

	}


	@Test
	public void testRandomPlayJavaNewVsJavaOld() throws InterruptedException {
		System.out.println("*** NEW vs OLD ***");
		randomPlays(new JavaNew(), new JavaOld());
	}

	@Test
	public void testRandomPlayJavaNewVsJavaScript() throws InterruptedException {
		System.out.println("*** NEW vs JS ***");
		randomPlays(new JavaNew(), new JavaScript());
	}

	@Test
	public void testRandomPlayJavaOldVsJavaScript() throws InterruptedException {
		System.out.println("*** OLD vs JS ***");
		randomPlays(new JavaOld(), new JavaScript());
	}

	private void randomPlays(IPosition p1, IPosition p2) throws InterruptedException {
		for (int i = 0; i < LOOP; i++) {
			p1.init();
			p2.init();
			System.out.println("*** GAME " + i + " ***");
			int halfMoveCount = 0;
			while (halfMoveCount < HALF_MOVE_MAX && assertAndPlay(halfMoveCount, p1, p2) > 0) {
				halfMoveCount ++;
			}
			System.out.println("*** DONE ***");
		}
	}

	private int assertAndPlay(int halfMoveCount, IPosition p1, IPosition p2) throws InterruptedException {

		// Get the legal moves
		List<String> moves1 = p1.getLegalMoves();
		List<String> moves2 = p2.getLegalMoves();

		int count = moves1.size();
		if (count == 0) {
			// Check that pos2 has no moves
			assertEquals(0, moves2.size());
			System.out.println("*** NO MORE MOVE ***");
			return 0;
		}

		// Compare the legal moves
		assertEquals(moves1, moves2);

		// Pick a random move
		String randomMove = moves1.get(random.nextInt(count));
		System.out.println(halfMoveCount + ": " + randomMove);
		
		// Play it
		assertTrue(p1.play(randomMove));
		assertTrue(p2.play(randomMove));
		
		// This is to calm down the Jersey/Node communication
		Thread.sleep(20);
		
		return count;
	}

}
