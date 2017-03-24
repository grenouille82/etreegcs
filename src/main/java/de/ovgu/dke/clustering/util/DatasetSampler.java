package de.ovgu.dke.clustering.util;

import de.ovgu.dke.util.ObjectSet;
import de.ovgu.dke.util.ObjectWrapper;
import de.ovgu.dke.util.Parametrizable;

public interface DatasetSampler extends Parametrizable
{
	public ObjectSet getDataset();
	
	public boolean hasNext();
	
	public ObjectWrapper next();
	
	public int epoch();
}
