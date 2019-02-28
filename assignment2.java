// Bristow, James, 11189075, Assignment 2, 159.103 
/* 
  * See the src directory for the other class files.
  * Player and enemy implementations are in src/assignment2/entity
  * Game states are in src/assignment2/state
  * System files are in src/assignment/system
  *
  * See the assets directory for images and audio.
  */ 
  
//javac src/assignment/state/*java && javac src/assignment/system/*java && javac src/assignment/service/*java && javac src/assignment/entity/*java && javac *java
//javac src/assignment2/state/*java && javac src/assignment2/system/*java && javac src/assignment2/service/*java  && javac src/assignment2/entity/*java && javac *java
//javac src/assignment2/entity/player/*java && javac src/assignment2/entity/item/*java && javac src/assignment2/entity/enemy/*java && javac src/assignment2/entity/boss/*java  && javac *java
//java assignment2

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.lang.*;
import javax.sound.sampled.*;
import src.assignment.system.*;
import src.assignment.state.*;
import src.assignment.service.*;
import src.assignment.service.Container;
import src.assignment2.entity.boss.*;
import src.assignment2.entity.enemy.*;
import src.assignment2.entity.player.*;
import src.assignment2.state.*;
import src.assignment2.service.*;
import src.assignment2.system.*;

public class assignment2 extends GameEngine implements InterfaceGame {
	
	/***Properties***/
	
	protected boolean booted;
	
	protected InterfaceStateManager stateManager;
	protected InterfaceGraphicsManager graphicsManager;
	protected InterfaceAudioManager audioManager;
	
	private InterfaceContainer container;
	
	// Main Function
	public static void main(String args[]) {
		createGame(new assignment2(), 60);
	}
	
	/***Constructor***/
	
	public assignment2 () {
		super();
		booted = false;
		boot(new ServiceContext(this));
		
	}
	
	/***Methods***/
	
	public void init() {
		setWindowSize(750, 750);
		container.getService(InterfacePlayerShip.class).reset();
		stateManager.switchState(GameState.Intro.name());	
		stateManager.getCurrentState().reset();	
	}
	
	public void update(double dt) {
		if (booted == false) { return; }
		stateManager.update(dt);
	}
	
	public void paintComponent() {
		if (booted == false) { return; }
		changeBackgroundColor(black);
		clearBackground(width(), height());
		stateManager.paintComponent();
	}
		
	public void drawDisplayLine(double x1, double y1, double x2, double y2, double l) {
		drawLine(x1, y1, x2, y2, l);
	}
	
	public void drawDisplayCircle(double x, double y, double r) {
		drawSolidCircle(x, y, r);
	}
	
	public void drawDisplayRectangle(double x, double y, double w, double h) {
		drawSolidRectangle(x, y, w, h);
	}
	
	public void keyPressed(KeyEvent e) {
		if (booted == false) { return; }
		stateManager.keyPressed(e);
	}
	
	public void keyReleased(KeyEvent e) {
		if (booted == false) { return; }
		stateManager.keyReleased(e);
	}
	
	public void mouseExited(MouseEvent e) {
		if (booted == false) { return; }
		stateManager.mouseExited(e);
	}
	
	public void mousePressed(MouseEvent e) {
		if (booted == false) { return; }
		stateManager.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (booted == false) { return; }
		stateManager.mouseReleased(e);
	}

	public void mouseMoved(MouseEvent e) {
		if (booted == false) { return; }
		stateManager.mouseMoved(e);		
	}

	public void mouseDragged(MouseEvent e) {
		if (booted == false) { return; }
		stateManager.mouseDragged(e);		
	}
	
	public void startAudioLoop(AudioSource audioSource) {
		AudioClip audio = AudioClip.class.cast(audioSource.get());
		startAudioLoop(audio);
	}
	
	public void stopAudioLoop(AudioSource audioSource) {
		AudioClip audio = AudioClip.class.cast(audioSource.get());
		stopAudioLoop(audio);
	}
	
	protected void boot(InterfaceServiceContext serviceContext) {
		
		serviceContext.bindServices();
		container = serviceContext.getContainer();
		
		stateManager = container.getService(InterfaceStateManager.class);
		registerAllStates();
		
		graphicsManager = container.getService(InterfaceGraphicsManager.class);
		loadAllImages();
		
		audioManager = container.getService(InterfaceAudioManager.class);		
		registerAllAudio();
		
		booted = true;
	}
	
