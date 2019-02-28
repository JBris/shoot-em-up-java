package src.assignment2.entity.enemy;

import java.lang.*;
import java.util.*;

public enum EnemyDifficulty {
	Easy, 
	Medium, 
	Hard;
	
	public static final ArrayList<String> getEnemyDifficultyMapping(EnemyDifficulty difficulty) {
		ArrayList<String> enemies = new ArrayList<String>();
		
		switch(difficulty) {
			case Easy:
				enemies.add(EnemyType.UFO.name());
				enemies.add(EnemyType.Dove.name());
				enemies.add(EnemyType.Paranoid.name());
				enemies.add(EnemyType.SmallJelly.name());				
				break;
			case Medium:
				enemies.add(EnemyType.Ninja.name());
				enemies.add(EnemyType.Ligher.name());	
				enemies.add(EnemyType.Turtle.name());	
				enemies.add(EnemyType.MediumJelly.name());							
				break;
			case Hard:
				enemies.add(EnemyType.Saboteur.name());
				enemies.add(EnemyType.Lightning.name());	
				enemies.add(EnemyType.BigJelly.name());										
				break;		
		}
		
		return enemies;
	} 
}

	