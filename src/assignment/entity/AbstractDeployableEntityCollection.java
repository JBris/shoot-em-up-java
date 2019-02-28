package src.assignment.entity;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment.state.*;
import src.assignment.system.*;

public abstract class AbstractDeployableEntityCollection implements InterfaceDeployableEntityCollection {
	
	/***Properties***/

	protected InterfaceGame game;
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	
	/***Constructor***/
	
	public AbstractDeployableEntityCollection(InterfaceGame game,  InterfaceStateManager stateManager, 
	InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;
	}
	
	/***Methods***/

	public void createCollection(int quantity) {
		for (int i = 0; i < quantity; i++) {
			addEntity();
		}
	}

	public void createCollection(String type, int quantity) {}
	protected abstract void addEntity();	
	
	protected void playSound(String soundName) {
		audioManager.playAudio(soundName);
	}
	
	protected double height() {
		return game.height();
	}
	
	protected double width() {
		return game.width();
	}
}