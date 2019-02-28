package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class Ligher extends AbstractEnemyShip {
	
	/***Properties***/
	
	protected int bouncesRemaining;
	protected boolean directAttackState;
	
	/***Constructor***/
	
	public Ligher(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	/***Getters and Setters***/
			
	/***Methods***/
		
	public void reset() {
		super.reset();
		if (squadSize % 2 != 0) { squadSize--; }
		if (squadSize > 6) { squadSize = 6; }
		bouncesRemaining = 6;
		directAttackState = false;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyBullet> bulletList) {
		super.update(dt, playerShip, bulletList);
		if (attackAvailable == false) { return; }
		
		ArrayList<InterfaceEnemyBullet> firedBullets = new ArrayList<InterfaceEnemyBullet>();
		
		if (directAttackState == false) {
			
			for (int i = 0; i < bulletList.size(); i++) {
				InterfaceEnemyBullet bullet = bulletList.get(i);
				if (bullet.isDeployed() == true) { continue; }
				bullet.fireAtVelocity(0, bulletBaseVelocity * projectileSpeedModifier, xCoordinate, yCoordinate);		
				enemyManager.deployBullet(bullet);	
				attackAvailable = false;
				directAttackState = true;
				break;
			}
			
		} else {
			
			for (int i = 0; i < bulletList.size(); i++) {
				InterfaceEnemyBullet bullet = bulletList.get(i);
				if (bullet.isDeployed() == true) { continue; }
				bullet.setBaseVelocity(bulletBaseVelocity * projectileSpeedModifier);
				bullet.fire(playerShip.getXCoordinate(), playerShip.getYCoordinate(), xCoordinate, yCoordinate);		
				enemyManager.deployBullet(bullet);	
				attackAvailable = false;
				directAttackState = false;
				increaseNextAttackTime();
				break;
			}	
			
		}
	}
	
	public void move() {
		super.move();
		if (hasEnteredWindow == false) { return; }
		
		if (bouncesRemaining > 0) {
			
			if (yVelocity < 0 && (yCoordinate < height() * 0.2)) {
				yCoordinate = height() * 0.2;
				yVelocity *= -1;
				bouncesRemaining--;
			} else if (yVelocity > 0 && (yCoordinate > height() * 0.4) ) {
				yCoordinate = height() * 0.4;
				yVelocity *= -1;
				bouncesRemaining--;
			}
		}

	}
	
	protected void init() {
		name = EnemyType.Ligher.name();
		skin = name;
		
		originalAttackTime = 0.25;
		attackTime = originalAttackTime;
		attackTimeIncrease = 1.5;

		baseXVelocity = 0;
		baseYVelocity = 100;
		bulletBaseVelocity = 200;

		baseSquadSize = 6;
		
		pointValue = 25;
		
		totalShieldAmount = 150;
		
		width = width() * 0.1;
		height = height() * 0.1;
				
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
									
			if (lastXCoordinate > width() / 2) {
				nextLaunchedShip.setYCoordinate(lastYCoordinate - lastHeight);
				nextLaunchedShip.setXCoordinate(width() - lastXCoordinate - lastWidth * 2);
			} else {
				nextLaunchedShip.setYCoordinate(lastYCoordinate);
				nextLaunchedShip.setXCoordinate(width() - lastXCoordinate);
			}
		}
	}
	
	protected void setInitialLaunchCoordinates() {
		isSquadLeader = true;
		xCoordinate = width() * 0.45;
		yCoordinate = -height() * 0.1;
	}
}