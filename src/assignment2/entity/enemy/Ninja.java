package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class Ninja extends AbstractEnemyShip {
	
	/***Properties***/
	
	protected boolean dropDownState, sideMovementState;
	
	/***Constructor***/
	
	public Ninja(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	/***Getters and Setters***/
			
	/***Methods***/
		
	public void reset() {
		super.reset();

		if (squadSize % 2 != 0) { squadSize--; }
		if (squadSize > 6) { squadSize = 6; }

		dropDownState = true;
		sideMovementState = true;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyBullet> bulletList) {
		super.update(dt, playerShip, bulletList);
		if (attackAvailable == false) { return; }
		
		ArrayList<InterfaceEnemyBullet> firedBullets = new ArrayList<InterfaceEnemyBullet>();
		
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceEnemyBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			
			firedBullets.add(bullet);
			if (firedBullets.size() >= 4) { break; }
		}
		
		int firedBulletSize = firedBullets.size();
		if (firedBulletSize < 4) { return; }
		
		attackAvailable = false;
		increaseNextAttackTime();
		firedBullets.get(0).fireAtVelocity(-bulletBaseVelocity * projectileSpeedModifier, 0, xCoordinate, yCoordinate);		
		firedBullets.get(1).fireAtVelocity(bulletBaseVelocity * projectileSpeedModifier, 0, xCoordinate, yCoordinate);		
		firedBullets.get(2).fireAtVelocity(0, -bulletBaseVelocity * projectileSpeedModifier, xCoordinate, yCoordinate);		
		firedBullets.get(3).fireAtVelocity(0, bulletBaseVelocity * projectileSpeedModifier, xCoordinate, yCoordinate);		
		
		for (int i = 0; i < firedBulletSize; i++ ) {
			InterfaceEnemyBullet bullet = firedBullets.get(i);
			enemyManager.deployBullet(bullet);
		}
	}
	
	public void launch(double dt) {
		isDeployed = true;
	}
	
	public void move() {
		super.move();
		if (hasEnteredWindow == false) { return; }
		
		if (dropDownState == true) {
			dropDownState();
		} else if (sideMovementState == true){
			sideMovementState();
		}
	}
	
	protected void init() {
		name = EnemyType.Ninja.name();
		skin = name;
		
		originalAttackTime = 1;
		attackTime = originalAttackTime;
		attackTimeIncrease = 1;
		
		baseXVelocity = 300;
		baseYVelocity = 100;
		bulletBaseVelocity = 200;
		
		baseSquadSize = 6;
		
		pointValue = 25;
		
		totalShieldAmount = 100;
		
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
			nextLaunchedShip.setXVelocity( lastLaunchedShip.getXVelocity() * -1 );
			
			if (lastXCoordinate > width() / 2) {
				nextLaunchedShip.setXCoordinate(width() - lastXCoordinate - lastWidth * 2);
			} else {
				nextLaunchedShip.setXCoordinate(width() - lastXCoordinate);
			}
		}
	}
	
	protected void setInitialLaunchCoordinates() {
		isSquadLeader = true;
		xCoordinate = -width() * 0.15;
		xVelocity = baseXVelocity;
		yVelocity = 0;
		yCoordinate = height() * 0.1;
	}
	
	protected void dropDownState() {
		if (xCoordinate < width() * 0.15 && (xVelocity < 0) ) {
			xCoordinate = width() * 0.15;
			xVelocity = 0;
			yVelocity = baseYVelocity;
			dropDownState = false;
		} else if (xCoordinate > width() * 0.85 && (xVelocity > 0) ) {
			xCoordinate = width() * 0.85;
			xVelocity = 0;
			yVelocity = baseYVelocity;
			dropDownState = false;
		}	
	}
	
	protected void sideMovementState() {
		if (yCoordinate > height() / 2) {
			yCoordinate = height() / 2;
			yVelocity = 0;
			dropDownState = true;
			sideMovementState = false;
			
			if (xCoordinate > width() / 2) {
				xVelocity = -baseXVelocity;
			} else {
				xVelocity = baseXVelocity;
			}
		} 	
	}
}