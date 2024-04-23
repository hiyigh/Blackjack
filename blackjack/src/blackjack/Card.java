package blackjack;

import java.io.Serializable;

public class Card implements Serializable{
	private boolean hidden;
	private int playerNum;
	public int num;
	public int shape;
	public void setPlayerNum(int pn) {
		this.playerNum = pn;
	}
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	public boolean getHidden() {
		return this.hidden;
	}
	public Card(){
	}
	public Card(int num, int shape, boolean hidden) {
		this.num = num;
		this.shape = shape;
		this.hidden = hidden;
	}
	public int getNum() {
		return this.num;
	}
}
