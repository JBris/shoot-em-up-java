package src.assignment.system;

import java.awt.*;
import java.util.*;
import java.lang.*;

public class Effect {
	
	/***Properties***/
	
	public int alpha;
	protected double timer;
	
	/***Constructor***/
	
	public Effect() {
		reset();
	}
	
	/***Getters and Setters***/

	public double getTimer() { return timer;}

	/***Methods***/

	public void updateTimer(double dt) {
		timer += dt;
	}
	
	public void reset() {
		timer = 0;
		alpha = 0;	
	}
	
	public void resetTimer() {
		timer = 0;
	}
	
	public void updateAlpha(double transitionTime) {
		alpha = (int) (255 * timer / transitionTime);
	}
	
}