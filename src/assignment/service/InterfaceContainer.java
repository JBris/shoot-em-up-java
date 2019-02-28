package src.assignment.service;

import java.util.*;
import java.lang.*;

public interface InterfaceContainer {
	
	public <T> void bind(Class<T> serviceId, T service);
	
	public <T> void unbind(Class<T> serviceId);

	public <T> T getService(Class<T> serviceId);
	
	public void mergeServices(Map<Class<?>, Object> serviceList);

}