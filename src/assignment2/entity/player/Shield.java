package src.assignment2.entity.player;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
import src.assignment.entity.*;
import src.assignment.state.*;
import src.assignment2.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class Shield extends AbstractEntity implements InterfaceShield {
	
	/***Properties***/
	
	protected int totalShieldAmount, currentShieldAmount, shieldRecharge;
	protected double shieldTimer;

	protected InterfaceExplosionManager explosionManager;
	protected InterfaceEntity subject;

	/***Constructor***/
	
	public Shield(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, InterfaceExplosionManager explosionManager) {
		super(game, stateManager, graphicsManager, audioManager);
		this.explosionManager = explosionManager;
		subject = this;
	}
	
	/***Getters and Setters***/
	
	public int getTotalShieldAmount() { return totalShieldAmount; }
	public void setTotalShieldAmount(int shieldAmount) { 
		if (shieldAmount < 0) { shieldAmount = 0; }
		totalShieldAmount = shieldAmount; 
		if (currentShieldAmount > totalShieldAmount) { resetCurrentShieldAmount(); }
	}
	
	public int getCurrentShieldAmount() { return currentShieldAmount; }
	public void setCurrentShieldAmount(int shieldAmount) {  }
	public void resetCurrentShieldAmount() { currentShieldAmount = totalShieldAmount; }
	
	public int getShieldRecharge() { return shieldRecharge; }
	public void setShieldRecharge(int shieldRecharge) { 
		if (shieldRecharge < 0) { shieldRecharge = 0; }
		this.shieldRecharge = shieldRecharge; 
	}
	
	public InterfaceEntity getSubject() { return subject; }
	public void setSubject(InterfaceEntity s) { subject = s; }
	
	/***Methods***/
	
	public void reset() {
		currentShieldAmount = totalShieldAmount;
		shieldTimer = 0;
	}
	
	public void fullReset(InterfaceGameOptions gameOptions) {
		totalShieldAmount =  gameOptions.getShieldAmount();
		shieldRecharge =  gameOptions.getShieldRecharge();
	}
	
	public void updateRechargeTimers(double dt) {
		if (currentShieldAmount < totalShieldAmount) {
			shieldTimer += dt;
			if (shieldTimer >= shieldRecharge) {
				shieldTimer -= shieldRecharge;
				currentShieldAmount++;
			}
		}
	}
	
	public void renderUi() {
		double gameWidth = width();
		double gameHeight = height();
		
		int i = 0;
		for (int j = 0; j < currentShieldAmount; j ++) {
			drawImage("Shield", gameWidth * 0.1 + gameWidth * 0.05 * i, gameHeight  * 0.8, gameWidth * 0.05, gameHeight * 0.05);
			i++;
		}
		
		for (int j = i; j < totalShieldAmount; j++) {
			drawImage("Empty", gameWidth * 0.1 + gameWidth * 0.05 * j, gameHeight  * 0.8, gameWidth * 0.05, gameHeight * 0.05);
		}
	}
	
	public void damage(int amount) {
		currentShieldAmount -= amount;
		
		if (currentShieldAmount >= 0) {
			if (currentShieldAmount == 0) { playSound("boss"); }
			explosionManager.createExplosion(ExplosionTypes.Shield.name(), subject);
		} else {
			explosionManager.createRandomExplosionsOnTarget(ExplosionTypes.Normal.name(), subject, 3);
			subject.kill();
		}
	}
}