package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class BigJelly extends AbstractEnemyShip {
	
	/***Properties***/
	
	protected boolean fallingState, downShot;
	protected int attackCount, maxAtacks;
	protected double yTarget;
	
	/***Constructor***/
	
	public BigJelly(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	/***Getters and Setters***/
			
	/***Methods***/
		
	public void reset() {
		super.reset();
		downShot = false;
		squadSize = 1; 
		fallingState = false;
		yTarget = 0;
		attackCount = 0;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyBullet> bulletList) {
		super.update(dt, playerShip, bulletList);
		if (attackAvailable == false) { return; }
		
		if (downShot == true) {
			downShot(bulletList);
		} else {
			for (int i = 0; i < bulletList.size(); i++) {
				InterfaceEnemyBullet bullet = bulletList.get(i);
				if (bullet.isDeployed() == true) { continue; }
				bullet.setBaseVelocity(bulletBaseVelocity * projectileSpeedModifier);
				bullet.fire(playerShip.getXCoordinate(), playerShip.getYCoordinate(), xCoordinate, yCoordinate);		
				enemyManager.deployBullet(bullet);	
				attackAvailable = false;
				break;
			}
			
			attackTime = originalAttackTime;
			downShot = true;
		}
		
	}
	
	public void move() {
		super.move();
		if (hasEnteredWindow == false) { return; }

		if (fallingState == false) {
			horizontalMovement();
		} else {
			if (yCoordinate >= yTarget) {
				returnToHorizontalMovement();
			}
		}
				
	}
	
	protected void horizontalMovement() {
	
		if (xVelocity < 0 && (xCoordinate < width() * 0.2) ) {
			xCoordinate = width() * 0.2;
			xVelocity = 0;
			yVelocity = baseYVelocity;
			fallingState = true;
			yTarget = yCoordinate + height() * 0.2;
		} else if (xVelocity > 0 && (xCoordinate > width() * 0.8) ) {
			xCoordinate = width() * 0.8;
			xVelocity = 0;
			yVelocity = baseYVelocity;
			fallingState = true;
			yTarget = yCoordinate + height() * 0.2;
		}
	}
	
	protected void returnToHorizontalMovement() {
		yCoordinate = yTarget;
		yVelocity = 0;
		fallingState = false;
		
		if(xCoordinate < width() / 2) {
			xVelocity = baseXVelocity;
		} else {
			xVelocity = -baseXVelocity;
		}
	}
	
	protected void init() {
		name = EnemyType.BigJelly.name();
		skin = name;
		
		originalAttackTime = 0.5;
		attackTime = originalAttackTime;
		attackTimeIncrease = 0.75;
		maxAtacks = 3;
		
		baseXVelocity = 50;
		baseYVelocity = 40;
		projectileSpeedModifier = 1;
		bulletBaseVelocity = 350;
		
		baseSquadSize = 4;
		
		pointValue = 75;
		
		totalShieldAmount = 750;
		
		width = width() * 0.12;
		height = height() * 0.12;
				
		baseNextDeploymentTime = 3;
		reset();
	}
	
	public void launch(double dt) {
		isDeployed = true;
	}
	
	protected void preLaunch(InterfaceEnemyShip lastLaunchedShip, InterfaceEnemyShip nextLaunchedShip) {
		isSquadLeader = true;
		
		Random random = new Random();
		double randomVal = random.nextDouble();
		if (randomVal < 0.5) {
			xCoordinate = -width() * 0.15;
			xVelocity = baseXVelocity;
		} else {
			xCoordinate = width() * 1.15;
			xVelocity = -baseXVelocity;
		}
		yCoordinate = height() * 0.1;
	}
	
	protected void downShot(ArrayList<InterfaceEnemyBullet> bulletList) {
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceEnemyBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			bullet.fireAtVelocity(0, bulletBaseVelocity * projectileSpeedModifier, xCoordinate, yCoordinate);		
			enemyManager.deployBullet(bullet);	
			attackAvailable = false;
			attackCount++;
			break;
		}
		
		if (attackCount >= maxAtacks) {
			downShot = false;
			attackTime = 3 * attackTimeIncreaseModifier;		
			attackCount = 0;
		} 
	}
}