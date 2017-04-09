package chess;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
	public void testLegalMoves() {
		int halfMoveCount = 0;
		while (halfMoveCount < 400 && assertAndPlay() > 0) {
			halfMoveCount ++;
		}
	}
		
	private int assertAndPlay() {
		
		// Compare the legal moves count
		Collection<chess.core.Move> moves1 = pos1.getLegalMoves();
		List<chess.old.Move> moves2 = pos2.getLegalMoves();
		int count = moves1.size();
		assertEquals(count, moves2.size());
		if (count == 0) return 0;
		
		// Compare the legal moves content
		assertEquals(
				moves1.stream().map(m -> pos1.moveToAlgeb(m)).sorted().collect(Collectors.toList()),
				moves2.stream().map(m -> pos2.moveToAlg(m)).sorted().collect(Collectors.toList())
				);
		
		// Make a map alg -> move1
		Map<String, chess.core.Move> mapMoves1 =
				moves1.stream()
					.collect(
							Collectors.toMap(
									(m -> pos1.moveToAlgeb(m)),
									(m -> m)
									));
		// Select a random move
		int rand = random.nextInt(count);
		chess.old.Move move = moves2.get(rand);
		String sMove = pos2.moveToAlg(move);
		
		// Print it
		System.out.println(rand + ":" + sMove);
		
		// Play it
		pos1 = pos1.play(mapMoves1.get(sMove));
		pos2.play(move);
		
		return count;
	}

}
