package src.assignment.state;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import src.assignment.system.*;

public abstract class AbstractState implements InterfaceState {
	
	/***Properties***/
		
	protected InterfaceGame game;
	
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	
	protected Effect fade;

	/***Constructor***/
	
	public AbstractState(InterfaceGame game, InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager) {
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
		fade = new Effect();
	}
	
	/***Methods***/
	
	public void reset() {}

	public void reset(boolean playAudio) {}
	
	public void update(double dt) {}
	
	public void paintComponent() {}
	
	public void keyPressed(KeyEvent e) {}
	
	public void keyReleased(KeyEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}
	
	public void mouseDragged(MouseEvent e) {}

	public abstract String getStateName();

	protected void switchState(String state) {
		stateManager.switchState(state);
	}
	
	protected void transitionState(String stateName, double dt, double transitionTime, boolean resetState, boolean resetArgument) {
		stateManager.transitionState(stateName, dt, transitionTime, resetState, resetArgument);
	}
	
	protected boolean getTransitioning() {
		return stateManager.getTransitioning();
	}
	
	protected void setTransitioning(boolean transitioning) {
		stateManager.setTransitioning(transitioning);
	}
	
	protected InterfaceState getCurrentState() {
		return stateManager.getCurrentState();
	}
	
	protected void changeColor(Color c) {
		graphicsManager.changeColor(c);
	}
	
	protected void fadeOut(int r, int b, int g, int alpha) {
		graphicsManager.fadeOut(r, b, g, alpha);
	}
	
	protected void fadeIn(int r, int b, int g, int alpha) {
		graphicsManager.fadeIn(r, b, g, alpha);
	}
	
	protected void updateFadeAlpha(double dt, double transitionTime) {
		fade.updateTimer(dt);
		fade.updateAlpha(transitionTime);
	}
	
	protected void drawText(double x, double y, String s, String font, int size) {
		graphicsManager.drawText(x, y, s, font, size);
	}
	
	protected void drawLine(double x1, double y1, double x2, double y2, double l) {
		game.drawDisplayLine(x1, y1, x2, y2, l);
	}
	
	protected void drawMenuText (int menuChoice, int menuItem, double width, double height, String text) {
		graphicsManager.drawMenuText(menuChoice, menuItem, width, height, text);
	}
	
	public void drawMenuText (int menuChoice, int menuItem, double width, double height, String text, int fontSize) {
		graphicsManager.drawMenuText(menuChoice, menuItem, width, height, text, fontSize);
	}
	
	protected void displayAnimatedText(AnimatedText text, double x, double yBase, double yIncrement, String font, int fontSize) {
		graphicsManager.displayAnimatedText(text, x, yBase, yIncrement, font, fontSize);
	}
	
	protected void drawImage(String name, double x, double y, double w, double h) {
		graphicsManager.drawImage(name, x, y, w, h);
	}

	protected void playAudio(String name) {
		audioManager.playAudio(name);
	}
	
	protected double height() {
		return game.height();
	}
	
	protected double width() {
		return game.width();
	}
		
	protected void sleep(double duration) {
		game.sleep(duration);
	}
	
	protected double getTime() {
		return game.getTime();
	}
}