package src.assignment2.entity.player;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class PlayerBulletFactory implements InterfacePlayerBulletFactory {
	
	/***Properties***/
	
	protected InterfaceGame game;
	
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	
	/***Constructor***/
	
	public PlayerBulletFactory(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
	}
	
	/***Methods***/
	
	public Map<String, InterfaceDeploymentBehaviour> initDeploymentBehaviours() {
		Map<String, InterfaceDeploymentBehaviour> deploymentBehaviours = new HashMap<String, InterfaceDeploymentBehaviour>();
		
		deploymentBehaviours.put(PlayerBulletType.Basic.name(), new SingleFileDeploymentBehaviour(game, stateManager, graphicsManager, audioManager, 0.1, 1, -1, 0));
		deploymentBehaviours.put(PlayerBulletType.Shotgun.name(), new SpreadDeploymentBehaviour(game, stateManager, graphicsManager, audioManager, 0.2, 7, -1, width() * 0.4));
		deploymentBehaviours.put(PlayerBulletType.Tracker.name(), new SpreadDeploymentBehaviour(game, stateManager, graphicsManager, audioManager, 0.2, 3, -1, width() * 0.2));
		deploymentBehaviours.put(PlayerBulletType.Bouncer.name(), new SpreadDeploymentBehaviour(game, stateManager, graphicsManager, audioManager, 0.2, 9, -1, width() * 2));
		deploymentBehaviours.put(PlayerBulletType.Ring.name(), new RingDeploymentBehaviour(game, stateManager, graphicsManager, audioManager, 0.2, 10, -1, width()  * 0.4));
		
		return deploymentBehaviours;
	}
	
	public Map<String, ArrayList<InterfaceBullet>> initSpecialBullets() {
		Map<String, ArrayList<InterfaceBullet>> specialBullets = new HashMap<String, ArrayList<InterfaceBullet>>();
		specialBullets.put(PlayerBulletType.Shotgun.name(), new ArrayList<InterfaceBullet>());
		specialBullets.put(PlayerBulletType.Tracker.name(), new ArrayList<InterfaceBullet>());
		specialBullets.put(PlayerBulletType.Bouncer.name(), new ArrayList<InterfaceBullet>());
		specialBullets.put(PlayerBulletType.Ring.name(), new ArrayList<InterfaceBullet>());
		return specialBullets;
	}
	
	public ArrayList<InterfaceBullet> createCollection(ArrayList<InterfaceBullet> bullets, String type, int quantity) {
	
		if (type == PlayerBulletType.Basic.name()) {
			return createBasicBulletCollection(bullets, quantity);
		} else if (type == PlayerBulletType.Shotgun.name()) {
			return createShotgunBulletCollection(bullets, quantity);
		} else if (type == PlayerBulletType.Tracker.name()) {
			return createTrackerBulletCollection(bullets, quantity);
		} else if (type == PlayerBulletType.Bouncer.name()) {
			return createBouncerBulletCollection(bullets, quantity);
		} else if (type == PlayerBulletType.Ring.name()) {
			return createRingBulletCollection(bullets, quantity);
		}
		
		return createBasicBulletCollection(bullets, quantity);
	}
	
	protected ArrayList<InterfaceBullet> createBasicBulletCollection(ArrayList<InterfaceBullet> bullets, int quantity) {
		for (int i = 0; i < quantity; i++) {
			bullets.add(new BasicBullet(game, stateManager, graphicsManager, audioManager));
		}
		return bullets;
	}
	
	protected ArrayList<InterfaceBullet> createShotgunBulletCollection(ArrayList<InterfaceBullet> bullets, int quantity) {
		for (int i = 0; i < quantity; i++) {
			bullets.add(new ShotgunBullet(game, stateManager, graphicsManager, audioManager));
		}
		return bullets;
	}
	
	protected ArrayList<InterfaceBullet> createTrackerBulletCollection(ArrayList<InterfaceBullet> bullets, int quantity) {
		for (int i = 0; i < quantity; i++) {
			bullets.add(new TrackerBullet(game, stateManager, graphicsManager, audioManager));
		}
		return bullets;
	}
	
	protected ArrayList<InterfaceBullet> createBouncerBulletCollection(ArrayList<InterfaceBullet> bullets, int quantity) {
		for (int i = 0; i < quantity; i++) {
			bullets.add(new BouncerBullet(game, stateManager, graphicsManager, audioManager));
		}
		return bullets;
	}
	
	protected ArrayList<InterfaceBullet> createRingBulletCollection(ArrayList<InterfaceBullet> bullets, int quantity) {
		for (int i = 0; i < quantity; i++) {
			bullets.add(new RingBullet(game, stateManager, graphicsManager, audioManager));
		}
		return bullets;
	}
	
	protected double width() {
		return game.width();
	}
	
}