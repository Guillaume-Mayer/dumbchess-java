package chess;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

import chess.core.ColorPosition;
import chess.core.Move;

public class Main {

	public static void main(String[] args) throws IOException {

		Properties props = new Properties();
		props.load(Main.class.getClassLoader().getResourceAsStream("dumbchess.properties"));

		System.out.println("Welcome to Dumb Chess " + props.getProperty("version"));
				
		ColorPosition cPos = ColorPosition.initial();
		Random random = new Random();
		int index = 1;
		while(index < 20) {
			List<Move> moves = cPos.getLegalMoves().stream().collect(Collectors.toList());
			Move m = moves.get(random.nextInt(moves.size()));
			System.out.println("--------------- " + cPos.moveToAlgeb(m) + " ------------------");
			cPos = cPos.play(m);
			System.out.println(cPos);
			index ++;
		}
				
		System.exit(0);
	}

}
