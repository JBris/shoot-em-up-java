package src.assignment.service;

import java.util.*;
import java.lang.*;

public class Container implements InterfaceContainer {
	
	/***Properties***/
	
	protected Map<Class<?>, Object>  serviceList;
	
	/***Constructor***/
	
	public Container() {
		serviceList = new HashMap<Class<?>, Object> ();
	}
	
	/***Methods***/
	
	public <T> void bind(Class<T> serviceId, T service) {
		serviceList.put(serviceId, service);
	}
	
	public <T> void unbind(Class<T> serviceId) {
		serviceList.remove(serviceId);
	}

	public <T> T getService(Class<T> serviceId) {
		Object service  = serviceList.get(serviceId);
		if (service == null) {
			throw new NullPointerException(String.format("Service %s not found within container.", serviceId.getSimpleName()));
		} 
		return serviceId.cast(service);
	}
	
	public void mergeServices(Map<Class<?>, Object> serviceList) {
		this.serviceList.putAll(serviceList);
	}
}