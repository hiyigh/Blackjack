package blackjack;

import java.io.Serializable;

public class Card implements Serializable{
	private int num;
	private int shape;
	public boolean hidden;
	public Card(int num, int shape, boolean hidden) {
		this.num = num;
		this.shape = shape;
		this.hidden = hidden;
	}
	public int getNum() {
		return num;
	}
	public int getShape() {
		return shape;
	}
	public boolean isHidden() {
		return hidden;
	}
}
