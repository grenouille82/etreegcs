/*
 * Created on 23.03.2007
 */
package de.ovgu.dke.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author kbade*/
public interface XMLSupportingClassIf {
	  /**
	   * Converts the object's content to an (DOM) Element decription using the XML_TAG of the object's class.
	   * This description may then be serialized to XML.
	   * 
	   * @param 	doc		  related document (from calling function)
	   * @return  Element element description of the object for export to XML
	   */
	public Element exportElement(Document doc);

	/**
	 * Converts the object's content to an (DOM) Element decription using the specified XML-tag.
	 * This description may then be serialized to XML.
	 * 
	 * @param 	doc		  related document (from calling function)
	 * @param 	xmlTag	XML-tag for this object (if null, the XML_TAG of the object's class os used)
	 * @return  Element element description of the object for export to XML
	 */
	public Element exportElement(Document doc, String xmlTag);

	/**
	 * Serializes the object to an XML String.
	 * 
	 * @return XML decription of the object
	 * @see #exportElement(Document, String)
	 */
	public String toXMLString();
	
	/**
	 * Deserializes the object from an Element (DOM) desrciption. 
	 * The values of all member variables are overwritten by those specified in the Element desrciption.   *  
	 * 
	 * @param e Element Element to be imported
	 */
	public void importElement(Element e);

	/**
	 * Deserializes the object from a XML-representation.
	 * 
	 * @param XMLString XML representation
	 * @see #importElement(Element)
	 */
	public void importFromXMLString (String XMLString);
	
	/**
	 * Deserializes the object from a file containing the XML-representation.
	 * 
	 * @param fileName name (and path) of the file containing the XML description of the object
	 * @see #importElement(Element)
	 */
	public void importFromFile(String fileName);

	/**
	 * Serializes the object to a XML String that is written to a file.
	 * If a file with the specified name already exists, it is overwritten.
	 * 
	 * @param fileName name (and path) of the file to write to
	 * @see #importElement(Element)
	 */
	public void writeToFile(String fileName);
}
