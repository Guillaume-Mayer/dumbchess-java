package chess.core;

import java.util.Optional;

class TiledMove implements Move {
	
	private int index1;
	private int index2;
	private Optional<Piece> promotion;
	
	public TiledMove(int index1, int index2) {
		this.index1 = index1;
		this.index2 = index2;
		this.promotion = Optional.empty();
	}

	public TiledMove(TiledMove original, Piece promotion) {
		this.index1 = original.index1;
		this.index2 = original.index2;
		this.promotion = Optional.of(promotion);
	}
	
	@Override
	public boolean isPromotion() {
		return promotion.isPresent();
	}
	
	@Override
	public Piece getPromotion() {
		return promotion.get();
	}

	@Override
	public String toString() {
		return "TiledMove [index1=" + index1 + ", index2=" + index2 + ", promotion=" + promotion + "]";
	}

	public int getIndex1() {
		return index1;
	}

	public int getIndex2() {
		return index2;
	}

}
