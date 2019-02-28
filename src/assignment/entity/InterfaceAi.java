package src.assignment.entity;

import java.util.*;
import java.lang.*;

public interface InterfaceAi {
	
	public void aiMove(double dt);
	
	public boolean isAiEnabled();
	public void setAiEnabled(boolean aiEnabled);
	
	public double getAiSpeed();
	public double getReactionTime();
	public double getReactionTimer();	
	public double getErrorMultiplier();
	public void setAiSpeed(double s);
	public void setReactionTime(double t);
	public void setReactionTimer(double t);
	public void setErrorMultiplier(double m);
	
}