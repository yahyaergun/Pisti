package com.peakgames.pisti.app;

import java.util.ArrayList;
import java.util.Collections;
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
	private int lastWinnerId; //keeps track of the last pile winner so it can be awarded
	
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
				for(Bot bot : players){
					currentPlayer = bot;
					Card cardToThrow = currentPlayer.throwACard();
					System.out.println("Bot "+ currentPlayer.toString() + " threw " + cardToThrow.toString());
					table.putOnPile(cardToThrow);
				}
			}
		}
		
		
		
		//award last pile winner
		players.get(lastWinnerId).addPoints(table.calculatePoints());
		players.get(lastWinnerId).addWonCards(table.getCardsOnPile().size());
		System.out.println("Last pile goes to ["+players.get(lastWinnerId).toString()+"]");
		table.clearPile();
		
		
		//award most card winner
		Collections.sort(players); // sort players by most cards won, ascending. see compareTo in Bot.java
		players.get(3).addPoints(3); // last one has most cards.
		System.out.println("The bot ["+players.get(3).toString()+"] won the most cards so it gets bonus 3 points!");
				
		System.out.println("SCORES");
		for(Bot bot : players){
			System.out.println(bot.toString()+ " has " + bot.getPoints() + " points.");
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
			System.out.println(currentPlayer.toString() + " has won the pile!");
			lastWinnerId = players.indexOf(currentPlayer);
			currentPlayer.addPoints(((CardsWonEvent) event).getTotalPoints());
			currentPlayer.addWonCards(((CardsWonEvent) event).getCardSize());
		}
	}
	
}
