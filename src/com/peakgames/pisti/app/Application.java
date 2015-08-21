package com.peakgames.pisti.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.peakgames.pisti.player.Bot;
import com.peakgames.pisti.player.DummyBot;
import com.peakgames.pisti.player.SmartBot;



public class Application {
	
	public static void main(String[] args) {
		
		int dumbWins=0, smartWins=0;
		long startTime = System.nanoTime();
		
		final ExecutorService service;
        
        List<Callable<Bot>> tasks = new ArrayList<Callable<Bot>>();
        service = Executors.newFixedThreadPool(10);
        
        for(int i=0; i<10000; i++){
        	tasks.add(new GameManager());
        }
        List<Future<Bot>> results = new ArrayList<Future<Bot>>();
        
        try {
			results = service.invokeAll(tasks);
			
			for(Future<Bot> winner : results){
		    	   Bot bot = winner.get();
		    	   if(bot instanceof DummyBot){
		    		   dumbWins++;
		    	   } else if (bot instanceof SmartBot){
		    		   smartWins++;
		    	   }
		    }
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
       
        System.out.println("DUMB WINS: " + dumbWins);
        System.out.println("SMART WINS: " + smartWins);
        
		long elapsedTime = System.nanoTime()-startTime;
		System.out.println("Runtime :"+elapsedTime/1000000000L+" seconds.");
		
		service.shutdownNow();
	}

}
