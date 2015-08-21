package com.peakgames.pisti.comparator;

import java.util.Comparator;

import com.peakgames.pisti.player.Bot;

public class CompareByWonCardSize implements Comparator<Bot>{

	@Override
	public int compare(Bot o1, Bot o2) {
		return o1.getWonCardCount() - o2.getWonCardCount();
	}
	
}
