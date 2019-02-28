package src.assignment2.service;

import java.util.*;
import java.lang.*;
import src.assignment2.entity.*;
import src.assignment2.entity.boss.*;
import src.assignment2.entity.enemy.*;
import src.assignment2.entity.item.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment2.state.*;
import src.assignment.service.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class ServiceContext extends AbstractServiceContext {

	/***Constructor***/
	
	public ServiceContext (InterfaceGame game) {
		super(game);
	}
	
	/***Methods***/
	
	public void bindServices() {
		super.bindServices();
		
		InterfaceGame game = container.getService(InterfaceGame.class);
		InterfaceStateManager stateManager = container.getService(InterfaceStateManager.class);
		InterfaceGraphicsManager graphicsManager = container.getService(InterfaceGraphicsManager.class);
		InterfaceAudioManager audioManager = container.getService(InterfaceAudioManager.class);
		InterfaceFile file = container.getService(InterfaceFile.class);

		InterfaceExplosionManager explosionManager = new ExplosionManager(game, stateManager, graphicsManager, audioManager);
		container.bind(InterfaceExplosionManager.class, explosionManager);
		
		InterfacePlayerShip playerShip = buildPlayerShip(game, stateManager, graphicsManager, audioManager);
		container.bind(InterfacePlayerShip.class, playerShip);

		InterfaceBossFactory bossFactory = new BossFactory(game, stateManager, graphicsManager, audioManager, explosionManager);
		InterfaceBossManager bossManager = new BossManager(game, stateManager, graphicsManager, audioManager, bossFactory, playerShip, explosionManager);
		container.bind(InterfaceBossManager.class, bossManager);

		InterfaceEnemyManager enemyManager = new EnemyManager(game, stateManager, graphicsManager, audioManager, playerShip, bossManager, explosionManager);
		container.bind(InterfaceEnemyManager.class, enemyManager);
		
		InterfaceItemManager itemManager = new ItemManager(game, stateManager, graphicsManager, audioManager, playerShip, enemyManager);
		container.bind(InterfaceItemManager.class, itemManager);
		
		InterfaceStageManager stageManager = new StageManager(game, stateManager, graphicsManager, audioManager, playerShip, enemyManager, itemManager, explosionManager);
		container.bind(InterfaceStageManager.class, stageManager);

		InterfaceGameOptions gameOptions =	 new GameOptions();
		container.bind(InterfaceGameOptions.class, gameOptions);
		
		InterfaceLeaderboard leaderboard = new Leaderboard(gameOptions, file);
		container.bind(InterfaceLeaderboard.class, leaderboard);
		
		InterfaceStateFactory stateFactory = new StateFactory(game, container, stateManager, graphicsManager, audioManager, gameOptions, leaderboard, playerShip, stageManager, explosionManager);
		container.bind(InterfaceStateFactory.class, stateFactory);
		
	}
	
	protected InterfacePlayerShip buildPlayerShip(InterfaceGame game, InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		InterfaceExplosionManager explosionManager = container.getService(InterfaceExplosionManager.class);
		
		InterfacePlayerBulletFactory playerBulletFactory = new PlayerBulletFactory (game, stateManager, graphicsManager, audioManager);
		InterfaceBulletCollection ammo = new BulletCollection(game, stateManager, graphicsManager, audioManager, playerBulletFactory);
		InterfaceEngine engine = new Engine(game, stateManager, graphicsManager, audioManager);
		InterfaceShield shield = new Shield(game, stateManager, graphicsManager, audioManager, explosionManager);
		
		return new PlayerShip(game, stateManager, graphicsManager, audioManager, ammo, engine, shield);
	}
	
}