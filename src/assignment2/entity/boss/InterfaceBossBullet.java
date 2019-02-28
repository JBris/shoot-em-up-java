package src.assignment2.entity.boss;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment2.entity.player.*;
import src.assignment.system.*;

public interface InterfaceBossBullet extends InterfaceEnemyBullet  {
		
	public double getLifeSpan();
	public void setLifeSpan(double l);
	
	public boolean getSpecialAttack();
	public void setSpecialAttack(boolean v);
	
	public void update(double dt, InterfacePlayerShip playerShip, InterfaceBoss boss);

}