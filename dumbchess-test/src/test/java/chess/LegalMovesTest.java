package chess;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.Ignore;
import org.junit.Test;

import chess.core.ColorPosition;
import chess.old.PositionClassic;

public class LegalMovesTest {

	private static final int LOOP = 100;
	private static final int HALF_MOVE_MAX = 100;
	private static final Random random = new Random(2);

	private interface IPosition {
		void init();
		List<String> getLegalMoves();
		void play(String move);
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
		public void play(String alg) {
			Optional<chess.core.Move> move = pos.getLegalMoves().stream().filter(m -> pos.moveToAlgeb(m).equals(alg)).findAny();
			assertTrue("JavaNew::play move is present", move.isPresent());
			pos = pos.play(move.get());
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
		public void play(String alg) {
			Optional<chess.old.Move> move = pos.getLegalMoves().stream().filter(m -> pos.moveToAlg(m).equals(alg)).findAny();
			assertTrue("JavaOld::play move is present", move.isPresent());
			pos.play(move.get());
		}

	}

	private class JavaScript implements IPosition {

		private WebTarget target = ClientBuilder.newClient().target("http://localhost:3000/chess");

		@Override
		public void init() {
			target.path("/restart").request().get();
		}

		@Override
		public List<String> getLegalMoves() {
			return target.request().get(ChessResponse.class).getLegalMoves().stream().map(m -> m.substring(12)).sorted().collect(Collectors.toList());
		}

		@Override
		public void play(String alg) {
			Response resp = target.path("/play/" + alg).request().get();
			assertEquals("JavaScript::play not OK (" + alg + ")", 200, resp.getStatus());
		}

	}


	@Test
	public void testRandomPlayJavaNewVsJavaOld() {
		randomPlays(new JavaNew(), new JavaOld());
	}

	@Test
	public void testRandomPlayJavaNewVsJavaScript() {
		randomPlays(new JavaNew(), new JavaScript());
	}

	@Test
	@Ignore
	public void testRandomPlayJavaOldVsJavaScript() {
		randomPlays(new JavaOld(), new JavaScript());
	}

	private void randomPlays(IPosition p1, IPosition p2) {
		for (int i = 0; i < LOOP; i++) {
			p1.init();
			p2.init();
			int halfMoveCount = 0;
			while (halfMoveCount < HALF_MOVE_MAX && assertAndPlay(p1, p2) > 0) {
				halfMoveCount ++;
			}
		}
	}

	private int assertAndPlay(IPosition p1, IPosition p2) {

		// Get the legal moves
		List<String> moves1 = p1.getLegalMoves();
		List<String> moves2 = p2.getLegalMoves();

		int count = moves1.size();
		if (count == 0) {
			// Check that pos2 has no moves
			assertEquals(0, moves2.size());
			return 0;
		}

		// Compare the legal moves
		assertEquals(moves1, moves2);

		// Pick a random move
		String randomMove = moves1.get(random.nextInt(count));

		// Play it
		p1.play(randomMove);
		p2.play(randomMove);

		return count;
	}

}
