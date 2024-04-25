package blackjack;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SSocket {
	//테이블	
	private static boolean[][] deck = new boolean[4][13];
	private static String[][] board = new String[16][60];
	private static Dealer dealer;
	private static int turn;
	//플레이어
	private static boolean[] onPlayers = new boolean[3];
	private static boolean[] bustPlayers = new boolean[3];
	private static int[] betList = new int[3];
	private static int[] scoreList = new int[3];
	//소켓
	private static ArrayList<Table> members = new ArrayList<>();
	private static int cnt;
	
	public static void main(String[] args) {
		ServerSocket ss;
		try {
			ss = new ServerSocket(7777);
			Socket cs = null;
			boolean flag = false;
			while(!flag) {
				cs = ss.accept();
				if (cnt <= 3) {
					Table in = new Table(cs, cnt++);
					members.add(in);
					in.start();
				} else {
					Table out = new Table(cs, cnt);
					out.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class Table extends Thread {
		Socket cs = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		boolean inGame = false;
		boolean nextCmd = false;
		int pNum;
		
		public Table(Socket cs, int playerNumber) {
			try {
				this.cs = cs;
				oos = new ObjectOutputStream(cs.getOutputStream());
				ois = new ObjectInputStream(cs.getInputStream());
				pNum = playerNumber;
				onPlayers[pNum] = true;
			} catch (IOException e) {
				System.out.println("table stream error");
				e.printStackTrace();
			}
		}
		public void close() {
			try {
				oos.close();
				ois.close();
				this.cs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			setBoard();
			dealer = Dealer.callDealer();
			
			sendMsg(oos, "Hello");
			displayBoard();	
			//first bet
			receiveCmd(ois, pNum);
			displayBoard();
			//카드 전달, 저장, 출력
			sendCard(oos, dealer.hit(deck, false));
			receiveCardInfo(ois, pNum);
			displayBoard();	
			turn++;
			
			sendCard(oos, dealer.hit(deck, false));
			receiveCardInfo(ois, pNum);
			displayBoard();	
			turn++;
		}
	}
	private static void setBoard() {
		for (int i = 0; i < board.length; i++) {
	        for (int j = 0; j < board[i].length; j++) {
	        	if (i == 0 || i == board.length - 1) {
	        		board[i][j] = "-";
	        	} else {
	        		board[i][j] = " ";
	        	}
	        }
	    }
	}
	private static void sendMsg(ObjectOutputStream oos, String msg) {
		for (int i = 0; i < members.size(); ++i) {
			try {
				oos.writeUTF(msg);
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private static void sendCard(ObjectOutputStream oos, Card card) {
		try {
			oos.writeObject(card);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void receiveScore(ObjectInputStream ois, int playerNumber) {
		try {
			int score = ois.readInt();
			scoreList[playerNumber] = score;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void receiveCardInfo(ObjectInputStream ois, int playerNumber) {
		try {
			Object obj = ois.readObject();
			if (obj instanceof String) {
				String str = (String)obj;
				board[12 - turn][playerNumber * 13] = str;
			} 
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void receiveCmd(ObjectInputStream ois, int playerNumber) {
		try {
			Object obj = ois.readObject();
			if(obj instanceof Cmd) {
				Cmd cmd = (Cmd)obj;
				executeCmd(cmd);
				board[14][playerNumber * 13] = cmd.getPlayerNumber() + "player " + cmd.getCmd();
				Integer b = betList[playerNumber];
				board[13][playerNumber * 13] = b + "B";
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private static void executeCmd(Cmd cmd) {
		// 0 hit, 1 stand, 2 bet, 3 split, 4 surrender, 5 double down , 6 bust
		int c = cmd.getCmdNum();
		switch(c) {
		case 0:	
			break;
		case 1:
			onPlayers[cmd.playerNumber] = false;
			break;
		case 2:
			betList[cmd.playerNumber] += cmd.bet;
			break;
		case 3:
			
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			bustPlayers[cmd.playerNumber] = true;
			break;
		}
	}
	private static void displayBoard() {
		for (int i = 0; i < members.size(); ++i) {
			 try {
				 members.get(i).oos.writeObject(board);
				 members.get(i).oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(int i = 0; i < board.length; ++i) {
			for (int j = 0; j < board[i].length; ++j) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}
}
