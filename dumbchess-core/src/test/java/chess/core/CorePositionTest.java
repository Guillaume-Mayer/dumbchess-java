package chess.core;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CorePositionTest {
	
	private CorePosition pos;
	
	private static final String fileNameSer = "files/corePosition.ser";
	private static final String fileNameDumb = "files/initialpos.dumbchess";
	
	@Before
	public void setUp() throws Exception {
		pos = CorePosition.initial();
	}

	@Test
	public void testInitialTiledBoard() {
		assertEquals(20, pos.getLegalMoves().stream().distinct().count());
		assertEquals("abcdecbaffffffff000000000000000000000000000000006666666612345321f0", pos.toHex());
		assertEquals("q83suv____8AAAAAAAAAAAAAAAAAAAAAZmZmZhI0UyHw", pos.toBase64());
		assertFalse(pos.isCheck());
		assertFalse(pos.isDraw());
		assertFalse(pos.isMate());
	}
	
	@Ignore
	@Test
	public void testSerialzation() throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileNameSer))) {
			oos.writeObject(pos);
		}
		File file = new File(fileNameSer);
		assertTrue(file.exists());
		assertEquals(1171, file.length());
	}
	
	@Ignore
	@Test
	public void testDeserialzation() throws IOException, ClassNotFoundException {
		try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(fileNameSer))) {
			CorePosition p = (CorePosition) oos.readObject();
			assertEquals(pos, p);
		}
	}
	
	@Test
	public void testSaveToFile() throws IOException {
		try (FileOutputStream fos = new FileOutputStream(fileNameDumb)) {
			pos.write(fos);
		}
	}
	
	@Test
	public void testLoadFromFile() throws IOException {
		CorePosition p = CorePosition.loadFromFile(fileNameDumb);
		assertEquals(pos.toString(), p.toString());
		assertEquals(pos.toHex(), p.toHex());
		assertEquals(pos.toBase64(), p.toBase64());
	}
	
	@Test
	public void testLoadFromResource() throws IOException {
		CorePosition p = CorePosition.loadFromResource("initialpos.dumbchess");
		assertEquals(pos.toString(), p.toString());
		assertEquals(pos.toHex(), p.toHex());
		assertEquals(pos.toBase64(), p.toBase64());
	}
	
	@After
	public void tearDown() throws Exception {
		
	}

}
