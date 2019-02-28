package src.assignment2.state;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class MenuState extends AbstractState {
	
	/***Properties***/

	protected boolean introComplete, titleFlashing, musicPlaying;
	protected int menuChoice;
	protected double dt;
	
	protected AnimatedText titleText;

	/***Constructor***/

	public MenuState(InterfaceGame game, InterfaceStateManager stateManager, 
		InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
		reset(false);
	}
	
	/***Methods***/
	
	public void paintComponent() {	
		double width = width();	
		double height = height();		
		double menuWidth = width* 0.3;
		graphicsManager.renderScrollingBackground("Menu");
		game.changeColor(Color.RED);
		drawText(titleText.xCoordinate, titleText.yCoordinate , titleText.getText()[0], "Arial", (int) (height * 0.08));

		if (introComplete == false) { return; }
	
		graphicsManager.playAnimation("PlayerMovement", width() * 0.05, height * 0.14, width * 0.075, height * 0.075);		
		graphicsManager.playAnimation("PlayerMovement", width() * 0.8, height * 0.14, width * 0.075, height * 0.075);		

		drawMenuText(menuChoice, 0, menuWidth, height * 0.4, "Start");
		drawMenuText(menuChoice, 1, menuWidth, height * 0.5, "Leaderboard");
		drawMenuText(menuChoice, 2, menuWidth, height * 0.6, "Options");
		drawMenuText(menuChoice, 3, menuWidth, height * 0.7, "Controls");
		drawMenuText(menuChoice, 4, menuWidth, height * 0.8, "Exit");
		
		
		if (titleFlashing == true) {
			fadeIn(255, 255, 255, fade.alpha);
		}
		
		if (getTransitioning() == true) {
			fadeOut(0, 0, 0, fade.alpha);
		}
	}
	
	public void reset(boolean playIntro) {
		introComplete = true;
		musicPlaying = true;
		titleFlashing = false;
		
		if (playIntro == true) {
			introComplete = false;
			musicPlaying = false;
			menuChoice = 0;
			graphicsManager.resetScrollingBackgroundYOffset();
		} 
		
		fade.reset();
		String[] title = { "BIONIC PILOT" };
		graphicsManager.registerText("Title", title, 0.8);
		titleText = graphicsManager.getText("Title");
		titleText.xCoordinate = game.width()  * 0.2;
		titleText.yCoordinate = -game.height()  * 0.2;
		titleText.xTarget = titleText.xCoordinate;
		titleText.yTarget = -titleText.yCoordinate;
	}
	
	public void update(double dt) {
		graphicsManager.incrementScrollingBackgroundYOffset();	
		if (introComplete == false) {
			titleText.setMoveCoordinates(500);
			titleText.move(dt);
			if (titleText.yCoordinate >= titleText.yTarget) { 
				titleText.syncCoordsWithTarget();
				introComplete = true;
				titleFlashing = true;
			}
			return;
		} 
		
		this.dt = dt;
		graphicsManager.updateAnimationFrame("PlayerMovement");

		if (musicPlaying == false) {
			musicPlaying = true;	
			playAudio("pewpew");
			audioManager.startAudioLoop("Menu");
		}
		
		if (titleFlashing == true) {
			updateFadeAlpha(dt, 1);
			if (fade.alpha >= 255) { titleFlashing = false; }
		}
		
		if (getTransitioning() == true) {
			double transitionTime = 2;
			updateFadeAlpha(dt, transitionTime);
			transitionState(GameState.Play.name(), dt, transitionTime,  true, true);
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode() ;
		if (keyCode == e.VK_ESCAPE) { System.exit(0); }
		
		if (introComplete == false || titleFlashing == true) { return; }
		if (getTransitioning() == true) { return; }
		
		if (keyCode == e.VK_UP) {
			playAudio("beep");
			if (menuChoice <= 0) { 
				menuChoice = 4; 
			} else {
				menuChoice --;
			}
		}
		
		if (keyCode == e.VK_DOWN) {
			playAudio("beep");
			if (menuChoice >= 4) { 
				menuChoice = 0; 
			} else {
				menuChoice ++;
			}
		}
		
		if (keyCode == e.VK_ENTER) {
			playAudio("back");
			if (menuChoice == 0) {
				setTransitioning(true);
				fade.reset();
			} else if (menuChoice == 1) {
				switchState(GameState.Leaderboard.name());
				getCurrentState().reset();
			} else if (menuChoice == 2) {
				switchState(GameState.Options.name());
				getCurrentState().reset();
			} else if (menuChoice == 3) {
				switchState(GameState.Controls.name());
				getCurrentState().reset();
			} else if (menuChoice == 4) {
				System.exit(0);
			}
		}
	}
	
	public String getStateName() { return GameState.Menu.name(); }
}