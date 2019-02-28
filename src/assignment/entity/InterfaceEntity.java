package src.assignment.entity;

import java.util.*;
import java.lang.*;
import  src.assignment.system.*;

public interface InterfaceEntity extends InterfaceCollide,  InterfaceMovement, InterfaceInput, InterfacePosition, InterfaceRenderable, InterfaceSound, InterfaceDimensions, InterfaceInvulnerable, InterfaceKillable, InterfaceDamageable, InterfaceSkinnable, InterfaceDeflectAttack {
	
	public String name();
	public void setName(String name);
	public void update(double dt);
	public void reset();
}
