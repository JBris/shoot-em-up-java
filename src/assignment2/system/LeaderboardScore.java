package src.assignment2.system;

import java.io.*;
import java.util.*;
import java.text.*;

public class LeaderboardScore implements Serializable {
	
	/***Properties***/
	
	public String name, date;
	public int score, stage, loop;
	public int shieldAmount, shieldRecharge;
	public int boostAmount, boostRecharge;
	public int lives;

	public Date oDate;
	public ProjectileSpeed projectileSpeed;
	public EnemyAttackRate enemyAttackRate;

	/***Constructor***/
	
	public LeaderboardScore(String name, int score, int stage, int loop, InterfaceGameOptions gameOptions) {
		this.name = name;
		this.score = score;
		this.stage = stage;
		this.loop = loop;
		
		shieldAmount = gameOptions.getShieldAmount();
		shieldRecharge = gameOptions.getShieldRecharge();
		boostAmount = gameOptions.getBoostAmount();
		boostRecharge = gameOptions.getBoostRecharge();
		lives = gameOptions.getLives();
		projectileSpeed = gameOptions.getProjectileSpeed();
		enemyAttackRate = gameOptions.getEnemyAttackRate();
		oDate = new Date();
		date = new SimpleDateFormat("dd/MM/yyyy").format(oDate);
	}
		
}