package src.assignment2.entity.item;

import java.awt.*;
import java.lang.*;
import java.util.*;
import src.assignment2.entity.player.*;
import src.assignment2.entity.enemy.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class ItemManager implements InterfaceItemManager {
	
	/***Properties***/
	
	protected InterfaceGame game;
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	protected InterfacePlayerShip playerShip;
	protected InterfaceEnemyManager enemyManager;
	
	protected boolean mercyState;
	protected double weaponsDropTimer, weaponsRandomModifier, weaponsTimeModifier;
	protected double pointsDropTimer, pointsRandomModifier, pointsTimeModifier;
	protected double deflectDropTimer, deflectRandomModifier, deflectTimeModifier;
	protected double baseVelocityModifier, baseRandomModifier;
	protected int pointsNextDropTime, weaponsNextDropTime, deflectNextDropTime, scoreMultiplier;
	
	protected String lastSpecialWeapon;
	protected ArrayList<String> specialWeaponNames;
	protected Map<String, InterfaceItem> specialWeaponsMap;
	
	protected ArrayList<InterfaceItem> pointDrops;
	protected ArrayList<InterfaceItem> deflectDrops;
	protected ArrayList<InterfaceItem> deployedItems;

	protected PlayerBulletType[] bulletTypes;
	
	/***Constructor***/
	
	public ItemManager(InterfaceGame game, InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
	InterfaceAudioManager audioManager, InterfacePlayerShip playerShip, InterfaceEnemyManager enemyManager) {
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
		this.playerShip = playerShip;
		this.enemyManager = enemyManager;
		init();
	}
	
	/***Getters and Setters***/
	
	public int getScoreMultiplier() { return scoreMultiplier; }
	public void setScoreMultiplier(int v) { scoreMultiplier = v; }
	public void incrementScoreMultiplier() { scoreMultiplier++; }
	
	public double getBaseRandomModifier() { return baseRandomModifier;}
	public void setBaseRandomModifier(double m) { baseRandomModifier = m; }
	public void increaseBaseRandomModifierByValue(double v) { baseRandomModifier += v; }
	
	public void setPointsRandomModifier(double m) { pointsRandomModifier = m; }
	public void increasePointsRandomModifierByValue(double v) { pointsRandomModifier += v; }
	
	public void setWeaponsRandomModifier(double m) { weaponsRandomModifier = m; }
	public void increaseWeaponsRandomModifierByValue(double v) { weaponsRandomModifier += v; }
	
	/***Methods***/

	public void reset() {
		if (lastSpecialWeapon != "") { specialWeaponNames.add(lastSpecialWeapon); }
		Collections.shuffle(specialWeaponNames);
		lastSpecialWeapon = "";
		
		mercyState = false;
		
		pointsDropTimer = 0;
		weaponsDropTimer = 0;
		deflectDropTimer =  0;
		
		pointsNextDropTime = 8;
		weaponsNextDropTime = 6; 
		deflectNextDropTime = 10;
		
		pointsRandomModifier = 0;
		weaponsRandomModifier = 1;
		deflectRandomModifier = 0;
		
		pointsTimeModifier = 0;
		weaponsTimeModifier = 0;
		deflectTimeModifier = 0;
		
		for (int i = 0; i < pointDrops.size(); i++) {
			pointDrops.get(i).reset();
		}

		for (int i = 0; i < deflectDrops.size(); i++) {
			deflectDrops.get(i).reset();
		}

		for (int i = 0; i < deployedItems.size(); i++) {
			deployedItems.get(i).reset();
		}
		
		for (int i = 0; i < deployedItems.size(); i++) {
			deployedItems.remove(i);
		}
	}
	
	public void fullReset() {
		reset();
		setScoreMultiplier(1);
		setBaseRandomModifier(0);
	}
	
	public void update(double dt) {
		updateAnimationFrames();
		updateDeployedItems(dt);
		
		if (mercyState == true) {
			if (playerShip.getMercyState() == false) {
				mercyState = false;
				weaponsDropTimer = 3;
				weaponsTimeModifier = 0;
				weaponsRandomModifier  = 1;
			}	
			return;
		}

		deployNewItems(dt);
		
		if (playerShip.getMercyState()  == true) { mercyState = true; }
	}
	
	public void paintComponent() {
		renderItems();
	}
	
	protected void init() {
		baseVelocityModifier = 1;
		scoreMultiplier = 1;
		baseRandomModifier = 0;
		lastSpecialWeapon = "";
		
		bulletTypes = PlayerBulletType.values();
		
		specialWeaponNames = new ArrayList<String> ();
		specialWeaponsMap = new HashMap<String, InterfaceItem>();
		pointDrops = new ArrayList<InterfaceItem>();
		deflectDrops = new ArrayList<InterfaceItem>();
		deployedItems = new ArrayList<InterfaceItem>();
		
		for(int i = 1; i < bulletTypes.length; i++) {
			String weaponName = bulletTypes[i].name();
			specialWeaponNames.add(weaponName);
			InterfaceItem item = createNewItem(weaponName);
			item.setIsWorthPoints(false);
			specialWeaponsMap.put(weaponName, item);
		}
		
		for (int i = 0; i < 3; i++) {
			pointDrops.add(createNewItem("Points"));
		}	
		
		for (int i = 0; i < 3; i++) {
			InterfaceItem item = createNewItem("Deflect");
			item.setIsWorthPoints(false);
			deflectDrops.add(item);
		}	
		
		fullReset();
	}
	
	public void setItemSpeed(ProjectileSpeed projectileSpeed) {
		if (projectileSpeed == ProjectileSpeed.Slow) {
			baseVelocityModifier = 0.8;
		} else if (projectileSpeed == ProjectileSpeed.Fast) {
			baseVelocityModifier = 1.2;
		} else {
			baseVelocityModifier = 1;	
		}
	}
	
	protected InterfaceItem createNewItem(String name) {
		return new Item(game, stateManager, graphicsManager, audioManager, name);
	}
	
	protected void updateAnimationFrames() {
		if (deployedItems.size() <= 0) { return; }
		graphicsManager.updateAnimationFrame("PointsDrop");
		graphicsManager.updateAnimationFrame("DeflectDrop");
	
		if (lastSpecialWeapon != "") { graphicsManager.updateAnimationFrame(lastSpecialWeapon + "Drop"); }
	}
	
	protected void deployNewItems(double dt) {
		pointsDropTimer += dt;
		weaponsDropTimer += dt;
		deflectDropTimer += dt;
		
		if (pointsDropTimer > pointsNextDropTime) {
			double randomNum = Math.random() + pointsRandomModifier - pointsTimeModifier;

			if (randomNum > 0.9) {
				pointsDropTimer -= 2 * pointsNextDropTime;
				pointsRandomModifier = -1;
				pointsTimeModifier += 0.1;
				weaponsRandomModifier -= 0.05;
				deflectRandomModifier -= 0.6;
				dropPoints(dt);
			} else {
				pointsDropTimer -= pointsNextDropTime;
				pointsRandomModifier += 0.65 + baseRandomModifier;
			}
		}
		

		if (weaponsDropTimer > weaponsNextDropTime) {
			double randomNum = Math.random() + weaponsRandomModifier - weaponsTimeModifier;

			if (randomNum > 0.9) {
				weaponsDropTimer -= 2 * weaponsNextDropTime;
				weaponsRandomModifier = -1;
				weaponsTimeModifier += 0.05;
				pointsRandomModifier -= 0.6;
				deflectRandomModifier -= 0.6;
				dropWeapon(dt);
			} else {
				weaponsDropTimer -= weaponsNextDropTime;
				weaponsRandomModifier += 0.8 + baseRandomModifier;
			}
		}
		
		if (deflectDropTimer > deflectNextDropTime) {
			double randomNum = Math.random() + deflectRandomModifier  - deflectTimeModifier;

			if (randomNum > 0.9) {
				deflectDropTimer -= 2 * deflectNextDropTime;
				deflectRandomModifier = -1;
				deflectTimeModifier += 0.2;
				pointsRandomModifier -= 0.6;
				weaponsRandomModifier -= 0.05;
				dropDeflect(dt);
			} else {
				deflectDropTimer -= deflectNextDropTime;
				deflectRandomModifier += 0.65 + baseRandomModifier;
			}
		}
	}
	
	protected void dropPoints(double dt) {
		double y = height() * -0.1;
		double x = getX();
	
		for (int i = 0; i < pointDrops.size(); i++) {
			InterfaceItem item = pointDrops.get(i);
			if (item.isDeployed() == true) { continue; }
				
			item.setDeployed(true);
			item.setXCoordinate(x);
			item.setYCoordinate(y);
			item.setYVelocity(item.getBaseVelocity() * baseVelocityModifier);
			item.move(dt);
			deployedItems.add(item);
			break;
		}
	}
	
	protected void dropWeapon(double dt) {
		double y = height() * -0.1;
		double x = getX();
		
		for (int i = 0; i < specialWeaponNames.size(); i++) {
			String nextSpecialWeapon = specialWeaponNames.get(i);
			if (playerShip.getActiveSpecialBulletName() == nextSpecialWeapon) { continue; }
			
			InterfaceItem item = specialWeaponsMap.get(nextSpecialWeapon);
			if (item.isDeployed() == true) { continue; }
			
			if (lastSpecialWeapon != "") { specialWeaponNames.add(lastSpecialWeapon); }
			lastSpecialWeapon = nextSpecialWeapon;
			specialWeaponNames.remove(i);
			Collections.shuffle(specialWeaponNames);
				
			item.setDeployed(true);
			item.setXCoordinate(x);
			item.setYCoordinate(y);
			item.setYVelocity(item.getBaseVelocity() * baseVelocityModifier);
			item.move(dt);
			deployedItems.add(item);
			break;
		}
	}
	
	protected void dropDeflect(double dt) {
		double y = height() * -0.1;
		double x = getX();
	
		for (int i = 0; i < deflectDrops.size(); i++) {
			InterfaceItem item = deflectDrops.get(i);
			if (item.isDeployed() == true) { continue; }
				
			item.setDeployed(true);
			item.setXCoordinate(x);
			item.setYCoordinate(y);
			item.setDeflectAttack(true);
			item.setYVelocity(item.getBaseVelocity() * baseVelocityModifier);
			item.move(dt);
			deployedItems.add(item);
			break;
		}
	}
	
	protected double getX() {
		Random random = new Random();
		return width() * (0.1 + ( random.nextDouble() * (0.9 - 0.1) ));
	}
	
	protected void updateDeployedItems(double dt) {
		for (int i = 0; i < deployedItems.size(); i++) {
			InterfaceItem item = deployedItems.get(i);
			if (item.isDeployed() == false) {
				deployedItems.remove(i);
				continue;
			}
			item.collide(playerShip, enemyManager, scoreMultiplier);
			item.update(dt);
		}
	}
	
	protected void renderItems() {
		for (int i = 0; i < deployedItems.size(); i++) {
			InterfaceItem item = deployedItems.get(i);
			if (item.isDeployed() == false) {
				deployedItems.remove(i);
				continue;
			}
			item.render();
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
}