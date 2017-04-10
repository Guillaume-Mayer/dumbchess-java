package chess.old;

import static org.junit.Assert.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.*;

public class PositionTest {
	
	private Position newPosition() {
		return new PositionClassic();
	}
		
	@Test
	public void testValue() {
		Position pos = newPosition();
		assertEquals(0, pos.getMaterialScore());
	}
		
	@Test
	public void testPositionFromFen() throws InvalidFenException {
		Position pos = newPosition();
		assertEquals(Const.INITIAL_FEN, pos.toFen());		
	}

	@Test
	public void testColor() {
		assertSame(Color.WHITE, Color.BLACK.swap());
		assertSame(Color.BLACK, Color.WHITE.swap());
	}
		
	@Test
	public void testPlayAndUnplay() {
		PositionClassic pos = (PositionClassic) newPosition();
		assertEquals(Const.INITIAL_FEN, pos.toFen());
		// Play
		Move m1 = pos.moveFromAlg("e2-e4");
		pos.play(m1);
		assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", pos.toFen());
		Move m2 = pos.moveFromAlg("c7-c5");
		pos.play(m2);
		assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2", pos.toFen());
		Move m3 = pos.moveFromAlg("Ng1-f3");
		pos.play(m3);
		assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2", pos.toFen());
		// Unplay
		pos.unplay((MoveClassic) m3);
		assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2", pos.toFen());
		pos.unplay((MoveClassic) m2);
		assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", pos.toFen());
		pos.unplay((MoveClassic) m1);
		assertEquals(Const.INITIAL_FEN, pos.toFen());
	}
	
	@Test
	public void testLegalMovesInitial() {
		// Build the expected moves list
		List<String> expected = new ArrayList<String>(20);
		expected.add("a2-a3");
		expected.add("a2-a4");
		expected.add("b2-b3");
		expected.add("b2-b4");
		expected.add("c2-c3");
		expected.add("c2-c4");
		expected.add("d2-d3");
		expected.add("d2-d4");
		expected.add("e2-e3");
		expected.add("e2-e4");
		expected.add("f2-f3");
		expected.add("f2-f4");
		expected.add("g2-g3");
		expected.add("g2-g4");
		expected.add("h2-h3");
		expected.add("h2-h4");
		expected.add("Nb1-a3");
		expected.add("Nb1-c3");
		expected.add("Ng1-f3");
		expected.add("Ng1-h3");
		expected.sort(String::compareTo);
		// Compare with the actual move list
		Position pos = newPosition();
		assertEquals(
				expected,
				pos.getLegalMoves()
					.stream()
					.map(m -> pos.moveToAlg(m))
					.sorted()
					.collect(Collectors.toList())
				);
	}
	
	@Test
	public void testLegalMovesAfterE2E4() {
		// Build the expected moves list
		List<String> expected = new ArrayList<String>(20);
		expected.add("a7-a6");
		expected.add("a7-a5");
		expected.add("b7-b6");
		expected.add("b7-b5");
		expected.add("c7-c6");
		expected.add("c7-c5");
		expected.add("d7-d6");
		expected.add("d7-d5");
		expected.add("e7-e6");
		expected.add("e7-e5");
		expected.add("f7-f6");
		expected.add("f7-f5");
		expected.add("g7-g6");
		expected.add("g7-g5");
		expected.add("h7-h6");
		expected.add("h7-h5");
		expected.add("Nb8-a6");
		expected.add("Nb8-c6");
		expected.add("Ng8-f6");
		expected.add("Ng8-h6");
		expected.sort(String::compareTo);
		// Compare with the actual move list
		Position pos = newPosition().play("e2-e4");
		assertEquals(
				expected,
				pos.getLegalMoves()
					.stream()
					.map(m -> pos.moveToAlg(m))
					.sorted()
					.collect(Collectors.toList())				
				);
	}
	
