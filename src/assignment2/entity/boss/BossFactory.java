package src.assignment2.entity.boss;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class BossFactory implements InterfaceBossFactory {
	
	/***Properties***/
	
	protected InterfaceGame game;
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	
	protected InterfaceExplosionManager explosionManager;

	/***Constructor***/
	
	public BossFactory(InterfaceGame game,  InterfaceStateManager stateManager, InterfaceGraphicsManager graphicsManager, 
		InterfaceAudioManager audioManager, InterfaceExplosionManager explosionManager) {
			
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
			
		this.explosionManager = explosionManager;
	}
	
	/***Methods***/
	
	public ArrayList<InterfaceBoss> createStageBosses() {
		ArrayList<InterfaceBoss> stageBosses = new ArrayList<InterfaceBoss>();
		
		stageBosses.add(new Odin(game, stateManager, graphicsManager, audioManager, explosionManager));
		stageBosses.add(new Zeus(game, stateManager, graphicsManager, audioManager, explosionManager));
		stageBosses.add(new Indra(game, stateManager, graphicsManager, audioManager, explosionManager));
		stageBosses.add(new Amun(game, stateManager, graphicsManager, audioManager, explosionManager));
		stageBosses.add(new Shangdi(game, stateManager, graphicsManager, audioManager, explosionManager));

		return stageBosses;
	}
	
	public InterfaceBoss createFinalBoss() {
		return new Unknown(game, stateManager, graphicsManager, audioManager, explosionManager);
	}
	
	public InterfaceBossBullet createBullet() {
		return new BossBullet(game, stateManager, graphicsManager, audioManager);
	}
}