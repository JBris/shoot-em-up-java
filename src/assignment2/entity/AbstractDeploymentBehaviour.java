package src.assignment2.entity;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment.state.*;
import src.assignment.system.*;

public abstract class AbstractDeploymentBehaviour  extends AbstractEntity implements InterfaceDeploymentBehaviour {
	
	/***Properties***/
	
	protected boolean allDeployed;
	protected double deploymentRate, deploymentTimer, baseVelocityModifier, deploymentSpread, deploymentSoundTimer;
	protected int yVelocityModifer, launchesPerDeployment;
	
	/***Constructor***/
	
	public AbstractDeploymentBehaviour(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, 
	double deploymentRate, int launchesPerDeployment, int yVelocityModifer, double deploymentSpread) {
		super(game, stateManager, graphicsManager, audioManager);
		this.deploymentRate = deploymentRate;
		this.launchesPerDeployment = launchesPerDeployment;
		this.yVelocityModifer = yVelocityModifer;
		this.deploymentSpread = deploymentSpread;
		baseVelocityModifier = 1;
		reset();
	}
	
	/***Getters and Setters***/
	
	public boolean allDeployed() { return allDeployed; }
	public void setAllDeployed(boolean d) { allDeployed = d; }
	
	public int getYVelocityModifier() { return yVelocityModifer;}
	public void setYVelocityModifier(int m) { yVelocityModifer = m; } 
	
	public double getDeploymentRate() { return deploymentRate; }
	public void setDeploymentRate(double r) { deploymentRate = r; }
	
	public int getLaunchesPerDeployment() { return launchesPerDeployment; } 
	public void setLaunchesPerDeployment(int n) { launchesPerDeployment = n; }
	
	public double getBaseVelocityModifier() { return baseVelocityModifier; }
	public void setBaseVelocityModifier(double v) { baseVelocityModifier = v; }
	
	public double getDeplymentSpread() { return deploymentSpread; }
	public void setDeplymentSpread(double s) { deploymentSpread = s; }
	
	/***Methods***/

	public void reset() {
		deploymentTimer = 0;	
		allDeployed = false;
	}
}