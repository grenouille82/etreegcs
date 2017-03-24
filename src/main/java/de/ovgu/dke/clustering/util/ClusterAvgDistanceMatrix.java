package de.ovgu.dke.clustering.util;

import java.util.NoSuchElementException;

import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

//TODO: make it more generic
public final class ClusterAvgDistanceMatrix 
{
	private AvgDistance intraDistanceMeasure;
	private CompleteLinkage interDistanceMeasure;
	
	private Cluster[] clusters;
	
	private double[][] matrix;
	
	private int size;
	
	public ClusterAvgDistanceMatrix(FlatClusterModel model)
	{
		initialize(model, model.getDistanceMetric());
		computeDistances();
	}
	
	public ClusterAvgDistanceMatrix(FlatClusterModel model, DistanceMeasure metric)
	{
		initialize(model, metric);
		computeDistances();
	}
	
	public double getDistance(Cluster a, Cluster b)
	{
		if(a == null)
			throw new NullPointerException(); 
		if(b == null)
			throw new NullPointerException();
		int idx1 = indexOf(a);
		int idx2 = indexOf(b);
		return matrix[idx1][idx2];
	}
	
	public double getDistance(int idx1, int idx2)
	{
		rangeCheck(idx1);
		rangeCheck(idx2);
		return matrix[idx1][idx2];
	}
	
	public double[] getDistances(Cluster cluster)
	{
		if(cluster == null)
			throw new NullPointerException();
		int index = indexOf(cluster);
		return matrix[index];
	}
	
	public double[] getDistances(int index)
	{
		rangeCheck(index);
		return matrix[index];
	}
	
	public int indexOf(Cluster cluster)
	{
		int idx = -1;
		for(int i=0, n=clusters.length; i<n; i++)
		{
			if(cluster.equals(clusters[i])) {
				idx = i;
				break;
			}
		}
		if(idx == -1)
			throw new NoSuchElementException();
		return idx;
	}
	
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i<size; i++)
		{
			builder.append('\t');
			builder.append(clusters[i]);
		}		
		for(int i=0; i<size; i++)
		{
			builder.append('\n');
			builder.append(clusters[i]);
			builder.append(':');
			for(int j=0; j<size; j++)
			{
				builder.append('\t');
				builder.append(matrix[i][j]);
			}
		}
		
		return builder.toString();
	}
	
	private final void rangeCheck(int index)
	{
		if (index >= size)
		    throw new IndexOutOfBoundsException(
			"Index: "+index+", Size: "+size);
	}
	
	private final void initialize(FlatClusterModel model, DistanceMeasure metric)
	{
		ObjectSet dataset 		= model.getDataset();
		
		size 	 = model.numberOfClusters();
		matrix 	 = new double[size][size];
		clusters = new Cluster[size];
		clusters = model.getClusters().toArray(clusters);
		
		intraDistanceMeasure = new AvgDistance(dataset, metric);
		interDistanceMeasure = new CompleteLinkage(dataset, metric);
	}
	
	private final void computeDistances()
	{
		for(int i=0; i<size; i++)
		{
			matrix[i][i] = intraDistanceMeasure.distance(clusters[i]);
			for(int j=i+1; j<size; j++)
			{
				matrix[i][j] = matrix[j][i] = 
					interDistanceMeasure.distance(clusters[i], clusters[j]);
			}
		}
	}
	
}
