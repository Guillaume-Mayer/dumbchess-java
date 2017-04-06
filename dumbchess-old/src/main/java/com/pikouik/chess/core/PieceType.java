package com.pikouik.chess.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum PieceType {
	KING  ('K', "K"), 
	QUEEN ('Q', "Q"), 
	ROOK  ('R', "R"), 
	BISHOP('B', "B"), 
	KNIGHT('N', "N"), 
	PAWN  ('P', "" );
	
	private char fenRepr;
	private String algRepr;
	private int value;
	
	PieceType(char fenRepr, String algRepr) {
		this.fenRepr = fenRepr;
		this.algRepr = algRepr;
	}
	
	static {
		try {
			loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadProperties() throws IOException {
		Properties props = new Properties();
		try (InputStream stream =  PieceType.class.getClassLoader().getResourceAsStream("piece.properties")) {
			props.load(stream);
			KING.value   = Integer.parseInt(props.getProperty("king.value"));
			QUEEN.value  = Integer.parseInt(props.getProperty("queen.value"));
			ROOK.value   = Integer.parseInt(props.getProperty("rook.value"));
			BISHOP.value = Integer.parseInt(props.getProperty("bishop.value"));
			KNIGHT.value = Integer.parseInt(props.getProperty("knight.value"));
			PAWN.value   = Integer.parseInt(props.getProperty("pawn.value"));
		}
	}

	int getValue() {
		return value;
	}

	public String getAlgRepr() {
		return algRepr;
	}

	char getFenRepr() {
		return fenRepr;
	}
	
	public static PieceType getFromAlgRepr(String alg) {
		if (KING.getAlgRepr().equals(alg)) return KING;
		if (QUEEN.getAlgRepr().equals(alg)) return QUEEN;
		if (ROOK.getAlgRepr().equals(alg)) return ROOK;
		if (BISHOP.getAlgRepr().equals(alg)) return BISHOP;
		if (KNIGHT.getAlgRepr().equals(alg)) return KNIGHT;
		return PAWN;
	}
	
}
