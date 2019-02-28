package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class SmallJelly extends AbstractEnemyShip {
	
	/***Properties***/
	
	protected boolean advancedMovementState, verticalState;
	
	/***Constructor***/
	
	public SmallJelly(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	/***Getters and Setters***/
			
	/***Methods***/
		
	public void reset() {
		super.reset();
		verticalState = true;
		advancedMovementState = true;
		
		if (squadSize % 2 != 0) { squadSize--; }
		if (squadSize > 6) { squadSize = 6; }
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyBullet> bulletList) {
		super.update(dt, playerShip, bulletList);
		if (attackAvailable == false) { return; }
		
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceEnemyBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			bullet.fireAtVelocity(0, bulletBaseVelocity * projectileSpeedModifier, xCoordinate, yCoordinate);		
			enemyManager.deployBullet(bullet);	
			attackAvailable = false;
			increaseNextAttackTime();
			break;
		}
	}
	
	public void move() {
		super.move();
		if (hasEnteredWindow == false) { return; }
		
		if (advancedMovementState == true) {
			if (verticalState == true) {
				verticalState();
			} else {
				horizontalState();
			}	
		}

	}
	
	protected void init() {
		name = EnemyType.SmallJelly.name();
		skin = name;
		
		originalAttackTime = 0.25;
		attackTime = originalAttackTime;
		attackTimeIncrease = 0.75;
		
		baseXVelocity = 50;
		baseYVelocity = 40;
		projectileSpeedModifier = 1;
		bulletBaseVelocity = 350;
		
		baseSquadSize = 4;
		
		pointValue = 5;
		
		totalShieldAmount = 70;
		
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
		yCoordinate = -height() * 0.2;
	}
	
	protected void verticalState() {
		if (yCoordinate > height() * 0.2) {
			yCoordinate = height() * 0.2;
			yVelocity = 0;
			xVelocity /= 2;
			verticalState = false; 
		}
	}
	
	protected void horizontalState() {
		if (xCoordinate < width() * 0.15 && (xVelocity < 0) ) {
			xCoordinate = width() * 0.15;
			xVelocity = 0;
			yVelocity = baseYVelocity;
			advancedMovementState = false;
		} else if (xCoordinate > width() * 0.85 && (xVelocity > 0) ) {
			xCoordinate = width() * 0.85;
			xVelocity = 0;
			yVelocity = baseYVelocity;
			advancedMovementState = false;
		}
	}
	
}