package src.assignment.system;

import java.io.*;
import java.lang.*;
import java.util.*;
import org.w3c.dom.*;

public interface InterfaceFile {
	
	public void writeSerializableArray(Serializable[] serializableList, String dir, String fileName);
	public <T> T readSerializableArray(Class<T> castClass, String dir, String fileName);
	
	public Element readXmlFile(String dir, String fileName);
	public Map<String, ArrayList<String>> convertNodeListToMap(String elementName, Element element);
	public <T> void writeXmlFile(T contents, String dir, String fileName);
	public Map<String, ArrayList<String>> convertXmlToArray(String dir, String fileName);
	public <T> T convertReadXmlFile (Class<T> castClass, String dir, String fileName);
}