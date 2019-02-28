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

public class Indra extends AbstractBoss {
	
	/***Properties***/
		
	protected double attackRate, cooldownTimer;
	protected boolean hiddenState, sideState, chaseState, cooldownState, mercyState;
	protected int numOfProjectilesFired, numOfProjectilesFiredCap, numOfProjectilesFiredHardCap;
	protected int shotsFired, shotsFiredCap, shotsFiredHardCap;
	protected int cooldownDuration;
	
	/***Constructor***/
	
	public Indra(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
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
		
		attackRate = 0.2;
		numOfProjectilesFired =  0;
		numOfProjectilesFiredCap = 10;
		numOfProjectilesFiredHardCap = numOfProjectilesFiredCap * 2;
		
		cooldownTimer = 0;
		cooldownDuration = 5;
		
		shotsFired = 0;
		shotsFiredCap = 6;
		shotsFiredHardCap = 3;
		
		mercyState = false;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		super.update(dt, playerShip, bulletList, bossManager);
		if (isDead == true || invulnerable == true) { return; }
		
		if (hiddenState == true) {
			hiddenState(dt, playerShip, bulletList, bossManager);
		} else if (sideState == true) {
			sideState(dt, playerShip, bulletList, bossManager);
		} else if (chaseState == true) {
			chaseState(dt, playerShip, bulletList, bossManager);
		} else if (cooldownState == true) {
			cooldownState(dt, playerShip, bulletList, bossManager);
		}

	}
	
	public void move() {
		if (hiddenState == true) {
			if (xVelocity > 0 && (xCoordinate > width() * 0.9)) {
				xVelocity = 0;
			} else if (xVelocity < 0 && (xCoordinate < width() * 0.1)) {
				xVelocity = 0;
			}

			if (yVelocity < 0 && (yCoordinate < width() * 0.2)) {
				yCoordinate = width() * 0.2;
				yVelocity = 0;
			}

		} else if (sideState == true) {
			if (yCoordinate > height() / 2) {
				yVelocity = 0;
			}
		} else if (chaseState == true) {
			if (yCoordinate < height()* 0.2) {
				xVelocity = 0;
				yVelocity = 0;
			}	
		}
	}
	
	public void update(double dt) {
		if (introState == true) {
			move(dt);
			if (yCoordinate < height() * 0.2) { return; }
			yCoordinate = height() * 0.2;
			introState = false;
			invulnerable = false;
			yVelocity = 0;
			
			hiddenState=  true;
			
			if (Math.random() < 0.5) {
				xVelocity = baseXVelocity;
			} else {
				xVelocity = -baseXVelocity;
			}
		}
	}
	
	public void manipulateBullet(double dt, InterfacePlayerShip playerShip, InterfaceBossBullet bullet) {
		if (bullet.getSpecialAttack() == false) { return; }
		
		if (hiddenState == true) {
			if (bullet.getYCoordinate() < playerShip.getYCoordinate()) { return; }
			bullet.setSpecialAttack(false);		
			bullet.moveToCoordinates(playerShip.getXCoordinate(), playerShip.getYCoordinate());
		} else {
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
	}

	protected void reverseBullet(InterfaceBossBullet bullet, double bulletXVelocity, double bulletYVelocity) {
		bullet.setXVelocity(-bulletXVelocity);
		bullet.setYVelocity(-bulletYVelocity);
		bullet.setSpecialAttack(false);
	}
	
	protected void hiddenState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (shotsFired > shotsFiredCap) {
			hiddenState = false;
			sideState = true;
			shotsFired = 0;
			attackTimer = 0;
			yVelocity = baseYVelocity;
		} else {
			attackTimer += dt; 
			if (attackTimer < attackRate) { return; }
			attackTimer -= attackRate;
			
			for (int i = 0; i < bulletList.size(); i++) {
				InterfaceBossBullet bullet = bulletList.get(i);
				if (bullet.isDeployed() == true) { continue; }
				bullet.fireAtVelocity(0, 550 * projectileSpeedModifier, xCoordinate, yCoordinate);
				bullet.setSpecialAttack(true);
				bossManager.deployBullet(bullet);
				numOfProjectilesFired++;				
				break;
			}
		
			
			if (numOfProjectilesFired >= numOfProjectilesFiredCap) { 
				attackTimer -= attackRate * 4 * attackTime; 
				numOfProjectilesFired = 0;
				shotsFired++;
			}
		}
	}

