package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public abstract class AbstractEnemyShip extends AbstractDeployableEntity implements InterfaceEnemyShip {
	
	/***Properties***/
	
	protected InterfaceEnemyManager enemyManager;
	protected InterfaceExplosionManager explosionManager;

	protected double attackTimer, originalAttackTime, attackTime, attackTimeIncrease, attackTimeIncreaseModifier;
	protected double baseXVelocity, baseYVelocity, bulletBaseVelocity, projectileSpeedModifier;
	protected double nextDeploymentTime, baseNextDeploymentTime;
	protected int pointValue, gameLoop, stage, totalShieldAmount, currentShieldAmount;
	public int baseSquadSize, squadSize, numOfSquadDeployed;
	protected boolean hasEnteredWindow, isWorthPoints, isSquadLeader, attackAvailable; 
		
	/***Constructor***/
	
	public AbstractEnemyShip(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager);
			
		this.enemyManager = enemyManager;
		this.explosionManager = explosionManager;
			
		yVelocityModifer = 1;		
		isWorthPoints = true;
		projectileSpeedModifier =1;
		attackTimeIncreaseModifier = 1;
		init();
	}
	
	/***Getters and Setters***/
		
	public void setProjectileSpeedModifier(double m) {  projectileSpeedModifier = m; }
	public void setAttackRateModifier(double m) { attackTimeIncreaseModifier = m; }
	
	public double getAttackTime() { return attackTime; }
	public void setAttackTime(double t) { attackTime = t; }
	
	public int getNumOfSquadDeployed() { return numOfSquadDeployed; }
	public void setNumOfSquadDeployed(int n) { numOfSquadDeployed = n; }
	
	public boolean isWorthPoints() { return isWorthPoints; }
	public void setIsWorthPoints(boolean p) { isWorthPoints = p; }
	
	public int getPointValue() { return pointValue; }
	public void setPointValue(int v) { pointValue = v; }
	
	public void setStageNum(int n) { stage = n; }
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
		
		hasEnteredWindow = false;
		isSquadLeader = false;

		nextDeploymentTime = baseNextDeploymentTime  - gameLoop * 0.05 - stage * 0.05;
		squadSize = baseSquadSize  + gameLoop + stage;
		if (squadSize > 8) { squadSize = 8; }
		
		currentShieldAmount = totalShieldAmount + stage + gameLoop;
		attackTime = originalAttackTime;
		attackTimer =  0;
		attackAvailable = false;
		
		isDeployed = false;
		numOfSquadDeployed = 0;
		isTargeted = false;
		
		xVelocity = 0;
		yVelocity = 0; 
		xCoordinate = -width();
		yCoordinate = -height();
	}
	
	public void damage(int d, InterfaceScore scorer) {
		currentShieldAmount -= d;
		if (currentShieldAmount < 0) { 
			scorer.incrementScoreByValue(pointValue * (gameLoop + 1) );
			kill(); 
		}
	}
	
	public void kill() {
		explosionManager.createExplosion(ExplosionTypes.Normal.name(), xCoordinate, yCoordinate);
		reset();
	}
	
	public void launchSquad(int index, ArrayList<InterfaceEnemyShip> enemyShips, double dt) {
		int count = 0;
		InterfaceEnemyShip lastLaunchedShip = this;
		
		for (int i = index; i < enemyShips.size(); i++) {
			InterfaceEnemyShip nextLaunchedShip = enemyShips.get(i);
			if (nextLaunchedShip.isDeployed() == true) { continue; }
			
			preLaunch(lastLaunchedShip, nextLaunchedShip);
			
			int numberOfDeployments = lastLaunchedShip.getNumOfSquadDeployed();
			nextLaunchedShip.setNumOfSquadDeployed(numberOfDeployments + 1);
			
			lastLaunchedShip = nextLaunchedShip;
			nextLaunchedShip.launch(dt);		
			enemyManager.deployShip(nextLaunchedShip);
						
			count++;
			if (count >= squadSize) { break; }
			
		}
		enemyManager.setNextDeploymentTime(nextDeploymentTime);
	}
	
	public void launch(double dt) {
		isDeployed = true;
		yVelocity = baseYVelocity; 
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyBullet> bulletList) {
		update(dt);
		
		if (hasEnteredWindow == false) {
			if (xCoordinate < 0) { return; }
			if (xCoordinate > width()) { return; }
			if (yCoordinate > height()) { return; }
			if (yCoordinate < 0) { return; }
			hasEnteredWindow = true;
			return;
		}
		
		if (attackAvailable == false) { 		
			attackTimer += dt;
			if (attackTimer > attackTime) {
				attackTimer -= attackTime;
				attackAvailable = true;
			}
		}
		
		playerShip.collide(this);
		collide(playerShip);
	}
	
	public void update(double dt) {
		move(dt);
		move();
	}
	
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

		damage(100, playerShip);
		playerShip.damage(totalShieldAmount);
	}
	
	public void move() {
		if (yCoordinate > height() + height / 2) {
			reset();
		} 
	}
	
	public void render() {
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
		attackTimeIncrease = 1;
		
		nextDeploymentTime = 0;
		
		reset();
	}
	
	protected void preLaunch(InterfaceEnemyShip lastLaunchedShip, InterfaceEnemyShip nextLaunchedShip) {
		xVelocity = 0; 
		xCoordinate = -width();
		yCoordinate = -height();
	}
	
	protected void attack(double dt, InterfacePlayerShip playerShip) {
		xVelocity = playerShip.getXCoordinate() - xCoordinate;
		yVelocity = playerShip.getYCoordinate()  - yCoordinate;

		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseVelocity / l;
		yVelocity = yVelocity * baseVelocity / l;
	}
	
	protected void increaseNextAttackTime() {
		attackTime += attackTimeIncrease * attackTimeIncreaseModifier;
	}
}