package src.assignment2.entity.boss;

import java.util.*;
import java.lang.*;

public interface InterfaceBossFactory {
	
	public ArrayList<InterfaceBoss> createStageBosses();
	public InterfaceBoss createFinalBoss();
	
	public InterfaceBossBullet createBullet();
}