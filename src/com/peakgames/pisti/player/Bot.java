package com.peakgames.pisti.player;

import java.util.Collections;
import java.util.List;
import java.util.Observer;

import com.peakgames.pisti.model.Card;

/**
 * Abstract Bot class to extend for Bot types.
 * Every extended class must override whichCardToThrow(), update() and toString().
 * 
 * @author Yahya
 *
 */
public abstract class Bot implements Observer{
	
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
	 * Implement card throwing strategy on extended classes for new bot types.
	 */
	protected abstract Card whichCardToThrow();
	
	/**
	 * Return full classname like "com.foo.bar.AlienBot" on new bot implementations.
	 * Used by scoreboard to list scores of different bot types.
	 */
	public abstract String toString();
	
	public int getPoints() {
		return points;
	}

	public void setHand(List<Card> hand) {
		Collections.sort(hand); //sort by value, since Card overrides compareTo of Comparable with card values.
		this.hand = hand;
	}
	
}
