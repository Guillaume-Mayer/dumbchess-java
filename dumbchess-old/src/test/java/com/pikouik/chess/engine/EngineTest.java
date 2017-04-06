package com.pikouik.chess.engine;

import static org.junit.Assert.*;

import org.junit.*;

import com.pikouik.chess.core.InvalidFenException;
import com.pikouik.chess.core.Move;
import com.pikouik.chess.core.Position;
import com.pikouik.chess.core.PositionClassic;

public class EngineTest {
	
	@Test
	public void testEvaluate() {
		Position pos = new PositionClassic()
				.play("e2-e4")
				.play("e7-e5")
				.play("Qd1-h5");
		EngineClassic engine = new EngineClassic(pos, 1);
		assertEquals(-91, engine.evaluate());
	}
	
	@Ignore
	@Test
	public void testDepth1() {
		Position pos = new PositionClassic();
		pos = pos.play("e2-e4");
		EngineClassic engine = new EngineClassic(pos, 1);
		engine.getBestMove();
		assertEquals(0, engine.getDetails().getScore());
		assertEquals("e7-e6", engine.getDetails().getMoves());
	}
	
	@Ignore
	@Test
	public void testDepth2() {
		Position pos = new PositionClassic();
		pos = pos.play("e2-e4");
		EngineClassic engine = new EngineClassic(pos, 2);
		engine.getBestMove();
		assertEquals(-91, engine.getDetails().getScore());
		assertEquals("e7-e5, Qd1-h5", engine.getDetails().getMoves());
	}
	
	@Ignore
	@Test
	public void testDepth3() {
		Position pos = new PositionClassic();
		pos = pos.play("e2-e4");
		EngineClassic engine = new EngineClassic(pos, 3);
		engine.getBestMove();
		assertEquals(28, engine.getDetails().getScore());
		assertEquals("e7-e6, Qd1-h5, Qd8-g5", engine.getDetails().getMoves());
	}
	
	@Ignore
	@Test
	public void testDepth4() {
		Position pos = new PositionClassic();
		pos = pos.play("e2-e4");
		EngineClassic engine = new EngineClassic(pos, 4);
		Move best = engine.getBestMove();
		assertEquals("e7-e6", pos.moveToAlg(best));
	}
	
	@Ignore
	@Test
	public void testDepth5() {
		Position pos = new PositionClassic();
		pos = pos.play("e2-e4");
		EngineClassic engine = new EngineClassic(pos, 5);
		Move best = engine.getBestMove();
		assertEquals("e7-e6", pos.moveToAlg(best));
	}
	
	@Ignore
	@Test
	public void testCheckMate() throws InvalidFenException {
		Position pos = new PositionClassic("rnb1kb1r/1ppp1N2/p4Q1p/4ppp1/1P3P2/P4PP1/1P1P4/RNBK1B1R w - - 1 17");
		EngineClassic engine = new EngineClassic(pos, 5);
		Move best = engine.getBestMove();
		System.out.println(pos.moveToAlg(best));
		//assertEquals("e7-e6", pos.moveToAlg(best));
		
	}

}
