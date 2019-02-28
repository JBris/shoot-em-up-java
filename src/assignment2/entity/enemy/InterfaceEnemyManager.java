package src.assignment2.entity.enemy;

import java.util.*;
import java.lang.*;
import java.util.*;
import src.assignment2.system.*;

public interface InterfaceEnemyManager {

	public int getNumOfLaunchesRemaining();	
	
	public boolean getVictoryState();
	
	public int getGameLoop();
	public void setGameLoop(int l);

	public int getStage();
	public void setStage(int s);
		
	public void setNextDeploymentTime(double t);
	
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed);
	public void setAttackRate(EnemyAttackRate enemyAttackRate);

	public void reset();
	public void fullReset();
	public void update(double dt);
	public void paintComponent();
	
	public void createEnemies(String enemyType, int numOfEnemies);
	public void createBullets(int numOfBullets);
	
	public void deployShip(InterfaceEnemyShip ship);
	public void deployBullet(InterfaceEnemyBullet bullet);
	
	public void deflectAllBullets();
	
	public void rebuildBosses();
	public void getNextBoss();
}