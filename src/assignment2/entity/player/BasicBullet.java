package src.assignment2.entity.player;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class BasicBullet  extends AbstractBullet {
	
	/***Constructor***/
	
	public BasicBullet(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
	}
	
	/***Methods***/
	
	protected void initAttributes() {
		damage = 20;
		yVelocityModifer = -1;
		skin = "BasicBullet";
		deploymentSound = "pew";

		isInfiniteAmmo = true;
		isRedeployable = true;
		totalRedeployments = -1;
		currentRedeployments = totalRedeployments;
		
		xCoordinate = -width();
		yCoordinate = -height();
		xVelocity = 0;
		yVelocity = 0; 
		baseVelocity = 1000;
		
		width = width() * 0.08;
		height = height() * 0.08;
		
		isDead = false;
		name = PlayerBulletType.Basic.name();
	}
}