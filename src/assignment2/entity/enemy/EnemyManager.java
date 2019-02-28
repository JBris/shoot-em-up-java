package src.assignment2.entity.enemy;

import java.awt.*;
import java.lang.*;
import java.util.*;
import src.assignment2.entity.boss.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class EnemyManager implements InterfaceEnemyManager {
	
	/***Properties***/
	
	protected InterfaceGame game;
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	protected InterfacePlayerShip playerShip;
	protected InterfaceBossManager bossManager;
	protected InterfaceEnemyShipFactory enemyShipFactory;
	protected InterfaceExplosionManager explosionManager;
	
	protected boolean enemyState, bossState, mercyState;
	protected int gameLoop, stage;
	protected double shipDeploymentTimer, baseShipDeploymentTime, nextShipDeploymentTime, enemyDifficultyRandomModifier;
	protected double enemyProjectileVelocityModifier, enemyAttackRateModifier;
	protected int totalNumOfLaunches, numOfLaunchesRemaining, baseNumberOfLaunches, numOfDestroyedShips;
	protected String currentEnemy, nextEnemy;
	
	protected Map< String, ArrayList<String> > shipDifficultyMap;
	protected Map<String, ArrayList<InterfaceEnemyShip> > shipMap;
	protected ArrayList<InterfaceEnemyShip> deployedShipList;
	
	protected ArrayList<InterfaceEnemyBullet> bulletList;
	protected ArrayList<InterfaceEnemyBullet> deployedBulletList;

	protected EnemyType[] enemyTypes;
	protected EnemyDifficulty[] enemyDifficulty;
	protected AnimatedEnemyType[] animatedEnemyType;
	
	/***Constructor***/
	
	public EnemyManager(InterfaceGame game, InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager, InterfacePlayerShip playerShip, InterfaceBossManager bossManager, InterfaceExplosionManager explosionManager) {
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
			
		this.playerShip = playerShip;
		this.bossManager = bossManager;
		enemyShipFactory = EnemyShipFactory.createInstance(game, stateManager, graphicsManager, audioManager, this, explosionManager);
		this.explosionManager = explosionManager;
		init();
	}
	
	/***Getters and Setters***/
	
	public int getNumOfLaunchesRemaining() { return numOfLaunchesRemaining; }
	
	public boolean getVictoryState() { return bossManager.getVictoryState(); }
	
	public int getGameLoop() { return gameLoop; }
	public void setGameLoop(int l) { 
		gameLoop = l;  
		bossManager.setGameLoop(l);
	}

	public int getStage() { return stage; }
	public void setStage(int s) { 
		stage = s; 
		bossManager.setStage(s);
	}
		
	public void setNextDeploymentTime(double t) { nextShipDeploymentTime = t; }
		
	/***Methods***/
	
	public void reset() {	
		
		if (stage > getTotalNumberOfRandomStages()) {
			enemyState = false; 
			bossState = true;
		} else {
			enemyState = true; 
			bossState = false;
		}

		mercyState = false;
		
		currentEnemy = "";
		nextEnemy = "";
		
		enemyDifficultyRandomModifier = 0;
		
		Random random = new Random();
		totalNumOfLaunches  = baseNumberOfLaunches + gameLoop * 5 + stage * 5 + (random.nextInt(6) + 5);
		numOfLaunchesRemaining = totalNumOfLaunches;
		numOfDestroyedShips = 0;

		shipDeploymentTimer = 0;	
		
		baseShipDeploymentTime = 2 - gameLoop / 5;		
		if (baseShipDeploymentTime < 0) { baseShipDeploymentTime = 0; }
		nextShipDeploymentTime = baseShipDeploymentTime + 2;
				
		bossManager.reset();
		
		for (int i = 0; i < enemyTypes.length; i++) {
			ArrayList<InterfaceEnemyShip> shipList = shipMap.get(enemyTypes[i].name());
			for (int k = 0; k < shipList.size(); k ++) {
				InterfaceEnemyShip ship = shipList.get(k);
				ship.reset();
				ship.setStageNum(stage);
				ship.setGameLoop(gameLoop);
			}
		}
		
		for (int i = 0; i <  deployedShipList.size(); i++) {
			deployedShipList.remove(i);
		}
		
		for (int i = 0; i <  bulletList.size(); i++) {
			bulletList.get(i).reset();
		}
		
		for (int i = 0; i <  deployedBulletList.size(); i++) {
			deployedBulletList.remove(i);
		}
	}
	
	public void fullReset() {
		stage = 1;
		gameLoop = 0;
		baseNumberOfLaunches = 100;
		
		enemyProjectileVelocityModifier = 1;
		enemyAttackRateModifier = 1;
		bossManager.fullReset();
		reset();
	}
	
	public void rebuildBosses() {
		bossManager.rebuildBosses();
	}
	
	public void getNextBoss() {
		bossManager.getNextBoss();
	}
	
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed) {
		if (projectileSpeed == ProjectileSpeed.Slow) {
			enemyProjectileVelocityModifier = 0.5;
		} else if (projectileSpeed == ProjectileSpeed.Fast) {
			enemyProjectileVelocityModifier = 1.5;
		} else {
			enemyProjectileVelocityModifier = 1;	
		}
		
		for (int i = 0; i < enemyTypes.length; i++) {
			ArrayList<InterfaceEnemyShip> shipList = shipMap.get(enemyTypes[i].name());
			for (int k = 0; k < shipList.size(); k ++) {
				InterfaceEnemyShip ship = shipList.get(k);
				ship.setProjectileSpeedModifier(enemyProjectileVelocityModifier);
			}
		}
		
		bossManager.setProjectileSpeed(projectileSpeed);
	}
	
	public void setAttackRate(EnemyAttackRate enemyAttackRate) {
		if (enemyAttackRate == EnemyAttackRate.Slow) {
			enemyAttackRateModifier = 2;
		} else if (enemyAttackRate == EnemyAttackRate.Fast) {
			enemyAttackRateModifier = 0.5;
		} else {
			enemyAttackRateModifier = 1;	
		}	
		
		for (int i = 0; i < enemyTypes.length; i++) {
			ArrayList<InterfaceEnemyShip> shipList = shipMap.get(enemyTypes[i].name());
			for (int k = 0; k < shipList.size(); k ++) {
				InterfaceEnemyShip ship = shipList.get(k);
				ship.setAttackRateModifier(enemyAttackRateModifier);
			}
		}
		
		bossManager.setAttackRate(enemyAttackRate);
	}
	
	public void update(double dt) {				
		if (enemyState == true) {		
			updateEnemyState(dt);
		} else if (bossState == true) {
			updateBossState(dt);
		}
	}
	
	public void deflectAllBullets() {
		if (enemyState == true) {
			for (int i = 0; i <  deployedBulletList.size(); i++) {
				InterfaceEnemyBullet bullet = deployedBulletList.get(i);
				if (bullet.isDeployed() == false) {
					deployedBulletList.remove(i);
					continue;
				}
				bullet.deflect();
			}	
		} else {
			bossManager.deflectAllBullets();
		}
	}
	
	public void paintComponent() {
		if (enemyState == true) {
			renderShips();
			renderBullets();
		} else if (bossState == true) {
			bossManager.paintComponent();
		}

	}
	
	public void createEnemies(String enemyType, int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = enemyShipFactory.createEnemies(enemyType, numOfEnemies);
		shipMap.put(enemyType, shipList);
	}
	
	public void createBullets(int numOfBullets) {
		for (int i = 0; i < numOfBullets; i++) {
			bulletList.add(enemyShipFactory.createBullet());
		}
	}
	
	public void deployShip(InterfaceEnemyShip ship) {
		deployedShipList.add(ship);
		numOfLaunchesRemaining--;
	}
	
	public void deployBullet(InterfaceEnemyBullet bullet) {
		deployedBulletList.add(bullet);	
	}
	
	protected void init() {
		enemyProjectileVelocityModifier = 1;
		
		enemyTypes = EnemyType.values();
		enemyDifficulty = EnemyDifficulty.values();
		animatedEnemyType = AnimatedEnemyType.values();
		
		deployedShipList = new ArrayList<InterfaceEnemyShip>();
		shipMap = new HashMap<String, ArrayList<InterfaceEnemyShip> >();
		shipDifficultyMap = new HashMap< String, ArrayList<String> >();
		
		bulletList = new ArrayList<InterfaceEnemyBullet>();
		deployedBulletList = new ArrayList<InterfaceEnemyBullet>();
		
		for (int i = 0; i < enemyDifficulty.length; i++) {
			EnemyDifficulty difficulty = enemyDifficulty[i];
			shipDifficultyMap.put(difficulty.name(), EnemyDifficulty.getEnemyDifficultyMapping(difficulty));
		}
		
		for (int i = 0; i < enemyTypes.length; i ++) {
			shipMap.put(enemyTypes[i].name(), new ArrayList<InterfaceEnemyShip>());
		}
		
		fullReset();
	}
	
	protected void launchShip(double dt) {
		
		shipDeploymentTimer += dt;
		if (shipDeploymentTimer <= baseShipDeploymentTime + nextShipDeploymentTime) { return; }
		shipDeploymentTimer -= nextShipDeploymentTime;
			
		chooseNextEnemy();
		ArrayList<InterfaceEnemyShip> enemyShips = shipMap.get(nextEnemy);
		
		for (int i = 0; i < enemyShips.size(); i++) {
			InterfaceEnemyShip squadLeader = enemyShips.get(i);
			if (squadLeader.isDeployed() == true) { continue; }
			squadLeader.launchSquad(i, enemyShips, dt);
			break;
		}
		
	}
	
	protected void chooseNextEnemy() {
		String difficulty = "";
		Random random = new Random();
		
		double randomNum = (0 + ( random.nextDouble() * (1 - 0) )) + enemyDifficultyRandomModifier + stage * 0.025 + gameLoop * 0.1 - numOfLaunchesRemaining * 0.0025;
		
		if (randomNum < 0.4) {
			difficulty = EnemyDifficulty.Easy.name();
			enemyDifficultyRandomModifier += 0.05;
		} else if (randomNum < 0.75) {
			difficulty = EnemyDifficulty.Medium.name();
			enemyDifficultyRandomModifier += 0.05;
		} else {
			difficulty = EnemyDifficulty.Hard.name();
			enemyDifficultyRandomModifier -= 0.35;
		}
		
		ArrayList<String> enemyNames = shipDifficultyMap.get(difficulty);
		do {
			nextEnemy = enemyNames.get(random.nextInt(enemyNames.size()));
		} while(currentEnemy == nextEnemy);
		currentEnemy = nextEnemy;
	}
	
	protected void renderShips() {
		for (int i = 0; i < deployedShipList.size(); i++) {
			InterfaceEnemyShip ship = deployedShipList.get(i);
			if (ship.isDeployed() == false) {
				continue;
			}
			ship.render();
		}		

	}
	
	protected void renderBullets() {
		for (int i = 0; i < deployedBulletList.size(); i++) {
			InterfaceEnemyBullet bullet = deployedBulletList.get(i);
			if (bullet.isDeployed() == false) {
				continue;
			}
			bullet.render();
		}		

	}		
	
	protected void updateEnemyState(double dt) {
		
		for (int i = 0; i < deployedShipList.size(); i++) {
			InterfaceEnemyShip ship = deployedShipList.get(i);
			if (ship.isDeployed() == false) {
				numOfDestroyedShips++;
				deployedShipList.remove(i);
				continue;
			}
			ship.update(dt, playerShip, bulletList);
		}
		
		for (int i = 0; i <  deployedBulletList.size(); i++) {
			InterfaceEnemyBullet bullet = deployedBulletList.get(i);
			if (bullet.isDeployed() == false) {
				deployedBulletList.remove(i);
				continue;
			}
			bullet.update(dt, playerShip, deployedShipList);
		}
		
		if (mercyState == true) {
			if (playerShip.getMercyState() == false) { mercyState = false; }
			return;
		}
			
		if (playerShip.getMercyState() == true) {
			mercyState = true;
			
			for (int i = 0; i <  deployedShipList.size(); i++) {
				deployedShipList.get(i).kill();
				numOfDestroyedShips--;
				numOfLaunchesRemaining++;
			}
			
			for (int i = 0; i <  deployedBulletList.size(); i++) {
				deployedBulletList.get(i).reset();
			}
			
			return;
		}
		
		updateEnemyAnimationFrames();
		
		if (numOfDestroyedShips >= totalNumOfLaunches) {
			
			enemyState = false;
			bossState = true;
			
			for (int i = 0; i <  deployedShipList.size(); i++) {
				deployedShipList.get(i).kill();
				deployedShipList.remove(i);
			}
			
		} else if (numOfLaunchesRemaining > 0) {
			launchShip(dt);
		}	
	}
	
	protected void updateBossState(double dt) {
		bossManager.update(dt);
	}
	
	protected void updateEnemyAnimationFrames () {
		if (deployedShipList.size() == 0) { return; }
		
		for (int i = 0; i < animatedEnemyType.length; i++) {
			String name = animatedEnemyType[i].name();
			graphicsManager.updateAnimationFrame(name);
		}
		
		if (deployedBulletList.size() > 0) {
			graphicsManager.updateAnimationFrame("EnemyBullet");	
		}
		
	}
	
	protected double width() {
		return game.width();
	}
	
	protected double height() {
		return game.height();
	}
	
	protected void changeColor(Color c) {
		graphicsManager.changeColor(c);
	}
	
	protected void drawText(double x, double y, String s, String font, int size) {
		graphicsManager.drawText(x, y, s, font, size);
	}
	
	protected void playAudio(String name) {
		audioManager.playAudio(name);
	}
	
	protected void displayAnimatedText(AnimatedText text, double x, double yBase, double yIncrement, String font, int fontSize) {
		graphicsManager.displayAnimatedText(text, x, yBase, yIncrement, font, fontSize);
	}
	
	protected int getTotalNumberOfRandomStages() {
		return StageManager.totalNumberOfRandomStages();
	}
}