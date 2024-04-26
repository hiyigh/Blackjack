package blackjack;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cmd implements Serializable{
	public int cmdNum = -1;
	public int bet = 0;
	public int playerNumber;
	public int splitCnt = 0;
	
	public void setCmd(int n, int playerNum) {
		this.cmdNum = n;
		this.playerNumber = playerNum;
	}
}
