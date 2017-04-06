package chess.benchmark;

import java.io.IOException;

import org.openjdk.jmh.annotations.Benchmark;

import chess.core.CorePosition;

public class PositionBenchmark {
	
    @Benchmark
    public void loadFromResource() throws IOException {
    	CorePosition.loadFromResource("initialpos.dumbchess");
    }

    @Benchmark
    public void loadFromFile() throws IOException {
    	CorePosition.loadFromFile("files/initialpos.dumbchess");
    }
    
}
