package blackjack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Player {
	private static final int serverPort = 7777;
	private final Scanner sc = new Scanner(System.in);
	private static ObjectInputStream ois = null;
	private static ObjectOutputStream oos = null;
	
	private static String[][] board = new String[16][60];

	//user info
	private ArrayList<Card> cards = new ArrayList<>();
	private ArrayList<Split> splitList = new ArrayList<>();
	private int playerNumber;
	private Cmd cmd;
	private int totalBet;
	private int money;
	
	public static void main (String[] args) {
		Player player = new Player();
	}
	public Player() {
		try {
			Socket ss = new Socket("192.168.20.133",serverPort);
			ois = new ObjectInputStream(ss.getInputStream());
			oos = new ObjectOutputStream(ss.getOutputStream());
			money = 1000;
			cmd = new Cmd();
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					//cmd
					//if splitList is not null // send split count 
					//for loop
					receiveMsg();
					displayBoard();				
					// 0 hit, 1 stand, 2 bet, 3 split, 4 surrender, 5 double down , 6 bust
					checkCmd(2, cmd);
					
					sendCmd(cmd);
					displayBoard();
					
					//first betting
					Card card = receiveCard();
					sendCard(card);	
					
					card = receiveCard();
					sendCard(card);	
					
					boolean onGame = true;
					boolean onPlay = true;
					while(onGame) {
						if (onPlay) {
							System.out.println("your turn");
							System.out.println("0 hit, 1 stand, 2 bet, 3 split, 4 surrender, 5 double down , 6 bust");
							int i = receiveInt(sc, 0, 6);
							onPlay = checkCmd(i, cmd);
							sendCmd(cmd);
							if (receiveMsg().equals("open")) {
								onPlay = false;
								onGame = false;
								sendScore();
							}
							displayBoard();
						} else {
							displayBoard();
						}
						
					}
				}
			});
			th.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private boolean checkCmd(int i , Cmd cmd) {
		switch(i) {
		case 0:
			cmd.setCmd(i, playerNumber);
			break;
		case 1:
			cmd.setCmd(i, playerNumber);
			break;
		case 2:
			int b = receiveInt(sc, 10, money);
			cmd.setCmd(i, playerNumber);
			cmd.setBet(b);
			break;
		case 3:
			break;
		case 4:
			cmd.setCmd(i, playerNumber);
			return false;
		case 5:
			cmd.setCmd(i, playerNumber);
			break;
		case 6:
			return false;
		}
		return true;
	}
	// stand , bet , split, double down, surrender, bust, counting
	private int receiveInt(Scanner sc, int start, int end) {
		int cmd;
		while(true) {
			try {
				cmd = sc.nextInt();
				if (start <= cmd && cmd <= end) {
					break;					
				}
			} catch (InputMismatchException e) {
				System.out.println("숫자만 입력");
			}
			System.out.println(start + " 부터" + end + " 까지 입력가능");
		}
		return cmd;
	}
	private Card receiveCard() {
		try {
			Object obj = ois.readObject();
			if (obj instanceof Card) {
				Card c = (Card)obj;
				cards.add(c);
				return c;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private String receiveMsg() {
		String msg = "";
		try {
			Object obj = (Object)ois.readUTF();
			if (obj instanceof String) {
				msg = (String)obj;
				System.out.println(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	private void sendScore() {
		int score = 0;
		for(int i = 0; i < cards.size(); ++i) {
			score += cards.get(i).getNum();
		}
		try {
			oos.writeInt(score);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void sendCard(Card c) {
		try {
			String str = null;
			if (c.isHidden()) {
				str = "□ □";
			} else {
				char shape = setCardShape(c.getShape());
				int number =c.getNum();
				str = shape +""+number;				
			}
			oos.writeObject(str);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		displayBoard();
	}
	private void sendCmd(Cmd cmd) {
		try {
			oos.writeObject(cmd);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void displayBoard() {
		try {
			Object obj = ois.readObject();
			if(obj instanceof String[][]) {
				board = (String[][])obj;			
				for (int i = 0 ; i < board.length; ++i) {
					for (int j = 0; j < board[i].length; ++j) {
						System.out.print(board[i][j]);
					}
					System.out.println();
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void setPlayerNumber() {
		try {
			this.playerNumber = ois.readInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private char setCardShape(int shape) {
		char sh = 0;
		switch(shape) {
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
	
}
