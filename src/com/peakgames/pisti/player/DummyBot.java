package com.peakgames.pisti.player;

import java.util.Observable;

import com.peakgames.pisti.event.CardThrownEvent;
import com.peakgames.pisti.event.CardsWonEvent;
import com.peakgames.pisti.model.Card;

public class DummyBot extends Bot {
	
	private Card topCardOnPile; // only knows the card on top.

	
	public DummyBot(){
		super();
	}

	@Override
	protected Card whichCardToThrow() {
		Card jack = null;
		
		if(topCardOnPile == null){
			return hand.get(0); //throw smallest card if the pile is empty.
		}
		
		for(Card c : hand){
			if(c.getValue()==topCardOnPile.getValue()){
				return c;
			} else if(c.getValue() == 11){
				jack = c;
			}
		}
		
		if (jack!=null){
			return jack; //has a jack, throws a jack.
		}
		
		return hand.get(0); //hand is always sorted, so first card is always smallest by value.
		
	}
	
	@Override
	public void update(Observable observable, Object event) {
		if(event instanceof CardThrownEvent){
			CardThrownEvent cardThrown = (CardThrownEvent) event;
			topCardOnPile = cardThrown.getCurrentCardOnTop();
		} else if (event instanceof CardsWonEvent){
			topCardOnPile = null;
		}
		
	}


}
