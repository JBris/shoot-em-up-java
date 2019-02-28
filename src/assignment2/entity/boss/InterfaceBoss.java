package src.assignment2.entity.boss;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public interface InterfaceBoss extends InterfaceDeployableEntity, InterfacePoints, InterfaceShieldable  {
		
	public boolean getIntroState();
	
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed);
	public void setAttackRate(EnemyAttackRate enemyAttackRate);
	
	public double getAttackTime();
	public void setAttackTime(double t);
	
	public void setStage(int n);
	public void setGameLoop(int l);
	
	public void launch();
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceBossBullet> bossBullets, InterfaceBossManager bossManager);
	public void manipulateBullet(double dt, InterfacePlayerShip playerShip, InterfaceBossBullet bossBullet);
	public void collide(InterfacePlayerShip playerShip);
	
}