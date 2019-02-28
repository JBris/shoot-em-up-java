package src.assignment2.state;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class IntroState extends AbstractState {
	
	/***Properties***/
	
	protected AnimatedText displayText;
	
	/***Constructor***/

	public IntroState(InterfaceGame game, InterfaceStateManager stateManager, 
		InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
		setOutputText();
		graphicsManager.resetScrollingBackgroundYOffset();
	}
	
	/***Methods***/
	
	public void paintComponent() {
		graphicsManager.renderScrollingBackground("Intro");

		game.changeColor(Color.YELLOW);
		displayAnimatedText(displayText, game.width() * 0.15, 0.1, 0.15, "Arial", (int) (game.height() * 0.03));
		
		if (getTransitioning() == true) {
			fadeOut(0, 0, 0, fade.alpha);
		}
	}
	
	public void update(double dt) {
		graphicsManager.incrementScrollingBackgroundYOffset();	

		boolean transitioning = getTransitioning();
		double transitionTime = 3;
		if (transitioning == true) {
			updateFadeAlpha(dt, transitionTime);
			transitionState(GameState.Menu.name(), dt, transitionTime,  true, true);
		}
		
		displayText.update(dt);
		if (displayText.isDisplayTimeReached(true) == false) { return; }
		displayText.transferTextChars();
		if (transitioning == false) { setTransitioning(displayText.isCharTransferComplete()); }
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode() ;
		if (keyCode == e.VK_ESCAPE) { System.exit(0); }
		setTransitioning(true);
	}
	
	public String getStateName() { return GameState.Intro.name(); }
	
	protected void setOutputText() {
		String[] text = {
			"The year is 1984.",
			"For the last one hundred years" ,
			"the USA and Soviet Union have been at war.",
			"This fight has long been a stalemate.",
			"Until now..."
		};
		
		graphicsManager.registerText("Intro", text, 0.05);
		displayText = graphicsManager.getText("Intro");
	}
}