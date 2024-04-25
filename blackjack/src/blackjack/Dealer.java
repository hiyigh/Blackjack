package blackjack;

public class Dealer {
	private static Dealer dealer = null;
	
	private Dealer(){

	}
	public static Dealer callDealer() {
		if(dealer == null) {
			return new Dealer();
		}
		return dealer;
	}
	public Card hit(boolean[][] deck, boolean hidden) {
		int shape;
		int idx;
		do {
			shape = (int)(Math.random() * 4);
			idx = (int)(Math.random() * 13);		
		}while(deck[shape][idx] == true);
		
		deck[shape][idx] = true;
		return new Card(shape, idx + 1, hidden);
	}
}
