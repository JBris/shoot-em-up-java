package src.assignment2.system;

import java.lang.*;
import java.util.*;
import src.assignment2.entity.player.*;
import src.assignment.system.*;

public class Leaderboard implements InterfaceLeaderboard {
	
	/***Properties***/
	
	protected int leaderboardSize;
	protected InterfaceGameOptions gameOptions;
	protected InterfaceFile file;
	protected LeaderboardScore[] leaderboardList;
	
	/***Constructor***/
	
	public Leaderboard(InterfaceGameOptions gameOptions, InterfaceFile file) {
		this.gameOptions = gameOptions;
		this.file = file;
		leaderboardSize = 9; 
		leaderboardList  = file.readSerializableArray(LeaderboardScore[].class, "leaderboard", "data");
	}
	
	/***Getters and Setters***/
	public int getLeaderboardSize() { return leaderboardSize; } 
	public void setLeaderboardSize(int leaderboardSize) { this.leaderboardSize = leaderboardSize; }
	
	public LeaderboardScore[] getLeaderboard() { return leaderboardList; } 
	
	/***Methods***/

	public void sort() {
		Arrays.sort(leaderboardList, new Comparator<LeaderboardScore>(){
			public int compare(LeaderboardScore o1, LeaderboardScore o2) {
				if ( o1.score > o2.score ) { return -1; }
				if ( o1.score == o2.score && o1.oDate.getTime() > o2.oDate.getTime() ) { return -1; }
				return 0;
			}
		});
	}
	
	public void savePlayerScore(InterfacePlayerShip player,  int stage, int loop, InterfaceGameOptions gameOptions) {
		add(player, stage, loop, gameOptions);
		sort();
		save();
	}
	
	public void savePlayerScore(InterfacePlayerShip player, int stage, int loop) { 
		savePlayerScore(player, stage, loop, gameOptions); 
	}

	public void add(InterfacePlayerShip player,  int stage, int loop, InterfaceGameOptions gameOptions) {
		int i = leaderboardList.length - 1;
		int score = player.getScore();
		if (leaderboardList[i].score > score) { return; }
		leaderboardList[i] = new LeaderboardScore(player.name() , score, stage, loop, gameOptions);
	}
	
	public void add(InterfacePlayerShip player,  int stage, int loop) {
		add(player, stage, loop, gameOptions); 
	}
	
	public void save() {
		file.writeSerializableArray(leaderboardList, "leaderboard", "data");
	}
	
	public void reset() {
		leaderboardList = new LeaderboardScore[leaderboardSize];
		for (int i = 0; i < leaderboardSize; i++) {
			leaderboardList[i] = new LeaderboardScore("Player " + Integer.toString(i + 1), i * 100, 1, 0, gameOptions);
		}
		sort();
		save();
	}
	
}