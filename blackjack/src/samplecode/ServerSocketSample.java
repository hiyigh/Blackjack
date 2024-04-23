package samplecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import blackjack.Dealer;
import blackjack.SSocket.PlayerThread;

public class SSocket {
	public static final Scanner sc = new Scanner(System.in);
	private static Dealer dealer;
	private static ArrayList<PlayerThread> playerList;
	private static int limitCount = 3;
	
	public static void main(String[] args) {
		ServerSocket ss;
		try {
			ss = new ServerSocket(7777);
			Socket cs = null;
			boolean flag = false;
			while(!flag) {
				cs = ss.accept();
				//참가 인원수 확인
				playerList.add(new PlayerThread(cs));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static class PlayerThread extends Thread {
		public Socket cs;
		public DataInputStream dis = null;
		public DataOutputStream dos = null;
		private boolean onGame = false;
		
		public PlayerThread(Socket cs) {
			this.cs = cs;
			try {
				dis = new DataInputStream(cs.getInputStream());
				dos = new DataOutputStream(cs.getOutputStream());
			} catch (IOException e) {				
				e.printStackTrace();
			}	
		}
		public void close() {
			try {
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
					
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					dos.close();
					dis.close();
					cs.close();
				} catch (IOException e) {
					System.out.println("close exception");
					e.printStackTrace();
				}
			}
		}
		private void sendMsg(String msg) {

		}
		private void sendInt(int i) {

		}
		private void spreadMsg(ArrayList<PlayerThread> playerList, String msg) {

		}
	}
}