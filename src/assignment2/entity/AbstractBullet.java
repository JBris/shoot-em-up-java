package src.assignment2.entity;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment.state.*;
import src.assignment.system.*;

public abstract class AbstractBullet extends AbstractDeployableEntity implements InterfaceBullet {
	
	/***Properties***/
	
	protected int damage, currentRedeployments, totalRedeployments;
	protected boolean isRedeployable, isInfiniteAmmo;
	protected String deploymentSound; 

	protected InterfaceDeployableEntity target;
	
	/***Constructor***/
	
	public AbstractBullet(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
		target = this;
		initAttributes();
	}
	
	/***Getters and Setters***/

	public int getDamage() { return damage; }
	public void setDamage(int d) { damage = d; }
	
	public boolean isRedeployable() { return isRedeployable; }
	public void setIsRedeployable(boolean r) { isRedeployable = r; }
	
	public InterfaceDeployableEntity getTarget() { return target; }
	public void setTarget(InterfaceDeployableEntity t) { target = t; } 
	
	public String getDeploymentSound() { return deploymentSound; }
	public void setDeploymentSound(String s) { deploymentSound = s; }
	
	/***Methods***/
	
	public void update(double dt) {
		move(dt);
		move();
	}
	
	public void move() {
		if (xCoordinate < -width /2 || xCoordinate > width() + width / 2) {
			reset();
		} else if (yCoordinate < -height / 2 || yCoordinate > height() + height / 2) {
			reset();
		}
	}
	
	public void collide(InterfaceDeployableEntity enemyShip, InterfaceScore player) {
		if (enemyShip.getInvulnerable() == true) { return; }
		
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
		
		enemyShip.damage(damage, player);
		reset();
	}
	
	public void render() {
		playAnimation(skin, xCoordinate - width / 2, yCoordinate - height / 2, width, height);		
	} 
	
	protected void initAttributes() {
		damage = 0;
		yVelocityModifer = -1;
		skin = "";
		deploymentSound = "pew";
		
		isInfiniteAmmo = false;
		isRedeployable = false;
		totalRedeployments = 0;
		currentRedeployments = totalRedeployments;
		
		xCoordinate = -width();
		yCoordinate = -height();
		xVelocity = 0;
		yVelocity = 0; 
		baseVelocity = 0;
		
		width = 0;
		height = 0;
		
		isDead = false;
		name = "";
 	}
	
	public void reset() {
		isDeployed = false;
		xVelocity = 0;
		yVelocity = 0; 
		xCoordinate = -width();
		yCoordinate = -height();
		
		if (isInfiniteAmmo == false && (isRedeployable == true) ) {
			if (currentRedeployments > 0) {
				currentRedeployments--;
			} else {
				isRedeployable = false;
			}
		}
	}
	
	public void resetRedeployments() {
		currentRedeployments = totalRedeployments;
		isRedeployable = true;
	}
}