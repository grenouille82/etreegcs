package de.ovgu.dke.util;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import com.csvreader.CsvReader;


public class CSVObjectSet implements ObjectSet
{
	private boolean labelled;
	private boolean classified;
	
	private ObjectWrapper[] objects;
	  
	public CSVObjectSet(String pathname)
	throws IOException
	{
		objects = loadData(pathname, null);
	}
	
	public CSVObjectSet(String pathname, String classHeader)
	throws IOException
	{
		objects = loadData(pathname, classHeader);
		classified = true;
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

	/* (non-Javadoc)
	 * @see de.ovgu.dke.som.model.ObjectSetInterfact#dataClassified()
	 */
	public boolean dataClassified() {
		return classified;
	}
	
	public boolean dataLabelled()
	{
		return labelled;
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
			writer.write(objects[i].getClassLabel());
			writer.write(',');
			writer.write(objects[i].getLabel());
			writer.write('\n');
		}
		writer.flush();
		writer.close();
	}

	private ObjectWrapper[] loadData(String pathname, String classHeader)
	throws FileNotFoundException, IOException
	{
		LinkedList<ObjectWrapper> data = new LinkedList<ObjectWrapper>();
		
		CsvReader reader = new CsvReader(pathname);
		reader.readHeaders();
	
		while(reader.readRecord())
		{
			String values[] = reader.getValues();
			int dim = values.length;
			if(classHeader!=null)
				dim--;
			double[] v = new double[dim];
			for(int i=0; i<dim; i++)
				v[i] = Double.parseDouble(values[i]);
			SimpleObjectWrapper wrapper = new SimpleObjectWrapper(v);
			if(classHeader!=null) {
				String classLabel = reader.get(classHeader);
				wrapper.setClassLabel(classLabel);
			}
			data.add(wrapper);
		}	
		
		reader.close();
		return data.toArray(new ObjectWrapper[0]);
	}
	
}
