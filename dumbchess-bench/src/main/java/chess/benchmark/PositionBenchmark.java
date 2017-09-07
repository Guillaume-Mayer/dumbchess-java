package chess.benchmark;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;

import chess.core.ColorPosition;
import chess.old.PositionClassic;

@State(Scope.Benchmark)
public class PositionBenchmark {

	private ColorPosition posI;
	private PositionClassic posM;
	private List<Integer> randomChoices;
	private static final int NB_MOVES = 7;

	@Setup(Level.Trial)
	public void prepareRandomChoices() {
		posI = ColorPosition.initial();
		posM = new PositionClassic();
		randomChoices = new ArrayList<>(NB_MOVES);
		//List<String> randomMoves = new ArrayList<>(NB_MOVES);
		Random random = new Random(1);
		while (randomChoices.size() < NB_MOVES) {
	        Collection<chess.core.Move> movesI = posI.getLegalMoves();
	        List<chess.old.Move> movesM = posM.getLegalMoves();
	        if (movesI.size() != movesM.size() || movesI.isEmpty()) {
	        	throw new RuntimeException("Different moves number or none");
	        }
	        int rand = random.nextInt(movesI.size());
	        chess.old.Move moveM = movesM.get(rand);
	        String sMove = posM.moveToAlg(moveM);
	        //randomMoves.add(sMove);
	        chess.core.Move moveI = movesI.stream().filter(m -> posI.moveToAlgeb(m).equals(sMove)).findAny().get();
	        posM.play(moveM);
	        posI = posI.play(moveI);
	        randomChoices.add(rand);
		}
	}

    @Benchmark
    public void playImmutable() {
    	posI = ColorPosition.initial();
        List<chess.core.Move> moves =
        	posI.getLegalMoves().stream()
        		.sorted((a,b) -> posI.moveToAlgeb(a).compareTo(posI.moveToAlgeb(b)))
        		.collect(Collectors.toList());
        int count = 0;
        while (!moves.isEmpty() && count < NB_MOVES) {
        	posI.play(moves.get(randomChoices.get(count)));
        	moves =
        			posI.getLegalMoves().stream()
                		.sorted((a,b) -> posI.moveToAlgeb(a).compareTo(posI.moveToAlgeb(b)))
                		.collect(Collectors.toList());
        	count++;
        }
    }

    @Benchmark
    public void playMutable() {
    	posM = new PositionClassic();
        List<chess.old.Move> moves =
        	posM.getLegalMoves().stream()
				.sorted((a,b) -> posM.moveToAlg(a).compareTo(posM.moveToAlg(b)))
				.collect(Collectors.toList());
        int count = 0;
        while (!moves.isEmpty() && count < NB_MOVES) {
        	posM.play(moves.get(randomChoices.get(count)));
        	moves =
                	posM.getLegalMoves().stream()
        				.sorted((a,b) -> posM.moveToAlg(a).compareTo(posM.moveToAlg(b)))
        				.collect(Collectors.toList());
        	count++;
        }
   }

}
