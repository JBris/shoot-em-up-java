package src.assignment2.entity.boss;

import java.util.*;
import java.lang.*;
import java.util.*;
import src.assignment2.system.*;

public interface InterfaceBossManager {

	public boolean getVictoryState();

	public void setProjectileSpeed(ProjectileSpeed projectileSpeed);
	public void setAttackRate(EnemyAttackRate enemyAttackRate);
	
	public void setStage(int n);
	public void setGameLoop(int l);	

	public void reset();
	public void fullReset();
	public void update(double dt);
	public void paintComponent();
	
	public void createBullets(int numOfBullets);
	public void deployBullet(InterfaceBossBullet bullet);
	public void deflectAllBullets();
	
	public void rebuildBosses();
	public void getNextBoss();
}