package de.ovgu.dke.aucoma.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Observable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;

//import de.ovgu.dke.aucoma.descriptors.MasterDescriptor;
//import de.ovgu.dke.aucoma.descriptors.editorial.*;


public class XMLSerializer {
	
	protected static Log svLog = LogFactory.getLog(XMLSerializer.class);
	
	protected static final XStream xstream = new XStream();
	
	static {
//		xstream.alias("SongDescriptor", MasterDescriptor.class);
//		xstream.useAttributeFor(MasterDescriptor.class, "title");
//		xstream.useAttributeFor(MasterDescriptor.class, "year");
//		xstream.useAttributeFor(MasterDescriptor.class, "luceneID");
//		
//		xstream.alias("Person", Person.class);
//		xstream.useAttributeFor(Person.class, "firstNames");
//		xstream.useAttributeFor(Person.class, "lastName");
//		
//		xstream.alias("Instrument", Instrument.class);
//		
//		xstream.alias("RecordingLocation", RecordingLocation.class);
//		xstream.useAttributeFor(RecordingLocation.class, "name");
//		
//		xstream.alias("Release", Release.class);
//		xstream.useAttributeFor(Release.class, "title");
//		xstream.useAttributeFor(Release.class, "type");
//		xstream.useAttributeFor(Release.class, "date");
//		xstream.useAttributeFor(Release.class, "country");
//		xstream.useAttributeFor(Release.class, "id");
//		
//		xstream.alias("BeatlesMetadata", BeatlesMetadataDescriptor.class);
		
		// this causes much trouble otherwise
		xstream.omitField(Observable.class, "obs");
		xstream.omitField(Observable.class, "changed");
	}
	
	public static String toXMLString(Object o) {
		return xstream.toXML(o);
	}
	
	public static Object readFile(String fileName) {
		try {
			return xstream.fromXML(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			svLog.error(e.getMessage(), e);
			return null;
		}		
	}
	
	public static boolean writeFile(String fileName, Object toWrite) {
		try {
			xstream.toXML(toWrite, new FileOutputStream(fileName));
			return true;
		} catch (FileNotFoundException e) {
			svLog.error(e.getMessage(), e);
			return false;
		}		
	}
	
	public static final XStream getXMLSerializer() {
		return xstream;
	}

}
