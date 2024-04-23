package blackjack;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class SSocket {
	public static final Scanner sc = new Scanner(System.in);
	private static Dealer dealer;
	private static ArrayList<PlayerThread> playerList = new ArrayList<>();
	private static Card[][] board = new Card[26][26];
	private static int limitCount = 3;
	private static int playernum = 0;
	private static int turn = 0;
	public static void main(String[] args) {
		ServerSocket ss;
		try {
			ss = new ServerSocket(7777);
			Socket cs = null;
			boolean flag = false;
			while(!flag) {
				cs = ss.accept();
				//참가 인원수 확인
				PlayerThread pt = new PlayerThread(cs, playernum++);
				if (playerList.size() <= limitCount) {
					playerList.add(pt);
					pt.start();
				} else {
					pt.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static class PlayerThread extends Thread {
		public Socket cs;
		public DataInputStream dis = null;
		public DataOutputStream dos = null;
		public ObjectOutputStream oos = null;
		private boolean onGame = false;
		private int playerNum;
		
		public PlayerThread(Socket cs, int playernum) {
			this.cs = cs;
			try {
				dis = new DataInputStream(cs.getInputStream());
				dos = new DataOutputStream(cs.getOutputStream());
				oos = new ObjectOutputStream(dos);
				this.playerNum = playernum;
				dos.writeInt(playernum);
			} catch (IOException e) {				
				e.printStackTrace();
			}	
		}
		public void close() {
			try {
				oos.close();
				dos.close();
				dis.close();
				cs.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			try {				
				while(!onGame) {
					dealer = Dealer.getDealer();
					boolean nextflag = false;
					//wait			
					while (playerList.size() < 1) {
						sendMsg("wait other people");
						Thread.sleep(5000);	
						nextflag = true;
					}
					if (nextflag) {
						sendMsg("next");						
					}
					// close server socket
					limitCount = playerList.size();
					
					sendMsg("welcome");
					sendMsg("next");
					
					sendMsg("play game?");
					sendMsg("next");
					
					int yn = inputCh();
					while( yn == -1) {
						yn = inputCh();
					};
					
					if (yn == 0) {
						break;
					}
				
					sendMsg("Start blackjack");
					System.out.println("Start blackjack");
					sendMsg("set chip");
					System.out.println("set chip");
					sendMsg("next");
					//send chip
					sendInt(1000);
					
					//set table
					Card openCard = dealer.deal(1, 0, true);
					openCard.setPlayerNum(playerNum);
					spreadBoard(openCard, turn++);
					
					Card closeCard = dealer.deal(2, 0, true);
					closeCard.setPlayerNum(playerNum);
					spreadBoard(closeCard, turn++);

					//set cmd num == 0
					cmd(inputInt());

					//set card
					//receive cmd
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					oos.close();
					dos.close();
					dis.close();
					cs.close();
				} catch (IOException e) {
					System.out.println("close exception");
					e.printStackTrace();
				}
			}
		}
		private void cmd(int cmd) {
			switch(cmd) {
			case 0:
				hit();
				break;
			case 1:
				break;
			case 2:
				split();
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			}
		}
		private void hit() {
			Card closeCard = dealer.deal(7, 0, false);
			closeCard.setPlayerNum(playerNum);
			spreadBoard(closeCard, turn++);
		}
		
		/* input method */
		private int inputCh() {
			char yn;
			try {
				yn = dis.readChar();
				if (yn == 'y' || yn == 'Y') {
					return 1;
				} else if (yn == 'n' || yn == 'N') {
					return 0;
				}
			} catch (IOException e) {				
				e.printStackTrace();
			}
			return -1;
		}
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
		
		/* send method */
		private void sendMsg(String msg) {
			try {
		        dos.writeUTF(msg);
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
		
		/* spread method */
		private void spreadMsg(ArrayList<PlayerThread> playerList, String msg) {
			if (playerList == null || msg == null) { 
				System.out.println("playerList is null");
				return;
			}
			for (int i = 0; i < playerList.size(); ++i) {
				try {
					dos = new DataOutputStream(playerList.get(i).cs.getOutputStream());
					dos.writeUTF(msg);
					dos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println(msg);
		}
		private void spreadBoard(Card card, int turn) {
			for (int i = 0; i < playerList.size(); ++i) {
		        board[turn][i * 13] = card;
		    }
			try {
			    for (int i = 0; i < playerList.size(); ++i) {
			        oos = new ObjectOutputStream(playerList.get(i).cs.getOutputStream());
		            oos.writeObject(board);
		            oos.flush();
			    }
			} catch (IOException e) {
			    e.printStackTrace();
			}
			
			for (int i = 0; i < board.length; i++) {
			    for (int j = 0; j < board[i].length; j++) {
			        System.out.print(board[i][j] + " ");
			    }
			    System.out.println();
			}
		}
		
		/* set method */
		
		
	}
}
