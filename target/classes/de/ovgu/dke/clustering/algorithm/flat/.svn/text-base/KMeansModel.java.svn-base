package de.ovgu.dke.clustering.algorithm.flat;

import java.util.Arrays;
import java.util.Collection;

import de.ovgu.dke.clustering.model.AbstractClusterModel;
import de.ovgu.dke.clustering.model.Cluster;
import de.ovgu.dke.clustering.model.FlatClusterModel;
import de.ovgu.dke.clustering.model.MutableCluster;
import de.ovgu.dke.clustering.model.MutablePrototypeCluster;
import de.ovgu.dke.clustering.model.SimplePrototypeCluster;
import de.ovgu.dke.clustering.util.PrototypeInitializer;
import de.ovgu.dke.clustering.util.PrototypeInitialzerType;
import de.ovgu.dke.util.DistanceMeasure;
import de.ovgu.dke.util.ObjectSet;

/**
 * 
 * @author mhermkes
 * TODO: - metric can also be a similarity measure
 * 		 - getError() must be more generic, so that this method based on the 
 *         used metric
 */
public class KMeansModel extends AbstractClusterModel<Cluster>
implements FlatClusterModel
{
	private MutablePrototypeCluster[] clusters;
	
	private DistanceMeasure metric;
	
	KMeansModel(ObjectSet dataset, double[][] centroids, DistanceMeasure metric) 
	{
		super(dataset);
		if(centroids == null)
			throw new NullPointerException();
		if(metric == null)
			throw new NullPointerException();
		initClusters(centroids);
		this.metric = metric;
	}
	
	KMeansModel(int k, ObjectSet dataset, PrototypeInitialzerType type, DistanceMeasure metric)
	{
		super(dataset);
		if(type == null)
			throw new NullPointerException();
		if(metric == null)
			throw new NullPointerException();
		if(k<0)
			throw new IllegalArgumentException("k must be greater than zero");
		
		PrototypeInitializer pInitializer = PrototypeInitialzerType.createPrototypeInitializer(type, dataset);
		initClusters(pInitializer.getNVectors(k));
		this.metric = metric;
	}

	public MutablePrototypeCluster getClusterAt(int idx) 
	{
		return clusters[idx];
	}

	public MutablePrototypeCluster getCluster(int id) 
	{
		MutablePrototypeCluster retVal = null;
		for(MutablePrototypeCluster c : clusters) 
		{
			if(c.getId() == id) {
				retVal = c;
				break;
			}
		}
		return retVal;
	}

	public MutablePrototypeCluster getClusterByData(int dataId) 
	{
		MutablePrototypeCluster retVal = null;
		for(MutablePrototypeCluster cluster : clusters)
		{
			if(cluster.belongsToCluster(dataId)) {
				retVal = cluster;
				break;
			}	
		}
		return retVal;
	}

	public int numberOfClusters() 
	{
		return clusters.length;
	}
	
	public double distance(int clusterIdx1, int clusterIdx2)
	{
		double[] centroid1 = getCentroid(clusterIdx1);
		double[] centroid2 = getCentroid(clusterIdx2);
		return metric.getDistance(centroid1, centroid2);
	}
	
	public double distance(int clusterIdx, double[] vector)
	{
		double[] centroid = getCentroid(clusterIdx);
		return metric.getDistance(centroid, vector);
	}

	public double[] getCentroid(int clusterIdx)
	{
		rangeCheck(clusterIdx);
		return clusters[clusterIdx].getCentroid();
	}
	
	public void updateCentroid(int clusterIdx)
	{
		rangeCheck(clusterIdx);
		clusters[clusterIdx].computeCentroid(dataset);
	}
	
	public double getError()
	{
		double error = 0d;
		double[] vector = null;
		for(MutablePrototypeCluster cluster : clusters)
		{
			for(Integer dataId : cluster.getData())
			{
				vector = dataset.get(dataId).getRepresentation();
				error += metric.getDistance(cluster.getCentroid(), vector);
			}
		}
		return error;
	}
	
	public void clearData()
	{
		for(MutablePrototypeCluster cluster : clusters)
			cluster.clearData();
	}
	
	
	public Collection<MutablePrototypeCluster> getClusters()
	{
		return Arrays.asList(clusters);
	}
	
	private final void initClusters(double[][] centroids)
	{
		int n = centroids.length;
		clusters = new MutablePrototypeCluster[n];
		MutablePrototypeCluster cluster = null;
		for(int i=0; i<n; i++)
		{
			cluster = new SimplePrototypeCluster(centroids[i]);
			clusters[i] = cluster;
		}
	}
	
	private final void rangeCheck(int index) 
	{
		if (index >= clusters.length)
		    throw new IndexOutOfBoundsException(
			"Index: "+index+", Size: "+clusters.length);
	}
}
