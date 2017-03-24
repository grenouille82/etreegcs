package de.ovgu.dke.clustering.util;

import java.util.Collection;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public final class AvgDistance extends AbstractIntraClusterDistance<Cluster> 
implements IntraClusterDistance<Cluster>
{
	public AvgDistance() {}
	
	public AvgDistance(ObjectSet dataset, DistanceMeasure metric)
	{
		super(dataset, metric);
	}

	public double distance(Cluster cluster) 
	{
		if(cluster == null)
			throw new NullPointerException();
		if(dataset == null)
			throw new IllegalStateException("dataset is not defined");
		if(metric == null)
			throw new IllegalStateException("metric is not defined");
		
		double d = 0d;
		
		Collection<Integer> data = cluster.getData();
		double[] x,y;
		for(int i : data)
		{
			x = dataset.get(i).getRepresentation();
			for(int j : data)
			{
				y = dataset.get(j).getRepresentation();
				d += metric.getDistance(x, y);
			}
		}
		
		int n = data.size();
		return d/(double)(n*(n-1));
	}

	public double distance(Cluster cluster, ProximityMatrix matrix) 
	{
		if(cluster == null)
			throw new NullPointerException();
		if(matrix == null)
			throw new NullPointerException();
		if(dataset == null)
			throw new IllegalStateException("dataset is not defined");
		
		double d = 0d;
		
		Collection<Integer> data = cluster.getData();
		for(int i : data)
		{
			for(int j : data)
				d += matrix.getValue(i, j);
		}
		
		int n = data.size();
		return d/(double)(n*(n-1));
	}

}
