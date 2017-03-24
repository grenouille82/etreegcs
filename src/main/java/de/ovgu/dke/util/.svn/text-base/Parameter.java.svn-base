/*
 * Created on 22.02.2005
 *
 */
package de.ovgu.dke.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A Parameter is a (name, value) pair. Both, name and value are Strings.
 * 
 * @author Sebastian Stober
 */
public class Parameter extends XMLSupportingClass {

	/**
	 * Name of the Parameter.
	 */
	private String mvName;

	/**
	 * Value of the Parameter.
	 */
	private String mvValue;
	
	/**
	 * 
	 */
	private Settings mvSettings;

	/**
	 * @see XMLSupportingClass#XML_TAG
	 */
	public static final String XML_TAG = "Parameter";

	/**
	 * Sets initial values for all member variables.
	 */
	private void initialize() {
		this.mvName 	= "";
		this.mvValue	= "";
		this.mvSettings = null;
	}

	/**
	 * Constructor - Creates an Parameter with empty name ("") and empty value
	 * ("").
	 */
	public Parameter() {
		this("", "", null);
	}

	/**
	 * Constructor - Creates an Parameter with a name and a value.
	 * 
	 * @param name
	 *            Name of the Parameter.
	 * @param value
	 *            Value of the Parameter.
	 */
	public Parameter(String name, String value) {
		this(name, value, null);
	}
	
	public Parameter(String name, String value, Settings settings)
	{
		this.mvName 	= name;
		this.mvValue 	= value;
		this.mvSettings = settings;
	}

	/**
	 * Returns the name of the Parameter.
	 * 
	 * @return Name of the Parameter.
	 */
	public String getName() {
		return mvName;
	}

	/**
	 * Returns the value of the Parameter.
	 * 
	 * @return Value of the Parameter.
	 */
	public String getValue() {
		return mvValue;
	}

	/**
	 * Assigns a name to the Parameter (overwrites it's prior name).
	 * 
	 * @param name
	 *            The new name of the Parameter.
	 */
	public void setName(String name) {
		this.mvName = name;
	}

	/**
	 * Assigns a value to the Parameter (overwrites it's prior value).
	 * 
	 * @param value
	 *            The new value of the Parameter.
	 */
	public void setValue(String value) {
		this.mvValue = value;
	}
	
	/**
	 * Returns the associated <code>Settings</code> for the <code>Parameter</code>. 
	 * That means the parameter has sub-settings. If no <code>Settings</code> are 
	 * present the method returns <code>null</code>, this is the case if <code>
	 * hasSettings()==true</code>.
	 * 
	 * @return the sub settings of the parameter if present, otherwise <code>null</code>.
	 */
	public Settings getSettings() {
		return mvSettings;
	}
	
	/**
	 * 
	 * @param settings 
	 */
	public void setSettings(Settings settings) {
		this.mvSettings = settings;
	}
	
	/**
	 * Checks whether the <code>Parameter</code> has settings. 
	 * @return <code>true</code> if <code>Settings</code> are present for the parameter,
	 * 		   otherwise <code>false</code>. 
	 */
	public boolean hasSettings() {
		return this.mvSettings != null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Element exportElement(Document doc, String xmlTag) {
		if (xmlTag == null) {
			xmlTag = XML_TAG;
		}
		// create element
		Element node = doc.createElement(xmlTag);

		node.setAttribute("Name", mvName);
		node.setAttribute("Value", mvValue);
		if(hasSettings())
			node.appendChild(mvSettings.exportElement(doc));
		return node;
	}

	/**
	 * {@inheritDoc}
	 */
	public void importElement(Element e) {
		initialize();
		mvName 	= e.getAttribute("Name");
		mvValue = e.getAttribute("Value");
		NodeList list = e.getElementsByTagName(Settings.XML_TAG);
		if(list.getLength() > 0 && list.item(0).getNodeType() == Element.ELEMENT_NODE) {
			mvSettings = new Settings();
			mvSettings.importElement((Element) list.item(0));
		}
		
	}

	/**
	 * Compares this Parameter with another one. The behavior of this function
	 * is used by {@link de.unimd.irgroup.carsa.config.Settings}.
	 * 
	 * @param o
	 *            2nd Parameter for comparison.
	 * @return True if the names of both Parameters are equal (ignoring case),
	 *         false otherwise.
	 * @see java.lang.String#equalsIgnoreCase(java.lang.String)
	 */
	public boolean equals(Object o) {
		if(o instanceof Parameter) {
			Parameter p = (Parameter) o;
			return this.mvName.equalsIgnoreCase(p.getName()); // compare names
		}
		return false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(mvName + "\t=\t" + mvValue);
		if(mvSettings != null) {
			sb.append("\n----------------------\n");
			sb.append(mvSettings.toString());
			sb.append("\n----------------------\n");
		}
		return sb.toString();
	}

}
