package de.ovgu.dke.clustering.util;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public abstract class AbstractInterClusterDistance<T extends Cluster>
implements InterClusterDistance<T>
{
	protected ObjectSet dataset; 
	protected DistanceMeasure metric;

	public AbstractInterClusterDistance() {}
	
	public AbstractInterClusterDistance(ObjectSet dataset, DistanceMeasure metric)
	{
		this.dataset = dataset;
		this.metric	 = metric;
	}
	
	public void define(ObjectSet dataset) 
	{
		if(dataset == null)
			throw new NullPointerException();
		this.dataset = dataset;
	}

	public void define(DistanceMeasure metric) 
	{
		if(metric == null)
			throw new NullPointerException();
		this.metric = metric;
	}
}
