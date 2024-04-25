package blackjack;

import java.io.Serializable;
import java.util.ArrayList;

public class Split extends Player {
	protected ArrayList<Card> cardList;
	protected int score;
	
	public ArrayList<Card> getCardList() {
		return cardList;
	}
	protected void setCardList(ArrayList<Card> cardList) {
		this.cardList = cardList;
	}
	protected int getScore() {
		return score;
	}
	protected void setScore(int score) {
		this.score = score;
	}
	
}
