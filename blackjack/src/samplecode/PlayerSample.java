import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import blackjack.Card;

public class Player {
	private static final Scanner sc = new Scanner(System.in);
	private static DataOutputStream dos = null;
	private static DataInputStream dis = null;
	private static final int serverPort = 7777;
	
	private int playerNum;
	private ArrayList<Card> cardList;
	public ArrayList<Player> splitList = new ArrayList<>();
	private int myScore;
	public int bet;
	private int chip;
	
	public static void main (String[] args) {
		Player player = new Player();
	}
	public Player() {		
		try {
			Socket ss = new Socket("server ip address",serverPort);
			dis = new DataInputStream(ss.getInputStream());
			dos = new DataOutputStream(ss.getOutputStream());
			myScore = 0;
			
			Thread th = new Thread(new Runnable() {

				@Override
				public void run() {
					printStringLoop();
					
					System.out.println("change chip");
					setChip(dis);
					
				}			
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void printStringLoop() {
	}
	private int inputInt(DataInputStream dis) {
	}
	
	public void setChip(DataInputStream dis) {

	}
	public int getPlayerNum() {

	}
	public int openCard(){

	}
	public void hit(DataInputStream dis, DataOutputStream dos) {

	}
	public void stand(DataOutputStream dos) {

	}
	public void split(DataOutputStream dos) {
		
	}
	private void bet(int bet) {
	}
	private void setCard(Card card) {
	}
	public int doubleDown() {
		return 0;
	}
	public boolean surrender(DataOutputStream dos) {

	}
	public boolean bet(int bet, DataInputStream dis, DataOutputStream dos) {

	}
	private static void sendMsg(DataOutputStream dos, String msg) {

	}
	private static void sendInt(DataOutputStream dos, int i) {
	
	}
}