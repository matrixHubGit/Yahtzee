/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		score = new int [TOTAL+1] [MAX_PLAYERS+1];
		scoreBool= new boolean [TOTAL+1] [MAX_PLAYERS+1];
		playGame();
	}

	private void playGame() {
		for(int j =0;j<13;j++) {
		     for(int i =1; i<=nPlayers;i++) {
		    display.printMessage(playerNames[i - 1]+" turn! Click \"Roll Dice\" button to roll the dice" );
		    display.waitForPlayerToClickRoll(i);
			roleDices();
			display.displayDice(dice);
			rollAgain(); //metoda koja ponavalja bacanje dva puta
			
			display.printMessage("Select a category for this roll");
			category = display.waitForPlayerToSelectCategory();
			checkForSecetingCategory(i);
			checkingForCategory(category);
			
			//ispisi na ikanu i unosenje u pomocni niz
			display.updateScorecard(category, i,checkingForCategory(category));
			upDateScoreTotalBonuse(category, i,checkingForCategory(category));
			display.updateScorecard(TOTAL, i,score[TOTAL][i]);
			
		     	}
		     
		   }
		int c=0;
		for(int i =1; i<=nPlayers;i++) {
			display.updateScorecard(UPPER_SCORE, i,score[UPPER_SCORE][i]);
			display.updateScorecard(LOWER_SCORE, i,score[LOWER_SCORE][i]);
			if(score[TOTAL][i]>c)
				c=i;
		}
		
		display.printMessage("Congratulations, "+playerNames[c-1]+",you're the winer with total"
				+ "scorof "+score[TOTAL][c-1]);
		
		
	}
	
/**
 * provjerava da li je kategorija zauzeta i ako jesete trazi ponovni izbor kategorije
 * @param i
 */
	private void checkForSecetingCategory(int i) {
		while(scoreBool[category][i]) {
			display.printMessage("Select another Category");
			category = display.waitForPlayerToSelectCategory();		
		}
		storToBoolArrey(category, i);
		
		
	}
	
/**
 * speram u "bolean" niz kategorije koje su vec koristene radi lakse kontrole
 * @param category2 kategorija koju treba "zastitii"
 * @param i ime igraca
 */
	private void storToBoolArrey(int category2, int i) {
		scoreBool[category][i]=true;
		
	}

	/**
	 * ponavlja bacanje dva puta 
	 */
private void rollAgain() {
	for(int j =0;j<2;j++) { //ponavalja selektovanje  kocki dva puta
	    display.printMessage("Select the dice you wish to re-roll and click \"Roll Dice\"");
		display.waitForPlayerToSelectDice();
		checkingSecetDices();
		display.displayDice(dice);
			}
		}

	
	/**
	 * provjerava da li se izabrana kategorija nalazi u nizu
	 * @param category
	 * @return
	 */
private int checkingForCategory(int category) {
	
	int sum=0;
	switch(category) { 
	case ONES:
	case TWOS:
	case THREES:
	case FOURS:
	case FIVES:
	case SIXES:
		
		return sumOfDices(category);
	}
	
//kategorija THREE_OF_A_KIND	
	if(category==THREE_OF_A_KIND) { 
		int count=contNumber();
		if(count>=3) 
			return sumOfDices();
		else 
			return 0;	
	}
// kategorija 	FOUR_OF_A_KIND
	if(category==FOUR_OF_A_KIND) {
		int count=contNumber();
		if(count>=4) 
			return sumOfDices();
		else 
			return 0;
	}
	
// kategorija 	YAHTZEE	
	if(category==YAHTZEE) {
		int count=contNumber();
		if(count==5) 
			return 50;
		else 
			return 0;
	}
	
// vraca sumu bilo koje formacije kockica
	if(category==CHANCE) {	
		return sumOfDices();
	}
	
//	kategorija LARGE_STRAIGHT
if(category==LARGE_STRAIGHT) {
	int x=checkForOneOrTwo();
	int count=0;
	for(int i=0; i<N_DICE;i++,x++) {
		  for(int j=0;j<N_DICE;j++) {
			  if(x==dice[j])
				count++;
		  }	
		 }
	 if(count==5) {
		 return 40;
		}
return 0;
		
}
// kategorija SMALL_STRAIGHT
if(category==SMALL_STRAIGHT) {
	sortNumber();
	int count=0;
	
	for(int i=1; i<=6;i++) {
		boolean ima= false;
		for(int j=0;j<5;j++){
			if(i==dice[j]) { 
				ima= true;
				break;}	
				}
	if(ima)count++;	
		else count=0;

	if (count>3) return 30;
	}
	return 0;
}

//  kategorija FULL_HOUSE
if(category==FULL_HOUSE) {
	sortNumber();
	int count =0;
	int count2=0;
	int a=dice[0];
	int b =findSecondNumber();

	for(int i=0;i<N_DICE;i++) {
		if(dice[i]==a)
			count++;
		if(dice[i]==b)
			count2++;
	}
	if(count ==2&&count2==3||count ==3&&count2==2) {
		return 25;
	}else{
		return 0;
	}
	
	
}

	return sum;
	
	}
