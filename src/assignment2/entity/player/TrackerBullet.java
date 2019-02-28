package src.assignment2.entity.player;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class TrackerBullet  extends AbstractBullet {
	
	/***Properties***/
	
	protected double trackerTimer, trackerTime;
	
	/***Constructor***/
	
	public TrackerBullet(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
		target = null;
	}
	
	/***Methods***/
	
	public void reset() {
		super.reset();
		trackerTimer = 0;
	}
	
	public void update(double dt) {
		move(dt);
		move();
		
		if (target == null) { return; }
		if (target.isDeployed() == false) { 
			target = null; 
			return;
		}
		
		trackerTimer += dt;
		if (trackerTimer < trackerTime) { return; }
		trackerTimer -= trackerTime;
		
		xVelocity = target.getXCoordinate() - xCoordinate;
		yVelocity = target.getYCoordinate()  - yCoordinate;

		double l = length(xVelocity, yVelocity);
		xVelocity = xVelocity * baseVelocity / l;
		yVelocity = yVelocity * baseVelocity / l;
		
	}
	
	public void collide(InterfaceDeployableEntity enemyShip, InterfaceScore player) {
		super.collide(enemyShip, player);
		
		if (target != null) { return; }
		target = enemyShip;
	}
	
	protected void initAttributes() {
		damage = 10;
		yVelocityModifer = -1;
		skin = "SpecialBullet";
		deploymentSound = "pewpew";

		isInfiniteAmmo = false;
		isRedeployable = true;
		totalRedeployments = 150;
		currentRedeployments = totalRedeployments;
		
		xCoordinate = -width();
		yCoordinate = -height();
		xVelocity = 0;
		yVelocity = 0; 
		baseVelocity = 900;
		
		width = width() * 0.06;
		height = height() * 0.06;
		
		isDead = false;
		name = PlayerBulletType.Tracker.name();
		
		trackerTime = 0.25;
	}
}