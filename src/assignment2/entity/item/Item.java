package src.assignment2.entity.item;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class Item extends AbstractDeployableEntity implements InterfaceItem {
	
	/***Properties***/
	
	protected int pointValue;
	protected boolean isWorthPoints; 
	
	/***Constructor***/
	
	public Item(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, String itemName) {
		super(game, stateManager, graphicsManager, audioManager);
		initAttributes(itemName);
	}
	
	/***Getters and Setters***/
	
	
	public boolean isWorthPoints() { return isWorthPoints; }
	public void setIsWorthPoints(boolean p) { isWorthPoints = p; }
	
	public int getPointValue() { return pointValue; }
	public void setPointValue(int v) { pointValue = v; }
	
	/***Methods***/
	
	public void update(double dt) {
		move(dt);
		move();
	}
	
	public void move() {
		if (xCoordinate < -width /2 || xCoordinate > width() + width / 2) {
			reset();
		} else if (yCoordinate > height() + height / 2) {
			reset();
		}
	}
	
	public void collide(InterfacePlayerShip playerShip, InterfaceEnemyManager enemyManager, int scoreMultiplier) {
		if (playerShip.getAllowInput() == false) { return; }
		
		double playerShipWidth = playerShip.getWidth();
		double playerShipHeight = playerShip.getHeight();
		double playerShipXPosition = playerShip.getXCoordinate();
		double playerShipYPosition = playerShip.getYCoordinate();
		double playerShipYVelocity = playerShip.getYVelocity();

		double playerShipLeft = playerShipXPosition - playerShipWidth / 2;
		double playerShipRight = playerShipXPosition + playerShipWidth / 2;
		double playerShipTop = playerShipYPosition - playerShipHeight / 2;
		double playerShipBottom = playerShipYPosition + playerShipHeight / 2;
		
		double itemLeft = xCoordinate - width / 2;
		double itemRight = xCoordinate + width / 2;
		double itemTop = yCoordinate - height /2;
		double itemBottom = yCoordinate + height /2;
				
		if (itemRight < playerShipLeft) { return; }
		if (itemLeft > playerShipRight) { return; }
		if (itemBottom < playerShipTop) { return; }
		if (itemTop > playerShipBottom) { return; }
		
		playSound("drop");		
		if (isWorthPoints == true) { 
			playerShip.incrementScoreByValue(pointValue * scoreMultiplier);
		} else if (deflectAttack == true) {
			enemyManager.deflectAllBullets();
		} else {
			playerShip.switchSpecialBullets(name);
		}
		
		reset();
	}
	
	public void render() {
		playAnimation(skin, xCoordinate - width / 2, yCoordinate - height / 2, width, height);		
	} 
	
	protected void initAttributes(String itemName) {
		yVelocityModifer = 1;
		skin = itemName + "Drop";
		xCoordinate = -width();
		yCoordinate = -height();
		xVelocity = 0;
		yVelocity = 0; 
		baseVelocity = 150;
		width = width() * 0.1;
		height = height() * 0.1;
		name = itemName;
		pointValue = 50;
		isWorthPoints = true;
 	}
	
	public void reset() {
		isDeployed = false;
		deflectAttack = false;
		xVelocity = 0;
		yVelocity = 0; 
		xCoordinate = -width();
		yCoordinate = -height();
	}
}