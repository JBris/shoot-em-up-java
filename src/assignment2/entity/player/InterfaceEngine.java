package src.assignment2.entity.player;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public interface InterfaceEngine extends InterfaceEntity, InterfaceKeyListener, InterfaceMouseListener, InterfaceWrapper {
	
	public boolean getUp();
	public boolean getDown();
	public boolean getLeft();
	public boolean getRight();	
	public boolean isBoosting();

	public void setUp(boolean d);
	public void setDown(boolean d);
	public void setLeft(boolean d);
	public void setRight(boolean d);
	public void setIsBoosting(boolean b);

	public int getTotalBoostAmount();
	public void setTotalBoostAmount(int boostAmount);
	public int getCurrentBoostAmount();
	public void setCurrentBoostAmount(int boostAmount);
	public void resetCurrentBoostAmount();
	
	public int getBoostRecharge();
	public void setBoostRecharge(int boostRecharge);
	
	public void updateRechargeTimers(double dt);
	
	public void renderUi();
	public void move(boolean allowInput);
	public void fullReset(InterfaceGameOptions gameOptions);
	public void disableBoost();
}