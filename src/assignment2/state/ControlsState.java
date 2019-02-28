package src.assignment2.state;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class ControlsState extends AbstractState {
	
	protected int menuChoice;
	
	/***Constructor***/

	public ControlsState(InterfaceGame game, InterfaceStateManager stateManager, 
		InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
	}
	
	/***Methods***/
	
	public void update(double dt) {
		graphicsManager.incrementScrollingBackgroundYOffset();		
	}
	
	public void paintComponent() {		
		int fontSize = (int) Math.round(height() * 0.035);
		String font = "Arial";
		double height = height();
		double widthTextAlign = width() * 0.15;
		
		graphicsManager.renderScrollingBackground("Menu");
		changeColor(Color.WHITE);
		drawText(widthTextAlign, height * 0.1 , "Player Movement - Arrow Keys", font, fontSize);
		drawText(widthTextAlign, height * 0.2 , "Basic Attack - Space", font, fontSize);
		drawText(widthTextAlign, height * 0.3 , "Special Attack - X", font, fontSize);
		drawText(widthTextAlign, height * 0.4 , "Dodge - Z", font, fontSize);	
		drawText(widthTextAlign, height * 0.5 , "Pause - P", font, fontSize);
		drawText(widthTextAlign, height * 0.6 , "Confirm - Enter", font, fontSize);
		drawText(widthTextAlign, height * 0.7 , "Back - Escape", font, fontSize);
		drawText(widthTextAlign, height * 0.8 , "Change Options - Left & Right Arrow", font, fontSize);
		changeColor(Color.YELLOW);
		drawText(widthTextAlign, height * 0.9 , "Back to Menu", font, fontSize);
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == e.VK_ENTER || keyCode == e.VK_ESCAPE) {
			playAudio("back");
			menuChoice = 0;
			switchState(GameState.Menu.name()); 
		}
	}
	
	public String getStateName() { return GameState.Controls.name(); }
}