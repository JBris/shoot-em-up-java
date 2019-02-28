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

public class Zeus extends AbstractBoss {
	
	/***Properties***/
	
	protected double attackRate;
	protected double spreadUnit;
	protected double chargeTimer, chargeTime;
	protected boolean sideAttackState, upAndCentreState, targetedShotState, chargeState, allInState;
	protected int attackCount, attackCountCap;
	protected int targetedShotCount, targetedShotCountCap, targetedShotCountHardCap;
	protected int allInShotCount, allInShotCountCap, allInShotCountHardCap;
	protected int numOfBounces, numOfBouncesCap;
	
	/***Constructor***/
	
	public Zeus(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
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
		upAndCentreState = false;
		targetedShotState = false;
		chargeState = false;
		allInState = false;

		chargeTimer = 0;
		chargeTime = 1;
		
		attackCount = 0;
		attackCountCap = 15;
		attackRate = 0.3;
		
		spreadUnit = width / 3;
		
		numOfBounces = 0;
		numOfBouncesCap = 2;
		
		targetedShotCount = 0;
		targetedShotCountCap = 4;
		targetedShotCountHardCap = targetedShotCountCap* 2;
		
		allInShotCount = 0;
		allInShotCountCap = 3;
		allInShotCountHardCap = allInShotCountCap * 2;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		super.update(dt, playerShip, bulletList, bossManager);
		if (isDead == true || invulnerable == true) { return; }
		
		if (sideAttackState == true) {
			sideAttackState(dt, playerShip, bulletList, bossManager);
		} else if (upAndCentreState == true) {
			upAndCentreState(dt, playerShip, bulletList, bossManager);		
		} else if (targetedShotState == true) {
			targetedShotState(dt, playerShip, bulletList, bossManager);			
		} else if (chargeState == true) {
			chargeState(dt, playerShip, bulletList, bossManager);			
		} else if (allInState == true) {
			allInState(dt, playerShip, bulletList, bossManager);						
		}
		
	}
	
	public void update(double dt) {
		if (introState == true) {
			move(dt);
			if (yCoordinate < height() * 0.2) { return; }
			yCoordinate = height() * 0.2;
			yVelocity = 0;
			
			introState = false;
			invulnerable = false;
			sideAttackState = true;
			
			if (Math.random() < 0.5) {
				xVelocity = baseXVelocity / 2;
			} else {
				xVelocity = -baseXVelocity / 2;
			}
		}
	}
	
