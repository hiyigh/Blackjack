package blackjack;

import java.util.ArrayList;

public class PlayerInfo {
	public boolean surrender = false;
	public boolean bust = false;
	public int bet = 0;
	public int num;
	public int score = 0;
	public int splitCnt = 0;
	public String msg = null;
	public ArrayList<Card> cards = new ArrayList<>();
}
