package src.assignment2.entity;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class SingleFileDeploymentBehaviour  extends AbstractDeploymentBehaviour {
	
	/***Constructor***/
	
	public SingleFileDeploymentBehaviour(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, 
	double deploymentRate, int launchesPerDeployment, int yVelocityModifer, double deploymentSpread) {
		super(game, stateManager, graphicsManager, audioManager, deploymentRate, launchesPerDeployment, yVelocityModifer, deploymentSpread);
	}
		
	/***Methods***/

	public void deployBullets(ArrayList<InterfaceBullet> bullets, ArrayList<InterfaceBullet> deployedBullets, double x, double y, double dt, boolean playSound) {
		deploymentTimer += dt;
		if (deploymentTimer < deploymentRate) { return; }
		deploymentTimer -= deploymentRate;

		int deployedCount = 0;
		int totalBullets = bullets.size();
		for (int i = 0; i < totalBullets; i++) {
			InterfaceBullet bullet = bullets.get(i);
			if (bullet.isDeployed() == true || bullet.isRedeployable() == false) { 
				deployedCount++;
				continue; 
			}
			
			if (playSound == true) { 
				deploymentSoundTimer += dt; 
				if (deploymentSoundTimer > 0.02) {
					deploymentSoundTimer -= 0.02;
					playSound(bullet.getDeploymentSound()); 
				}
			}
			
			bullet.setDeployed(true);
			bullet.setXCoordinate(x);
			bullet.setYCoordinate(y);
			bullet.setYVelocity(bullet.getBaseVelocity() * baseVelocityModifier * bullet.getYVelocityModifier());
			bullet.move(dt);
			deployedBullets.add(bullet);
			break;
		}
		if (deployedCount == totalBullets) { allDeployed = true; }
	}
	
}