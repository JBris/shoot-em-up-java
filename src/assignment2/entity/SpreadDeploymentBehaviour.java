package src.assignment2.entity;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class SpreadDeploymentBehaviour  extends AbstractDeploymentBehaviour {
	
	/***Constructor***/
	
	public SpreadDeploymentBehaviour(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, 
	double deploymentRate, int launchesPerDeployment, int yVelocityModifer, double deploymentSpread) {
		super(game, stateManager, graphicsManager, audioManager, deploymentRate, launchesPerDeployment, yVelocityModifer, deploymentSpread);
	}
		
	/***Methods***/

	public void deployBullets(ArrayList<InterfaceBullet> bullets, ArrayList<InterfaceBullet> deployedBullets, double x, double y, double dt, boolean playSound) {
		deploymentTimer += dt;
		if (deploymentTimer < deploymentRate) { return; }
		deploymentTimer -= deploymentRate;

		int numOfBullets = bullets.size();
		ArrayList<InterfaceBullet> bulletsToDeploy = new ArrayList<InterfaceBullet>();
		for (int i = 0; i < launchesPerDeployment; i++) {
			for (int j = 0; j < numOfBullets; j++) {
				InterfaceBullet bullet = bullets.get(j);
				if (bullet.isDeployed() == true || bullet.isRedeployable() == false) { 
					continue; 
				}
				
				if (j > numOfBullets - launchesPerDeployment) { 
					allDeployed = true;
					return;
				}
				
				bulletsToDeploy.add(bullet);
				if (bulletsToDeploy.size() == launchesPerDeployment) {
	
					if (playSound == true) { 
						deploymentSoundTimer += dt; 
						if (deploymentSoundTimer > 0.02) {
							deploymentSoundTimer -= 0.02;
							playSound(bullet.getDeploymentSound()); 
						}
					}
			
					deploySpreadBullets(bulletsToDeploy, deployedBullets, x , y , dt);
					return;
				}
				
			}
		}
	
		allDeployed = true;
	}
	
	protected void deploySpreadBullets(ArrayList<InterfaceBullet> bullets, ArrayList<InterfaceBullet> deployedBullets, double x, double y, double dt) {
		
		double spread = deploymentSpread * ((int) launchesPerDeployment / 2);
		
		for (int i = 0; i < bullets.size(); i++) {
			InterfaceBullet bullet = bullets.get(i);
			bullet.setDeployed(true);
			bullet.setXCoordinate(x);
			bullet.setYCoordinate(y);
			
			bullet.setXVelocity(spread);
			spread -= deploymentSpread;
			
			bullet.setYVelocity(bullet.getBaseVelocity() * baseVelocityModifier * bullet.getYVelocityModifier());
			bullet.move(dt);
			deployedBullets.add(bullet);
		}
	}
	
}