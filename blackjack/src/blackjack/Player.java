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
	private static final Scanner sc = new Scanner(System.in);
	private static ObjectInputStream ois = null;
	private static ObjectOutputStream oos = null;

	//user info
	private ArrayList<Split> splitList = new ArrayList<>();
	private int playerNumber;
<<<<<<< HEAD
	private Request request;
	private int totalBet;
=======
	private Cmd cmd;
	private int totalBet = 0;
>>>>>>> 54b0f5cdedc5a7d90ede0c4aa7f3bfe014494351
	private int money;
	private int splitCnt = 0;
	
	private boolean surrenflag = false;
	private boolean bustflag = false;
	
	public static void main (String[] args) {
		Player player = new Player();
	}
	public Player() {
		try {
			Socket ss = new Socket("192.168.20.133",serverPort);
			ois = new ObjectInputStream(ss.getInputStream());
			oos = new ObjectOutputStream(ss.getOutputStream());
			money = 1000;
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
<<<<<<< HEAD
					
					receiveMsg();
					receiveBoard();				
					// 0 hit, 1 stand, 2 bet, 3 split, 4 surrender, 5 double down , 6 bust
					sendRequest();
					receiveBoard();
					
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
=======
					while(true) {
						receiveMsg();
						
						displayBoard();
						
>>>>>>> 54b0f5cdedc5a7d90ede0c4aa7f3bfe014494351
						
					}
				}
			});
			th.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
<<<<<<< HEAD
	private void sendRequest(Request re, int number) {
		re.setcNum(number);
		switch(number) {
		case 0:
			re.setMsg("hit");
			break;
		case 1:
			re.setMsg("stand");
			break;
		case 2:
			re.setMsg("bet");
			re.setBet(intScanner(sc, 10, money));
			break;
		case 3:
			re.setMsg("split");
			break;
		case 4:
			re.setMsg("surrender");
			break;
		case 5:
			re.setMsg("doubledown");
			break;
		case 6:
			re.setMsg("bust");
			break;
		}
	}
	// stand , bet , split, double down, surrender, bust, counting
	private int intScanner(Scanner sc, int start, int end) {
=======
	private void sendCmd() {
		Cmd cmd = new Cmd();
		int cn = sendInt(sc, 0, 5);
		if (cn == 2) {
			cmd.bet = sendInt(sc, 10, money);		
		} else if (cn == 3){
			Split split = new Split();
			splitList.add(split);
			splitCnt += 1;
			cmd.splitCnt = splitCnt;
		} else if (cn == 4) {
			surrenflag = true;
		} else if (cn == 6) {
			bustflag = true;
		} 
		cmd.setCmd(cn, playerNumber);
		
		try {
			oos.writeObject(cmd);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//hit, stand , bet , split, surrender, double down, bust, counting
	private int sendInt(Scanner sc, int start, int end) {
>>>>>>> 54b0f5cdedc5a7d90ede0c4aa7f3bfe014494351
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
<<<<<<< HEAD
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
	private void receiveBoard() {
=======
	
	private void displayBoard() {
>>>>>>> 54b0f5cdedc5a7d90ede0c4aa7f3bfe014494351
		try {
			Object obj = ois.readObject();
			if (obj instanceof char[][]) {
				char[][] board = (char[][])obj;
				for (int i = 0; i < board.length; ++i) {
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
	
}
