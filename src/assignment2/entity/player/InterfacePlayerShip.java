package src.assignment2.entity.player;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public interface InterfacePlayerShip extends InterfaceEntity, InterfaceScore, InterfaceKeyListener, InterfaceMouseListener, InterfaceAttacker, InterfaceShieldable, InterfaceEngine {
	
	public boolean getMercyState();
	public boolean getGameOverTrigger();
	public void setGameOverTrigger(boolean t);

	public String getActiveSpecialBulletName(); 
	
	public boolean isSpecialAttacking();
	public void setSpecialAttacking(boolean isAttacking);
	
	public int getLives();
	public void setLives(int lives);
	
	public ProjectileSpeed getProjectileSpeed();
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed);
	
	public void promptName();
	public void fullReset(InterfaceGameOptions gameOptions);
	public void paintComponent();
	
	public void switchSpecialBullets(String type);
	public void collide(InterfaceDeployableEntity enemyShip);
	public void disableAttack();
	public void clearSpecialAttack();
}