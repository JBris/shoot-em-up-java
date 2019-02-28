package src.assignment2.system;

import java.awt.*;
import java.lang.*;
import java.util.*;
import src.assignment.entity.*;
import src.assignment2.entity.item.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class ExplosionManager implements InterfaceExplosionManager {
	
	/***Properties***/
	
	protected InterfaceGame game;
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	
	protected double explosionAudioTimer;
	
	protected Map< String, ArrayList<Explosion> > explosionMap;
	protected ArrayList<Explosion> deployedExplosionList;

	/***Constructor***/
	
	public ExplosionManager(InterfaceGame game, InterfaceStateManager stateManager, 
		InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager) {
		this.game = game;
		this.stateManager = stateManager;
		this.graphicsManager = graphicsManager;
		this.audioManager = audioManager;		
		init();
	}
	
	/***Getters and Setters***/
	
	
	/***Methods***/

	public void reset() {
		explosionAudioTimer = 0;
		
		ExplosionTypes[] explosionTypes = ExplosionTypes.values();
		for (int i = 0; i <  explosionTypes.length; i++) {
			ArrayList<Explosion> explosionList = getExplosionList(explosionTypes[i].name());
			for (int j = 0; j < explosionList.size(); j++) {
				explosionList.get(j).reset();
			}	
		}
		
		for (int i = 0; i< deployedExplosionList.size(); i++) {
			deployedExplosionList.remove(i);
		}
	}
	
	
	public void paintComponent() {
		for (int i = 0; i < deployedExplosionList.size(); i++) {
			Explosion explosion = deployedExplosionList.get(i);
			if (explosion.explosionFinished == true) {
				deployedExplosionList.remove(i);
				continue;
			}
			explosion.paintComponent();
		}
	}
	
	public void update(double dt) {
		for (int i = 0; i < deployedExplosionList.size(); i++) {
			Explosion explosion = deployedExplosionList.get(i);
			if (explosion.explosionFinished == true) {
				deployedExplosionList.remove(i);
				continue;
			}
			explosion.update(dt);
		}
	}
	
	public void setExplosionAnimation(Animation explosion) {
		ArrayList<Explosion> explosionList = getExplosionList(explosion.name);
		for (int i = 0; i < explosionList.size(); i++) {
			explosionList.get(i).setExplosionAnimation(explosion);
		}
	}
	
	public void createExplosion(String type, double x, double y, double dt) {
		ArrayList<Explosion> explosionList = getExplosionList(type);
		for (int i = 0; i < explosionList.size(); i++) {
			Explosion explosion = explosionList.get(i);
			if (explosion.exploding == true) {  continue; }
			
			explosionAudioTimer += dt;
			double audioTime = 0.02;
			if (explosionAudioTimer > audioTime) {
				explosionAudioTimer -= audioTime;
				playAudio(explosion.explosionSound);
			}
			
			explosion.explode(x, y);
			deployedExplosionList.add(explosion);
			break;
		}
	}
	
	public void createExplosion(String type, double x, double y) {
		ArrayList<Explosion> explosionList = getExplosionList(type);
		for (int i = 0; i < explosionList.size(); i++) {
			Explosion explosion = explosionList.get(i);
			if (explosion.exploding == true) {  continue; }
			playAudio(explosion.explosionSound);
			explosion.explode(x, y);
			deployedExplosionList.add(explosion);
			break;
		}
	}
	
	public void createSizedExplosion(String type, double x, double y, double size) {
		ArrayList<Explosion> explosionList = getExplosionList(type);
		for (int i = 0; i < explosionList.size(); i++) {
			Explosion explosion = explosionList.get(i);
			if (explosion.exploding == true) {  continue; }
			playAudio(explosion.explosionSound);
			explosion.size = size;
			explosion.explode(x, y);
			deployedExplosionList.add(explosion);
			break;
		}
	}

	public void createExplosion(String type, InterfaceEntity target, double dt) {
		ArrayList<Explosion> explosionList = getExplosionList(type);
		for (int i = 0; i < explosionList.size(); i++) {
			Explosion explosion = explosionList.get(i);
			if (explosion.exploding == true) {  continue; }
			
			explosionAudioTimer += dt;
			double audioTime = 0.02;
			if (explosionAudioTimer > audioTime) {
				explosionAudioTimer -= audioTime;
				playAudio(explosion.explosionSound);
			}
			
			explosion.explode(target);
			deployedExplosionList.add(explosion);
			break;
		}
	}
	
	public void createExplosion(String type, InterfaceEntity target) {
		ArrayList<Explosion> explosionList = getExplosionList(type);
		for (int i = 0; i < explosionList.size(); i++) {
			Explosion explosion = explosionList.get(i);
			if (explosion.exploding == true) {  continue; }
			playAudio(explosion.explosionSound);
			explosion.explode(target);
			deployedExplosionList.add(explosion);
			break;
		}
	}
	
	public void createRandomExplosionsOnTarget(String type, InterfaceEntity target, int numOfExplosions) {
		ArrayList<Explosion> explosionList = getExplosionList(type);
		ArrayList<Explosion> randomExplosionList = new ArrayList<Explosion>();

		double targetWidth = target.getWidth();
		double targetHeight = target.getHeight();
		double targetXPosition = target.getXCoordinate();
		double targetYPosition = target.getYCoordinate();
		double targetLeft = targetXPosition - targetWidth / 2;
		double targetRight = targetXPosition + targetWidth / 2;
		double targetTop = targetYPosition - targetHeight / 2;
		double targetBottom = targetYPosition + targetHeight / 2;
		
		for (int i = 0; i < explosionList.size(); i++) {
			Explosion explosion = explosionList.get(i);
			if (explosion.exploding == true) {  continue; }
			randomExplosionList.add(explosion);			
			if (randomExplosionList.size() >= numOfExplosions) { break; }
		}
		
		int size = randomExplosionList.size();
		if (size == 0) { return; }
		
		playAudio(randomExplosionList.get(0).explosionSound);
		for (int i = 0; i < size; i++) {
			Explosion explosion = explosionList.get(i);
			double x  = randomCoord(targetLeft, targetRight);
			double y = randomCoord(targetBottom, targetTop);
			explosion.explode(x, y);
			deployedExplosionList.add(explosion);
		}
	}
	
	public void createSizedRandomExplosionsOnTarget(String type, InterfaceEntity target, int numOfExplosions, double explosionSize) {
		ArrayList<Explosion> explosionList = getExplosionList(type);
		ArrayList<Explosion> randomExplosionList = new ArrayList<Explosion>();

		double targetWidth = target.getWidth();
		double targetHeight = target.getHeight();
		double targetXPosition = target.getXCoordinate();
		double targetYPosition = target.getYCoordinate();
		double targetLeft = targetXPosition - targetWidth / 2;
		double targetRight = targetXPosition + targetWidth / 2;
		double targetTop = targetYPosition - targetHeight / 2;
		double targetBottom = targetYPosition + targetHeight / 2;
		
		for (int i = 0; i < explosionList.size(); i++) {
			Explosion explosion = explosionList.get(i);
			if (explosion.exploding == true) {  continue; }
			randomExplosionList.add(explosion);			
			if (randomExplosionList.size() >= numOfExplosions) { break; }
		}
		
		int size = randomExplosionList.size();
		if (size == 0) { return; }
		
		playAudio(randomExplosionList.get(0).explosionSound);
		for (int i = 0; i < size; i++) {
			Explosion explosion = explosionList.get(i);
			double x  = randomCoord(targetLeft, targetRight);
			double y = randomCoord(targetBottom, targetTop);
			explosion.size = explosionSize;
			explosion.explode(x, y);
			deployedExplosionList.add(explosion);
		}		
	}
	
	public void registerExplosions(String name, int quantity, double explosionTime, String explosionSound) {
		ArrayList<Explosion> explosionList = getExplosionList(name);
		
		for (int i = 0; i < quantity; i++) {
			explosionList.add(new Explosion(game,stateManager, graphicsManager, audioManager, explosionTime, explosionSound));
		}	
	}
	
	protected ArrayList<Explosion> getExplosionList(String name) {
		ArrayList<Explosion> explosionList = explosionMap.get(name);
		if (explosionList == null) {
			throw new NullPointerException(String.format("Explosion type %s does not exist.", name));
		} 
		return explosionList;
	}
	
	protected void init() {
		explosionAudioTimer = 0;
		explosionMap = new  HashMap< String, ArrayList<Explosion> >();
		explosionMap.put(ExplosionTypes.Normal.name(), new ArrayList<Explosion>() );
		explosionMap.put(ExplosionTypes.Shield.name(), new ArrayList<Explosion>() );
		deployedExplosionList =  new  ArrayList<Explosion>();
	}
	
	protected double randomCoord(double firstDimension, double secondDimension) {
		Random random = new Random();
		return (firstDimension + ( random.nextDouble() * (secondDimension - firstDimension) ));
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
	
	protected void playAudio(String name) {
		audioManager.playAudio(name);
	}
}