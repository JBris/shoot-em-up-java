package src.assignment.entity;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment.state.*;
import src.assignment.system.*;

public abstract class AbstractDeployableEntity extends AbstractEntity implements InterfaceDeployableEntity {
	
	/***Properties***/
	
	protected int yVelocityModifer;
	protected boolean isDeployed, isTargeted;
	
	/***Constructor***/
	
	public AbstractDeployableEntity(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		super(game, stateManager, graphicsManager, audioManager);
	}
	
	/***Getters and Setters***/

	public boolean getIsTargeted() { return isTargeted; }
	public void setIsTargeted(boolean t) { isTargeted = t; }
	
	public boolean isDeployed() { return isDeployed; }
	public void setDeployed(boolean deployed) { isDeployed = deployed; } 
	
	public int getYVelocityModifier() { return yVelocityModifer;}
	public void setYVelocityModifier(int m) { yVelocityModifer = m; } 
	
}