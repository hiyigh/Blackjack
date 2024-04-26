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
	private Cmd cmd;
	private int totalBet = 0;
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
			cmd = new Cmd();
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
					while(true) {
						receiveMsg();
						
						displayBoard();
						
						
					}
				}
			});
			th.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
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
	
	private void displayBoard() {
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
