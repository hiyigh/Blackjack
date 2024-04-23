package blackjack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Player {
	private static final Scanner sc = new Scanner(System.in);
	private static DataOutputStream dos = null;
	private static DataInputStream dis = null;
	private static final int serverPort = 7777;
	
	private ArrayList<Card> cardList = new ArrayList<>();
	public ArrayList<Player> splitList = new ArrayList<>();
	private static String[][] board = new String[25][25];

	private int myScore;
	public int bet;
	private int chip;
	private int playerNum;
	private boolean splitUser;
	
	public static void main (String[] args) {
		Player player = new Player();
	}
	public Player() {		
		try {
			Socket ss = new Socket("192.168.20.133",serverPort);
			dis = new DataInputStream(ss.getInputStream());
			dos = new DataOutputStream(ss.getOutputStream());
			myScore = 0;
			playerNum = dis.readInt();
			
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					String msg = null;				
					if (splitUser) {
						
					}
					//welcome msg
					inputStr();
					//yes or no
					inputStr();
					sendChar();
									
					//start blackjack
					inputStr();
					//send player num
					sendInt(playerNum);
					//set chip
					setChip();

					//set card
					hit();
					hit();
					System.out.println(cardList.get(0).num);
					System.out.println(cardList.get(1).num);
					
					inputStr();
					//send cmd
				}			
			});
			th.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/* game method */
	public void setChip() {
		this.chip = inputInt();
	}
	public int getPlayerNum() {
		return this.playerNum;
	}

	private void setCard(Card card) {
		if (card == null ) {
			System.out.println("null card");
		}
		this.cardList.add(card);
	}
	private char setCardShape(Card card) {
		char sh = 0;
		switch(card.shape) {
		case 0:
			sh = '\u2665';
			break;
		case 1:
			sh = '\u2660';
			break;
		case 2:
			sh = '\u2666';
			break;
		case 3:
			sh = '\u2663';
			break;
		}
		return sh;
	}
	
	/* player cmd == 0 */
	public void hit() {
		ObjectInputStream objIn = null;
		try {
			objIn = new ObjectInputStream(dis);
			Object obj = objIn.readObject();
			if (obj instanceof Card) {
				Card card = (Card)obj;
				cardList.add(card);
			} else {
				System.out.println("hit error");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/* player cmd == 1 */
	public void stand() {
		sendInt(1);
	}
	/* player cmd == 2 */
	public void split() {
		if (!this.cardList.get(0).equals(this.cardList.get(1))) {
			System.out.println("cant split");
			return;
		}
		
		if ((this.chip / 2) < this.bet) {
			System.out.println("not enough chip");
			return;
		}
		
		Player newHand = new Player();
		newHand.setCard(this.cardList.get(0));
		cardList.remove(0);
		
		newHand.bet(this.bet);
		sendInt(2);
	}
	/* player cmd == 3 */
	private void bet(int bet) {
		this.bet = bet;
	}
	/* player cmd == 4 */
	public boolean surrender() {
		sendInt( 4);		
		return true;
	}
	/* player cmd == 5 */
	public int doubleDown() {
		return 0;
	}
	/* player cmd == 6 */
	public boolean bet(int bet, DataInputStream dis, DataOutputStream dos) {
		if (bet > chip) {
			System.out.println("need more chip");
			return false;
		}
		sendInt(5);
		sendInt(bet);
		return true;
	}
	/* player cmd == 7 */
	public int openCard(){
		for (int i = 0; i < cardList.size(); ++i) {
			myScore += cardList.get(i).num;
		}
		return this.myScore;
	}
	
	/* input method */
	private int inputInt() {
		int i = -1;
		boolean flag;
		do {
			try {
				i = dis.readInt();
				flag = true;
			} catch(IOException e) {
				flag = false;
			}
		}while(!flag);
		return i;
	}
	private void inputStr() {
		String msg;
		while(true) {
			try {
				msg = dis.readUTF();
				if (msg.equals("next")) {
					break;
				}
				System.out.println(msg);
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
	}
	
	/* send method */
	private void sendMsg(String msg) {
		try {
			dos.writeChars(msg);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void sendInt(int i) {
		try {
			dos.writeInt(i);
			dos.flush();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	private void sendChar() {
		try {
			dos.writeChar(sc.next().charAt(0));
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
}
