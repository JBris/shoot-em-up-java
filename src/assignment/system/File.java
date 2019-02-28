package src.assignment.system;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import javax.xml.bind.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import java.util.*;
import java.lang.*;
import java.lang.Exception.*;
import src.assignment.state.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import org.w3c.dom.Element;

public class File implements InterfaceFile {
	
	/***Properties***/

	protected InterfaceGame game;
	
	/***Constructor***/
	
	public File(InterfaceGame game) {
		this.game = game;
	}
	
	/***Methods***/
	
	public void writeSerializableArray(Serializable[] serializableList, String dir, String fileName) {
		try{
			FileOutputStream out = new FileOutputStream(String.format("data/%s/%s.txt", dir, fileName), false);
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(serializableList);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	
	}
	
	public <T> T readSerializableArray(Class<T> castClass, String dir, String fileName) {
		try {
			FileInputStream in = new FileInputStream(String.format("data/%s/%s.txt", dir, fileName));
			ObjectInputStream ois = new ObjectInputStream(in);
			T content = castClass.cast(ois.readObject());
			ois.close();
			return content;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public Element readXmlFile(String dir, String fileName) {
		String fileBody = "";
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Element doc = null;
		
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(String.format("data/%s/%s.xml", dir, fileName));
			doc = dom.getDocumentElement();			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
	public Map<String, ArrayList<String>> convertNodeListToMap(String elementName, Element element) {
		Map<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();
		NodeList extractedNodes = element.getElementsByTagName(elementName);
		if (extractedNodes == null || extractedNodes.getLength() == 0) { return data; }
			
		Node firstNode = extractedNodes.item(0);
		NodeList childNodes = firstNode.getChildNodes();
		
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			String nodeName = node.getNodeName();
			if (nodeName == "#text") { continue; }
				
			ArrayList<String> childData = extractChildNodes(node);
			data.put(nodeName, childData);
		}
		
		return data;
	}
	
	public <T> void writeXmlFile(T contents, String dir, String fileName) {
		StringWriter sw = new StringWriter();
		JAXB.marshal(contents, sw);
		String xmlString = sw.toString();
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(String.format("data/%s/%s.xml", dir, fileName)));
			out.write(xmlString);   
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	public <T> T convertReadXmlFile (Class<T> castClass, String dir, String fileName) {
		String fileBody = "";
		try  {
			fileBody = new String ( Files.readAllBytes( Paths.get(String.format("data/%s/%s.xml", dir, fileName)) ) );
		} 
		catch (IOException e)  {
			e.printStackTrace();
		}

		T contents = JAXB.unmarshal(new StringReader(fileBody), castClass);
		return contents;
	}
	
	public Map<String, ArrayList<String>> convertXmlToArray(String dir, String fileName) {
		Map<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();
		Element doc = readXmlFile(dir, fileName);
		NodeList nl = doc.getChildNodes();

		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			String nodeName = node.getNodeName();
			if (nodeName == "#text") { continue; }
				
			ArrayList<String> childData = extractChildNodes(node);
			data.put(nodeName, childData);
		}
		
		return data ;
	}
	
	
	protected ArrayList<String> extractChildNodes(Node parentNode) {
		ArrayList<String> childData = new ArrayList<String>();
		NodeList childNodes = parentNode.getChildNodes();
		
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			String content = child.getTextContent().trim();
			if (content.length() > 0) {
				childData.add(content);
			}
		}
		return childData;
	} 
}