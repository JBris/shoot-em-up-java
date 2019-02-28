package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class Dove extends AbstractEnemyShip {
	
	/***Properties***/
	
	protected boolean dropState, bounceState, finalState;
	
	/***Constructor***/
	
	public Dove(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	/***Getters and Setters***/
			
	/***Methods***/
		
	public void reset() {
		super.reset();
		if (squadSize > 8) { squadSize = 8; }
		dropState = true;
		bounceState = true;
		finalState = false;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyBullet> bulletList) {
		super.update(dt, playerShip, bulletList);
		if (attackAvailable == false) { return; }
		
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceEnemyBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			bullet.setBaseVelocity(bulletBaseVelocity * projectileSpeedModifier);
			bullet.fire(playerShip.getXCoordinate(), playerShip.getYCoordinate(), xCoordinate, yCoordinate);		
			enemyManager.deployBullet(bullet);	
			attackAvailable = false;
			increaseNextAttackTime();
			break;
		}
	}
	
	public void launch(double dt) {
		isDeployed = true;
	}
	
	public void move() {
		super.move();
		
		if (hasEnteredWindow == false) { return; }
		
		if (dropState == true) {
			
			if (xCoordinate < width() * 0.15 && xVelocity < 0) {
				xCoordinate = width() * 0.15;
				xVelocity = 0;
				yVelocity = baseYVelocity;
				dropState = false;
			} else if (xCoordinate > width() * 0.85 && xVelocity > 0) {
				xCoordinate = width() * 0.85;
				xVelocity = 0;
				yVelocity = baseYVelocity;
				dropState = false;
			}
			
		} else if (bounceState == true) {
			
			if (yCoordinate > height() * 0.9) {
				yCoordinate =  height() * 0.9;
				yVelocity *= -1;
				bounceState = false;
				finalState = true;
			}
			
		} else if (finalState == true) {
			
			if (yCoordinate < height() * 0.1) {
				yCoordinate =  height() * 0.1;
				yVelocity *= -1;
				finalState = false;
			}
			
		}
	}
	
	protected void init() {
		name = EnemyType.Dove.name();
		skin = name;
		
		originalAttackTime = 0.25;
		attackTime = originalAttackTime;
		attackTimeIncrease = 0.75;
		
		baseXVelocity = 200;
		baseYVelocity = 200;
		bulletBaseVelocity = 350;
		
		baseSquadSize = 3;
		
		pointValue = 20;
		
		totalShieldAmount = 70;
		
		width = width() * 0.12;
		height = height() * 0.12;
				
		baseNextDeploymentTime = 4;
		reset();
	}
	
	protected void preLaunch(InterfaceEnemyShip lastLaunchedShip, InterfaceEnemyShip nextLaunchedShip) {
		if (numOfSquadDeployed == 0) {
			setInitialLaunchCoordinates();
		} else {
			
			double lastXCoordinate = lastLaunchedShip.getXCoordinate();
			double lastYCoordinate = lastLaunchedShip.getYCoordinate();
			double lastWidth = lastLaunchedShip.getWidth();
			double lastHeight = lastLaunchedShip.getHeight();
	
			nextLaunchedShip.setYCoordinate(lastYCoordinate);
			
			if (lastXCoordinate < width() / 2) {
				nextLaunchedShip.setXCoordinate(lastXCoordinate - lastWidth * 2);
				nextLaunchedShip.setXVelocity(lastLaunchedShip.getXVelocity());
			} else {
				nextLaunchedShip.setXCoordinate(lastXCoordinate + lastWidth * 2);
				nextLaunchedShip.setXVelocity(lastLaunchedShip.getXVelocity());
			}
		}
	}
	
	protected void setInitialLaunchCoordinates() {
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
}