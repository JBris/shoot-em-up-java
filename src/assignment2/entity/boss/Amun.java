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

public class Amun extends AbstractBoss {
	
	/***Properties***/
		
	protected double attackRate, bounceTimer, bounceTime, friedTimer;
	protected int bounceCount, bounceCountCap;
	protected int bulletsPerShot, bulletsPerShotCap, bulletsPerShotHardCap;
	protected int numOfCentreBullets, numOfCentreBulletsCap, numOfCentreBulletsHardCap, systemFriedDuration;
	protected int numOfCentreShots, numOfCentreShotsCap, numOfCentreShotsHardCap;
	protected boolean bounceState, goingUp, bottomState, sideState, centreState, restState, systemFried;
	
	/***Constructor***/
	
	public Amun(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, explosionManager);			
	}
	
	/***Getters and Setters***/
		
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed) { 		
		if (projectileSpeed == ProjectileSpeed.Slow) {
			projectileSpeedModifier = 0.6;
		} else if (projectileSpeed == ProjectileSpeed.Fast) {
			projectileSpeedModifier = 1.4;
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
		
		attackRate = 0.35;
		
		bounceCount = 0;
		bounceCountCap = 6;

		bulletsPerShot = 0;
		bulletsPerShotCap = 9;
		bulletsPerShotHardCap = bulletsPerShotCap * 2;
		
		numOfCentreBullets = 0;
		numOfCentreBulletsCap = 10;
		numOfCentreBulletsHardCap = numOfCentreBulletsCap * 2;
		
		numOfCentreShots = 0;
		numOfCentreShotsCap = 2;
		numOfCentreShotsHardCap = numOfCentreShotsCap * 2;
		
		systemFried = false;
		systemFriedDuration = 2;
		friedTimer = 0;
		
		bounceState = true;
		goingUp = false;
		bottomState = false;
		sideState = false;
		centreState = false;
		restState = false;
		
		attackAvailable = true;
		
		bounceTimer = 0;
		bounceTime = 2;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		super.update(dt, playerShip, bulletList, bossManager);
		if (isDead == true || invulnerable == true) { return; }
		
		if (bounceState == true) {
			bounceState(dt, playerShip, bulletList, bossManager);
		} else if (bottomState == true) {
			bottomState(dt, playerShip, bulletList, bossManager);		
		} else if (sideState == true) {
			sideState(dt, playerShip, bulletList, bossManager);		
		} else if (centreState == true) {
			centreState(dt, playerShip, bulletList, bossManager);		
		} else if (restState == true) {
			restState(dt, playerShip, bulletList, bossManager);				
		}
	}
	
	public void move() {
		if (bounceState == true) {
			
			if (yVelocity > 0 && (yCoordinate > height() * 0.9)) {
				yCoordinate = height() * 0.9;
				yVelocity = bounce(yVelocity);
			} else if (yVelocity < 0 && (yCoordinate < height() * 0.1)) {
				yCoordinate = height() * 0.1;
				yVelocity = bounce(yVelocity);
			}
		
			if (xVelocity > 0 && (xCoordinate > width() * 0.9)) {
				xCoordinate = width() * 0.9;
				xVelocity = bounce(xVelocity);
			} else if (xVelocity < 0 && (xCoordinate < width() * 0.1)) {
				xCoordinate = height() * 0.1;
				xVelocity = bounce(xVelocity);
			}
		} else if (bottomState == true) {
			if (goingUp == false && (yCoordinate > height() * 0.7)) {
				xVelocity = 0;
				yVelocity = -baseYVelocity;
				attackAvailable = true;
			} else if (goingUp == true && (yCoordinate < height() * 0.2)) {
				stop();
				attackAvailable = true; 
			}
			
			if (xVelocity > 0 && (xCoordinate > width() * 0.9)) {
				xVelocity *= -1;
			} else if (xVelocity < 0 && (xCoordinate < width() * 0.1)) {
				xVelocity *= -1;
			}
		} else if (centreState == true) {  
			if  (xVelocity > 0 && (xCoordinate > width() * 0.4)) { stop(); }
			else if  (xVelocity < 0 && (xCoordinate < width() * 0.6)) { stop(); }
		} else if (restState == true) {
				if (yVelocity > 0 && (yCoordinate > height() * 0.85)) {
				yCoordinate = height() * 0.85;
				yVelocity *= -0.2;
			} 
		}
	}
	
	protected double bounce (double velocity) {
		attackAvailable = true;
		bounceCount++;
		return velocity * -0.2;
	}
	
	public void manipulateBullet(double dt, InterfacePlayerShip playerShip, InterfaceBossBullet bullet) {
		if (bullet.getSpecialAttack() == false) { return; }
		
		double bulletXCoordinate = bullet.getXCoordinate();
		double bulletYCoordinate = bullet.getYCoordinate();
		double bulletXVelocity = bullet.getXVelocity();
		double bulletYVelocity = bullet.getYVelocity();
		double bulletWidth = bullet.getWidth();
		double bulletHeight = bullet.getHeight();

		if (bulletXVelocity > 0 && (bulletXCoordinate > width() - bulletWidth / 2)) {
			reverseBullet(bullet, bulletXVelocity, bulletYVelocity);	
		} else if (bulletXVelocity < 0 && (bulletXCoordinate < bulletWidth / 2)) { 
			reverseBullet(bullet, bulletXVelocity, bulletYVelocity);		
		}
			
		if (bulletYVelocity > 0 && (bulletYCoordinate > width() - bulletHeight / 2)) {
			reverseBullet(bullet, bulletXVelocity, bulletYVelocity);	
		} else if (bulletYVelocity < 0 && (bulletYCoordinate < bulletHeight / 2)) { 
			reverseBullet(bullet, bulletXVelocity, bulletYVelocity);		
		}
		
	}
	
	protected void reverseBullet(InterfaceBossBullet bullet, double bulletXVelocity, double bulletYVelocity) {
		bullet.setXVelocity(-bulletXVelocity);
		bullet.setYVelocity(-bulletYVelocity);
		bullet.setSpecialAttack(false);
	}
	
	protected void bounceState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		
		if (bounceCount > bounceCountCap) {
			bounceCount = 0;
			bounceTimer = 0;
			
			goingUp = false;
			bounceState= false;
			bottomState = true;
			attackAvailable = false;
			
			stop();
			
			xVelocity = width() / 2 - xCoordinate;
			yVelocity = height() * 0.8 - yCoordinate;
			double l = length(xVelocity, yVelocity);
			xVelocity = xVelocity * baseXVelocity / l;
			yVelocity = yVelocity * baseYVelocity / l;
			
		} else {
			bounceTimer += dt;
			if (bounceTimer < bounceTime) { return; }
			bounceTimer -= bounceTime;
		
			if (goingUp == false) {
				goingUp = true;
				
				xVelocity = playerShip.getXCoordinate() - xCoordinate;
				yVelocity = playerShip.getYCoordinate()  - yCoordinate;

				double l = length(xVelocity, yVelocity);
				xVelocity = xVelocity * baseXVelocity / l * 3;
				yVelocity = yVelocity * baseYVelocity / l * 3;
				
				if (attackAvailable == true) { 
					multiShotDown(dt, playerShip, bulletList, bossManager); 
					attackAvailable = false;
				}
				
			} else {
				goingUp = false;
				
				xVelocity = playerShip.getXCoordinate() - xCoordinate;
				yVelocity = playerShip.getYCoordinate()  - yCoordinate;

				double l = length(xVelocity, yVelocity);
				xVelocity = xVelocity * baseXVelocity / l * 3;
				yVelocity = -baseYVelocity;
				
				if (attackAvailable == true) {
					multiShotAtPlayer(dt, playerShip, bulletList, bossManager); 
					attackAvailable = false;
				}
			}
		}		
	}
	
	protected void bottomState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (goingUp == false) {
			if (attackAvailable == true) {
				multiShotDown(dt, playerShip, bulletList, bossManager); 				
				attackAvailable = false;
				goingUp = true;
			}
		} else {
			if (attackAvailable == false) { return; }	
			
			multiShotAtPlayer(dt, playerShip, bulletList, bossManager, false); 
			attackAvailable = false;
			bottomState = false;		
			sideState = true;	
			goingUp = false;
			
			if (xCoordinate < width() / 2) {
				xVelocity = baseXVelocity;
			} else {
				xVelocity = -baseXVelocity;
			}
		}		
	}
	
	protected void sideState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (xVelocity > 0 && (xCoordinate < width() * 0.85)) { return; }
		if (xVelocity < 0 && (xCoordinate > width() * 0.15)) { return; }
		
		sideState = false;
		centreState = true;
		attackTimer = 0;
		multiShotDown(dt, playerShip, bulletList, bossManager); 			
		
		xVelocity = width() / 2 - xCoordinate;
		yVelocity = height() / 2 - yCoordinate;
		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseXVelocity / l;
		yVelocity = yVelocity * baseYVelocity / l;
		
	}
	
	protected void centreState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (systemFried == false) {
			attackTimer += dt;
			if (attackTimer < attackRate * attackTime) { return; }
			attackTimer -= attackRate * attackTime;
			
			for (int i = 0; i < bulletList.size(); i++) {
				InterfaceBossBullet bullet = bulletList.get(i);
				if (bullet.isDeployed() == true) { continue; }
				numOfCentreBullets++;	
				bullet.setBaseVelocity(bulletBaseVelocity * projectileSpeedModifier * 2);
				bossManager.deployBullet(bullet);	
				bullet.fire(playerShip.getXCoordinate(), playerShip.getYCoordinate(), xCoordinate, yCoordinate);			
				break;
			}
		
			if (numOfCentreBullets > numOfCentreBulletsCap) {
				numOfCentreShots++;
				attackTimer -= attackRate * attackTime * 5;
				numOfCentreBullets = 0;
			}	

			if (numOfCentreShots > numOfCentreShotsCap) {
				systemFried = true;
				numOfCentreShots = 0;
				attackTimer = 0;	
			}
		}  else {
			friedTimer += dt;
			if (friedTimer < systemFriedDuration) { return; }
			friedTimer = 0;
			systemFried = false;
			centreState = false;
			restState = true;
			
			xVelocity = baseXVelocity;
			yVelocity = baseYVelocity;
			
			double l = length(xVelocity, yVelocity);
			xVelocity = xVelocity * baseXVelocity / l;
			yVelocity = yVelocity * baseYVelocity / l;
		}
	}
	
	
	protected void restState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (xVelocity > 0 && (xCoordinate < width() * 0.85)) { return; }
		if  (xVelocity < 0 && (xCoordinate > width() * 0.15)) { return; }
			
		stop();
		attackTimer = 0;
		attackAvailable = true;
		restState = false;
		bounceState = true;
		if (bulletsPerShotCap < bulletsPerShotHardCap) { bulletsPerShotCap++; }
		if (numOfCentreBulletsCap < numOfCentreBulletsHardCap) { numOfCentreBulletsCap++; }
		if (numOfCentreShotsCap < numOfCentreShotsHardCap) { numOfCentreShotsCap++; }
	}
	
	protected void multiShotDown(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		multiShotDown(dt, playerShip, bulletList, bossManager, true); 
	}
	
	protected void multiShotDown(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager, boolean bounce) {
		int numOfBullets = bulletsPerShotCap;
		if (numOfBullets % 2 == 0) { numOfBullets--; }
		
		ArrayList<InterfaceBossBullet> firedBullets = new ArrayList<InterfaceBossBullet>();

		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			firedBullets.add(bullet);
			if (firedBullets.size() >= numOfBullets) { break; }
		}
		
		int firedBulletSize = firedBullets.size();
		if (firedBulletSize < numOfBullets) { return; }
		
		double playerWidth = playerShip.getWidth();

		double spreadUnit = playerWidth * 4;
		double spread = spreadUnit * 4;
			
		for (int i = 0; i < firedBulletSize; i++ ) {
			InterfaceBossBullet bullet = firedBullets.get(i);
			bullet.fireAtVelocity(spread, bulletBaseVelocity * projectileSpeedModifier, xCoordinate, yCoordinate);
			if (bounce == true) { bullet.setSpecialAttack(true); }
			spread -= spreadUnit;
			bossManager.deployBullet(bullet);
		}
	}
	
	protected void multiShotAtPlayer(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		multiShotAtPlayer(dt, playerShip, bulletList, bossManager, true); 
	}
	
	protected void multiShotAtPlayer(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager, boolean bounce) {
		int numOfBullets = bulletsPerShotCap;
		if (numOfBullets % 2 == 0) { numOfBullets--; }
		
		ArrayList<InterfaceBossBullet> firedBullets = new ArrayList<InterfaceBossBullet>();

		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			firedBullets.add(bullet);
			if (firedBullets.size() >= numOfBullets) { break; }
		}
		
		int firedBulletSize = firedBullets.size();
		if (firedBulletSize < numOfBullets) { return; }
		
		double playerXCoordinate = playerShip.getXCoordinate();
		double playerYCoordinate = playerShip.getYCoordinate();
		double playerWidth = playerShip.getWidth();

		double spreadUnit = playerWidth * 4;
		double spread = spreadUnit * 4;
			
		for (int i = 0; i < firedBulletSize; i++ ) {
			InterfaceBossBullet bullet = firedBullets.get(i);
			bullet.setBaseVelocity(bulletBaseVelocity * projectileSpeedModifier);
			bullet.fire(playerXCoordinate + spread, playerYCoordinate, xCoordinate, yCoordinate);	
			spread -= spreadUnit;
			if (bounce == true) { bullet.setSpecialAttack(true); }
			bossManager.deployBullet(bullet);
		}
	}
	
	protected void init() {
		name = StageBoss.Amun.name();
		skin = name;
		
		originalAttackTime = 0.5;
		attackTime = originalAttackTime;
		
		baseXVelocity = 300;
		baseYVelocity = 300;
		projectileSpeedModifier = 1;
		bulletBaseVelocity = 350;
				
		pointValue = 500;
		
		totalShieldAmount = 8000;
		
		width = width() * 0.3;
		height = height() * 0.3;
				
		reset();
	}
	
	protected int setBossShieldAmount() {
		return totalShieldAmount + stage * 400 + gameLoop * 500;
	}
}