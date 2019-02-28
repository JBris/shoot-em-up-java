package src.assignment2.entity;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class RingDeploymentBehaviour  extends SpreadDeploymentBehaviour {
	
	/***Constructor***/
	
	public RingDeploymentBehaviour(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, 
	double deploymentRate, int launchesPerDeployment, int yVelocityModifer, double deploymentSpread) {
		super(game, stateManager, graphicsManager, audioManager, deploymentRate, launchesPerDeployment, yVelocityModifer, deploymentSpread);
	}
		
	/***Methods***/
	
	protected void deploySpreadBullets(ArrayList<InterfaceBullet> bullets, ArrayList<InterfaceBullet> deployedBullets, double x, double y, double dt) {
		int numOfBullets = bullets.size();
		int halfBullets = numOfBullets / 2;
		double spread = deploymentSpread * ((int) halfBullets / 2);
		

		int i = 0;
		for (int j = 0; j < halfBullets; j++) {
			i++;
			InterfaceBullet bullet = bullets.get(j);
			bullet.setDeployed(true);
			bullet.setXCoordinate(x);
			bullet.setYCoordinate(y);
			
			bullet.setXVelocity(spread);
			spread -= deploymentSpread;
			
			bullet.setYVelocity(bullet.getBaseVelocity() * baseVelocityModifier * bullet.getYVelocityModifier());
			bullet.move(dt);
			deployedBullets.add(bullet);
		}
		
		spread = deploymentSpread * ((int) halfBullets / 2);
		
		for (int j = i; j < numOfBullets; j++) {
			InterfaceBullet bullet = bullets.get(j);
			bullet.setDeployed(true);
			bullet.setXCoordinate(x);
			bullet.setYCoordinate(y);
			
			bullet.setXVelocity(spread);
			spread -= deploymentSpread;
			
			bullet.setYVelocity(bullet.getBaseVelocity() * baseVelocityModifier * -bullet.getYVelocityModifier());
			bullet.move(dt);
			deployedBullets.add(bullet);
		}
	}
	
}