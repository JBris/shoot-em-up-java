package src.assignment2.entity.boss;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class BossManager implements InterfaceBossManager {
	
	/***Properties***/
	
	protected InterfaceGame game;
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	
	protected InterfaceBossFactory bossFactory;
	protected InterfacePlayerShip playerShip;
	protected InterfaceExplosionManager explosionManager;
	
	protected boolean victoryState, bossIntroState, prepBossIntroState, mercyState;
	protected boolean dangerTextFinished;
	protected int gameLoop, stage;
	protected double bossDeploymentTimer;

	protected ArrayList<InterfaceBoss> stageBosses;
	protected ArrayList<InterfaceBoss> foughtStageBosses;
	
	protected ArrayList<InterfaceBossBullet> bulletList;
	protected ArrayList<InterfaceBossBullet> deployedBulletList;
	
	protected InterfaceBoss stageBoss;
	protected InterfaceBoss finalBoss;

	protected AnimatedText dangerText;

	/***Constructor***/
	
	public BossManager(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceBossFactory bossFactory, InterfacePlayerShip playerShip, InterfaceExplosionManager explosionManager) {
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
			
		this.bossFactory = bossFactory;
		this.playerShip = playerShip;
		this.explosionManager = explosionManager;
			
		init();
	}
	
	/***Getters and Setters***/
			
	public boolean getVictoryState() { return victoryState; }

	public void setGameLoop(int l) { gameLoop = l;  }
	public void setStage(int s) { stage = s; }
	
	/***Methods***/
	
	public void fullReset() {
		stage = 1;
		gameLoop = 0;
		
		rebuildStageBosses();
		finalBoss.reset();
		reset();
	}
	
	public void reset() {
		victoryState = false;
		bossIntroState = true;
		prepBossIntroState = true;
		dangerTextFinished = false;
		mercyState =  false;		
		bossDeploymentTimer = 0;
		
		for (int i = 0; i <  bulletList.size(); i++) {
			bulletList.get(i).reset();
		}
		
		for (int i = 0; i <  deployedBulletList.size(); i++) {
			deployedBulletList.remove(i);
		}
	}
	
	public void rebuildBosses() {
		stageBoss = null;
		rebuildStageBosses();
	}
	
	public void getNextBoss() {
		
		if (stage > getTotalNumberOfRandomStages()) {
			stageBoss = finalBoss;
			rebuildStageBosses();
		} else {
			stageBoss = stageBosses.get(0);
			foughtStageBosses.add(stageBoss);
			stageBosses.remove(0);
		}
		
		stageBoss.setStage(stage);
		stageBoss.setGameLoop(gameLoop);
		setBossText();		
	}
	
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed) {
		for (int i = 0; i < stageBosses.size(); i++) {
			stageBosses.get(i).setProjectileSpeed(projectileSpeed);
		}
	}
	
	public void setAttackRate(EnemyAttackRate enemyAttackRate) {
		for (int i = 0; i < stageBosses.size(); i++) {
			stageBosses.get(i).setAttackRate(enemyAttackRate);
		}
	}
	
	public void update(double dt) {
		if (bossIntroState == false) {
			updateBossState(dt);			
			return;
		} 
		
		if (prepBossIntroState == false) {
			updateIntroState(dt);
		} else { 
			playerShip.setInvulnerable(true);
			playerShip.disableBoost();
			playerShip.disableAttack();
			playAudio("boss");
			prepBossIntroState = false;
		}
	}
	
	public void createBullets(int numOfBullets) {
		for (int i = 0; i < numOfBullets; i++) {
			bulletList.add(bossFactory.createBullet());
		}
	}
	
	public void deployBullet(InterfaceBossBullet bullet) {
		deployedBulletList.add(bullet);	
	}
	
	public void deflectAllBullets() {
		for (int i = 0; i <  deployedBulletList.size(); i++) {
			InterfaceBossBullet bullet = deployedBulletList.get(i);
			if (bullet.isDeployed() == false) {
				deployedBulletList.remove(i);
				continue;
			}
			bullet.deflect();
		}	
	}
	
	public void paintComponent() {
		if (bossIntroState == true) {
			changeColor(Color.RED); 
			displayAnimatedText(dangerText, game.width() * 0.4, 0.15, 0.2, "Arial", (int) (game.height() * 0.08));		
		} else {
			double gameHeight = height();
			changeColor(Color.WHITE);
			drawText(width() * 0.45, gameHeight * 0.075,  Integer.toString(stageBoss.getCurrentShieldAmount()), "Arial", (int) Math.round(gameHeight * 0.04));
			renderBullets();
		}
		stageBoss.render();
	}
		
	protected void init() {
		foughtStageBosses = new ArrayList<InterfaceBoss>();
		bulletList = new ArrayList<InterfaceBossBullet>();
		deployedBulletList = new ArrayList<InterfaceBossBullet>();
		
		stageBosses = bossFactory.createStageBosses();
		finalBoss = bossFactory.createFinalBoss();
	}
	
	protected void rebuildStageBosses() {
		for (int i = 0; i < foughtStageBosses.size(); i++) {
			stageBosses.add(foughtStageBosses.get(i));
		}
		
		for (int i = 0; i < foughtStageBosses.size(); i++) {
			foughtStageBosses.remove(i);
		}				

		for (int i = 0; i < stageBosses.size(); i++) {
			stageBosses.get(i).reset();
		}
		
		shuffleStageBosses();
	}
	
	protected void shuffleStageBosses() {
		Collections.shuffle(stageBosses);
	}
	
	protected void updateIntroState(double dt) {
		if (dangerTextFinished == false) {
			dangerText.update(dt);
			if (dangerText.isDisplayTimeReached(true) == false) { return; }
			
			dangerText.transferTextChars();
			if (dangerText.isCharTransferComplete()  == false) { return; }
			
			bossDeploymentTimer += dt;
			if (bossDeploymentTimer < 0.35) { return; }
			
			dangerTextFinished = true;
			stageBoss.launch();
			
		}
		
		stageBoss.update(dt);
		if (stageBoss.getIntroState()  == true) { return; }
		bossIntroState = false;
		playerShip.setInvulnerable(false);
	}
	
	protected void updateBossState(double dt) {
		graphicsManager.updateAnimationFrame(stageBoss.getSkin());
		stageBoss.update(dt, playerShip, bulletList, this);
			
		if (deployedBulletList.size() > 0) {
			graphicsManager.updateAnimationFrame("BossBullet");	
		}
						
		for (int i = 0; i <  deployedBulletList.size(); i++) {
			InterfaceBossBullet bullet = deployedBulletList.get(i);
			if (bullet.isDeployed() == false) {
				deployedBulletList.remove(i);
				continue;
			}
			bullet.update(dt, playerShip, stageBoss);
			stageBoss.manipulateBullet(dt, playerShip, bullet);
		}
		
		if (stageBoss.isDeployed() == false) {
			victoryState = true;			
		}
		
		if (mercyState == true) {
			if (playerShip.getMercyState() == false) { mercyState = false; }
			return;
		}
			
		if (playerShip.getMercyState() == true) {
			mercyState = true;			
			for (int i = 0; i <  deployedBulletList.size(); i++) {
				deployedBulletList.get(i).reset();
			}
		}
		
	}
	
	protected void renderBullets() {
		for (int i = 0; i < deployedBulletList.size(); i++) {
			InterfaceBossBullet bullet = deployedBulletList.get(i);
			if (bullet.isDeployed() == false) {
				continue;
			}
			bullet.render();
		}		

	}
	
	protected void setBossText() {
		String[] rawDangerText = new String[1];
		rawDangerText[0] = "Danger";
		
		graphicsManager.registerText("Danger", rawDangerText,  0.075);
		dangerText = graphicsManager.getText("Danger");
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