	protected void loadAllImages() {
		registerImage("Intro", "assets/image/seamless space_0.jpg");
		registerImage("Menu", "assets/image/stars_texture.jpg");
		registerScrollingBackground("Factory", "assets/image/sci_fi_bg1.jpg");
		registerScrollingBackground("Cave", "assets/image/back_cave.jpg");
		registerScrollingBackground("Highway", "assets/image/highway.jpg");
		registerScrollingBackground("Mountains", "assets/image/mountains.png");
		registerScrollingBackground("Desert", "assets/image/desert.png");
		registerImage("Final", "assets/image/living-tissue-background-preview.png");
		
		registerImage("Shield", "assets/image/shield.png");
		registerImage("Boost", "assets/image/boost.png");
		registerImage("Empty", "assets/image/empty.png");
		registerAnimation("PlayerMovement", "assets/image/ship.png", 16, 24, 10, 2, 5, 0 , 0 , 2);
		Image playerShip = loadImage("assets/image/ship.png");
		graphicsManager.registerImage("PlayerLives", subImage(playerShip, 32, 0, 16, 24));
		
		registerAnimation("PointsDrop", "assets/image/power-up.png", 16, 16, 4, 2, 2, 0 , 0 , 1);
		registerAnimation("DeflectDrop", "assets/image/DeflectDrop.png", 40, 40, 2, 0, 2, 0 , 0 , 1);
	
		registerExplosionAnimations();
		registerWeaponAnimations();
		registerEnemies();
		registerBosses();
	}
		
	protected void registerImage(String name, String path) {
		graphicsManager.registerImage(name, loadImage(path));
	}
	
	protected void registerAnimation(String name, String file, int spriteWidth, int spriteHeight, int numFrames, int numOfRows, int numOfColumns, int startingRow, int startingColumn, double duration) {
		Image image = loadImage(file);
		registerAnimation(name, image, spriteWidth, spriteHeight,numFrames, numOfRows, numOfColumns, startingRow, startingColumn, duration);
	}
	
	protected void registerAnimation(String name, Image image, int spriteWidth, int spriteHeight, int numFrames, int numOfRows, int numOfColumns, int startingRow, int startingColumn, double duration) {
		Image[] animation = graphicsManager.buildAnimation(image, spriteWidth, spriteHeight, numFrames, numOfRows, numOfColumns, startingRow, startingColumn);
		graphicsManager.registerAnimation(name, animation, duration);
	}
	
	protected void registerScrollingBackground(String name, String path) {
		registerImage(name, path);
		graphicsManager.registerScrollingBackground(name);
	}
	
	protected void registerExplosionAnimations() {
		registerAnimation(ExplosionTypes.Normal.name(), "assets/image/explosion.png", 16, 16, 5, 0, 5, 0 , 0 , 1 / 5);
		registerAnimation(ExplosionTypes.Shield.name(), "assets/image/blue-explosion.png", 16, 16, 5, 0, 5, 0 , 0 , 0.5 / 5);
		InterfaceExplosionManager explosionManager = container.getService(InterfaceExplosionManager.class);
		
		explosionManager.registerExplosions(ExplosionTypes.Normal.name(), 40, 1, "explosion");
		explosionManager.registerExplosions(ExplosionTypes.Shield.name(), 10, 0.5, "back");
		
		explosionManager.setExplosionAnimation(graphicsManager.getAnimation(ExplosionTypes.Normal.name()));
		explosionManager.setExplosionAnimation(graphicsManager.getAnimation(ExplosionTypes.Shield.name()));
	}
	
