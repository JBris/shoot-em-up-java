package src.assignment2.entity.boss;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class BossBullet  extends EnemyBullet implements InterfaceBossBullet {
	
	/***Properties***/
	
	protected boolean specialAttack;
	protected double lifeSpan;
	
	/***Constructor***/
	
	public BossBullet(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
	}
	
	/***Getters and Setters***/

	public boolean getSpecialAttack() { return specialAttack; }
	public void setSpecialAttack(boolean v) { specialAttack = v; }
	
	public double getLifeSpan() { return lifeSpan; }
	public void setLifeSpan(double l) { lifeSpan = l; }
	
	/***Methods***/
	
	public void reset() {
		super.reset();
		specialAttack = false;
		lifeSpan = 0;
	}
	
  	public void update(double dt, InterfacePlayerShip playerShip, InterfaceBoss boss) {
		update(dt);
		collide(playerShip, boss);
	}
	
	protected void collide(InterfacePlayerShip playerShip, InterfaceBoss boss) {
		if (friendlyFire == false) {
			playerCollision(playerShip);
		} else {
			bossCollision(playerShip, boss);
		}
	}
	
	protected void init() {
		damage = 1;
		yVelocityModifer = -1;
		skin = "BossBullet";

		xCoordinate = -width();
		yCoordinate = -height();
		xVelocity = 0;
		yVelocity = 0; 
		
		pointValue = 35;
		
		width = width() * 0.06;
		height = height() * 0.06;
		
		isDead = false;
		name = skin;
		
		friendlyFire = false;
	}
	
	protected void bossCollision(InterfacePlayerShip playerShip, InterfaceBoss boss) {
		double bossWidth = boss.getWidth();
		double bossHeight = boss.getHeight();
		double bossXPosition = boss.getXCoordinate();
		double bossYPosition = boss.getYCoordinate();
		double bossYVelocity = boss.getYVelocity();

		double bossLeft = bossXPosition - bossWidth / 2;
		double bossRight = bossXPosition + bossWidth / 2;
		double bossTop = bossYPosition - bossHeight / 2;
		double bossBottom = bossYPosition + bossHeight / 2;
		
		double bulletLeft = xCoordinate - width / 2;
		double bulletRight = xCoordinate + width / 2;
		double bulletTop = yCoordinate - height /2;
		double bulletBottom = yCoordinate + height /2;
		
		if (bulletRight < bossLeft) { return; }
		if (bulletLeft > bossRight) { return; }
		if (bulletBottom < bossTop) { return; }
		if (bulletTop > bossBottom) { return; }
		
		boss.damage(50, playerShip);
		reset();
	}
}