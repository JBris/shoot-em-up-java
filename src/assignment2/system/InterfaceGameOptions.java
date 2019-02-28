package src.assignment2.system;

public interface InterfaceGameOptions {
	
	public int getShieldAmount();
	public void incrementShieldAmount();
	public void decrementShieldAmount();
	public void setShieldAmount(int shieldAmount);

	public int getShieldRecharge();
	public void setShieldRecharge(int shieldRecharge);
	
	public int getBoostAmount();
	public void incrementBoostAmount();
	public void decrementBoostAmount();
	public void setBoostAmount(int boostAmount);
	
	public int getBoostRecharge();
	public void setBoostRecharge(int boostRecharge);
	
	public int getLives();
	public void incrementLives();
	public void decrementLives();
	public void setLives(int lives);
	
	public ProjectileSpeed getProjectileSpeed();
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed);
	
	public EnemyAttackRate getEnemyAttackRate();
	public void setEnemyAttackRate(EnemyAttackRate enemyAttackRate);
	
	public PlayerControls getPlayerControls();
	public void setPlayerControls(PlayerControls playerControls);
	
}