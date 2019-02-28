package src.assignment.entity;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.state.*;
import src.assignment.system.*;

public abstract class AbstractEntity implements InterfaceEntity {
	
	/***Properties***/
	
	protected InterfaceGame game;
	
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	
	protected String name, skin;
	protected double xCoordinate, yCoordinate;
	protected double xVelocity, yVelocity, baseVelocity;
	protected double width, height;
	protected boolean invulnerable, invisible, isDead, deflectAttack, allowInput;
	
	/***Constructor***/
	
	public AbstractEntity(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
	}
	
	/***Getters and Setters***/
	
	public  String name() { return name; }
	public void setName(String name) { this.name = name; }

	public String getSkin() { return skin; }
	public void setSkin(String skin) { this.skin = skin; }
	
	public double getXCoordinate() { return xCoordinate; }
	public double getYCoordinate() { return yCoordinate; }
	
	public void setXCoordinate(double x) { xCoordinate = x; }
	public void setYCoordinate(double y) { yCoordinate = y; }
	
	public double getXVelocity() { return xVelocity; }
	public double getYVelocity() { return yVelocity; }
	
	public void setXVelocity(double x) { xVelocity = x; }
	public void setYVelocity(double y) { yVelocity = y; }
	
	public double getBaseVelocity() { return baseVelocity; }
	public void setBaseVelocity(double v) { baseVelocity = v; }
	
	public double getHeight() { return height; }
	public double getWidth() { return width; }
	public void setHeight(double h) { height = h; }
	public void setWidth(double w) { width = w; }
	
	public boolean getInvulnerable() { return invulnerable; }
	public void setInvulnerable(boolean invulnerable) { this.invulnerable = invulnerable; }
	
	public boolean isDead() { return isDead; }
	
	public boolean getDeflectAttack() { return deflectAttack; }
	public void setDeflectAttack(boolean d) { deflectAttack = d; }
	
	public boolean getAllowInput() { return allowInput; } 
	public void setAllowInput(boolean allowInput) { this.allowInput = allowInput; }
	
	/***Methods***/

	public void move(double dt) {
		xCoordinate += xVelocity * dt;
		yCoordinate += yVelocity * dt;
	}
	
	public void move() {}
	
	public void kill() { isDead = true; }
	public void revive() { isDead = false; }
	public void damage(int amount) {}
	public void damage(int amount, InterfaceScore scorer) {}
	
	public void moveToCoordinates(double x, double y) {
		xVelocity = x - xCoordinate;
		yVelocity = y - yCoordinate;
		double l = length(xVelocity, yVelocity);
		yVelocity *= baseVelocity / l;	
		xVelocity *= baseVelocity / l;	
	}
	
	public double rand(double max) {
		return game.rand(max);
	}
	
	public int rand(int max) {
		return game.rand(max);
	}
	
	public void stop() {
		xVelocity = 0;
		yVelocity = 0;
	}
	
	protected void drawText(double x, double y, String s, String font, int size) {
		graphicsManager.drawText(x, y, s, font, size);
	}
	
	protected void drawLine(double x1, double y1, double x2, double y2, double l) {
		game.drawDisplayLine(x1, y1, x2, y2, l);
	}
	
	public void playSound(String soundName) {
		audioManager.playAudio(soundName);
	}
	
	protected void changeColor(Color c) {
		graphicsManager.changeColor(c);
	}
	
	public void collide() {}
	
	public void reset() {}
	
	public void update(double dt) {}

	public void render() {}

	public void updateAnimationFrame() {}
	
	protected void drawSolidCircle(double x, double y, double r) {
		game.drawDisplayCircle(x, y, r);
	}
	
	protected void drawSolidRectangle(double x, double y, double w, double h) {
		game.drawDisplayRectangle(x, y , w, h);
	}
	
	protected void updateAnimationFrame(String name) {
		graphicsManager.updateAnimationFrame(name);
	}
	
	protected void playAnimation(String name, double x, double y, double w, double h) {
		graphicsManager.playAnimation(name, x, y, w, h);
	}

	protected void drawImage(String name, double x, double y, double w, double h) {
		graphicsManager.drawImage(name, x, y, w, h);
	}
	
	protected double height() {
		return game.height();
	}
	
	protected double width() {
		return game.width();
	}
	
	protected double length(double x, double y) {
		return game.length(x, y);
	}
	
	protected void switchState(String state) {
		stateManager.switchState(state);
	}
	
	protected InterfaceState getCurrentState() {
		return stateManager.getCurrentState();
	}
}