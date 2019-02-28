package src.assignment2.system;

import java.awt.*;
import java.lang.*;
import java.util.*;
import src.assignment2.entity.enemy.*;
import src.assignment2.entity.item.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class StageManager implements InterfaceStageManager {
	
	/***Properties***/
	
	protected InterfaceGame game;
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	protected InterfacePlayerShip playerShip;
	protected InterfaceEnemyManager enemyManager;
	protected InterfaceItemManager itemManager;
	protected InterfaceExplosionManager explosionManager;
	
	protected AnimatedText introText;
	protected AnimatedText endingText;
	
	protected int stageNumber, gameLoopNumber, numOfRandomStages, numOfTotalStages, introTime, endingTime;
	protected double introTimer, endingTimer, nextStageTimer;
	protected boolean inIntroState, inEndingState, stageCompletionTrigger, isTransitioning, shipArrived, audioClipPlaying;
	
	protected String currentMusic, currentBackground;

	/***Constructor***/
	
	public StageManager(InterfaceGame game, InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager, InterfacePlayerShip playerShip, InterfaceEnemyManager enemyManager, 
		InterfaceItemManager itemManager, InterfaceExplosionManager explosionManager) {
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
			
		this.playerShip = playerShip;
		this.enemyManager = enemyManager;
		this.itemManager = itemManager;
		this.explosionManager = explosionManager;
			
		fullReset(false);
	}
	
	/***Getters and Setters***/
	
	public String getBackgroundName() { return currentBackground; }
	
	public int getStageNumber(){ return stageNumber; }
	public int getGameLoopNumber(){ return gameLoopNumber; }
	public int getNumOfRandomStages() { return numOfRandomStages; }
	public int getNumOfTotalStages() { return numOfTotalStages; }
	
	public boolean getInIntroState() { return inIntroState; }
	public boolean getInEndingState() { return inEndingState; }
	public boolean getStageCompletionTrigger() { return stageCompletionTrigger; }
	public void setStageCompletionTrigger(boolean s) { stageCompletionTrigger = s; }
	
	public boolean getIsTransitioning() { return isTransitioning; }
	public void setIsTransitioning(boolean isTransitioning) { this.isTransitioning = isTransitioning; }
	
	/***Methods***/

	public static int totalNumberOfRandomStages() { return 5; }
	
	public void reset(InterfaceGameOptions gameOptions, boolean bootComplete) {
		fullReset(bootComplete);
		enemyManager.setProjectileSpeed(gameOptions.getProjectileSpeed());
		enemyManager.setAttackRate(gameOptions.getEnemyAttackRate());
		itemManager.setItemSpeed(gameOptions.getProjectileSpeed());
	}
	
	public void nextStage() {
		if (stageNumber > numOfTotalStages) { stageNumber = 0; }
		
		if (stageNumber == numOfTotalStages) {
			stageNumber = 1;
			gameLoopNumber++;
			currentBackground = getRandomBackgroundName();
			currentMusic = getRandomAudio();
			loopModifiers();
		} else if (stageNumber == numOfRandomStages) {
			stageNumber++;
			currentBackground = "Final";
			currentMusic = "Final";
		} else {
			stageNumber++;
			currentBackground = getRandomBackgroundName();
			currentMusic = getRandomAudio();
		}
		
		resetStageStates();
		enemyManager.getNextBoss();
	}
	
	public void nextStage(double dt, double transitionLength) {
		nextStageTimer += dt;
		if (nextStageTimer < transitionLength) { return; }
		nextStage();
	}
	
	public void cleanup() {
		if (audioClipPlaying == true) {
			audioManager.stopAudioLoop(currentMusic);
		}
	}
	
	public void paintComponent() {
		renderCurrentBackground();
		itemManager.paintComponent();

		if (inIntroState == true) {
			changeColor(Color.WHITE); 
			displayAnimatedText(introText, game.width() * 0.45, 0.15, 0.2, "Arial", (int) (game.height() * 0.04));
		} else if (inEndingState == true) {
			changeColor(Color.WHITE); 
			displayAnimatedText(endingText, game.width() * 0.35, 0.15, 0.2, "Arial", (int) (game.height() * 0.04));	
		} else {
			enemyManager.paintComponent();			
		}
	}
	
	public void renderCurrentBackground() {
		graphicsManager.renderScrollingBackground(currentBackground);
	}
	
	public void update(double dt) {
		if (inIntroState == true) {
			updateIntroState(dt);
			return;
		}
		
		graphicsManager.incrementScrollingBackgroundYOffset();	
		
		enemyManager.update(dt);
		itemManager.update(dt);
		explosionManager.update(dt);
		
		if (inEndingState == true) {
			updateEndingState(dt);
			return;
		}

		if (enemyManager.getVictoryState() == true) {
			beginEndingState();	
		}
	}
	
	protected void updateIntroState(double dt) {
		playerShip.updateAnimationFrame();
		if (shipArrived == false) {
			double yTarget =  height() * 0.6;
			playerShip.moveToCoordinates(playerShip.getXCoordinate(), yTarget);
			playerShip.move(dt);
			
			if (playerShip.getYCoordinate() <= yTarget + playerShip.getHeight() / 2) { 
				playerShip.setYVelocity(0);
				playerShip.setYCoordinate(yTarget + playerShip.getHeight() / 2);
				shipArrived = true;
			}
			return;
		}
	
		if (introText.isCharTransferComplete()  == false) {
			introText.update(dt);
			if (introText.isDisplayTimeReached(true) == false) { return; }
			introText.transferTextChars();
			return;
		} 
			
		introTimer += dt;
		if(introTimer > introTime) { 
			audioManager.startAudioLoop(currentMusic);
			playerShip.setAllowInput(true);
			playerShip.setInvulnerable(false);
			inIntroState = false; 
			shipArrived = false;
			audioClipPlaying = true;
		}
	}
	
	protected void beginEndingState() {
		audioManager.stopAudioLoop(currentMusic);
		audioClipPlaying = false;
		explosionManager.reset();
		
		playAudio("drop");
		if (stageNumber == numOfTotalStages) {
			playerShip.incrementScoreByValue(1000 + 100 * gameLoopNumber);
		} else {
			playerShip.incrementScoreByValue(100 * (gameLoopNumber + 1));
		}
		
		playAudio("victory");
		playerShip.setAllowInput(false);
		playerShip.disableBoost();
		playerShip.disableAttack();
		playerShip.setInvulnerable(true);
		playerShip.resetCurrentShieldAmount();
		playerShip.resetCurrentBoostAmount();
		playerShip.clearSpecialAttack();
		
		inEndingState = true;
	}
	
	protected void updateEndingState(double dt) {
		if (shipArrived == false) {
			double width = width();
			double height = height();
			double yTarget = -playerShip.getHeight();
			playerShip.moveToCoordinates(width / 2, yTarget);
			playerShip.move(dt);
			
			if (playerShip.getYCoordinate() <= yTarget + playerShip.getHeight() / 2) { 
				playerShip.setYCoordinate(yTarget);
				playerShip.setYVelocity(0);
				shipArrived = true;
			}
		}
	
		if (endingText.isCharTransferComplete()  == false) {
			endingText.update(dt);
			if (endingText.isDisplayTimeReached(true) == false) { return; }
			endingText.transferTextChars();
			return;
		} 
			
		endingTimer += dt;
		if(endingTimer > endingTime) { 
			stageCompletionTrigger = true;
		}
	}
	
	protected void fullReset(boolean bootComplete) {
		stageNumber = 1;
		gameLoopNumber = 0;	
		numOfRandomStages = getTotalNumberOfRandomStages();
		numOfTotalStages = numOfRandomStages + 1;
		resetStageStates();
		resetStageText();
		
		enemyManager.fullReset();
		enemyManager.rebuildBosses();
		enemyManager.getNextBoss();

		itemManager.fullReset();
		if (bootComplete == true) {
			currentBackground = getRandomBackgroundName();
			currentMusic = getRandomAudio();
		}
	}
	
	protected void loopModifiers() {
		if (gameLoopNumber % 2 == 0) {
			int playerTotalShieldAmount = playerShip.getTotalShieldAmount();
			int playerTotalBoostAmount = playerShip.getTotalBoostAmount();
			
			if (playerTotalShieldAmount > 0) { playerShip.setTotalShieldAmount(playerTotalShieldAmount - 1); }
			if (playerTotalBoostAmount > 0) { playerShip.setTotalBoostAmount(playerTotalBoostAmount- 1); }
		}

		playerShip.setShieldRecharge(playerShip.getShieldRecharge() + 5);
		playerShip.setBoostRecharge(playerShip.getBoostRecharge() + 2); 
		
		enemyManager.setGameLoop(gameLoopNumber);
		
		itemManager.incrementScoreMultiplier();	
		itemManager.increaseBaseRandomModifierByValue(0.05);
	}
	
	protected String getRandomBackgroundName() {
		return graphicsManager.getRandomScrollingBackgroundName();
	}
	
	protected String getRandomAudio() {
		return audioManager.getRandomFromPlayList();
	}

	protected void resetStageStates() {
		inIntroState = true;
		
		introTime = 2;
		introTimer =  0;
		
		inEndingState = false;
		stageCompletionTrigger = false;
		endingTimer = 0;
		
		if (stageNumber == numOfRandomStages) {
			endingTime = 4;
		} else {
			endingTime = 2;
		}
		
		isTransitioning = false;
		
		nextStageTimer = 0;
		shipArrived = false;
		audioClipPlaying = false;
		graphicsManager.resetScrollingBackgroundYOffset();
		explosionManager.reset();
		
		resetStageText();
		enemyManager.setStage(stageNumber);
		enemyManager.reset();
		itemManager.reset();
		stateManager.getCurrentState().reset(false);
	}
	
	protected void resetStageText() {
		String[] intro;
		String[] ending;
		
		if (gameLoopNumber > 0) {
			intro = new String[2];
			intro[1] = "Loop " + Integer.toString(gameLoopNumber);
		} else {
			intro = new String[1];
		}
		
		intro[0] = "Stage " + Integer.toString(stageNumber);
		if (stageNumber != numOfTotalStages) {
			ending = new String[1];
			ending[0] = "Stage " +  Integer.toString(stageNumber) + " Complete!";
		} else {
			ending = new String[3];
			ending[0] = "Congratulations " + playerShip.name() + "!";
			ending[1] = "You have won the war.";
			ending[2] = "Or have you?";
		}
		
		graphicsManager.registerText("Intro", intro, 0.08);
		graphicsManager.registerText("Ending", ending,  0.08);
		introText = graphicsManager.getText("Intro");
		endingText = graphicsManager.getText("Ending");
	}
	
	protected int getTotalNumberOfRandomStages() {
		return StageManager.totalNumberOfRandomStages();
	}
	
	protected double width() {
		return game.width();
	}
	
	protected double height() {
		return game.height();
	}
	
	protected void changeColor(Color c) {
		graphicsManager.changeColor(c);
	}
	
	protected void drawText(double x, double y, String s, String font, int size) {
		graphicsManager.drawText(x, y, s, font, size);
	}
	
	protected void playAudio(String name) {
		audioManager.playAudio(name);
	}
	
	protected void displayAnimatedText(AnimatedText text, double x, double yBase, double yIncrement, String font, int fontSize) {
		graphicsManager.displayAnimatedText(text, x, yBase, yIncrement, font, fontSize);
	}
	
	protected void updateAnimationFrame(String name) {
		graphicsManager.updateAnimationFrame(name);
	}
}