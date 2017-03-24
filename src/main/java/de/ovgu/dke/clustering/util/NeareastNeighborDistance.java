package de.ovgu.dke.clustering.util;

import java.util.Collection;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public class NeareastNeighborDistance extends AbstractIntraClusterDistance<Cluster>
implements IntraClusterDistance<Cluster>
{
	public NeareastNeighborDistance() 
	{
		super();
	}

	public NeareastNeighborDistance(ObjectSet dataset, DistanceMeasure metric) 
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
		double minDist;
		for(int i : data)
		{
			minDist = Double.POSITIVE_INFINITY;
			x = dataset.get(i).getRepresentation();
			for(int j : data)
			{
				if(i!=j) {
					y = dataset.get(j).getRepresentation();
					minDist = Math.min(metric.getDistance(x, y), minDist);
				}
			}
			d += minDist;
		}
		
		return d/(double)data.size();
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
		double minDist;
		for(int i : data)
		{
			minDist = Double.POSITIVE_INFINITY;
			for(int j : data)
			{
				if(i!=j)
					minDist = Math.min(matrix.getValue(i, j), minDist);
			}
			d += minDist;
		}
		
		return d/(double)data.size();
	}
}
