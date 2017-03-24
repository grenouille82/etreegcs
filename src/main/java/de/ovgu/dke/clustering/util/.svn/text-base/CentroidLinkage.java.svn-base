package de.ovgu.dke.clustering.util;


import de.ovgu.dke.clustering.model.PrototypeCluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public class CentroidLinkage extends AbstractInterClusterDistance<PrototypeCluster>
implements InterClusterDistance<PrototypeCluster>
{
	
	public CentroidLinkage() 
	{
		super();
	}

	public CentroidLinkage(ObjectSet dataset, DistanceMeasure metric) 
	{
		super(dataset, metric);
	}

	public double distance(PrototypeCluster a, PrototypeCluster b) 
	{
		if(a == null)
			throw new NullPointerException();
		if(b == null)
			throw new NullPointerException();
		if(dataset == null)
			throw new IllegalStateException("dataset is not defined");
		if(metric == null)
			throw new IllegalStateException("metric is not defined");
		
		double[] centroidA = a.getCentroid();
		double[] centroidB = b.getCentroid();
		return metric.getDistance(centroidA, centroidB);
	}

	public double distance(PrototypeCluster a, PrototypeCluster b, ProximityMatrix matrix) 
	{
		return distance(a, b);
	}

}