	protected void registerWeaponAnimations() {
		Image bullets =  loadImage("assets/image/laser-bolts.png");
		registerAnimation("BasicBullet", subImage(bullets, 0, 16, 32, 16), 16, 16, 2, 1, 2, 0 , 0 , 1);
		registerAnimation("SpecialBullet", subImage(bullets, 0, 0, 32, 16), 16, 16, 2, 1, 2, 0 , 0 , 1);
		
		Image shotgun = loadImage("assets/image/shotgun.png");
		registerAnimation("ShotgunDrop", shotgun, 74, 74, 3, 1, 3, 0, 0, 1);
		graphicsManager.registerImage("ShotgunIcon", subImage(shotgun, 0, 0, 74, 74));
		
		Image tracker = loadImage("assets/image/tracker.png");
		registerAnimation("TrackerDrop", tracker, 32, 32, 6, 1, 6, 0, 0, 1);
		graphicsManager.registerImage("TrackerIcon", subImage(tracker, 0, 0, 32, 32));
		
		Image bouncer = loadImage("assets/image/bouncer.png");
		registerAnimation("BouncerDrop", bouncer, 74, 74, 3, 1, 3, 0, 0, 1);
		graphicsManager.registerImage("BouncerIcon", subImage(bouncer, 0, 0, 74, 74));
		
		Image ring = loadImage("assets/image/ring.png");
		registerAnimation("RingDrop", ring, 42, 42, 3, 1, 3, 0, 0, 1);
		graphicsManager.registerImage("RingIcon", subImage(ring, 0, 0, 42, 42));
	}
	
	protected void registerEnemies() {
		InterfaceEnemyManager enemyManager = container.getService(InterfaceEnemyManager.class);
		enemyManager.createBullets(100);

		enemyManager.createEnemies(EnemyType.UFO.name(), 20);
		registerAnimation(EnemyType.UFO.name(), "assets/image/UFO.png", 32, 32, 4, 0, 4, 0 , 0 ,  0.5);
		
		enemyManager.createEnemies(EnemyType.Dove.name(), 20);
		registerAnimation(EnemyType.Dove.name(), "assets/image/Dove.png", 32, 32, 4, 0, 4, 0 , 0 ,  0.5);
		
		enemyManager.createEnemies(EnemyType.Paranoid.name(), 20);
		registerAnimation(EnemyType.Paranoid.name(), "assets/image/Paranoid.png", 32, 32, 4, 0, 4, 0 , 0 ,  0.5);
		
		enemyManager.createEnemies(EnemyType.Ninja.name(), 15);
		registerAnimation(EnemyType.Ninja.name(), "assets/image/Ninja.png", 32, 32, 4, 0, 4, 0 , 0 ,  0.5);
		
		enemyManager.createEnemies(EnemyType.Ligher.name(), 15);	
		registerAnimation(EnemyType.Ligher.name(), "assets/image/Ligher.png", 32, 32, 4, 0, 4, 0 , 0 ,  0.5);

		enemyManager.createEnemies(EnemyType.Turtle.name(), 15);	
		registerImage(EnemyType.Turtle.name(), "assets/image/Turtle.png");
		
		enemyManager.createEnemies(EnemyType.Saboteur.name(), 10);	
		registerAnimation(EnemyType.Saboteur.name(), "assets/image/Saboteur.png", 32, 32, 4, 0, 4, 0 , 0 ,  0.5);
		
		enemyManager.createEnemies(EnemyType.Lightning.name(), 10);	
		registerAnimation(EnemyType.Lightning.name(), "assets/image/Lightning.png", 32, 32, 4, 0, 4, 0 , 0 ,  0.5);
		
		enemyManager.createEnemies(EnemyType.SmallJelly.name(), 20);	
		registerAnimation(EnemyType.SmallJelly.name(), "assets/image/enemy-small.png", 16, 16, 2, 0, 2, 0 , 0 ,  0.5);
		
		enemyManager.createEnemies(EnemyType.MediumJelly.name(), 15);	
		registerAnimation(EnemyType.MediumJelly.name(), "assets/image/enemy-medium.png", 32, 16, 2, 0, 2, 0 , 0 ,  0.5);
		
		enemyManager.createEnemies(EnemyType.BigJelly.name(), 10);	
		registerAnimation(EnemyType.BigJelly.name(), "assets/image/enemy-big.png", 32, 32, 2, 0, 2, 0 , 0 ,  0.5);
		
		registerAnimation("EnemyBullet", "assets/image/EnemyBullet.png", 30, 30, 2, 0, 2, 0 , 0 ,  1);
	}
	
