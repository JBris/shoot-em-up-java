package src.assignment.entity;

import java.util.*;
import java.lang.*;

public interface InterfaceShieldable {

	public int getTotalShieldAmount();
	public void setTotalShieldAmount(int shieldAmount);
	
	public int getCurrentShieldAmount();
	public void setCurrentShieldAmount(int shieldAmount);
	public void resetCurrentShieldAmount();
	
	public int getShieldRecharge();
	public void setShieldRecharge(int shieldRecharge);
	public void updateRechargeTimers(double dt);

}