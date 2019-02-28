package src.assignment2.entity.boss;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public abstract class AbstractBoss extends AbstractDeployableEntity implements InterfaceBoss {
	
	/***Properties***/
	
	protected InterfaceExplosionManager explosionManager;

	protected double attackTimer, originalAttackTime, attackTime;
	protected double baseXVelocity, baseYVelocity, bulletBaseVelocity, projectileSpeedModifier;
	protected double deathTimer;
	
	protected int pointValue, gameLoop, stage, totalShieldAmount, currentShieldAmount;
	protected int numOfDeathExplosions, numOfSimultaneousDeathExplosions, numOfDeathExplosionsCap;
	protected boolean introState, isWorthPoints, attackAvailable, deathComplete; 

	/***Constructor***/
	
	public AbstractBoss(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager);			
		this.explosionManager = explosionManager;
			
		yVelocityModifer = 1;		
		isWorthPoints = true;
		projectileSpeedModifier =1;
		init();
	}
	
	/***Getters and Setters***/
			
	public void setIsTargeted(boolean t) { }

	public boolean getIntroState() { return introState; }
	
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed) { projectileSpeedModifier = 1; }
	public void setAttackRate(EnemyAttackRate enemyAttackRate) { attackTime = 1; }
	
	public double getAttackTime() { return attackTime; }
	public void setAttackTime(double t) { attackTime = t; }
	
	public boolean isWorthPoints() { return isWorthPoints; }
	public void setIsWorthPoints(boolean p) { isWorthPoints = p; }
	
	public int getPointValue() { return pointValue; }
	public void setPointValue(int v) { pointValue = v; }
	
	public void setStage(int n) { stage = n; }
	public void setGameLoop(int l) { gameLoop = l; }
	
	public int getTotalShieldAmount() { return totalShieldAmount; }
	public void setTotalShieldAmount(int shieldAmount) { 
		if (shieldAmount < 0) { shieldAmount = 0; }
		totalShieldAmount = shieldAmount; 
		if (currentShieldAmount > totalShieldAmount) { resetCurrentShieldAmount(); }
	}
	
	public int getCurrentShieldAmount() { return currentShieldAmount; }
	public void setCurrentShieldAmount(int shieldAmount) {  }
	public void resetCurrentShieldAmount() { currentShieldAmount = totalShieldAmount; }
	
	public int getShieldRecharge() { return -1; }
	public void setShieldRecharge(int shieldRecharge) {}
	public void updateRechargeTimers(double dt) {}

	/***Methods***/
		
	public void reset() {			
		introState = true;
		
		invulnerable = true;
		invisible = false;
		deathComplete = false;
		isDead = false;
		deathTimer = 0;
		numOfSimultaneousDeathExplosions = 8;
		numOfDeathExplosions = 0;
		numOfDeathExplosionsCap = 5;
		
		currentShieldAmount = setBossShieldAmount(); 
		attackTime = originalAttackTime;
		attackTimer =  0;
		attackAvailable = false;
		
		isDeployed = false;
		
		xVelocity = 0;
		yVelocity = 0; 
		xCoordinate = -width();
		yCoordinate = -height();
	}
	
	public void damage(int d, InterfaceScore scorer) {
		currentShieldAmount -= d;
		if (currentShieldAmount <= 0) { 
			currentShieldAmount = 0;
			invulnerable = true;
			scorer.incrementScoreByValue(pointValue * (gameLoop + 1) );
			isDead = true;
		}
	}
	
	public void launch() {
		isDeployed = true;
		xCoordinate = width() / 2;
		yCoordinate =  -height / 2;
		yVelocity = 150; 
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bossBullets, InterfaceBossManager bossManager) {
		if (isDead == false) {
			move(dt);
			move();
			playerShip.collide(this);
			collide(playerShip);			
		} else {
			updateDeath(dt);
		}
	}
	
	protected void updateDeath(double dt) {
		if (deathComplete == true) {
			explosionManager.createSizedExplosion(ExplosionTypes.Normal.name(), xCoordinate, yCoordinate, width * 2);
			reset();
			return;
		}
		
		double explosionTime = 1;
		deathTimer+= dt;
		if (deathTimer < explosionTime) { return; }
		deathTimer -= explosionTime;
		
		if (numOfDeathExplosions < numOfDeathExplosionsCap - 2) {
			explosionManager.createRandomExplosionsOnTarget(ExplosionTypes.Normal.name(), this, numOfSimultaneousDeathExplosions);
			numOfDeathExplosions++;			
		} else if (numOfDeathExplosions < numOfDeathExplosionsCap - 1) { 
			invisible = true;
			explosionManager.createSizedRandomExplosionsOnTarget(ExplosionTypes.Normal.name(), this, numOfSimultaneousDeathExplosions + 2, width);
			numOfDeathExplosions++;			
		} else if (numOfDeathExplosions == numOfDeathExplosionsCap) {
			explosionManager.createSizedRandomExplosionsOnTarget(ExplosionTypes.Normal.name(), this, numOfSimultaneousDeathExplosions + 2, width * 2);
			numOfDeathExplosions++;		
		} else {
			deathComplete = true;
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
		}
	}
	
	public void manipulateBullet(double dt, InterfacePlayerShip playerShip, InterfaceBossBullet bossBullet) {}
	
	public void collide(InterfacePlayerShip playerShip) {
		if (playerShip.getAllowInput() == false) { return; }
		if (playerShip.getInvulnerable() == true) { return; }

		double playerShipWidth = playerShip.getWidth();
		double playerShipHeight = playerShip.getHeight();
		double playerShipXPosition = playerShip.getXCoordinate();
		double playerShipYPosition = playerShip.getYCoordinate();
		double playerShipYVelocity = playerShip.getYVelocity();

		double playerShipLeft = playerShipXPosition - playerShipWidth / 2;
		double playerShipRight = playerShipXPosition + playerShipWidth / 2;
		double playerShipTop = playerShipYPosition - playerShipHeight / 2;
		double playerShipBottom = playerShipYPosition + playerShipHeight / 2;
		
		double shipLeft = xCoordinate - width / 2;
		double shipRight = xCoordinate + width / 2;
		double shipTop = yCoordinate - height /2;
		double shipBottom = yCoordinate + height /2;
				
		if (shipRight < playerShipLeft) { return; }
		if (shipLeft > playerShipRight) { return; }
		if (shipBottom < playerShipTop) { return; }
		if (shipTop > playerShipBottom) { return; }

		playerShip.damage(totalShieldAmount);
	}
	
	public void render() {
		if (invisible == true) { return; }
		playAnimation(skin, xCoordinate - width / 2, yCoordinate - height / 2, width, height);		
	} 
	
	protected void init() {
		name = "";
		skin = "";
		
		baseVelocity = 0;
		bulletBaseVelocity = 0;
		projectileSpeedModifier = 1;
		
		pointValue = 0;
		isWorthPoints = true;
		
		totalShieldAmount = 0;
		
		width = 0;
		height = 0;
				
		originalAttackTime = 0;
		attackTime = originalAttackTime;
				
		reset();
	}
	
	protected int setBossShieldAmount() {
		return totalShieldAmount + stage * 200 + gameLoop * 1000;
	}
	
}