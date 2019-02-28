package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class Turtle extends AbstractEnemyShip {
	
	/***Properties***/
		
	/***Constructor***/
	
	public Turtle(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	/***Getters and Setters***/
			
	/***Methods***/
		
	public void reset() {
		super.reset();
		squadSize = 3;
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
			increaseNextAttackTime();
			break;
		}
	}
	

	public void render() {
		drawImage(skin, xCoordinate - width / 2, yCoordinate - height / 2, width, height);		
	} 
	
	protected void init() {
		name = EnemyType.Turtle.name();
		skin = name;
		
		originalAttackTime = 0.25;
		attackTime = originalAttackTime;
		attackTimeIncrease = 0.75;
		
		baseXVelocity = 0;
		baseYVelocity = 100;
		bulletBaseVelocity = 350;
		
		baseSquadSize = 3;
		
		pointValue = 35;
		
		totalShieldAmount = 120;
		
		width = width() * 0.12;
		height = height() * 0.12;
				
		baseNextDeploymentTime = 3;
		reset();
	}
	
	protected void preLaunch(InterfaceEnemyShip lastLaunchedShip, InterfaceEnemyShip nextLaunchedShip) {
		if (numOfSquadDeployed == 0) {
			setInitialLaunchCoordinates();
		} else {
			
			double lastXCoordinate = lastLaunchedShip.getXCoordinate();
			double lastYCoordinate = lastLaunchedShip.getYCoordinate();
			double lastWidth = lastLaunchedShip.getWidth();
			double lastHeight = lastLaunchedShip.getHeight();
	
			nextLaunchedShip.setXCoordinate(lastXCoordinate + width() * 0.25);
			nextLaunchedShip.setXVelocity(0);
			nextLaunchedShip.setYCoordinate(lastYCoordinate);
			nextLaunchedShip.setYVelocity(baseYVelocity);
		}
	}
	
	protected void setInitialLaunchCoordinates() {
		isSquadLeader = true;
		xCoordinate = width() * 0.25;
		xVelocity = 0;
		yVelocity = baseYVelocity;
		yCoordinate = -height() * 0.1;
	}
}