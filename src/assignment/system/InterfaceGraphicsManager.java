package src.assignment.system;

import java.awt.*;
import java.util.*;
import java.lang.*;

public interface InterfaceGraphicsManager {
	
	public void registerImage(String name, Image image);
	public void registerScrollingBackground(String name);
	public String getRandomScrollingBackgroundName();
	public void renderScrollingBackground(String name);
	public void incrementScrollingBackgroundYOffset();
	public void resetScrollingBackgroundYOffset();
	public void resetScrollingBackgrounds();
	public void shuffleBackgrounds();

	public Image getImage(String name);
	
	public Image[] buildAnimation(Image image, int width, int height, int numFrames, int numOfRows, int numOfColumns, int startingRow, int startingColumn);
	public void registerAnimation(String name, Image[] animation, double duration);
	public Animation getAnimation(String name);
	public void updateAnimationFrame(String name);
	public int getFrame(double duration, int numOfFrames);
	public void playAnimation(String name, double x, double y, double w, double h);
	
	public void registerText(String name, String[] text, double displayTime);
	public AnimatedText getText(String name);
	public void displayAnimatedText(AnimatedText text, double x, double yBase, double yIncrement, String font, int fontSize);
	
	public void drawImage(String name, double x, double y, double w, double h);
	public void drawImage(Image image, double x, double y, double w, double h);

	public void changeColor(Color c);
	
	public void fadeOut(int r, int b, int g, int alpha);
	public void fadeIn(int r, int b, int g, int alpha);

	public void changeBackgroundColor(Color c);
	
	public void drawText(double x, double y, String s, String font, int size);

	public void drawMenuText (int menuChoice, int menuItem, double width, double height, String text);	
	public void drawMenuText (int menuChoice, int menuItem, double width, double height, String text, int fontSize);
}