package src.assignment2.entity.player;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
import src.assignment.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment.state.*;
import src.assignment2.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class PlayerShip extends AbstractEntity implements InterfacePlayerShip {
	
	/***Properties***/
	
	protected int score;
	protected int lives;
	protected double invulnerabilityTimer, deathTimer;
	protected boolean arrived, gameOver, mercyState;
		
	protected ProjectileSpeed projectileSpeed;
	protected InterfaceBulletCollection ammo;
	protected InterfaceEngine engine;
	protected InterfaceShield shield; 
	
	/***Constructor***/
	
	public PlayerShip(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, InterfaceBulletCollection ammo, 
	InterfaceEngine engine, InterfaceShield shield) {
		super(game, stateManager, graphicsManager, audioManager);
		this.ammo = ammo;
		this.engine = engine;
		this.shield = shield;
		init();
	}
	
	/***Getters and Setters***/
	
	public boolean getMercyState() { return mercyState; }
	
	public boolean getGameOverTrigger() { return gameOver; }
	public void setGameOverTrigger(boolean t) { gameOver = t; }
	
	public int getScore() { return score; }
	public void setScore(int score) { this.score = score; }
		
	public boolean getUp() { return engine.getUp(); }
	public boolean getDown() { return engine.getDown(); }
	public boolean getLeft() { return engine.getLeft(); }
	public boolean getRight() { return engine.getRight(); }
	public boolean isBoosting() { return engine.isBoosting(); }

	public void setUp(boolean d) { engine.setUp(d); }
	public void setDown(boolean d) { engine.setDown(d); }
	public void setLeft(boolean d) { engine.setLeft(d); }
	public void setRight(boolean d) { engine.setRight(d); }
	public void setIsBoosting(boolean b) { engine.setIsBoosting(b); }
	
	public boolean isAttacking() { return ammo.isAttacking(); }
	public void setAttacking(boolean isAttacking) { ammo.setAttacking(isAttacking); }
	public boolean isSpecialAttacking() { return ammo.isSpecialAttacking(); }
	public void setSpecialAttacking(boolean isAttacking) { ammo.setSpecialAttacking(isAttacking); }
	
	public int getTotalShieldAmount() { return shield.getTotalShieldAmount(); }
	public void setTotalShieldAmount(int shieldAmount) { shield.setTotalShieldAmount(shieldAmount); }
	
	public int getCurrentShieldAmount() { return shield.getCurrentShieldAmount(); }
	public void setCurrentShieldAmount(int shieldAmount) { shield.setCurrentShieldAmount(shieldAmount); }
	public void resetCurrentShieldAmount() { shield.resetCurrentShieldAmount(); }
	
	public int getShieldRecharge() { return shield.getShieldRecharge(); }
	public void setShieldRecharge(int shieldRecharge) { shield.setShieldRecharge(shieldRecharge); }
	
	public int getTotalBoostAmount() { return engine.getTotalBoostAmount(); }
	public void setTotalBoostAmount(int boostAmount) {  engine.setTotalBoostAmount(boostAmount); }
	
	public int getCurrentBoostAmount() { return engine.getCurrentBoostAmount(); }
	public void setCurrentBoostAmount(int boostAmount) {  engine.setCurrentBoostAmount(boostAmount); }
	public void resetCurrentBoostAmount() { engine.resetCurrentBoostAmount(); }
	
	public int getBoostRecharge() { return engine.getBoostRecharge(); }
	public void setBoostRecharge(int boostRecharge) {  engine.setBoostRecharge(boostRecharge); }
	
	public int getLives() { return lives; }
	public void setLives(int lives) { this.lives = lives; }
	
	public ProjectileSpeed getProjectileSpeed() { return projectileSpeed; }
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed) { this.projectileSpeed = projectileSpeed; }
	
	public InterfaceEntity getSubject() { return null; }
	public void setSubject(InterfaceEntity s) { }
	
	public String getActiveSpecialBulletName() { return ammo.getActiveSpecialBulletName(); }
	
	/***Methods***/
	
	public void incrementScore() { 
		score++; 
	}
	
	public void incrementScoreByValue(int value) { 
		score += value; 
	} 

	public void update(double dt) {
		if (isDead == false) {
			updateRechargeTimers(dt);
			move(dt);
			move(allowInput);
			ammo.update(dt);
			engine.update(dt);
		} else {
			updateDeadState(dt);
		}
		updateAnimationFrame();
	}
	
	public void collide(InterfaceDeployableEntity enemyShip) {
		ammo.collide(enemyShip, this);
	}
	
	public void reset() {
		deflectAttack= false;
		arrived = false;
		invulnerabilityTimer = 0;
		deathTimer = 0;
		
		xCoordinate = width() / 2;
		yCoordinate = height() + height;
		
		xVelocity = 0;
		yVelocity = 0;
		
		ammo.reset();
		engine.reset();
		shield.reset();
	}
	
	public void fullReset(InterfaceGameOptions gameOptions) {
		score = 0;
		name = "";
		gameOver = false;
		allowInput = false;
		isDead = false;
		invulnerable = true;
		mercyState = false;
		
		lives =  gameOptions.getLives();
		projectileSpeed =  gameOptions.getProjectileSpeed();
		ammo.fullReset(gameOptions);
		engine.fullReset(gameOptions);
		shield.fullReset(gameOptions);
	}
	
	public void move(double dt) {
		engine.move(dt);
	}
	
	public void move(boolean allowInput) {
		engine.move(allowInput);
	}
	
	public void updateRechargeTimers(double dt) {
		shield.updateRechargeTimers(dt);
		engine.updateRechargeTimers(dt);
	}
	
	public void moveToCoordinates(double x, double y) {
		engine.moveToCoordinates(x, y);
	}
	
	public void disableBoost() {
		engine.disableBoost();
	}
	
	public void launchAttack(double dt) {
		ammo.launchAttack(dt);
	}
	
	public void switchSpecialBullets(String type) {
		ammo.switchSpecialBullets(type);
	}
	
	public void disableAttack() {
		ammo.disableAttack();
	}
	
	public void clearSpecialAttack() {
		ammo.clearSpecialAttack();
	}
	
	public void damage(int amount) {
		shield.damage(amount);
	}
	
	public void kill() {
		super.kill();
		reset();

		lives --;
		if (lives < 0) { gameOver = true; }
	
		allowInput = false;
		invulnerable = true;
		mercyState = true;
	}
	
	public void paintComponent() {
		renderUi();
		render();
		renderBullets();	
	}
	
	public void render() {
		playAnimation("PlayerMovement", xCoordinate - width, yCoordinate - height, width * 2, height * 2);		
		engine.render();
	}
	
	public void updateAnimationFrame() {
		updateAnimationFrame("PlayerMovement");
	}
	
	public void renderUi() {
		double gameWidth = width();
		double gameHeight = height();
		String font = "Arial";
		
		String renderedLives;
		if (lives >= 0) {
			renderedLives = Integer.toString(lives);
		} else {
			renderedLives = "0";
		}		

		changeColor(Color.WHITE);
		drawText(gameWidth / 2, gameHeight * 0.95,  Integer.toString(score), font, (int) Math.round(gameHeight * 0.03));
		drawImage("PlayerLives", gameWidth * 0.825, gameHeight * 0.915, gameWidth * 0.05, gameHeight * 0.05);
		drawText(gameWidth * 0.9, gameHeight * 0.95, renderedLives, font, (int) Math.round(gameHeight * 0.03));
		engine.renderUi();
		shield.renderUi();
		ammo.renderUi();
	}
	
	public void keyPressed(KeyEvent e) {
		engine.keyPressed(e);
		ammo.keyPressed(e);
		
		int keyCode = e.getKeyCode() ;
	}
	
	public void keyReleased(KeyEvent e) {
		engine.keyReleased(e);
		ammo.keyReleased(e);
	}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {
		ammo.mousePressed(e);	
		engine.mousePressed(e);		
	}

	public void mouseReleased(MouseEvent e) {
		ammo.mouseReleased(e);		
	}

	public void mouseMoved(MouseEvent e) {
		engine.mouseMoved(e);		
	}

	public void mouseDragged(MouseEvent e) {
		engine.mouseDragged(e);		
	}
	
	public void promptName() {
		while(name.length() == 0) {
			name = JOptionPane.showInputDialog("Please enter your name: ");
			if (name == null) {
				switchState(GameState.Menu.name());
				getCurrentState().reset(true);
				return;
			}
			name = name.trim();
		}
		
		game.measureTime();
	}
	
	protected void init() {
		ammo.setSubject(this);
		engine.setSubject(this);
		shield.setSubject(this);
		
		initAmmo();
		width = width() * 0.05;
		height = height() * 0.05;
		reset();
	}
	
	protected void updateDeadState(double dt) {
		if (gameOver == true) { return; }
		
		deathTimer += dt;
		if (deathTimer < 1) { return; }
		
		if (arrived == false) {
			double yTarget =  height() * 0.7;
			moveToCoordinates(xCoordinate, yTarget);
			move(dt);
	
			if (yCoordinate <= yTarget + height / 2) { 
				yVelocity = 0;
				yCoordinate = yTarget + height / 2;
				allowInput = true;
				arrived = true;
			}
			return;
		}
		
		move(dt);
		move(allowInput);
		engine.update(dt);
		
		invulnerabilityTimer += dt;
		if (invulnerabilityTimer > 1) {
			invulnerable = false;
			isDead = false;
			mercyState = false;
		}
	}
		
	protected void renderBullets() {
		ammo.renderBullets();
	}
	
	protected void initAmmo() {
		ammo.createCollection(PlayerBulletType.Basic.name(), 20);
		ammo.createCollection(PlayerBulletType.Shotgun.name(), 60);
		ammo.createCollection(PlayerBulletType.Tracker.name(), 60);
		ammo.createCollection(PlayerBulletType.Bouncer.name(), 60);
		ammo.createCollection(PlayerBulletType.Ring.name(), 60);
	}
}