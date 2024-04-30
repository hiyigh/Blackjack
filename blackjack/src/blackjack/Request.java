package blackjack;

import java.io.Serializable;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Request implements Serializable{
	public String msg = null;
	public String card = null;
	public int bet;
	public int cNum;
	public Request(int cnum) {
		this.cNum = cnum;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getBet() {
		return bet;
	}
	public void setBet(int bet) {
		this.bet = bet;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getCard() {
		return this.card;
	}
	public int getcNum() {
		return cNum;
	}
	public void setcNum(int cNum) {
		this.cNum = cNum;
	}
	
	
}
