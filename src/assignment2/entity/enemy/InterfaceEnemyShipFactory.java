package src.assignment2.entity.enemy;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.player.*;
import src.assignment.system.*;

public interface InterfaceEnemyShipFactory  {

	public ArrayList<InterfaceEnemyShip> createEnemies(String enemyType, int numOfEnemies);
	public InterfaceEnemyBullet createBullet();
	
}