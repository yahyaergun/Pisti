package com.peakgames.pisti.event;

import com.peakgames.pisti.model.Card;

/**
 * Notifies observers that a new card is put on table.
 * @author Yahya
 *
 */
public class CardThrownEvent extends PistiEvent{
	
	private Card currentCardOnTop;

	public CardThrownEvent(Card currentCardOnTop) {
		this.currentCardOnTop = currentCardOnTop;
	}

	public Card getCurrentCardOnTop() {
		return currentCardOnTop;
	}

	public void setCurrentCardOnTop(Card currentCardOnTop) {
		this.currentCardOnTop = currentCardOnTop;
	}
	
	
}