	public void move() {
		
		if (sideAttackState == true) {
			if (xVelocity > 0 && (xCoordinate > width() * 0.9)) {
				xCoordinate = width() * 0.9;
				stop();
				attackAvailable = true;
				attackTimer = 0;
			} else if (xVelocity < 0 && (xCoordinate < width() * 0.1)) {
				xCoordinate = height() * 0.1;
				stop();
				attackAvailable = true;
				attackTimer = 0;
			}
		} else if (upAndCentreState == true) {
			if (yVelocity < 0 && (yCoordinate < height() * 0.1)) {
				yCoordinate = height() * 0.1;
				yVelocity = 0;
			}
			
			if (xVelocity < 0 && (xCoordinate < width() / 2)) {
				xCoordinate = width() / 2;
				xVelocity = 0;
				attackAvailable = true;
			} else if (xVelocity > 0 && (xCoordinate > width() / 2)) {
				xCoordinate = width() / 2;
				xVelocity = 0;
				attackAvailable = true;
			}
		} else if (chargeState ==true) {
			if (yVelocity > 0 && (yCoordinate > height() * 0.95)) {
				yCoordinate = height() * 0.95;
				yVelocity *= -0.2;
			} else if (yVelocity < 0 && (yCoordinate < height() * 0.05)) {
				yCoordinate = height() * 0.05;
				yVelocity *= -0.2;
			}
		
			if (xVelocity > 0 && (xCoordinate > width() * 0.95)) {
				xCoordinate = width() * 0.95;
				xVelocity *= -0.2;
			} else if (xVelocity < 0 && (xCoordinate < width() * 0.05)) {
				xCoordinate = height() * 0.05;
				xVelocity *= -0.2;
			}			
		} else if (allInState == true) {
			if  (xVelocity > 0 && (xCoordinate > width() * 0.5)) { xVelocity = 0; }
			else if  (xVelocity < 0 && (xCoordinate < width() * 0.5)) { xVelocity = 0; }
			
			if  (yVelocity > 0 && (yCoordinate > height() * 0.2)) { yVelocity = 0; }
			else if  (yVelocity < 0 && (yCoordinate < height() * 0.2)) { yVelocity = 0; }
		}

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
	
	protected void sideAttackState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager)  {
		if (attackAvailable == false) {  
			attackTimer += dt;
			if (attackTimer < attackRate * attackTime * 2) { return; }
			attackTimer -= attackRate * attackTime;
			fireDown(dt, playerShip, bulletList, bossManager);
			return;
		}
		
		attackTimer += dt;
		if (attackTimer < attackRate * attackTime) { return; }
		attackTimer -= attackRate * attackTime;
			
		int spreadModifier = 1;
		double xCoordinate = this.xCoordinate + width / 4;
		
		if (xCoordinate > width() / 2) {
			spreadModifier = -1;
			xCoordinate -= width / 2;
		}
			
		sideShots(dt, playerShip, bulletList, bossManager, spreadModifier, xCoordinate);
		attackCount++;
		
		if (attackCount < attackCountCap) { return; }
		
		attackAvailable = false;
		attackCount = 0;
		numOfBounces++;
			
		if (xCoordinate > width() / 2) {
			xVelocity = -baseXVelocity / 2;
		} else {
			xVelocity = baseXVelocity / 2;	
		}	
			
		if (numOfBounces >= numOfBouncesCap) {
			numOfBounces = 0;
			sideAttackState = false;	
			upAndCentreState = true;
			yVelocity = -baseYVelocity * 0.2;
		}
	}
	