	@Test
	public void testLegalMovesAfterC7C5() {
		// Build the expected moves list
		List<String> expected = new ArrayList<String>();
		expected.add("a2-a3");
		expected.add("a2-a4");
		expected.add("b2-b3");
		expected.add("b2-b4");
		expected.add("c2-c3");
		expected.add("c2-c4");
		expected.add("d2-d3");
		expected.add("d2-d4");
		expected.add("e4-e5");
		expected.add("f2-f3");
		expected.add("f2-f4");
		expected.add("g2-g3");
		expected.add("g2-g4");
		expected.add("h2-h3");
		expected.add("h2-h4");
		expected.add("Nb1-a3");
		expected.add("Nb1-c3");
		expected.add("Ng1-f3");
		expected.add("Ng1-h3");
		expected.add("Ng1-e2");
		expected.add("Bf1-e2");
		expected.add("Bf1-d3");
		expected.add("Bf1-c4");
		expected.add("Bf1-b5");
		expected.add("Bf1-a6");
		expected.add("Qd1-e2");
		expected.add("Qd1-f3");
		expected.add("Qd1-g4");
		expected.add("Qd1-h5");
		expected.add("Ke1-e2");
		expected.sort(String::compareTo);
		// Get the actual move list
		Position pos = newPosition()
				.play("e2-e4")
				.play("c7-c5");
		// Assert both lists are equals regardless on order
		assertEquals(
				expected,
				pos.getLegalMoves()
					.stream()
					.map(m -> pos.moveToAlg(m))
					.sorted()
					.collect(Collectors.toList())
				);
	}

	@Test
	public void testLegalMovesAfterNG1F3() {
		// Build the expected moves list
		List<String> expected = new ArrayList<String>();
		expected.add("a7-a6");
		expected.add("a7-a5");
		expected.add("b7-b6");
		expected.add("b7-b5");
		expected.add("c5-c4");
		expected.add("d7-d6");
		expected.add("d7-d5");
		expected.add("e7-e6");
		expected.add("e7-e5");
		expected.add("f7-f6");
		expected.add("f7-f5");
		expected.add("g7-g6");
		expected.add("g7-g5");
		expected.add("h7-h6");
		expected.add("h7-h5");
		expected.add("Nb8-a6");
		expected.add("Nb8-c6");
		expected.add("Ng8-f6");
		expected.add("Ng8-h6");
		expected.add("Qd8-c7");
		expected.add("Qd8-b6");
		expected.add("Qd8-a5");
		expected.sort(String::compareTo);
		// Compare with the actual move list
		Position pos = newPosition()
				.play("e2-e4")
				.play("c7-c5")
				.play("Ng1-f3");
		// Assert both lists are equals regardless on order
		assertEquals(
				expected,
				pos.getLegalMoves()
					.stream()
					.map(m -> pos.moveToAlg(m))
					.sorted()
					.collect(Collectors.toList())
				);
	}
	
	@Test
	public void testLegalMovesAfterD7D6() {
		// Build the expected moves list
		List<String> expected = new ArrayList<String>();
		expected.add("a2-a3");
		expected.add("a2-a4");
		expected.add("b2-b3");
		expected.add("b2-b4");
		expected.add("c2-c3");
		expected.add("c2-c4");
		expected.add("d2-d3");
		expected.add("d2-d4");
		expected.add("e4-e5");
		expected.add("g2-g3");
		expected.add("g2-g4");
		expected.add("h2-h3");
		expected.add("h2-h4");
		expected.add("Nb1-a3");
		expected.add("Nb1-c3");
		expected.add("Nf3-g1");
		expected.add("Nf3-h4");
		expected.add("Nf3-g5");
		expected.add("Nf3-e5");
		expected.add("Nf3-d4");
		expected.add("Bf1-e2");
		expected.add("Bf1-d3");
		expected.add("Bf1-c4");
		expected.add("Bf1-b5+");
		expected.add("Bf1-a6");
		expected.add("Qd1-e2");
		expected.add("Ke1-e2");
		expected.add("Rh1-g1");
		expected.sort(String::compareTo);
		// Compare with the actual move list
		Position pos = newPosition()
				.play("e2-e4")
				.play("c7-c5")
				.play("Ng1-f3")
				.play("d7-d6");
		assertEquals(
				expected,
				pos.getLegalMoves()
					.stream()
					.map(m -> pos.moveToAlg(m))
					.sorted()
					.collect(Collectors.toList())
					);
	}
	
