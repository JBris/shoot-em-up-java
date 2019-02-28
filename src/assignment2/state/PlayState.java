package src.assignment2.state;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class PlayState extends AbstractState {
	
	/***Properties***/
	
	protected InterfaceGameOptions gameOptions;
	protected InterfaceLeaderboard leaderboard;
	protected InterfacePlayerShip playerShip;
	protected InterfaceStageManager stageManager;
	protected InterfaceExplosionManager explosionManager;

	protected boolean paused, inGameOverState, fadeInEnabled, fadeOutEnabled; 
	
	/***Constructor***/

	public PlayState(InterfaceGame game, InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager, InterfaceGameOptions gameOptions, InterfaceLeaderboard leaderboard, 
		InterfacePlayerShip playerShip, InterfaceStageManager stageManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager);
		this.gameOptions = gameOptions;
		this.leaderboard = leaderboard;
		this.playerShip = playerShip;
		this.stageManager = stageManager;
		this.explosionManager = explosionManager;
		reset(false);
	}
	
	/***Methods***/
	
	public void reset(boolean fullReset) {
		paused = false;
		fadeInEnabled = true;
		fade.reset();

		if (fullReset == true) {
			fullReset();
		}
		
		playerShip.reset();
	}
	
	public void paintComponent() {		
		int fontSize = (int) Math.round(height() * 0.05);
		String font = "Arial";
		double width = width();
		double height = height();

		if (inGameOverState == false) {
			stageManager.paintComponent();		
			playerShip.paintComponent();
			explosionManager.paintComponent();

			if (paused == true) {
				changeColor(Color.WHITE);
				drawText(width * 0.35, height / 2 , "Paused", font, fontSize * 2); 
			}
			
		} else {		
			if (fadeOutEnabled == true) {
				stageManager.paintComponent();		
				playerShip.paintComponent();
				explosionManager.paintComponent();
				fadeOut(0, 0, 0, fade.alpha);
			} else {
				double widthTextAlign = width() * 0.3;
				int loopNumber = stageManager.getGameLoopNumber();
				changeColor(Color.YELLOW);			
				graphicsManager.renderScrollingBackground("Intro");
				drawText(widthTextAlign, height * 0.3 , "Game Over!", font, fontSize);	
				drawText(widthTextAlign, height * 0.4 , playerShip.name(), font, fontSize);			
				drawText(widthTextAlign, height * 0.5 , "Score: " +  Integer.toString(playerShip.getScore()), font, fontSize);			
				drawText(widthTextAlign, height * 0.6 , "Stage: " +  Integer.toString(stageManager.getStageNumber()), font, fontSize);			

				if (loopNumber > 0) {
					drawText(widthTextAlign, height * 0.7 , "Loop: " +  Integer.toString(loopNumber), font, fontSize);			
				}
				
				drawText(widthTextAlign, height  * 0.85 , "Press Esc", font, fontSize);	
				
	
			}
		}
		
		if (fadeInEnabled == true) {
			fadeIn(0, 0, 0, fade.alpha);	
		} else if (getTransitioning() == true || getIsStageTransitioning() == true) {
			fadeOut(0, 0, 0, fade.alpha);
		}
	}
	
	public void update(double dt) {
		if (fadeInEnabled == true) {
			updateFadeAlpha(dt, 1);	
			if (fade.alpha >= 255) {
				if (inGameOverState == true) { playerShip.setAllowInput(true); }
				fadeInEnabled = false;
			}
			return;
		}
		
		if (getTransitioning() == true) {
			double transitionTime = 2;
			updateFadeAlpha(dt, transitionTime);
			transitionState(GameState.Menu.name(), dt, transitionTime,  true, true);
			return;
		}
		
		if (inGameOverState == true) { 
			updateGameOver(dt); 
			return;
		}
		
		if (getGameOverTrigger() == true) { 
			beginGameOverState(); 
			return;
		}
		
		if (getIsStageTransitioning() == true) {
			double transitionTime = 2;
			updateFadeAlpha(dt, transitionTime);
			nextStage(dt, transitionTime);
			return;
		}
			
		if (getStageCompletionTrigger()  == true) {
			cleanup();
			setIsStageTransitioning(true);
			return;
		}
		
		if (paused == true) { return; }
		
		playerShip.update(dt);
		stageManager.update(dt);
	}
	
	public String getStateName() { return GameState.Play.name(); }

	public void keyPressed(KeyEvent e) {
		if (getTransitioning() == true || getIsStageTransitioning() == true) { return; }
		if (playerShip.getAllowInput() == false) { return; }
		int keyCode = e.getKeyCode() ;
		
		if (keyCode == e.VK_ESCAPE) {
			playAudio("back");
			if (getGameOverTrigger() == false) {
				setGameOverTrigger(true);
			} else {
				cleanup();
				setTransitioning(true);
			}
		}	
		
		if (getGameOverTrigger()  == true) { return; }
		
		if (keyCode == e.VK_P) {
			pause();
		}
		if (paused == true) { return; }
		
		if (gameOptions.getPlayerControls() != PlayerControls.Keyboard) { return; }
		playerShip.keyPressed(e);			
	}
	
	public void keyReleased(KeyEvent e) {
		if (getTransitioning() == true || getIsStageTransitioning() == true) { return; }
		if (playerShip.getAllowInput() == false || getGameOverTrigger()  == true || paused == true) { return; }
		
		if (gameOptions.getPlayerControls() != PlayerControls.Keyboard) { return; }
		playerShip.keyReleased(e);		
	}
	
	public void mouseExited(MouseEvent e) {
		if (getTransitioning() == true || getIsStageTransitioning() == true) { return; }
		if (playerShip.getAllowInput() == false || getGameOverTrigger()  == true || paused == true) { return; }
		if (gameOptions.getPlayerControls() == PlayerControls.Mouse) { return; }

		pause(); 
	}
	
	public void mousePressed(MouseEvent e) {
		if (getTransitioning() == true || getIsStageTransitioning() == true) { return; }
		if (playerShip.getAllowInput() == false || getGameOverTrigger()  == true || paused == true) { return; }	

		if (gameOptions.getPlayerControls() != PlayerControls.Mouse) { return; }
		playerShip.mousePressed(e);			
	}

	public void mouseReleased(MouseEvent e) {
		if (getTransitioning() == true || getIsStageTransitioning() == true) { return; }
		if (playerShip.getAllowInput() == false || getGameOverTrigger()  == true || paused == true) { return; }	

		if (gameOptions.getPlayerControls() != PlayerControls.Mouse) { return; }
		playerShip.mouseReleased(e);			
	}

	public void mouseMoved(MouseEvent e) {
		if (getTransitioning() == true || getIsStageTransitioning() == true) { return; }
		if (playerShip.getAllowInput() == false || getGameOverTrigger()  == true || paused == true) { return; }		
		
		if (gameOptions.getPlayerControls() != PlayerControls.Mouse) { return; }
		playerShip.mouseMoved(e);	
	}
	
	public void mouseDragged(MouseEvent e) {
		if (getTransitioning() == true || getIsStageTransitioning() == true) { return; }
		if (playerShip.getAllowInput() == false || getGameOverTrigger()  == true || paused == true) { return; }		
		
		if (gameOptions.getPlayerControls() != PlayerControls.Mouse) { return; }
		playerShip.mouseDragged(e);	
	}
	
	protected void pause() {
		if (paused == false) {	
			paused = true;	
			playerShip.stop();
			playerShip.disableBoost();
			playerShip.disableAttack();
		} else {
			paused = false;
		}	
	}
	
	protected void beginGameOverState() {
		inGameOverState = true;
		savePlayerScore();
		playerShip.setAllowInput(false);
		playerShip.setInvulnerable(true);
		fadeOutEnabled = true;
		fade.reset();
	}
	
	protected void savePlayerScore() {
		leaderboard.savePlayerScore(playerShip, stageManager.getStageNumber(), stageManager.getGameLoopNumber(), gameOptions);
	}
	
	protected void updateGameOver(double dt) {		
		graphicsManager.incrementScrollingBackgroundYOffset();	
		
		if (fadeOutEnabled == false) { return; }
		
		playerShip.updateAnimationFrame();
		explosionManager.update(dt);
		
		updateFadeAlpha(dt, 2);	
		if (fade.alpha >= 255) {
			fade.reset();
			fadeOutEnabled = false;
			fadeInEnabled = true;
			graphicsManager.resetScrollingBackgroundYOffset();
		}
	}
	
	protected boolean getGameOverTrigger() {
		return playerShip.getGameOverTrigger();
	}
	
	protected void setGameOverTrigger(boolean t) {
		playerShip.setGameOverTrigger(t);
	}
	
	protected void fullReset() {
		inGameOverState = false;
		fadeOutEnabled = false;
		
		audioManager.stopAudioLoop("Menu");
		graphicsManager.resetScrollingBackgrounds();
		audioManager.resetPlayList();
		stageManager.reset(gameOptions, true);
		playerShip.fullReset(gameOptions);
		playerShip.promptName();
	}
	
	protected void cleanup() {
		stageManager.cleanup();
		playerShip.reset();
		fade.reset();
	}
	
	protected boolean getInIntroState() {
		return stageManager.getInIntroState();
	}
	
	protected boolean getStageCompletionTrigger() {
		return stageManager.getStageCompletionTrigger();
	}
	
	protected void setStageCompletionTrigger(boolean s) {
		stageManager.setStageCompletionTrigger(s);
	}
	
	protected boolean getIsStageTransitioning() {
		return stageManager.getIsTransitioning();
	}
	
	protected void setIsStageTransitioning(boolean isTransitioning) {
		stageManager.setIsTransitioning(isTransitioning);
	}
	
	protected void nextStage(double dt, double transitionTime) {
		stageManager.nextStage(dt, transitionTime);
	}
}