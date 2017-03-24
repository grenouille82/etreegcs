package de.ovgu.dke.util;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public interface ObjectSet {

	public abstract int size();

	/** return array of objects */
	public abstract ObjectWrapper[] getObjects();

	public abstract int getObjectSize();

	public abstract ObjectWrapper get(int index);

	public abstract boolean dataClassified();
	
	public void writeToFile(String pathname)
	throws IOException, FileNotFoundException;

}