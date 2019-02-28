package src.assignment2.entity.player;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment2.entity.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class BulletCollection extends AbstractDeployableEntityCollection implements InterfaceBulletCollection {
	
	/***Properties***/
		
	protected boolean isAttacking, isSpecialAttacking;
	protected String activeSpecialBulletName;
	
	protected InterfacePlayerBulletFactory bulletFactory;
	protected ArrayList<InterfaceBullet> basicBullets;
	protected ArrayList<InterfaceBullet> activeSpecialBullets;
	protected ArrayList<InterfaceBullet> deployedBullets;
	protected InterfaceDeploymentBehaviour activeDeploymentBehaviours;
	protected Map<String, InterfaceDeploymentBehaviour> deploymentBehaviours;
	protected Map<String, ArrayList<InterfaceBullet>> specialBullets;
	
	protected PlayerBulletType[] bulletTypes;
	
	protected PlayerControls playerControls;	
	protected InterfaceEntity subject;
	
	/***Constructor***/
	
	public BulletCollection(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, InterfacePlayerBulletFactory bulletFactory) {
		super(game, stateManager, graphicsManager, audioManager);
		this.bulletFactory = bulletFactory;
		subject = new BasicBullet(game, stateManager, graphicsManager, audioManager);
		bulletTypes = PlayerBulletType.values();
		playerControls = PlayerControls.Keyboard;
		initBullets();
		reset();
	}
	
	/***Getters and Setters***/
	
	public String getActiveSpecialBulletName() { return activeSpecialBulletName; }
	
	public boolean isAttacking() { return isAttacking; }
	public void setAttacking(boolean isAttacking) { this.isAttacking = isAttacking; }
	
	public boolean isSpecialAttacking() { return isSpecialAttacking; }
	public void setSpecialAttacking(boolean isAttacking) { isSpecialAttacking = isAttacking; }
	
	public void setProjectileSpeed(ProjectileSpeed projectileSpeed) { 
		setBaseVelocityModifier(projectileSpeed);
	}

	public InterfaceEntity getSubject() { return subject; }
	public void setSubject(InterfaceEntity s) { subject = s; }
	
	/***Methods***/
	
	public void createCollection(String type, int quantity) {
		if (deploymentBehaviours.get(type) == null) {
			throw new NullPointerException(String.format("Bullets of type %s do not exist.", type));
		}
		
		String basicType = PlayerBulletType.Basic.name();
		if (type == basicType) {
			basicBullets = bulletFactory.createCollection(basicBullets, basicType, quantity);
			return;
		}
		
		ArrayList<InterfaceBullet> createdSpecialBullets = specialBullets.get(type);
		if (createdSpecialBullets == null) {
			throw new NullPointerException(String.format("Special bullets of type %s do not exist.", type));
		}
		
		specialBullets.put(type, bulletFactory.createCollection(createdSpecialBullets, type, quantity));
	}
	
	public void switchSpecialBullets(String type) {
		
		ArrayList<InterfaceBullet> switchedSpecialBullets = specialBullets.get(type);
		InterfaceDeploymentBehaviour switchedDeploymentBehaviours = deploymentBehaviours.get(type);
		
		if (switchedSpecialBullets == null || switchedDeploymentBehaviours == null) {
			throw new NullPointerException(String.format("Special bullets of type %s do not exist.", type));
		}	
		
		activeSpecialBullets = switchedSpecialBullets;	
		activeDeploymentBehaviours = switchedDeploymentBehaviours;	
		activeSpecialBulletName = type;
		activeDeploymentBehaviours.reset();	
		for(int i = 0; i < activeSpecialBullets.size(); i++) {
			InterfaceBullet bullet = activeSpecialBullets.get(i);
			bullet.reset();
			bullet.resetRedeployments();
		}
		
		if (playerControls == PlayerControls.Mouse && (isAttacking == true)) {
			isAttacking = false;	
			isSpecialAttacking = true;
		}
	}
	
	public void launchAttack(double dt) {
		if (isAttacking == true) {
			launchSimpleAttack(dt);
		} else if (isSpecialAttacking == true) {
			launchSpecialAttack(dt);
		}
	}
		
	protected void launchSimpleAttack(double dt) {
		InterfaceDeploymentBehaviour attackingDeploymentBehaviours = deploymentBehaviours.get(PlayerBulletType.Basic.name());
		attackingDeploymentBehaviours.deployBullets(basicBullets, deployedBullets, subject.getXCoordinate(), subject.getYCoordinate(), dt, true);
	}
	
	protected void launchSpecialAttack(double dt) {
		if (activeSpecialBullets == null || activeDeploymentBehaviours == null) {
			return;
		}
		
		activeDeploymentBehaviours.deployBullets(activeSpecialBullets, deployedBullets, subject.getXCoordinate(), subject.getYCoordinate(), dt, true);
		if (activeDeploymentBehaviours.allDeployed() == true) { 
			activeSpecialBullets = null;
			activeDeploymentBehaviours = null;
			activeSpecialBulletName = null;
		}
	}
	
	public void fullReset(InterfaceGameOptions gameOptions) {
		playerControls = gameOptions.getPlayerControls();
		setBaseVelocityModifier(gameOptions.getProjectileSpeed());
	}
		
	public void reset() {
		isAttacking = false;
		clearSpecialAttack();

		for (int i = 0; i < bulletTypes.length; i++) { 
			deploymentBehaviours.get(bulletTypes[i].name()).reset();
		}
		
		for (int i = 0; i < basicBullets.size(); i++) {
			InterfaceBullet bullet = basicBullets.get(i);
			bullet.reset();
			bullet.resetRedeployments();
		}
		
		for (int i = 1; i < bulletTypes.length; i++) { 
			ArrayList<InterfaceBullet> resetSpecialBullets = specialBullets.get(bulletTypes[i].name());
			for (int j = 0; j < resetSpecialBullets.size(); j++) {
				InterfaceBullet bullet = resetSpecialBullets.get(j);
				bullet.reset();
				bullet.resetRedeployments();
			}
		}
		
		for (int i = 0; i < deployedBullets.size(); i++) {
			deployedBullets.remove(i);
		}
	}
	
	public void update(double dt) {
		launchAttack(dt);
		updateBullets(dt);
	}
	
	public void collide(InterfaceDeployableEntity enemyShip, InterfaceScore player) {
		for (int i = 0; i < deployedBullets.size(); i++) {
			InterfaceBullet bullet = deployedBullets.get(i);
			if (bullet.isDeployed() == false) { 
				deployedBullets.remove(i);
				continue; 
			}
			bullet.collide(enemyShip, player);
		}
	}
	
	public void renderUi() {
		if (activeSpecialBulletName == null) { return; }
		
		double gameWidth = width();
		double gameHeight = height();

		graphicsManager.drawImage(activeSpecialBulletName + "Icon", gameWidth * 0.8, gameHeight * 0.8, gameWidth * 0.1, gameHeight * 0.1);
	}
	
	public void renderBullets() {
		for (int i = 0; i < deployedBullets.size(); i++) {
			InterfaceBullet bullet = deployedBullets.get(i);
			if (bullet.isDeployed() == false) { 
				deployedBullets.remove(i);
				continue; 
			}
			bullet.render();
		}
	}
	
	public void updateBullets(double dt) {
		if (deployedBullets.size() <= 0) { return; }
		graphicsManager.updateAnimationFrame("BasicBullet");
		graphicsManager.updateAnimationFrame("SpecialBullet");
		for (int i = 0; i < deployedBullets.size(); i++) {
			InterfaceBullet bullet = deployedBullets.get(i);
			if (bullet.isDeployed() == false) { 
				deployedBullets.remove(i);
				continue; 
			}
			bullet.update(dt);
		}
	}
	
	public void disableAttack() {
		isAttacking = false;
		isSpecialAttacking = false;
	} 
	
	public void clearSpecialAttack() {
		activeSpecialBullets = null;
		activeDeploymentBehaviours = null;
		activeSpecialBulletName = null;
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode() ;
		
		if (keyCode == e.VK_SPACE) {
			isAttacking = true;
		} else if (keyCode == e.VK_X) {
			isSpecialAttacking = true;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode() ;
		
		if (keyCode == e.VK_SPACE) {
			isAttacking = false;
		}
		
		if (keyCode == e.VK_X) {
			isSpecialAttacking = false;
		}
	}
	
	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent e) {
		int button = e.getButton();
		
		if (button != e.BUTTON1) { return; }
		if (activeSpecialBulletName == null) { 
			isAttacking = true;
		} else {
			isSpecialAttacking = true;
		}
	}

	public void mouseReleased(MouseEvent e) {
		int button = e.getButton();
		
		if (button != e.BUTTON1) { return; }
		if (activeSpecialBulletName == null) { 
			isAttacking = false;
		} else {
			isSpecialAttacking = false;
		}
	}

	public void mouseMoved(MouseEvent e) {}
		
	public void mouseDragged(MouseEvent e) {}

	protected void addEntity() {
		basicBullets.add(new BasicBullet(game, stateManager, graphicsManager, audioManager));
	}
	
	protected void initBullets() {
		basicBullets = new ArrayList<InterfaceBullet>();
		deployedBullets = new ArrayList<InterfaceBullet>();
		specialBullets = bulletFactory.initSpecialBullets();
		deploymentBehaviours = bulletFactory.initDeploymentBehaviours();
	}
	
	protected void setBaseVelocityModifier(ProjectileSpeed projectileSpeed) {
		double baseVelocityModifier = 1;
		if (projectileSpeed == ProjectileSpeed.Slow) {
			baseVelocityModifier = 0.7;
		} else if (projectileSpeed == ProjectileSpeed.Fast) {
			baseVelocityModifier = 1.5;
		}

		for (int i = 0; i < bulletTypes.length; i++) { 
			deploymentBehaviours.get(bulletTypes[i].name()).setBaseVelocityModifier(baseVelocityModifier);
		}
	}
}