package src.assignment2.system;

import java.util.*;
import src.assignment2.entity.player.*;

public interface InterfaceLeaderboard {
	public LeaderboardScore[] getLeaderboard();	
	public int getLeaderboardSize();
	public void setLeaderboardSize(int leaderboardSize);

	public void savePlayerScore(InterfacePlayerShip player, int stage, int loop, InterfaceGameOptions gameOptions);
	public void savePlayerScore(InterfacePlayerShip player, int stage, int loop);
	public void sort();
	public void add(InterfacePlayerShip player,  int stage, int loop, InterfaceGameOptions gameOptions);
	public void add(InterfacePlayerShip player,  int stage, int loop);
	public void reset();
	public void save();
}