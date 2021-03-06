package de.ovgu.dke.clustering.msctest;

import java.util.Collection;
import java.util.LinkedList;

import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.ObjectWrapper;
import de.ovgu.dke.util.SimpleObjectSet;

public class MCSUtil 
{
	public static ObjectSet removeVectorsByClassLabels(ObjectSet dataset, Collection<String> ignoredClasses)
	{
		LinkedList<ObjectWrapper> vectors = new LinkedList<ObjectWrapper>();
		for(int i=0; i<dataset.size(); i++)
		{
			ObjectWrapper wrapper = dataset.get(i);
			if(!ignoredClasses.contains(wrapper.getClassLabel())) {
				vectors.add(wrapper);
			}
		}
		//TODO: set Attributes
		SimpleObjectSet retVal = new SimpleObjectSet(vectors.toArray(new ObjectWrapper[0]), null);
		return retVal;
	}
}
