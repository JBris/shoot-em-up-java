package src.assignment.state;

import java.awt.event.*;
import java.util.*;
import java.lang.*;
import src.assignment.system.*;

public class StateManager implements InterfaceStateManager {
	
	/***Properties***/
	
	protected boolean transitioning;
	protected double transitionTimer;
	
	protected InterfaceGame game;
	protected InterfaceState currentState;
	protected Map<String, InterfaceState> stateList;

	/***Constructor***/
	
	public StateManager(InterfaceGame game, InterfaceState initialState) {
		this.game = game;
		resetTransitionTime();
		stateList = new HashMap<String, InterfaceState>();
		currentState = initialState;
	}

	/***Getters and Setters***/
	
	public InterfaceState getCurrentState() { return currentState; }
		
	public boolean getTransitioning() { return transitioning; }
	public void setTransitioning(boolean transitioning) { this.transitioning = transitioning; }	
	
	/***Methods***/
	
	public void registerState(InterfaceState state) {
		stateList.put(state.getStateName(), state);
	}

	public void switchState(String stateName) {
		InterfaceState newState = stateList.get(stateName);
		if (newState == null) {
			throw new NullPointerException(String.format("State %s has not been registered.", stateName));
		} 
		resetTransitionTime();
		currentState = newState;
	}
	
	public void transitionState(String stateName, double dt,  double transitionTime, boolean resetState, boolean resetArgument) {
		transitionTimer += dt;
		if (transitionTimer >= transitionTime) { 
			switchState(stateName);	
			if (resetState == true) { 
				currentState.reset(resetArgument);	
			} 
		}
	}

	public void paintComponent() {
		currentState.paintComponent();
	}
	
	public void update(double dt) {
		currentState.update(dt);
	}
	
	public void keyPressed(KeyEvent e) { 
		currentState.keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e) {
		currentState.keyReleased(e);
	}
	
	public void mouseExited(MouseEvent e) {
		currentState.mouseExited(e);
	}
	
	public void mousePressed(MouseEvent e) {
		currentState.mousePressed(e);
	}
	
	public void mouseReleased(MouseEvent e) {
		currentState.mouseReleased(e);
	}
	
	public void mouseMoved(MouseEvent e) {
		currentState.mouseMoved(e);
	}

	public void mouseDragged(MouseEvent e) {
		currentState.mouseDragged(e);
	}
	
	public String getServiceId() { return "StateManager"; }
	
	protected  void resetTransitionTime() {
		transitionTimer = 0;
		transitioning = false;
	}
}