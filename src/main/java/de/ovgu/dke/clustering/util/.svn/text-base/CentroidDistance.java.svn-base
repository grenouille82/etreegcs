package de.ovgu.dke.clustering.util;

import java.util.Collection;

import de.ovgu.dke.clustering.model.PrototypeCluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public class CentroidDistance extends AbstractIntraClusterDistance<PrototypeCluster>
implements IntraClusterDistance<PrototypeCluster>
{
	public CentroidDistance() {}
	
	public CentroidDistance(ObjectSet dataset, DistanceMeasure metric)
	{
		super(dataset, metric);
	}
	
	public double distance(PrototypeCluster cluster) 
	{
		if(cluster == null)
			throw new NullPointerException();
		if(dataset == null)
			throw new IllegalStateException("dataset is not defined");
		if(metric == null)
			throw new IllegalStateException("metric is not defined");
		
		double d = 0d;
		
		Collection<Integer> data = cluster.getData();
		double[] centroid		 = cluster.getCentroid();
		double[] x;
		for(int i : data)
		{
			x = dataset.get(i).getRepresentation();
			d += metric.getDistance(centroid, x);
		}
		
		return d/(double)data.size();
	}

	public double distance(PrototypeCluster cluster, ProximityMatrix matrix) 
	{
		return distance(cluster);
	}

}
