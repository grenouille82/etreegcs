package de.ovgu.dke.clustering.util;

import java.util.Collection;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

public class CompleteLinkage extends AbstractInterClusterDistance<Cluster>
implements InterClusterDistance<Cluster>
{
	
	
	public CompleteLinkage() 
	{
		super();
	}

	public CompleteLinkage(ObjectSet dataset, DistanceMeasure metric) 
	{
		super(dataset, metric);
	}

	public double distance(Cluster a, Cluster b) 
	{
		if(a == null)
			throw new NullPointerException();
		if(b == null)
			throw new NullPointerException();
		if(dataset == null)
			throw new IllegalStateException("dataset is not defined");
		if(metric == null)
			throw new IllegalStateException("metric is not defined");
		
		double d = 0d;
		
		Collection<Integer> dataA = a.getData();
		Collection<Integer> dataB = b.getData();
		double[] x,y;
		for(int i : dataA)
		{
			x = dataset.get(i).getRepresentation();
			for(int j : dataB)
			{
				y = dataset.get(j).getRepresentation();
				d += metric.getDistance(x, y);
			}
		}
		System.out.println(a.size() + "\t" + b.size());
		return d/(double)(a.size()*b.size());
	}

	public double distance(Cluster a, Cluster b, ProximityMatrix matrix) 
	{
		if(a == null)
			throw new NullPointerException();
		if(b == null)
			throw new NullPointerException();
		if(matrix == null)
			throw new NullPointerException();
		if(dataset == null)
			throw new IllegalStateException("dataset is not defined");
		
		double d = 0d;
		
		Collection<Integer> dataA = a.getData();
		Collection<Integer> dataB = b.getData();
		for(int i : dataA)
		{
			for(int j : dataB)
				d += matrix.getValue(i, j);
		}
		
		return d/(double)(a.size()*b.size());
	}

}
