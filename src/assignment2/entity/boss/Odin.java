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

public class Odin extends AbstractBoss {
	
	/***Properties***/
		
	protected int numOfBounces, numOfBouncesCap, numOfBouncesHardCap;
	protected int numOfShots, numOfShotsCap, numOfShotsHardCap;
	protected int actionTime, stateTransitionTime;
	protected double actionTimer, stateTransitionTimer;
	protected boolean bounceState, chaseState, restState, increaseShots;
	
	/***Constructor***/
	
	public Odin(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
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
		
		numOfBounces = 0;
		numOfBouncesCap = 5;
		numOfBouncesHardCap = numOfBouncesCap * 2;
		
		attackAvailable  = false;
		numOfShots = 1;
		numOfShotsCap = 10;
		numOfShotsHardCap = numOfShotsCap * 2;
		
		increaseShots = true;
		
		actionTimer = 0;
		actionTime = 1;
		
		stateTransitionTimer = 0;
		stateTransitionTime = 4;
		
		bounceState = true;
		chaseState = false;
		restState = false;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		super.update(dt, playerShip, bulletList, bossManager);
		if (isDead == true || invulnerable == true) { return; }
		
		if (bounceState == true) {
			bounceState(dt, playerShip, bulletList, bossManager);
		} else if (chaseState == true) {
			chaseState(dt, playerShip, bulletList, bossManager);
		} else if (restState == true) {
			restState(dt, playerShip, bulletList, bossManager);
		}

	}
	
	public void move() {
		if (yVelocity > 0 && (yCoordinate > height() * 0.95)) {
			yCoordinate = height() * 0.95;
			yVelocity = bounce(yVelocity);
		} else if (yVelocity < 0 && (yCoordinate < height() * 0.05)) {
			yCoordinate = height() * 0.05;
			yVelocity = bounce(yVelocity);
		}
		
		if (xVelocity > 0 && (xCoordinate > width() * 0.95)) {
			xCoordinate = width() * 0.95;
			xVelocity = bounce(xVelocity);
		} else if (xVelocity < 0 && (xCoordinate < width() * 0.05)) {
			xCoordinate = height() * 0.05;
			xVelocity = bounce(xVelocity);
		}
	}
	
	public void update(double dt) {
		if (introState == true) {
			move(dt);
			if (yCoordinate < height() * 0.2) { return; }
			yCoordinate = height() * 0.2;
			introState = false;
			invulnerable = false;
			
			if (Math.random() < 0.5) {
				xVelocity = baseXVelocity;
			} else {
				xVelocity = -baseXVelocity;
			}
		}
	}
		
	protected void init() {
		name = StageBoss.Odin.name();
		skin = name;
		
		originalAttackTime = 0.5;
		attackTime = originalAttackTime;
		
		baseXVelocity = 300;
		baseYVelocity = 300;
		projectileSpeedModifier = 1;
		bulletBaseVelocity = 350;
				
		pointValue = 500;
		
		totalShieldAmount = 6000;
		
		width = width() * 0.2;
		height = height() * 0.2;
				
		reset();
	}
	
	protected void bounceState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (numOfBounces >= numOfBouncesCap) {
			bounceState = false;
			chaseState = true;
			stateTransition(baseXVelocity, baseYVelocity);
			
			if (numOfBouncesCap < numOfBouncesHardCap) {
				numOfBouncesCap++;	
			}
			
			if (numOfShotsCap < numOfShotsHardCap) {
				numOfShotsCap++;	
			}
		
		} else {
			if (attackAvailable == false) { return; }
			consecutiveShots(dt, playerShip, bulletList, bossManager);
	
		}
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
	
	protected double bounce(double velocity) {
		
		if (bounceState == true) {
			velocity *= -1.1;
		} else {
			velocity *= -0.2;
		}
		
		numOfBounces++;
		attackAvailable = true;
		return velocity;
	}

	protected void chaseState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
	
		if (numOfBounces >= numOfBouncesCap) {
			chaseState = false; 
			restState = true;
			stateTransition(baseXVelocity * 0.2, baseYVelocity * 0.2);
			return;
		} 
		
		if (attackAvailable == true) { consecutiveShots(dt, playerShip, bulletList, bossManager); }
		
		actionTimer += dt;
		if (actionTimer < actionTime) { return; }
		actionTimer -= actionTime;
		
		xVelocity = playerShip.getXCoordinate() - xCoordinate;
		yVelocity = playerShip.getYCoordinate()  - yCoordinate;

		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseXVelocity / l * 3;
		yVelocity = yVelocity * baseYVelocity / l * 3;
	
	}
	
	protected void restState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		actionTimer += dt;
		stateTransitionTimer += dt;
		
		if (attackAvailable == true) { consecutiveShots(dt, playerShip, bulletList, bossManager); }
		
		if (stateTransitionTimer < stateTransitionTime) { return; }
		stateTransitionTimer -= stateTransitionTime;
	
		double xVelocity = 0;
		double yVelocity = 0;
		if (Math.random() < 0.5) {
			xVelocity = baseXVelocity;
		} else {
			xVelocity = -baseXVelocity;
		}
		
		if (Math.random() < 0.5) {
			yVelocity = baseYVelocity;
		} else {
			yVelocity = -baseYVelocity;
		}
		
		bounceState = true; 
		restState = false;
		decreaseStateTransitionTime();
		stateTransition(xVelocity, yVelocity);
	}
	
	protected void stateTransition(double xVelocity, double yVelocity) {
		numOfBounces = 0;
		numOfShots = 0;
		attackAvailable = true;
		
		actionTimer = 0;
		stateTransitionTimer = 0;
		
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
	}
	
	protected void decreaseStateTransitionTime() {
		if (stateTransitionTime > 2) {
			stateTransitionTime -= 0.5;
		}
	}
	
	protected int setBossShieldAmount() {
		return totalShieldAmount + stage * 200 + gameLoop * 500;
	}
}