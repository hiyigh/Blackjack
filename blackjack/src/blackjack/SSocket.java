package blackjack;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	private static Dealer dealer;
	//플레이어
	private static Card[][] players = new Card[3][13];
	private static int playerNum = 0;
	//입출력
	
	public static void main(String[] args) {
		ServerSocket ss;
		try {
			ss = new ServerSocket(7777);
			Socket cs = null;
			boolean flag = false;
			while(!flag) {
				cs = ss.accept();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class PlayerThread extends Thread {
		
		public PlayerThread(Socket cs, int playernum) {
			
		}
		public void close() {
			
		}
		@Override
		public void run() {
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
