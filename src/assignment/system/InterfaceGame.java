package src.assignment.system;

import java.awt.*;
import java.util.*;
import java.lang.*;

import src.assignment.state.*;

public interface InterfaceGame {
	
	public void update(double dt);
	
	public void paintComponent();
	
	public int width();
	
	public int height();  
	
	public void changeBackgroundColor(Color c);

	public void changeColor(Color c);
		
	public void sleep(double duration);
	public long measureTime();
	public long getTime();
	
	public void drawText(double x, double y, String s, String font, int size);
	public void drawDisplayLine(double x1, double y1, double x2, double y2, double l);
	public void drawDisplayCircle(double x, double y, double r);
	public void drawDisplayRectangle(double x, double y, double w, double h);
	
	public Image subImage(Image source, int x, int y, int w, int h);
	public void drawImage(Image image, double x, double y, double w, double h);
	
	public void startAudioLoop(AudioSource audioSource);
	public void stopAudioLoop(AudioSource audioSource);
	
	public double rand(double max);
	
	public int rand(int max);
	
	public double length(double x, double y);
}