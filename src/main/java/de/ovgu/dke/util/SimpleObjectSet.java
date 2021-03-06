package de.ovgu.dke.util;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author  Andreas Nuernberger
 * @version
 *
 * 28.03.02 aljoscha added Class Obj, ObjSet is now an array of Obj's
 *
 * This class assumes identical structures of the objects stored!
 */
public class SimpleObjectSet implements ObjectSet {
		
    boolean classified = true;
    boolean labelled = true;

    protected Attribute[] attributes;
    
    /** the objects */
    protected ObjectWrapper[] objects;
    
    /** Creates new ObjSet */
    public SimpleObjectSet(ObjectWrapper[] objects, Attribute[] attributes) {
    	this.objects = objects;
    	this.attributes = attributes;
    }
    
    /* (non-Javadoc)
	 * @see de.ovgu.dke.som.model.ObjectSetInterfact#size()
	 */
    public int size() {
        return objects.length;
    }
    
    /* (non-Javadoc)
	 * @see de.ovgu.dke.som.model.ObjectSetInterfact#getObjects()
	 */
    public ObjectWrapper[] getObjects() {
        return objects;
    }
    
    public Attribute[] getAttributes() {
    	return attributes;
    }

	/* (non-Javadoc)
	 * @see de.ovgu.dke.som.model.ObjectSetInterfact#getObjectSize()
	 */
	public int getObjectSize() {
		if ((objects != null) && (objects.length > 0))
			return objects[0].representation.length;
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see de.ovgu.dke.som.model.ObjectSetInterfact#get(int)
	 */
	public ObjectWrapper get(int index) {
		return objects[index];
	}
	
	public Attribute getAttribute(int index) {
		return attributes[index];
	}

	/* (non-Javadoc)
	 * @see de.ovgu.dke.som.model.ObjectSetInterfact#dataClassified()
	 */
	public boolean dataClassified() {
		return classified;
	}
	
	public void writeToFile(String pathname)
	throws IOException, FileNotFoundException
	{
		FileWriter writer = new FileWriter(pathname);
		int dim = getObjectSize();
		for(int i=0; i<dim; i++)
			writer.write("dim"+i+",");
		writer.write("class,label\n");
		for(int i=0, n=objects.length; i<n; i++)
		{
			double[] v = objects[i].getRepresentation();
			for(int j=0; j<dim; j++)
			{
				writer.write(Double.toString(v[j]));
				writer.write(',');
			}
			/*writer.write(objects[i].getClassLabel());
			
			writer.write(',');
			writer.write(objects[i].getLabel());*/
			writer.write('\n');
		}
		writer.flush();
		writer.close();
	}
}
