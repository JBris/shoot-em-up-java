package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class Lightning extends AbstractEnemyShip {
	
	/***Properties***/
		
	protected boolean inPosition, movedDown, triAttack;
	protected double movementTimer;
	protected int movementTime;
	
	/***Constructor***/
	
	public Lightning(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	/***Getters and Setters***/
			
	/***Methods***/
		
	public void reset() {
		super.reset();
		squadSize = 2;
		inPosition = false;
		triAttack = true;
		movedDown = false;
		movementTimer = 0;
		movementTime = 15;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyBullet> bulletList) {
		super.update(dt, playerShip, bulletList);
		if (attackAvailable == false) { return; }
		
		if (triAttack == true) {
			triAttack(playerShip, bulletList);
		} else {
		
			for (int i = 0; i < bulletList.size(); i++) {
				InterfaceEnemyBullet bullet = bulletList.get(i);
				if (bullet.isDeployed() == true) { continue; }
				bullet.setBaseVelocity(bulletBaseVelocity * projectileSpeedModifier);
				bullet.fire(playerShip.getXCoordinate(), playerShip.getYCoordinate(), xCoordinate, yCoordinate);		
				enemyManager.deployBullet(bullet);	
				attackAvailable = false;
				triAttack = true;
				increaseNextAttackTime();
				break;
			}
			
		}
	}
	
	public void update(double dt) {
		super.update(dt);
		
		if (inPosition == true && (movedDown == false) ) {
			movementTimer += dt;
			if (movementTimer > movementTime) {
				yVelocity = baseYVelocity;
				movedDown = true;
			}
		}

	}
	
	public void move() {
		super.move();
		
		if (hasEnteredWindow == false) { return; }
		
		if (inPosition == false) {
			if (yVelocity > 0 && (yCoordinate > height() * 0.15)) {
				yCoordinate =  height() * 0.15;
				yVelocity = 0;
				inPosition = true;
			} else if (xVelocity > 0 && (xCoordinate > width() * 0.85)) {
				xCoordinate =  width() * 0.85;
				xVelocity = 0;
				inPosition = true;
			} else if (xVelocity < 0 && (xCoordinate < width() * 0.15)) {
				xCoordinate = width() * 0.15;
				xVelocity = 0;
				inPosition = true; 
			}
		}

	}
	
	protected void init() {
		name = EnemyType.Lightning.name();
		skin = name;
		
		originalAttackTime = 1;
		attackTime = originalAttackTime;
		attackTimeIncrease = 1.5;
		
		baseXVelocity = 80;
		baseYVelocity = 80;
		bulletBaseVelocity = 400;
		
		baseSquadSize = 2;
		
		pointValue = 50;
		
		totalShieldAmount = 550;
		
		width = width() * 0.12;
		height = height() * 0.12;
				
		baseNextDeploymentTime = 3;
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

			if (lastXCoordinate < 0) {
				nextLaunchedShip.setXCoordinate(width() * 1.15);
				nextLaunchedShip.setYCoordinate(height() * 0.15);
				nextLaunchedShip.setXVelocity( lastLaunchedShip.getXVelocity() * -1 );
			} else {
				nextLaunchedShip.setXCoordinate(width() * 0.85);
				nextLaunchedShip.setYCoordinate(-height() * 0.15);
				nextLaunchedShip.setYVelocity(baseYVelocity);
			}
		}
	}
	
	public void launch(double dt) {
		isDeployed = true;
	}
	
	protected void setInitialLaunchCoordinates() {
		isSquadLeader = true;
		
		Random random = new Random();
		double randomVal = random.nextDouble();
		if (randomVal < 0.5) {
			xCoordinate = width() * 0.15;
			yCoordinate = -height() * 0.15;
			xVelocity = 0;
			yVelocity = baseYVelocity;
		} else {
			xCoordinate = -width() * 0.15;
			yCoordinate = height() * 0.15;
			xVelocity = baseXVelocity;
			yVelocity = 0;
		}
	}
	
	protected void triAttack(InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyBullet> bulletList) {
		ArrayList<InterfaceEnemyBullet> firedBullets = new ArrayList<InterfaceEnemyBullet>();
		
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceEnemyBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			
			firedBullets.add(bullet);
			if (firedBullets.size() >= 3) { break; }
		}
		
		int firedBulletSize = firedBullets.size();
		if (firedBulletSize < 3) { return; }
		
		attackAvailable = false;
		triAttack = false;
		attackTime -= 1 * attackTimeIncreaseModifier;
		
		double playerXCoordinate = playerShip.getXCoordinate();
		double playerYCoordinate = playerShip.getYCoordinate();
		double playerWidth = playerShip.getWidth();

		firedBullets.get(0).fire(playerXCoordinate, playerYCoordinate, xCoordinate, yCoordinate);		
		firedBullets.get(1).fire(playerXCoordinate - playerWidth * 2, playerYCoordinate, xCoordinate, yCoordinate);		
		firedBullets.get(2).fire(playerXCoordinate + playerWidth * 2, playerYCoordinate, xCoordinate, yCoordinate);		
		
		for (int i = 0; i < firedBulletSize; i++ ) {
			InterfaceEnemyBullet bullet = firedBullets.get(i);
			enemyManager.deployBullet(bullet);
		}	
	}
}