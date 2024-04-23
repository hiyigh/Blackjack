package blackjack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import blackjack.SSocket.PlayerThread;

public class Dealer {
	private static boolean[][] deck = new boolean[4][13];
	private static ArrayList<Card> dealerCardList;
	private static ArrayList<Player> winnerList = new ArrayList<>();
	private static int playerScore = -1;
	private static int playerNum = -1;
	
	private static int dealerScore;
	private static final int seventeen = 17;
	private static int highscore = 0;
	
	private static Dealer dealer = null;
	
	private Dealer(){
		dealerScore = 0;
		dealerCardList = new ArrayList<Card>();
	}
	public static Dealer getDealer() {
		if(dealer == null) {
			return new Dealer();
		}
		return dealer;
	}
	public Card deal(int turn, int playerChoice, boolean open) {
		if (0 < turn && turn < 3) {
			return selectCard(open);
		}
		// player hit
		if (playerChoice == 0) {
			return selectCard(open);
		}
		return null;
	}
	public Card selectCard(boolean open) {
		int shape;
		int idx;
		do {
			shape = (int)(Math.random() * 4);
			idx = (int)(Math.random() * 13);		
		}while(deck[shape][idx] == true);
		
		deck[shape][idx] = true;
		return new Card(shape, idx, open);
	}
	public String hit() {
		if (seventeen < dealerScore) {
			dealerCardList.add(selectCard(true));
			calculateDealerScore();
			return "dealer hit";
		} else {
			return "dealer stand";
		}
	}
	public void calculateDealerScore() {
		for (int i =0; i < dealerCardList.size(); ++i) {			
			dealerScore += dealerCardList.get(i).getNum();
		}
	}
	public int selectWinner(ArrayList<Player> playerList) {
		if(playerList == null) {
			System.out.println("playerList is null");
			return -1;
		}
		for (int i = 0; i < playerList.size(); ++i) {
			if (!(playerList.get(i).splitList.isEmpty())) {
				selectWinner(playerList.get(i).splitList);
			}
			playerScore = playerList.get(i).openCard(); 
			if(playerScore == 21) {
				winnerList.add(playerList.get(i));				
			} else if (playerScore >= 22) {
				//bust
				return -1;
			} else if (highscore < playerScore) {
				highscore = playerScore;
				playerNum = playerList.get(i).getPlayerNum();
			}
		}
		return playerNum;
	}
}
