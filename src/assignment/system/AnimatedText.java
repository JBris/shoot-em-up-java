package src.assignment.system;

import java.awt.*;
import java.util.*;
import java.lang.*;

public class AnimatedText {
	
	/***Properties***/
	
	public double xCoordinate, yCoordinate, xTarget, yTarget;
	public double xVelocity, yVelocity;
	
	protected InterfaceGame game;
	
	protected double displayTimer, displayTime;
	protected String name;
	protected String[] text, displayText;
	
	/***Constructor***/
	
	public AnimatedText(InterfaceGame game, String name, String[] text, double displayTime) {
		this.game = game;
		this.name = name;
		this.text= text;
		this.displayTime = displayTime;
		displayTimer = 0;
		
		int length = text.length;
		displayText = new String[length];
		for (int i = 0; i < length; i++) {
			displayText[i] = "";
		}
	}
	
	/***Getters and Setters***/

	public String getName() { return name; }
	public double getDisplayTime() { return displayTime;}
	public double getDisplayTimer() { return displayTimer;}
	public String[] getText() { return text; }
	public String[] getDisplayText() { return displayText; }
	
	/***Methods***/

	public void update(double dt) {
		displayTimer += dt;
	}
	
	public void resetDisplayTimer() {
		displayTimer = 0;
	}
	
	public boolean isDisplayTimeReached(boolean decrementTimer) {
		if (displayTimer < displayTime) {  return false; }
		if (decrementTimer == true) { displayTimer -= displayTime; }
		return true;
	}
	
	public void transferTextChars() {
		for (int i = 0; i < text.length; i++) {
			if (text[i].length() == displayText[i].length()) { continue; }
			int j = displayText[i].length();
			displayText[i] = text[i].substring(0, j + 1);
			break;
		}
	}
	
	public boolean isCharTransferComplete() {
		int length = text.length - 1;
		return text[length].length() == displayText[length].length();
	}

	public void setMoveCoordinates(double speed) {
		setMoveCoordinates(xTarget, yTarget, speed);
	}
	
	public void setMoveCoordinates(double x, double y, double speed) {
		xVelocity = x - xCoordinate;
		yVelocity = y - yCoordinate;
		double l = game.length(xVelocity, yVelocity);
		yVelocity *= speed/l;	
		xVelocity *= speed/l;	
	}
	
	public void move(double dt) {
		xCoordinate += xVelocity * dt;
		yCoordinate += yVelocity * dt;
	}
	
	public void syncCoordsWithTarget() {
		xCoordinate = xTarget;
		yCoordinate = yTarget;
	}
	
}