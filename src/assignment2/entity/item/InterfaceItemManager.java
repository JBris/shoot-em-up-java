package src.assignment2.entity.item;

import java.util.*;
import java.lang.*;
import java.util.*;
import src.assignment2.system.*;

public interface InterfaceItemManager {
	public int getScoreMultiplier();
	public void setScoreMultiplier(int v);
	public void incrementScoreMultiplier();

	public double getBaseRandomModifier();
	public void setBaseRandomModifier(double m);
	public void increaseBaseRandomModifierByValue(double v);
	
	public void setPointsRandomModifier(double m);
	public void increasePointsRandomModifierByValue(double v);
	
	public void setWeaponsRandomModifier(double m);
	public void increaseWeaponsRandomModifierByValue(double v);
	
	public void setItemSpeed(ProjectileSpeed projectileSpeed);
	
	public void update(double dt);
	public void reset();
	public void fullReset();
	public void paintComponent();
}