package chess.core;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ColorPositionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_a2a4_e7e6_Ra1a3() {
		final ColorPosition pos0 = ColorPosition.initial();
		Collection<Move> moves = pos0.getLegalMoves();
		Optional<Move> a2a4 = moves.stream()
				.filter(m -> pos0.moveToAlgeb(m).equals("a2-a4"))
				.findAny();
		assertTrue(a2a4.isPresent());
		final ColorPosition pos1 = pos0.play(a2a4.get());
		System.out.println("After a2-a4:\n" + pos1);
		moves = pos1.getLegalMoves();
		Optional<Move> e7e6 = moves.stream()
				.filter(m -> pos1.moveToAlgeb(m).equals("e7-e6"))
				.findAny();
		assertTrue(e7e6.isPresent());
		final ColorPosition pos2 = pos1.play(e7e6.get());
		System.out.println("After e7-e6:\n" + pos2);
		moves = pos2.getLegalMoves();
		Optional<Move> Ra1a3 = moves.stream()
				.filter(m -> pos2.moveToAlgeb(m).equals("Ra1-a3"))
				.findAny();
		assertTrue(Ra1a3.isPresent());
		final ColorPosition pos3 = pos2.play(Ra1a3.get());
		System.out.println("After Ra1-a3:\n" + pos3);
		moves = pos3.getLegalMoves();
		
	}

}
