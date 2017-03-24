package de.ovgu.dke.clustering.msctest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.ObjectWrapper;
import de.ovgu.dke.util.SimpleObjectSet;
import de.ovgu.dke.util.SimpleObjectWrapper;

public class SampleDataset {

	public static void main(String args[])
	{
		ObjectSet dataset = createDataset();
		try {
			dataset.writeToFile("/home/grenouille/irg_workspace/datasets/uniform_4cluster.csv");
			
		} catch(IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static ObjectSet createDataset()
	{
		Random rnd = new Random(1);
		ObjectWrapper[] wrapper = new ObjectWrapper[800];
		for(int i=0; i<200; i++)
		{
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*3d+5d), rnd.nextDouble()*3d+5d});
			wrapper[i].setClassLabel("cluster1");
			wrapper[i].setLabel("None");
		}
		for(int i=200; i<400; i++)
		{
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*3d+5d), rnd.nextDouble()*3d+10d} );
			wrapper[i].setClassLabel("cluster2");
			wrapper[i].setLabel("None");
		}
		for(int i=400; i<600; i++)
		{
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*3d+5d), rnd.nextDouble()*3d+15d});
			wrapper[i].setClassLabel("cluster3");
			wrapper[i].setLabel("None");
		}
		for(int i=600; i<800; i++)
		{
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*3d+5d), rnd.nextDouble()*3d+20d});
			wrapper[i].setClassLabel("cluster4");
			wrapper[i].setLabel("None");
		}
		/*
		for(int i=800; i<1000; i++)
		{
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*3d+5d), rnd.nextDouble()*3d+40d});
			wrapper[i].setClassLabel("cluster5");
			wrapper[i].setLabel("None");
		}*/
		
		/*
		for(int i=450; i<500; i++)
			wrapper[i] = new SimpleObjectWrapper(null, new double[] {(rnd.nextDouble()*20d+200d), rnd.nextDouble()*20d+200d});

*/
		//TODO: set Attributes
		return new SimpleObjectSet(wrapper, null);
	}
}
