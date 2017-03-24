package de.ovgu.dke.clustering.model;

import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

/**
 * 
 * @author mhermkes
 *
 */
public abstract class AbstractClusterModel<T extends Cluster> implements ClusterModel<T> 
{
	protected ObjectSet dataset;
	
	protected DistanceMeasure metric;
	
	public AbstractClusterModel(ObjectSet dataset)
	{
		if(dataset == null)
			throw new NullPointerException();
		this.dataset = dataset;
	}

	public ObjectSet getDataset() 
	{
		return dataset;
	}
	
	public DistanceMeasure getDistanceMetric()
	{
		return metric;
	}
	
	public void setDistanceMetric(DistanceMeasure metric)
	{
		if(metric == null)
			throw new NullPointerException();
		this.metric = metric;
	}

	

}
