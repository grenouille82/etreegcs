/*
 * Created on 22.02.2005
 *
 */
package de.ovgu.dke.util;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A set of Parameters {@link de.unimd.irgroup.carsa.config.Parameter}. The
 * Parameters are accessed through their names. Thus, Parameter names need to be
 * unique.
 * 
 * @author Sebastian Stober
 */
public class Settings extends XMLSupportingClass {

	/**
	 * Stores the Parameters contained in this Settings.
	 */
	Map<String, Parameter> parameters;
	
	/**
	 * @see XMLSupportingClass#XML_TAG
	 */
	public static final String XML_TAG = "Settings";

	/**
	 * Sets initial values for all member variables.
	 */
	private void initialize() {
		parameters = new HashMap<String, Parameter>();
	}

	/**
	 * Constructor - Creates an empty Settings object.
	 */
	public Settings() {
		initialize();
	}

	/**
	 * Checks if a Parameter is defined in this Settings.
	 * 
	 * @param parameterName
	 *            Name of the Parameter.
	 * @return True, if such a Parameter is defined, false otherwise.
	 */
	public boolean parameterDefined(String parameterName) {
		return parameters.containsKey(parameterName);
	}

	/**
	 * Sets a Parameter. If a Parameter with the same name already exists in the
	 * Settings, it is overwritten.
	 * 
	 * @param param
	 *            Parameter to set.
	 * @see Parameter#equals(Object)
	 */
	public void setParameter(Parameter param) {
		parameters.put(param.getName(),param);
	}

	/**
	 * Returns the value of a Parameter in the Settings.
	 * 
	 * @param name
	 *            Name of the Parameter.
	 * @return Value of the Parameter with the specified name, empty string (not
	 *         null) if parameter name unknown.
	 * @see Parameter#equals(Object)
	 */
	public String getValueOf(String name) {
		Parameter p = parameters.get(name);
		
		return p == null ? "" : p.getValue();
	}
	
	/**
	 * Gets the <code>Parameter</code> object specified by the name in this settings if
	 * present, otherwise the method returns <code>null</code>
	 * @param name Name of the Parameter.
	 * @return The parameter withe the specified name if present, otherwise <code>null</code>.
	 */
	public Parameter getParameter(String name)
	{
		return parameters.get(name);
	}

	/**
	 * Returns all Parameters in the Settings.
	 * 
	 * @return Array of all Parameters in the Settings.
	 */
	public Parameter[] getParameters() {
		return parameters.values().toArray(new Parameter[0]);
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

		/*
		for (int i = 0; i < mvParameters.size(); i++) {
			node.appendChild(mvParameters.elementAt(i).exportElement(doc));
		}
		*/
		
		for (Parameter p : parameters.values())
			node.appendChild(p.exportElement(doc));

		return node;
	}

	/**
	 * {@inheritDoc}
	 */
	public void importElement(Element e) {

		initialize();

		// get Parameters
		NodeList nodes = e.getElementsByTagName(Parameter.XML_TAG);

		Parameter param;
		for (int i = 0; i < nodes.getLength(); i++) {
			Node n = nodes.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				param = new Parameter();
				param.importElement((Element) n);
				// mvParameters.addElement(param);
				this.setParameter(param); // don't allow multiple entries with
				// same name
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("Settings");
		for (Object param : parameters.values())
			strBuilder.append('\n').append(param);

		return strBuilder.toString();
	}

}
