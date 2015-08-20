package com.peakgames.pisti.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.peakgames.pisti.event.CardsWonEvent;
import com.peakgames.pisti.model.Card;
import com.peakgames.pisti.model.Deck;
import com.peakgames.pisti.model.Table;
import com.peakgames.pisti.player.Bot;
import com.peakgames.pisti.player.DummyBot;

public class GameManager implements Observer{
	
	private Table table;
	private Deck deck;
	private List<Bot> players;
	private Bot currentPlayer;
	
	public GameManager(){
		deck = new Deck();
		table = new Table();
	}
	
	public void startGame(){
		players = new ArrayList<>();
		
		DummyBot dummy1 = new DummyBot();
		DummyBot dummy2 = new DummyBot();
		DummyBot dummy3 = new DummyBot();
		DummyBot dummy4 = new DummyBot();
		
		players.add(dummy1);
		players.add(dummy2);
		players.add(dummy3);
		players.add(dummy4);
		
		table.addObserver(this); //to keep scores.
		table.addObserver(dummy1);
		table.addObserver(dummy2);
		table.addObserver(dummy3);
		table.addObserver(dummy4);
		
		table.putInitialCards(deck.getFourCards());
		
		while(!deck.isEmpty()){
			//deal cards
			for(Bot bot : players){
				bot.setHand(deck.getFourCards());
			}
			
			//start playing
			for(int i = 0 ; i < 4 ; i++){
				System.out.println("Round-"+i);
				for(Bot bot : players){
					currentPlayer = bot;
					Card cardToThrow = currentPlayer.throwACard();
					System.out.println("Bot "+ currentPlayer.toString() + "threw " + cardToThrow.toString());
					table.putOnPile(cardToThrow);
				}
			}
			
		}
	}
	
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	public Deck getDeck() {
		return deck;
	}
	public void setDeck(Deck deck) {
		this.deck = deck;
	}
	public List<Bot> getPlayers() {
		return players;
	}
	public void setPlayers(List<Bot> players) {
		this.players = players;
	}
	public Bot getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(Bot currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	@Override
	public void update(Observable paramObservable, Object event) {
		if (event instanceof CardsWonEvent){
			currentPlayer.addPoints(((CardsWonEvent) event).getTotalPoints());
			currentPlayer.addWonCards(((CardsWonEvent) event).getCardSize());
		}
	}
	
}
