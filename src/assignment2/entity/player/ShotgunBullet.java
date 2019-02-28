package src.assignment2.entity.player;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class ShotgunBullet  extends AbstractBullet {
	
	/***Constructor***/
	
	public ShotgunBullet(InterfaceGame game,  InterfaceStateManager stateManager, 
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
		baseVelocity = 900;
		
		width = width() * 0.1;
		height = height() * 0.1;
		
		isDead = false;
		name = PlayerBulletType.Shotgun.name();
	}
}