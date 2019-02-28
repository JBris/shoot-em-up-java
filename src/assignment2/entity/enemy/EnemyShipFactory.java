package src.assignment2.entity.enemy;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class EnemyShipFactory implements InterfaceEnemyShipFactory {
	
	/***Properties***/
	
	protected InterfaceGame game;
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	
	protected InterfaceEnemyManager enemyManager;
	protected InterfaceExplosionManager explosionManager;

	/***Constructor***/
	
	public EnemyShipFactory(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
			
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
			
		this.enemyManager = enemyManager;
		this.explosionManager = explosionManager;
	}
	
	/***Methods***/
		
	public static InterfaceEnemyShipFactory createInstance(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager,  InterfaceEnemyManager enemyManager, InterfaceExplosionManager explosionManager) {
		return new EnemyShipFactory(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager);
	}
	
	public ArrayList<InterfaceEnemyShip> createEnemies(String enemyType, int numOfEnemies) {
		
		if (enemyType == EnemyType.UFO.name()) {
			return createUFOs(numOfEnemies);
		} else if (enemyType == EnemyType.Dove.name()) {
			return createDoves(numOfEnemies);
		} else if (enemyType == EnemyType.Paranoid.name()) {
			return createParanoids(numOfEnemies);
		} else if (enemyType == EnemyType.Ninja.name()) {
			return createNinjas(numOfEnemies);
		} else  if (enemyType == EnemyType.Ligher.name()) {
			return createLighers(numOfEnemies);
		} else if (enemyType == EnemyType.Turtle.name()) {
			return createTurtles(numOfEnemies);
		} else if (enemyType == EnemyType.Saboteur.name()) {
			return createSaboteurs(numOfEnemies);
		} else if (enemyType == EnemyType.SmallJelly.name()) {
			return createSmallJellies(numOfEnemies);
		} else if (enemyType == EnemyType.MediumJelly.name()) {
			return createMediumJellies(numOfEnemies);
		} else if (enemyType == EnemyType.BigJelly.name()) {
			return createBigJellies(numOfEnemies);
		} 
		
		return createLightnings(numOfEnemies);
	}
	
	public InterfaceEnemyBullet createBullet() {
		return new EnemyBullet(game, stateManager, graphicsManager, audioManager);
	}
	
	protected ArrayList<InterfaceEnemyShip> createUFOs(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();
		
		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new UFO(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}
	
	protected ArrayList<InterfaceEnemyShip> createDoves(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();
		
		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new Dove(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}
	
	protected ArrayList<InterfaceEnemyShip> createParanoids(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();
		
		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new Paranoid(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}
	
	protected ArrayList<InterfaceEnemyShip> createNinjas(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();
		
		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new Ninja(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}
	
	protected ArrayList<InterfaceEnemyShip> createLighers(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();

		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new Ligher(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}

	protected ArrayList<InterfaceEnemyShip> createTurtles(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();

		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new Turtle(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}
	
	protected ArrayList<InterfaceEnemyShip> createSaboteurs(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();
		
		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new Saboteur(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}
	
	protected ArrayList<InterfaceEnemyShip> createLightnings(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();
		
		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new Lightning(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}
	
	protected ArrayList<InterfaceEnemyShip> createSmallJellies(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();
		
		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new SmallJelly(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}
	
	protected ArrayList<InterfaceEnemyShip> createMediumJellies(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();
		
		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new MediumJelly(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}
	
	protected ArrayList<InterfaceEnemyShip> createBigJellies(int numOfEnemies) {
		ArrayList<InterfaceEnemyShip> shipList = new ArrayList<InterfaceEnemyShip>();
		
		for (int i = 0; i < numOfEnemies; i++) {
			shipList.add(new BigJelly(game, stateManager, graphicsManager, audioManager, enemyManager, explosionManager));
		}
		
		return shipList;
	}
}