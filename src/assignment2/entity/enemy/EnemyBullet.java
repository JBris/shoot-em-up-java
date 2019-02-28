package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class EnemyBullet  extends AbstractDeployableEntity implements InterfaceEnemyBullet {
	
	/***Properties***/

	protected int damage, pointValue;
	protected boolean friendlyFire, isWorthPoints;
	
	/***Constructor***/
	
	public EnemyBullet(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
		init();
	}
	
	/***Getters and Setters***/

	public int getDamage() { return damage; }
	public void setDamage(int d) { damage = d; }
	
	public boolean getFriendlyFire() { return friendlyFire; }
	public void setFriendlyFire(boolean f) { friendlyFire = f; }
	
	public boolean isWorthPoints() { return isWorthPoints; }
	public void setIsWorthPoints(boolean p) { isWorthPoints = p; }
	
	public int getPointValue() { return pointValue; }
	public void setPointValue(int v) { pointValue = v; }
	
	/***Methods***/
	
	public void reset() {
		damage = 1;

		baseVelocity = 200;

		isDeployed = false;
		friendlyFire = false;
		
		xVelocity = 0;
		yVelocity = 0; 
		xCoordinate = -width();
		yCoordinate = -height();
	}
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyShip> enemyShipList) {
		update(dt);
		collide(playerShip, enemyShipList);
	}
	
	public void update(double dt) {
		move(dt);
		move();
	}
	
	protected void collide(InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyShip> enemyShipList) {
		if (friendlyFire == false) {
			playerCollision(playerShip);
		} else {
			enemyCollisions(playerShip, enemyShipList);
		}
	}
	
	public void move() {
		if (xCoordinate < -width /2 || xCoordinate > width() + width / 2) {
			reset();
		} else if (yCoordinate < -height / 2 || yCoordinate > height() + height / 2) {
			reset();
		}
	}
	
	public void fire(double x, double y, double originX, double originY) {
		isDeployed = true;
		
		xCoordinate = originX;
		yCoordinate = originY;
		
		xVelocity = x - xCoordinate;
		yVelocity = y  - yCoordinate;

		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseVelocity / l;
		yVelocity = yVelocity * baseVelocity / l;
	}
	
	public void fireAtVelocity(double xVelocity, double yVelocity, double originX, double originY) {
		isDeployed = true;

		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		xCoordinate = originX;
		yCoordinate = originY;
	}
	
	public void deflect() {
		friendlyFire = true;
		xVelocity *= -3;
		yVelocity *= -3;
	}
	
	public void render() {
		playAnimation(skin, xCoordinate - width / 2, yCoordinate - height / 2, width, height);		
	} 
	
	protected void init() {
		damage = 1;
		yVelocityModifer = -1;
		skin = "EnemyBullet";

		xCoordinate = -width();
		yCoordinate = -height();
		xVelocity = 0;
		yVelocity = 0; 
		
		pointValue = 25;
		
		width = width() * 0.06;
		height = height() * 0.06;
		
		isDead = false;
		name = skin;
		
		friendlyFire = false;
	}
	
	protected void playerCollision(InterfacePlayerShip playerShip) {
		if (playerShip.getAllowInput() == false) { return; }

		if (playerShip.getDeflectAttack() == false) {
			if (playerShip.getInvulnerable() == true) { return; }
			playerCollisionNoDeflect(playerShip);
		} else {
			playerCollisionDeflect(playerShip);
		}
	}
	
	protected void playerCollisionNoDeflect(InterfacePlayerShip playerShip) {
		double enemyShipWidth = playerShip.getWidth();
		double enemyShipHeight = playerShip.getHeight();
		double enemyShipXPosition = playerShip.getXCoordinate();
		double enemyShipYPosition = playerShip.getYCoordinate();
		double enemyShipYVelocity = playerShip.getYVelocity();

		double enemyShipLeft = enemyShipXPosition - enemyShipWidth / 2;
		double enemyShipRight = enemyShipXPosition + enemyShipWidth / 2;
		double enemyShipTop = enemyShipYPosition - enemyShipHeight / 2;
		double enemyShipBottom = enemyShipYPosition + enemyShipHeight / 2;
		
		double bulletLeft = xCoordinate - width / 2;
		double bulletRight = xCoordinate + width / 2;
		double bulletTop = yCoordinate - height /2;
		double bulletBottom = yCoordinate + height /2;
				
		if (bulletRight < enemyShipLeft) { return; }
		if (bulletLeft > enemyShipRight) { return; }
		if (bulletBottom < enemyShipTop) { return; }
		if (bulletTop > enemyShipBottom) { return; }
		
		reset();
		playerShip.damage(1);	
	}
	
	protected void playerCollisionDeflect(InterfacePlayerShip playerShip) {
		double enemyShipWidth = playerShip.getWidth();
		double enemyShipHeight = playerShip.getHeight();
		double enemyShipXPosition = playerShip.getXCoordinate();
		double enemyShipYPosition = playerShip.getYCoordinate();
		double enemyShipYVelocity = playerShip.getYVelocity();

		double enemyShipLeft = enemyShipXPosition - enemyShipWidth * 1.5;
		double enemyShipRight = enemyShipXPosition + enemyShipWidth * 1.5;
		double enemyShipTop = enemyShipYPosition - enemyShipHeight * 1.5;
		double enemyShipBottom = enemyShipYPosition + enemyShipHeight * 1.5;
		
		double bulletLeft = xCoordinate - width * 1.5;
		double bulletRight = xCoordinate + width * 1.5;
		double bulletTop = yCoordinate - height * 1.5;
		double bulletBottom = yCoordinate + height * 1.5;
				
		if (bulletRight < enemyShipLeft) { return; }
		if (bulletLeft > enemyShipRight) { return; }
		if (bulletBottom < enemyShipTop) { return; }
		if (bulletTop > enemyShipBottom) { return; }
		
		playSound("drop");
		playerShip.incrementScoreByValue(pointValue);			
		deflect();
	}
	
	protected void enemyCollisions(InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyShip> enemyShipList) { 
		for (int i = 0; i < enemyShipList.size(); i++) {
			InterfaceEnemyShip enemyShip = enemyShipList.get(i);
			if (enemyShip.isDeployed() == false) { continue; }
			enemyCollision(playerShip, enemyShip);
		}
	}
	
	protected void enemyCollision(InterfacePlayerShip playerShip, InterfaceEnemyShip enemyShip) {
		double enemyShipWidth = enemyShip.getWidth();
		double enemyShipHeight = enemyShip.getHeight();
		double enemyShipXPosition = enemyShip.getXCoordinate();
		double enemyShipYPosition = enemyShip.getYCoordinate();
		double enemyShipYVelocity = enemyShip.getYVelocity();

		double enemyShipLeft = enemyShipXPosition - enemyShipWidth / 2;
		double enemyShipRight = enemyShipXPosition + enemyShipWidth / 2;
		double enemyShipTop = enemyShipYPosition - enemyShipHeight / 2;
		double enemyShipBottom = enemyShipYPosition + enemyShipHeight / 2;
		
		double bulletLeft = xCoordinate - width / 2;
		double bulletRight = xCoordinate + width / 2;
		double bulletTop = yCoordinate - height /2;
		double bulletBottom = yCoordinate + height /2;
		
		if (bulletRight < enemyShipLeft) { return; }
		if (bulletLeft > enemyShipRight) { return; }
		if (bulletBottom < enemyShipTop) { return; }
		if (bulletTop > enemyShipBottom) { return; }
		
		enemyShip.damage(150, playerShip);
		reset();
	}
}