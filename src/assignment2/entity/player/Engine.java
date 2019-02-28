package src.assignment2.entity.player;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
import src.assignment.entity.*;
import src.assignment.state.*;
import src.assignment2.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class Engine extends AbstractEntity implements InterfaceEngine {
	
	/***Properties***/
	
	protected int totalBoostAmount, currentBoostAmount, boostRecharge;
	protected double boostTimer, boostStateTimer, originalBaseVelocity, boostStateDuration;
	protected double targetX, targetY;
	protected boolean up, down, left, right, attemptingBoost, isBoosting;
	
	protected PlayerControls playerControls;	
	protected InterfaceEntity subject;
	
	/***Constructor***/
	
	public Engine(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
		subject = this;
		boostStateDuration = 0.5;
		playerControls = PlayerControls.Keyboard;
	}
	
	/***Getters and Setters***/
	
	public boolean getUp() { return up; }
	public boolean getDown() { return down; }
	public boolean getLeft() { return left; }
	public boolean getRight() { return right; }
	public boolean isBoosting() { return isBoosting; }

	public void setUp(boolean d) { up = d; }
	public void setDown(boolean d) { down = d; }
	public void setLeft(boolean d) { left = d; }
	public void setRight(boolean d) { right = d; }
	public void setIsBoosting(boolean b) { isBoosting = b; }
		
	public int getTotalBoostAmount() { return totalBoostAmount; }
	public void setTotalBoostAmount(int boostAmount) { 
		if (boostAmount < 0) { boostAmount = 0; }
		totalBoostAmount = boostAmount; 
		if (currentBoostAmount > totalBoostAmount) { resetCurrentBoostAmount(); }
	}
		
	public int getCurrentBoostAmount() { return currentBoostAmount; }
	public void setCurrentBoostAmount(int boostAmount) { currentBoostAmount = boostAmount; }
	public void resetCurrentBoostAmount() { currentBoostAmount = totalBoostAmount; }
	
	public int getBoostRecharge() { return boostRecharge; }
	public void setBoostRecharge(int boostRecharge) { 
		if (boostRecharge < 0) { boostRecharge = 0; }
		this.boostRecharge = boostRecharge; 
	}
	
	public InterfaceEntity getSubject() { return subject; }
	public void setSubject(InterfaceEntity s) { subject = s; }
	
	/***Methods***/
		
	public void reset() {
		originalBaseVelocity = 1200;
		disableBoost();
		currentBoostAmount = totalBoostAmount;
	}
	
	public void fullReset(InterfaceGameOptions gameOptions) {
		totalBoostAmount =  gameOptions.getBoostAmount();
		boostRecharge =  gameOptions.getBoostRecharge();
		playerControls = gameOptions.getPlayerControls();
	}
	
	public void update(double dt) {
		if (attemptingBoost == true) {
			boost(dt); 
		}
		updateBoost(dt);
	}

	public void updateBoost(double dt) {
		if (isBoosting == false) { return; }
		boostStateTimer += dt;
		if (boostStateTimer < boostStateDuration) { return; }
		boostStateTimer -= boostStateDuration;
		isBoosting = false;
		subject.setInvulnerable(false);
		subject.setDeflectAttack(false);
		baseVelocity /= 4;
	}
	
	public void move(double dt) {
		subject.setXCoordinate(subject.getXCoordinate() + subject.getXVelocity() * dt );
		subject.setYCoordinate(subject.getYCoordinate() + subject.getYVelocity() * dt );
	}
	
	public void move(boolean allowInput) {
		if (allowInput == false) { return; }
		
		if (playerControls == PlayerControls.Keyboard) { 
			keyBoardMove();
		} else if (playerControls == PlayerControls.Mouse) {
			double xVelocity =  subject.getXVelocity();
			double yVelocity =  subject.getYVelocity();
			
			double xCoordinate = subject.getXCoordinate();
			double yCoordinate = subject.getYCoordinate();
			
			if (xVelocity > 0 && (xCoordinate  > targetX)) {
				subject.setXCoordinate(targetX);
				subject.setXVelocity(0);
			} else if (xVelocity < 0 && (xCoordinate  < targetX)) { 
				subject.setXCoordinate(targetX);
				subject.setXVelocity(0);				
			}
			
			if (yVelocity > 0 && (yCoordinate  > targetY)) {
				subject.setYCoordinate(targetY);
				subject.setYVelocity(0);
			} else if (yVelocity < 0 && (yCoordinate  < targetY)) { 
				subject.setYCoordinate(targetY);
				subject.setYVelocity(0);				
			}
		}
	}
		
	public void render() {
		if (isBoosting == false) { return; }
		
		double subjectXCoordinate = subject.getXCoordinate();
		double subjectYCoordinate = subject.getYCoordinate();
		double subjectWidth = subject.getWidth() * 2;
		double subjectHeight = subject.getHeight() * 2;

		if (up == true && left == true) { 
			playAnimation("PlayerMovement", subjectXCoordinate + subjectWidth, subjectYCoordinate + subjectHeight, subjectWidth, subjectHeight);
			playAnimation("PlayerMovement", subjectXCoordinate + subjectWidth * 1.5, subjectYCoordinate + subjectHeight * 1.5, subjectWidth, subjectHeight);				
		} else if (up == true && right == true) {
			playAnimation("PlayerMovement", subjectXCoordinate - subjectWidth, subjectYCoordinate + subjectHeight, subjectWidth, subjectHeight);	
			playAnimation("PlayerMovement", subjectXCoordinate - subjectWidth * 1.5, subjectYCoordinate + subjectHeight * 1.5, subjectWidth, subjectHeight);	
		} else if (down == true && left == true) { 
			playAnimation("PlayerMovement", subjectXCoordinate + subjectWidth, subjectYCoordinate - subjectHeight, subjectWidth, subjectHeight);
			playAnimation("PlayerMovement", subjectXCoordinate + subjectWidth * 1.5, subjectYCoordinate - subjectHeight * 1.5, subjectWidth, subjectHeight);				
		} else if (down == true && right == true) {
			playAnimation("PlayerMovement", subjectXCoordinate - subjectWidth, subjectYCoordinate - subjectHeight, subjectWidth, subjectHeight);
			playAnimation("PlayerMovement", subjectXCoordinate - subjectWidth * 1.5, subjectYCoordinate - subjectHeight * 1.5, subjectWidth, subjectHeight);
		} else if (up == true) { 
			playAnimation("PlayerMovement", subjectXCoordinate - subjectWidth / 2, subjectYCoordinate + subjectHeight, subjectWidth, subjectHeight);
			playAnimation("PlayerMovement", subjectXCoordinate - subjectWidth / 2, subjectYCoordinate + subjectHeight * 1.5, subjectWidth, subjectHeight);
		} else if (down == true) { 
			playAnimation("PlayerMovement", subjectXCoordinate - subjectWidth / 2, subjectYCoordinate - subjectHeight, subjectWidth, subjectHeight);	
			playAnimation("PlayerMovement", subjectXCoordinate - subjectWidth / 2, subjectYCoordinate - subjectHeight * 1.5, subjectWidth, subjectHeight);	
		} else if (left == true) { 
			playAnimation("PlayerMovement", subjectXCoordinate + subjectWidth, subjectYCoordinate - subjectHeight / 2, subjectWidth, subjectHeight);	
			playAnimation("PlayerMovement", subjectXCoordinate +  subjectWidth * 1.5 , subjectYCoordinate - subjectHeight / 2, subjectWidth, subjectHeight);	
		} else if (right == true) { 
			playAnimation("PlayerMovement", subjectXCoordinate - subjectWidth, subjectYCoordinate - subjectHeight / 2, subjectWidth, subjectHeight);
			playAnimation("PlayerMovement", subjectXCoordinate - subjectWidth * 1.5, subjectYCoordinate - subjectHeight / 2, subjectWidth, subjectHeight);
		}
	}
	
	public void renderUi() {
		double gameWidth = width();
		double gameHeight = height();
		
		int i = 0;
		for (int j = 0; j < currentBoostAmount; j ++) {
			drawImage("Boost", gameWidth * 0.1 + gameWidth * 0.05 * i, gameHeight  * 0.9, gameWidth * 0.05, gameHeight * 0.05);
			i++;
		}
		
		for (int j = i; j < totalBoostAmount; j++) {
			drawImage("Empty", gameWidth * 0.1 + gameWidth * 0.05 * j, gameHeight  * 0.9, gameWidth * 0.05, gameHeight * 0.05);
		}
	}
	
	public void updateRechargeTimers(double dt) {
		if (currentBoostAmount < totalBoostAmount) {
			boostTimer += dt;
			if (boostTimer >= boostRecharge) {
				boostTimer -= boostRecharge;
				currentBoostAmount++;
			}
		}
	}
	
	public void moveToCoordinates(double x, double y) {
		subject.setXVelocity(x - subject.getXCoordinate());
		subject.setYVelocity(y - subject.getYCoordinate());
		double subjectXVelocity = subject.getXVelocity();
		double subjectYVelocity = subject.getYVelocity();
		double l = length(subjectXVelocity, subjectYVelocity);
		subject.setXVelocity(subjectXVelocity * baseVelocity / l * 0.2);
		subject.setYVelocity(subjectYVelocity * baseVelocity / l * 0.2);
	}
	
	public void disableBoost() {
		isBoosting = false;
		attemptingBoost = false;
		baseVelocity = originalBaseVelocity;
		boostTimer = 0;
		boostStateTimer =  0;
		
		up = false;
		down = false;
		left = false;
		right = false;
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode() ;

		double subjectYCoordinate = subject.getYCoordinate();
		if (keyCode == e.VK_UP) {
  			if (subjectYCoordinate >  height / 2) { up = true; }
		}
		
		if (keyCode == e.VK_DOWN) { 
			if (subjectYCoordinate < height() - height / 2) {  down = true; }
		}
		
		double subjectXCoordinate = subject.getXCoordinate();
		if (keyCode == e.VK_LEFT) {
  			if (subjectXCoordinate >  width / 2) { left = true; }
		}
		
		if (keyCode == e.VK_RIGHT) { 
			if (subjectXCoordinate < width() - width / 2) {  right = true; }
		}
		
		if (keyCode == e.VK_Z) { 
			attemptingBoost = true;
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode() ;
		
		if (keyCode == e.VK_UP) {
			subject.setYVelocity(0);
			up = false;
		}
		
		if (keyCode == e.VK_DOWN) { 
			subject.setYVelocity(0);
			down = false;	
		}
		
		if (keyCode == e.VK_LEFT) {
			subject.setXVelocity(0);
			left = false;
		}
		
		if (keyCode == e.VK_RIGHT) { 
			subject.setXVelocity(0);
			right = false;	
		}
	
	}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (button == e.BUTTON2 || button == e.BUTTON3) {  
			attemptingBoost = true;
		}
	}

	public void mouseReleased(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		mouseMove(e);
	}	

	public void mouseDragged(MouseEvent e) {
		mouseMove(e);
	}
	
	protected void keyBoardMove() {
		double subjectXCoordinate = subject.getXCoordinate();
		double subjectWidth = subject.getWidth();
		if (subjectXCoordinate < subjectWidth / 2) {
			subject.setXCoordinate(subjectWidth / 2);
			left = false;
			subject.setXVelocity(0);
		} else if (subjectXCoordinate > width() - subjectWidth / 2) {
			subject.setXCoordinate(width() - subjectWidth / 2);
			right = false;
			subject.setXVelocity(0);
		}
		
		double subjectYCoordinate = subject.getYCoordinate();
		double subjectHeight = subject.getHeight();
		if (subjectYCoordinate < subjectHeight / 2) {
			subject.setYCoordinate(subjectHeight / 2);
			up = false;
			subject.setYVelocity(0);
		} else if (subjectYCoordinate > height() - subjectHeight / 2) {
			subject.setYCoordinate(height() - subjectHeight / 2);
			down = false;
			subject.setYVelocity(0);
		}			
		
		if (up == true) { 
			subject.setYVelocity(-baseVelocity);
		}
		
		if (down == true) { 
			subject.setYVelocity(baseVelocity);
		}
		
		if (left == true) { 
			subject.setXVelocity(-baseVelocity);
		}
		
		if (right == true) { 
			subject.setXVelocity(baseVelocity);
		}		
	}
	
	protected void mouseMove(MouseEvent e) {
		double x = e.getX();
		double y = e.getY();

		double subjectWidth = subject.getWidth();
		double subjectHeight = subject.getHeight();
		
		if (x < subjectWidth / 2 || x > width() - subjectWidth / 2) { return; } 
		if (y < subjectHeight / 2 || y > height() - subjectHeight / 2) { return; }
		
		targetX = x;
		targetY = y;

		subject.setXVelocity(targetX - subject.getXCoordinate());
		subject.setYVelocity(targetY - subject.getYCoordinate());
		double subjectXVelocity = subject.getXVelocity();
		double subjectYVelocity = subject.getYVelocity();
		double l = length(subjectXVelocity, subjectYVelocity);
		subject.setXVelocity(subjectXVelocity * baseVelocity / l);
		subject.setYVelocity(subjectYVelocity * baseVelocity / l);
	}
	
	protected void mouseMove1(MouseEvent e) {
		double x = e.getX();
		double y = e.getY();
		
		double subjectWidth = subject.getWidth();
		double subjectHeight = subject.getHeight();

		if (x > subjectWidth / 2 && x  < width() - subjectWidth / 2) {
			subject.setXCoordinate(x);			
		}
		
		if (y > subjectHeight / 2 && y  < height() - subjectHeight / 2) {
			subject.setYCoordinate(y);
		}
		
	}
	
	protected void boost(double dt) {
		attemptingBoost = false;
		if ( isBoosting == true || currentBoostAmount <= 0) { return; }
		
		currentBoostAmount--;
		isBoosting = true;
		playSound("boost");
		subject.setInvulnerable(true);
		subject.setDeflectAttack(true);
		baseVelocity *= 4;
	}
}