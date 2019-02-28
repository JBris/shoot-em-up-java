package src.assignment.system;

import java.awt.*;
import java.util.*;
import java.lang.*;
import src.assignment.state.*;

public class Animation {
	
	public String name;
	public Image frames[];
	public int numOfFrames, currentFrame;
	public double duration;
	
	public Animation(String name, Image[] frames, int numOfFrames, double duration) {
		this.name = name;
		this.frames= frames;
		this.numOfFrames = numOfFrames;
		this.duration = duration;
		currentFrame = 0;
	}
}