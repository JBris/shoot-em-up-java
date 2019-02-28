package src.assignment2.entity;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;

public interface InterfaceDeploymentBehaviour extends InterfaceEntity, InterfaceYVelocityModifier  {

	public double getDeplymentSpread();
	public void setDeplymentSpread(double s);
	
	public boolean allDeployed();
	public void setAllDeployed(boolean d);
	
	public double getDeploymentRate();
	public void setDeploymentRate(double r);
	
	public int getLaunchesPerDeployment();
	public void setLaunchesPerDeployment(int n);
	
	public double getBaseVelocityModifier();
	public void setBaseVelocityModifier(double v);
	
	public void deployBullets(ArrayList<InterfaceBullet> bullets, ArrayList<InterfaceBullet> deployedBullets, double x, double y, double dt, boolean playSound);
	public void reset();
}