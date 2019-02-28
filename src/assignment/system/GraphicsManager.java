package src.assignment.system;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.state.*;

public class GraphicsManager implements InterfaceGraphicsManager {
	
	/***Properties***/
		
	protected InterfaceGame game;
	
	protected String currentBackground;
	protected double scrollingBackgroundYOffset;
	
	protected Map<String, Image> imageList;
	protected Map<String, Animation> animationList;
	protected Map<String, AnimatedText> textList;
	protected ArrayList<String> scrollingBackgroundList;
	protected ArrayList<String> displayedBackgroundList;

	/***Constructor***/
	
	public GraphicsManager(InterfaceGame game) {
		this.game = game;
		currentBackground = "";
		
		imageList = new HashMap<String, Image>();
		animationList = new HashMap<String, Animation>();
		textList = new HashMap<String, AnimatedText>();
		scrollingBackgroundList = new ArrayList<String>();
		displayedBackgroundList = new ArrayList<String>();
	}
	
	/***Methods***/

	//Image
	
	public void registerImage(String name, Image image) {
		imageList.put(name, image);
	}
	
	public Image getImage(String name) {
		Image image = imageList.get(name);
		if (image == null) {
			throw new NullPointerException(String.format("Image %s has not been registered.", name));
		} 
		return image;	
	}
	
	//Backgrounds
	
	public void registerScrollingBackground(String name) {
		scrollingBackgroundList.add(name);
	}
	
	public String getRandomScrollingBackgroundName() {
		String justDisplayed = currentBackground;
		
		if (scrollingBackgroundList.size() ==  0) {
			resetScrollingBackgrounds();
		}
		
		String name = scrollingBackgroundList.get(0);
		while(name == justDisplayed) {
			resetScrollingBackgrounds();	
			name = scrollingBackgroundList.get(0);
		}
		
		displayedBackgroundList.add(name);
		scrollingBackgroundList.remove(0);
		currentBackground = name;
		return name;
	}
	
	public void renderScrollingBackground(String name) {
		double width = width();
		double height = height();
		drawImage(name, 0, scrollingBackgroundYOffset, width, height * 2);
		drawImage(name, 0, scrollingBackgroundYOffset - height * 2, width, height * 2);
	}
	
	public void incrementScrollingBackgroundYOffset() {
		scrollingBackgroundYOffset += 1;
		if (scrollingBackgroundYOffset >= height() * 2) { scrollingBackgroundYOffset = 0; }
	}

	public void resetScrollingBackgroundYOffset() {
		scrollingBackgroundYOffset = 0;
	}
	
	public void resetScrollingBackgrounds() {
		for (int i =0; i < displayedBackgroundList.size(); i++) {
			scrollingBackgroundList.add(displayedBackgroundList.get(i));
			displayedBackgroundList.remove(i);
		}
		shuffleBackgrounds();
	}
	
	public void shuffleBackgrounds() {
		Collections.shuffle(scrollingBackgroundList);
	}
	
	//Animation
	
	public Image[] buildAnimation(Image image, int width, int height, int numFrames, int numOfRows, int numOfColumns, int startingRow, int startingColumn) {
		Image[] animation = new Image[numFrames];
		for(int i = startingColumn; i < numFrames; i++) {
			int y = i / numOfColumns;
			int x = i % numOfColumns;

			animation[i] = game.subImage(image, x* width, startingRow * height + y * height, width, height);
		}
		return animation;
	}
	
	public void registerAnimation(String name, Image[] frames, double duration) {
		Animation animation = new Animation(name, frames, frames.length, duration);
		animationList.put(name, animation);
	}

	public Animation getAnimation(String name) {
		Animation animation  = animationList.get(name);
		if (animation == null) {
			throw new NullPointerException(String.format("Animation %s has not been registered.", name));
		} 
		return animation;	
	}
	
	public void updateAnimationFrame(String name) {
		Animation animation = getAnimation(name);
		animation.currentFrame = getFrame(animation.duration, animation.numOfFrames);
	}
	
	public int getFrame(double duration, int numOfFrames) {
		return (int)Math.floor(((getTime()/1000.0 % duration) / duration) * numOfFrames);
	}
	
	public void playAnimation(String name, double x, double y, double w, double h) {
		Animation animation = getAnimation(name);
		game.drawImage(animation.frames[animation.currentFrame], x, y, w, h);
	}
	
	//Text
	
	public void registerText(String name, String[] text, double displayTime) {
		AnimatedText animatedText = new AnimatedText(game, name, text, displayTime);
		textList.put(name, animatedText);
	}
	
	public AnimatedText getText(String name) {
		AnimatedText animatedText  = textList.get(name);
		if (animatedText == null) {
			throw new NullPointerException(String.format("Text %s has not been registered.", name));
		} 
		return animatedText;	
	}
	
	public void displayAnimatedText(AnimatedText text, double x, double yBase, double yIncrement, String font, int fontSize) {
		double height = game.height();
		String[] displayText = text.getDisplayText();
		
		for (int i = 0; i < displayText.length; i++) {
			double y = height * (i * yBase + yIncrement);
			drawText(x, y, displayText[i], font, fontSize);
		}		
	}

	//Other
	
	public void drawImage(String name, double x, double y, double w, double h) {
		Image image = getImage(name);
		drawImage(image, x, y, w, h);
	}
	
	public void drawImage(Image image, double x, double y, double w, double h) {
		game.drawImage(image, x, y, w, h);
	}
	
	public void changeColor(Color c) {
		game.changeColor(c);
	}
	
	public void fadeIn(int r, int b, int g, int alpha) {
		game.changeColor(new Color(r, b, g, 255 - alpha));
		game.drawDisplayRectangle(0, 0, game.width(), game.height());
	}
		
	public void fadeOut(int r, int b, int g, int alpha) {
		game.changeColor(new Color(r, b, g, alpha));
		game.drawDisplayRectangle(0, 0, game.width(), game.height());
	}
	
	public void changeBackgroundColor(Color c) {
		game.changeBackgroundColor(c);
	}
	
	public void drawText(double x, double y, String s, String font, int size) {
		game.drawText(x, y , s, font, size);
	}
	
	public void drawMenuText (int menuChoice, int menuItem, double width, double height, String text) {
		if (menuChoice == menuItem) {
			game.changeColor(Color.YELLOW);
		} else {
			game.changeColor(Color.WHITE);
		}
		
		game.drawText(width, height , text, "Arial", (int) (game.height() * 0.05));
	}	
	
	public void drawMenuText (int menuChoice, int menuItem, double width, double height, String text, int fontSize) {
		if (menuChoice == menuItem) {
			game.changeColor(Color.YELLOW);
		} else {
			game.changeColor(Color.WHITE);
		}
		
		game.drawText(width, height , text, "Arial", fontSize);
	}
	
	protected long getTime() {
		return System.currentTimeMillis();
	}
	
	protected double width() {
		return game.width();
	}
	
	protected double height() {
		return game.height();
	}
}