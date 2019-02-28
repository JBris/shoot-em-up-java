package src.assignment2.entity.boss;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class Shangdi extends AbstractBoss {
	
	/***Properties***/
	
	protected boolean randomState, chaseState, mirrorState, sideState, mercyState;
	protected int numOfShots, numOfShotsCap, numOfShotsHardCap;

	protected double movementTimer, movementTime;
	protected int movementTimeCap, numOfMovements, numOfMovementsCap, numOfMovementsHardCap;
	
	/***Constructor***/
	
	public Shangdi(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, explosionManager);			
	}
	
	/***Getters and Setters***/
		
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed) { 		
		if (projectileSpeed == ProjectileSpeed.Slow) {
			projectileSpeedModifier = 0.5;
		} else if (projectileSpeed == ProjectileSpeed.Fast) {
			projectileSpeedModifier = 1.5;
		} else {
			projectileSpeedModifier = 1;	
		}
	}
	
	public void setAttackRate(EnemyAttackRate enemyAttackRate) { 
		if (enemyAttackRate == EnemyAttackRate.Slow) {
			attackTime = 1.5;
		} else if (enemyAttackRate == EnemyAttackRate.Fast) {
			attackTime = 0.75;
		} else {
			attackTime = 1;	
		}	
	}
	
	/***Methods***/
	
	public void reset() {
		super.reset();
		
		randomState = true;
		chaseState = false;
		mirrorState = false;
		sideState = false;
		mercyState = false;
			
		numOfShots = 0;
		numOfShotsCap = 10;
		numOfShotsHardCap = numOfShotsCap * 2;
		
		movementTimer = 0;
		movementTime = 1.5;
		movementTimeCap = 1;
		
		numOfMovements = 0;
		numOfMovementsCap = 8;
		numOfMovementsHardCap = numOfMovementsCap / 2;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		super.update(dt, playerShip, bulletList, bossManager);
		if (isDead == true || invulnerable == true) { return; }
		
		if (randomState == true) {
			randomState(dt, playerShip, bulletList, bossManager);
		} else if (chaseState == true) {
			chaseState(dt, playerShip, bulletList, bossManager);		
		} else if (mirrorState == true) {
			mirrorState(dt, playerShip, bulletList, bossManager);					
		} else if (sideState == true) {
			sideState(dt, playerShip, bulletList, bossManager);								
		}
	}
	
	public void move() {
		
		if (sideState == true) {
			if  (xVelocity > 0 && (xCoordinate > width() * 0.9)) { xVelocity = 0; }
			else if  (xVelocity < 0 && (xCoordinate < width() * 0.1)) { xVelocity = 0; }
			
			if  (yVelocity > 0 && (yCoordinate > height() / 2)) { yVelocity = 0; }
			else if  (yVelocity < 0 && (yCoordinate < height() / 2)) { yVelocity = 0; }
			return;		
		}
		
		if (yVelocity < 0 && (yCoordinate < height() * 0.15)) {
				yCoordinate = height() * 0.15;
				yVelocity *= -1;
		} else if (yVelocity > 0 && (yCoordinate > height() * 0.85)) {
				yCoordinate = height() * 0.85;
				yVelocity *= -1;
		} else if (xVelocity < 0 && (xCoordinate < width() * 0.15)) {
				xCoordinate = width() * 0.15;
				xVelocity *= -1;
		} else if (xVelocity > 0 && (xCoordinate > width() * 0.85)) {
				xCoordinate = width() * 0.85;
				xVelocity *= -1;
		}

	}
	
	protected void randomState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) { 
		if (attackAvailable == false) {

			movementTimer += dt;
			if (movementTimer <= movementTime * attackTime) { return; }
			movementTimer -= movementTime;
		
			Random random = new Random();
			double x = width() * (0.05 + ( random.nextDouble() * (0.95 - 0.05) ));
			double y = height() * (0.05 + ( random.nextDouble() * (0.95 - 0.05) ));
		
			xVelocity = x - xCoordinate;
			yVelocity = y  - yCoordinate;

			double l = length(xVelocity, yVelocity);
			xVelocity = xVelocity * baseXVelocity / l;
			yVelocity = yVelocity * baseYVelocity / l;
			
			attackAvailable = true;
		
			numOfMovements++;
			
			if (numOfMovements < numOfMovementsCap) { return; }
			numOfMovements = 0;
			movementTimer = 0;
			randomState = false;
			chaseState =  true;
			
		} else {
			consecutiveShots(dt, playerShip, bulletList, bossManager);			
		}
	}
	
	protected void chaseState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
			if (mercyState == true) {
				if (playerShip.getMercyState() == false) { mercyState = false; }
				return;
			}
		
			xVelocity = playerShip.getXCoordinate() - xCoordinate;
			yVelocity = playerShip.getYCoordinate() - yCoordinate;

			double l = length(xVelocity, yVelocity);
			xVelocity = xVelocity * baseXVelocity / l * 1.2 * attackTime;
			yVelocity = yVelocity * baseYVelocity / l * 1.2 * attackTime;		
			
			if (playerShip.getMercyState() == true) { 
				mercyState = true;
				stop(); 
			}	
			
			if (attackAvailable == true) {consecutiveShots(dt, playerShip, bulletList, bossManager); }	

			attackTimer += dt;
			if (attackTimer < movementTime * attackTime) { return; }
			attackTimer -= movementTime;
			attackAvailable = true;

			numOfMovements++;
			
			if (numOfMovements < numOfMovementsCap) { return; }
			numOfMovements = 0;
			movementTimer = 0;		
			attackTimer -= movementTime;
			chaseState =  false;			
			mirrorState = true;
	}
	
	protected void mirrorState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (mercyState == true) {
			if (playerShip.getMercyState() == false) { mercyState = false; }
			return;
		}
		
		xVelocity = -playerShip.getXVelocity();
		yVelocity = -playerShip.getYVelocity();
			
		if (playerShip.getMercyState() == true) { 
			mercyState = true;
			stop(); 
		}	
			
		if (attackAvailable == true) {consecutiveShots(dt, playerShip, bulletList, bossManager); }	

		attackTimer += dt;
		if (attackTimer < movementTime * attackTime) { return; }
		attackTimer -= movementTime;
		attackAvailable = true;

		numOfMovements++;
			
		if (numOfMovements < numOfMovementsCap) { return; }
		numOfMovements = 0;
		movementTimer = 0;		
		attackTimer -= movementTime;
		mirrorState =  false;			
		sideState = true;
		
		double x = width() * 0.9;
		if (Math.random() < 0.5) {
			x = width() * 0.1;
		} 
		
		xVelocity = x - xCoordinate;
		yVelocity = height() / 2  - yCoordinate;
		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseXVelocity / l;
		yVelocity = yVelocity * baseYVelocity / l;
	}
	
	
	protected void sideState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (mercyState == true) {
			if (playerShip.getMercyState() == false) { mercyState = false; }
			return;
		}
		
		if (playerShip.getMercyState() == true) { 
			mercyState = true;
			stop(); 
		}	
			
		if (attackAvailable == true) {consecutiveShots(dt, playerShip, bulletList, bossManager); }	

		attackTimer += dt;
		if (attackTimer < movementTime * attackTime) { return; }
		attackTimer -= movementTime;
		attackAvailable = true;

		numOfMovements++;
			
		if (numOfMovements < numOfMovementsCap) { return; }
		
		attackTimer = 0;
		attackAvailable = false;
		numOfMovements = 0;
		movementTimer = -0.5;		
		
		if (numOfShotsCap <numOfShotsHardCap) { numOfShotsCap++; }
		if (numOfMovementsCap > numOfMovementsHardCap) { numOfMovementsCap--; }
		if (movementTime > movementTimeCap) { movementTime -= 0.1; }
		
		sideState =  false;			
		randomState = true;
	}
	
	
	protected void consecutiveShots(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		Random rand = new Random();
		
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			
			numOfShots++;
			bullet.setBaseVelocity(bulletBaseVelocity * projectileSpeedModifier);
			bossManager.deployBullet(bullet);	

			double randomTarget = rand.nextDouble();
			if (randomTarget < 0.3) {
				bullet.fire(playerShip.getXCoordinate() - playerShip.getWidth() * 4, playerShip.getYCoordinate(), xCoordinate, yCoordinate);		
			} else if (randomTarget < 0.6) {
				bullet.fire(playerShip.getXCoordinate() + playerShip.getWidth() * 4, playerShip.getYCoordinate(), xCoordinate, yCoordinate);			
			} else {
				bullet.fire(playerShip.getXCoordinate(), playerShip.getYCoordinate(), xCoordinate, yCoordinate);			
			}
			
			break;
		}
		
		if (numOfShots >= numOfShotsCap) { 
			numOfShots = 0;
			attackAvailable = false; 
		}
	}
	
	protected void init() {
		name = StageBoss.Shangdi.name();
		skin = name;
		
		originalAttackTime = 0.5;
		attackTime = originalAttackTime;
		
		baseXVelocity = 550;
		baseYVelocity = 550;
		projectileSpeedModifier = 1;
		bulletBaseVelocity = 600;
				
		pointValue = 500;
		
		totalShieldAmount = 5000;
		
		width = width() * 0.3;
		height = height() * 0.3;
				
		reset();
	}
	
	protected int setBossShieldAmount() {
		return totalShieldAmount + stage * 400 + gameLoop * 1000;
	}
}