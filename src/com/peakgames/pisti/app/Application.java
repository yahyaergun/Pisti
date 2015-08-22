package com.peakgames.pisti.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.peakgames.pisti.player.Bot;


public class Application {
	
	public static void main(String[] args) {
		
		if(args.length != 6){
			throw new ArrayIndexOutOfBoundsException("Arguments missing. Example usage: java -cp pisti.jar com.peakgames.pisti.app.Application <concurrentGameCount> <totalGameCount> <Bot1 classname> <Bot2 classname> <Bot3 classname> <Bot4 classname>");
		}
		
		long startTime = System.nanoTime();
		int concurrentGames = 0, totalGameCount = 0;
		
		List<String> params = Arrays.asList(args); //convert to list so we can get rid of first 2 params later.
		
		try {
			concurrentGames = Integer.valueOf(params.get(0));
			totalGameCount = Integer.valueOf(params.get(1));
		} catch (NumberFormatException e) {
	        System.err.println("Argument " + args[0] + " OR "+ args[1] + " is not an integer.");
	        e.printStackTrace();
	        System.exit(1);
	    }
		
		params = params.subList(2, 6); //only bot names left on params
		Set<String> uniqueBots = new HashSet<>(params); //get unique bot names for scoreboard
		Map<String, Integer> scores = new HashMap<>();
		for(String s : uniqueBots){	//init scoreboard for every unique botname.
			scores.put(s, 0);
		}
		
		final ExecutorService service = Executors.newFixedThreadPool(concurrentGames); //create fixed thread pool with desired concurrent game count.
        
        List<Callable<Bot>> tasks = new ArrayList<Callable<Bot>>(); //create callable list of tasks to start games simultaneously on the mark via invokeAll
        for(int i=0; i<totalGameCount; i++){
        	tasks.add(new GameManager(params));
        }
        List<Future<Bot>> results = new ArrayList<Future<Bot>>();
        
        try {
			results = service.invokeAll(tasks); //start running games.
			
			for(Future<Bot> winner : results){
		    	   Bot bot = winner.get();
		    	   scores.put(bot.toString(), scores.get(bot.toString())+1);		    	   
		    }
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
       
        System.out.println("Total games played : "+totalGameCount);
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() +" : " + entry.getValue() +" wins.");
        }
        
		long elapsedTime = System.nanoTime()-startTime;
		System.out.println("Simulation duration : "+elapsedTime/1000000L+" ms.");
		
		service.shutdownNow();
	}
	


}