	protected void registerBosses() {
		InterfaceBossManager bossManager = container.getService(InterfaceBossManager.class);
		bossManager.createBullets(100);
		
		registerAnimation("BossBullet", "assets/image/BossBullet.png", 30, 30, 2, 0, 2, 0 , 0 ,  1);
		
		registerAnimation(StageBoss.Odin.name(), "assets/image/Odin.png", 138, 138, 4, 0, 4, 0 , 0 ,  2);
		registerAnimation(StageBoss.Zeus.name(), "assets/image/Zeus.png", 248, 146, 3, 0, 3, 0 , 0 ,  1);
		registerAnimation(StageBoss.Indra.name(), "assets/image/Indra.png", 128, 128, 2, 0, 2, 0 , 0 ,  2);
		registerAnimation(StageBoss.Amun.name(), "assets/image/Amun.png", 156, 211, 3, 0, 3, 0 , 0 ,  2);
		registerAnimation(StageBoss.Shangdi.name(), "assets/image/Shangdi.png", 189, 210, 2, 0, 2, 0 , 0 ,  2);
		
		registerAnimation(StageBoss.ImperfectUnknown.name(), "assets/image/ImperfectUnknown.png", 176, 325, 2, 0, 2, 0 , 0 ,  2);
		registerAnimation(StageBoss.PerfectUnknown.name(), "assets/image/PerfectUnknown.png", 135, 248, 2, 0, 2, 0 , 0 ,  2);
	}
	
	protected void registerAllAudio() {
		registerAudio("pew", "assets/audio/SHOOT012.wav", 15);
		registerAudio("pewpew", "assets/audio/SHOOT011.wav", 15);
		registerAudio("beep", "assets/audio/GUI Sound Effects_051.wav", 10);
		registerAudio("back", "assets/audio/GUI Sound Effects_054.wav", 5);
		registerAudio("invalid", "assets/audio/FX236.wav", 5);
		registerAudio("drop", "assets/audio/FX161.wav", 5);
		registerAudio("boost", "assets/audio/sfx_fly.wav", 5);
		registerAudio("explosion", "assets/audio/8bit_bomb_explosion.wav", 15);
		registerAudio("victory", "assets/audio/victory jingle.wav", 3);
		registerAudio("boss", "assets/audio/ambient_alarm1.wav", 3);

		registerAudio("Menu", "assets/audio/This Place Is So Lonely v1.0.wav");
		registerAudio("Boss", "assets/audio/Boss 1 v1.0.wav");
		registerAudio("Warning", "assets/audio/Warning v1.0.wav");
		registerAudio("Speed", "assets/audio/The Speed Consumes Me v1.0.wav");
		registerAudio("Flaming", "assets/audio/Flaming Soul Part 1 v1.0.wav");
		registerAudio("Trip", "assets/audio/This Trip Might Be Our Last v1.0.wav");
		registerAudio("Final", "assets/audio/Battle in the Stratosphere.wav");

		audioManager.addToPlayList("Boss");
		audioManager.addToPlayList("Warning");
		audioManager.addToPlayList("Speed");
		audioManager.addToPlayList("Flaming");
		audioManager.addToPlayList("Trip");

		audioManager.shufflePlayList();
	}
	
	protected void registerAudio(String audioName, String audioPath) {
		AudioClip audio = loadAudio(audioPath);	
		AudioSource audioSource =  new AudioSource<AudioClip>(audioName, audio);
		audioManager.registerAudio(audioSource);
	}
	
	protected void registerAudio(String audioName, String audioPath, int numOfClips)  {
		AudioClip audioClip = loadAudio(audioPath);	
		AudioSource audioSource =  new AudioSource<AudioClip>(audioName, audioClip);
		audioSource.mFormat = audioClip.getAudioFormat();
		audioSource.mData = audioClip.getData();
		audioSource.mLength = (int) audioClip.getBufferSize();
		audioSource.addClips(numOfClips);
		audioManager.registerAudio(audioSource);	
	}
	
	protected void registerAllStates() {
		InterfaceStateFactory stateFactory = container.getService(InterfaceStateFactory.class);
		registerState(stateFactory, GameState.Intro.name());
		registerState(stateFactory, GameState.Menu.name());
		registerState(stateFactory, GameState.Play.name());
		registerState(stateFactory, GameState.Leaderboard.name());
		registerState(stateFactory, GameState.Options.name());
		registerState(stateFactory, GameState.Controls.name());
	}
	
	protected void registerState(InterfaceStateFactory stateFactory, String stateName) {
		InterfaceState state = stateFactory.createInstance(stateName);
		stateManager.registerState(state);
	}
}