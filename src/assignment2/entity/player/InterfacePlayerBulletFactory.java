package src.assignment2.entity.player;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public interface InterfacePlayerBulletFactory {
	
	public Map<String, InterfaceDeploymentBehaviour> initDeploymentBehaviours();
	public Map<String, ArrayList<InterfaceBullet>> initSpecialBullets();

	public ArrayList<InterfaceBullet> createCollection(ArrayList<InterfaceBullet> bullets, String type, int quantity);
}