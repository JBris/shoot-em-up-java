package src.assignment2.system;

import java.util.*;
import java.lang.*;
import java.util.*;

public interface InterfaceStageManager {
	
	public int getStageNumber();
	public int getGameLoopNumber();
	public int getNumOfRandomStages();
	public int getNumOfTotalStages();
		
	public boolean getInIntroState();
	public boolean getInEndingState();
	
	public boolean getStageCompletionTrigger();
	public void setStageCompletionTrigger(boolean s);
	
	public boolean getIsTransitioning();
	public void setIsTransitioning(boolean isTransitioning);
	
	public void nextStage();
	public void nextStage(double dt, double transitionLength);
	public void reset(InterfaceGameOptions gameOptions, boolean setAudio);
	public void cleanup();
	
	public void paintComponent();
	public void renderCurrentBackground();
	
	public void update(double dt);
}