package chess;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import chess.core.ColorPosition;
import chess.old.PositionClassic;

public class LegalMovesTest {
	
	ColorPosition pos1;
	PositionClassic pos2;
	Random random;
	
	@Before
	public void setUp() throws Exception {
		pos1 = ColorPosition.initial();
		pos2 = new PositionClassic();
		random = new Random();
	}

	@Test
	public void test() {
		Collection<chess.core.Move> moves1 = pos1.getLegalMoves();
		List<chess.old.Move> moves2 = pos2.getLegalMoves();
		int count = moves1.size();
		assertEquals(count, moves2.size());
		assertEquals(
				moves1.stream().map(m -> pos1.moveToAlgeb(m)).sorted().collect(Collectors.toList()),
				moves2.stream().map(m -> pos2.moveToAlg(m)).sorted().collect(Collectors.toList())
				);
		
	}

}
