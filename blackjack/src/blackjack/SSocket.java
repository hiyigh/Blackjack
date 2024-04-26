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
	private static char[][] board = new char[16][60];
	private static Dealer dealer;
	private static int turn;
	//플레이어
	private static ArrayList<PlayerInfo> players = new ArrayList<>();
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
			displayBoard(-1);
			
			
		}
	}
	private static void setBoard() {
		for (int i = 0; i < board.length; i++) {
	        for (int j = 0; j < board[i].length; j++) {
	        	if (i == 0 || i == board.length - 1) {
	        		board[i][j] = '-';
	        	} else {
	        		board[i][j] = ' ';
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
	private static void receiveCmd(ObjectInputStream ois, int playerNumber) {
		try {
			Object obj = ois.readObject();
			if(obj instanceof Cmd) {
				Cmd cmd = (Cmd)obj;
				parseCmd(cmd);
				displayBoard(playerNumber);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private static void parseCmd(Cmd cmd) {
		// 문자 , 번호, 배팅액, 서랜, 버스트, 스코어 ,
		PlayerInfo pi = players.get(cmd.playerNumber);
		pi.num = cmd.playerNumber;
		switch(cmd.cmdNum) {
		case 0:
			pi.msg = "hit";
			Card card = dealer.hit(deck, true);
			card.setPlayerNum(cmd.playerNumber);
			pi.cards.add(card);
			break;
		case 1:
			pi.msg = "stand";
			break;
		case 2:
			pi.msg = "bet";
			pi.bet += cmd.bet;
			break;
		case 3:
			pi.msg = "split";
			pi.splitCnt = cmd.splitCnt;
			break;
		case 4:
			pi.msg = "surrender";
			pi.surrender = true;
			break;
		case 5:
			pi.msg = "double down";
			break;
		}
	}
	private static void displayBoard(int playerNumber) {
		//update board
		if(playerNumber != -1) {		
			PlayerInfo pi = players.get(playerNumber);
			int c_len = pi.cards.size();
			char shape;
			int num;
			String msg = pi.msg;
			Card card = null;
			for (int i = 0; i < c_len; ++i) {
				card = pi.cards.get(i);
				shape = setCardShape(pi.cards.get(i).getShape());
				num = card.getNum();
				// 
				board[12-i][playerNumber * 20] = shape;
				board[12-i][playerNumber * 20 + 1] = (char)num;
			}
			board[13][playerNumber * 20] = (char)playerNumber;
			for (int i = 0; i < msg.length(); ++i) {
				board[13][playerNumber * 20 + (i+1)] = msg.charAt(i);
			}
			board[14][playerNumber * 20] = (char)pi.bet;
			String bet = "bet";
			for (int i = 0; i < 3; ++i) {			
				board[14][playerNumber * 20 + (i + 1)] = bet.charAt(i);
			}
			//
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
		} else {
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
	private static char setCardShape(int shape) {
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
