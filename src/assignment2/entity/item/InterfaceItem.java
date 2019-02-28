package src.assignment2.entity.item;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment2.entity.enemy.*;
import src.assignment2.entity.player.*;
import src.assignment.system.*;

public interface InterfaceItem extends InterfaceDeployableEntity, InterfacePoints  {
	public void collide(InterfacePlayerShip playerShip, InterfaceEnemyManager enemyManager, int scoreMultiplier);
}