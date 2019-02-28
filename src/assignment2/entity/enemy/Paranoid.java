package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class Paranoid extends AbstractEnemyShip {
	
	/***Properties***/
		
	protected boolean diagonalState;
	
	/***Constructor***/
	
	public Paranoid(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	/***Getters and Setters***/
			
	/***Methods***/
	
	public void reset() {
		super.reset();		
		if (squadSize > 8) { squadSize = 8; }
		diagonalState = false;
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyBullet> bulletList) {
		super.update(dt, playerShip, bulletList);
		if (attackAvailable == false) { return; }
		
		for (int i = 0; i < bulletList.size(); i++) {
			InterfaceEnemyBullet bullet = bulletList.get(i);
			if (bullet.isDeployed() == true) { continue; }
			bullet.fireAtVelocity(0, bulletBaseVelocity * projectileSpeedModifier, xCoordinate, yCoordinate);		
			enemyManager.deployBullet(bullet);	
			attackAvailable = false;
			increaseNextAttackTime();
			break;
		}
	}
		
	public void move() {
		super.move();
		
		if (hasEnteredWindow == false) { return; }
		
		if (diagonalState == false) {
			checkDiagonalState();
		}  else {
			if (xVelocity < 0 && (xCoordinate < width() * 0.1 )) {
				xCoordinate = width() * 0.1;
				xVelocity *= -1;
			} else if (xVelocity > 0 && (xCoordinate > width() * 0.9 )) {
				xCoordinate = width() * 0.9;
				xVelocity *= -1;
			}
		}
	}
	
	protected void init() {
		name = EnemyType.Paranoid.name();
		skin = name;
		
		originalAttackTime = 0.25;
		attackTime = originalAttackTime;
		attackTimeIncrease = 0.75; 
		
		baseXVelocity = 200;
		baseYVelocity = 50;
		bulletBaseVelocity = 350;
		baseSquadSize = 4;
		
		pointValue = 6;
		
		totalShieldAmount = 70;
		
		width = width() * 0.12;
		height = height() * 0.12;
				
		baseNextDeploymentTime = 4;
		reset();
	}
	
	protected void preLaunch(InterfaceEnemyShip lastLaunchedShip, InterfaceEnemyShip nextLaunchedShip) {
		if (numOfSquadDeployed == 0) {
			setInitialLaunchCoordinates();
		} else {
			
			double lastXCoordinate = lastLaunchedShip.getXCoordinate();
			double lastYCoordinate = lastLaunchedShip.getYCoordinate();
			double lastHeight = lastLaunchedShip.getHeight();
			
			nextLaunchedShip.setXCoordinate(lastXCoordinate);
			nextLaunchedShip.setYCoordinate(lastYCoordinate - lastHeight * 2);
		}
	}
	
	protected void setInitialLaunchCoordinates() {
		isSquadLeader = true;
		
		Random random = new Random();
		double randomVal = random.nextDouble();
		if (randomVal < 0.5) {
			xCoordinate = width() * 0.25;
			xVelocity = 0;
		} else {
			xCoordinate = width() * 0.75;
			xVelocity = 0;
		}
		yCoordinate = -height() * 0.1;
	}
	
	protected void checkDiagonalState() {
		if (yCoordinate < height()  * 0.2) { return; }
		
		yCoordinate = height()  * 0.2;
		diagonalState = true;
		
		if (xCoordinate > width() / 2) {
			xVelocity = -baseXVelocity;
		} else {
			xVelocity = baseXVelocity;
		}
		 		
	}
}