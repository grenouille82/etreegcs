/*
 * Created on 13.10.2004
 */
package de.ovgu.dke.util;

import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;
import org.xml.sax.InputSource;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Provides functions for serialization to and deserialization from (DOM) Elements, XML strings or files.
 * Functions to be implemented are exportElement(Document doc, String xmlTag) and importElement(Element e).
 * 
 * @author Sebastian Stober
 */
public abstract class XMLSupportingClass implements XMLSupportingClassIf{

  /**
   * XML-tag to be used for (de)serialization. 
   * Has to be overridden by any inheriting class and has to be unique.
   */
  public static final String XML_TAG = "Node";
  
  /**
   * Converts the object's content to an (DOM) Element decription using the XML_TAG of the object's class.
   * This description may then be serialized to XML.
   * 
   * @param 	doc		  related document (from calling function)
   * @return  Element element description of the object for export to XML
   */
  public Element exportElement(Document doc) {
    return exportElement(doc, null);
  }
  
  /**
   * Converts the object's content to an (DOM) Element decription using the specified XML-tag.
   * This description may then be serialized to XML.
   * 
   * @param 	doc		  related document (from calling function)
   * @param 	xmlTag	XML-tag for this object (if null, the XML_TAG of the object's class os used)
   * @return  Element element description of the object for export to XML
   */
  public abstract Element exportElement(Document doc, String xmlTag);

  /**
   * Serializes the object to an XML String.
   * 
   * @return XML decription of the object
   * @see #exportElement(Document, String)
   */
  public String toXMLString() {
    
    try {
      // create document
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      doc.appendChild(exportElement(doc));
           
      // Setup format settings
      OutputFormat outFormat = new OutputFormat();
      outFormat.setEncoding("UTF-8");
      outFormat.setVersion("1.0");
      outFormat.setIndenting(false);
      //outFormat.setIndent(4);
      
      // Apply the format settings
      XMLSerializer docSerializer = new XMLSerializer();
      docSerializer.setOutputFormat(outFormat);
      
      // Define a Writer
      StringWriter strWriter = new StringWriter();
      docSerializer.setOutputCharStream(strWriter);      

      // Serialize XML Document      
      docSerializer.serialize(doc);
      strWriter.close();
      
      return strWriter.toString();      

    } catch (Exception e) {
      System.out.println("Error " + e);
      e.printStackTrace();
    }
    return null;
  }
  
  /**
   * Deserializes the object from an Element (DOM) desrciption. 
   * The values of all member variables are overwritten by those specified in the Element desrciption.   *  
   * 
   * @param e Element Element to be imported
   */
  public abstract void importElement(Element e);
  
  /**
   * Deserializes the object from a XML-representation.
   * 
   * @param XMLString XML representation
   * @see #importElement(Element)
   */
  public void importFromXMLString (String XMLString) {    
    //initialize();    
    try {
      
      // Create a builder factory
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      
      // convert XMLString into a format that can be parsed
      char[] aryXML = new char[XMLString.length()];
      XMLString.getChars(0, XMLString.length(), aryXML, 0);      
      InputSource in = new InputSource(new CharArrayReader(aryXML));
      
      // Create the builder and parse
      Document doc = factory.newDocumentBuilder().parse(in);
      
      
      importElement(doc.getDocumentElement());      

    } catch (Exception e) {
      System.err.println("importFromXMLString(): error parsing XML string.");
      e.printStackTrace();
    }     
  }
  
  /**
   * Deserializes the object from a file containing the XML-representation.
   * 
   * @param fileName name (and path) of the file containing the XML description of the object
   * @see #importElement(Element)
   */
  public void importFromFile(String fileName) {
    try {
	    //  Create a builder factory
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    //  Create the builder and parse
	    Document doc = factory.newDocumentBuilder().parse(new File(fileName));
	    importElement(doc.getDocumentElement());   
    } catch (Exception e) {
      System.err.println("importFromXMLString(): error parsing XML string.");
      e.printStackTrace();
    }    
  }
  
  /**
   * Serializes the object to a XML String that is written to a file.
   * If a file with the specified name already exists, it is overwritten.
   * 
   * @param fileName name (and path) of the file to write to
   * @see #importElement(Element)
   */
  public void writeToFile(String fileName) {
    try {
      // create document
      Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      doc.appendChild(exportElement(doc));
           
      // Setup format settings
      OutputFormat outFormat = new OutputFormat();
      outFormat.setEncoding("UTF-8");
      outFormat.setVersion("1.0");
      outFormat.setIndenting(false);
      //outFormat.setIndent(4);
      
      // Apply the format settings
      XMLSerializer docSerializer = new XMLSerializer();
      docSerializer.setOutputFormat(outFormat);
      
      // Define a Writer
      FileOutputStream output = new FileOutputStream(fileName);
      Writer outWriter = new OutputStreamWriter(output, "UTF-8");
      
      docSerializer.setOutputCharStream(outWriter);      

      // Serialize XML Document      
      docSerializer.serialize(doc);
      outWriter.flush();
      outWriter.close();      

    } catch (Exception e) {
      System.out.println("Error " + e);
      e.printStackTrace();
    }
    
  }
  
}
