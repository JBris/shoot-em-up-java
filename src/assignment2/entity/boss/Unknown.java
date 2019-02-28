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

public class Unknown extends AbstractBoss {
	
	/***Properties***/
	
	protected boolean metamorphosisComplete;
	protected boolean horizontalSideAttack, verticalSideAttack;
	protected boolean centreState, rainState, randomState, frenziedRandomState, chaseState, chargeState, mirrorState, invertedTopMirrorState, topState, finalState, mercyState;
	protected boolean bounceAttack, playerTrackAttack;
	protected int attackCount, attackCountCap, attackCountHardCap;
	protected int numOfMultiShots, numOfConsecutiveShotsCount, numOfConsecutiveShots;
	protected double slowShotAttackRate, slowShotAttackRateCap, fastShotAttackRate;
	protected double xCoordinateTarget, yCoordinateTarget;
	protected double movementTimer, movementTime, movementTimeCap;
	protected int numOfMovements, numOfMovementsCap, numOfMovementsHardCap;
	protected double bulletLifeSpan;
	
	/***Constructor***/
	
	public Unknown(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
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
		
		metamorphosisComplete = false;
		skin = StageBoss.ImperfectUnknown.name();
		
		centreState = true;
		rainState = false;
		randomState = false;
		frenziedRandomState = false;
		chaseState= false;
		chargeState = false;
		mirrorState = false;
		invertedTopMirrorState = false;
		topState = false;
		finalState = false;
		
		mercyState = false;
		
		horizontalSideAttack = false;
		verticalSideAttack = false;
		
		bounceAttack = true;
		playerTrackAttack = false;
		
		attackAvailable = true;
		
		attackCount = 0;
		attackCountCap = 4;
		attackCountHardCap = attackCountCap / 2;
		
		slowShotAttackRate = 3;
		slowShotAttackRateCap = 1;
		
		fastShotAttackRate = 0.35;
		
		numOfConsecutiveShotsCount = 0;
		numOfConsecutiveShots = 10;
		numOfMultiShots = 20;
		
		xCoordinateTarget = 0;
		yCoordinateTarget = 0;
		
		movementTimer = 0;
		movementTime = slowShotAttackRate + 1;
		movementTimeCap = slowShotAttackRateCap + 1;
		
		numOfMovements = 0;
		numOfMovementsCap = 4;
		numOfMovementsHardCap = numOfMovementsCap / 2;
		
		bulletLifeSpan = 1;
	}
	
	public void damage(int d, InterfaceScore scorer) {
		super.damage(d, scorer);
		
		if (metamorphosisComplete == false) {
			if (currentShieldAmount > totalShieldAmount * 0.6) { return; }
			skin = StageBoss.PerfectUnknown.name();
			explosionManager.createSizedRandomExplosionsOnTarget(ExplosionTypes.Shield.name(), this, 10, width / 2);
			metamorphosisComplete = true;
		}

	}
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		super.update(dt, playerShip, bulletList, bossManager);
		if (isDead == true || invulnerable == true) { return; }
		
