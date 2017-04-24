package chess.core;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Optional;

import org.junit.Test;

public class ColorPositionTest {

	@Test
	public void test_a2a4_e7e6_Ra1a3() {
		final ColorPosition pos0 = ColorPosition.initial();
		Collection<Move> moves = pos0.getLegalMoves();
		Optional<Move> a2a4 = moves.stream()
				.filter(m -> pos0.moveToAlgeb(m).equals("a2-a4"))
				.findAny();
		assertTrue(a2a4.isPresent());
		final ColorPosition pos1 = pos0.play(a2a4.get());
		//System.out.println("After a2-a4:\n" + pos1);
		moves = pos1.getLegalMoves();
		Optional<Move> e7e6 = moves.stream()
				.filter(m -> pos1.moveToAlgeb(m).equals("e7-e6"))
				.findAny();
		assertTrue(e7e6.isPresent());
		final ColorPosition pos2 = pos1.play(e7e6.get());
		//System.out.println("After e7-e6:\n" + pos2);
		moves = pos2.getLegalMoves();
		Optional<Move> Ra1a3 = moves.stream()
				.filter(m -> pos2.moveToAlgeb(m).equals("Ra1-a3"))
				.findAny();
		assertTrue(Ra1a3.isPresent());
		final ColorPosition pos3 = pos2.play(Ra1a3.get());
		//System.out.println("After Ra1-a3:\n" + pos3);
		moves = pos3.getLegalMoves();
		
	}
	
	@Test
	public void testCastleQueenSide() {
		final ColorPosition pos0 = ColorPosition.initial();
		final ColorPosition pos1 =
				pos0.play("f2-f3")
					.play("a7-a6")
					.play("g2-g4")
					.play("b7-b6")
					.play("b2-b3")
					.play("d7-d6")
					.play("d2-d3")
					.play("Nb8-d7")
					.play("Ng1-h3")
					.play("Bc8-b7")
					.play("g4-g5")
					.play("e7-e6")
					.play("Nb1-c3")
					.play("Bb7-e4")
					.play("Bc1-a3")
					.play("Bf8-e7")
					.play("f3xe4")
					.play("Be7-f6")
					.play("Nh3-g1")
					.play("Nd7-f8")
					.play("Ba3-c5")
					.play("Ra8-b8")
					.play("Bc5-a3")
					.play("b6-b5")
					.play("Qd1-d2")
					.play("c7-c5");
		// Check that queen castling is possible
		assertEquals(
				1,
				pos1.getLegalMoves().stream()
					.filter(m -> "O-O-O".equals(pos1.moveToAlgeb(m)))
					.count()
		);
		final ColorPosition pos2 = pos1.play("Ba3xc5")
					.play("h7-h5")
					.play("Qd2-e3")
					.play("Rb8-b7")
					.play("Bf1-h3")
					.play("Qd8-b6")
					.play("g5-g6");
		// Check that queen castling is not possible
		assertEquals(
				0,
				pos2.getLegalMoves().stream()
					.filter(m -> "O-O-O".equals(pos2.moveToAlgeb(m)))
					.count()
		);
	}
	@Test
	public void testCastleKing() {
		ColorPosition pos = ColorPosition.initial()
				.play("c2-c4")
				.play("Nb8-c6")
				.play("Nb1-c3")
				.play("Nc6-d4")
				.play("b2-b4")
				.play("f7-f6")
				.play("g2-g3")
				.play("a7-a5")
				.play("Bc1-b2")
				.play("e7-e6")
				.play("Ng1-f3")
				.play("d7-d5")
				.play("Qd1-b1")
				.play("Ra8-b8")
				.play("Nc3-d1")
				.play("b7-b6")
				.play("Nd1-c3")
				.play("Rb8-a8")
				.play("c4xd5")
				.play("Nd4-f5")
				.play("Bb2-c1")
				.play("Qd8xd5")
				.play("Qb1-b3")
				.play("g7-g6")
				.play("Bc1-b2")
				.play("Bf8-e7")
				.play("b4-b5")
				.play("Qd5xd2+")
				.play("Nf3xd2")
				.play("Be7-c5")
				.play("Bf1-h3")
				.play("h7-h6");
		// Check that castle king side is possible
		assertTrue(
			pos.getLegalMoves().stream()
				.anyMatch(m -> "O-O".equals(pos.moveToAlgeb(m))));
	}
	
	@Test
	public void testCastleKing2() {
		ColorPosition pos = ColorPosition.initial()
				.play("Ng1-h3")
				.play("Ng8-f6")
				.play("Nh3-g1")
				.play("Nf6-h5")
				.play("g2-g3")
				.play("a7-a6")
				.play("d2-d4")
				.play("Nh5xg3")
				.play("e2-e3")
				.play("f7-f6")
				.play("Bf1-g2")
				.play("Ng3-f5")
				.play("Nb1-c3")
				.play("Nf5-h6")
				.play("Bg2-c6")
				.play("Nh6-f5")
				.play("Bc6xb7")
				.play("Nf5-h6")
				.play("Ra1-b1")
				.play("Nh6-f5")
				.play("Bc1-d2")
				.play("Nf5-g3")
				.play("Bb7-d5")
				.play("Ng3xh1")
				.play("a2-a4")
				.play("d7-d6")
				.play("f2-f3")
				.play("g7-g5")
				.play("Nc3-a2")
				.play("c7-c6")
				.play("Rb1-c1")
				.play("Bc8-h3")
				.play("b2-b4")
				.play("h7-h6")
				.play("Bd5-b3")
				.play("g5-g4")
				.play("Bb3-e6")
				.play("Nh1-g3")
				.play("Bd2-c3")
				.play("f6-f5")
				.play("Qd1-d2")
				.play("Qd8-c8")
				.play("Qd2-d3")
				.play("g4xf3")
				.play("Ng1xh3")
				.play("a6-a5")
				.play("Qd3-a6")
				.play("Ra8xa6")
				.play("Nh3-g1")
				.play("Ra6-b6")
				.play("Be6-f7+")
				.play("Ke8-d7")
				.play("Bf7-g6")
				.play("Ng3-h1")
				.play("Ng1xf3")
				.play("Qc8-c7");
		// Check that castle king side is not possible
		assertFalse(
			pos.getLegalMoves().stream()
				.anyMatch(m -> "O-O".equals(pos.moveToAlgeb(m))));
	}
	
}
