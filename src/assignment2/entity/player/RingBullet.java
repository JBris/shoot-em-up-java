package src.assignment2.entity.player;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class RingBullet  extends AbstractBullet {
	
	/***Constructor***/
	
	public RingBullet(InterfaceGame game,  InterfaceStateManager stateManager, 
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
		totalRedeployments = 200;
		currentRedeployments = totalRedeployments;
		
		xCoordinate = -width();
		yCoordinate = -height();
		xVelocity = 0;
		yVelocity = 0; 
		baseVelocity = 800;
		
		width = width() * 0.07;
		height = height() * 0.07;
		
		isDead = false;
		name = PlayerBulletType.Ring.name();
	}
}