	protected void upAndCentreState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager)  { 
		if (attackAvailable == false) { return; }
		
		attackTimer += dt;
		if (attackTimer < attackRate * attackTime) { return; }
		attackTimer -= attackRate * attackTime;
			
		sideShots(dt, playerShip, bulletList, bossManager, -1, xCoordinate - width / 4);
		sideShots(dt, playerShip, bulletList, bossManager, 1, xCoordinate + width / 4);
		
		attackCount++;
		
		if (attackCount < attackCountCap) { return; }
		
		attackAvailable = false;
		attackCount = 0;
		attackTimer = 0;
		upAndCentreState = false;
		targetedShotState = true;
	}
	
	protected void targetedShotState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager)  {  
		attackTimer += dt;
		if (attackTimer < attackRate * attackTime) { return; }
		attackTimer -= attackRate * attackTime;
		
		Random rand = new Random();

		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			
			attackCount++;
			bullet.setBaseVelocity(bulletBaseVelocity * projectileSpeedModifier * 1.5);
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
		
		attackCount++;
		if (attackCount < attackCountCap * 2) { return; }
		attackTimer -= attackRate * attackTime * 5;
		attackCount = 0;
		targetedShotCount++;
		
		if (targetedShotCount < targetedShotCountCap) { return; }
		
		targetedShotCount = 0;
		if (targetedShotCountCap < targetedShotCountHardCap) {
			targetedShotCountCap++;
		}
		
		targetedShotState = false;
		chargeState = true;
		attackAvailable = false;
		attackTimer = 0;
	}
	
	protected void chargeState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager)  {  
		if (attackAvailable == false) {
			chargeTimer += dt;
			if (chargeTimer < chargeTime) { return; }
			chargeTimer -= chargeTime;
			
			xVelocity = playerShip.getXCoordinate() - xCoordinate;
			yVelocity = playerShip.getYCoordinate()  - yCoordinate;

			double l = length(xVelocity, yVelocity);
			xVelocity = xVelocity * baseXVelocity / l * 3;
			yVelocity = yVelocity * baseYVelocity / l * 3;	
			attackAvailable = true;
			
			attackTimer -= attackRate * attackTime * 5;
		} else {
			attackTimer += dt;
			if (attackTimer < attackRate * attackTime) { return; }
			attackTimer -= attackRate * attackTime;
			
			if (Math.random() < 0.5) {
				sideShots(dt, playerShip, bulletList, bossManager, 1, xCoordinate + width / 4, true);
			} else {
				sideShots(dt, playerShip, bulletList, bossManager, -1, xCoordinate - width / 4, true);
			}
			
			attackCount++;
			if (attackCount < attackCountCap) { return; }
			attackCount = 0;
			attackAvailable = false;
				
			numOfBounces++;
			if (numOfBounces < numOfBouncesCap * 2) { return; }
			numOfBounces = 0;
			attackTimer -= attackRate * attackTime * 5;
			chargeState = false;
			allInState = true;
			
			xVelocity = width() / 2 - xCoordinate;
			yVelocity = height() * 0.2 - yCoordinate;
			double l = length(xVelocity, yVelocity);
			xVelocity = xVelocity * baseXVelocity / l;
			yVelocity = yVelocity * baseYVelocity / l;
		}
	}
	
	protected void allInState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager)  {
		attackTimer += dt;
		if (attackTimer < attackRate * attackTime) { return; }
		attackTimer -= attackRate * attackTime;
		
		sideShots(dt, playerShip, bulletList, bossManager, -1, xCoordinate - width / 4, true);
		sideShots(dt, playerShip, bulletList, bossManager, 1, xCoordinate + width / 4, true);
		
		attackCount++;
		
		if (attackCount < attackCountCap) { return; }
		attackCount = 0;
		allInShotCount++;
		attackTimer -= attackRate * attackTime * 10;
		
		if (allInShotCount < allInShotCountCap) { return; } 
		
		allInShotCount = 0;
		if (allInShotCountCap < allInShotCountHardCap) { allInShotCountCap++; }
		
		attackAvailable = false;
		attackTimer = 0;
		allInState = false;
		sideAttackState = true;
		attackTimer -= attackRate * attackTime * 5;

		if (Math.random() < 0.5) {
			xVelocity = baseXVelocity / 2;
		} else {
			xVelocity = -baseXVelocity / 2;
		}
	}
	
	protected void fireDown(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager)  { 
		ArrayList<InterfaceBossBullet> firedBullets = new  ArrayList<InterfaceBossBullet>();
		
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			firedBullets.add(bullet);
			if (firedBullets.size() >= 2) { break; }
		}
		
		int size = firedBullets.size();
		if (size < 2) { return; }
		
		InterfaceBossBullet bullet = firedBullets.get(0);
		bullet.fireAtVelocity(0, bulletBaseVelocity * projectileSpeedModifier, xCoordinate - width / 4, yCoordinate);
		bossManager.deployBullet(bullet);	
		bullet =  firedBullets.get(1);
		bullet.fireAtVelocity(0, bulletBaseVelocity * projectileSpeedModifier, xCoordinate + width / 4, yCoordinate);
		bossManager.deployBullet(bullet);	
	}
	
	protected void sideShots(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager, int spreadModifier, double xCoordinate, boolean specialAttack)  { 
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			if (specialAttack == true) { bullet.setSpecialAttack(true); }
			bullet.fireAtVelocity(spreadUnit * attackCount * spreadModifier, bulletBaseVelocity * projectileSpeedModifier, xCoordinate, yCoordinate);
			bossManager.deployBullet(bullet);
			break;
		}	
	}
	
	protected void sideShots(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager, int spreadModifier, double xCoordinate)  { 
		sideShots(dt, playerShip, bulletList, bossManager, spreadModifier, xCoordinate, false);			
	}
	
	protected void init() {
		name = StageBoss.Zeus.name();
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
		return totalShieldAmount + stage * 400 + gameLoop * 1000;
	}
}