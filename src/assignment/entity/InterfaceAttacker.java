package src.assignment.entity;

import java.util.*;
import java.lang.*;

public interface InterfaceAttacker {	
	public boolean isAttacking();
	public void setAttacking(boolean isAttacking);
	public void launchAttack(double dt);
	
}