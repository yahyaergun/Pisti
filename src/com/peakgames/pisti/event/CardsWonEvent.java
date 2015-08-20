package com.peakgames.pisti.event;

public class CardsWonEvent extends PistiEvent{
	
	private int totalPoints;
	private int cardSize;
	
	
	public CardsWonEvent(int totalPoints, int cardSize) {
		this.totalPoints = totalPoints;
		this.cardSize = cardSize;
	}
	
	
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	public int getCardSize() {
		return cardSize;
	}
	public void setCardSize(int cardSize) {
		this.cardSize = cardSize;
	}
	
	

}