	protected void sideState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {

		if (shotsFired > shotsFiredCap) {
			sideState = false;
			chaseState = true;
			shotsFired = 0;
			attackTimer = 0;
			
			xVelocity = playerShip.getXCoordinate() - xCoordinate;
			yVelocity = playerShip.getYCoordinate() - yCoordinate;

			double l = length(xVelocity, yVelocity);
			xVelocity = xVelocity * baseXVelocity / l;
			yVelocity = yVelocity * baseYVelocity / l;	
			
		} else {
			attackTimer += dt; 
			if (attackTimer < attackRate) { return; }
			attackTimer -= attackRate;
			bounceAttack(dt, playerShip, bulletList, bossManager);
		}
		
	}
	
	protected void bounceAttack(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		Random rand = new Random();
		
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceBossBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			
			numOfProjectilesFired++;	
			bullet.setSpecialAttack(true);				
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
					
		if (numOfProjectilesFired >= numOfProjectilesFiredCap) { 
			attackTimer -= attackRate * 4 * attackTime; 
			numOfProjectilesFired = 0;
			shotsFired++;
		}	
	}
	
	protected void chaseState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		if (shotsFired > shotsFiredCap / 2) {
			chaseState = false;
			cooldownState = true;
			shotsFired = 0;
			attackTimer = 0;
			stop();
		} else {			
			if (mercyState == true) {
				if (playerShip.getMercyState() == false) { mercyState = false; }
				return;
			}
		
			xVelocity = playerShip.getXCoordinate() - xCoordinate;
			yVelocity = playerShip.getYCoordinate() - yCoordinate;

			double l = length(xVelocity, yVelocity);
			xVelocity = xVelocity * baseXVelocity / l * 1.2 * attackTime;
			yVelocity = yVelocity * baseYVelocity / l * 1.2 * attackTime;		

			attackTimer += dt; 
			if (attackTimer < attackRate * 2) { return; }
			attackTimer -= attackRate;
			
			bounceAttack(dt, playerShip, bulletList, bossManager);
			
			if (playerShip.getMercyState() == true) { 
				mercyState = true;
				stop(); 
			}	
			
		}
	}

	protected void cooldownState(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bulletList, InterfaceBossManager bossManager) {
		cooldownTimer += dt;
		if (cooldownTimer < cooldownDuration) { return; }
		cooldownTimer = 0;
		
		hiddenState = true;
		if (shotsFiredCap > shotsFiredHardCap) { shotsFiredCap--; }
		if (numOfProjectilesFiredCap < numOfProjectilesFiredHardCap) { numOfProjectilesFiredCap++; }
			
		if (Math.random() < 0.5) {
			xVelocity = width() * 0.1 - xCoordinate;
		} else {
			xVelocity = width() * 0.9 - xCoordinate;
		}
		
		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseXVelocity / l;
		yVelocity = -baseYVelocity;
	}
	
	protected void init() {
		name = StageBoss.Indra.name();
		skin = name;
		
		originalAttackTime = 0.5;
		attackTime = originalAttackTime;
		
		baseXVelocity = 300;
		baseYVelocity = 300;
		projectileSpeedModifier = 1;
		bulletBaseVelocity = 300;
				
		pointValue = 500;
		
		totalShieldAmount = 5000;
		
		width = width() * 0.2;
		height = height() * 0.2;
				
		reset();
	}
	
	protected int setBossShieldAmount() {
		return totalShieldAmount + stage * 300 + gameLoop * 500;
	}
}