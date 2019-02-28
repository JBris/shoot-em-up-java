package src.assignment2.entity.enemy;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.system.*;

public interface InterfaceEnemyShip extends InterfaceDeployableEntity, InterfacePoints, InterfaceShieldable  {
	
	public void setProjectileSpeedModifier(double m);
	public void setAttackRateModifier(double m);
	
	public double getAttackTime();
	public void setAttackTime(double t);
	
	public int getNumOfSquadDeployed();
	public void setNumOfSquadDeployed(int n);
	
	public void setStageNum(int n);
	public void setGameLoop(int l);
	
	public void launchSquad(int index, ArrayList<InterfaceEnemyShip> enemyShips, double dt);
	public void launch(double dt);
	public void update(double dt, InterfacePlayerShip playerShip, ArrayList<InterfaceEnemyBullet> bulletList);
	
	public void collide(InterfacePlayerShip playerShip);
}