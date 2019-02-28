package src.assignment.entity;

import java.util.*;
import java.lang.*;

public interface InterfaceDamageable {
	
	public void damage(int amount);
	public void damage(int amount, InterfaceScore scorer);
}