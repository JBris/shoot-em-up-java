package src.assignment2.state;

import java.util.*;
import java.lang.*;
import src.assignment2.entity.player.*;
import src.assignment.service.*;
import src.assignment.state.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public class StateFactory extends AbstractStateFactory {
	
	/***Properties***/
	
	protected InterfaceGameOptions options;
	protected InterfaceLeaderboard leaderboard;
	protected InterfacePlayerShip playerShip;
	protected InterfaceStageManager stageManager;
	protected InterfaceExplosionManager explosionManager;

	/***Constructor***/
	
	public StateFactory(InterfaceGame game, InterfaceContainer container, InterfaceStateManager stateManager, 
		InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager, InterfaceGameOptions options, 
		InterfaceLeaderboard leaderboard, InterfacePlayerShip playerShip, InterfaceStageManager stageManager,
		InterfaceExplosionManager explosionManager) {
		super(game, container, stateManager, graphicsManager, audioManager);
		this.options = options;
		this.leaderboard = leaderboard;
		this.playerShip = playerShip;
		this.stageManager = stageManager;
		this.explosionManager = explosionManager;
	}

	/***Methods***/

	public InterfaceState createInstance (String stateName) {
		
		if (stateName == GameState.Menu.name()) {
			return createMenuState();	
		} else if (stateName == GameState.Leaderboard.name()) {
			return createLeaderboardState();
		} else if (stateName == GameState.Options.name()) {
			return createOptionsState();
		} else if (stateName == GameState.Controls.name()) {
			return createControlsState();
		} else if (stateName == GameState.Play.name()) {
			return createPlayState();
		} else if (stateName == GameState.Intro.name()) {
			return createIntroState();
		}
		
		return createBlankState();
	}
	
	protected InterfaceState createMenuState() {
		return new MenuState(game, stateManager, graphicsManager, audioManager);
	}
	
	protected InterfaceState createLeaderboardState() {
		return new LeaderboardState(game, stateManager, graphicsManager, audioManager, leaderboard);
	}
	
	protected InterfaceState createOptionsState() {
		return new OptionsState(game, stateManager, graphicsManager, audioManager, options);
	}
	
	protected InterfaceState createControlsState() {
		return new ControlsState(game, stateManager, graphicsManager, audioManager);
	}
	
	protected InterfaceState createPlayState() {
		return new PlayState(game, stateManager, graphicsManager, audioManager, options, leaderboard, playerShip, stageManager, explosionManager);
	}
	
	protected InterfaceState createIntroState() {
		return new IntroState(game, stateManager, graphicsManager, audioManager);
	}
}