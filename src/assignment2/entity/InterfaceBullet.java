package src.assignment2.entity;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.enemy.*;

public interface InterfaceBullet extends InterfaceDeployableEntity, InterfaceDamager  {

	public String getDeploymentSound();
	public void setDeploymentSound(String s);
	
	public boolean isRedeployable();
	public void setIsRedeployable(boolean r);
	
	public InterfaceDeployableEntity getTarget();
	public void setTarget(InterfaceDeployableEntity t);
	
	public void resetRedeployments();
	public void collide(InterfaceDeployableEntity enemyShip, InterfaceScore player);
	
}