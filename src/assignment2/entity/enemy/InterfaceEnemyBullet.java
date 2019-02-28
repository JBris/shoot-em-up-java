package src.assignment2.entity.enemy;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.system.*;

public interface InterfaceEnemyBullet extends InterfaceDeployableEntity, InterfacePoints  {
	
	public int getDamage();
	public void setDamage(int d);
	
	public boolean getFriendlyFire();
	public void setFriendlyFire(boolean f);
	
	public void fire(double x, double y, double originX, double originY);
	public void fireAtVelocity(double xVelocity, double yVelocity, double originX, double originY);
	public void deflect();
	
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyShip> enemyShipList);

}