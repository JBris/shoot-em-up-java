package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class Saboteur extends AbstractEnemyShip {
	
	/***Properties***/
		
	protected boolean freeMovement;
	protected double movementTimer, flyAwayTimer;
	protected int movementTime, flyAwayTime, attackCount, maxAtacks;
	
	/***Constructor***/
	
	public Saboteur(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	/***Getters and Setters***/
			
	/***Methods***/
		
	public void reset() {
		super.reset();
		squadSize = 1;
		
		attackCount = 0;
		
		freeMovement = true;
		movementTimer = 0;
		flyAwayTimer = 0;
		movementTime = 4;
		flyAwayTime = movementTime * 4;
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
			attackCount++;
			break;
		}
		
		if (attackCount >= maxAtacks) {
			attackCount = 0;
			attackTime = 2 * attackTimeIncreaseModifier;
		} else {
			attackTime = originalAttackTime;
		}
	}
	
	public void update(double dt) {
		super.update(dt);
		
		if (hasEnteredWindow == false) { return; }
		if (freeMovement == false) { return; }
		
		flyAwayTimer += dt;
		if (flyAwayTimer > flyAwayTime) {
			freeMovement = false;
			xVelocity = 0;
			yVelocity = baseYVelocity;
			return;
		}
		
		movementTimer += dt;
		if (movementTimer <= movementTime) { return; }
		movementTimer -= movementTime;
		
		Random random = new Random();
		double x = width() * (0.1 + ( random.nextDouble() * (0.9 - 0.1) ));
		double y = height() * (0.1 + ( random.nextDouble() * (0.9 - 0.1) ));
		
		xVelocity = x - xCoordinate;
		yVelocity = y  - yCoordinate;

		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseXVelocity / l;
		yVelocity = yVelocity * baseYVelocity / l;
	}
	
	public void move() {
		super.move();
		
		if (freeMovement == false) { return; }
		
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
	
	protected void init() {
		name = EnemyType.Saboteur.name();
		skin = name;
		
		originalAttackTime = 0.5;
		attackTime = originalAttackTime;
		maxAtacks = 3;

		baseXVelocity = 300;
		baseYVelocity = 300;
		bulletBaseVelocity = 450;
		
		baseSquadSize = 1;
		
		pointValue = 50;
		
		totalShieldAmount = 400;
		
		width = width() * 0.12;
		height = height() * 0.12;
				
		baseNextDeploymentTime = 1;
		reset();
	}
	
	protected void preLaunch(InterfaceEnemyShip lastLaunchedShip, InterfaceEnemyShip nextLaunchedShip) {
		Random random = new Random();
		xCoordinate = width() * (0.1 + ( random.nextDouble() * (0.9 - 0.1) ));
		yCoordinate = -height() * 0.1;
	}
	
}