package src.assignment2.entity.player;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public interface InterfaceBulletCollection extends InterfaceDeployableEntityCollection, InterfaceAttacker, InterfaceKeyListener, InterfaceMouseListener, InterfaceWrapper  {
	
	public String getActiveSpecialBulletName(); 
	
	public boolean isSpecialAttacking();
	public void setSpecialAttacking(boolean isAttacking);
	
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed);

	public void switchSpecialBullets(String type);
	
	public void update(double dt);
	public void updateBullets(double dt);
	public void collide(InterfaceDeployableEntity enemyShip, InterfaceScore player);
	
	public void renderUi();
	public void renderBullets();
	
	public void disableAttack();
	public void clearSpecialAttack();
	public void fullReset(InterfaceGameOptions gameOptions);
	public void reset();
}