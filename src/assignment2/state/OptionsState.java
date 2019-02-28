package src.assignment2.state;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class OptionsState extends AbstractState {
	
	/***Properties***/

	protected int menuChoice;
	
	protected InterfaceGameOptions gameOptions;

	/***Constructor***/

	public OptionsState(InterfaceGame game, InterfaceStateManager stateManager, 
		InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, 
		InterfaceGameOptions gameOptions) {
		super(game, stateManager, graphicsManager, audioManager);
		this.gameOptions = gameOptions;
	}
	
	/***Methods***/
	
	public void paintComponent() {
		int fontSize = (int) Math.round(height() * 0.035);		
		double menuWidth = width() * 0.15;
		double imageWidth = width() * 0.07;
		double imageHeight = height() * 0.07;
		double height = height();
		graphicsManager.renderScrollingBackground("Menu");
		
		drawMenuText(menuChoice, 0, menuWidth, height * 0.15, "Shield Levels: " , fontSize);
		renderPlayerImages("Shield", gameOptions.getShieldAmount(), height * 0.15, imageWidth, imageHeight);
		renderOptionsText(gameOptions.getShieldRecharge(), "Shield Recharge", "Never", true, 1, menuWidth, height * 0.25, fontSize);
		
		drawMenuText(menuChoice, 2, menuWidth, height * 0.35, "Boost Amount: ", fontSize);
		renderPlayerImages("Boost", gameOptions.getBoostAmount(), height * 0.35, imageWidth, imageHeight);
		renderOptionsText(gameOptions.getBoostRecharge(), "Boost Recharge", "Never", true, 3, menuWidth, height * 0.45, fontSize);
		
		drawMenuText(menuChoice, 4, menuWidth, height * 0.55, "Lives: ", fontSize);
		renderPlayerImages("PlayerLives", gameOptions.getLives(), height * 0.55, imageWidth, imageHeight);

		drawMenuText(menuChoice, 5, menuWidth, height * 0.65, "Player Controls: " + gameOptions.getPlayerControls().name(), fontSize);
 		drawMenuText(menuChoice, 6, menuWidth, height * 0.75, "Projectile Speed: " + gameOptions.getProjectileSpeed().name(), fontSize);
		drawMenuText(menuChoice, 7, menuWidth, height * 0.85, "Enemy Attack Rate: " + gameOptions.getEnemyAttackRate().name(), fontSize);
		
		drawMenuText(menuChoice, 8, menuWidth, height * 0.95, "Back to Menu", fontSize);	
	}
	
	public void update(double dt) {
		graphicsManager.incrementScrollingBackgroundYOffset();		
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if(keyCode == e.VK_ESCAPE) {
			playAudio("back");
			menuChoice = 0;
			switchState(GameState.Menu.name()); 
		}
		
		if (keyCode == e.VK_UP) {
			playAudio("beep");
			if (menuChoice <= 0) { 
				menuChoice = 8; 
			} else {
				menuChoice -- ;
			}
		}
		
		if (keyCode == e.VK_DOWN) {
			playAudio("beep");
			if (menuChoice >= 8) { 
				menuChoice = 0; 
			} else {
				menuChoice ++;
			}
		}
		
		if (keyCode == e.VK_ENTER && menuChoice == 8) {
			playAudio("back");
			menuChoice = 0;
			switchState(GameState.Menu.name()); 
		}
		
		changeOptions(e);
	}
	
	public void changeOptions(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		if (keyCode == e.VK_LEFT) {
			playAudio("beep");	
			switch(menuChoice) {
								
				case 0:
					gameOptions.decrementShieldAmount();
					if (gameOptions.getShieldAmount() < 0) { gameOptions.setShieldAmount(5); }
					break;
					
				case 1:
					int shieldRecharge = gameOptions.getShieldRecharge();
					shieldRecharge -= 5;
					if (shieldRecharge < 0) { shieldRecharge = 100; } 
					gameOptions.setShieldRecharge(shieldRecharge);
					break;
				
				case 2: 
					gameOptions.decrementBoostAmount();
					if (gameOptions.getBoostAmount() < 0) { gameOptions.setBoostAmount(5); }
					break; 
					
				case 3:
					int boostRecharge = gameOptions.getBoostRecharge();
					boostRecharge -= 2;
					if (boostRecharge < 0) { boostRecharge = 50; } 
					gameOptions.setBoostRecharge(boostRecharge);
					break;
				
				case 4: 
					gameOptions.decrementLives();
					if (gameOptions.getLives() < 0) { gameOptions.setLives(5); }
					break; 
				case 5:
					PlayerControls playerControls = gameOptions.getPlayerControls();
					if (playerControls == PlayerControls.Keyboard) {
						gameOptions.setPlayerControls(PlayerControls.Mouse);
					} else {
						gameOptions.setPlayerControls(PlayerControls.Keyboard);
					}
					break;					
				case 6:
					ProjectileSpeed projectileSpeed = gameOptions.getProjectileSpeed();
					if (projectileSpeed == ProjectileSpeed.Fast) {
						gameOptions.setProjectileSpeed(ProjectileSpeed.Medium);
					} else if (projectileSpeed == ProjectileSpeed.Medium) {
						gameOptions.setProjectileSpeed(ProjectileSpeed.Slow);
					} else {
						gameOptions.setProjectileSpeed(ProjectileSpeed.Fast);
					}
					break;
				case 7:
					EnemyAttackRate enemyAttackRate = gameOptions.getEnemyAttackRate();
					if (enemyAttackRate == EnemyAttackRate.Fast) {
						gameOptions.setEnemyAttackRate(EnemyAttackRate.Medium);
					} else if (enemyAttackRate == EnemyAttackRate.Medium) {
						gameOptions.setEnemyAttackRate(EnemyAttackRate.Slow);
					} else {
						gameOptions.setEnemyAttackRate(EnemyAttackRate.Fast);
					}
					break;
			}
		}
	
		if (keyCode == e.VK_RIGHT) {
			playAudio("beep");	
			switch(menuChoice) {
				
				case 0:
					gameOptions.incrementShieldAmount();
					if (gameOptions.getShieldAmount() > 5) { gameOptions.setShieldAmount(0); }
					break;
					
				case 1:
					int shieldRecharge = gameOptions.getShieldRecharge();
					shieldRecharge += 5;
					if (shieldRecharge > 100) { shieldRecharge = 0; } 
					gameOptions.setShieldRecharge(shieldRecharge);
					break;
				
				case 2: 
					gameOptions.incrementBoostAmount();
					if (gameOptions.getBoostAmount() > 5) { gameOptions.setBoostAmount(0); }
					break; 
					
				case 3:
					int boostRecharge = gameOptions.getBoostRecharge();
					boostRecharge += 2;
					if (boostRecharge > 50) { boostRecharge = 0; } 
					gameOptions.setBoostRecharge(boostRecharge);
					break;
				
				case 4: 
					gameOptions.incrementLives();
					if (gameOptions.getLives() > 5) { gameOptions.setLives(0); }
					break; 
				case 5:
					PlayerControls playerControls = gameOptions.getPlayerControls();
					if (playerControls == PlayerControls.Keyboard) {
						gameOptions.setPlayerControls(PlayerControls.Mouse);
					} else {
						gameOptions.setPlayerControls(PlayerControls.Keyboard);
					}
					break;			
				case 6:
					ProjectileSpeed projectileSpeed = gameOptions.getProjectileSpeed();
					if (projectileSpeed == ProjectileSpeed.Slow) {
						gameOptions.setProjectileSpeed(ProjectileSpeed.Medium);
					} else if (projectileSpeed == ProjectileSpeed.Medium) {
						gameOptions.setProjectileSpeed(ProjectileSpeed.Fast);
					} else {
						gameOptions.setProjectileSpeed(ProjectileSpeed.Slow);
					}
					break;
				case 7:
					EnemyAttackRate enemyAttackRate = gameOptions.getEnemyAttackRate();
					if (enemyAttackRate == EnemyAttackRate.Slow) {
						gameOptions.setEnemyAttackRate(EnemyAttackRate.Medium);
					} else if (enemyAttackRate == EnemyAttackRate.Medium) {
						gameOptions.setEnemyAttackRate(EnemyAttackRate.Fast);
					} else {
						gameOptions.setEnemyAttackRate(EnemyAttackRate.Slow);
					}
					break;
			}
		}	
	}
	
	public String getStateName() { return GameState.Options.name(); }
	
	protected void renderPlayerImages(String image, int iterations, double height, double imageWidth, double imageHeight) {
		double gameWidth = width();
		double gameHeight = height();
		
		for (int i =  0; i < iterations; i++) {
			drawImage(image, gameWidth * 0.4 + gameWidth * 0.075 * i, height - gameHeight * 0.06, imageWidth, imageHeight);
		}
	}
	
	protected void renderOptionsText(int optionValue, String optionLabel, String zeroValueLabel, boolean appendSeconds, 
		int menuSelection, double menuWidth, double height, int fontSize) {
		String optionMessage = optionLabel + ": ";
		
		if (optionValue == 0) { 
			optionMessage += zeroValueLabel; 
		} else {
			optionMessage += Integer.toString(optionValue);
			if (appendSeconds == true) { optionMessage += " seconds"; }
		}
		
		drawMenuText(menuChoice, menuSelection, menuWidth, height, optionMessage, fontSize);
	}
}