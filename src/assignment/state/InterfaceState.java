package src.assignment.state;

import java.util.*;
import java.lang.*;
import src.assignment.system.*;

public interface InterfaceState extends InterfaceKeyListener, InterfaceMouseListener {
	
	public void reset();
	
	public void reset(boolean playAudio);
	
	public void update(double dt);
	
	public void paintComponent();
	
	public String getStateName();
}