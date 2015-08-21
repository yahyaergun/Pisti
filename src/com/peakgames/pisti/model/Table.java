package com.peakgames.pisti.model;

import java.util.Collections;
import java.util.Observable;
import java.util.Stack;
import java.util.Vector;

import com.peakgames.pisti.event.CardThrownEvent;
import com.peakgames.pisti.event.CardsWonEvent;

public class Table extends Observable{

	private Stack<Card> cardsOnPile;

	public Table() {
		super();
		cardsOnPile = new Stack<>();
	}

	public void putInitialCards(Vector<Card> v) {
		clearPile(); // not needed, clear anyway.
		Collections.reverse(v); // need to reverse the vector to get the ordering of cards correct on table
		
		for (Card c : v){ //no .addAll because we want to notify smart bots for each card, addAll does the same anyway.
			cardsOnPile.add(c);
			setChanged();
			notifyObservers(new CardThrownEvent(c));
		}

	}

	public void putOnPile(Card thrown){
		
		if(cardsOnPile.isEmpty()){
			cardsOnPile.push(thrown);
			setChanged();
			notifyObservers(new CardThrownEvent(thrown));
		} else {
			Card cardOnTop = cardsOnPile.peek();
			
			if(cardOnTop.getValue()==thrown.getValue() || thrown.getValue() == 11){
				// throw cardsWonEvent after putting card on stack
				cardsOnPile.push(thrown);
				setChanged();
				notifyObservers(new CardThrownEvent(thrown)); //fire thrown event to notify players before clearing pile.
				setChanged();
				notifyObservers(new CardsWonEvent(calculatePoints(cardsOnPile), cardsOnPile.size()));
				clearPile();
				
			} else {
				cardsOnPile.push(thrown);
				setChanged();
				notifyObservers(new CardThrownEvent(thrown));
			}
		}
		
	}

	public static int calculatePoints(Stack<Card> cardsOnPile) {
		int points = 0;

		if (cardsOnPile.size() == 2 && cardsOnPile.elementAt(0).getValue() == cardsOnPile.elementAt(1).getValue()) {
			if (cardsOnPile.peek().getValue() == 11) {
				points += 20;
//				System.out.println("Double Pisti!!");
			} else {
				points += 10;
//				System.out.println("Pisti!");
			}
		}

		for (Card c : cardsOnPile) {
			if (c.getValue() == 1 || c.getValue() == 11){
				points += 1;
			} else if (c.getType() == CardType.CLUBS && c.getValue() == 2){
				points += 2;
			} else if (c.getType() == CardType.DIAMONDS && c.getValue() == 10){
				points += 3;
			}
				
		}
		
		return points;
	}
	
	public Stack<Card> getCardsOnPile() {
		return cardsOnPile;
	}

	public void clearPile() {
		cardsOnPile.removeAllElements();
	}
	

}
