package src.assignment.state;

import java.util.*;
import java.lang.*;

public interface InterfaceStateFactory {
	
	public InterfaceState createInstance (String stateName);
	
	public InterfaceState createBlankState();
	
}