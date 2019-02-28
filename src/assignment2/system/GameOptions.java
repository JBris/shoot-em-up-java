package src.assignment2.system;

public class GameOptions implements InterfaceGameOptions {
	
	/***Properties***/
	
	protected int shieldAmount, shieldRecharge;
	protected int boostAmount, boostRecharge;
	protected int lives;
	
	protected ProjectileSpeed projectileSpeed;
	protected EnemyAttackRate enemyAttackRate;
	protected PlayerControls playerControls;
	
	/***Constructor***/
	
	public GameOptions() {
		shieldAmount = 2;
		shieldRecharge = 30;
		boostAmount = 3;
		boostRecharge = 4;
		lives = 3;
		projectileSpeed = ProjectileSpeed.Medium;
		enemyAttackRate = EnemyAttackRate.Medium;
		playerControls = PlayerControls.Keyboard;
	}
	
	/***Getters and Setters***/
	
	public int getShieldAmount() { return shieldAmount; }
	public void incrementShieldAmount() { shieldAmount++; } 
	public void decrementShieldAmount() { shieldAmount--; }
	public void setShieldAmount(int shieldAmount) { this.shieldAmount = shieldAmount; }

	public int getShieldRecharge() { return shieldRecharge; }
	public void setShieldRecharge(int shieldRecharge) { this.shieldRecharge = shieldRecharge; }
	
	public int getBoostAmount() { return boostAmount; }
	public void incrementBoostAmount() { boostAmount++; } 
	public void decrementBoostAmount() { boostAmount--; }
	public void setBoostAmount(int boostAmount) { this.boostAmount = boostAmount; }
	
	public int getBoostRecharge() { return boostRecharge; }
	public void setBoostRecharge(int boostRecharge) { this.boostRecharge = boostRecharge; }
	
	public int getLives() { return lives; }
	public void incrementLives() { lives++; } 
	public void decrementLives() { lives--; }
	public void setLives(int lives) { this.lives = lives; }
	
	public ProjectileSpeed getProjectileSpeed() { return projectileSpeed; }
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed) { this.projectileSpeed = projectileSpeed; }
	
	public EnemyAttackRate getEnemyAttackRate() { return enemyAttackRate; } 
	public void setEnemyAttackRate(EnemyAttackRate enemyAttackRate) { this.enemyAttackRate = enemyAttackRate; }
	
	public PlayerControls getPlayerControls() { return playerControls; } 
	public void setPlayerControls(PlayerControls playerControls) { this.playerControls = playerControls; }
	
}