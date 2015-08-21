package com.peakgames.pisti.model;

import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

public class Deck {

	private Stack<Card> cards;

	public Deck() {
		cards = new Stack<>();

		// populate the deck with cards and shuffle
		for (CardType type : CardType.values()) {
			for (int i = 1; i < 14; i++) {
				cards.push(new Card(type, i));
			}
		}
		
		shuffleDeck(this);
		
	}

	public static Deck shuffleDeck(Deck deck) {
		Collections.shuffle(deck.getCards(), new Random(System.nanoTime()));
		return deck;
	}
	
	public Vector<Card> getFourCards(){
		
		Vector<Card> v = new Vector<>(4);
		
		for (int i = 0 ; i < 4 ; i++){
			v.add(cards.pop());
		}
		
		return v;
	}
	
	public boolean isEmpty(){
		return cards.isEmpty();
	}
	

	public Stack<Card> getCards() {
		return cards;
	}

	@Override
	public String toString() {
		return cards.toString();
	}
	
	


}
