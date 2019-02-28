package src.assignment.service;

import java.util.*;
import java.lang.*;
import src.assignment.state.*;
import src.assignment.system.*;

public abstract class AbstractServiceContext implements InterfaceServiceContext {
	
	/***Properties***/
	
	protected InterfaceGame game;		
	
	protected InterfaceContainer container;
	
	/***Constructor***/
	
	public AbstractServiceContext(InterfaceGame game) {
		this.game = game;
		container = new Container();
		container.bind(InterfaceGame.class, this.game);
	}
	
	/***Methods***/

	public void bindServices() {
		container.bind(InterfaceStateManager.class, new StateManager(game, new BlankState()));
		container.bind(InterfaceGraphicsManager.class, new GraphicsManager(game));
		container.bind(InterfaceAudioManager.class, new AudioManager(game));
		container.bind(InterfaceFile.class, new File(game));
	}
	
	public InterfaceContainer getContainer() {
		return container;
	}
}