/**
 * trazi drugi broj radi lakseg pretrazivanja FULL_hous kategorije
 * @return
 */
private int findSecondNumber() {
	for(int i=1; i<N_DICE;i++) {
		if(dice[0]!=dice[i])
			return dice[i];
	}
		return 0;
	}

/**
 * sortiranje brojeva za potrebe   SMALL_STRAIGHT
 * @return
 */
private void sortNumber() {
	int c=0;
	for(int i=0; i<N_DICE;i++) {
		for(int j=0;j<N_DICE;j++){
			if(dice[i]<dice[j]) {
				 c = dice[i];
				dice[i]=dice[j];
				dice[j]=c;}
		}
		}
	
	}

/**metoda koja vraca jedan ili dva
 * koja se odnosi na LARGE_STRAIGHT
 * @return
 */
private int checkForOneOrTwo() {
	int x=0;
	for(int i=0; i<N_DICE;i++) {
			if(ONES==dice[i]) 
			return ONES;
			 if(TWOS==dice[i]) 
			return TWOS;	 
			
		}
	return x;
	}
	
	
 
/**
 * metoda bezparamterara samo za zbrajanje svih brojeva sa kociki
 * @return
 */
private int sumOfDices() {
	int sum=0;
	for(int i =0; i<N_DICE;i++) {
			sum +=dice[i];
	}
	
	return sum;
	
	}

/**
 * provjerava da li ima brojeva koji se ponavljaju i vraca koliko se
 *  puta vraca broj koji se najvise ponavlja
 * @return  
 */
private int contNumber() {
	int count=0;
	int x=0;
	for(int i=0; i<N_DICE;i++) {
		for(int j=0;j<N_DICE;j++){
			if(dice[i]==dice[j]) count++;	
		}
		if(count>x)
			x=count;
		count=0;
	}
	
	return x;
}
/**
 * zbrajanje svih clanova unutar niza dice
 * @param category2
 * @return
 */
private int sumOfDices(int category2) {
	int sum=0;
	for(int i =0; i<N_DICE;i++) {
		if(category==dice[i]) {
			sum +=category;
		}
	}
	return sum;
	}


/**
 * provjerava da li ima oznacenih kocki
 */
	private void checkingSecetDices() {
		for(int i =0; i<N_DICE;i++) {
			if(display.isDieSelected(i)) {
				dice[i]=rgen.nextInt(1, 6);
			}	
		}
	}

/**
 * metoda predstavlja bacanje pet kocki
 * @return kao rezultat vraca niz od pet brojeva koji predstavljaju
 * brojeve sa kocki
 */
private int [] roleDices() {
	dice= new int [N_DICE];
	for (int i =0; i<N_DICE;i++) {
		dice[i]=rgen.nextInt(1, 6);
	}
	return dice;
}


private void upDateScoreTotalBonuse(int category, int name, int scr) {
	score[category][name]=scr;
	score[UPPER_SCORE][name]=sumColon(name);
	score[LOWER_SCORE][name]=sumColon2(name);
	if(score[UPPER_SCORE][name]>63) {
		score[UPPER_BONUS][name]=35;
	}	
	score[TOTAL][name]=score[UPPER_BONUS][name]+score[LOWER_SCORE][name]+score[UPPER_SCORE][name];
}

/**
 * suma donjeg dijela tabele
 * @param name
 * @return
 */
private int sumColon2(int name) {
	int sum=0;
	for(int i=9;i<LOWER_SCORE;i++) {
		sum+=score[i][name];
	}
	return sum;
}
/**
 * suma gornjeg dijela tabele
 * @param name
 * @return
 */
private int sumColon(int name) {
int sum=0;
	for(int i=1;i<UPPER_SCORE;i++) {
		sum+=score[i][name];
	}
	return sum;
}


/* Private instance variables */
	private int category;
	private int nPlayers;
	private int [] dice;
	private boolean [][] scoreBool;
	private int [][] score;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}
