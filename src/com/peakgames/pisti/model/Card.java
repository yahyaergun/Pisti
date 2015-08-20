package com.peakgames.pisti.model;

/**
 * Immutable Card class.
 * @author Yahya
 *
 */
public class Card implements Comparable<Card>{
	
	private final CardType type;
	private final int value;
	
	public Card(CardType type, int value){
		this.type = type;
		this.value = value;
	}

	public CardType getType() {
		return type;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		
		if(value == 1){
			return "Ace of " + type.toString();
		} else if(value == 11){
			return "Jack of "+type.toString();
		} else if(value == 12){
			return "Queen of "+type.toString();
		} else if(value == 13){
			return "King of "+type.toString();
		} else {
			return  value + " of " + type.toString();
		}
		
	}

	@Override
	public int compareTo(Card paramCard) {
		
		if(value == paramCard.value){
			return 0;
		} else if (value > paramCard.value){
			return 1;
		} else {
			return -1;
		}
	}
	
	

}
