package src.assignment.state;

import java.awt.event.*;
import java.util.*;
import java.lang.*;

public class BlankState implements InterfaceState {
	
	/***Methods***/
	
	public void reset() {}
	
	public void reset(boolean playAudio) {}
	
	public void update(double dt) {}
	
	public void paintComponent() {}
	
	public String getStateName() { return ""; }
	
	public void keyPressed(KeyEvent e) { }
	
	public void keyReleased(KeyEvent e) { }
	
	public void mouseExited(MouseEvent e) {}
	
	public void mousePressed(MouseEvent event) {}

	public void mouseReleased(MouseEvent event) {}

	public void mouseMoved(MouseEvent event) {}

	public void mouseDragged(MouseEvent event) {}
	
}