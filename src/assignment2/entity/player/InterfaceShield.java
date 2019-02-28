package src.assignment2.entity.player;

import java.util.*;
import java.lang.*;
import src.assignment.entity.*;
import src.assignment.system.*;
import src.assignment2.system.*;

public interface InterfaceShield extends InterfaceEntity, InterfaceShieldable, InterfaceWrapper, InterfaceDamageable {
	
	public void renderUi();
	public void updateRechargeTimers(double dt);
	public void fullReset(InterfaceGameOptions gameOptions);

}