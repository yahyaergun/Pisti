package com.peakgames.pisti.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Stack;

import com.peakgames.pisti.event.CardThrownEvent;
import com.peakgames.pisti.event.CardsWonEvent;
import com.peakgames.pisti.model.Card;
import com.peakgames.pisti.model.Table;

/**
 * Knows discarded cards, keeps track of the pile.
 * @author Yahya
 *
 */
public class SmartBot extends Bot {
	
	private Map<Integer, Integer> countMap; // card.value <-> how many times it's played. Bot's memory.
	private Stack<Card> currentPile; //current pile of cards on table

	public SmartBot() {
		super();
		countMap = new HashMap<>();
		currentPile = new Stack<>();
	}
	
	
	@Override
	protected Card whichCardToThrow() {
		Card jack = null;
		
		if(!currentPile.isEmpty()){ //cards available on pile
			
			for(Card c : hand){
				if(c.getValue()==currentPile.peek().getValue()){
					return c; //get pile at all costs if available (if pile has no points, still a chance to get 3 points by most cards won.)
				} else if(c.getValue() == 11){
					jack = c; //keep ref of jack at hand
				}
			}
			
			//use jack if it worths 2 points min. (1 by pile, 1 by jack itself)
			if(jack!=null && Table.calculatePoints(currentPile) > 0 ){
				return jack;
			}
		}
		
		// no card on pile OR will not get the pile, so throw the card with minimum chance to be collected by other players.
		return getCardWithMinCollectChance(); 
	}
	
	
	/**
	 * Finds the card at hand which is least likely to be collected. (Maximum of the cards played in the game, by value)
	 * @return Card with lowest chance to be collected
	 */
	private Card getCardWithMinCollectChance(){
		
		Card tempCard = hand.get(0);
		Integer count1, count2;
		for (Card c : hand){
			count1 = countMap.get(c.getValue());
			count2 = countMap.get(tempCard.getValue());
			
			if (count1 == null || count2 == null){
				continue;
			} else if( countMap.get(c.getValue()) > countMap.get(tempCard.getValue())){
				tempCard = c;
			}
		}
		
		return tempCard;
	}
	
	@Override
	public void update(Observable observable, Object event) {
		if(event instanceof CardThrownEvent){
			Card c = ((CardThrownEvent) event).getCurrentCardOnTop();
			currentPile.push(c);  //keep track of the pile
			
			int value = c.getValue();
			Integer occurenceOfCardValue = countMap.get(value); //no primitives to avoid NullPointerException.
			
			//memorize cards by values.
			if (occurenceOfCardValue == null){
				countMap.put(value, 1);
			} else {
				countMap.put(value, ++occurenceOfCardValue);
			}
		} else if(event instanceof CardsWonEvent){
			currentPile.clear();
		}

	}

	@Override
	public String toString() {
		return "com.peakgames.pisti.player.SmartBot";
	}

}
