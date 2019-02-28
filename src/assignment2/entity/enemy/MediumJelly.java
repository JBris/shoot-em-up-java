package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class MediumJelly extends AbstractEnemyShip {
	
	/***Properties***/
	
	protected int bouncesLeft;
	protected boolean dropState;
	
	/***Constructor***/
	
	public MediumJelly(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	/***Getters and Setters***/
			
	/***Methods***/
		
	public void reset() {
		super.reset();
		squadSize = 2; 
		
		dropState = false;
		bouncesLeft  = 3;
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
	
	public void move() {
		super.move();
		if (hasEnteredWindow == false) { return; }
		
		if (dropState == false) {
			if (xVelocity < 0 && (xCoordinate < width() * 0.15)) {
				xCoordinate = width() * 0.15;
				bounce();
			} else if (xVelocity > 0 && (xCoordinate > width() * 0.85)) {
				xCoordinate = width() * 0.85;
				bounce();
			}
		}
	}
	
	public void launch(double dt) {
		isDeployed = true;
	}
	
	protected void init() {
		name = EnemyType.MediumJelly.name();
		skin = name;
		
		originalAttackTime = 0.25;
		attackTime = originalAttackTime;
		attackTimeIncrease = 0.75;
		
		baseXVelocity = 50;
		baseYVelocity = 40;
		projectileSpeedModifier = 1;
		bulletBaseVelocity = 350;
		
		baseSquadSize = 2;
		
		pointValue = 25;
		
		totalShieldAmount = 250;
		
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

			nextLaunchedShip.setXCoordinate(width() * 1.15);
			nextLaunchedShip.setYCoordinate(height() * 0.15);
			nextLaunchedShip.setXVelocity( lastLaunchedShip.getXVelocity() * -1 );
		}
	}
	
	protected void setInitialLaunchCoordinates() {
		isSquadLeader = true;
		xCoordinate = -width() * 0.15;
		yCoordinate = height() * 0.15;
		xVelocity = baseXVelocity;
		yVelocity = 0;
	}
	
	protected void bounce() {
		if (bouncesLeft <= 0) {
			xVelocity = 0;
			yVelocity = baseYVelocity;
			dropState = true;
		} else {
			xVelocity *= -1;
			bouncesLeft --; 
		}
	}
	
}