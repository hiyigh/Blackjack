package blackjack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Player implements Serializable {
	private static final Scanner sc = new Scanner(System.in);
	private static DataOutputStream dos = null;
	private static DataInputStream dis = null;
	private static ObjectInputStream ois = null;
	private static ObjectOutputStream oos = null;
	private static final int serverPort = 7777;
	
	private static String[][] board = new String[26][26];
	
	public static void main (String[] args) {
		Player player = new Player();
	}
	public Player() {		
		try {
			Socket ss = new Socket("192.168.20.133",serverPort);
			dis = new DataInputStream(ss.getInputStream());
			dos = new DataOutputStream(ss.getOutputStream());
			ois = new ObjectInputStream(dis);
			
			Thread th = new Thread(new Runnable() {
				@Override
				public void run() {
				}

			});
			th.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
