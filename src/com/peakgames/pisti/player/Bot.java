package com.peakgames.pisti.player;

import java.util.Collections;
import java.util.List;
import java.util.Observer;

import com.peakgames.pisti.model.Card;

/**
 * Abstract Bot class to extend for Bot types.
 * @author Yahya
 *
 */
public abstract class Bot implements Observer, Comparable<Bot>{
	
	protected List<Card> hand;
	private int points;
	private int wonCardCount;
	
	public Bot(){
		points = 0;
		wonCardCount = 0;
	}

	public final Card throwACard(){
		Card c = whichCardToThrow();
		hand.remove(c);
		return c;
	}
	
	public final void addPoints(int earned){
		points += earned;
	}
	
	public final void addWonCards(int wonCardCount){
		this.wonCardCount += wonCardCount;
	}

	public int getWonCardCount() {
		return wonCardCount;
	}

	/**
	 * implement card throwing strategy on extended classes for smart or dumb bots.
	 */
	protected abstract Card whichCardToThrow();
	
	public int getPoints() {
		return points;
	}

	public void setHand(List<Card> hand) {
		Collections.sort(hand); //sort by value, since Card overrides compareTo of Comparable with card values.
		this.hand = hand;
	}

	//compare by won cards, so it will be easier to find who won the most cards with Collections.sort
	@Override
	public int compareTo(Bot bot) {
		
		if (wonCardCount == bot.wonCardCount){
			return 0;
		} else if (wonCardCount > bot.wonCardCount){
			return 1;
		} else {
			return -1;
		}
	}
	
	
	

	
}
