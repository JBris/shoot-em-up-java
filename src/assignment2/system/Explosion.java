package src.assignment2.system;

import java.awt.*;
import java.lang.*;
import java.util.*;
import src.assignment.entity.*;
import src.assignment2.entity.item.*;
import src.assignment2.entity.player.*;
import src.assignment.state.*;
import src.assignment.system.*;

public class Explosion extends AbstractEntity {
	
	/***Properties***/
	
	public boolean exploding, explosionFinished;
	public double explosionTime;
	public double size;
	public String explosionSound;
	
	public InterfaceEntity target;
	
	protected int currentFrame, numOfFrames;
	protected double explosionTimer;
	protected String animationName;
	
	public Image[] frames;
	
	/***Constructor***/
	
	public Explosion(InterfaceGame game,  InterfaceStateManager stateManager, 
		InterfaceGraphicsManager graphicsManager, InterfaceAudioManager audioManager,
		double explosionTime, String explosionSound) {
		super(game, stateManager, graphicsManager, audioManager);
		this.explosionTime = explosionTime;
		this.explosionSound = explosionSound;
		target = this;
		init();
	}
	
	/***Getters and Setters***/
	
	
	/***Methods***/

	public void reset() {
		exploding = false;
		explosionFinished = true;
		explosionTimer = 0;
		currentFrame = 0;
		size = width() * 0.08;
	}
	
	public void paintComponent() {	
		drawImage(frames[currentFrame], target.getXCoordinate() - size / 2, target.getYCoordinate() - size / 2, size, size);
	}
	
	public void update(double dt) {
		currentFrame = getFrame(explosionTime, numOfFrames);
		explosionTimer += dt;
		if (explosionTimer < explosionTime) { return; }
		reset();
	}
	
	public void explode(double x, double y) {
		exploding = true;
		explosionFinished = false;
		xCoordinate = x;
		yCoordinate = y;
	}
	
		
	public void explode(InterfaceEntity target) {
		this.target = target;
		explode(target.getXCoordinate(), target.getYCoordinate());
	}
	
	public void setExplosionAnimation(Animation explosion) {
		numOfFrames = explosion.numOfFrames;
		animationName = explosion.name;
		frames = explosion.frames;
	}
	
	protected void init() {
		numOfFrames = 0;
		animationName = "Explosion";
		frames = new Image[0];
		reset();
	}
	
	protected void drawImage(Image image, double x, double y, double w, double h) {
		graphicsManager.drawImage(image, x, y, w, h);
	}
	
	protected int getFrame(double duration, int numOfFrames)  {
		return graphicsManager.getFrame(duration, numOfFrames);
	}
}