		if (centreState == true) {
			centreState(dt, playerShip, bulletList, bossManager);
		} else if (rainState == true) {
			rainState(dt, playerShip, bulletList, bossManager);
		} else if (randomState == true) {
			randomState(dt, playerShip, bulletList, bossManager);			
		} else if (frenziedRandomState == true) {
			frenziedRandomState(dt, playerShip, bulletList, bossManager);			
		} else if (chaseState == true) {
			chaseState(dt, playerShip, bulletList, bossManager);						
		} else if (chargeState == true) {
			chargeState(dt, playerShip, bulletList, bossManager);										
		} else if (mirrorState == true) {
			mirrorState(dt, playerShip, bulletList, bossManager);										
		} else if (invertedTopMirrorState == true) {
			invertedTopMirrorState(dt, playerShip, bulletList, bossManager);										
		} else if (topState == true) {
			topState(dt, playerShip, bulletList, bossManager);													
		} else if (finalState == true) {
			finalState(dt, playerShip, bulletList, bossManager);																
		}
	}
	
	public void update(double dt) {
		if (introState == true) {
			move(dt);
			if (yCoordinate < height() / 2) { return; }
			yCoordinate = height() / 2;
			yVelocity = 0;
			introState = false;
			invulnerable = false;
			bounceAttack = true;
			attackAvailable = true;
		}
	}
	
	public void move() {
		if (rainState == true) {
			if (xVelocity > 0 && (xCoordinate > width() * 0.85)) {
				xVelocity = 0;
			} else if (xVelocity < 0 && (xCoordinate < width() * 0.15)) {
				xVelocity = 0;
			}

			if (yVelocity < 0 && (yCoordinate < width() * 0.2)) {
				yVelocity = 0;
			}
			return;
		}
		
		if (chargeState == true) {
			if (yVelocity < 0 && (yCoordinate < height() * 0.05)) {
				yCoordinate = height() * 0.05;
				yVelocity *= -0.2;
			} else if (yVelocity > 0 && (yCoordinate > height() * 0.95)) {
				yCoordinate = height() * 0.95;
				yVelocity *= -0.2;
			} else if (xVelocity < 0 && (xCoordinate < width() * 0.05)) {
				xCoordinate = width() * 0.05;
				xVelocity *= -0.2;
			} else if (xVelocity > 0 && (xCoordinate > width() * 0.95)) {
				xCoordinate = width() * 0.95; 
				xVelocity *=-0.2;
			}
			return;
		}
		
		if (invertedTopMirrorState == true) {
			 if (yVelocity < 0 && (yCoordinate < height() * 0.2)) {
				yVelocity = 0;
			}
		}
		
		if (topState == true) {
			if (xVelocity > 0 && (xCoordinate > width() / 2)) {
				xVelocity = 0;
			} else if (xVelocity < 0 && (xCoordinate < width() / 2)) {
				xVelocity = 0;
			}
			return;
		}
		
		if (finalState == true) {
			if  (xVelocity > 0 && (xCoordinate > width() /2 )) { xVelocity = 0; }
			else if  (xVelocity < 0 && (xCoordinate < width() / 2)) { xVelocity = 0; }	
			
			if  (yVelocity > 0 && (yCoordinate > height() /2 )) { yVelocity = 0; }
			else if  (yVelocity < 0 && (yCoordinate < height() / 2)) { yVelocity = 0; }	
		}
		
		if (randomState == true) {
			if (xVelocity > 0 && (xCoordinate > xCoordinateTarget)) {
				xVelocity = 0;
			} else if (xVelocity < 0 && (xCoordinate < xCoordinateTarget)) {
				xVelocity = 0;
			}

			if (yVelocity < 0 && (yCoordinate < yCoordinateTarget)) {
				yVelocity = 0;
			} else if (yVelocity > 0 && (yCoordinate > yCoordinateTarget)) {
				yVelocity = 0;
			}
		}
		
		if (yVelocity < 0 && (yCoordinate < height / 2)) {
				yCoordinate =  height / 2;
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
	
	public void manipulateBullet(double dt, InterfacePlayerShip playerShip, InterfaceBossBullet bullet) {
		if (bullet.getSpecialAttack() == false) { return; }
		
		if (bounceAttack == true) {
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
		} else if (playerTrackAttack == true) {
			double lifeSpan = bullet.getLifeSpan();
			if (lifeSpan < bulletLifeSpan) { 
				bullet.setLifeSpan(lifeSpan += dt);
				return; 
			}
			scatterShot(dt, playerShip, bullet);
		} 
	}
	
	protected void scatterShot(double dt, InterfacePlayerShip playerShip, InterfaceBossBullet bullet) {
		double rand = Math.random();
		bullet.setBaseVelocity(bulletBaseVelocity * 1.5);
		bullet.setSpecialAttack(false);

		if (rand < 0.3) {
			bullet.fire(playerShip.getXCoordinate() - playerShip.getWidth() * 4, playerShip.getYCoordinate(), bullet.getXCoordinate(), bullet.getYCoordinate());	
		} else if (rand < 0.6) {
			bullet.fire(playerShip.getXCoordinate() + playerShip.getWidth() * 4, playerShip.getYCoordinate(), bullet.getXCoordinate(), bullet.getYCoordinate());		
		} else {
			bullet.fire(playerShip.getXCoordinate(), playerShip.getYCoordinate(), bullet.getXCoordinate(), bullet.getYCoordinate());		
		}
	}
	
	protected void centreState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (attackAvailable == false) {
			attackTimer += dt;
			if (attackTimer < slowShotAttackRate * attackTime * 2) { return; }
			attackTimer -= 3;
			
			attackCount = 0;
			attackAvailable = true;
			
			bounceAttack = false;
			playerTrackAttack = true;
			
			centreState = false;
			rainState = true;
			
			if (Math.random() < 0.5) {
				xVelocity = width() * 0.1 - xCoordinate;
			} else {
				xVelocity = width() * 0.9 - xCoordinate;
			}
		
			yVelocity = height() * 0.1 - yCoordinate;
			double l = length(xVelocity, yVelocity);
			xVelocity = xVelocity * baseXVelocity / l;
		
		} else {
			attackTimer += dt;
			if (attackTimer < slowShotAttackRate * attackTime) { return; }
			attackTimer -= slowShotAttackRate * attackTime;
			
			multiShotAtPlayer(dt, playerShip, bulletList, bossManager, bulletBaseVelocity * 1.2, true); 

			attackCount++;
			if (attackCount < attackCountCap) { return; }
			attackAvailable = false;			
		}
	}
	
	protected void rainState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (attackAvailable == false) {
			attackAvailable = true;
			attackCount++;
			if (attackCount < attackCountCap) { return; }
			attackCount = 0;
			
			rainState = false;
			randomState = true;
			
			if (Math.random() < 0.5) {
				horizontalSideAttack = true;
			} else {
				verticalSideAttack = true;
			}
			
		} else {
			attackTimer += dt;
			if (attackTimer < fastShotAttackRate * attackTime) { return; }
			attackTimer -= fastShotAttackRate * attackTime;
			
			double bulletXVelocity = bulletBaseVelocity * 0.5;
			if (xCoordinate > width() / 2) {
				bulletXVelocity = -bulletBaseVelocity * 0.5;
			}
						
			for (int i = 0; i < bulletList.size(); i++) {
				InterfaceBossBullet bullet = bulletList.get(i);
				if (bullet.isDeployed() == true) { continue; }
				numOfConsecutiveShotsCount++;
				bossManager.deployBullet(bullet);	
				bullet.fireAtVelocity(bulletXVelocity, 0, xCoordinate, yCoordinate);
				bullet.setSpecialAttack(true);
				break;
			}
			
			if (numOfConsecutiveShotsCount < numOfConsecutiveShots) { return; }
			numOfConsecutiveShotsCount = 0;
			attackTimer -= 1 ;
			attackAvailable = false;
		}
	}
	
	protected void randomState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) { 

		attackTimer += dt;
		if (attackTimer >= slowShotAttackRate * attackTime) { 
			attackTimer -= slowShotAttackRate * attackTime;
			if (horizontalSideAttack == true) {
				verticalSideAttack = true;
				horizontalSideAttack = false;
				sideShots(dt, playerShip, bulletList, bossManager, bulletBaseVelocity * 0.05, 1, 0,  true); 
			} else if (verticalSideAttack == true) {
				horizontalSideAttack = true;
				verticalSideAttack = false;
				sideShots(dt, playerShip, bulletList, bossManager, bulletBaseVelocity * 0.05, 0, 1,  true); 
			}
		}
			
		movementTimer += dt;
		if (movementTimer <= movementTime * attackTime) { return; }
		movementTimer -= movementTime;

		Random random = new Random();
		xCoordinateTarget = width() * (0.05 + ( random.nextDouble() * (0.95 - 0.05) ));
		yCoordinateTarget = height() * (0.05 + ( random.nextDouble() * (0.95 - 0.05) ));
	
		xVelocity = xCoordinateTarget - xCoordinate;
		yVelocity = yCoordinateTarget  - yCoordinate;

		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseXVelocity / l;
		yVelocity = yVelocity * baseYVelocity / l;
							
		numOfMovements++;
		if (numOfMovements < numOfMovementsCap) { return; }
		numOfMovements = 0;
		movementTimer = 0;
			
		randomState = false;
		frenziedRandomState = true;
			
		horizontalSideAttack = false;
		verticalSideAttack = false;		

		xCoordinateTarget = width() / 2;
		yCoordinateTarget = height() / 2;
	}
	
	protected void frenziedRandomState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) { 
		
		if (numOfMovements >= numOfMovementsCap) { 
			attackTimer += dt;
			if (attackTimer < slowShotAttackRate * attackTime) { return; }
			attackTimer -= 1;
			movementTimer = 0;
			
			numOfMovements = 0;
			movementTimer = 0;
		
			frenziedRandomState = false;
			chaseState = true;
		
			bulletLifeSpan = 2;
			bulletBaseVelocity /= 2;
			
			return;
		}

		
		attackTimer += dt;
		if (attackTimer >= slowShotAttackRate * attackTime) { 
			attackTimer -= slowShotAttackRate * attackTime;
			multiShotAtPlayer(dt, playerShip, bulletList, bossManager, -1, -1, bulletBaseVelocity * 0.1, true); 
		}
		
		movementTimer += dt;
		if (movementTimer <= movementTime * attackTime) { return; }
		movementTimer -= movementTime;
	
		Random random = new Random();
		xCoordinateTarget = width() * (0.05 + ( random.nextDouble() * (0.95 - 0.05) ));
		yCoordinateTarget = height() * (0.05 + ( random.nextDouble() * (0.95 - 0.05) ));
		
		xVelocity = xCoordinateTarget - xCoordinate;
		yVelocity = yCoordinateTarget  - yCoordinate;

		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseXVelocity / l;
		yVelocity = yVelocity * baseYVelocity / l;
							
		numOfMovements++;
	}
	
	protected void chaseState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) { 
		if (mercyState == true) {
			if (playerShip.getMercyState() == false) { mercyState = false; }
			return;
		}
		
		if (numOfMovements >= numOfMovementsCap) { 
			movementTimer += dt;
			if (movementTimer <= movementTime * attackTime) { return; }
			movementTimer  = 0;
		
			attackTimer -= 1;
			numOfMovements = 0;
			movementTimer = -1;
		
			bulletLifeSpan = 1;
			bulletBaseVelocity *= 2;
		
			chaseState = false;
			chargeState = true;	
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
		
		attackTimer += dt;
		if (attackTimer < slowShotAttackRate * attackTime * 2) { return; }
		attackTimer -= slowShotAttackRate * attackTime * 2;
		sideShots(dt, playerShip, bulletList, bossManager, bulletBaseVelocity, 0, 1,  true); 
		sideShots(dt, playerShip, bulletList, bossManager, bulletBaseVelocity, 1, 0,  true); 
		
		numOfMovements++;

	}
	
	protected void chargeState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {  
		attackTimer += dt;
		if (attackTimer >= fastShotAttackRate * attackTime) { 
			attackTimer -= fastShotAttackRate * attackTime;
			
			for (int i = 0; i < bulletList.size(); i++) {
				InterfaceBossBullet bullet = bulletList.get(i);
				if (bullet.isDeployed() == true) { continue; }
				numOfConsecutiveShotsCount++;
				bossManager.deployBullet(bullet);	
				bullet.fireAtVelocity(-xVelocity * 0.1, -yVelocity * 0.1, xCoordinate, yCoordinate);
				bullet.setSpecialAttack(true);
				break;
			}
			
			if (numOfConsecutiveShotsCount < numOfConsecutiveShots) { return; }
			numOfConsecutiveShotsCount = 0;
			attackTimer -= 1 ;
		}
			
		movementTimer += dt;
		if (movementTimer <= 1) { return; }
		movementTimer -= movementTime;
	
		xVelocity = playerShip.getXCoordinate() - xCoordinate;
		yVelocity = playerShip.getYCoordinate()  - yCoordinate;

		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseXVelocity / l * 2.5;
		yVelocity = yVelocity * baseYVelocity / l * 2.5;
							
		numOfMovements++;
		if (numOfMovements < numOfMovementsCap) { return; }
		numOfMovements = 0;
		movementTimer = 0;

		chargeState = false;	
		mirrorState = true;
				
		attackTimer = -1;
	}
	
	protected void mirrorState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {  
		xVelocity = -playerShip.getXVelocity();
		yVelocity = -playerShip.getYVelocity();
		
		attackTimer += dt;
		if (attackTimer < fastShotAttackRate * attackTime / 2) { return; }
		attackTimer -= fastShotAttackRate * attackTime / 2;
			
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			numOfConsecutiveShotsCount++;
			bossManager.deployBullet(bullet);	
			bullet.fire(playerShip.getXCoordinate(), playerShip.getYCoordinate(), xCoordinate, yCoordinate);		
			bullet.setSpecialAttack(true);
			break;
		}
		
		if (numOfConsecutiveShotsCount < numOfConsecutiveShots * 2) { return; }
		numOfConsecutiveShotsCount = 0;
		attackTimer -= 1 ;
		attackCount++;
		
		if (attackCount < attackCountCap * 2) { return; }	
		attackTimer -= 2;
		
		attackCount = 0;
		
		mirrorState = false;
		invertedTopMirrorState = true;
		
		yVelocity = -baseYVelocity / 2;
	}
	
	protected void invertedTopMirrorState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {  
		
		if (attackCount >= attackCountCap * 2) { 

			movementTimer += dt;
			if (movementTimer <= 1.5) { return; }
			movementTimer -= movementTime;
		
			attackCount = 0;
			invertedTopMirrorState = false;
			topState = true;
		
			//bounceAttack = true;
			//playerTrackAttack = false;
		
			if (xCoordinate > width() / 2) {
				xVelocity = -baseXVelocity;
			} else {
				xVelocity = baseXVelocity;
			}
			return;
		}	
		
		xVelocity = -playerShip.getXVelocity();
		
		attackTimer += dt;
		if (attackTimer < fastShotAttackRate * attackTime) { return; }
		attackTimer -= fastShotAttackRate * attackTime;
			
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			numOfConsecutiveShotsCount++;
			bossManager.deployBullet(bullet);	
			bullet.fireAtVelocity(0, bulletBaseVelocity, xCoordinate, yCoordinate);
			bullet.setSpecialAttack(true);
			break;
		}
			
		if (numOfConsecutiveShotsCount < numOfConsecutiveShots) { return; }
		numOfConsecutiveShotsCount = 0;
		attackTimer -= 1 ;
		
		attackCount++;
	}
	
	protected void topState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {  
		attackTimer += dt;
		if (attackTimer < slowShotAttackRate * attackTime) { return; }
		attackTimer -= slowShotAttackRate * attackTime;
			
		sideShots(dt, playerShip, bulletList, bossManager, bulletBaseVelocity, 0, 1,  true); 
		sideShots(dt, playerShip, bulletList, bossManager, bulletBaseVelocity, 1, 0,  true);

		attackCount++;
		if (attackCount < attackCountCap) { return; }
		attackTimer = -1;
		attackCount = 0;
		
		topState = false;
		finalState = true;
		
		bounceAttack = false;
		playerTrackAttack = true;
		
		xVelocity = width() / 2 - xCoordinate;
		yVelocity = height() / 2 - yCoordinate;
		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseXVelocity / l;
		yVelocity = yVelocity * baseYVelocity / l;
		
	}
	
	protected void finalState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {  
		
		if (attackCount > attackCountCap) { 	
			movementTimer += dt;
			if (movementTimer <= 2) { return; }
			movementTimer = 0;
			
			attackCount = 0;
			attackTimer = 0;
			
			bounceAttack = true;
			playerTrackAttack = false;
			
			finalState = false;
			centreState = true;
			
			if (attackCountCap > attackCountHardCap) { attackCountCap--; }
			if (movementTime > movementTimeCap) { movementTime--; }
			if (numOfMovementsCap > numOfMovementsCap) { numOfMovementsCap--; }
		
			return;
		}
		
		attackTimer += dt;
		if (attackTimer < fastShotAttackRate * attackTime / 2) { return; }
		attackTimer -= fastShotAttackRate * attackTime / 2;
			
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			numOfConsecutiveShotsCount++;
			bossManager.deployBullet(bullet);	
			bullet.fire(playerShip.getXCoordinate(), playerShip.getYCoordinate(), xCoordinate, yCoordinate);		
			bullet.setSpecialAttack(true);
			break;
		}
		
		if (numOfConsecutiveShotsCount < numOfConsecutiveShots) { return; }
		numOfConsecutiveShotsCount = 0;
		attackTimer -= 1 ;
		attackCount++;
	}
	
	protected void multiShotAtPlayer(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager, double bulletBaseVelocity, boolean bounce) {		
		multiShotAtPlayer(dt, playerShip, bulletList, bossManager, 1, 1, bulletBaseVelocity * 1.2, true); 
	}
	
	protected void multiShotAtPlayer(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager, int xCoordinateModifier, int yCoordinateModifier, double bulletBaseVelocity, boolean bounce) {		
		ArrayList<InterfaceBossBullet> firedBullets = new ArrayList<InterfaceBossBullet>();

		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			firedBullets.add(bullet);
			if (firedBullets.size() >= numOfMultiShots) { break; }
		}
		
		int firedBulletSize = firedBullets.size();
		if (firedBulletSize < numOfMultiShots) { return; }
		
		double playerXCoordinate = playerShip.getXCoordinate();
		double playerYCoordinate = playerShip.getYCoordinate();
		double playerWidth = playerShip.getWidth();

		double spreadUnit = playerWidth * 4;
		double spread = spreadUnit * 10;
			
		for (int i = 0; i < firedBulletSize; i++ ) {
			InterfaceBossBullet bullet = firedBullets.get(i);
			bullet.setBaseVelocity(bulletBaseVelocity * projectileSpeedModifier);
			bullet.fire(playerXCoordinate + spread * xCoordinateModifier, playerYCoordinate * yCoordinateModifier, xCoordinate, yCoordinate);	
			spread -= spreadUnit;
			if (bounce == true) { bullet.setSpecialAttack(true); }
			bossManager.deployBullet(bullet);
		}
	}
	
	protected void sideShots(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager, double bulletBaseVelocity, int xVelocityModifier, int yVelocityModifier, boolean specialAttack) {		
		ArrayList<InterfaceBossBullet> firedBullets = new ArrayList<InterfaceBossBullet>();

		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			firedBullets.add(bullet);
			if (firedBullets.size() >= numOfMultiShots) { break; }
		}
		
		int firedBulletSize = firedBullets.size();
		if (firedBulletSize < numOfMultiShots) { return; }
		
		double playerXCoordinate = playerShip.getXCoordinate();
		double playerYCoordinate = playerShip.getYCoordinate();

		double playerWidth = playerShip.getWidth();
		double spreadUnit = playerWidth * 4;
		double spread = spreadUnit * 10;	
		
		for (int i = 0; i < firedBulletSize; i++ ) {
			InterfaceBossBullet bullet = firedBullets.get(i);
			bullet.setBaseVelocity(bulletBaseVelocity * projectileSpeedModifier);
			bullet.fireAtVelocity(spread * xVelocityModifier, spread * yVelocityModifier, xCoordinate, yCoordinate);
			spread -= spreadUnit;
			if (specialAttack == true) { bullet.setSpecialAttack(true); }
			bossManager.deployBullet(bullet);
		}
	}
	
	protected void reverseBullet(InterfaceBossBullet bullet, double bulletXVelocity, double bulletYVelocity) {
		bullet.setXVelocity(-bulletXVelocity);
		bullet.setYVelocity(-bulletYVelocity);
		bullet.setSpecialAttack(false);
	}
	
	
	protected void init() {
		name = "Unknown";
		skin = StageBoss.ImperfectUnknown.name();
		
		originalAttackTime = 0.5;
		attackTime = originalAttackTime;
		
		baseXVelocity = 550;
		baseYVelocity = 550;
		projectileSpeedModifier = 1;
		bulletBaseVelocity = 500;
				
		pointValue = 2000;
		
		totalShieldAmount = 20000;
		
		width = width() * 0.3;
		height = height() * 0.3;
				
		reset();
	}
	
	protected int setBossShieldAmount() {
		return totalShieldAmount + stage * 0 + gameLoop * 1000;
	}
}