	@Test
	public void testLegalMovesAfterD2D4() {
		// Build the expected moves list
		List<String> expected = new ArrayList<String>();
		expected.add("a7-a6");
		expected.add("a7-a5");
		expected.add("b7-b6");
		expected.add("b7-b5");
		expected.add("c5-c4");
		expected.add("c5xd4");
		expected.add("d6-d5");
		expected.add("e7-e6");
		expected.add("e7-e5");
		expected.add("f7-f6");
		expected.add("f7-f5");
		expected.add("g7-g6");
		expected.add("g7-g5");
		expected.add("h7-h6");
		expected.add("h7-h5");
		expected.add("Nb8-a6");
		expected.add("Nb8-c6");
		expected.add("Nb8-d7");
		expected.add("Ng8-f6");
		expected.add("Ng8-h6");
		expected.add("Qd8-c7");
		expected.add("Qd8-b6");
		expected.add("Qd8-a5+");
		expected.add("Qd8-d7");
		expected.add("Ke8-d7");
		expected.add("Bc8-d7");
		expected.add("Bc8-e6");
		expected.add("Bc8-f5");
		expected.add("Bc8-g4");
		expected.add("Bc8-h3");
		expected.sort(String::compareTo);
		// Compare with the actual move list
		Position pos = newPosition()
				.play("e2-e4")
				.play("c7-c5")
				.play("Ng1-f3")
				.play("d7-d6")
				.play("d2-d4");
		assertEquals(
				expected,
				pos.getLegalMoves()
					.stream()
					.map(m -> pos.moveToAlg(m))
					.sorted()
					.collect(Collectors.toList())
				);
	}
	
	@Test
	public void testRandomPlayAndUnplay() {
		PositionClassic pos = (PositionClassic) newPosition();
		Color color = Color.WHITE;
		boolean castlingQ = false , castlingK = false, enPassant = false, promote = false;
		while (!(castlingQ && castlingK && enPassant && promote)) {
			Random rand = new Random();
			Deque<Move> played = new ArrayDeque<>();
			List<Move> moves = pos.getLegalMoves(color);
			while (!moves.isEmpty() && !pos.isDraw()) {
				Move m = moves.get(rand.nextInt(moves.size()));
				pos.play(m);
				played.addFirst(m);
				color = color.swap();
				moves = pos.getLegalMoves(color);
				if (m.getCastling() == Side.QUEEN) {
					castlingQ = true;
				} else if (m.getCastling() == Side.KING) {
					castlingK = true;
				} else if (m.isEnPassant()) {
					enPassant = true;
				} else if (m.isPromote()) {
					promote = true;
				}
			}
			String fen = pos.toFen();
			assertTrue(fen.indexOf('K') != -1);
			assertTrue(fen.indexOf('k') != -1);
			while (!played.isEmpty()) {
				pos.unplay((MoveClassic) played.removeFirst());
			}
			assertEquals(Const.INITIAL_FEN, pos.toFen());
		}
	}
	
	@Test
	public void testEnPassant() {
		Position pos = newPosition()
				.play("e2-e4")
				.play("a7-a6")
				.play("e4-e5")
				.play("d7-d5");
		List<Move> enPassant = pos.getLegalMoves()
			.stream()
			.filter(m -> m.isEnPassant())
			.collect(Collectors.toList());				
		assertEquals(1, enPassant.size());
		pos = pos.play(enPassant.get(0));
		assertEquals(-100, pos.getMaterialScore());
	}
	
}
