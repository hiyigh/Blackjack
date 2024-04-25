package blackjack;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Cmd implements Serializable{
	public String cmd = null;
	public int cmdNum = -1;
	public int bet = 0;
	public int playerNumber;
	public int splitCnt = 0;
	public void setCmd(int n, int playerNum) {
		this.cmdNum = n;
		switch(n) {
		case 0:
			cmd = "hit";
			break;
		case 1:
			cmd = "stand";
			break;
		case 2:
			cmd = "bet";
			break;
		case 3:
			cmd = "split";
			break;
		case 4:
			cmd = "surrender";
			break;
		case 5:
			cmd = "surrender";
			break;
		case 6:
			cmd = "bust";
			break;
		}
		this.playerNumber = playerNum;
	}
	public void setBet(int b) {
		this.bet = b;
	}
	public int getPlayerNumber() {
		return this.playerNumber;
	}
	public int getCmdNum() {
		return this.cmdNum;
	}
	public String getCmd() {
		return cmd;
	}
}
