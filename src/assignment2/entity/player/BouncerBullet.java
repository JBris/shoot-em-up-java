package src.assignment2.entity.player;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class BouncerBullet  extends AbstractBullet {
	
	/***Constructor***/
	
	public BouncerBullet(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
	}
	
	/***Methods***/
	
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
		baseVelocity = 1000;
		
		width = width() * 0.06;
		height = height() * 0.06;
		
		isDead = false;
		name = PlayerBulletType.Bouncer.name();
	}
	
	public void move(double dt) {
		if (xCoordinate > width() + width / 2) {
			xCoordinate = width() + width / 2;
			xVelocity *= -1;
		} else if (xCoordinate < width / 2) {
			xCoordinate = width / 2;
			xVelocity *= -1;
		}
		
		xCoordinate += xVelocity * dt;
		yCoordinate += yVelocity * dt;
	}
}