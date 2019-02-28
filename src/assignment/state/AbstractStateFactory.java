package src.assignment.state;

import java.util.*;
import java.lang.*;
import src.assignment.system.*;
import src.assignment.service.*;

public abstract class AbstractStateFactory implements InterfaceStateFactory {
	
	/***Properties***/

	protected InterfaceGame game;
	protected InterfaceContainer container;
	
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	
	/***Constructor***/

	public AbstractStateFactory(InterfaceGame game, InterfaceContainer container, InterfaceStateManager stateManager, 
		InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		this.game = game;
		this.container  = container;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
	}

	/***Methods***/

	public abstract InterfaceState createInstance (String stateName);
	
	public InterfaceState createBlankState() {
		return new BlankState();
	}
}