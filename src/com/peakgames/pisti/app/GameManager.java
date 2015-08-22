package com.peakgames.pisti.app;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Callable;

import com.peakgames.pisti.comparator.CompareByPoints;
import com.peakgames.pisti.comparator.CompareByWonCardSize;
import com.peakgames.pisti.event.CardsWonEvent;
import com.peakgames.pisti.model.Card;
import com.peakgames.pisti.model.Deck;
import com.peakgames.pisti.model.Table;
import com.peakgames.pisti.player.Bot;

/**
 * Manages a pisti game. Implements Callable interface for concurrency.
 * @author Yahya
 *
 */
public class GameManager implements Observer, Callable<Bot>{
	
	private Table table;
	private Deck deck;
	private List<Bot> players;
	private Bot currentPlayer;
	private int lastWinnerId; //keeps track of the last pile winner so it can be awarded
	
	public GameManager(List<String> botNames){
		deck = new Deck();
		table = new Table();
		players = new ArrayList<Bot>();
		
		//create player objects via reflection
		for (String botName : botNames){
			try {
				players.add(newInstance(botName));
			} catch (ClassNotFoundException | NoSuchMethodException	| InstantiationException | 
					IllegalAccessException| IllegalArgumentException | InvocationTargetException e) {
						System.err.println("No such bot class found: " + botName);
						e.printStackTrace();
						System.exit(1);
			}
		}
	}
	
	/**
	 * Starts a Pisti game.
	 * @returns winner bot.
	 */
	public Bot startGame(){
		
		table.addObserver(this); //to keep scores.
		table.addObserver(players.get(0)); //observer pattern to notify players via events.
		table.addObserver(players.get(1));
		table.addObserver(players.get(2));
		table.addObserver(players.get(3));
		
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
//					System.out.println("Bot "+ currentPlayer.toString() + " threw " + cardToThrow.toString());
					table.putOnPile(cardToThrow);
				}
			}
		}
		
		//award last pile winner
		players.get(lastWinnerId).addPoints(Table.calculatePoints(table.getCardsOnPile()));
		players.get(lastWinnerId).addWonCards(table.getCardsOnPile().size());
//		System.out.println("Last pile goes to ["+players.get(lastWinnerId).toString()+"]");
		table.clearPile();
		
		
		//award most card winner
		Collections.sort(players, new CompareByWonCardSize()); // sort players by most cards won.
		players.get(3).addPoints(3); // last one has most cards.
//		System.out.println("The bot ["+players.get(3).toString()+"] won the most cards so it gets bonus 3 points!");
				
//		System.out.println("###   SCORES   ###");
		Collections.sort(players, new CompareByPoints()); // sort by points
		
		Bot winner = players.get(3);
//		System.out.println("WINNER IS: "+ winner);
		
		return winner;
	}
	
	@Override
	public void update(Observable paramObservable, Object event) {
		if (event instanceof CardsWonEvent){
//			System.out.println(currentPlayer.toString() + " has won the pile!");
			lastWinnerId = players.indexOf(currentPlayer);
			currentPlayer.addPoints(((CardsWonEvent) event).getTotalPoints());
			currentPlayer.addWonCards(((CardsWonEvent) event).getCardSize());
		}
	}

	@Override
	public Bot call() throws Exception {
		return startGame();
	}
	
	/**
	 * Create bot instances from classnames.
	 * 
	 * @param className
	 * @return Bot instances.
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Bot newInstance(final String className) throws ClassNotFoundException, 
															        NoSuchMethodException, 
															        InstantiationException, 
															        IllegalAccessException, 
															        IllegalArgumentException, 
															        InvocationTargetException{
	  return (Bot) Class.forName(className).getConstructor().newInstance();
	}

}
