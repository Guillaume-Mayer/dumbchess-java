package chess;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.Ignore;
import org.junit.Test;

import chess.core.ColorPosition;
import chess.old.PositionClassic;

public class LegalMovesTest {
	
	private ColorPosition pos1;
	private PositionClassic pos2;
	private WebTarget target = ClientBuilder.newClient().target("http://localhost:3000/chess");

	private Random random = new Random();
	
	@Ignore
	@Test
	public void testRandomPlay() {
		for (int i = 0; i < 200; i++) {
			randomPlay(i);
		}
	}
		
	private int randomPlay(long seed) {
		pos1 = ColorPosition.initial();
		pos2 = new PositionClassic();
		target.path("/restart").request().get();
		int halfMoveCount = 0;
		while (halfMoveCount < 200 && assertAndPlay(random) > 0) {
			halfMoveCount ++;
		}
		return halfMoveCount;
	}
		
	private int assertAndPlay(Random random) {
		
		// Compare the legal moves count
		Collection<chess.core.Move> moves1 = pos1.getLegalMoves();
		List<chess.old.Move> moves2 = pos2.getLegalMoves();
		List<String> moves3 = target.request().get(ChessResponse.class).getLegalMoves();
		
		int count = moves1.size();
		if (count == 0) {
			assertEquals(0, moves2.size());
			assertEquals(0, moves3.size());
			return 0;
		}
		
		// Compare the legal moves content
		assertEquals(
				moves1.stream().map(m -> pos1.moveToAlgeb(m)).sorted().collect(Collectors.toList()),
				moves2.stream().map(m -> pos2.moveToAlg(m)).sorted().collect(Collectors.toList())
				);
		
		// Compare the legal moves content
		assertEquals(
				moves1.stream().map(m -> pos1.moveToAlgeb(m)).sorted().collect(Collectors.toList()),
				moves3.stream().map(m -> m.substring(12)).sorted().collect(Collectors.toList())
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
		
		// Play it
		pos1 = pos1.play(mapMoves1.get(sMove));
		pos2.play(move);
		target.path("/play/" + sMove).request().get();
		
		return count;
	}
		
}
