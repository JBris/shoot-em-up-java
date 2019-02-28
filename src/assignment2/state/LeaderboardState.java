package src.assignment2.state;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class LeaderboardState extends AbstractState {
	
	protected int menuChoice;
	protected boolean viewingPlayer;
	
	InterfaceLeaderboard leaderboard;

	/***Constructor***/

	public LeaderboardState(InterfaceGame game, InterfaceStateManager stateManager, 
		InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, InterfaceLeaderboard leaderboard) {
		super(game, stateManager, graphicsManager, audioManager);
		this.leaderboard = leaderboard;
		reset();
	}
	
	/***Methods***/
	
	public void paintComponent() {	
		int fontSize = (int) Math.round(height() * 0.03);		
		double leftAlign = width() * 0.1;
		double rightAlign = width() * 0.8;
		double height = height();
		String font = "Arial";
		graphicsManager.renderScrollingBackground("Menu");
		LeaderboardScore[] leaderboardList = leaderboard.getLeaderboard();	

		if (viewingPlayer == false) {
			game.changeColor(Color.ORANGE);
			drawText(leftAlign, height * 0.05 , "Player", font, fontSize);
			drawText(rightAlign, height * 0.05 , "Score", font, fontSize);

			for (int i = 0; i < leaderboardList.length; i++) {
				if (leaderboardList[i] == null) { break; }
				drawMenuText(menuChoice, i, leftAlign, height * (0.15 + i * 0.075) , leaderboardList[i].name, fontSize);
				drawMenuText(menuChoice, i, rightAlign, height * (0.15 + i * 0.075) , Integer.toString(leaderboardList[i].score), fontSize);
			}
			
			drawMenuText(menuChoice, leaderboardList.length, leftAlign, height * (0.15 + (leaderboardList.length * 0.075)) , "Back to Menu", fontSize);
			drawMenuText(menuChoice, leaderboardList.length + 1, leftAlign, height * (0.25 + (leaderboardList.length * 0.075)) , "Reset Leaderboard", fontSize);

		} else {
			game.changeColor(Color.ORANGE);
			drawText(leftAlign, height * 0.07 * 1 , "Player", font, fontSize);
			game.changeColor(Color.WHITE);
			LeaderboardScore score = 	leaderboardList[menuChoice];
			drawText(leftAlign, height * 0.07 * 2 , "Name: " + score.name, font, fontSize);
			drawText(leftAlign, height * 0.07 * 3 , "Score: " + Integer.toString(score.score), font, fontSize);
			drawText(leftAlign, height * 0.07 * 4 , "Stage: " + Integer.toString(score.stage), font, fontSize);
			drawText(leftAlign, height * 0.07 * 5 , "Loop: " + Integer.toString(score.loop), font, fontSize);
			drawText(leftAlign, height * 0.07 * 6, "Shield Amount: " + Integer.toString(score.shieldAmount), font, fontSize);
			drawText(leftAlign, height * 0.07 * 7, "Shield Recharge: " + Integer.toString(score.shieldRecharge), font, fontSize);
			drawText(leftAlign, height * 0.07 * 8 , "Boost Amount: " + Integer.toString(score.boostAmount), font, fontSize);
			drawText(leftAlign, height * 0.07 * 9 , "Boost Recharge: " + Integer.toString(score.boostRecharge), font, fontSize);
			drawText(leftAlign, height * 0.07 * 10 , "Lives: " + Integer.toString(score.lives), font, fontSize);
			drawText(leftAlign, height * 0.07 * 11, "Projectile Speed: " + score.projectileSpeed.name(), font, fontSize);
			drawText(leftAlign, height * 0.07 * 12, "Enemy Attack Rate: " + score.enemyAttackRate.name(), font, fontSize);
			drawText(leftAlign, height * 0.07 * 13, "Date: " + score.date, font, fontSize);
		}

	}
	
	public void reset() {
		viewingPlayer = false;
		menuChoice = 0;
		leaderboard.sort();
	}
	
	public void update(double dt) {
		graphicsManager.incrementScrollingBackgroundYOffset();		
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		LeaderboardScore[] leaderboardList  = leaderboard.getLeaderboard();	

		if(keyCode == e.VK_ESCAPE) {
			playAudio("back");
			menuChoice = 0;
			switchState(GameState.Menu.name()); 
		}
		
		if (keyCode == e.VK_ENTER && menuChoice < leaderboardList.length && menuChoice >= 0) {
			playAudio("beep");
			if (viewingPlayer == false) {
				viewingPlayer = true;
			} else {
				viewingPlayer = false;	
			}
		}

		if (viewingPlayer == true) { return; }
		
		if (keyCode == e.VK_UP) {
			playAudio("beep");
			if (menuChoice <= 0) { 
				menuChoice = leaderboardList.length + 1; 
			} else {
				menuChoice -- ;
			}
		}
		
		if (keyCode == e.VK_DOWN) {
			playAudio("beep");
			if (menuChoice >= leaderboardList.length + 1) { 
				menuChoice = 0; 
			} else {
				menuChoice ++;
			}
		}
		
		if (keyCode == e.VK_ENTER) {
			if (menuChoice == leaderboardList.length) {
				playAudio("back");
				menuChoice = 0;
				switchState(GameState.Menu.name()); 	
			} else if (menuChoice == leaderboardList.length + 1) {
				leaderboard.reset();
			}
		}
		
	}
	
	public String getStateName() { return GameState.Leaderboard.name(); }
}