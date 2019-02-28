package src.assignment2.system;

import java.util.*;
import java.lang.*;
import java.util.*;
import src.assignment.entity.*;
import src.assignment.system.*;

public interface InterfaceExplosionManager {
	public void reset();
	public void paintComponent();
	public void update(double dt);
	
	public void registerExplosions(String name, int quantity, double explosionTime, String explosionSound);
	public void setExplosionAnimation(Animation explosion);
	
	public void createExplosion(String type, double x, double y, double dt);
	public void createExplosion(String type, double x, double y);
	
	public void createSizedExplosion(String type, double x, double y, double size);
	
	public void createExplosion(String type, InterfaceEntity target, double dt);
	public void createExplosion(String type, InterfaceEntity target);
	public void createRandomExplosionsOnTarget(String type, InterfaceEntity target, int numOfExplosions);
	public void createSizedRandomExplosionsOnTarget(String type, InterfaceEntity target, int numOfExplosions, double size);

}